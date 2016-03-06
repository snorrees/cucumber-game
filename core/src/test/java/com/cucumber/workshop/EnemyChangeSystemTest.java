package com.cucumber.workshop;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.utils.EntityBuilder;
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
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Snorre E. Brekke - Computas
 */
public class EnemyChangeSystemTest {

    World world;

    @Before
    public void setUp() throws Exception {
        world = new World(
                new WorldConfiguration()
                        .setSystem(EnemyChangeSystem.class)
                        .setSystem(EntityFactorySystem.class)
        );

        world.createEntity().edit().create(InteractionType.class);
        world.process();
    }

    @Test
    public void should_change_type_of_enemy_when_an_enemychange_event_is_created() throws Exception {
        final Entity enemy = createEnemy(Type.GREEN);
        createEnemyChangeEvent();
        world.process();
        assertThat(enemy.getComponent(Enemy.class).type).isEqualTo(Type.BLUE);
    }

    @Test
    public void should_remove_update_texture_refs_when_type_is_changed() throws Exception {
        final Entity enemy = createEnemy(Type.GREEN);
        createEnemyChangeEvent();
        world.process();
        assertThat(enemy.getComponent(TextureRef.class).id).isEqualTo(Type.BLUE.image + ".png");
        assertThat(enemy.getComponent(NormalRef.class).id).isEqualTo(Type.BLUE.image + "_n.png");
    }

    @Test
    public void should_remove_textures_when_type_is_changed() throws Exception {
        final Entity enemy = createEnemy(Type.GREEN);
        createEnemyChangeEvent();
        world.process();
        assertThat(enemy.getComponent(TextureComponent.class)).isNull();
        assertThat(enemy.getComponent(NormalTextureComponent.class)).isNull();
    }

    @Test
    public void should_be_possible_to_re_add_texturecomponent_via_listener() throws Exception {
        final Entity enemy = createEnemy(Type.GREEN);
        createEnemyChangeEvent();

        world.getAspectSubscriptionManager().get(Aspect.all(TextureRef.class).exclude(TextureComponent.class))
                .addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) {
                        final int[] data = entities.getData();
                        for (int i = 0; i < entities.size(); i++) {
                            world.getEntity(data[i]).edit().create(TextureComponent.class);
                        }
                    }

                    @Override
                    public void removed(IntBag entities) {
                    }
                });

        world.process();
        assertThat(enemy.getComponent(TextureComponent.class)).isNotNull();
    }

    private void createEnemyChangeEvent() {
        new EntityBuilder(world).with(EnemyChange.class).build();
    }

    private Entity createEnemy(Type type) {
        Entity enemy1 = new EntityBuilder(world)
                .with(
                        Enemy.class,
                        Tint.class,
                        TextureComponent.class,
                        NormalTextureComponent.class,
                        TextureRef.class,
                        NormalRef.class
                )
                .build();

        final Enemy enemyType = enemy1.getComponent(Enemy.class);
        enemyType.type(type);
        enemy1.getComponent(TextureRef.class).id(type.image);
        return enemy1;
    }
}