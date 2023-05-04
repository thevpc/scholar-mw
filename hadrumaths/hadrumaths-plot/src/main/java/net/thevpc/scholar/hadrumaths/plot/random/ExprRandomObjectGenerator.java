package net.thevpc.scholar.hadrumaths.plot.random;

import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceExprType;
import net.thevpc.scholar.hadrumaths.util.internal.CanProduceClass;
import net.thevpc.common.collections.AcceptDenyEnumSet;
import net.thevpc.common.collections.ClassMapList;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.integration.DIntegralXY;
import net.thevpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.complex2complex.DefaultCustomCCFunctionXExpr;
import net.thevpc.scholar.hadrumaths.symbolic.complex2complex.DefaultCustomCCFunctionXYExpr;
import net.thevpc.scholar.hadrumaths.symbolic.conv.DefaultDoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2matrix.DefaultDoubleToMatrix;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.Any;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.ParametrizedScalarProduct;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.*;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.*;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.trigo.*;
import net.thevpc.scholar.hadrumaths.util.RandomList;
import net.thevpc.scholar.hadrumaths.Samples;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

public class ExprRandomObjectGenerator extends RandomObjectGenerator<ExprRandomObjectGenerator> {

    private ExprChecker checker;
    private boolean acceptInfDomain = true;
    private boolean acceptDomain1 = true;
    private boolean acceptDomain2 = true;
    private boolean acceptDomain3 = true;
    private AcceptDenyEnumSet<ExprType> exprTypes = AcceptDenyEnumSet.of(ExprType.class);

    public ExprRandomObjectGenerator(ExprChecker checker) {
        super(Expr.class);
        this.checker = checker;
        setErrorList(checker.getErrorList());
        addInstanceFilter(Expr.class, new InstanceValidator() {
            @Override
            public void checkValid(Object object, RandomObjectGeneratorContext context) {
                if (object instanceof Expr) {
                    Expr e = (Expr) object;
                    Domain d = e.getDomain();
                    switch (d.getDomain().getDimension()) {
                        case 1: {
                            if (!isAcceptDomain1()) {
                                throw new GenerateException(object.getClass(), "DomainX are disabled");
                            }
                            break;
                        }
                        case 2: {
                            if (!isAcceptDomain2()) {
                                throw new GenerateException(object.getClass(), "DomainXY are disabled");
                            }
                            break;
                        }
                        case 3: {
                            if (!isAcceptDomain3()) {
                                throw new GenerateException(object.getClass(), "DomainXYZ are disabled");
                            }
                            break;
                        }
                    }
                    if (!exprTypes.isAccept(e.getType())) {
                        throw new GenerateException(object.getClass(), e.getType() + " is disabled");
                    }
                }
            }
        });
        init();
    }

    public boolean isAcceptDomain1() {
        return acceptDomain1;
    }

    public ExprRandomObjectGenerator setAcceptDomain1(boolean acceptDomain1) {
        this.acceptDomain1 = acceptDomain1;
        return this;
    }

    public boolean isAcceptDomain2() {
        return acceptDomain2;
    }

    public ExprRandomObjectGenerator setAcceptDomain2(boolean acceptDomain2) {
        this.acceptDomain2 = acceptDomain2;
        return this;
    }

    public boolean isAcceptDomain3() {
        return acceptDomain3;
    }

    public ExprRandomObjectGenerator setAcceptDomain3(boolean acceptDomain3) {
        this.acceptDomain3 = acceptDomain3;
        return this;
    }

    private void init() {
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);
//        denyType(.class);

