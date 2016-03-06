package com.cucumber.workshop.logic.spatial;

import com.artemis.PooledComponent;
import net.mostlyoriginal.api.component.common.Tweenable;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Radius extends PooledComponent implements Tweenable<Radius> {
    public float r;

    public Radius r(float r){
        this.r = r;
        return this;
    }

    @Override
    protected void reset() {
        this.r = 0;
    }

    @Override
    public Radius tween(Radius from, Radius to, float percentage) {
        r = from.r + (to.r - from.r) * percentage;
        return this;
    }
}
