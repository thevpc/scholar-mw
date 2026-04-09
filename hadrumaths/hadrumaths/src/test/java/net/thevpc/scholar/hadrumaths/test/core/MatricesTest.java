package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MatricesTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testMatrixAddition() {
        ComplexMatrix m1 = Maths.matrix(new Complex[][] {
                {Complex.of(1, 2), Complex.of(3, 4)},
                {Complex.of(5, 6), Complex.of(7, 8)}
        });
        ComplexMatrix m2 = Maths.matrix(new Complex[][] {
                {Complex.of(2, -1), Complex.of(-1, -1)},
                {Complex.of(0, 1), Complex.of(1, 0)}
        });
        ComplexMatrix r = m1.add(m2);

        Assertions.assertEquals(Complex.of(3, 1), r.get(0, 0));
        Assertions.assertEquals(Complex.of(2, 3), r.get(0, 1));
        Assertions.assertEquals(Complex.of(5, 7), r.get(1, 0));
        Assertions.assertEquals(Complex.of(8, 8), r.get(1, 1));
    }

    @Test
    public void testMatrixSubtraction() {
        ComplexMatrix m1 = Maths.matrix(new Complex[][] {
                {Complex.of(1, 2), Complex.of(3, 4)},
                {Complex.of(5, 6), Complex.of(7, 8)}
        });
        ComplexMatrix m2 = Maths.matrix(new Complex[][] {
                {Complex.of(2, -1), Complex.of(-1, -1)},
                {Complex.of(0, 1), Complex.of(1, 0)}
        });
        ComplexMatrix r = m1.sub(m2);

        Assertions.assertEquals(Complex.of(-1, 3), r.get(0, 0));
        Assertions.assertEquals(Complex.of(4, 5), r.get(0, 1));
        Assertions.assertEquals(Complex.of(5, 5), r.get(1, 0));
        Assertions.assertEquals(Complex.of(6, 8), r.get(1, 1));
    }

    @Test
    public void testMatrixMultiplication() {
        ComplexMatrix m1 = Maths.matrix(new Complex[][] {
                {Complex.of(1, 1), Complex.of(0, 1)},
                {Complex.of(-1, 0), Complex.of(1, -1)}
        });
        ComplexMatrix m2 = Maths.matrix(new Complex[][] {
                {Complex.of(2, 0), Complex.of(1, -1)},
                {Complex.of(0, 2), Complex.of(0, 0)}
        });
        // r[0,0] = (1+i)*2 + i*2i = 2+2i - 2 = 2i
        // r[0,1] = (1+i)*(1-i) + i*0 = 1^2 - i^2 = 2
        // r[1,0] = (-1)*2 + (1-i)*2i = -2 + 2i + 2 = 2i
        // r[1,1] = (-1)*(1-i) + (1-i)*0 = -1+i
        ComplexMatrix r = m1.mul(m2);

        Assertions.assertEquals(Complex.of(0, 2), r.get(0, 0));
        Assertions.assertEquals(Complex.of(2, 0), r.get(0, 1));
        Assertions.assertEquals(Complex.of(0, 2), r.get(1, 0));
        Assertions.assertEquals(Complex.of(-1, 1), r.get(1, 1));
    }

    @Test
    public void testMatrixInversion() {
        ComplexMatrix m1 = Maths.matrix(new Complex[][] {
                {Complex.of(4, 0), Complex.of(3, 0)},
                {Complex.of(3, 0), Complex.of(2, 0)}
        });
        // det = 8 - 9 = -1
        // inv = [2, -3; -3, 4] / -1 = [-2, 3; 3, -4]
        ComplexMatrix r = m1.inv();

        Assertions.assertEquals(Complex.of(-2, 0), r.get(0, 0));
        Assertions.assertEquals(Complex.of(3, 0), r.get(0, 1));
        Assertions.assertEquals(Complex.of(3, 0), r.get(1, 0));
        Assertions.assertEquals(Complex.of(-4, 0), r.get(1, 1));
    }

    @Test
    public void testMatrixDeterminant() {
        ComplexMatrix m1 = Maths.matrix(new Complex[][] {
                {Complex.of(4, 0), Complex.of(3, 0)},
                {Complex.of(3, 0), Complex.of(2, 0)}
        });
        Complex det = m1.det();
        Assertions.assertEquals(Complex.of(-1, 0), det);
    }
}
