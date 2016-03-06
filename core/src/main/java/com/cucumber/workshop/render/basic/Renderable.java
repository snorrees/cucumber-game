package com.cucumber.workshop.render.basic;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Renderable extends PooledComponent {
    public int layer;

    @Override
    protected void reset() {
        layer = 0;
    }
}
