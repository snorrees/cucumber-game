package com.cucumber.workshop.logic.spatial;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class VelocitySystem extends IteratingSystem{

    private ComponentMapper<Position> positions;
    private ComponentMapper<Velocity> velocities;

    public VelocitySystem() {
        super(Aspect.all(Position.class, Velocity.class));
    }

    @Override
    protected void process(int entityId) {
        Position position = positions.get(entityId);
        Velocity velocity = velocities.get(entityId);

        position.x += velocity.dx * world.delta;
        position.y += velocity.dy * world.delta;
    }
}
