package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The analytic scalar-product rules (FormalScalarProductOperator) must agree with numeric
 * quadrature. FORMAL_SP is the "hard" variant, so a missing rule fails loudly instead of
 * silently falling back.
 */
public class ScalarProductCrossCheckTest {

    static final double PI = Math.PI;
    static final double TOL = 5e-4;   // quadrature error of the numeric reference

    static void crossCheck(String what, Domain d, Expr f, Expr g) {
        Complex formal = Maths.FORMAL_SP.eval(d, f, g).toComplex();
        Complex numeric = Maths.NUMERIC_SP.eval(d, f, g).toComplex();
        Assertions.assertEquals(numeric.getReal(), formal.getReal(),
                TOL * Math.max(1.0, Math.abs(numeric.getReal())), what + " (re)");
        Assertions.assertEquals(numeric.getImag(), formal.getImag(),
                TOL * Math.max(1.0, Math.abs(numeric.getImag())), what + " (im)");
    }

    @Test
    public void cosCosVsCosCos() {
        Domain d = Domain.ofBounds(0, 3, -1, 2);
        Expr f = FunctionFactory.cosXcosY(1.3, 2 * PI / 3, 0.2, 3 * PI / 2, -0.4, d);
        Expr g = FunctionFactory.cosXcosY(0.7, PI / 2, 0.1, 2 * PI / 5, 0.3, d);
        crossCheck("coscos x coscos", d, f, g);
    }

    @Test
    public void cosCosVsCosCosSame() {
        Domain d = Domain.ofBounds(-1, 2, 0, 1.5);
        Expr f = FunctionFactory.cosXcosY(1.1, 1.7, 0.3, 2.3, -0.2, d);
        crossCheck("coscos x itself", d, f, f);
        // Hermitian: <f,f> must be real and positive
        Complex self = Maths.FORMAL_SP.eval(d, f, f).toComplex();
        Assertions.assertEquals(0.0, self.getImag(), 1e-10, "<f,f> imaginary part");
        Assertions.assertTrue(self.getReal() > 0, "<f,f> must be positive");
    }

    @Test
    public void cosCosVsLinear() {
        Domain d = Domain.ofBounds(0, 2, 0, 1);
        Expr c = FunctionFactory.cosXcosY(1.0, PI, 0.0, 2 * PI, 0.0, d);
        Expr lin = Maths.X.mul(1.2).plus(0.5).mul(d);
        crossCheck("coscos x linear", d, c, lin);
    }

    @Test
    public void linearVsLinear() {
        Domain d = Domain.ofBounds(-1, 2, 0, 2);
        Expr a = Maths.X.mul(d);
        Expr b = Maths.Y.plus(1.0).mul(d);
        crossCheck("linear x linear", d, a, b);
    }

    @Test
    public void cosXPlusYVsCosXPlusY() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr a = FunctionFactory.cosXPlusY(1.2, 2.0, 1.0, 0.3, d);
        Expr b = FunctionFactory.cosXPlusY(0.8, -1.0, 2.5, -0.2, d);
        crossCheck("cos(x+y) x cos(x+y)", d, a, b);
    }

    @Test
    public void constantsVsConstants() {
        Domain d = Domain.ofBounds(0, 2, 0, 3);
        Expr a = Maths.expr(1.5, d);
        Expr b = Maths.expr(-0.75, d);
        crossCheck("cst x cst", d, a, b);
        // exact: <a,b> = 1.5 * -0.75 * area(6)
        Complex sp = Maths.FORMAL_SP.eval(d, a, b).toComplex();
        Assertions.assertEquals(1.5 * -0.75 * 6, sp.getReal(), 1e-12, "exact constant integral");
    }

    @Test
    public void cosCosVsConstant() {
        Domain d = Domain.ofBounds(0, 2 * PI, 0, PI);
        Expr c = FunctionFactory.cosXcosY(1.0, 1.0, 0.0, 1.0, 0.0, d);
        Expr one = Maths.expr(2.0, d);
        crossCheck("coscos x cst", d, c, one);
        // integral of cos(x)cos(y) over [0,2pi]x[0,pi] = 0
        Complex sp = Maths.FORMAL_SP.eval(d, c, one).toComplex();
        Assertions.assertEquals(0.0, sp.getReal(), 1e-9, "orthogonality to constant");
    }

    @Test
    public void linearVsConstant() {
        Domain d = Domain.ofBounds(0, 4, 0, 1);
        Expr lin = Maths.X.plus(0.1).mul(d);
        Expr cst = Maths.expr(3.0, d);
        crossCheck("linear x cst", d, lin, cst);
        // integral of 3*(x + 0.1) over x in [0,4], y in [0,1] = 3*(8 + 0.4)
        Complex sp = Maths.FORMAL_SP.eval(d, lin, cst).toComplex();
        Assertions.assertEquals(3.0 * (8.0 + 0.4), sp.getReal(), 1e-9, "exact linear integral");
    }
}
