package com.cucumber.workshop.logic.collision;

import static org.assertj.core.api.Assertions.assertThat;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.utils.EntityBuilder;
import com.cucumber.workshop.logic.basic.Damage;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.enemy.Enemy;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Radius;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class InteractionCollisionSystemTest {

    World world;
    Entity interaction;
    Entity target;

    @Before
    public void setUp() throws Exception {
        world = new World(
                new WorldConfiguration()
                        .setSystem(DamageCollisionSystem.class)
        );

        target = new EntityBuilder(world)
                .with(Position.class,
                        Radius.class,
                        HitPoints.class)
                .build();

        interaction = new EntityBuilder(world)
                .with(Position.class,
                        Radius.class,
                        Damage.class)
                .build();
    }

    @Test
    public void should_remove_one_hp_from_enemy_when_interaction_within_circle() throws Exception {
        target.edit().create(Enemy.class);
        target.getComponent(Position.class).x(0).y(0);
        target.getComponent(Radius.class).r(1);
        HitPoints hp = target.getComponent(HitPoints.class).hp(1);

        interaction.getComponent(Position.class).x(0).y(0);
        interaction.getComponent(Radius.class).r(0.1f);

        world.process();

        assertThat(hp.isAlive()).isFalse();
    }

    @Test
    public void should_NOT_remove_one_hp_when_interaction_within_circle_of_non_enemy() throws Exception {
        target.getComponent(Position.class).x(0).y(0);
        target.getComponent(Radius.class).r(1);
        HitPoints hp = target.getComponent(HitPoints.class).hp(1);

        interaction.getComponent(Position.class).x(0).y(0);
        interaction.getComponent(Radius.class).r(0.1f);

        world.process();

        assertThat(hp.isAlive()).isTrue();
    }

    @Test
    public void should_NOT_remove_one_hp_from_enemy_when_interaction_outside_circle() throws Exception {
        target.getComponent(Position.class).x(0).y(0);
        target.getComponent(Radius.class).r(1);
        HitPoints hp = target.getComponent(HitPoints.class).hp(1);

        interaction.getComponent(Position.class).x(1.1f).y(1);
        interaction.getComponent(Radius.class).r(0.1f);

        world.process();

        assertThat(hp.isAlive()).isTrue();
    }
}