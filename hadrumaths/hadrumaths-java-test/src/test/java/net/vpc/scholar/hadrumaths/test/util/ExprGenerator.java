package net.vpc.scholar.hadrumaths.test.util;

import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.common.util.Filter;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.DoubleValidator;
import net.vpc.scholar.hadrumaths.util.IntValidator;
import net.vpc.scholar.hadrumaths.util.RandomList;
import net.vpc.scholar.hadrumaths.util.test.TestInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class ExprGenerator {
    private static Class[] EXPR_CLASSES = null;

    public static Class[] findExprClasses() {
        List<Class> clss = new ArrayList<>();
        if (EXPR_CLASSES == null) {
            for (Class cls : ClassPathUtils.resolveContextClassesList()) {
                try {
                    if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())) {
                        if (createConstructors((Class<Expr>) cls).length == 0
                                || RWG.class.equals(cls)
                                || AxisTransform.class.equals(cls)
                                || DxySum.class.equals(cls)
                                || PiecewiseSine2XFunctionXY.class.equals(cls)
                                || SinSeqXY.class.equals(cls)
                                || SinSeqXZ.class.equals(cls)
                                || SinSeqXZ.class.equals(cls)
                                || PiecewiseSineXFunctionXY.class.equals(cls)
                                || Rooftop2DFunctionXY.class.equals(cls)
                                || Any.class.equals(cls)
//                                || ComplexOld.class.equals(cls)
                                || Polyhedron.class.equals(cls)
                                || DDyIntegralX.class.equals(cls)
                                || SinSeqYZ.class.equals(cls)
                                ) {
                            System.err.println("Ignored " + cls);
                        } else {
                            clss.add(cls);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            EXPR_CLASSES = clss.toArray(new Class[clss.size()]);
        }
        return EXPR_CLASSES;
    }


    private boolean acceptBoundaryValue = true;
    private boolean acceptNaNValue = true;
    private boolean acceptInfValue = true;
    private boolean acceptInfDomain = true;
    private boolean acceptDomain1 = true;
    private boolean acceptDomain2 = true;
    private boolean acceptDomain3 = true;
    private int expressionComplexity = 4;
    private Filter<Expr> expressionClassFilter = null;
    private Filter expressionInstanceFilter = null;

    public static void main(String[] args) {
//        Maths.Config.setCacheExpressionPropertiesEnabled(false);
//        try {
//            Expr ee = (Expr) IOUtils.loadObject("/home/vpc/a.ser");
//            Expr expr = ee.getSubExpressions().get(0);
//            boolean dc = ee.isDC();
//            System.out.println(ee.toDC());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if(true){
//            return;
//        }
//        Sub sub = new Sub(Maths.X, new ComplexValue(Complex.MINUS_ONE, null));
//        System.out.println(sub.toString());
//        if (true) {
//            return;
//        }
//        System.out.println(new Linear(0,Double.NaN,1,null));
//        if (true) {
//            return;
//        }
//        for (int i = 0; i < 10; i++) {
//            Expr r = generateAny(Linear.class, null, 8);
//            System.out.println(r);
//        }
//        if (true) {
//            return;
//        }
        ExprGenerator g = new ExprGenerator();
        for (int i = 0; i < 1000; i++) {
            Expr r = g.generate(Expr.class);
            String sf = FormatFactory.format(r);
            System.out.println(sf);
            System.out.print("");
        }
    }

    public boolean isAcceptBoundaryValue() {
        return acceptBoundaryValue;
    }

    public ExprGenerator setAcceptBoundaryValue(boolean acceptBoundaryValue) {
        this.acceptBoundaryValue = acceptBoundaryValue;
        return this;
    }

    public boolean isAcceptNaNValue() {
        return acceptNaNValue;
    }

    public ExprGenerator setAcceptNaNValue(boolean acceptNaNValue) {
        this.acceptNaNValue = acceptNaNValue;
        return this;
    }

    public boolean isAcceptInfValue() {
        return acceptInfValue;
    }

    public ExprGenerator setAcceptInfValue(boolean acceptInfValue) {
        this.acceptInfValue = acceptInfValue;
        return this;
    }

    public boolean isAcceptInfDomain() {
        return acceptInfDomain;
    }

    public ExprGenerator setAcceptInfDomain(boolean acceptInfDomain) {
        this.acceptInfDomain = acceptInfDomain;
        return this;
    }

    public int getExpressionComplexity() {
        return expressionComplexity;
    }

    public ExprGenerator setExpressionComplexity(int expressionComplexity) {
        this.expressionComplexity = expressionComplexity;
        return this;
    }

    public boolean isAcceptDomain1() {
        return acceptDomain1;
    }

    public ExprGenerator setAcceptDomain1(boolean acceptDomain1) {
        this.acceptDomain1 = acceptDomain1;
        return this;
    }

    public boolean isAcceptDomain2() {
        return acceptDomain2;
    }

    public ExprGenerator setAcceptDomain2(boolean acceptDomain2) {
        this.acceptDomain2 = acceptDomain2;
        return this;
    }

    public boolean isAcceptDomain3() {
        return acceptDomain3;
    }

    public ExprGenerator setAcceptDomain3(boolean acceptDomain3) {
        this.acceptDomain3 = acceptDomain3;
        return this;
    }

    public Filter<Expr> getExpressionClassFilter() {
        return expressionClassFilter;
    }

    public ExprGenerator setExpressionClassFilter(Filter<Expr> expressionClassFilter) {
        this.expressionClassFilter = expressionClassFilter;
        return this;
    }

    public Filter getExpressionInstanceFilter() {
        return expressionInstanceFilter;
    }

    public ExprGenerator setExpressionInstanceFilter(Filter expressionInstanceFilter) {
        this.expressionInstanceFilter = expressionInstanceFilter;
        return this;
    }

    private static Expr check(Expr e, Filter<Expr> filter) {
        Expr ee = null;
        Stack<Expr> tt = new Stack<>();
        tt.push(e);
        while (!tt.empty()) {
            Expr y = tt.pop();
            if (filter.accept(y)) {
                ee = y;
            }
            for (Expr expr : y.getSubExpressions()) {
                tt.push(expr);
            }
        }
        return ee;
    }

    public DoubleToDouble generateDD() {
        Expr expr = generateAny(Expr.class, new Filter() {
            @Override
            public boolean accept(Object o) {
                return ((Expr) o).isDD();
            }
        }, expressionComplexity);
        DoubleToDouble doubleToDouble = expr.toDD();
        return doubleToDouble;
    }

    public DoubleToComplex generateDC() {
        return generateAny(Expr.class, new Filter() {
            @Override
            public boolean accept(Object o) {
                return ((Expr) o).isDC();
            }
        }, expressionComplexity).toDC();
    }

    public DoubleToVector generateDV() {
        return generateAny(Expr.class, new Filter() {
            @Override
            public boolean accept(Object o) {
                return ((Expr) o).isDV();
            }
        }, expressionComplexity).toDV();
    }

    public DoubleToMatrix generateDM() {
        return generateAny(Expr.class, new Filter() {
            @Override
            public boolean accept(Object o) {
                return ((Expr) o).isDM();
            }
        }, expressionComplexity).toDM();
    }

    public <T> T generate(Class<T> cls) {
        return generateAny(cls, expressionInstanceFilter, expressionComplexity);
    }

    private Domain generateDomain() {
        return generateAny(Domain.class, null, 1);
    }

    private <T> T generateAny(Class<T> cls, int complexity) {
        return generateAny(cls, null, complexity);
    }

    private <T> T generateAny(Class<T> cls, Filter filter, int complexity) {
        Filter filter2=new Filter() {
            @Override
            public boolean accept(Object value) {
                if(value instanceof Expr){
                    Domain d = ((Expr) value).getDomain();
                    switch (d.getDomainDimension()){
                        case 1:{
                            if(!isAcceptDomain1()){
                                return false;
                            }
                            break;
                        }
                        case 2:{
                            if(!isAcceptDomain2()){
                                return false;
                            }
                            break;
                        }
                        case 3:{
                            if(!isAcceptDomain2()){
                                return false;
                            }
                            break;
                        }
                    }
                }
                return filter == null || filter.accept(value);
            }
        };
        for (int i = 0; i < 100; i++) {
            Object o = generateAny0(cls, filter2, complexity);
            if (o == null) {
                throw new IllegalArgumentException("Why");
            }
            if (filter2.accept(o)) {
                return (T) o;
            }
        }
        for (int i = 0; i < 10; i++) {
            Object o = generateAny0(cls, filter2, complexity);
            if (o == null) {
                throw new IllegalArgumentException("Why");
            }
            if (filter2.accept(o)) {
                return (T) o;
            }
        }
        throw new IllegalArgumentException("Unable to create " + cls);
    }

    public static Class[] findExprSubClasses(Class cls) {
        List<Class> all = new ArrayList<>();
        for (Class aClass : findExprClasses()) {
            if (cls.isAssignableFrom(aClass)) {
                all.add(aClass);
            }
        }
        return all.toArray(new Class[all.size()]);
    }

    private <T> T generateAny0(Class<T> cls, Filter filter, int complexity) {
        if (complexity <= 0) {
            if (Expr.class.isAssignableFrom(cls)
                    && !Domain.class.equals(cls)
                    && !Complex.class.equals(cls)
                    && !DoubleValue.class.equals(cls)
                    && !Discrete.class.equals(cls)
                    && !DoubleParam.class.equals(cls)
                    ) {
                if (cls.isAssignableFrom(DoubleValue.class)) {
                    return (T) generateAny0(DoubleValue.class, filter, 1);
                } else if (cls.isAssignableFrom(Complex.class)) {
                    return (T) generateAny0(Complex.class, filter, 1);
                } else {
                    throw new IllegalArgumentException("Not Yet supported");
                }
            }
        }
        if (cls.equals(Expr.class)) {
            Class[] exprClasses = findExprClasses();
            for (int i = 0; i < 100; i++) {
                try {
                    return (T) generateAny(exprClasses[Maths.randomInt(exprClasses.length)], null, complexity);
                }catch (IllegalArgumentException ex){
                    //ignore
                }
            }
            throw new IllegalArgumentException("Unable to create instance of "+Arrays.asList(exprClasses));
        } else if (cls.equals(Complex.class)) {
            Double a = generateAny(Double.class, null, complexity - 1);
            Double b = generateAny(Double.class, null, complexity - 1);
            return (T) Complex.valueOf(a, b);
        } else if (cls.equals(DomainExpr.class)) {
            Double a = generateAny(Double.class, null, complexity - 1);
            Double b = generateAny(Double.class, null, complexity - 1);
            return (T) DomainExpr.forBounds(Complex.ZERO,Complex.ONE);
        } else if (cls.equals(Plus.class)) {
            return (T) new Plus(
                    generateAny(Expr.class, null, complexity - 1),
                    generateAny(Expr.class, null, complexity - 1)
            );
        } else if (cls.equals(Mul.class)) {
            return (T) new Mul(
                    generateAny(Expr.class, null, complexity - 1),
                    generateAny(Expr.class, null, complexity - 1)
            );
        } else if (cls.equals(RooftopXFunctionXY.class)) {
            return (T) new RooftopXFunctionXY(generateDomain(), Axis.X, 1, 3, false);
        } else if (cls.equals(Rooftop.class)) {
            return (T) new Rooftop(true, true, true, true, true, false, 1, 1, generateDomain());
        } else if (cls.equals(PiecewiseSineXFunctionXY.class)) {
            return (T) new PiecewiseSineXFunctionXY(generateDomain(), Axis.X, 0.5);
        } else if (cls.equals(Discrete.class)) {
            return (T) new Discrete(generateDomain(), new Complex[][][]{{{Complex.ONE}}}, new double[1], new double[1], new double[1], 0.1, 0.1, 0.1, Axis.X, Axis.Y, Axis.Z, 3);
        } else if (cls.equals(VDiscrete.class)) {
            switch (Maths.randomInt(1, 4)) {
                case 1:
                    return (T) new VDiscrete(generateAny(Discrete.class, null, complexity));
                case 2:
                    return (T) new VDiscrete(generateAny(Discrete.class, null, complexity), generateAny(Discrete.class, null, complexity));
                case 3:
                    return (T) new VDiscrete(generateAny(Discrete.class, null, complexity), generateAny(Discrete.class, null, complexity), generateAny(Discrete.class, null, complexity));
            }
            throw new IllegalArgumentException("Unsupported");
        } else if (cls.equals(DDiscrete.class)) {
            return (T) new DDiscrete(generateDomain(), new double[][][]{{{1}}}, new double[1], new double[1], new double[1], 0.1, 0.1, 0.1, Axis.X, Axis.Y, Axis.Z, 3);
        } else if (DoubleParam.class.equals(cls)) {
            return (T) new DoubleParam("p");
        } else if (cls.equals(Domain.class)) {
            RandomList rnd = new RandomList();
            rnd.addIf(isAcceptInfDomain() && isAcceptDomain1(), () -> Domain.FULLX);
            rnd.addIf(isAcceptInfDomain() && isAcceptDomain2(), () -> Domain.FULLXY);
            rnd.addIf(isAcceptInfDomain() && isAcceptDomain3(), () -> Domain.FULLXYZ);
            rnd.addIf(isAcceptDomain1(), () -> {
                double[] b = randomDomainBounds();
                return (T) Domain.forBounds(b[0], b[1]);
            });
            rnd.addIf(isAcceptDomain2(), () -> {
                double[] b1 = randomDomainBounds();
                double[] b2 = randomDomainBounds();
                return (T) Domain.forBounds(b1[0], b1[1], b2[0], b2[1]);
            });
            rnd.addIf(isAcceptDomain3(), () -> {
                double[] b1 = randomDomainBounds();
                double[] b2 = randomDomainBounds();
                double[] b3 = randomDomainBounds();
                return (T) Domain.forBounds(b1[0], b1[1], b2[0], b2[1], b3[0], b3[1]);
            });
            T random = (T) rnd.random();
            if (((Domain) random).isInfinite()) {
                System.out.println("Why");
            }
            return random;
        } else if (Expr.class.isAssignableFrom(cls)) {
            Class[] allPossibilities = findExprSubClasses(cls);
            if (allPossibilities.length == 0) {
                allPossibilities = findExprSubClasses(cls);
                throw new IllegalArgumentException("Not Found");
            }
            Filter filter2 = filter;
            if (cls.equals(DoubleToDouble.class)) {
                filter2 = new Filter() {
                    @Override
                    public boolean accept(Object value) {
                        return ((Expr) value).isDD() && (filter == null || filter.accept(value));
                    }
                };
            }
            for (int j = 0; j < 100; j++) {
                try {
                    Class cls2 = allPossibilities[Maths.randomInt(allPossibilities.length)];
                    if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
                        return (T) generateAny(cls2, filter2, complexity);
                    } else {
                        ClsConstructor[] constructors = createConstructors(cls2);
                        if (constructors.length == 0) {
                            throw new IllegalArgumentException("Unable to create Constructors for " + cls2);
                        }
                        ClsConstructor constructor = constructors[Maths.randomInt(constructors.length)];
                        ClsParam[] params = constructor.getParams();
                        Object[] args = new Object[params.length];
                        for (int i = 0; i < args.length; i++) {
                            args[i] = generateAny(params[i].getType(), params[i].getConstraints(), complexity - 1);
                        }
                        T t = (T) constructor.create(args);
                        if (t == null) {
                            t = (T) constructor.create(args);
                        }
                        return t;
                    }
                } catch (IllegalArgumentException ex) {
                    //
                }
            }
            throw new IllegalArgumentException("Unsupported....");
        } else if (Double.TYPE.equals(cls) || Double.class.equals(cls)) {
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
            return (T) rnd.random();
        } else if (Integer.TYPE.equals(cls) || Integer.class.equals(cls)) {
            RandomList rnd = new RandomList();
            rnd.addIf(true, () -> 0);
            rnd.addIf(true, () -> 1);
            rnd.addIf(true, () -> -1);
            rnd.addIf(isAcceptBoundaryValue(), () -> Integer.MIN_VALUE);
            rnd.addIf(isAcceptBoundaryValue(), () -> Integer.MAX_VALUE);
            rnd.addIf(true, 11, () -> Maths.randomInt(-5, 5));
            return (T) rnd.random();
        } else if (String.class.equals(cls)) {
            switch (Maths.randomInt(3)) {
                case 0:
                    return (T) "example1";
                case 1:
                    return (T) "example2";
                default:
                    return (T) "example3";
            }
        } else if (Boolean.TYPE.equals(cls)) {
            switch (Maths.randomInt(2)) {
                case 0:
                    return (T) Boolean.FALSE;
                default:
                    return (T) Boolean.TRUE;
            }
        } else if (CustomCCFunctionXDefinition.class.equals(cls)) {
            return (T) new CustomCCFunctionXDefinition("Example", new MyCustomCCFunctionX());
        } else if (CustomCCFunctionXYDefinition.class.equals(cls)) {
            return (T) new CustomCCFunctionXYDefinition("Example", new MyCustomCCFunctionXY());
        } else if (CustomDDFunctionXDefinition.class.equals(cls)) {
            return (T) new CustomDDFunctionXDefinition("Example", new MyCustomDDFunctionX());
        } else if (CustomDCFunctionXDefinition.class.equals(cls)) {
            return (T) new CustomDCFunctionXDefinition("Example", new MyCustomDCFunctionX());
        } else if (CustomDDFunctionXYDefinition.class.equals(cls)) {
            return (T) new CustomDDFunctionXYDefinition("Example", new MyCustomDDFunctionXY());
        } else if (CustomDCFunctionXYDefinition.class.equals(cls)) {
            return (T) new CustomDCFunctionXYDefinition("Example", new MyCustomDCFunctionXY());
        } else if (Axis.class.equals(cls)) {
            return (T) Axis.values()[Maths.randomInt(Axis.values().length)];
        } else if (cls.isEnum()) {
            return (T) cls.getEnumConstants()[Maths.randomInt(cls.getEnumConstants().length)];
        } else if (Polygon.class.equals(cls)) {
            return (T) new Polygon(new Point(0, 0), new Point(1, 0), new Point(1, 1));
        } else if (DIntegralXY.class.equals(cls)) {
            return (T) new DQuadIntegralXY();
        } else if (Geometry.class.equals(cls)) {
            return (T) generateDomain().toGeometry();
        } else if (Samples.class.equals(cls)) {
            switch (Maths.randomInt(1, 4)) {
                case 1:
                    return (T) Samples.relative(5);
                case 2:
                    return (T) Samples.relative(5, 5);
                case 3:
                    return (T) Samples.relative(5, 5, 5);
            }
            throw new IllegalArgumentException("Unsupported " + cls);
        } else {
            throw new IllegalArgumentException("Unsupported " + cls);
        }
    }

    private double[] randomDomainBounds() {
        RandomList rnd = new RandomList<double[]>();
        rnd.addIf(isAcceptInfDomain(), () -> new double[]{Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY});
        rnd.addIf(isAcceptInfDomain(), () -> new double[]{Maths.randomDouble(-5, 5), Double.POSITIVE_INFINITY});
        rnd.addIf(isAcceptInfDomain(), () -> new double[]{Double.NEGATIVE_INFINITY, Maths.randomDouble(-5, 5)});
        rnd.addIf(true, () -> {
            int ax = Maths.randomInt(-5, 5);
            int bx = ax + Maths.randomInt(0, 5);
            return new double[]{ax, bx};
        });
        return (double[]) rnd.random();
    }

    private static ClsConstructor[] createConstructors(Class<Expr> cls) {
        List<ClsConstructor> all = new ArrayList<>();
        Constructor<?>[] constructors = cls.getConstructors();
        for (Constructor<?> constructor : constructors) {
            //@TestInfo("DisableRandomCalls")
            TestInfo annotation = constructor.getAnnotation(TestInfo.class);
            if (annotation != null && new HashSet<String>(Arrays.asList(annotation.value())).contains("DisableRandomCalls")) {
                //ignore
            } else {
                all.add(new SimpleClsConstructor(constructor));
            }
        }
        for (Method method : cls.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())
                    && cls.isAssignableFrom(method.getReturnType())
                    ) {
                //@TestInfo("DisableRandomCalls")
                TestInfo annotation = method.getAnnotation(TestInfo.class);
                if (annotation != null && new HashSet<String>(Arrays.asList(annotation.value())).contains("DisableRandomCalls")) {
                    //ignore
                } else {
                    all.add(new MethodClsConstructor(method));
                }
            }
        }
        return all.toArray(new ClsConstructor[all.size()]);
    }

    private static class SimpleClsConstructor implements ClsConstructor {
        private final Constructor<?> constructor;

        public SimpleClsConstructor(Constructor<?> constructor) {
            this.constructor = constructor;
        }

        @Override
        public Object create(Object[] ags) {
            try {
                return constructor.newInstance(ags);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                e.getTargetException().printStackTrace();
                throw new IllegalArgumentException(e.getTargetException());
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public ClsParam[] getParams() {
            Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            return getClsParams(parameterAnnotations, parameterTypes);
        }
    }

    private static class DefaultClsParam implements ClsParam {
        private Class cls;
        private Filter constraints;

        public DefaultClsParam(Class cls, Filter constraints) {
            this.cls = cls;
            this.constraints = constraints;
        }

        public Class getType() {
            return cls;
        }

        public Filter getConstraints() {
            return constraints;
        }
    }

    private interface ClsParam {
        Class getType();

        Filter getConstraints();

    }

    private interface ClsConstructor {
        Object create(Object[] ags);

        ClsParam[] getParams();
    }

    private static class MethodClsConstructor implements ClsConstructor {
        private final Method method;

        public MethodClsConstructor(Method method) {
            this.method = method;
        }

        @Override
        public Object create(Object[] ags) {
            try {
                return method.invoke(null, ags);
            } catch (InvocationTargetException e) {
                throw new IllegalArgumentException(e.getTargetException());
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        @Override
        public ClsParam[] getParams() {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Class<?>[] parameterTypes = method.getParameterTypes();
            return getClsParams(parameterAnnotations, parameterTypes);
        }
    }

    private static ClsParam[] getClsParams(Annotation[][] parameterAnnotations, Class<?>[] parameterTypes) {
        ClsParam[] tt = new ClsParam[parameterTypes.length];
        for (int i = 0; i < tt.length; i++) {
            Annotation[] anns = parameterAnnotations[i];
            Filter f = anns.length == 0 ? null : new Filter() {
                @Override
                public boolean accept(Object o) {
                    for (Annotation ann : anns) {
                        if (ann instanceof DoubleValidator) {
                            DoubleValidator dv = (DoubleValidator) ann;
                            if (!dv.NaN()) {
                                if (Double.isNaN((Double) o)) {
                                    return false;
                                }
                            }
                            if (!dv.negativeInf()) {
                                if (Double.NEGATIVE_INFINITY == ((Double) o)) {
                                    return false;
                                }
                            }
                            if (!dv.positiveInf()) {
                                if (Double.POSITIVE_INFINITY == ((Double) o)) {
                                    return false;
                                }
                            }
                        }
                        if (ann instanceof IntValidator) {
                            IntValidator i = (IntValidator) ann;
                            int v = (int) o;
                            if (i.min() != Integer.MIN_VALUE) {
                                if (v < i.min()) {
                                    return false;
                                }
                            }
                            if (i.max() != Integer.MAX_VALUE) {
                                if (v > i.max()) {
                                    return false;
                                }
                            }
                        }
                    }
                    return true;
                }
            };
            tt[i] = new DefaultClsParam(parameterTypes[i], f);
        }
        return tt;
    }

    private static class MyCustomCCFunctionX implements CustomCCFunctionX {
        private static final long serialVersionUID = 1L;
        @Override
        public Complex evalComplex(Complex c) {
            return Complex.ONE;
        }
    }

    private static class MyCustomCCFunctionXY implements CustomCCFunctionXY {
        private static final long serialVersionUID = 1L;
        @Override
        public Complex evalComplex(Complex c, Complex y) {
            return Complex.ONE;
        }
    }

    private static class MyCustomDDFunctionX implements CustomDDFunctionX {
        private static final long serialVersionUID = 1L;
        @Override
        public double evalDouble(double c) {
            return 1;
        }
    }

    private static class MyCustomDCFunctionX implements CustomDCFunctionX {
        private static final long serialVersionUID = 1L;
        @Override
        public Complex evalComplex(double c) {
            return Complex.ONE;
        }
    }

    private static class MyCustomDDFunctionXY implements CustomDDFunctionXY {
        private static final long serialVersionUID = 1L;
        @Override
        public double evalDouble(double a, double c) {
            return 1;
        }
    }

    private static class MyCustomDCFunctionXY implements CustomDCFunctionXY {
        private static final long serialVersionUID = 1L;
        @Override
        public Complex evalComplex(double c, double y) {
            return Complex.ONE;
        }
    }
}
