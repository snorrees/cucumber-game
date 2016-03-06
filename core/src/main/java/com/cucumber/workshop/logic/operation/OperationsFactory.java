package com.cucumber.workshop.logic.operation;

import com.badlogic.gdx.math.Interpolation;
import net.mostlyoriginal.api.operation.common.Operation;

/**
 * @author Snorre E. Brekke - snorre.e.brekke@gmail.com
 */
public class OperationsFactory {

    public static TweenRadiusOperation tweenRadius(float from, float to,
                                                   float duration,
                                                   Interpolation interpolation) {
        final TweenRadiusOperation operation = Operation.prepare(TweenRadiusOperation.class);
        operation.setup(interpolation, duration);
        operation.getFrom().r = from;
        operation.getTo().r = to;
        return operation;
    }

    public static TweenFadeOperation tweenFade(float from, float to,
                                                   float duration,
                                                   Interpolation interpolation) {
        final TweenFadeOperation operation = Operation.prepare(TweenFadeOperation.class);
        operation.setup(interpolation, duration);
        operation.getFrom().alpha = from;
        operation.getTo().alpha = to;
        return operation;
    }
}
