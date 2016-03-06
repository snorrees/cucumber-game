package com.cucumber.workshop.logic.basic;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Lifetime extends PooledComponent{
    public float time = 0;

    public Lifetime time(float time) {
        this.time = time;
        return this;
    }

    public boolean isAlive(){
        return time > 0;
    }

    @Override
    protected void reset() {
        this.time = 0;
    }
}
