package com.cucumber.workshop.logic.spatial;

import static org.assertj.core.api.Assertions.assertThat;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class VelocitySystemTest {

    World world;
    Position position;
    Velocity velocity;
    Entity entity;

    @Before
    public void setUp() throws Exception {
        world = new World(
                new WorldConfiguration()
                        .setSystem(VelocitySystem.class)
        );

        entity = world.createEntity();
        position = entity.edit().create(Position.class);
        velocity = entity.edit().create(Velocity.class);
    }

    @Test
    public void should_increase_x_component() throws Exception {
        position.x(0).y(0);
        velocity.dx(1);

        world.setDelta(0.5f);
        world.process();

        assertThat(position.x).isEqualTo(0.5f);
    }

    @Test
    public void should_increase_y_component() throws Exception {
        position.x(0).y(0);
        velocity.dy(1);

        world.setDelta(0.5f);
        world.process();

        assertThat(position.y).isEqualTo(0.5f);
    }
}