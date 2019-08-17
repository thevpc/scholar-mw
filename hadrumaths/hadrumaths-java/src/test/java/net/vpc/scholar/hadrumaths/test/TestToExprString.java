package net.vpc.scholar.hadrumaths.test;

import net.vpc.common.classpath.ClassPathUtils;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadruplot.Samples;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.PlatformUtils;

import java.lang.reflect.*;
import java.util.*;

public class TestToExprString {
    public static void main(String[] args) {
        for (Class cls : ClassPathUtils.resolveContextClassesList()) {
            try {
                if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                    try {
                        PlatformUtils.requireEqualsAndHashCode(cls);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
//                    List<Object> instances = createInstances(cls, new HashMap<>(), new HashSet<>());
//                    for (Object instance : instances) {
//                        String value = instance.toString();
//                        if(value.contains("Infinityi")){
//                            System.out.println("");
//                        }
//                        System.out.println(instance.getClass().getName()+" :: "+instance.toString());
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Object[]> createInstances(Class[] cls, Map<Class, List<Object>> visited, Set<Class> onHold) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object[][] possibleValuesByArgIndex = new Object[cls.length][];
        for (int i = 0; i < cls.length; i++) {
            List<Object> instances = createInstances(cls[i], visited, onHold);
            possibleValuesByArgIndex[i] = instances.toArray();
        }
        return populateChoices(possibleValuesByArgIndex);

    }

    private static Object[] populateChoicesSample(Object[][] values) {
        Object[] v = new Object[values.length];
        for (int i = 0; i < v.length; i++) {
            if (values[i].length == 0) {
                return null;
            }
            v[i] = values[i][Maths.randomInt(values[i].length)];
        }
        return v;
    }

    private static List<Object[]> populateChoices(Object[][] possibleValuesByArgIndex) {
        if (true) {
            List<Object[]> list = new ArrayList<>();
            Object[] e = populateChoicesSample(possibleValuesByArgIndex);
            if (e != null) {
                list.add(e);
            }
            return list;
        }

        List<Object[]> result = new ArrayList<>();
        Maths.loopOver(possibleValuesByArgIndex, new LoopAction() {
            @Override
            public void next(Object[] values) {
                result.add(values);
            }
        });
        return result;
    }

    private static List<Object> createInstances(Class cls, Map<Class, List<Object>> visited, Set<Class> onHold) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (visited.containsKey(cls)) {
            return visited.get(cls);
        }
        if (onHold.contains(cls)) {
            throw new RuntimeException("On hold " + cls);
        }
        onHold.add(cls);
//        System.out.println("creating instances for "+cls);
        List<Object> all = new ArrayList<>();
        try {
            if (cls.equals(Integer.class) || cls.equals(Integer.TYPE)) {
                all.addAll(Arrays.asList(0, 1, -1, 2));
                return all;
            }
            if (cls.equals(Double.class) || cls.equals(Double.TYPE)) {
                all.addAll(Arrays.asList(0.0, 1.0, -1.0, 2.0, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
                return all;
            }
            if (cls.equals(Complex.class)) {
                Maths.loopOver(new Object[][]{
                        createInstances(Double.class, visited, onHold).toArray(),
                        createInstances(Double.class, visited, onHold).toArray()
                }, new LoopAction() {
                    @Override
                    public void next(Object[] values) {
                        all.add(Complex.valueOf((double) values[0], (double) values[1]));
                    }
                });
                return all;
            }
            if (cls.equals(Domain.class)) {
                for (Object a : createInstances(Double.class, visited, onHold)) {
                    for (Object b : createInstances(Double.class, visited, onHold)) {
                        all.add(Domain.forBounds((double) a, (double) b));
                        for (Object c : createInstances(Double.class, visited, onHold)) {
                            for (Object d : createInstances(Double.class, visited, onHold)) {
                                try {
                                    Domain e = Domain.forBounds((double) a, (double) b, (double) c, (double) d);
                                    all.add(e);
                                } catch (Exception ex) {
                                    //System.err.println(ex);
                                }
                            }
                        }
                    }
                }
                return all;
            }

            if (cls.equals(String.class)) {
                all.add("S");
                return all;
            }
            if (cls.equals(boolean.class)) {
                all.add(false);
                all.add(true);
                return all;
            }
            if (cls.equals(double[].class)) {
                all.add(new double[]{2, 1});
                return all;
            }
            if (cls.equals(double[][].class)) {
                all.add(new double[][]{{2, 1}});
                return all;
            }
            if (cls.equals(double[][][].class)) {
                all.add(new double[][][]{{{2, 1}}});
                return all;
            }
            if (cls.equals(Samples.class)) {
                all.add(Samples.absolute(new double[]{2, 1}));
                return all;
            }
            if (cls.isEnum()) {
                all.addAll(Arrays.asList(cls.getEnumConstants()));
                return all;
            }
            if (cls.equals(DoubleToComplex.class)) {
                all.addAll(createInstances(Complex.class, visited, onHold));
                return all;
            }

            if (cls.equals(CustomDDFunctionXYDefinition.class)) {
                all.add(new CustomDDFunctionXYDefinition("example", new CustomDDFunctionXY() {
                    @Override
                    public double evalDouble(double x, double y) {
                        return 0;
                    }
                }));
                return all;
            }

            if (cls.equals(CustomDDFunctionXDefinition.class)) {
                all.add(new CustomDDFunctionXDefinition("example", new CustomDDFunctionX() {
                    @Override
                    public double evalDouble(double x) {
                        return 0;
                    }
                }));
                return all;
            }
            if (cls.equals(CustomDCFunctionX.class)) {
                all.add(new CustomDCFunctionX() {
                    @Override
                    public Complex evalComplex(double c) {
                        return Complex.ONE;
                    }
                });
                return all;
            }
            if (cls.isArray()) {
                for (Object o1 : createInstances(cls.getComponentType(), visited, onHold)) {
                    Object o = Array.newInstance(cls.getComponentType(), 1);
                    Array.set(o, 0, o1);
                    all.add(o);
                }
                return all;
            }


            if (!Expr.class.isAssignableFrom(cls)) {
                System.err.println("Unsupported creation of " + cls);
                return all;
            }
            if (cls.equals(Expr.class)) {
                all.addAll(createInstances(Complex.class, visited, onHold));
                return all;
            }

            if (cls.equals(DoubleValue.class)) {
                all.add(new DoubleValue(2, Domain.FULLX));
                return all;
            }
            if (cls.equals(DIntegralXY.class)) {
                all.add(new DQuadIntegralXY());
                return all;
            }
            if (cls.equals(DoubleToDouble.class)) {
                all.addAll(createInstances(DoubleValue.class, visited, onHold));
                return all;
            }
            if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
                System.err.println("Interface could not e instantiated : " + cls.getName());
                return all;
            }

            if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                for (Constructor cons : cls.getConstructors()) {
                    if (Modifier.isPublic(cons.getModifiers())) {
                        List<Object[]> instances = createInstances(cons.getParameterTypes(), visited, onHold);
                        for (Object[] instance : instances) {
                            try {
                                Object e = cons.newInstance(instance);
                                all.add(e);
                            } catch (InvocationTargetException ex) {
                                System.err.println(ex.getTargetException() + " : new " + cls.getName() + "(" + Arrays.toString(instance) + ")");
                            } catch (Exception ex) {
                                System.err.println(ex);
                            }
                        }
                    }
                }
                for (Method method : cls.getDeclaredMethods()) {
                    if (Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())
                            && method.getReturnType().equals(cls)
                            ) {
                        List<Object[]> instances = createInstances(method.getParameterTypes(), visited, onHold);
                        for (Object[] instance : instances) {
                            try {
                                Object invoke = method.invoke(null, instance);
                                if (invoke != null) {
                                    all.add(invoke);
                                }
                            } catch (InvocationTargetException ex) {
                                System.err.println(ex.getTargetException() + " : " + cls.getName() + "." + method.getName() + "(" + Arrays.toString(instance) + ")");
                            } catch (Exception ex) {
                                System.err.println(ex);
                            }
                        }
                    }
                }
                if (all.isEmpty()) {
                    System.err.println("Ignored creation of " + cls);
                }
                return all;
            }
            return all;
        } finally {
            visited.put(cls, all);
            onHold.remove(cls);
        }
    }

    //    @Test
    public void testOne() {
        for (Class cls : ClassPathUtils.resolveContextClassesList()) {
            try {
                if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
                    List<Object> instances = createInstances(cls, new HashMap<>(), new HashSet<>());
                    for (Object instance : instances) {
                        String value = instance.toString();
                        System.out.println(instance.getClass().getName() + " :: " + instance.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
