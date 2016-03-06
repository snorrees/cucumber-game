package com.cucumber.workshop.logic.basic;

import com.artemis.PooledComponent;
import net.mostlyoriginal.api.component.common.Tweenable;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Fade extends PooledComponent implements Tweenable<Fade>{
    public static final Fade ALPHA_ONE = new Fade();
    public float alpha = 1;

    public Fade alpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    @Override
    protected void reset() {
        this.alpha = 1;
    }

    @Override
    public Fade tween(Fade from, Fade to, float percentage) {
        alpha = from.alpha + (to.alpha - from.alpha) * percentage;
        return this;
    }
}
