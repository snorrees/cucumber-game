package com.cucumber.workshop;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.systems.IteratingSystem;
import com.artemis.utils.IntBag;
import com.cucumber.workshop.logic.collision.EnemyChange;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.enemy.InteractionType;
import com.cucumber.workshop.logic.enemy.Type;
import com.cucumber.workshop.render.basic.Tint;
import com.cucumber.workshop.render.texture.NormalRef;
import com.cucumber.workshop.render.texture.NormalTextureComponent;
import com.cucumber.workshop.render.texture.TextureComponent;
import com.cucumber.workshop.render.texture.TextureRef;

/**
 * @author Snorre E. Brekke - Computas
 */
public class EnemyChangeSystem extends IteratingSystem {

    private ComponentMapper<Enemy> enemyTypes;
    private ComponentMapper<TextureComponent> textures;
    private ComponentMapper<NormalTextureComponent> normals;
    private ComponentMapper<InteractionType> active;

    private EntityFactorySystem entityFactory;
    private EntitySubscription enemies;
    private EntitySubscription activeTypes;

    public EnemyChangeSystem() {
        super(Aspect.all(EnemyChange.class));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initialize() {
        enemies = world.getAspectSubscriptionManager().get(
                Aspect.all(
                        Enemy.class,
                        Tint.class,
                        TextureRef.class,
                        TextureComponent.class,
                        NormalRef.class,
                        NormalTextureComponent.class
                )
        );
        activeTypes = world.getAspectSubscriptionManager().get( Aspect.all(InteractionType.class));
    }

    @Override
    protected boolean checkProcessing() {
        return getSubscription().getEntities().size() > 0 &&activeTypes.getEntities().size() > 0;
    }

    @Override
    protected void begin() {
        InteractionType interactionType = active.get(activeTypes.getEntities().get(0));
        Type type = interactionType.type;
        interactionType.type = type.previousType();
    }

    @Override
    protected void process(int entityId) {
        IntBag entities = enemies.getEntities();
        final int[] data = entities.getData();
        for (int i = 0; i < entities.size(); i++) {
            Entity e = world.getEntity(data[i]);

            final Enemy enemy = enemyTypes.get(e);
            final Type currentType = enemy.type;
            enemy.type = currentType.nextType();

            textures.remove(e);
            normals.remove(e);

            entityFactory.setTextureRefs(e, enemy.type.image);
        }

        world.delete(entityId);
    }
}
