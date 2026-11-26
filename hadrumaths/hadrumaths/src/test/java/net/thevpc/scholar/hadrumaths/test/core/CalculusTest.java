package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.integration.IntegrationOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Calculus paths: symbolic differentiation (checked against a central difference) and numeric
 * integration (checked against closed-form values). Neither had any test before.
 */
public class CalculusTest {

    static final double H = 1e-5;

    static void assertDerivative(Expr f, Axis axis, double x, double y, String what) {
        Expr df = Maths.derive(f, axis);
        double numeric;
        if (axis == Axis.X) {
            numeric = (f.toDD().evalDouble(x + H, y) - f.toDD().evalDouble(x - H, y)) / (2 * H);
        } else {
            numeric = (f.toDD().evalDouble(x, y + H) - f.toDD().evalDouble(x, y - H)) / (2 * H);
        }
        double symbolic = df.toDD().evalDouble(x, y);
        Assertions.assertEquals(numeric, symbolic, 1e-4 * Math.max(1.0, Math.abs(numeric)),
                what + " d/d" + axis + " at (" + x + "," + y + ") : " + df);
    }

    @Test
    public void derivativeOfPolynomials() {
        for (double x : new double[]{-2, -0.5, 0, 0.5, 2, 3.5}) {
            assertDerivative(Maths.X.pow(Maths.expr(2.0)), Axis.X, x, 0, "x^2");
            assertDerivative(Maths.X.pow(Maths.expr(3.0)), Axis.X, x, 0, "x^3");
            assertDerivative(Maths.X.sqr(), Axis.X, x, 0, "sqr(x)");
            assertDerivative(Maths.X.plus(1).sqr(), Axis.X, x, 0, "(x+1)^2");
            assertDerivative(Maths.X.mul(Maths.Y), Axis.X, x, 1.5, "x*y");
            assertDerivative(Maths.X.mul(Maths.Y), Axis.Y, x, 1.5, "x*y in y");
        }
    }

    @Test
    public void derivativeOfTranscendentalFunctions() {
        for (double x : new double[]{-1.5, -0.3, 0, 0.3, 1.5}) {
            assertDerivative(Maths.X.sin(), Axis.X, x, 0, "sin");
            assertDerivative(Maths.X.cos(), Axis.X, x, 0, "cos");
            assertDerivative(Maths.X.exp(), Axis.X, x, 0, "exp");
            assertDerivative(Maths.X.sin().mul(Maths.X.exp()), Axis.X, x, 0, "sin*exp");
            assertDerivative(Maths.X.sqr().sin(), Axis.X, x, 0, "sin(x^2)");
            assertDerivative(Maths.X.cos().mul(Maths.X.exp()), Axis.X, x, 0, "cos*exp");
        }
    }

    @Test
    public void derivativeOfCosXCosYShapesHasTheRightSign() {
        // simplify turns 2*cos(X) into a CosXCosY; its differentiator used +amp*a instead of -amp*a
        for (double x : new double[]{-1.7, -0.4, 0, 0.6, 1.9}) {
            Assertions.assertEquals(-2 * Math.sin(x),
                    Maths.derive(Maths.X.cos().mul(2), Axis.X).toDD().evalDouble(x), 1e-6,
                    "d/dx 2cos(x) at " + x);
        }
    }

    @Test
    public void derivativeOfLinearNodes() {
        // simplify turns polynomials into Linear, which had no differentiator at all
        Expr d = Maths.derive(Maths.X.mul(2).plus(3), Axis.X);
        Assertions.assertEquals(2.0, d.toDD().evalDouble(0.7), 1e-9);
        Expr dy = Maths.derive(Maths.X.mul(2).plus(Maths.Y.mul(5)), Axis.Y);
        Assertions.assertEquals(5.0, dy.toDD().evalDouble(0.3, 1.1), 1e-9);
    }

    @Test
    public void derivativeOfQuotient() {
        for (double x : new double[]{-3, -1, 0, 0.5, 2}) {
            assertDerivative(Maths.X.div(Maths.X.plus(2)), Axis.X, x, 0, "x/(x+2)");
            assertDerivative(Maths.X.plus(1).div(Maths.X.sqr().plus(1)), Axis.X, x, 0, "(x+1)/(x^2+1)");
        }
    }

    // ------------------------------------------------------------------ integration
    @Test
    public void integralOfPolynomialsAndTrig() {
        IntegrationOperator io = IntegrationOperatorFactory.numeric();
        Domain unit = Domain.ofBounds(0, 1, 0, 1);
        Domain unit1 = Domain.ofBounds(0, 1);

        Assertions.assertEquals(1.0 / 3.0, io.eval(unit, Maths.X.sqr()).getReal(), 1e-6, "int x^2 on [0,1]^2");
        Assertions.assertEquals(1.0 / 3.0, io.eval(unit1, Maths.X.sqr()).getReal(), 1e-6, "int x^2 on [0,1]");
        Assertions.assertEquals(0.5, io.eval(unit, Maths.X).getReal(), 1e-6, "int x");
        Assertions.assertEquals(0.25, io.eval(unit, Maths.X.mul(Maths.Y)).getReal(), 1e-6, "int x*y");
        Assertions.assertEquals(2.0, io.eval(Domain.ofBounds(0, Math.PI, 0, 1), Maths.X.sin()).getReal(), 1e-6, "int sin");
        Assertions.assertEquals(Math.E - 1, io.eval(unit, Maths.X.exp()).getReal(), 1e-6, "int exp");
        Assertions.assertEquals(1.0,
                io.eval(Domain.ofBounds(0, Math.PI / 2, 0, Math.PI / 2),
                        Maths.X.cos().mul(Maths.Y.cos())).getReal(), 1e-6, "int cos*cos");
    }

    @Test
    public void integralOfARestrictedFunctionUsesItsSupport() {
        IntegrationOperator io = IntegrationOperatorFactory.numeric();
        Expr f = Maths.expr(1.0, Domain.ofBounds(0, 0.5, 0, 2));
        // integral over a larger box still sees only the support
        Assertions.assertEquals(0.5 * 2, io.eval(Domain.ofBounds(0, 1, 0, 2), f).getReal(), 1e-6);
    }

    @Test
    public void integralOfASumIsTheSumOfIntegrals() {
        IntegrationOperator io = IntegrationOperatorFactory.numeric();
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr a = Maths.X;
        Expr b = Maths.X.sqr();
        Complex whole = io.eval(d, a.plus(b));
        Complex parts = io.eval(d, a).add(io.eval(d, b));
        Assertions.assertEquals(parts.getReal(), whole.getReal(), 1e-6);
    }
}
