package com.cucumber.workshop.logic.operation;

import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.reflect.ClassReflection;
import com.artemis.utils.reflect.ReflectionException;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.api.component.common.Tweenable;
import net.mostlyoriginal.api.operation.common.TemporalOperation;
import net.mostlyoriginal.api.utils.Preconditions;

public abstract class TweenOperation<T extends Component & Tweenable> extends TemporalOperation {

    protected final T a;
    protected final T b;
    protected final Class<T> type;
    protected ComponentMapper<T> m;

    public TweenOperation(Class<T> type) {
        try {
            this.type = type;
            a = ClassReflection.newInstance(type);
            b = ClassReflection.newInstance(type);
        } catch (ReflectionException e) {
            String error = "Couldn't instantiate object of type " + type.getName();
            throw new RuntimeException(error, e);
        }
    }

    @Override
    public void act(float percentage, Entity e) {
        applyTween(e, percentage);
    }

    @SuppressWarnings("unchecked")
    protected final void applyTween(Entity e, float a) {

        if (m == null) {
            // resolve component mapper if not set yet.
            // gets cleared every reset for non managed tweens.
            m = e.getWorld().getMapper(type);
        }

        // apply tween to component, create if missing.
        m.create(e).tween(this.a, b, MathUtils.clamp(a, 0, 1));
    }

    public TweenOperation<T> setup(Interpolation interpolation, float duration) {
        Preconditions.checkArgument(duration != 0, "Duration cannot be zero.");
        this.interpolation = Preconditions.checkNotNull(interpolation);
        this.duration = duration;
        return this;
    }

    @SuppressWarnings("unchecked")
    public final T getFrom() {
        return (T) a;
    }

    @SuppressWarnings("unchecked")
    public final T getTo() {
        return (T) b;
    }
}
