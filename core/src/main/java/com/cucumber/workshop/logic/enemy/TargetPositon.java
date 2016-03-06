package com.cucumber.workshop.logic.enemy;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class TargetPositon extends PooledComponent {
    public float x;
    public float y;

    public TargetPositon x(float x) {
        this.x = x;
        return this;
    }

    public TargetPositon y(float y) {
        this.y = y;
        return this;
    }

    @Override
    protected void reset() {
        x = 0;
        y = 0;
    }
}
