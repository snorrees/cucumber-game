package com.cucumber.workshop.logic.operation;

import static org.assertj.core.api.Assertions.assertThat;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.utils.EntityBuilder;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.spatial.Radius;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class HpControlledRadiusSystemTest {

    World world;
    Entity entity;

    @Before
    public void setUp() throws Exception {
        world = new World(
                new WorldConfiguration()
                        .setSystem(HpControlledRadiusSystem.class)
                        .setSystem(OperationsSystem.class)
        );

        entity = new EntityBuilder(world)
                .with(HitPoints.class,
                        Radius.class,
                        Operations.class,
                        HpControlledRadius.class)
                .build();

    }

    @Test
    public void should_update_radius_after_radius_tween_has_completed() throws Exception {
        entity.getComponent(HitPoints.class).hp(1);
        Radius radius = entity.getComponent(Radius.class);
        radius.r(1);
        entity.getComponent(HpControlledRadius.class)
                .baseRadius(10)
                .hpMultiplier(5);

        world.setDelta(1);
        world.process();

        assertThat(radius.r).isEqualTo(15);
    }

}