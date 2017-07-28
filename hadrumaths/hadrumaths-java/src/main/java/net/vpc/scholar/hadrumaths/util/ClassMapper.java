//package net.vpc.scholar.math.util;
//
//import java.util.HashSet;
//import java.util.Hashtable;
//import java.util.Map;
//import java.util.Stack;
//
///**
// * @author Taha BEN SALAH (taha.bensalah@gmail.com)
// * @creationtime  13 juil. 2006 22:14:21
// */
//public class ClassMapper<V> extends Hashtable<Class, V> {
//    private static final long serialVersionUID = -1010101010101001057L;
//    private Hashtable<Class, V> cache = new Hashtable<Class, V>();
//
//    public ClassMapper(int initialCapacity, float loadFactor) {
//        super(initialCapacity, loadFactor);
//    }
//
//    public ClassMapper(int initialCapacity) {
//        super(initialCapacity);
//    }
//
//    public ClassMapper() {
//    }
//
//    public ClassMapper(Map<? extends Class, ? extends V> t) {
//        super(t);
//    }
//
//    public V put(Class key, V value) {
//        cache.clear();
//        return super.put(key, value);
//    }
//
//    public V remove(Object key) {
//        cache.clear();
//        return super.remove(key);
//    }
//
//    public V getBest(Class clazz) {
//        V value;
//        HashSet<Class> seen = new HashSet<Class>();
//        Stack<Class> queue = new Stack<Class>();
//        queue.add(0, clazz);
//        seen.add(clazz);
//        while (queue.size() > 0) {
//            Class k = queue.pop();
//            value = get(k);
//            if (value == null) {
//                value = cache.get(clazz);
//            }
//            if (value != null) {
//                cache.put(clazz, value);
//                return value;
//            } else {
//                Class superclass = k.getSuperclass();
//                if (superclass != null && !seen.contains(superclass)) {
//                    seen.add(superclass);
//                    queue.add(0, k.getSuperclass());
//                }
//                Class[] interfaces = k.getInterfaces();
//                for (Class aClass : interfaces) {
//                    if (!seen.contains(aClass)) {
//                        seen.add(aClass);
//                        queue.add(0, aClass);
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    public Class getKey(Class clazz) {
//        HashSet<Class> seen = new HashSet<Class>();
//        Stack<Class> queue = new Stack<Class>();
//        queue.add(0, clazz);
//        seen.add(clazz);
//        while (queue.size() > 0) {
//            Class k = queue.pop();
//            if (containsKey(k)) {
//                return k;
//            }
//            Class superclass = k.getSuperclass();
//            if (superclass != null && !seen.contains(superclass)) {
//                seen.add(superclass);
//                queue.add(0, k.getSuperclass());
//            }
//            Class[] interfaces = k.getInterfaces();
//            for (Class aClass : interfaces) {
//                if (!seen.contains(aClass)) {
//                    seen.add(aClass);
//                    queue.add(0, aClass);
//                }
//            }
//        }
//        return null;
//    }
//
//}
