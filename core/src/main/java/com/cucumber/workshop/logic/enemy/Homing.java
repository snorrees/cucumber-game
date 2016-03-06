package com.cucumber.workshop.logic.enemy;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Homing extends PooledComponent {

    public float turnrate = 0;

    public Homing turnrate(float turnrate) {
        this.turnrate = turnrate;
        return this;
    }

    @Override
    protected void reset() {
        turnrate = 0;
    }
}
