package com.cucumber.workshop.logic.spatial;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Angle extends PooledComponent{
    public static final Angle ZERO = new Angle();
    public float degrees = 0;

    public Angle degrees(float degrees) {
        this.degrees = degrees;
        return this;
    }

    @Override
    protected void reset() {
        this.degrees = 0;
    }
}
