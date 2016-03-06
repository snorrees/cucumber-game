package com.cucumber.workshop.logic.operation;

import java.util.Iterator;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class OperationsSystem extends IteratingSystem{

    private ComponentMapper<Operations> operations;

    public OperationsSystem() {
        super(Aspect.all(Operations.class));
    }

    @Override
    protected void process(int entityId) {
        Array<TweenOperation> tasks = operations.get(entityId).operations;
        Iterator<TweenOperation> iterator = tasks.iterator();
        Entity e = world.getEntity(entityId);
        while (iterator.hasNext()) {
            TweenOperation operation = iterator.next();
            boolean completed = operation.process(world.delta, e);
            if (completed) {
                iterator.remove();
            }
        }
    }
}
