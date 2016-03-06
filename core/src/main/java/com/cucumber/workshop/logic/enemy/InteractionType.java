package com.cucumber.workshop.logic.enemy;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class InteractionType extends PooledComponent{
    public static final InteractionType FALLBACK = new InteractionType();

    public Type type = Type.GREEN;

    public InteractionType type(Type type) {
        this.type = type;
        return this;
    }

    @Override
    protected void reset() {
        type = Type.GREEN;
    }
}
