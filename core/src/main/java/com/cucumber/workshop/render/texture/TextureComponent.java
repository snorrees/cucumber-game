package com.cucumber.workshop.render.texture;

import com.artemis.PooledComponent;
import com.artemis.annotations.Transient;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
@Transient
public class TextureComponent extends PooledComponent{

    public Texture texture;

    @Override
    protected void reset() {
        texture = null;
    }
}
