package com.cucumber.workshop.logic.spatial;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class AngleRotationSystem  extends IteratingSystem {
    private ComponentMapper<Angle> angles;
    private ComponentMapper<AngleRotation> rotations;

    public AngleRotationSystem() {
        super(Aspect.all(Angle.class, AngleRotation.class));
    }

    @Override
    protected void process(int entityId) {
        Angle angle = angles.get(entityId);
        AngleRotation rotation = rotations.get(entityId);
        angle.degrees += rotation.degreesPerSecond * world.delta;
    }
}
