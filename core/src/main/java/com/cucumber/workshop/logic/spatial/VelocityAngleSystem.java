package com.cucumber.workshop.logic.spatial;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class VelocityAngleSystem extends IteratingSystem{

    private ComponentMapper<Velocity> velocities;
    private ComponentMapper<Angle> angles;

    public VelocityAngleSystem() {
        super(Aspect.all(Velocity.class,
                         Angle.class,
                         AngleFollowVelocity.class));
    }

    @Override
    protected void process(int id) {
        Velocity vel = velocities.get(id);
        Angle angle = angles.get(id);

        float newAngle = MathUtils.atan2(vel.dy, vel.dx) * MathUtils.radDeg;
        angle.degrees(newAngle);
    }
}
