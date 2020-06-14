package net.vpc.scholar.hadrumaths.test.symbolic;

//package net.vpc.scholar.hadrumaths.test;
//
//import net.vpc.scholar.hadrumaths.CArray;
//import net.vpc.scholar.hadrumaths.Complex;
//import org.junit.jupiter.api.Test;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.util.Arrays;
//import java.util.Objects;
//
///**
// * @author Taha Ben Salah (taha.bensalah@gmail.com)
// * @creationtime 31 juil. 2007 21:46:18
// */
//public class TestComplex {
////    public static void main(String[] args) {
////        for (int i = 0; i < 100; i++) {
////            Complex c = Complex.valueOf(i);
////            System.out.println(c.cotanh() + ":" + c.cosh().div(c.sinh()));
////        }
////    }
//
//
//    @Test
//    public void testComplex() {
//
//        //        double rr=0.23324645131006538;
//        //        ComplexOld v1 = new ComplexOld(rr,0);
//        //        Complex v2 = Complex.valueOf(rr);
//        //        System.out.println(v1.log10());
//        //        System.out.println(v2.log10());
//        //        if(true){
//        //            return;
//        //        }
//        for (Method method : Complex.class.getMethods()) {
//            double a = Math.random();
//            double b = Math.random();
//            Complex c1 = Complex.valueOf(a, b);
//            Complex c2 = Complex.valueOf(a, 0);
//            Complex c3 = Complex.valueOf(0, a);
//
//            double c = Math.random();
//            double d = Math.random();
//            Complex c4 = Complex.valueOf(c, d);
//            Complex c5 = Complex.valueOf(c, 0);
//            Complex c6 = Complex.valueOf(0, d);
//
//            Complex d1 = new ComplexOld(a, b);
//            Complex d2 = new ComplexOld(a, 0);
//            Complex d3 = new ComplexOld(0, a);
//            Complex d4 = new ComplexOld(c, d);
//            Complex d5 = new ComplexOld(c, 0);
//            Complex d6 = new ComplexOld(0, d);
//            assertEquals(c1, d1);
//            assertEquals(c2, d2);
//            assertEquals(c3, d3);
//            assertEquals(c4, d4);
//            assertEquals(c5, d5);
//            assertEquals(c6, d6);
//            //            System.out.println(c1.mul(c4)+ "  :: " + d1.mul(c4));
//
//            Complex[] cc = new Complex[]{c1, c2, c3, c4, c5, c6};
//            Complex[] dd = new Complex[]{d1, d2, d3};
//            for (int i = 0; i < 3; i++) {
//                String mname = method.getName();
//                if (Arrays.asList("wait", "notify", "getClass", "notifyAll", "compareTo"
//                        , "setProperties", "setProperty", "getStringProperty"
//                        , "getDoubleProperty", "getLongProperty", "getIntProperty",
//                        "getDistance", "composeX", "composeY", "setParam", "equals", "getProperty", "setTitle",
//                        "isInvariant"
//                ).contains(mname)) {
//                    continue;
//                }
//                if (!Modifier.isStatic(method.getModifiers()) && Modifier.isPublic(method.getModifiers())) {
//                    if (method.getParameterCount() == 0) {
//                        //                        System.out.println("invoke " + method);
//                        if (mname.equals("toDD") || mname.equals("toReal") || mname.equals("toDouble")) {
//                            if (cc[i].getImag() != 0) {
//                                continue;
//                            }
//                        }
//                        testInvoke(method, cc[i], dd[i]);
//                    } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(Complex.class)) {
//                        //                        System.out.println("invoke " + method);
//                        for (int j = 0; j < 3; j++) {
//                            testInvoke(method, cc[i], dd[i], cc[j + 3]);
//                        }
//                    } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(Double.TYPE)) {
//                        //                        System.out.println("invoke " + method);
//                        for (int j = 0; j < 3; j++) {
//                            testInvoke(method, cc[i], dd[i], Math.random() * 10);
//                        }
//                    } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(Integer.TYPE)) {
//                        //                        System.out.println("invoke " + method);
//                        for (int j = 0; j < 3; j++) {
//                            testInvoke(method, cc[i], dd[i], (int) (Math.random() * 10));
//                        }
//                    } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(double[].class)) {
//                        //                        System.out.println("invoke " + method);
//                        //                        for (int j = 0; j < 3; j++) {
//                        //                            testInvoke(method, cc[i], dd[i],(int)(Math.random()*10));
//                        //                        }
//                    } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(Complex[].class)) {
//                        //                        System.out.println("invoke " + method);
//                        //                        for (int j = 0; j < 3; j++) {
//                        //                            testInvoke(method, cc[i], dd[i],(int)(Math.random()*10));
//                        //                        }
//                    } else if (method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(CArray.class)) {
//                        //                        System.out.println("invoke " + method);
//                        //                        for (int j = 0; j < 3; j++) {
//                        //                            testInvoke(method, cc[i], dd[i],(int)(Math.random()*10));
//                        //                        }
//                    } else {
//                        System.err.println("Ignored method " + method);
//                    }
//                }
//            }
//
//        }
//    }
//
//    private static void testInvoke(Method method, Object o1, Object o2, Object... params) {
//        try {
//            assertEquals(invoke(method, o1, params), invoke(method, o2, params));
//        } catch (IllegalArgumentException ex) {
//            System.err.println("failed method " + method);
//            System.err.println("\t" + o1.getClass().getSimpleName() + " vs " + o2.getClass().getSimpleName());
//            System.err.println("\t" + ex.toString());
//            try {
//                Object invoke1 = invoke(method, o1, params);
//                Object invoke2 = invoke(method, o2, params);
//                assertEquals(invoke1, invoke2);
//            } catch (IllegalArgumentException ex2) {
//            }
//        }
//    }
//
//    private static Object invoke(Method m, Object o, Object... params) {
//        try {
//            return m.invoke(o, params);
//        } catch (IllegalAccessException e) {
//            throw new IllegalArgumentException();
//        } catch (InvocationTargetException e) {
//            throw new IllegalArgumentException();
//        }
//    }
//
//    private static void assertEquals(Object a, Object b) {
//        if (!Objects.equals(a, b)) {
//            throw new IllegalArgumentException("Not equals " + a + " and " + b);
//        }
//    }
//
//}
