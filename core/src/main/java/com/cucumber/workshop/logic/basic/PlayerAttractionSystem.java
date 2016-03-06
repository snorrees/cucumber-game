package com.cucumber.workshop.logic.basic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Velocity;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class PlayerAttractionSystem extends IteratingSystem{

    private ComponentMapper<Position> positions;
    private ComponentMapper<Velocity> velocities;

    public PlayerAttractionSystem() {
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
