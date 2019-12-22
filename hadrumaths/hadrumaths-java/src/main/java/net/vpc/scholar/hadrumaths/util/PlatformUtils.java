package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.MathsBase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class PlatformUtils {


    public static void requireEqualsAndHashCode(Class o) {
        if (!hasValidEqualsAndHashCode(o)) {
            throw new IllegalArgumentException("Class " + o + " should implement equals() & hashCode() methods ");
        }
    }

    private static boolean hasStateFields(Class cls) {
        for (Field field : cls.getDeclaredFields()) {
            int m = field.getModifiers();
            if (!Modifier.isStatic(m) && field.getAnnotation(NonStateField.class) == null) {
                return true;
            }
        }
        return false;
    }





    public static boolean hasValidEqualsAndHashCode(Class cls) {
        boolean e = false;
        boolean h = false;
        try {
            Method equals = cls.getDeclaredMethod("equals", Object.class);
            e = true;
        } catch (NoSuchMethodException ee) {
            //
        }
        try {
            Method hashCode = cls.getDeclaredMethod("hashCode");
            h = true;
        } catch (NoSuchMethodException ee) {
            //
        }
        if (e != h) {
            throw new IllegalArgumentException("Class " + cls + " should implement BOTH equals() and hashCode() methods ");
        }
        if (!e) {
            if (hasStateFields(cls)) {
                return false;
            }
            Class s = cls.getSuperclass();
            if (s != null && s != Object.class) {
                return hasValidEqualsAndHashCode(s);
            }
            return false;
        }
        return true;
    }



    public static long gc2() {
        long before = MathsBase.inUseMemory();
        Runtime rt = Runtime.getRuntime();
        long lastFreed = 0;
        int iterations = 0;
        for (int i = 0; i < 10; i++) {
            long before1 = MathsBase.inUseMemory();
            rt.gc();
            long after1 = MathsBase.inUseMemory();
            long freed1 = before1 - after1;
            long freed = before - after1;
            iterations++;
            long deltaFreed = (freed1 > lastFreed) ? (freed1 - lastFreed) : (lastFreed - freed1);
            lastFreed = freed1;
//            System.out.println("\tfreed : "+MathsBase.formatMemory(freed1) +" : "+MathsBase.formatMemory(freed));
            if (deltaFreed < 1024) break;
        }
        long after = MathsBase.inUseMemory();
        long freed = before - after;
//        System.out.println("freed in "+iterations+" iterations : "+MathsBase.formatMemory(freed));
        return freed;
    }

//    public static long gc() {
//        long before1 = MathsBase.inUseMemory();
//        Runtime rt = Runtime.getRuntime();
//        rt.gc();
//        long after1 = MathsBase.inUseMemory();
//        long freed1 = before1-after1;
//        System.out.println("freed : "+MathsBase.formatMemory(freed1));
//        return freed1;
//    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++) {
////            boolean[] o=new boolean[100000];
//            gc2();
////            System.out.println(MathsBase.formatMemory(gc2()));
//        }
//    }


    public static <K, V> Map<K, ? super V> merge(Map<K, ? super V> destination, Map<K, ? extends V>... sources) {
        if (destination == null) {
            destination = new HashMap<K, V>();
        }
        if (sources != null) {
            for (Map<K, ? extends V> source : sources) {
                if (source != null) {
                    destination.putAll(source);
                }
            }
        }
        return destination;
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        }
        if (o1 == null || o2 == null) {
            return false;
        }
        return o1.equals(o2);
    }

    public static <T> T notnull(T v,T defaultValue) {
        if (v == null) {
            return defaultValue;
        }
        return v;
    }
}
