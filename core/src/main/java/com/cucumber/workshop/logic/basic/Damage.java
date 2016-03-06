package com.cucumber.workshop.logic.basic;

import com.artemis.PooledComponent;
import com.cucumber.workshop.logic.enemy.Type;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Damage extends PooledComponent {
    public Type type = Type.GREEN;
    public float amount = 1;

    public Damage type(Type type) {
        this.type = type;
        return this;
    }

    public Damage amount(float amount) {
        this.amount = amount;
        return this;
    }


    @Override
    protected void reset() {
        type = Type.GREEN;
        amount = 1;
    }
}
