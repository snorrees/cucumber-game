package com.cucumber.workshop.logic.basic;

import com.artemis.PooledComponent;
import com.cucumber.workshop.logic.enemy.Type;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Weapon extends PooledComponent {
    public Type type = Type.GREEN;
    public Action action;

    public Weapon type(Type type) {
        this.type = type;
        return this;
    }

    public Weapon action(Action action) {
        this.action = action;
        return this;
    }

    @Override
    protected void reset() {
        type = Type.GREEN;
    }
}
