package net.vpc.scholar.hadrumaths.test.numbers;

import net.vpc.common.util.Chronometer;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.interop.jblas.JBlasComplexMatrixFactory;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TestMatrices {

    public static final double MAX_ERR = 1E-1;
    int SIZE = 600;

    private ComplexMatrixFactory[] getFactories() {
        return new ComplexMatrixFactory[]{
            //                MemMatrixFactory.INSTANCE,
            //                MemRawD1MatrixFactory.INSTANCE,
            //                SmartMatrixFactory.INSTANCE,
            //                OjalgoMatrixFactory.INSTANCE,
            JBlasComplexMatrixFactory.INSTANCE, //                Maths.Config.getLargeMatrixFactory()
        };
    }

    //
//    @Test
//    public void testA() {
//        Matrix m=Maths.matrix("[\n" +
//                "32.67500832511648+0.9652827387682853i 28.845522089527538+13.862573108900323i\n" +
//                "54.49374833211236-73.37528044272875i  91.52848047601915+0.6557994140523506i\n" +
//                "]");
//
//        MatrixFactory[] factories = getLineSourceFactories();
//        for (MatrixFactory f : factories) {
//            System.out.println(f.getId()+"="+f.newMatrix(m).invSolve());
//        }
//    }
    @Test
    public void testMul() {
        String op = "multiplication";
        System.out.println("========= " + op);
        ComplexMatrixFactory ref = MemComplexMatrixFactory.INSTANCE;
        ComplexMatrixFactory[] factories = getFactories();
        for (int i = 0; i < factories.length; i++) {
            ComplexMatrixFactory f = factories[i];
            Chronometer chrono = Maths.chrono();
//            System.out.println("=== "+f);
            ComplexMatrix m1 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m2 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m3=null;
            try {
                m3 = m1.mul(m2);
            } catch (UnsatisfiedLinkError ex) {
                System.err.println(ex);
                //ignore error;
                continue;
            }
//            Matrix m3 = m1.mul(m2);
            chrono.stop();
            ComplexMatrix other = ref.newMatrix(m2);
            ComplexMatrix m3Ref = ref.newMatrix(m1).mul(other);
            boolean ok = checkMatrixEquality(m3, m3Ref);
            System.out.println("=== " + f + " : " + chrono + " : " + ok);
            if (!ok) {
                System.out.println(">>>>>>>>>>>>>> ERROR in " + f.getId() + " vs " + ref);
                System.out.println(f.getId() + "  " + "m1=" + m1);
                System.out.println(f.getId() + "  " + "m2=" + m2);
                System.out.println(f.getId() + "  " + "m3=m1*m2=" + m3);
                System.out.println("m3ref=" + m3Ref);
                throw new IllegalArgumentException("Problem with " + op + " by " + f.getId());
            }
            Assertions.assertTrue(ok);
        }
    }

    private boolean checkMatrixEquality(ComplexMatrix m3, ComplexMatrix m3Ref) {
        boolean a = m3.equals(m3Ref);
        if (!a) {
            double e = m3.getError(m3Ref);
            if (e < MAX_ERR) {
                return true;
            }
            DMatrix errorMatrix = m3.getErrorMatrix(m3Ref);
            double m = errorMatrix.maxAbs();
            return false;
        }
        return a;
    }

    @Test
    public void testAdd() {
        String op = "addition";
        System.out.println("========= " + op);
        ComplexMatrixFactory ref = MemComplexMatrixFactory.INSTANCE;
        ComplexMatrixFactory[] factories = getFactories();
        for (int i = 0; i < factories.length; i++) {
            ComplexMatrixFactory f = factories[i];
            Chronometer chrono = Maths.chrono();
//            System.out.println("=== "+f);
            ComplexMatrix m1 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m2 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m3 = m1.add(m2);
            chrono.stop();
            ComplexMatrix other = ref.newMatrix(m2);
            ComplexMatrix m3Ref = ref.newMatrix(m1).add(other);
            boolean ok = checkMatrixEquality(m3, m3Ref);
            System.out.println("=== " + f + " : " + chrono + " : " + ok);
            if (!ok) {
                System.out.println(">>>>>>>>>>>>>> ERROR in " + f.getId() + " vs " + ref);
                System.out.println(f.getId() + "  " + "m1=" + m1);
                System.out.println(f.getId() + "  " + "m2=" + m2);
                System.out.println(f.getId() + "  " + "m3=m1+m2=" + m3);
                System.out.println("m3ref=" + m3Ref);
                throw new IllegalArgumentException("Problem with " + op + " by " + f.getId());
            }
            Assertions.assertTrue(ok);
        }
    }

    @Test
    public void testSub() {
        String op = "substruction";
        System.out.println("========= " + op);
        ComplexMatrixFactory ref = MemComplexMatrixFactory.INSTANCE;
        ComplexMatrixFactory[] factories = getFactories();
        for (int i = 0; i < factories.length; i++) {
            ComplexMatrixFactory f = factories[i];
            Chronometer chrono = Maths.chrono();
//            System.out.println("=== "+f);
            ComplexMatrix m1 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m2 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m3 = m1.sub(m2);
            chrono.stop();
            ComplexMatrix other = ref.newMatrix(m2);
            ComplexMatrix m3Ref = ref.newMatrix(m1).sub(other);
            boolean ok = checkMatrixEquality(m3, m3Ref);
            System.out.println("=== " + f + " : " + chrono + " : " + ok);
            if (!ok) {
                System.out.println(">>>>>>>>>>>>>> ERROR in " + f.getId() + " vs " + ref);
                System.out.println(f.getId() + "  " + "m1=" + m1);
                System.out.println(f.getId() + "  " + "m2=" + m2);
                System.out.println(f.getId() + "  " + "m3=m1-m2=" + m3);
                System.out.println("m3ref=" + m3Ref);
                throw new IllegalArgumentException("Problem with " + op + " by " + f.getId());
            }
            Assertions.assertTrue(ok);
        }
    }

    @Test
    public void testDiv() {
        String op = "divide";
        System.out.println("========= " + op);
        ComplexMatrixFactory ref = MemComplexMatrixFactory.INSTANCE;
        ComplexMatrixFactory[] factories = getFactories();
        for (int i = 0; i < factories.length; i++) {
            ComplexMatrixFactory f = factories[i];
            Chronometer chrono = Maths.chrono();
//            System.out.println("=== "+f);
            ComplexMatrix m1 = f.newRandom(SIZE, SIZE, 0, 100);
            ComplexMatrix m2 = generateInvertible(SIZE, f);
            ComplexMatrix m3 = null;
            try {
                m3 = m1.div(m2);
            } catch (UnsatisfiedLinkError ex) {
                System.err.println(ex);
                //ignore error;
                continue;
            }
            chrono.stop();
            ComplexMatrix other = ref.newMatrix(m2);
            ComplexMatrix m3Ref = ref.newMatrix(m1).div(other);
            boolean ok = checkMatrixEquality(m3, m3Ref);
            System.out.println("=== " + f + " : " + chrono + " : " + ok);
            if (!ok) {
                System.out.println(">>>>>>>>>>>>>> ERROR in " + f.getId() + " vs " + ref);
                System.out.println(f.getId() + "  " + "m1=" + m1);
                System.out.println(f.getId() + "  " + "m2=" + m2);
                System.out.println(f.getId() + "  " + "m3=m1/m2=" + m3);
                System.out.println("m3ref=" + m3Ref);
                throw new IllegalArgumentException("Problem with " + op + " by " + f.getId());
            }
            Assertions.assertTrue(ok);
        }
    }

    @Test
    public void testInv() {
        String op = "invert";
        System.out.println("========= " + op);
        ComplexMatrixFactory ref = MemComplexMatrixFactory.INSTANCE;
        ComplexMatrixFactory[] factories = getFactories();
        for (int i = 0; i < factories.length; i++) {
            ComplexMatrixFactory f = factories[i];
            Chronometer chrono = Maths.chrono();
//            System.out.println("=== "+f);
            ComplexMatrix m1 = generateInvertible(SIZE, f);
            ComplexMatrix m3 = m1.invSolve();
            chrono.stop();
            ComplexMatrix m3Ref = ref.newMatrix(m1).inv();
            boolean ok = checkMatrixEquality(m3, m3Ref);
            System.out.println("=== " + f + " : " + chrono + " : " + ok);
            if (!ok) {
                System.out.println(">>>>>>>>>>>>>> ERROR in " + f.getId() + " vs " + ref);
                System.out.println(f.getId() + "  " + "m1=" + m1);
                System.out.println(f.getId() + "  " + "m3=inv(m1)=" + m3);
                System.out.println("m3ref=" + m3Ref);
                throw new IllegalArgumentException("Problem with " + op + " by " + f.getId());
            }
            Assertions.assertTrue(ok);
        }
    }

    private static ComplexMatrix generateInvertible(int size, ComplexMatrixFactory f) {
        Complex[][] a = new Complex[size][size];
        for (int i = 0; i < size; i++) {
            MutableComplex s = MutableComplex.Zero();
            for (int j = 0; j < size; j++) {
                if (i != j) {
                    Complex c = Maths.randomComplex().mul(100);
                    a[i][j] = c;
                    s.add(c.abs());
                }
            }
            s.add(Complex.of(Maths.randomDouble(1), Maths.randomDouble(1)));
            a[i][i] = s.toImmutable();
        }
        return f.newMatrix(a);
    }
}
