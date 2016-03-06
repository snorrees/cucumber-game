package com.cucumber.workshop.logic.collision;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.badlogic.gdx.math.Circle;
import com.cucumber.workshop.logic.basic.Damage;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.basic.Player;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import net.mostlyoriginal.api.system.core.DualEntityProcessingSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class DamageCollisionSystem extends DualEntityProcessingSystem {

    private final Circle damageCircle = new Circle();
    private final Circle enemyCircle = new Circle();

    private EntitySubscription players;
    private Entity player;

    private boolean damageInflicted;

    private ComponentMapper<Position> positions;
    private ComponentMapper<Radius> radii;
    private ComponentMapper<HitPoints> hitpoints;
    private ComponentMapper<Damage> damages;
    private ComponentMapper<Enemy> enemies;

    public DamageCollisionSystem() {
        super(Aspect.all(Position.class,
                         Radius.class,
                         Damage.class),
              Aspect.all(Position.class,
                         Radius.class,
                         HitPoints.class,
                         Enemy.class));
    }

    @Override
    protected void initialize() {
        players = world.getAspectSubscriptionManager().get(Aspect.all(Player.class));
    }

    @Override
    protected void begin() {
        if (players.getEntities().size() > 0) {
            player = world.getEntity(players.getEntities().get(0));
        }
    }

    @Override
    protected void process(int damageId, int enemyId) {
        Damage damage = damages.get(damageId);
        Enemy enemy = enemies.get(enemyId);

        if (damage.type == enemy.type) {
            Position actionPos = positions.get(damageId);
            Radius actionRadius = radii.get(damageId);
            damageCircle.set(actionPos.x, actionPos.y, actionRadius.r);

            Position enemyPos = positions.get(enemyId);
            Radius enemyRadius = radii.get(enemyId);
            enemyCircle.set(enemyPos.x, enemyPos.y, enemyRadius.r);

            if (damageCircle.overlaps(enemyCircle)){
                HitPoints hp = hitpoints.get(enemyId);
                hp.damage(damage.amount);
                damageInflicted = true;
            }
        }
    }

    @Override
    protected void end() {
        if (damageInflicted) {
            world.createEntity().edit().create(EnemyChange.class);
        }
        player = null;
        damageInflicted = false;
    }
}
