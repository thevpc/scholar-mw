package net.thevpc.scholar.hadrumaths;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The same logical matrix, stored by different implementations, must answer every operation
 * the same way. Several class-dependent defects were found in this area (norms, abs, mul(Complex)),
 * so this checks the storage classes against each other and against a reference.
 */
public class StorageConsistencyTest {

    static final int R = 3, C = 4;

    static Complex v(int i, int j) {
        return Complex.of(10 * i + j, (i - j) * 0.5 + 0.25);
    }

    static Complex[][] data() {
        Complex[][] m = new Complex[R][C];
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                m[i][j] = v(i, j);
            }
        }
        return m;
    }

    static Complex[][] square() {
        Complex[][] m = new Complex[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                m[i][j] = v(i, j);
                if (i == j) {
                    m[i][j] = m[i][j].add(Complex.of(20, 0)); // well conditioned
                }
            }
        }
        return m;
    }

    static ComplexMatrix rawD1(Complex[][] d) {
        MemRawD1ComplexMatrix m = new MemRawD1ComplexMatrix(d.length, d[0].length);
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                m.set(i, j, d[i][j]);
            }
        }
        return m;
    }

    static ComplexMatrix[] sameMatrix(Complex[][] d) {
        return new ComplexMatrix[]{
                new MemComplexComplexMatrix(d),
                rawD1(d),
                Maths.matrix(d)
        };
    }

    static void assertSame(double a, double b, String what) {
        Assertions.assertEquals(a, b, 1e-9 * Math.max(1.0, Math.abs(a)), what);
    }

    static void assertSameValue(Complex a, Complex b, String what) {
        assertSame(a.getReal(), b.getReal(), what + " (re)");
        assertSame(a.getImag(), b.getImag(), what + " (im)");
    }

    @Test
    public void elementAccessAndScalarOpsAgree() {
        Complex[][] d = data();
        ComplexMatrix[] ms = sameMatrix(d);
        Complex c = Complex.of(2, -1);
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                for (ComplexMatrix m : ms) {
                    String tag = m.getClass().getSimpleName() + "[" + i + "," + j + "]";
                    assertSameValue(v(i, j), m.get(i, j), tag + " get");
                    assertSameValue(v(i, j).mul(c), m.mul(c).get(i, j), tag + " mul(c)");
                    assertSameValue(v(i, j).div(c), m.div(c).get(i, j), tag + " div(c)");
                    assertSameValue(v(i, j).add(c), m.add(c).get(i, j), tag + " add(c)");
                    assertSameValue(v(i, j).minus(c), m.sub(c).get(i, j), tag + " sub(c)");
                    assertSame(v(i, j).absdbl(), m.abs().get(i, j).getReal(), tag + " abs");
                    assertSame(v(i, j).absdblsqr(), m.abssqr().get(i, j).getReal(), tag + " abssqr");
                }
            }
        }
    }

    @Test
    public void normsAgreeAcrossStorages() {
        Complex[][] d = data();
        ComplexMatrix[] ms = sameMatrix(d);
        double one = ms[0].norm1(), inf = ms[0].normInf(), max = ms[0].normMax(), fro = ms[0].normF(), two = ms[0].norm2();
        double maxAbs = ms[0].maxAbs(), minAbs = ms[0].minAbs();
        for (ComplexMatrix m : ms) {
            String tag = m.getClass().getSimpleName();
            assertSame(one, m.norm1(), tag + " norm1");
            assertSame(inf, m.normInf(), tag + " normInf");
            assertSame(max, m.normMax(), tag + " normMax");
            assertSame(fro, m.normF(), tag + " normF");
            assertSame(two, m.norm2(), tag + " norm2");
            assertSame(maxAbs, m.maxAbs(), tag + " maxAbs");
            assertSame(minAbs, m.minAbs(), tag + " minAbs");
        }
    }

    @Test
    public void transposesAgreeAcrossStorages() {
        Complex[][] d = data();
        ComplexMatrix[] ms = sameMatrix(d);
        for (ComplexMatrix m : ms) {
            String tag = m.getClass().getSimpleName();
            ComplexMatrix t = m.transpose();
            ComplexMatrix h = m.transposeHermitian();
            for (int i = 0; i < R; i++) {
                for (int j = 0; j < C; j++) {
                    assertSameValue(v(i, j), t.get(j, i), tag + " transpose");
                    assertSameValue(v(i, j).conj(), h.get(j, i), tag + " hermitian");
                }
            }
        }
    }

    @Test
    public void multiplicationAgreesAcrossStorages() {
        Complex[][] a = data();
        Complex[][] b = new Complex[C][2];
        for (int i = 0; i < C; i++) {
            for (int j = 0; j < 2; j++) {
                b[i][j] = Complex.of(i - j, i * j + 1);
            }
        }
        ComplexMatrix expected = null;
        for (ComplexMatrix m : sameMatrix(a)) {
            for (ComplexMatrix n : sameMatrix(b)) {
                ComplexMatrix r = m.mul(n);
                Assertions.assertEquals(R, r.getRowCount());
                Assertions.assertEquals(2, r.getColumnCount());
                if (expected == null) {
                    expected = r;
                } else {
                    for (int i = 0; i < R; i++) {
                        for (int j = 0; j < 2; j++) {
                            assertSameValue(expected.get(i, j), r.get(i, j),
                                    "product " + m.getClass().getSimpleName() + " x " + n.getClass().getSimpleName());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void inverseAndDeterminantAgreeAcrossStorages() {
        Complex[][] d = square();
        Complex referenceDet = null;
        for (ComplexMatrix m : sameMatrix(d)) {
            String tag = m.getClass().getSimpleName();
            ComplexMatrix inv = m.inv();
            ComplexMatrix id = m.mul(inv);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    assertSameValue(i == j ? Complex.ONE : Complex.ZERO, id.get(i, j), tag + " A*inv(A)");
                }
            }
            Complex det = m.det();
            // det(A) * det(inv A) == 1
            assertSame(1.0, det.mul(inv.det()).absdbl(), tag + " det(A)*det(inv A)");
            if (referenceDet == null) {
                referenceDet = det;
            } else {
                assertSameValue(referenceDet, det, tag + " det vs reference");
            }
        }
    }
}
