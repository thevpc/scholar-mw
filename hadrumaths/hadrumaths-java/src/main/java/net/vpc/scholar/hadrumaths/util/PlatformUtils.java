package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.Conj;
import net.vpc.scholar.hadrumaths.symbolic.Cos;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class PlatformUtils {
    public static void main(String[] args) {
        requireEqualsAndHashCode(Conj.class);
    }
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
}
