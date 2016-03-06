package com.cucumber.workshop.logic.basic;

import com.artemis.PooledComponent;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class HitPoints extends PooledComponent{
    public float hp = 1;

    public HitPoints hp(float hp) {
        this.hp = hp;
        return this;
    }

    public HitPoints damage(float damage) {
        this.hp -= damage;
        return this;
    }

    public boolean isAlive(){
        return hp > 0;
    }

    @Override
    protected void reset() {
        this.hp = 1;
    }
}
