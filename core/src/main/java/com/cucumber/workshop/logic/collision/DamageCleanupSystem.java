package com.cucumber.workshop.logic.collision;

import com.artemis.Aspect;
import com.artemis.systems.IteratingSystem;
import com.cucumber.workshop.logic.basic.Damage;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class DamageCleanupSystem extends IteratingSystem{

    public DamageCleanupSystem() {
        super(Aspect.all(Damage.class));
    }

    @Override
    protected void process(int entityId) {
        world.delete(entityId);
    }
}
