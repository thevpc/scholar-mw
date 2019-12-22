package net.vpc.scholar.hadrumaths;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

class PrivateUnsafe {
    private static final sun.misc.Unsafe UNSAFE_INSTANCE;
    private static Throwable error = null;

    static {
        Object theUnsafe = null;
        try {
            Class<?> unsafeType = Class.forName("sun.misc.Unsafe");
            Field unsafeField = unsafeType.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            theUnsafe = unsafeField.get(unsafeType);
        } catch (Throwable e) {
            error = e;
        }
        UNSAFE_INSTANCE = (sun.misc.Unsafe) theUnsafe;
    }

    private PrivateUnsafe() {
    }

    static Unsafe getUnsafeInstance() {
        if (UNSAFE_INSTANCE == null && error != null) {
            throw new Error("Could not access sun.misc.Unsafe type", error);
        }
        return UNSAFE_INSTANCE;
    }

}
