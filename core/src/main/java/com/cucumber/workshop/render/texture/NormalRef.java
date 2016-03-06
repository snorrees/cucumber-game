package com.cucumber.workshop.render.texture;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class NormalRef extends PooledComponent{
    public String id = "";

    public NormalRef id(String id) {
        this.id = id;
        return this;
    }

    @Override
    protected void reset() {
        id = "";
    }
}
