package com.cucumber.workshop.render.basic;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Tint extends PooledComponent {
    public static final Tint WHITE = new Tint();
    public Color color = new Color(1,1,1,1);

    public Tint color(Color color) {
        this.color = color;
        return this;
    }

    public Tint color(float r, float g, float b) {
        this.color.set(r, g, b, 1);
        return this;
    }

    @Override
    protected void reset() {
        color.set(1, 1, 1, 1);
    }
}
