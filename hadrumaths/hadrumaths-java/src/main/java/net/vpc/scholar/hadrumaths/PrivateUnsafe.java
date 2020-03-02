package net.vpc.scholar.hadrumaths;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

class PrivateUnsafe {
    private static final sun.misc.Unsafe UNSAFE_INSTANCE;
    private static final int ARCH_MODEL_BITS = Integer.valueOf(
            System.getProperty("sun.arch.data.model") != null ? System.getProperty("sun.arch.data.model")
                    : System.getProperty("os.arch").contains("64") ? "64" : "32"
    );
    private static Throwable error = null;
    private static final int BYTE_BITS = 8;
    private static final int WORD = ARCH_MODEL_BITS / BYTE_BITS;
    private static final int JOBJECT_MIN_SIZE = 16;

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

    public static int sizeOf(Class src) {
        List<Field> instanceFields = new LinkedList<Field>();
        while (instanceFields.isEmpty()) {
            if (src == null || src == Object.class) {
                return JOBJECT_MIN_SIZE;
            }
            for (Field f : src.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    instanceFields.add(f);
                }
            }
            src = src.getSuperclass();
        }
        long maxOffset = 0;
        Unsafe unsafeInstance = PrivateUnsafe.getUnsafeInstance();
        for (Field f : instanceFields) {
            long offset = unsafeInstance.objectFieldOffset(f);
            if (offset > maxOffset) {
                maxOffset = offset;
            }
        }
        return (((int) maxOffset / WORD) + 1) * WORD;
    }
}
