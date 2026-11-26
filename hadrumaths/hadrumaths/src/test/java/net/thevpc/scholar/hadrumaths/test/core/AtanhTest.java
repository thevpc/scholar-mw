package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

/**
 * atanh: C99 branch cuts on Complex, and the Expr node wiring.
 */
public class AtanhTest {

    static double tol(double v) {
        return 1e-12 * Math.max(1.0, Math.abs(v));
    }

    static void eq(double e, double a, String what) {
        Assertions.assertEquals(e, a, tol(e), what);
    }

    static void eq(Complex e, Complex a, String what) {
        eq(e.getReal(), a.getReal(), what + " (re)");
        eq(e.getImag(), a.getImag(), what + " (im)");
    }

    @Test
    public void complexAtanhMatchesC99() {
        // values from cmath.atanh
        eq(Complex.of(0.5493061443340549), Complex.of(0.5).atanh(), "atanh(0.5)");
        eq(Complex.of(0.5493061443340549, Math.PI / 2), Complex.of(2).atanh(), "atanh(2)");
        eq(Complex.of(-0.5493061443340549, Math.PI / 2), Complex.of(-2).atanh(), "atanh(-2)");
        eq(Complex.of(0.40235947810852507, 1.3389725222944935), Complex.of(2, 1).atanh(), "atanh(2+i)");
        eq(Complex.of(0, -1.1071487177940904), Complex.of(0, -2).atanh(), "atanh(-2i)");
        eq(0.0, Complex.ZERO.atanh().getReal(), "atanh(0)");
        eq(Double.POSITIVE_INFINITY, Complex.ONE.atanh().getReal(), "atanh(1)");
        eq(Double.NEGATIVE_INFINITY, Complex.MINUS_ONE.atanh().getReal(), "atanh(-1)");
    }

    @Test
    public void atanhInvertsTanh() {
        Random r = new Random(31);
        for (int i = 0; i < 3000; i++) {
            // principal domain of atanh: |Im| < pi/2
            Complex z = Complex.of((r.nextDouble() * 2 - 1) * 3, (r.nextDouble() * 2 - 1) * 1.4);
            eq(z, z.tanh().atanh(), "atanh(tanh z)");
        }
    }

    @Test
    public void expressionNodeEvaluates() {
        Expr e = Maths.X.atanh();
        Assertions.assertTrue(e.toString().contains("atanh"), "prints as atanh: " + e);
        eq(0.5493061443340549, e.toDC().evalComplex(0.5).getReal(), "expr atanh(0.5)");
        eq(0.0, e.toDC().evalComplex(0.5).getImag(), "expr atanh(0.5) imag");
        Complex at2 = e.toDC().evalComplex(2.0);
        eq(0.5493061443340549, at2.getReal(), "expr atanh(2) re");
        eq(Math.PI / 2, at2.getImag(), "expr atanh(2) im");
    }

    @Test
    public void constantExpressionIsFoldedThroughTheNode() {
        Expr c = Maths.expr(0.5).atanh();
        eq(0.5493061443340549, c.toDC().evalComplex(0.0).getReal(), "constant atanh");
    }

    @Test
    public void matrixAtanhIsElementWise() {
        Complex[][] m = {
                {Complex.of(0.5), Complex.of(0.0)},
                {Complex.of(-0.25), Complex.of(0, 0.5)}};
        net.thevpc.scholar.hadrumaths.ComplexMatrix r = Maths.matrix(m).atanh();
        eq(m[0][0].atanh(), r.get(0, 0), "m[0,0]");
        eq(m[1][1].atanh(), r.get(1, 1), "m[1,1]");
        eq(Complex.ZERO, r.get(0, 1), "m[0,1]");
    }
}
