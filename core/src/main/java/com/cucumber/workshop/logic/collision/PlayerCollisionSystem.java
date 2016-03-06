package com.cucumber.workshop.logic.collision;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Circle;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.basic.Player;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import net.mostlyoriginal.api.system.core.DualEntityProcessingSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class PlayerCollisionSystem extends DualEntityProcessingSystem {

    private final Circle enemyCircle = new Circle();
    private final Circle playerCircle = new Circle();

    private ComponentMapper<Position> positions;
    private ComponentMapper<Radius> radiuses;
    private ComponentMapper<HitPoints> hitpoints;

    public PlayerCollisionSystem() {
        super(Aspect.all(Position.class,
                         Radius.class,
                         HitPoints.class,
                         Player.class),
              Aspect.all(Position.class,
                         Radius.class,
                         Enemy.class));
    }

    @Override
    protected void process(int playerId, int enemyId) {
        Position playerPos = positions.get(playerId);
        Radius playerRadius = radiuses.get(playerId);
        playerCircle.set(playerPos.x, playerPos.y, playerRadius.r);

        Position enemyPos = positions.get(enemyId);
        Radius enemyRadius = radiuses.get(enemyId);
        enemyCircle.set(enemyPos.x, enemyPos.y, enemyRadius.r);

        if (enemyCircle.overlaps(playerCircle)){
            HitPoints playerHp = hitpoints.get(playerId);
            playerHp.damage(1);
            hitpoints.get(enemyId).hp(0);
        }
    }
}
