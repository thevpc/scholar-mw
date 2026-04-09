package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExprTest {

    private static final double DELTA = 1e-9;

    @Test
    public void testExprEvaluation() {
        Expr x = Maths.X;
        Expr y = Maths.Y;

        // f = 2*x + y
        Expr f = x.mul(2).plus(y);

        Assertions.assertEquals(5.0, f.toDD().evalDouble(2.0, 1.0, 0.0), DELTA);
        Assertions.assertEquals(4.0, f.toDD().evalDouble(1.0, 2.0, 0.0), DELTA);
    }

    @Test
    public void testTrigonometricExpr() {
        Expr x = Maths.X;
        Expr f = x.cos().plus(x.sin());

        Assertions.assertEquals(Math.cos(Math.PI/4) + Math.sin(Math.PI/4), f.toDD().evalDouble(Math.PI/4), DELTA);
        Assertions.assertEquals(1.0, f.toDD().evalDouble(0), DELTA);
        Assertions.assertEquals(1.0, f.toDD().evalDouble(Math.PI/2), DELTA);
    }

    @Test
    public void testExprDerivative() {
        Expr x = Maths.X;
        // f = x^2
        Expr f = x.mul(x);
        
        Expr df = Maths.derive(f, net.thevpc.scholar.hadrumaths.Axis.X); 
        
        // derivative is 2*x
        Assertions.assertEquals(4.0, df.toDD().evalDouble(2.0), DELTA);
        Assertions.assertEquals(6.0, df.toDD().evalDouble(3.0), DELTA);
    }

    @Test
    public void testComplexExpr() {
        Expr c = Maths.expr(Complex.of(2, 3));
        Expr x = Maths.X;
        
        Expr f = x.mul(c); // (2+3i)*x
        
        Complex r = f.toDC().evalComplex(2.0); // 2 * (2+3i) = 4 + 6i
        Assertions.assertEquals(4.0, r.getReal(), DELTA);
        Assertions.assertEquals(6.0, r.getImag(), DELTA);
    }
}
