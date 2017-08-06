package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Matrix;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class PlatformUtils {
    private static Comparator<Class> CLASS_HIERARCHY_COMPARATOR = new Comparator<Class>() {
        @Override
        public int compare(Class o1, Class o2) {
            if (o1.isAssignableFrom(o2)) {
                return 1;
            } else if (o2.isAssignableFrom(o1)) {
                return -1;
            }
            if (o1.isInterface() && !o2.isInterface()) {
                return 1;
            }
            if (o2.isInterface() && !o1.isInterface()) {
                return -1;
            }
            return 0;
        }
    };

    public static void requireEqualsAndHashCode(Class o){
        if(!hasValidEqualsAndHashCode(o)){
            throw new IllegalArgumentException("Class "+o+" should implement equals() & hashCode() methods ");
        }
    }
    private static boolean hasStateFields(Class cls){
        for (Field field : cls.getDeclaredFields()) {
            int m = field.getModifiers();
            if(!Modifier.isStatic(m) && field.getAnnotation(NonStateField.class)==null){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(lowestCommonAncestor(Complex.class,Matrix.class));
        System.out.println(commonAncestors(Complex.class,Matrix.class));
    }

    public static Class lowestCommonAncestor(Class a,Class b){
        if(a.equals(b)){
            return a;
        }
        if(a.isAssignableFrom(b)){
            return a;
        }
        if(b.isAssignableFrom(a)){
            return b;
        }
        Class[] aHierarchy = findClassHierarchy(a, null);
        Class[] bHierarchy = findClassHierarchy(b, null);
        int i1=-1;
        int i2=-1;
        for (int ii = 0; ii < aHierarchy.length; ii++) {
            for (int jj = 0; jj < bHierarchy.length; jj++) {
                if(aHierarchy[ii].equals(bHierarchy[jj])){
                    if(i1<0 || ii+jj<i1+i2) {
                        i1 = ii;
                        i2 = jj;
                    }
                }
            }
        }
        if(i1<0){
            return Object.class;
        }
        return aHierarchy[i1];
    }

    public static List<Class> commonAncestors(Class a, Class b){
        Class[] aHierarchy = findClassHierarchy(a, null);
        Class[] bHierarchy = findClassHierarchy(b, null);
        int i1=-1;
        int i2=-1;
        List<Class> all=new ArrayList<>();
        for (int ii = 0; ii < aHierarchy.length; ii++) {
            for (int jj = 0; jj < bHierarchy.length; jj++) {
                if(aHierarchy[ii].equals(bHierarchy[jj])){
                    all.add(aHierarchy[ii]);
                }
            }
        }
        return all;
    }

    public static boolean hasValidEqualsAndHashCode(Class cls){
        boolean e=false;
        boolean h=false;
        try {
            Method equals = cls.getDeclaredMethod("equals", Object.class);
            e=true;
        } catch (NoSuchMethodException ee) {
            //
        }
        try {
            Method hashCode = cls.getDeclaredMethod("hashCode");
            h=true;
        } catch (NoSuchMethodException ee) {
            //
        }
        if(e!=h){
            throw new IllegalArgumentException("Class "+cls+" should implement BOTH equals() and hashCode() methods ");
        }
        if(!e){
            if(hasStateFields(cls)){
                return false;
            }
            Class s = cls.getSuperclass();
            if(s!=null && s!=Object.class){
                return hasValidEqualsAndHashCode(s);
            }
            return false;
        }
        return true;
    }

    public static Class[] findClassHierarchy(Class clazz, Class baseType) {
        HashSet<Class> seen = new HashSet<Class>();
        Queue<Class> queue = new LinkedList<Class>();
        List<Class> result = new LinkedList<Class>();
        queue.add(clazz);
        while (!queue.isEmpty()) {
            Class i = queue.remove();
            if(baseType==null || baseType.isAssignableFrom(i)) {
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(i);
                    if (i.getSuperclass() != null) {
                        queue.add(i.getSuperclass());
                    }
                    for (Class ii : i.getInterfaces()) {
                        queue.add(ii);
                    }
                }
            }
        }
        Collections.sort(result, CLASS_HIERARCHY_COMPARATOR);
        return result.toArray(new Class[result.size()]);
    }

    public static Class[] findClassOnlyHierarchy(Class clazz, Class baseType) {
        HashSet<Class> seen = new HashSet<Class>();
        Queue<Class> queue = new LinkedList<Class>();
        List<Class> result = new LinkedList<Class>();
        queue.add(clazz);
        while (!queue.isEmpty()) {
            Class i = queue.remove();
            if(baseType==null || baseType.isAssignableFrom(i)) {
                if (!seen.contains(i)) {
                    seen.add(i);
                    result.add(i);
                    if (i.getSuperclass() != null) {
                        queue.add(i.getSuperclass());
                    }
                }
            }
        }
        Collections.sort(result, CLASS_HIERARCHY_COMPARATOR);
        return result.toArray(new Class[result.size()]);
    }
}
