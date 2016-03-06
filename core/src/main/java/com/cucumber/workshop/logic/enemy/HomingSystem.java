package com.cucumber.workshop.logic.enemy;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.cucumber.workshop.logic.spatial.Position;
import com.cucumber.workshop.logic.spatial.Velocity;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class HomingSystem extends IteratingSystem {

    private ComponentMapper<Position> positions;
    private ComponentMapper<Homing> homings;
    private ComponentMapper<Velocity> velocities;
    private ComponentMapper<TargetPositon> targets;

    public HomingSystem() {
        super(Aspect.all(
                Position.class,
                Velocity.class,
                Homing.class,
                TargetPositon.class
              )
        );
    }

    @Override
    protected void process(int entityId) {
        Position pos = positions.get(entityId);
        Homing homing = homings.get(entityId);
        TargetPositon targetPos = targets.get(entityId);
        Velocity velocity = velocities.get(entityId);

        float angle = velocity.angle();

        float x = pos.x;
        float y = pos.y;

        float targetX = targetPos.x;
        float targetY = targetPos.y;

        float diffX = targetX - x;
        float diffy = targetY - y;

        float targetAngle = MathUtils.atan2(diffy, diffX) * MathUtils.radiansToDegrees;
        targetAngle = normalize(targetAngle);

        float turnValue = homing.turnrate * world.delta;
        int direction;

        float diff = Math.abs(targetAngle - angle);
        if (diff > 180) {
            if (targetAngle > angle) {
                angle += 360;
            } else {
                targetAngle += 360;
            }
        }


        if (Math.abs(targetAngle - angle) <= turnValue) {
            turnValue = Math.abs(targetAngle - angle);
        }

        if (targetAngle > angle) {
            direction = (targetAngle - angle <= 180) ? 1 : -1;
        } else {
            direction = (angle - targetAngle <= 180) ? -1 : 1;
        }

        turnValue *= direction;

        angle += turnValue;

        float scalar = velocity.scalar();
        velocity.dx = scalar * MathUtils.cosDeg(angle);
        velocity.dy = scalar * MathUtils.sinDeg(angle);
    }

    public static float normalize(float angle) {
        float rotation = angle % 360;
        return rotation < 0 ? rotation + 360 : rotation;
    }

}
