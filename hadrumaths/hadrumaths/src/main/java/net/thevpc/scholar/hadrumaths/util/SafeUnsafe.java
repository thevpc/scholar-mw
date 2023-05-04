package net.thevpc.scholar.hadrumaths.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class SafeUnsafe implements UnsafeHandler{
    public static final SafeUnsafe INSTANCE=new SafeUnsafe();
    private static final int ARCH_MODEL_BITS = Integer.valueOf(
            System.getProperty("sun.arch.data.model") != null ? System.getProperty("sun.arch.data.model")
                    : System.getProperty("os.arch").contains("64") ? "64" : "32"
    );
    private static Throwable error = null;
    private static final int BYTE_BITS = 8;
    private static final int WORD = ARCH_MODEL_BITS / BYTE_BITS;
    private static final int JOBJECT_MIN_SIZE = 16;


    private SafeUnsafe() {
    }

    public int sizeOf(Class src) {
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
        int maxOffset = JOBJECT_MIN_SIZE;
        for (Field f : instanceFields) {
            Class<?> t = f.getType();
            if(t.isPrimitive()){
                switch (t.getName()){
                    case "boolean":
                    case "byte":
                    {
                        maxOffset += 1;
                        break;
                    }
                    case "short": {
                        maxOffset += 2;
                        break;
                    }
                    case "int":
                    case "float":
                    case "char":
                    {
                        maxOffset += 4;
                        break;
                    }
                    case "long":
                    case "double":
                    {
                        maxOffset += 8;
                        break;
                    }
                    default:{
                        throw new IllegalArgumentException("unsupported");
                    }
                }
            }else{
                maxOffset+=ARCH_MODEL_BITS/BYTE_BITS;
            }
        }
        return maxOffset;//(((int) maxOffset / WORD) + 1) * WORD;
    }

}
