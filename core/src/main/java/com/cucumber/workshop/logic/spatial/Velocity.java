package com.cucumber.workshop.logic.spatial;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.MathUtils;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Velocity extends PooledComponent {
    public float dx;
    public float dy;

    public Velocity dx(float x) {
        this.dx = x;
        return this;
    }

    public Velocity dy(float y) {
        this.dy = y;
        return this;
    }

    public float angle() {
        return MathUtils.atan2(dy, dx) * MathUtils.radDeg;
    }

    public float scalar() {
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    protected void reset() {
        dx = 0;
        dy = 0;
    }
}
