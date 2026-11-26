package net.thevpc.scholar.hadrumaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The flat-storage complex matrix used to ignore the row index in norm1()/normInf()
 * (both summed the first row once per row). It also lost its Frobenius implementation
 * when norm2() was unified to the spectral norm; normF() must stay available.
 */
public class MemRawD1NormTest {

    private static final double SQRT72 = Math.sqrt(72.0);

    private static MemRawD1ComplexMatrix sample() {
        // row-major:  [ 1    2i   3      ]
        //             [ 4    5    6+6i   ]
        MemRawD1ComplexMatrix m = new MemRawD1ComplexMatrix(2, 3);
        m.set(0, 0, Complex.of(1, 0));
        m.set(0, 1, Complex.of(0, 2));
        m.set(0, 2, Complex.of(3, 0));
        m.set(1, 0, Complex.of(4, 0));
        m.set(1, 1, Complex.of(5, 0));
        m.set(1, 2, Complex.of(6, 6));
        return m;
    }

    @Test
    public void norm1IsMaxColumnSum() {
        // max(|1|+|4|, |2i|+|5|, |3|+|6+6i|) = 3 + sqrt(72)
        Assertions.assertEquals(3 + SQRT72, sample().norm1(), 1e-12);
    }

    @Test
    public void normInfIsMaxRowSum() {
        // max(1+2+3, 4+5+|6+6i|) = 9 + sqrt(72)
        Assertions.assertEquals(9 + SQRT72, sample().normInf(), 1e-12);
    }

    @Test
    public void normFIsFrobenius() {
        // 1 + 4 + 9 + 16 + 25 + (36+36) = 127
        Assertions.assertEquals(Math.sqrt(127), sample().normF(), 1e-12);
        Assertions.assertEquals(Math.sqrt(127), sample().norm(NormStrategy.NORM_F), 1e-12);
    }

    @Test
    public void spectralNormIsConsistentWithFrobeniusOrdering() {
        // ||A||_2 <= ||A||_F <= sqrt(rank)*||A||_2, here rank = 2
        MemRawD1ComplexMatrix m = sample();
        double n2 = m.norm2();
        double nf = m.normF();
        Assertions.assertTrue(n2 <= nf + 1e-12);
        Assertions.assertTrue(nf <= Math.sqrt(2) * n2 + 1e-12);
    }

    @Test
    public void getStillReturnsCanonicalValues() {
        Assertions.assertSame(Complex.ZERO, Complex.of(0, 0));
        Assertions.assertSame(Complex.ONE, Complex.of(1, 0));
        Assertions.assertSame(Complex.I, Complex.of(0, 1));
        Assertions.assertSame(Complex.TWO, Complex.of(2));
        Assertions.assertSame(Complex.MINUS_ONE, Complex.of(-1));
    }
}
