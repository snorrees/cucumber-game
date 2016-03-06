package com.cucumber.workshop;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.systems.IteratingSystem;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.enemy.InteractionType;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.render.basic.Tint;
import com.cucumber.workshop.render.texture.NormalTextureComponent;
import com.cucumber.workshop.render.texture.TextureComponent;

/**
 * @author Snorre E. Brekke - Computas
 */
public class EnemyTypeTintSystem extends IteratingSystem {

    private ComponentMapper<Enemy> enemyTypes;
    private ComponentMapper<TextureComponent> textures;
    private ComponentMapper<NormalTextureComponent> normals;
    private ComponentMapper<Tint> tints;
    private ComponentMapper<InteractionType> active;

    private EntityFactorySystem entityFactory;
    private EntitySubscription enemies;
    private EntitySubscription activeTypes;

    private Type playerInteractionType;

    public EnemyTypeTintSystem() {
        super(Aspect.all(Enemy.class, Tint.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initialize() {
        activeTypes = world.getAspectSubscriptionManager().get( Aspect.all(InteractionType.class));
    }

    @Override
    protected boolean checkProcessing() {
        return activeTypes.getEntities().size() > 0;
    }

    @Override
    protected void begin() {
        InteractionType interactionType = active.get(activeTypes.getEntities().get(0));
        playerInteractionType = interactionType.type;
    }

    @Override
    protected void process(int entityId) {
        final Enemy enemy = enemyTypes.get(entityId);
        Tint tint = tints.get(entityId);
        if (playerInteractionType == enemy.type) {
            tint.color(1, 1, 1);
        } else {
            tint.color(0f, 0.0f, 0.0f);
        }
    }
}
