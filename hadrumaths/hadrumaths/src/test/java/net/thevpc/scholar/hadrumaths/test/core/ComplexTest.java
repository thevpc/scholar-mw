package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.Complex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComplexTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testAddition() {
        Complex a = Complex.of(2.0, 3.0);
        Complex b = Complex.of(1.5, -1.0);
        Complex r = a.plus(b);
        Assertions.assertEquals(3.5, r.getReal(), DELTA);
        Assertions.assertEquals(2.0, r.getImag(), DELTA);
    }

    @Test
    public void testSubtraction() {
        Complex a = Complex.of(2.0, 3.0);
        Complex b = Complex.of(1.5, -1.0);
        Complex r = a.minus(b);
        Assertions.assertEquals(0.5, r.getReal(), DELTA);
        Assertions.assertEquals(4.0, r.getImag(), DELTA);
    }

    @Test
    public void testMultiplication() {
        // (2 + 3i) * (1 - i) = 2 - 2i + 3i - 3i^2 = 2 + i + 3 = 5 + i
        Complex a = Complex.of(2.0, 3.0);
        Complex b = Complex.of(1.0, -1.0);
        Complex r = a.mul(b);
        Assertions.assertEquals(5.0, r.getReal(), DELTA);
        Assertions.assertEquals(1.0, r.getImag(), DELTA);
    }

    @Test
    public void testDivision() {
        // (5 + i) / (1 - i) = (5 + i)(1 + i) / (1^2 + 1^2) = (5 + 5i + i - 1) / 2 = (4 + 6i) / 2 = 2 + 3i
        Complex a = Complex.of(5.0, 1.0);
        Complex b = Complex.of(1.0, -1.0);
        Complex r = a.div(b);
        Assertions.assertEquals(2.0, r.getReal(), DELTA);
        Assertions.assertEquals(3.0, r.getImag(), DELTA);
    }

    @Test
    public void testAbs() {
        Complex a = Complex.of(3.0, 4.0);
        Assertions.assertEquals(5.0, a.absdbl(), DELTA);
    }

    @Test
    public void testConj() {
        Complex a = Complex.of(2.0, 3.0);
        Complex r = a.conj();
        Assertions.assertEquals(2.0, r.getReal(), DELTA);
        Assertions.assertEquals(-3.0, r.getImag(), DELTA);
    }

    @Test
    public void testInv() {
        // 1 / (1 - i) = (1 + i) / 2 = 0.5 + 0.5i
        Complex a = Complex.of(1.0, -1.0);
        Complex r = a.inv();
        Assertions.assertEquals(0.5, r.getReal(), DELTA);
        Assertions.assertEquals(0.5, r.getImag(), DELTA);
    }
    
    @Test
    public void testExp() {
        // e^(a + bi) = e^a * (cos(b) + i*sin(b))
        Complex a = Complex.of(1.0, Math.PI / 2);
        Complex r = a.exp();
        Assertions.assertEquals(0.0, r.getReal(), DELTA);
        Assertions.assertEquals(Math.E, r.getImag(), DELTA);
    }
}
