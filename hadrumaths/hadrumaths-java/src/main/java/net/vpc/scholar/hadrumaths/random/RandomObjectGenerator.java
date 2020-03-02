package net.vpc.scholar.hadrumaths.random;

import net.vpc.common.util.AcceptDenyClassSet;
import net.vpc.common.util.Chronometer;
import net.vpc.common.util.ClassMap;
import net.vpc.common.util.ClassMapList;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.RandomList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class RandomObjectGenerator<B extends RandomObjectGenerator> {
    protected ClassMap<Generator> generators = new ClassMap<Generator>(null, Generator.class);
    private AcceptDenyClassSet<Object> clsTypes = new AcceptDenyClassSet<Object>(Object.class);
    private boolean acceptBoundaryValue = true;
    private boolean acceptNaNValue = true;
    private boolean acceptInfValue = true;
    private int complexity = 4;
    private ClassMapList<InstanceValidator> instanceFilters = new ClassMapList<>(InstanceValidator.class);
    private Set<String> unrepeatableErrorMessages = new HashSet<>();
    private Class[] objectClasses = null;
    private ErrorList errorList;
    private Class expectedClass;

    public RandomObjectGenerator(Class expectedClass) {
        this.expectedClass = expectedClass;
        register(new Class[]{Double.class, Double.TYPE}, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                RandomList rnd = new RandomList();
                rnd.addIf(true, () -> 0.0);
                rnd.addIf(true, () -> 1.0);
                rnd.addIf(true, () -> -1.0);
                rnd.addIf(isAcceptInfValue(), () -> Double.NEGATIVE_INFINITY);
                rnd.addIf(isAcceptInfValue(), () -> Double.POSITIVE_INFINITY);
                rnd.addIf(isAcceptNaNValue(), () -> Double.NaN);
                rnd.addIf(isAcceptBoundaryValue(), () -> Double.MIN_VALUE);
                rnd.addIf(isAcceptBoundaryValue(), () -> Double.MAX_VALUE);
                rnd.addIf(true, 11, () -> Maths.randomDouble(-5, 5));
                return rnd.random();
            }
        });
        register(new Class[]{Integer.class, Integer.TYPE}, new Generator() {
                    @Override
                    public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                        RandomList rnd = new RandomList();
                        rnd.addIf(true, () -> 0);
                        rnd.addIf(true, () -> 1);
                        rnd.addIf(true, () -> -1);
                        rnd.addIf(isAcceptBoundaryValue(), () -> Integer.MIN_VALUE);
                        rnd.addIf(isAcceptBoundaryValue(), () -> Integer.MAX_VALUE);
                        rnd.addIf(true, 11, () -> Maths.randomInt(-5, 5));
                        return rnd.random();
                    }
                }
        );
        register(String.class, new Generator() {
                    @Override
                    public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                        return randomElement("string1", "string2", "string3", "string4", "string5");
                    }
                }
        );
        register(new Class[]{Boolean.TYPE, Boolean.class}, new Generator() {
                    @Override
                    public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                        return randomElement(Boolean.FALSE, Boolean.TRUE);
                    }
                }
        );
    }

    public void register(Class[] cls, Generator g) {
        for (Class cl : cls) {
            register(cl, g);
        }
    }

    public boolean isAcceptInfValue() {
        return acceptInfValue;
    }

    public boolean isAcceptNaNValue() {
        return acceptNaNValue;
    }

    public boolean isAcceptBoundaryValue() {
        return acceptBoundaryValue;
    }

    public void register(Class cls, Generator g) {
        generators.put(cls, g);
    }

    public static <T> T randomElement(T... arr) {
        return arr[Maths.randomInt(arr.length)];
    }

    public B setAcceptBoundaryValue(boolean acceptBoundaryValue) {
        this.acceptBoundaryValue = acceptBoundaryValue;
        return getThis();
    }

    protected B getThis() {
        return (B) this;
    }

    public B setAcceptNaNValue(boolean acceptNaNValue) {
        this.acceptNaNValue = acceptNaNValue;
        return getThis();
    }

    public B setAcceptInfValue(boolean acceptInfValue) {
        this.acceptInfValue = acceptInfValue;
        return getThis();
    }

    private static ClsParam[] getClsParams(Annotation[][] parameterAnnotations, Class<?>[] parameterTypes) {
        ClsParam[] tt = new ClsParam[parameterTypes.length];
        for (int i = 0; i < tt.length; i++) {
            Annotation[] anns = parameterAnnotations[i];
            Map<Class, InstanceValidator> t = new HashMap<>();
            for (Annotation ann : anns) {
                if (ann instanceof DoubleValidator) {
                    InstanceValidator notAllowed = new InstanceValidator() {
                        @Override
                        public void checkValid(Object object, RandomObjectGeneratorContext context) {
                            DoubleValidator dv = (DoubleValidator) ann;
                            if (!dv.NaN()) {
                                if (Double.isNaN((Double) object)) {
                                    throw new GenerateException(object.getClass(), "NaN not allowed");
                                }
                            }
                            if (!dv.negativeInf()) {
                                if (Double.NEGATIVE_INFINITY == ((Double) object)) {
                                    throw new GenerateException(object.getClass(), "-Inf not allowed");
                                }
                            }
                            if (!dv.positiveInf()) {
                                if (Double.POSITIVE_INFINITY == ((Double) object)) {
                                    throw new GenerateException(object.getClass(), "+Inf not allowed");
                                }
                            }
                        }
                    };
                    t.put(Double.class, notAllowed);
                    t.put(Double.TYPE, notAllowed);

                }
                if (ann instanceof IntValidator) {
                    InstanceValidator notAllowed = new InstanceValidator() {
                        @Override
                        public void checkValid(Object object, RandomObjectGeneratorContext context) {
                            IntValidator i = (IntValidator) ann;
                            int v = (int) object;
                            if (i.min() != Integer.MIN_VALUE) {
                                if (v < i.min()) {
                                    throw new GenerateException(object.getClass(), v + "<" + i.min());
                                }
                            }
                            if (i.max() != Integer.MAX_VALUE) {
                                if (v > i.max()) {
                                    throw new GenerateException(object.getClass(), v + ">" + i.max());
                                }
                            }
                        }
                    };
                    t.put(Integer.class, notAllowed);
                    t.put(Integer.TYPE, notAllowed);


                }
            }
            tt[i] = new DefaultClsParam(parameterTypes[i], t);
        }
        return tt;
    }

    public ErrorList getErrorList() {
        return errorList;
    }

    public B setErrorList(ErrorList errorList) {
        this.errorList = errorList;
        return getThis();
    }

    public B addInstanceFilter(Class type, InstanceValidator filter) {
        if (filter != null) {
            instanceFilters.add(type, filter);
        }
        return getThis();
    }

    public B removeInstanceFilter(Class type, InstanceValidator filter) {
        if (filter != null) {
            instanceFilters.remove(type, filter);
        }
        return getThis();
    }

    public B removeInstanceFilters(Class type) {
        List<InstanceValidator> list = instanceFilters.getExact(type);
        if (list != null) {
            list.clear();
        }
        return getThis();
    }

    public B acceptType(Class a) {
        clsTypes.accept(a);
        return getThis();
    }

    public B denyType(Class a) {
        clsTypes.deny(a);
        return getThis();
    }

    public <T> T randomObject(Class<T> cls) {
        return randomObject(cls, null, createContext());
    }

    public <T> T randomObject(Class<T> cls, Class asType, RandomObjectGeneratorContext context) {
        for (int i = 0; i < 100; i++) {
            try {
                T o = randomObjectOnce(cls, asType, context);
                for (InstanceValidator v : context.getFilter().getAll(cls)) {
                    v.checkValid(o, context);
                }
                return o;
            } catch (Exception ex) {
                //ignore error
            }
        }
        //last time without error blackout
        T o = randomObjectOnce(cls, asType, context);
        for (InstanceValidator v : context.getFilter().getAll(cls)) {
            v.checkValid(o, context);
        }
        return (T) o;
    }

    public RandomObjectGeneratorContext createContext() {
        return new RandomObjectGeneratorContext(getComplexity(), getInstanceFilters(), clsTypes, this);
    }

    protected <T> T randomObjectOnce(Class<T> cls, Class asType, RandomObjectGeneratorContext context) {
        if (asType == null) {
            asType = cls;
        }
        Generator g = generators.get(cls);
        if (g == null) {
            throw new NonRepeatableException(cls, "Missing generator for " + cls);
        }
        T u = _generateByGenerator(cls, asType, g, context);
        if (!cls.isPrimitive()) {
            if (!cls.isInstance(u) || !asType.isInstance(u)) {
                u = _generateByGenerator(cls, asType, g, context);
                if (!cls.isInstance(u) || !asType.isInstance(u)) {
                    throw new GenerateException(cls, "Type mismatch " + className(u.getClass()) + ". Expecting " + className(cls) + " as " + className(asType) + ")");
                }
            }
        }
        validateRandomGeneration(u, context);
        return u;
    }

    public int getComplexity() {
        return complexity;
    }

    public ClassMapList<InstanceValidator> getInstanceFilters() {
        return instanceFilters;
    }

    private <T> T _generateByGenerator(Class<T> cls, Class asType, Generator g, RandomObjectGeneratorContext context) {
        T u = null;
        try {
            u = (T) g.generate(cls, asType, context);
        } catch (GenerateException e) {
            if (errorList != null) {
                if (e instanceof NonRepeatableException) {
                    String m = e.getMessage();
                    if (!unrepeatableErrorMessages.contains(m)) {
                        unrepeatableErrorMessages.add(m);
                        errorList.addError(cls,
                                new GenerateException(cls,
                                        "Unable to instantiate " + className(cls) + " as " + className(asType) + ": " + e.getMessage(),
                                        e));
                    }
                } else {
                    errorList.addError(cls, new GenerateException(cls,
                            "Unable to instantiate " + className(cls) + " as " + className(asType) + ": " + e.getMessage(),
                            e
                    ));
                }
            }
            throw e;
        }
        if (u == null) {
            if (errorList != null) {
                errorList.addError(cls, "Unable to instantiate : Null Result");
            }
            throw new GenerateException(cls, "Unable to instantiate : Null Result");
        }
        return u;
    }

    public static String className(Class cls) {
        try {
            return cls.getSimpleName();
        } catch (Throwable e) {
            return cls.getName();
        }
    }

    protected <T> void validateRandomGeneration(T object, RandomObjectGeneratorContext context) {
    }

    public B setComplexity(int complexity) {
        this.complexity = complexity;
        return getThis();
    }

    public Generator getGenerator(Class cls) {
        return generators.get(cls);
    }

    public Class[] findExprSubClasses(Class required) {
        List<Class> all = new ArrayList<>();
        for (Class current : findObjectClasses()) {
            if (Modifier.isPublic(current.getModifiers()) && isAssignableClass(required, current)) {
                all.add(current);
            }
        }
        return all.toArray(new Class[0]);
    }

    public Class[] findObjectClasses() {
        List<Class> clss = new ArrayList<>();
        if (objectClasses == null) {
            Chronometer chrono = Chronometer.start();
            TreeSet<String> ignored = new TreeSet<>();
            TreeSet<String> error = new TreeSet<>();
            for (Class cls : ExprProjectClasses.getProjectClasses()) {
                String simpleName = null;
                try {
                    simpleName = className(cls);
                    if (getConstructors(cls, expectedClass).length > 0 ||
                            (expectedClass.isAssignableFrom(cls) && generators.getRegisteredKeys(cls).length > 0)) {
                        if (!getClsTypes().isAccept(cls)) {
                            ignored.add(simpleName);
                        } else {
                            if (expectedClass.isAssignableFrom(cls)) {
                                if (!cls.isInterface() && Modifier.isPublic(cls.getModifiers())) {
                                    if (cls.getAnnotation(IgnoreRandomGeneration.class) == null) {
                                        clss.add(cls);
                                    }
                                }
                            } else {
                                //clss.add(cls);
                            }
                        }
                    } else {
                        if (expectedClass.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                            error.add(simpleName);
                        }
                    }
                } catch (Throwable e) {
                    error.add(simpleName == null ? ("?(" + cls.toString() + ")") : simpleName);
                }
            }
            objectClasses = clss.stream().filter(c -> clsTypes.isAccept(c)).toArray(Class[]::new);
            System.err.println("Within " + chrono.stop() + " detected " + objectClasses.length + " types, ignored " + ignored.size() + " types : " + String.join(", ", ignored));
        }
        return objectClasses;
    }

    protected boolean isAssignableClass(Class required, Class current) {
        if (required.isAssignableFrom(current)) {
            return true;
        } else {
            CanProduceClass ann = (CanProduceClass) required.getAnnotation(CanProduceClass.class);
            if (ann != null) {
                for (Class ac : ann.value()) {
                    if (required.isAssignableFrom(ac)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ClsConstructor[] getConstructors(Class cls, Class expectedClass) {
        List<ClsConstructor> all = new ArrayList<>();
        if (Modifier.isPublic(cls.getModifiers()) && !Modifier.isAbstract(cls.getModifiers()) && expectedClass.isAssignableFrom(cls)) {
            Constructor<?>[] constructors = cls.getConstructors();
            for (Constructor<?> constructor : constructors) {
                //@RandomGenerationProperties("DisableRandomCalls")
                if (constructor.getAnnotation(IgnoreRandomGeneration.class) != null) {

                } else {
                    if (Modifier.isPublic(constructor.getModifiers())) {
                        all.add(new SimpleClsConstructor(cls, constructor));
                    }
                }
            }
        }
        if (expectedClass.isAssignableFrom(cls) ||
                cls.getAnnotation(CanProduceClass.class) != null ||
                cls.getAnnotation(CanProduceExprType.class) != null) {
            Method[] declaredMethods = null;
            try {
                declaredMethods = cls.getDeclaredMethods();
            } catch (java.lang.NoClassDefFoundError ex) {
                //ignore
            }
            if (declaredMethods != null) {
                for (Method method : declaredMethods) {
                    if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())
                            && expectedClass.isAssignableFrom(method.getReturnType())
                    ) {
                        //@RandomGenerationProperties("DisableRandomCalls")
                        if (method.getAnnotation(IgnoreRandomGeneration.class) != null) {

                        } else {
                            all.add(new MethodClsConstructor(cls, method));
                        }
                    }
                }
            }
        }
        return all.toArray(new ClsConstructor[0]);
    }

    protected AcceptDenyClassSet<Object> getClsTypes() {
        return clsTypes;
    }

    protected void logSimpleRepeatableException(GenerateException ex) {
        String m = ex.toString();
        if (!unrepeatableErrorMessages.contains(m)) {
            unrepeatableErrorMessages.add(m);
            System.err.println(m);
        }
    }

    public Class getExpectedClass() {
        return expectedClass;
    }

    public interface Generator {
        Object generate(Class cls, Class asType, RandomObjectGeneratorContext context);
    }

    interface ClsParam {
        Class getType();

        Map<Class, InstanceValidator> getConstraints();

    }

    interface ClsConstructor {
        Object create(Object[] ags);

        ClsParam[] getParams();

        Class getType();

    }

    public interface InstanceValidator {
        void checkValid(Object object, RandomObjectGeneratorContext context);

        default InstanceValidator and(InstanceValidator other) {
            return other == null ? this : new AndInstanceValidator(this, other);
        }

        default InstanceValidator or(InstanceValidator other) {
            return other == null ? this : new OrInstanceValidator(this, other);
        }
    }

    private static class SimpleClsConstructor implements ClsConstructor {
        private final Constructor<?> constructor;
        private Class type;

        public SimpleClsConstructor(Class type, Constructor<?> constructor) {
            this.constructor = constructor;
            this.type = type;
        }

        @Override
        public Object create(Object[] ags) {
            try {
                return constructor.newInstance(ags);
            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//                e.getTargetException().printStackTrace();
                throw new GenerateException(getType(), e.getTargetException());
            } catch (Exception e) {
//                e.printStackTrace();
                throw new GenerateException(getType(), e);
            }
        }

        @Override
        public ClsParam[] getParams() {
            Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            return getClsParams(parameterAnnotations, parameterTypes);
        }

        @Override
        public Class getType() {
            return type;
        }
    }

    public static class GenerateException extends RuntimeException {
        private Class cls;

        public GenerateException(Class cls, Throwable cause) {
            super("Unable to generate " + cls + " : " + cause.toString(), cause);
            this.cls = cls;
        }

        public GenerateException(Class cls) {
            this.cls = cls;
        }

        public GenerateException(Class cls, String message) {
            super(message);
            this.cls = cls;
        }

        public GenerateException(Class cls, String message, Throwable cause) {
            super(message, cause);
            this.cls = cls;
        }

        public GenerateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class cls) {
            super(message, cause, enableSuppression, writableStackTrace);
            this.cls = cls;
        }

        public Class getCls() {
            return cls;
        }
    }

    private static class DefaultClsParam implements ClsParam {
        private Class cls;
        private Map<Class, InstanceValidator> constraints;

        public DefaultClsParam(Class cls, Map<Class, InstanceValidator> constraints) {
            this.cls = cls;
            this.constraints = constraints;
        }

        public Class getType() {
            return cls;
        }

        public Map<Class, InstanceValidator> getConstraints() {
            return constraints;
        }
    }

    private static class MethodClsConstructor implements ClsConstructor {
        private final Method method;
        private final Class type;

        public MethodClsConstructor(Class type, Method method) {
            this.method = method;
            this.type = type;
        }

        @Override
        public Object create(Object[] ags) {
            try {
                return method.invoke(null, ags);
            } catch (InvocationTargetException e) {
                throw new GenerateException(type, e.getTargetException());
            } catch (Exception e) {
                throw new GenerateException(type, e);
            }
        }

        @Override
        public ClsParam[] getParams() {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Class<?>[] parameterTypes = method.getParameterTypes();
            return getClsParams(parameterAnnotations, parameterTypes);
        }

        public Class getType() {
            return type;
        }
    }

    protected static class NonRepeatableException extends GenerateException {
        public NonRepeatableException(Class cls) {
            super(cls);
        }

        public NonRepeatableException(Class cls, String message) {
            super(cls, message);
        }

        public NonRepeatableException(Class cls, String message, Throwable cause) {
            super(cls, message, cause);
        }

        public NonRepeatableException(Class cls, Throwable cause) {
            super(cls, cause);
        }
    }

    public static class RandomObjectGeneratorContext {
        private int complexity;
        private RandomObjectGenerator generator;
        private ClassMapList<InstanceValidator> filter;
        private AcceptDenyClassSet<Object> clsTypes;

        public RandomObjectGeneratorContext(int complexity, ClassMapList<InstanceValidator> filter, AcceptDenyClassSet<Object> clsTypes, RandomObjectGenerator generator) {
            this.complexity = complexity;
            this.generator = generator;
            this.filter = filter;
            this.clsTypes = clsTypes;
        }

        public ClassMapList<InstanceValidator> getFilter() {
            return filter;
        }

        public RandomObjectGeneratorContext setFilter(ClassMapList<InstanceValidator> filter) {
            return new RandomObjectGeneratorContext(complexity, filter, clsTypes, generator);
        }

        public RandomObjectGeneratorContext addFilter(Class type, InstanceValidator filter) {
            ClassMapList<InstanceValidator> c = this.filter.copy();
            c.add(type, filter);
            return setFilter(c);
        }

        public RandomObjectGeneratorContext removeFilter(Class type, InstanceValidator filter) {
            ClassMapList<InstanceValidator> c = this.filter.copy();
            c.add(type, filter);
            return setFilter(c);
        }

        public AcceptDenyClassSet<Object> getClsTypes() {
            return clsTypes;
        }

        public int getComplexity() {
            return complexity;
        }

        public RandomObjectGeneratorContext setComplexity(int complexity) {
            return new RandomObjectGeneratorContext(complexity, filter, clsTypes, generator);
        }

        public RandomObjectGeneratorContext setAccept(Class... any) {
            return new RandomObjectGeneratorContext(complexity, filter, new AcceptDenyClassSet<Object>(clsTypes.getBaseType()).acceptAll(any), generator);
        }

        public RandomObjectGeneratorContext addAccept(Class... any) {
            return new RandomObjectGeneratorContext(complexity, filter, new AcceptDenyClassSet<Object>(clsTypes).acceptAll(any), generator);
        }

        public RandomObjectGeneratorContext addDeny(Class... any) {
            return new RandomObjectGeneratorContext(complexity, filter, new AcceptDenyClassSet<Object>(clsTypes).denyAll(any), generator);
        }

        public <T extends RandomObjectGenerator> T getGenerator() {
            return (T) generator;
        }

        public RandomObjectGeneratorContext decComplexity() {
            if (complexity > 0) {
                return setComplexity(complexity - 1);
            }
            return this;
        }
    }

    private static class AndInstanceValidator implements InstanceValidator {
        private InstanceValidator a;
        private InstanceValidator b;

        public AndInstanceValidator(InstanceValidator a, InstanceValidator b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void checkValid(Object object, RandomObjectGeneratorContext context) {
            a.checkValid(a, context);
            b.checkValid(a, context);
        }
    }

    private static class OrInstanceValidator implements InstanceValidator {
        private InstanceValidator a;
        private InstanceValidator b;

        public OrInstanceValidator(InstanceValidator a, InstanceValidator b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void checkValid(Object object, RandomObjectGeneratorContext context) {
            Exception r = null;
            try {
                a.checkValid(a, context);
            } catch (RuntimeException ex) {
                r = ex;
            }
            try {
                b.checkValid(a, context);
            } catch (RuntimeException ex) {
                if (r == null) {
                    throw ex;
                }
                throw new GenerateException(object.getClass(), "InvalidConditions(" + (r.getMessage()) + "," + ex.getMessage() + ")");
            }
        }
    }
}
