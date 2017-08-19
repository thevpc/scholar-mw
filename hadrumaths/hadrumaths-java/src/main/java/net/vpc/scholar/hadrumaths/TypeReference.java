package net.vpc.scholar.hadrumaths;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * This is super type token implementation as described in
 * http://gafter.blogspot.com/2006/12/super-type-tokens.html
 * References a generic type.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public abstract class TypeReference<T> implements Serializable{

    private final Type type;
    private volatile Constructor<?> constructor;

    private TypeReference(Type type) {
        this.type = type;
    }

    protected TypeReference() {
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        this.type = ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public static void main(String[] args) throws Exception {
        List<String> l1 = new TypeReference<ArrayList<String>>() {
        }.newInstance();
        List l2 = new TypeReference<ArrayList>() {
        }.newInstance();
    }

    @Override
    public String toString() {
        return "TypeReference<"+type+">";
    }

    public static TypeReference of(Type type, Type... args) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            ParameterizedType ptype2 = new MyParameterizedType(ptype, args);
            return new TypeReference(ptype2) {
            };
        }
        return new TypeReference(type) {
        };
    }

    /**
     * Instantiates a new instance of {@code T} using the default, no-arg
     * constructor.
     */
    @SuppressWarnings("unchecked")
    public T newInstance()
            throws NoSuchMethodException, IllegalAccessException,
            InvocationTargetException, InstantiationException {
        if (constructor == null) {
            Class<?> rawType = type instanceof Class<?>
                    ? (Class<?>) type
                    : (Class<?>) ((ParameterizedType) type).getRawType();
            constructor = rawType.getConstructor();
        }
        return (T) constructor.newInstance();
    }

    /**
     * Gets the referenced type.
     */
    public Class getTypeClass() {
        try {
            Type tt=type;
            while(tt instanceof ParameterizedType) {
                tt=((ParameterizedType) tt).getRawType();
            }
            return (Class) tt;
        }catch (ClassCastException ex){
            throw ex;
        }
    }

    public Type getType() {
        return this.type;
    }

    public boolean isAssignableFrom(TypeReference<?> cls) {
        return getTypeClass().isAssignableFrom(cls.getTypeClass());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeReference<?> that = (TypeReference<?>) o;

        return type != null ? type.equals(that.type) : that.type == null;
    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    public boolean isInterface() {
        return getTypeClass().isInterface();
    }

    public TypeReference[] getInterfaces() {
        Class[] interfaces = getTypeClass().getInterfaces();
        TypeReference[] typeReferences = new TypeReference[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            typeReferences[i]=of(interfaces[i]);//TODO params?
        }
        return typeReferences;
    }

    public TypeReference getSuperclass() {
        Class superclass = getTypeClass().getSuperclass();
        if(superclass==null){
            return null;
        }
        return of(superclass);
    }

    public <T> boolean isInstance(T t) {
        return getTypeClass().isInstance(t);
    }

    private static class MyParameterizedType implements ParameterizedType {
        private final ParameterizedType ptype;
        private final Type[] args;

        public MyParameterizedType(ParameterizedType ptype, Type... args) {
            this.ptype = ptype;
            this.args = args;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return ptype.getRawType();
        }

        @Override
        public Type getOwnerType() {
            return ptype.getOwnerType();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyParameterizedType that = (MyParameterizedType) o;

            if (ptype != null ? !ptype.equals(that.ptype) : that.ptype != null) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return Arrays.equals(args, that.args);
        }

        @Override
        public int hashCode() {
            int result = ptype != null ? ptype.hashCode() : 0;
            result = 31 * result + Arrays.hashCode(args);
            return result;
        }
    }
}
