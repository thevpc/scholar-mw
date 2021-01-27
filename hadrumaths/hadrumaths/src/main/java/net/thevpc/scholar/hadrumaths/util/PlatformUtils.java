package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.scholar.hadrumaths.Maths;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.thevpc.scholar.hadrumaths.util.internal.NonStateField;

public class PlatformUtils {

    public static String[] splitPath(String... items) {
        List<String> a = new ArrayList<>();
        if (items != null) {
            for (String item : items) {
                if (item != null) {
                    for (String s : item.split("[/\\\\]+")) {
                        if (!s.isEmpty()) {
                            a.add(s);
                        }
                    }
                }
            }
        }
        return a.toArray(new String[0]);
    }

    public static boolean containsConcreteMethodHierarchy(Class cls, String name, Class... typeArgs) {
        boolean b = containsImmediateConcreteMethod(cls, name, typeArgs);
        if (b) {
            return true;
        }
        Class s = cls.getSuperclass();
        if (s != null) {
            return containsConcreteMethodHierarchy(s, name, typeArgs);
        }
        return false;
    }

    public static boolean containsHierarchyConcreteMethodButObject(Class cls, String name, Class... typeArgs) {
        boolean b = containsImmediateConcreteMethod(cls, name, typeArgs);
        if (b) {
            return true;
        }
        Class s = cls.getSuperclass();
        if (s != null && !s.equals(Object.class)) {
            return containsConcreteMethodHierarchy(s, name, typeArgs);
        }
        return false;
    }

    public static boolean isImplements(Class cls, Class interfaceType) {
        for (Class anInterface : cls.getInterfaces()) {
            if (anInterface.equals(interfaceType) || isImplements(anInterface, interfaceType)) {
                return true;
            }
        }
        Class c = cls.getSuperclass();
        if (c != null) {
            return isImplements(c, interfaceType);
        }
        return false;
    }

    public static boolean containsImmediateConcreteMethod(Class cls, String name, Class... typeArgs) {
        try {
            Method m = cls.getDeclaredMethod(name, typeArgs);
            return !Modifier.isAbstract(m.getModifiers());
        } catch (NoSuchMethodException ee) {
            //
        }
        return false;
    }

    public static boolean isAbstract(Class cls) {
        return Modifier.isAbstract(cls.getModifiers());
    }

    public static boolean isConcreteClass(Class cls) {
        return !Modifier.isAbstract(cls.getModifiers()) && !cls.isInterface();
    }

    public static void requireEqualsAndHashCode(Class o) {
        if (!hasValidEqualsAndHashCode(o)) {
            throw new IllegalArgumentException("Class " + o.getName() + " should implement equals() & hashCode() methods ");
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
            throw new IllegalArgumentException("Class " + cls.getName() + " should implement BOTH equals() and hashCode() methods ");
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
        long before = Maths.inUseMemory();
        Runtime rt = Runtime.getRuntime();
        long lastFreed = 0;
        int iterations = 0;
        for (int i = 0; i < 10; i++) {
            long before1 = Maths.inUseMemory();
            rt.gc();
            long after1 = Maths.inUseMemory();
            long freed1 = before1 - after1;
            long freed = before - after1;
            iterations++;
            long deltaFreed = (freed1 > lastFreed) ? (freed1 - lastFreed) : (lastFreed - freed1);
            lastFreed = freed1;
//            System.out.println("\tfreed : "+Maths.formatMemory(freed1) +" : "+Maths.formatMemory(freed));
            if (deltaFreed < 1024) break;
        }
        long after = Maths.inUseMemory();
        long freed = before - after;
//        System.out.println("freed in "+iterations+" iterations : "+Maths.formatMemory(freed));
        return freed;
    }

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

    public static <T> T notnull(T v, T defaultValue) {
        if (v == null) {
            return defaultValue;
        }
        return v;
    }

//    public static long gc() {
//        long before1 = Maths.inUseMemory();
//        Runtime rt = Runtime.getRuntime();
//        rt.gc();
//        long after1 = Maths.inUseMemory();
//        long freed1 = before1-after1;
//        System.out.println("freed : "+Maths.formatMemory(freed1));
//        return freed1;
//    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 100; i++) {
////            boolean[] o=new boolean[100000];
//            gc2();
////            System.out.println(Maths.formatMemory(gc2()));
//        }
//    }

    public static int getArrayDimension(Class c) {
        int dim = 0;
        Class cls = c;
        while (cls.isArray()) {
            dim++;
            cls = cls.getComponentType();
        }
        return dim;
    }

    public static Class getArrayRootComponentType(Class c) {
        int dim = 0;
        Class cls = c;
        while (cls.isArray()) {
            dim++;
            cls = cls.getComponentType();
        }
        return cls;
    }

    public static List<Conflict> resolveConflicts(Map any) {
        Map<Integer, Conflict> c = new HashMap<>();
        for (Object o : any.keySet()) {
            int r = o.hashCode();
            Conflict conflict = c.get(r);
            if (conflict == null) {
                conflict = new Conflict(r);
                c.put(r, conflict);
            }
            conflict.items.add(o);
        }
        List<Conflict> a = new ArrayList<>();
        for (Map.Entry<Integer, Conflict> e : c.entrySet()) {
            if (e.getValue().items.size() >= 2) {
                a.add(e.getValue());
            }
        }
        a.sort(null);
        return a;
    }

    public static class Conflict implements Comparable<Conflict> {
        private int hashCode;
        private List<Object> items = new ArrayList<>();

        public Conflict(int hashCode) {
            this.hashCode = hashCode;
        }

        public int getHashCodeId() {
            return hashCode;
        }

        public List<Object> getItems() {
            return items;
        }

        @Override
        public int compareTo(Conflict o) {
            int r = Integer.compare(items.size(), o.items.size());
            if (r != 0) {
                return r;
            }
            return Integer.compare(hashCode, o.hashCode);
        }
    }
}
