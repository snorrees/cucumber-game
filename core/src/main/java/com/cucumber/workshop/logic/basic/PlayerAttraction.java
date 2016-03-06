package com.cucumber.workshop.logic.basic;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class PlayerAttraction extends PooledComponent{
    public float effect = 1;

    public PlayerAttraction effect(float effect) {
        this.effect = effect;
        return this;
    }

    @Override
    protected void reset() {
        this.effect = 1;
    }
}
