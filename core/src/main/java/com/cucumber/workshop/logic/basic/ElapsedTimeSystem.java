package com.cucumber.workshop.logic.basic;

import com.artemis.BaseSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class ElapsedTimeSystem extends BaseSystem {
    private double time;

    @Override
    protected void processSystem() {
        time += world.delta;
    }

    public double time() {
        return time;
    }
}
