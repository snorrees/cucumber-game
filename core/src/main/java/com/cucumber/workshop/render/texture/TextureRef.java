package com.cucumber.workshop.render.texture;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class TextureRef extends PooledComponent{
    public String id = "";

    public TextureRef id(String id) {
        this.id = id;
        return this;
    }

    @Override
    protected void reset() {
        id = "";
    }
}
