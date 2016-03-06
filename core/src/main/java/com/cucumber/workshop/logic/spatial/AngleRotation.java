package com.cucumber.workshop.logic.spatial;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class AngleRotation extends PooledComponent{
    public float degreesPerSecond = 0;

    public AngleRotation degreesPerSecond(float degrees) {
        this.degreesPerSecond = degrees;
        return this;
    }

    @Override
    protected void reset() {
        this.degreesPerSecond = 0;
    }
}
