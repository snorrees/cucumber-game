package com.cucumber.workshop.logic.operation;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class HpControlledRadius extends PooledComponent {
    public float baseRadius = 10;
    public float hpMultiplier = 1;

    public HpControlledRadius baseRadius(float baseRadius) {
        this.baseRadius = baseRadius;
        return this;
    }

    public HpControlledRadius hpMultiplier(float hpMultiplier) {
        this.hpMultiplier = hpMultiplier;
        return this;
    }

    @Override
    protected void reset() {
        this.baseRadius = 10;
        this.hpMultiplier = 1;
    }
}
