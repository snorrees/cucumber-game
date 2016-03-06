package com.cucumber.workshop.logic.enemy;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Enemy extends PooledComponent {
    public Type type = Type.GREEN;

    public Enemy type(Type type) {
        this.type = type;
        return this;
    }

    @Override
    protected void reset() {
        type = Type.GREEN;
    }

}
