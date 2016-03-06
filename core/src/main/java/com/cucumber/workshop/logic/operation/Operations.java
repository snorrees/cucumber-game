package com.cucumber.workshop.logic.operation;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.Array;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class Operations extends PooledComponent {

    public final Array<TweenOperation> operations = new Array<>();

    public Operations add(TweenOperation operation) {
        operations.add(operation);
        return this;
    }

    public boolean contains(Class<? extends TweenOperation> operationType) {
        for (TweenOperation operation : operations) {
            if (operation.getClass().equals(operationType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void reset() {
        operations.clear();
    }
}
