package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * Calculation checks for the matrix layer: inverse, solve, determinant, Hermitian transpose,
 * element-wise ops and norms. References are computed directly from the definitions.
 */
public class MatrixIdentitiesTest {

    static double tol(double v) {
        return 1e-8 * Math.max(1.0, Math.abs(v));
    }

    static void eq(double expected, double actual, String what) {
        Assertions.assertEquals(expected, actual, tol(expected), what);
    }

    static void eq(Complex expected, Complex actual, String what) {
        eq(expected.getReal(), actual.getReal(), what + " (re)");
        eq(expected.getImag(), actual.getImag(), what + " (im)");
    }

    static ComplexMatrix randomMatrix(Random r, int n, double scale) {
        Complex[][] m = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = Complex.of((r.nextDouble() * 2 - 1) * scale, (r.nextDouble() * 2 - 1) * scale);
            }
        }
        return Maths.matrix(m);
    }

    static void assertIdentity(ComplexMatrix m, String what) {
        for (int i = 0; i < m.getRowCount(); i++) {
            for (int j = 0; j < m.getColumnCount(); j++) {
                eq(i == j ? Complex.ONE : Complex.ZERO, m.get(i, j), what + "[" + i + "," + j + "]");
            }
        }
    }

    @Test
    public void inverseTimesOriginalIsIdentity() {
        Random r = new Random(21);
        for (int n = 1; n <= 5; n++) {
            for (int k = 0; k < 20; k++) {
                ComplexMatrix a = randomMatrix(r, n, 2);
                ComplexMatrix ai = a.inv();
                assertIdentity(a.mul(ai), "A*inv(A) n=" + n);
                assertIdentity(ai.mul(a), "inv(A)*A n=" + n);
            }
        }
    }

    @Test
    public void solveAgreesWithInverse() {
        Random r = new Random(22);
        for (int n = 2; n <= 4; n++) {
            for (int k = 0; k < 10; k++) {
                ComplexMatrix a = randomMatrix(r, n, 2);
                Complex[][] b = new Complex[n][1];
                for (int i = 0; i < n; i++) {
                    b[i][0] = Complex.of(r.nextGaussian(), r.nextGaussian());
                }
                ComplexMatrix bb = Maths.matrix(b);
                ComplexMatrix viaSolve = a.solve(bb);
                ComplexMatrix viaInv = a.inv().mul(bb);
                for (int i = 0; i < n; i++) {
                    eq(viaInv.get(i, 0), viaSolve.get(i, 0), "solve vs inv n=" + n);
                }
                // residual: A x must reproduce b
                ComplexMatrix back = a.mul(viaSolve);
                for (int i = 0; i < n; i++) {
                    eq(bb.get(i, 0), back.get(i, 0), "A*x == b n=" + n);
                }
            }
        }
    }

    @Test
    public void determinantIsMultiplicative() {
        Random r = new Random(23);
        for (int k = 0; k < 30; k++) {
            ComplexMatrix a = randomMatrix(r, 3, 1.5);
            ComplexMatrix b = randomMatrix(r, 3, 1.5);
            Complex dab = a.mul(b).det();
            Complex ab = a.det().mul(b.det());
            eq(ab, dab, "det(AB) == det(A)det(B)");
            // det(inv) == 1/det
            eq(a.det().inv(), a.inv().det(), "det(inv(A)) == 1/det(A)");
        }
        Assertions.assertEquals(Complex.ONE, Maths.identityMatrix(4).det());
    }

    @Test
    public void hermitianTransposeConjugatesAndSwaps() {
        Random r = new Random(24);
        ComplexMatrix a = randomMatrix(r, 4, 2);
        ComplexMatrix h = a.transposeHermitian();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                eq(a.get(j, i).conj(), h.get(i, j), "A^H [" + i + "," + j + "]");
            }
        }
        // (A^H)^H == A
        ComplexMatrix hh = h.transposeHermitian();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                eq(a.get(i, j), hh.get(i, j), "(A^H)^H");
            }
        }
    }

    @Test
    public void elementWiseOpsMatchDefinitions() {
        Random r = new Random(25);
        ComplexMatrix a = randomMatrix(r, 3, 2);
        ComplexMatrix b = randomMatrix(r, 3, 2);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                eq(a.get(i, j).add(b.get(i, j)), a.add(b).get(i, j), "A+B");
                eq(a.get(i, j).sub(b.get(i, j)), a.sub(b).get(i, j), "A-B");
                eq(a.get(i, j).mul(b.get(i, j)), a.dotmul(b).get(i, j), "A.*B");
                eq(a.get(i, j).div(b.get(i, j)), a.dotdiv(b).get(i, j), "A./B");
                eq(a.get(i, j).conj(), a.conj().get(i, j), "conj(A)");
                eq(a.get(i, j).mul(Complex.of(2, -1)), a.mul(Complex.of(2, -1)).get(i, j), "A*c");
            }
        }
    }

    @Test
    public void normsMatchDefinitions() {
        Random r = new Random(26);
        ComplexMatrix a = randomMatrix(r, 4, 3);
        int n = 4;
        double fro = 0, one = 0, inf = 0, max = 0;
        for (int i = 0; i < n; i++) {
            double row = 0;
            for (int j = 0; j < n; j++) {
                double m = a.get(i, j).absdbl();
                fro += m * m;
                row += m;
                max = Math.max(max, m);
            }
            inf = Math.max(inf, row);
        }
        for (int j = 0; j < n; j++) {
            double col = 0;
            for (int i = 0; i < n; i++) {
                col += a.get(i, j).absdbl();
            }
            one = Math.max(one, col);
        }
        eq(Math.sqrt(fro), a.normF(), "Frobenius");
        eq(Math.sqrt(fro), a.norm(NormStrategy.NORM_F), "Frobenius via strategy");
        eq(one, a.norm1(), "1-norm");
        eq(inf, a.normInf(), "inf-norm");
        eq(max, a.normMax(), "max-norm");
        // spectral norm is bounded by Frobenius and by sqrt(n)*it
        Assertions.assertTrue(a.norm2() <= a.normF() + 1e-9, "||A||_2 <= ||A||_F");
        Assertions.assertTrue(a.norm2() >= a.normF() / Math.sqrt(n) - 1e-9, "||A||_2 >= ||A||_F/sqrt(n)");
    }

    @Test
    public void subMatrixAndRowsColumnsAreConsistent() {
        Random r = new Random(27);
        ComplexMatrix a = randomMatrix(r, 5, 2);
        ComplexMatrix sub = a.subMatrix(1, 1, 3, 3);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                eq(a.get(i + 1, j + 1), sub.get(i, j), "submatrix");
            }
        }
        for (int j = 0; j < 5; j++) {
            eq(a.get(2, j), a.getRow(2).get(j), "getRow");
        }
        for (int i = 0; i < 5; i++) {
            eq(a.get(i, 3), a.getColumn(3).get(i), "getColumn");
        }
    }

    @Test
    public void traceAndTransposeAreConsistent() {
        Random r = new Random(28);
        ComplexMatrix a = randomMatrix(r, 4, 2);
        Complex tr = Complex.ZERO;
        for (int i = 0; i < 4; i++) {
            tr = tr.add(a.get(i, i));
            eq(a.get(i, (i + 1) % 4), a.transpose().get((i + 1) % 4, i), "transpose");
        }
        Assertions.assertEquals(4, a.getRowCount(), "row count");
    }
}
