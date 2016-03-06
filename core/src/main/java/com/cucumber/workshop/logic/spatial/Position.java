package com.cucumber.workshop.logic.spatial;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Position extends PooledComponent {
    public float x;
    public float y;

    public Position x(float x) {
        this.x = x;
        return this;
    }

    public Position y(float y) {
        this.y = y;
        return this;
    }

    @Override
    protected void reset() {
        x = 0;
        y = 0;
    }
}
