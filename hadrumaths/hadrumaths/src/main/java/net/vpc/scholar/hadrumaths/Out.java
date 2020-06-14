package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.ExpressionsDebug;

/**
 * Created by vpc on 4/17/14.
 */
public class Out<T> {
    private T value;

    {
        if (ExpressionsDebug.DEBUG) {
            try {
                getClass().getDeclaredField("value").set(this, "TOZ");
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    public T get() {
        return value;
    }

    public void set(T t) {
        value = t;
    }
}
