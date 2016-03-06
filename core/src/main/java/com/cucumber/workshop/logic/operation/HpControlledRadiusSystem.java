package com.cucumber.workshop.logic.operation;

import static com.badlogic.gdx.math.Interpolation.pow3In;
import static com.cucumber.workshop.logic.operation.OperationsFactory.tweenRadius;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.cucumber.workshop.logic.basic.HitPoints;
import com.cucumber.workshop.logic.spatial.Radius;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class HpControlledRadiusSystem extends IteratingSystem{

    private ComponentMapper<HitPoints> hps;
    private ComponentMapper<Radius> radii;
    private ComponentMapper<HpControlledRadius> controllers;
    private ComponentMapper<Operations> operations;

    public HpControlledRadiusSystem() {
        super(Aspect.all(HitPoints.class,
                         Radius.class,
                         Operations.class,
                         HpControlledRadius.class));
    }

    @Override
    protected void process(int entityId) {
        HitPoints hp = hps.get(entityId);
        Radius radius = radii.get(entityId);
        HpControlledRadius control = controllers.get(entityId);
        float targetRadius = control.baseRadius + control.hpMultiplier * hp.hp;

        if (radius.r != targetRadius) {
            Operations tasks = this.operations.get(entityId);
            if (!tasks.contains(TweenRadiusOperation.class)) {
                tasks.operations.add(tweenRadius(radius.r, targetRadius, 0.25f, pow3In));
            }
        }
    }
}