        register(Expr.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                RandomObjectGeneratorContext nextContext = context.decComplexity();
                if (cls.equals(Expr.class)) {
                    //this code for one odd reason does not work
                    //it is a java compiler issue
//                    Stream<Class> ss = Stream.of(findObjectClasses());
//                    Class[] exprClasses = ss
//                            .filter(x -> context.getClsTypes().isAccept(x))
//                            .toArray(Class[]::new);

                    List<Class> _objectClasses = new ArrayList<>();
                    for (Class x : findObjectClasses()) {
                        if (context.getClsTypes().isAccept(x)) {
                            _objectClasses.add(x);
                        }
                    }
                    Class[] exprClasses = _objectClasses.toArray(new Class[0]);

                    switch (exprClasses.length) {
                        case 0:
                            throw new GenerateException(cls, "Unable to create instance of " + Arrays.asList(exprClasses));
                        case 1: {
                            return randomObject(exprClasses[0], asType, nextContext);
                        }
                        default: {
                            return randomObject(exprClasses[Maths.randomInt(exprClasses.length)], asType, nextContext);
                        }
                    }
                } else {
                    Class[] allPossibilities = findExprSubClasses(cls);
                    if (allPossibilities.length == 0) {
                        throw new NonRepeatableException(cls, "Could not find any class for " + cls.getSimpleName() + " as " + asType.getSimpleName());
                    }
                    Class cls2 = Maths.randomArrayElement(allPossibilities);
                    if (generators.getExact(cls2) != null) {
                        return randomObjectOnce(cls2, asType, context);
                    }
                    ClsConstructor[] constructors = getConstructors(cls2, Expr.class);
                    if (constructors.length == 0) {
                        throw new NonRepeatableException(cls, "Unable to create Constructors for " + cls2.getSimpleName());
                    }
                    ClsConstructor constructor = Maths.randomArrayElement(constructors);
                    Object t = generateWithConstructor(constructor, nextContext);
                    if (cls.isInstance(t)) {
                        return t;
                    }
                    throw new NonRepeatableException(cls, "Cannot create " + cls2.getSimpleName() + " as " + cls.getSimpleName() + " (" + asType.getSimpleName() + ")");
                }
            }
        });

        register(Sqrtn.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                try {
                    Method m = cls.getDeclaredMethod("of", Expr.class, Integer.TYPE);
                    if (asType.equals(Expr.class)) {
                        return m.invoke(null, Maths.X, 1);
                    }
                    if (asType.equals(DoubleToDouble.class)) {
                        return (DoubleToDouble) m.invoke(null, randomObject(DoubleToDouble.class, null, context.decComplexity()), 1);
                    }
                    if (asType.equals(DoubleToComplex.class)) {
                        return (DoubleToComplex) m.invoke(null, randomObject(DoubleToComplex.class, null, context.decComplexity()), 1);
                    }
                    if (asType.equals(DoubleToVector.class)) {
                        return (DoubleToVector) m.invoke(null, randomObject(DoubleToVector.class, null, context.decComplexity()), 1);
                    }
                    if (asType.equals(DoubleToMatrix.class)) {
                        return (DoubleToMatrix) m.invoke(null, randomObject(DoubleToMatrix.class, null, context.decComplexity()), 1);
                    }
                    return m.invoke(null, Maths.X, 1);
                } catch (Exception e) {
                    throw new NonRepeatableException(cls, "Cannot create " + cls.getSimpleName() + " as (" + asType.getSimpleName() + "):" + e.toString(), e);
                }
            }
        });

        for (Class oneArgFunction : new Class[]{Abs.class, Acos.class, Acosh.class, Acotan.class, Arg.class,
            Asin.class, Asinh.class, Atan.class, Conj.class, Cos.class, Cosh.class, Cotan.class, Cotanh.class,
            Db.class, Db2.class, Exp.class, Inv.class, Log.class, Log10.class,
            Sin.class, Sincard.class, Sinh.class, Sqr.class, Sqrt.class, Tan.class, Tanh.class,
            Neg.class
        }) {
            register(oneArgFunction, new Generator() {
                @Override
                public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                    try {
                        Class asType2 = asType;
                        if (asType2.equals(Expr.class) || asType2.equals(oneArgFunction)) {
                            asType2 = Maths.randomArrayElement(getProducedExprInterfaces(cls));
                        }
                        Method m = cls.getDeclaredMethod("of", Expr.class);
                        if (asType2.equals(Expr.class)) {
                            return m.invoke(null, Maths.X);
                        }
                        if (asType2.equals(DoubleToDouble.class)) {
                            return invoke(cls, asType2, m, DoubleToDouble.class, context.decComplexity());
                        }
                        if (asType2.equals(DoubleToComplex.class)) {
                            return invoke(cls, asType2, m, DoubleToComplex.class, context.decComplexity());
                        }
                        if (asType2.equals(DoubleToVector.class)) {
                            return invoke(cls, asType2, m, DoubleToVector.class, context.decComplexity());
                        }
                        if (asType2.equals(DoubleToMatrix.class)) {
                            return invoke(cls, asType2, m, DoubleToMatrix.class, context.decComplexity());
                        }
                        throw new GenerateException(cls, "Unable to generate " + cls.getSimpleName() + " as " + asType.getSimpleName());
                    } catch (GenerateException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new NonRepeatableException(cls, "Cannot create " + cls.getSimpleName() + " as (" + asType.getSimpleName() + ")" + e.toString(), e);
                    }
                }

                private <T> T invoke(Class cls, Class asType, Method m, Class<T> type, RandomObjectGeneratorContext context) throws Exception {
                    Object r = m.invoke(null, randomObject(type, null, context));
                    if (asType.isInstance(r)) {
                        return (T) r;
                    }
                    throw new GenerateException(cls, "Cannot create " + cls.getSimpleName() + ". Result was " + r.getClass().getSimpleName() + ". Was expecting " + cls.getSimpleName() + " as " + asType.getSimpleName());
                }
            });
        }
        register(Any.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                try {
                    return Any.of((Expr) randomObject(asType, null, context.decComplexity()));
                } catch (GenerateException e) {
                    throw e;
                } catch (Exception e) {
                    throw new NonRepeatableException(cls, "Cannot create " + cls.getSimpleName() + " as (" + asType.getSimpleName() + ")" + e.toString(), e);
                }
            }
        });

        for (Class twoArgsFunction : new Class[]{Div.class, Pow.class, Reminder.class, Minus.class
        }) {
            register(twoArgsFunction, new Generator() {
                @Override
                public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                    try {

                        Class asType2 = asType;
                        Method m = cls.getDeclaredMethod("of", Expr.class, Expr.class);
                        if (asType2.equals(Expr.class) || asType2.equals(twoArgsFunction)) {
                            asType2 = Maths.randomArrayElement(getProducedExprInterfaces(cls));
                        }
                        if (asType2.equals(DoubleToDouble.class)) {
                            return invoke(cls, asType2, m, DoubleToDouble.class, DoubleToDouble.class, context.decComplexity());
                        }
                        if (twoArgsFunction.equals(DoubleToComplex.class) || asType2.equals(DoubleToComplex.class)) {
                            return invoke(cls, asType2, m, DoubleToComplex.class, DoubleToDouble.class, context.decComplexity());
                        }
                        if (asType2.equals(DoubleToVector.class)) {
                            return invoke(cls, asType2, m, DoubleToVector.class, DoubleToDouble.class, context.decComplexity());
                        }
                        if (asType2.equals(DoubleToMatrix.class)) {
                            return invoke(cls, asType2, m, DoubleToMatrix.class, DoubleToDouble.class, context.decComplexity());
                        }
                        throw new GenerateException(cls, "Unable to generate " + cls.getSimpleName() + " as " + asType.getSimpleName());
                    } catch (GenerateException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new NonRepeatableException(cls, "Cannot create " + cls.getSimpleName() + " as (" + asType.getSimpleName() + ") : " + e.toString(), e);
                    }
                }

                private <T> T invoke(Class<T> cls, Class asType, Method m, Class type1, Class type2, RandomObjectGeneratorContext context) throws Exception {
                    Object r = m.invoke(null, randomObject(type1, null, context), randomObject(type2, null, context));
                    if (asType.isInstance(r)) {
                        return (T) r;
                    }
                    r = m.invoke(null, randomObject(type1, null, context), randomObject(type2, null, context));
                    if (asType.isInstance(r)) {
                        return (T) r;
                    }
                    throw new GenerateException(cls, "Cannot create " + cls.getSimpleName() + ". Result was " + r.getClass().getSimpleName() + ". Was expecting " + cls.getSimpleName());
                }
            });
        }

        register(Complex.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Double a = randomObject(Double.class, null, context.decComplexity());
                Double b = randomObject(Double.class, null, context.decComplexity());
                return Complex.of(a, b);
            }
        });
        register(DomainExpr.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Double a = randomObject(Double.class, null, context.decComplexity());
                Double b = randomObject(Double.class, null, context.decComplexity());
                return DomainExpr.ofBounds(Complex.ZERO, Complex.ONE);
            }
        });
        register(Plus.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Class aClass = resolveClass(asType);
                if (aClass.equals(DoubleToVector.class)) {
                    Expr a = (Expr) randomObject(aClass, aClass, context.decComplexity());
                    Expr b = a.mul(2).plus(Maths.X);
                    return Maths.add(a, b);
                }
                if (aClass.equals(DoubleToMatrix.class)) {
                    Expr a = (Expr) randomObject(aClass, aClass, context.decComplexity());
                    Expr b = DefaultDoubleToMatrix.of(a.toDM()).mul(2).plus(Maths.X);
                    return Maths.add(a, b);
                }
                Expr a = (Expr) randomObject(aClass, aClass, context.decComplexity());
                Expr b = (Expr) randomObject(aClass, aClass, context.decComplexity());
                return Maths.add(a, b);
            }
        });
        register(Mul.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Class aClass = resolveClass(asType);
                Expr a = (Expr) randomObject(aClass, aClass, context.decComplexity());
                Expr b;
                if (a instanceof DoubleToVector) {
                    b = a;
                } else if (a instanceof DoubleToMatrix) {
                    DoubleToMatrix m = (DoubleToMatrix) a;
                    b = DefaultDoubleToMatrix.of(m).transpose();
                } else {
                    b = (Expr) randomObject(aClass, aClass, context.decComplexity());
                }
                return Maths.mul(a, b);
            }
        });
        register(RooftopXFunctionXY.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new RooftopXFunctionXY(randomDomain(context.decComplexity()), Axis.X, 1, 3, false);
            }
        });
        register(Rooftop.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new Rooftop("NEWSX", 1, 1, randomDomain(context.decComplexity()));
            }
        });
        register(PiecewiseSineXFunctionXY.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new PiecewiseSineXFunctionXY(randomDomain(context.decComplexity()), Axis.X, 0.5);
            }
        });
        register(CDiscrete.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Domain domain = randomDomain(context.decComplexity());
                return CDiscrete.of(domain, new Complex[][][]{{{Complex.ONE}}});
            }
        });
        register(VDiscrete.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                switch (Maths.randomInt(1, 4)) {
                    case 1:
                        return new VDiscrete(randomObject(CDiscrete.class, null, context.decComplexity()));
                    case 2:
                        return new VDiscrete(randomObject(CDiscrete.class, null, context.decComplexity()), randomObject(CDiscrete.class, null, context.decComplexity()));
                    case 3:
                        return new VDiscrete(randomObject(CDiscrete.class, null, context.decComplexity()), randomObject(CDiscrete.class, null, context.decComplexity()), randomObject(CDiscrete.class, null, context.decComplexity()));
                }
                throw new IllegalArgumentException("Unsupported");
            }
        });
        register(DDiscrete.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Domain d = randomDomain(context.decComplexity());
                double[][][] a = null;
                switch (d.dimension()) {
                    case 1: {
                        a = new double[][][]{{{1, 2, 3}}};
                        break;
                    }
                    case 2: {
                        a = new double[][][]{{{1, 2, 3}, {1, 2, 3}}};
                        break;
                    }
                    case 3: {
                        a = new double[][][]{{{1, 2, 3}, {1, 2, 3}}, {{1, 2, 3}, {1, 2, 3}}};
                        break;
                    }
                }
                return DDiscrete.of(d, a);
            }
        });
        register(DoubleParam.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return Maths.param("p");
            }
        });
        register(Domain.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                ExprRandomObjectGenerator g = context.getGenerator();
                RandomList rnd = new RandomList();
                rnd.addIf(isAcceptInfDomain() && g.isAcceptDomain1(), () -> Domain.FULLX);
                rnd.addIf(isAcceptInfDomain() && g.isAcceptDomain2(), () -> Domain.FULLXY);
                rnd.addIf(isAcceptInfDomain() && g.isAcceptDomain3(), () -> Domain.FULLXYZ);
                rnd.addIf(g.isAcceptDomain1(), () -> {
                    double[] b = randomDomainBounds();
                    return Domain.ofBounds(b[0], b[1]);
                });
                rnd.addIf(g.isAcceptDomain2(), () -> {
                    double[] b1 = randomDomainBounds();
                    double[] b2 = randomDomainBounds();
                    return Domain.ofBounds(b1[0], b1[1], b2[0], b2[1]);
                });
                rnd.addIf(g.isAcceptDomain3(), () -> {
                    double[] b1 = randomDomainBounds();
                    double[] b2 = randomDomainBounds();
                    double[] b3 = randomDomainBounds();
                    return Domain.ofBounds(b1[0], b1[1], b2[0], b2[1], b3[0], b3[1]);
                });
                Object random = rnd.random();
                if (((Domain) random).isInfinite()) {
                    System.out.println("Why");
                }
                return random;
            }
        });
        register(Expr[].class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                int r = Maths.randomInt(1, 3);
                Expr[] rr = new Expr[r];
                for (int i = 0; i < rr.length; i++) {
                    rr[i] = randomExpr(context.decComplexity());
                }
                return rr;
            }
        });
        register(DoubleToComplex[][].class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                int r = Maths.randomInt(1, 2);
                DoubleToComplex[][] rr = new DoubleToComplex[r][r];
                for (int i = 0; i < rr.length; i++) {
                    for (int j = 0; j < rr.length; j++) {
                        rr[i][j] = randomDC(context.decComplexity());
                    }
                }
                return rr;
            }
        });
        register(DoubleToComplex[].class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                int r = Maths.randomInt(1, 2);
                DoubleToComplex[] rr = new DoubleToComplex[r];
                for (int i = 0; i < rr.length; i++) {
                    rr[i] = randomDC(context.decComplexity());
                }
                return rr;
            }
        });

        register(Geometry.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                ClassMapList<InstanceValidator> filter2 = new ClassMapList<>(InstanceValidator.class);
                filter2.add(Domain.class, new InstanceValidator() {
                    @Override
                    public void checkValid(Object object, RandomObjectGeneratorContext context) {
                        Domain o = (Domain) object;
                        if (o.dimension() != 2) {
                            throw new GenerateException(object.getClass(), "Required DomainXY for Geometry");
                        }
                        if (o.isInfinite()) {
                            throw new GenerateException(object.getClass(), "Required Finite DomainXY for Geometry");
                        }
                    }
                });
                Domain o = randomObject(Domain.class, null, context.setFilter(filter2));
                return o.toGeometry();
            }
        }
        );
        register(CustomCCFunctionXDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomCCFunctionXDefinition("CustomCCX", new MyFunctionCCX());
            }
        }
        );
        register(CustomCCFunctionXYDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomCCFunctionXYDefinition("CustomCCXY", new MyFunctionCCXY());
            }
        }
        );
        register(CustomDDFunctionXDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDDFunctionXDefinition("CustomDDX", new MyCustomDDFunctionX());
            }
        }
        );
        register(CustomDDFunctionXYDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDDFunctionXYDefinition("CustomDDXY", new MyCustomDDFunctionXY());
            }
        }
        );
        register(CustomDCFunctionXDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDCFunctionXDefinition("CustomDCX", new MyFunctionDCX());
            }
        }
        );
        register(CustomDCFunctionXYDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDCFunctionXYDefinition("CustomDCXY", new MyFunctionDCXY());
            }
        }
        );
        register(Samples.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                switch (Maths.randomInt(1, 4)) {
                    case 1:
                        return Samples.relative(5);
                    case 2:
                        return Samples.relative(5, 5);
                    case 3:
                        return Samples.relative(5, 5, 5);
                }
                throw new IllegalArgumentException("Unsupported " + cls);
            }
        }
        );
        register(DIntegralXY.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new DQuadIntegralXY();
            }
        }
        );
        register(Polygon.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return GeometryFactory.createPolygon(new Point(0, 0), new Point(1, 0), new Point(1, 1));
            }
        }
        );
        register(Enum.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return cls.getEnumConstants()[Maths.randomInt(cls.getEnumConstants().length)];
            }
        }
        );
        register(CustomDDFunctionXDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDDFunctionXDefinition("CustomDDX", new MyCustomDDFunctionX());
            }
        }
        );
        register(CustomDDFunctionXYDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDDFunctionXYDefinition("CustomDDXY", new MyCustomDDFunctionXY());
            }
        }
        );
        register(CustomDCFunctionXYDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDCFunctionXYDefinition("CustomDCXY", new MyFunctionDCXY());
            }
        }
        );
        register(CustomDCFunctionXDefinition.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                return new CustomDCFunctionXDefinition("CustomDCX", new MyFunctionDCX());
            }
        }
        );
        register(IfExpr.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Class aClass = resolveClass(asType);
                ExprRandomObjectGenerator g = context.getGenerator();
                return IfExpr.of(
                        g.randomDD(context.decComplexity()),
                        (Expr) g.randomObjectOnce(aClass, null, context.decComplexity()),
                        (Expr) g.randomObjectOnce(aClass, null, context.decComplexity())
                );
            }
        }
        );
        register(NotExpr.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                RandomObjectGeneratorContext g2 = context.decComplexity();
                ExprRandomObjectGenerator eg = context.getGenerator();
                return NotExpr.of(eg.randomDD(context.decComplexity()));
            }
        }
        );
        register(ParametrizedScalarProduct.class, new Generator() {
            @Override
            public Object generate(Class cls, Class asType, RandomObjectGeneratorContext context) {
                Class t = resolveClass(asType, DoubleToDouble.class, DoubleToComplex.class);
                if (t.equals(DoubleToDouble.class)) {
                    switch (Maths.randomInt(0, 2)) {
                        case 0: {
                            Expr a = generateSimpleDD(context);
                            Expr b = generateSimpleDD(context);
                            return ParametrizedScalarProduct.of(a, b);
                        }
                        case 1: {
                            Expr a = generateSimpleDD(context);
                            Expr b = generateSimpleDD(context);
                            Expr c = generateSimpleDD(context);
                            Expr d = generateSimpleDD(context);
                            return ParametrizedScalarProduct.of(Maths.vector(a, b), Maths.vector(c, d));
                        }
                    }
                }
                if (t.equals(DoubleToComplex.class)) {
                    switch (Maths.randomInt(0, 2)) {
                        case 0: {
                            Expr a = generateSimpleDC(context);
                            Expr b = generateSimpleDC(context);
                            return ParametrizedScalarProduct.of(a, b);
                        }
                        case 1: {
                            Expr a = generateSimpleDC(context);
                            Expr b = generateSimpleDC(context);
                            Expr c = generateSimpleDC(context);
                            Expr d = generateSimpleDC(context);
                            return ParametrizedScalarProduct.of(Maths.vector(a, b), Maths.vector(c, d));
                        }
                    }
                }
                throw new IllegalArgumentException("Unsupported");
            }

            Expr generateSimpleDD(RandomObjectGeneratorContext context) {
                return (Expr) context.getGenerator().randomObject(
                        Maths.randomArrayElement(new Class[]{CosXCosY.class, Linear.class, DefaultDoubleValue.class, DoubleExpr.class}),
                        null, context);
            }

            Expr generateSimpleDC(RandomObjectGeneratorContext context) {
                return DefaultDoubleToComplex.of(generateSimpleDD(context).toDD(), generateSimpleDD(context).toDD());
            }
        }
        );
    }

    protected <T> T randomObjectOnce(Class<T> cls, Class asType, RandomObjectGeneratorContext context) {
        if (context.getComplexity() <= 0) {
            if (Expr.class.isAssignableFrom(cls)
                    && !DefaultDoubleValue.class.equals(cls)
                    && !DoubleExpr.class.equals(cls)
                    && !Domain.class.equals(cls)
                    && !Complex.class.equals(cls)
                    && !DoubleValue.class.equals(cls)
                    && !ComplexValue.class.equals(cls)
                    && !DefaultComplexValue.class.equals(cls)
                    && !DDiscrete.class.equals(cls)
                    && !CDiscrete.class.equals(cls)
                    && !VDiscrete.class.equals(cls)
                    && !DoubleParam.class.equals(cls)
                    && !XX.class.equals(cls)
                    && !YY.class.equals(cls)
                    && !ZZ.class.equals(cls)
                    && !Shape2D.class.equals(cls)
                    && !CosXCosY.class.equals(cls)
                    && !CosXPlusY.class.equals(cls)
                    && !DefaultCustomDDFunctionXExpr.class.equals(cls)
                    && !DefaultCustomDDFunctionXYExpr.class.equals(cls)
                    && !DefaultCustomDCFunctionXYExpr.class.equals(cls)
                    && !DefaultCustomDCFunctionXExpr.class.equals(cls)
                    && !DefaultCustomCCFunctionXYExpr.class.equals(cls)
                    && !DefaultCustomCCFunctionXExpr.class.equals(cls)
                    && !UFunction.class.equals(cls)
                    && !Linear.class.equals(cls)
                    && !CExp.class.equals(cls)) {
                if (cls.equals(Expr.class)) {
                    return (T) Maths.TWO;
                }
                if (cls.equals(DoubleToDouble.class)) {
                    return (T) Maths.TWO;
                }
                if (cls.equals(DoubleToComplex.class)) {
                    return (T) Complex.TWO;
                }
                if (cls.equals(DoubleToVector.class)) {
                    return (T) Complex.TWO.toDV();
                }
                if (cls.equals(Sin.class)) {
                    return (T) Sin.of(Complex.ONE);
                }
                if (cls.equals(Cos.class)) {
                    return (T) Cos.of(Complex.ONE);
                }
                if (cls.equals(Tan.class)) {
                    return (T) Tan.of(Complex.ONE);
                }
                if (cls.equals(Atan.class)) {
                    return (T) Atan.of(Complex.ONE);
                }
                if (cls.equals(Arg.class)) {
                    return (T) Arg.of(Complex.ONE);
                }
                if (cls.equals(Neg.class)) {
                    return (T) Neg.of(Complex.ONE);
                }
                if (cls.equals(Log.class)) {
                    return (T) Log.of(Complex.ONE);
                }
                if (cls.equals(Log10.class)) {
                    return (T) Log10.of(Complex.ONE);
                }
                if (cls.equals(Sinh.class)) {
                    return (T) Sinh.of(Complex.ONE);
                }
                if (cls.equals(Asin.class)) {
                    return (T) Asin.of(Complex.ONE);
                }
                if (cls.equals(Inv.class)) {
                    return (T) Inv.of(Complex.ONE);
                }
                if (cls.equals(Db.class)) {
                    return (T) Db.of(Complex.ONE);
                }
                if (cls.equals(Sqr.class)) {
                    return (T) Sqr.of(Complex.ONE);
                }
                if (cls.equals(Cotan.class)) {
                    return (T) Cotan.of(Complex.ONE);
                }
                if (cls.equals(Conj.class)) {
                    return (T) Conj.of(Complex.ONE);
                }
                if (cls.equals(Sqrt.class)) {
                    return (T) Sqrt.of(Complex.ONE);
                }
                if (cls.equals(Sqrtn.class)) {
                    return (T) Sqrtn.of(Complex.ONE, 1);
                }
                if (cls.equals(Db2.class)) {
                    return (T) Db2.of(Complex.ONE);
                }
                if (cls.equals(Reminder.class)) {
                    return (T) Reminder.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(Pow.class)) {
                    return (T) Pow.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(GtExpr.class)) {
                    return (T) GtExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(GteExpr.class)) {
                    return (T) GteExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(LteExpr.class)) {
                    return (T) LteExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(LtExpr.class)) {
                    return (T) LtExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(EqExpr.class)) {
                    return (T) EqExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(OrExpr.class)) {
                    return (T) OrExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(NeExpr.class)) {
                    return (T) NeExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(Div.class)) {
                    return (T) Div.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(Plus.class)) {
                    return (T) Plus.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(Mul.class)) {
                    return (T) Mul.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(Minus.class)) {
                    return (T) Minus.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(AndExpr.class)) {
                    return (T) AndExpr.of(Maths.TWO, Maths.ONE);
                }
                if (cls.equals(NotExpr.class)) {
                    return (T) NotExpr.of(Maths.TWO);
                }
                if (cls.equals(Tanh.class)) {
                    return (T) Tanh.of(Maths.TWO);
                }
                if (cls.equals(DDzIntegralXY.class)) {
                    return (T) new DDzIntegralXY(Maths.TWO);
                }
                if (cls.equals(Acotan.class)) {
                    return (T) Acotan.of(Maths.TWO);
                }
                if (cls.equals(Cosh.class)) {
                    return (T) Cosh.of(Maths.TWO);
                }
                if (cls.equals(Asinh.class)) {
                    return (T) Asinh.of(Maths.TWO);
                }
                if (cls.equals(Sincard.class)) {
                    return (T) Sincard.of(Maths.TWO);
                }
                if (cls.equals(Acosh.class)) {
                    return (T) Acosh.of(Maths.TWO);
                }
                if (cls.equals(Acos.class)) {
                    return (T) Acos.of(Maths.TWO);
                }
                if (cls.equals(Cotanh.class)) {
                    return (T) Cotanh.of(Maths.TWO);
                }
                if (cls.equals(Real.class)) {
                    return (T) new Real(Complex.ONE);
                }
                if (cls.equals(Exp.class)) {
                    return (T) Exp.of(Complex.ONE);
                }
                if (cls.equals(Imag.class)) {
                    return (T) new Imag(Complex.I);
                }
                if (cls.equals(Abs.class)) {
                    return (T) Abs.of(Maths.TWO);
                }
                if (cls.equals(DomainExpr.class)) {
                    return (T) DomainExpr.ofBounds(Maths.ONE, Maths.TWO);
                }
//                if (cls.equals(FixedAxisZFunction.class)) {
//                    return (T) new FixedAxisZFunction(Maths.ONE, 1);
//                }
                if (cls.equals(DefaultDoubleToVector.class)) {
                    return (T) DefaultDoubleToVector.of(Complex.ONE);
                }
                if (cls.equals(DefaultDoubleToComplex.class)) {
                    return (T) new DefaultDoubleToComplex(Maths.ONE);
                }
                if (cls.equals(DefaultDoubleToMatrix.class)) {
                    return (T) DefaultDoubleToMatrix.of(Complex.ONE);
                }
                if (cls.equals(DefaultDoubleToMatrix.class) || cls.equals(DoubleToMatrix.class)) {
                    return (T) DefaultDoubleToMatrix.of(Complex.ONE);
                }
                if (cls.equals(ParametrizedScalarProduct.class)) {
                    return (T) ParametrizedScalarProduct.of(
                            Complex.ONE.mul(Domain.ofBounds(0, 1)),
                            Complex.ONE.mul(Domain.ofBounds(0, 1))
                    );
                }
                if (cls.equals(Rooftop.class)) {
                    return (T) new Rooftop("NS", 2, 2, randomDomain(2, context));
                }
                if (cls.equals(IfExpr.class)) {
                    return (T) IfExpr.of(Maths.DZEROX, Maths.X, Maths.Y);
                }
                if (cls.equals(Any.class)) {
                    return (T) Any.of(Maths.X);
                }
                throw new NonRepeatableException(cls, "Cannot create " + cls + " with complexity 0");
            }
        }
        return super.randomObjectOnce(cls, asType, context);
    }

    protected Object generateWithConstructor(ClsConstructor constructor, RandomObjectGeneratorContext context) {
        ClsParam[] params = constructor.getParams();
        Object[] args = new Object[params.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = generateWithParam(params[i], context);
        }
        Object t = constructor.create(args);
        if (t == null) {
            t = constructor.create(args);
        }
        return t;
    }

    public Class[] getProducedExprInterfaces(Class current, Class... possibilities) {
        if (possibilities.length == 0) {
            possibilities = new Class[]{DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class};
        }
        Set<Class> poss = new HashSet<>(Arrays.asList(possibilities));
        Set<Class> all = new HashSet<>();
        for (Class possibility : possibilities) {
            if (possibility.isAssignableFrom(current)) {
                all.add(possibility);
            }
        }
        CanProduceClass ann = (CanProduceClass) current.getAnnotation(CanProduceClass.class);
        if (ann != null) {
            for (Class ac : ann.value()) {
                if (poss.contains(ac)) {
                    all.add(ac);
                }
            }
        }
        return all.toArray(new Class[0]);
    }

    public static Class resolveClass(Class asType, Class... from) {
        if (from.length == 0) {
            from = new Class[]{DoubleToDouble.class, DoubleToComplex.class, DoubleToVector.class, DoubleToMatrix.class};
        }
        for (Class aClass : from) {
            if (asType.equals(aClass)) {
                return aClass;
            }
        }
        return Maths.randomArrayElement(from);
    }

    private Domain randomDomain(RandomObjectGeneratorContext context) {
        return randomObject(Domain.class, null, context);
    }

    public boolean isAcceptInfDomain() {
        return acceptInfDomain;
    }

    public ExprRandomObjectGenerator setAcceptInfDomain(boolean acceptInfDomain) {
        this.acceptInfDomain = acceptInfDomain;
        return this;
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

    protected Expr randomExpr(RandomObjectGeneratorContext context) {
        return randomObject(Expr.class, null, context);
    }

    protected DoubleToComplex randomDC(RandomObjectGeneratorContext context) {
        return randomObject(DoubleToComplex.class, null, context).toDC();
    }

    protected DoubleToDouble randomDD(RandomObjectGeneratorContext context) {
        DoubleToDouble expr = randomObject(DoubleToDouble.class, null, context);
        DoubleToDouble e = expr.toDD();
        if (!e.is(ExprType.DOUBLE_DOUBLE) && !e.is(ExprType.DOUBLE_EXPR) && !e.is(ExprType.DOUBLE_NBR)) {
            throw new IllegalArgumentException(" Why Not DD :: " + e);
        }
        return e;
    }

    protected Domain randomDomain(int i, RandomObjectGeneratorContext context) {
        return randomObject(Domain.class, null, context.addFilter(Domain.class, new DomainDimensionInstanceValidator(i)));
    }

    protected Object generateWithParam(ClsParam param, RandomObjectGeneratorContext context) {
        RandomObjectGeneratorContext nextContext2 = context;
        for (Map.Entry<Class, InstanceValidator> ee : param.getConstraints().entrySet()) {
            nextContext2 = nextContext2.addFilter(ee.getKey(), ee.getValue());
        }
        return randomObject(param.getType(), null, nextContext2);
    }

    protected <T> void validateRandomGeneration(T object, RandomObjectGeneratorContext context) {
        if (object instanceof Expr) {
            checker.checkExpression((Expr) object);
        }
    }

    protected boolean isAssignableClass(Class required, Class current) {
        if (required.isAssignableFrom(current)) {
            return true;
        }
        return isAssignableClassByCanProduceClass(required, current)
                && isAssignableClassByCanProduceExprType(required, current);
    }

    private boolean isAssignableClassByCanProduceClass(Class required, Class current) {
        CanProduceClass ann = (CanProduceClass) current.getAnnotation(CanProduceClass.class);
        if (ann == null) {
            return false;
        }
        for (Class ac : ann.value()) {
            if (required.isAssignableFrom(ac)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAssignableClassByCanProduceExprType(Class required, Class current) {
        EnumSet<ExprType> currentTypes = EnumSet.noneOf(ExprType.class);
        if (DoubleValue.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.DOUBLE_EXPR);
            currentTypes.add(ExprType.DOUBLE_DOUBLE);
        }
        if (Complex.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.COMPLEX_NBR);
        }
        if (ComplexValue.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.COMPLEX_EXPR);
            currentTypes.add(ExprType.DOUBLE_COMPLEX);
        }
        if (DoubleToDouble.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.DOUBLE_DOUBLE);
        }
        if (DoubleToComplex.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.DOUBLE_COMPLEX);
        }
        if (DoubleToVector.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.DOUBLE_CVECTOR);
        }
        if (DoubleToMatrix.class.isAssignableFrom(current)) {
            currentTypes.add(ExprType.DOUBLE_CMATRIX);
        }
        CanProduceExprType ann = (CanProduceExprType) current.getAnnotation(CanProduceExprType.class);
        if (ann != null) {
            currentTypes.addAll(Arrays.asList(ann.value()));
        }
        for (ExprType currentType : currentTypes) {
            if (exprTypes.isAccept(currentType)) {
                return true;
            }
        }
        return currentTypes.isEmpty();
    }

    protected static Expr check(Expr e, Predicate<Expr> filter) {
        Expr ee = null;
        Stack<Expr> tt = new Stack<>();
        tt.push(e);
        while (!tt.empty()) {
            Expr y = tt.pop();
            if (filter.test(y)) {
                ee = y;
            }
            for (Expr expr : y.getChildren()) {
                tt.push(expr);
            }
        }
        return ee;
    }

    public Expr randomExpr() {
        return randomExpr(createContext());
    }

    public DoubleToDouble randomDD() {
        return randomDD(createContext());
    }

    public DoubleToComplex randomDC() {
        return randomDC(createContext());
    }

    public DoubleToVector randomDV() {
        return randomDV(createContext());
    }

    protected DoubleToVector randomDV(RandomObjectGeneratorContext context) {
        return randomObject(DoubleToVector.class, null, context).toDV();
    }

    public DoubleToMatrix randomDM() {
        return randomDM(createContext());
    }

    protected DoubleToMatrix randomDM(RandomObjectGeneratorContext context) {
        return randomObject(DoubleToMatrix.class, null, context).toDM();
    }

    public ExprRandomObjectGenerator acceptType(ExprType a) {
        exprTypes.accept(a);
        return this;
    }

    public ExprRandomObjectGenerator denyType(ExprType a) {
        exprTypes.deny(a);
        return this;
    }

    private static class MyFunctionCCX implements FunctionCCX {

        private static final long serialVersionUID = 1L;

        @Override
        public Complex eval(Complex c) {
            return Complex.ONE;
        }
    }

    private static class MyFunctionCCXY implements FunctionCCXY {

        private static final long serialVersionUID = 1L;

        @Override
        public Complex eval(Complex c, Complex y) {
            return Complex.ONE;
        }
    }

    private static class MyCustomDDFunctionX implements FunctionDDX {

        private static final long serialVersionUID = 1L;

        @Override
        public double eval(double c) {
            return 1;
        }
    }

    private static class MyFunctionDCX implements FunctionDCX {

        private static final long serialVersionUID = 1L;

        @Override
        public Complex eval(double c) {
            return Complex.ONE;
        }
    }

    private static class MyCustomDDFunctionXY implements FunctionDDXY {

        private static final long serialVersionUID = 1L;

        @Override
        public double eval(double a, double c) {
            return 1;
        }
    }

    private static class MyFunctionDCXY implements FunctionDCXY {

        private static final long serialVersionUID = 1L;

        @Override
        public Complex eval(double c, double y) {
            return Complex.ONE;
        }
    }

    private static class ExprTypeInstanceValidator implements InstanceValidator {

        private EnumSet<ExprType> accepted = EnumSet.noneOf(ExprType.class);

        public ExprTypeInstanceValidator(ExprType... accepted) {
            this.accepted.addAll(Arrays.asList(accepted));
        }

        @Override
        public void checkValid(Object object, RandomObjectGeneratorContext context) {
            Expr e = (Expr) object;
            for (ExprType exprType : accepted) {
                if (e.is(exprType)) {
                    return;
                }
            }
            throw new GenerateException(object.getClass(), "Required " + accepted);
        }
    }

    private static class DomainDimensionInstanceValidator implements InstanceValidator {

        private int dim;

        public DomainDimensionInstanceValidator(int dim) {
            this.dim = dim;
        }

        @Override
        public void checkValid(Object object, RandomObjectGeneratorContext context) {
            if (((Domain) object).dimension() != dim) {
                throw new GenerateException(object.getClass(), "Required dimension " + dim);
            }
        }
    }
}
