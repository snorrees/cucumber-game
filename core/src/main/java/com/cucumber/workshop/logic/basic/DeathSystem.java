package com.cucumber.workshop.logic.basic;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class DeathSystem extends IteratingSystem{

    private ComponentMapper<HitPoints> hps;

    public DeathSystem() {
        super(Aspect.all(HitPoints.class));
    }

    @Override
    protected void process(int entityId) {
        HitPoints hp = hps.get(entityId);
        if (!hp.isAlive()) {
            world.delete(entityId);
        }
    }
}
