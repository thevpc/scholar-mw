package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MutableComplex;
import org.junit.Test;

public class TestComplex2 {

    @Test
    public void testMul() {
        double[] V = {Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.MAX_VALUE, Double.MIN_VALUE, 0, 1, -1};
        for (int i = 0; i < V.length; i++) {
            for (int j = 0; j < V.length; j++) {
                Complex c1 = Complex.valueOf(V[i], V[j]);
                for (int k = 0; k < V.length; k++) {
                    for (int l = 0; l < V.length; l++) {
                        Complex c2 = Complex.valueOf(V[k], V[l]);
                        try {
                            checkMul(c1, c2);
                        } catch (Throwable ex) {
                            checkMul(c1, c2);
                        }
                    }
                }
            }
        }
    }

    private void checkMul(Complex c1, Complex c2) {
        Complex m1 = c1.mul(c2);

        MutableComplex d1 = MutableComplex.forComplex(c1);
        MutableComplex d2 = MutableComplex.forComplex(c2);
        d1.mul(d2);
        Complex m2 = d1.toComplex();
        if (!m1.equals(m2)) {
            System.err.println("Error : (" + c1 + ") * (" + c2 + ") = " + m1 + " <> " + m2);
        } else {
            System.out.println("OK : (" + c1 + ") * (" + c2 + ") = " + m1);
        }
        if (m1.isNaN()) {
            Assert.assertTrue(m2.isNaN());
        } else {
            Assert.assertEquals(m1, m2);
        }
    }

    @Test
    public void testMutableComplex() {
        Complex c = Maths.randomComplex();
        MutableComplex m = MutableComplex.forComplex(c);
        for (int i = 0; i < 1000; i++) {
            Complex c2 = Maths.randomComplex();
            switch (Maths.randomInt(5)) {
                case 0: {
                    System.out.println("Check mul");
                    c = c.mul(c2);
                    m.mul(c2);
                    break;
                }
                case 1: {
                    System.out.println("Check add");
                    c = c.add(c2);
                    m.add(c2);
                    break;
                }
                case 2: {
                    System.out.println("Check exp");
                    c = c.exp();
                    m.exp();
                    break;
                }
                case 3: {
                    System.out.println("Check addProduct(,)");
                    Complex c3 = Maths.randomComplex();
                    c = c.add(c2.mul(c3));
                    m.addProduct(c2, c3);
                    break;
                }
                case 4: {
                    System.out.println("Check addProduct(,,)");
                    Complex c3 = Maths.randomComplex();
                    Complex c4 = Maths.randomComplex();
                    c = c.add(c2.mul(c3).mul(c4));
                    m.addProduct(c2, c3, c4);
                    break;
                }
            }
            Complex mc = m.toComplex();
            boolean ignoreAssert = false;
            if (!c.equals(mc)) {
                c.equals(mc);
                if (c.isNaN() && c.isNaN()) {
                    ignoreAssert = true;
                } else {
                    System.out.println("Why in test");
                }
            }
            if (!ignoreAssert) {
                org.junit.Assert.assertEquals(c, mc);
            }
        }
    }
}
