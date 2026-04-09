package net.thevpc.scholar.hadrumaths.test.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;

public class ScalarProductTest {
    
    private static final double DELTA = 1e-9;
    // DQuadIntegralXY has tolerance=1e-6; for 2D the error accumulates from nested calls
    private static final double NUMERIC_DELTA = 1e-6;

    @Test
    public void testScalarProductConstants() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr f1 = Maths.expr(2.0, d);
        Expr f2 = Maths.expr(3.0, d);

        // integral of 2 * 3 on area 1x1 = 6
        Complex sp = Maths.scalarProduct(f1, f2).toComplex();
        Assertions.assertEquals(6.0, sp.getReal(), DELTA);
        Assertions.assertEquals(0.0, sp.getImag(), DELTA);
    }
    
    @Test
    public void testScalarProduct1D() {
        Domain d = Domain.ofBounds(0, 2);
        // f1 = x
        Expr f1 = Maths.X.mul(d);
        // f2 = 1
        Expr f2 = Maths.expr(1.0, d);
        
        // integral of x * 1 from 0 to 2 = [x^2/2] = 2
        Complex sp = Maths.scalarProduct(f1, f2).toComplex();
        Assertions.assertEquals(2.0, sp.getReal(), DELTA);
        Assertions.assertEquals(0.0, sp.getImag(), DELTA);
    }

    @Test
    public void testScalarProduct2D() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        // f1 = XY
        Expr f1 = Maths.X.mul(Maths.Y).mul(d);
        // f2 = XY
        Expr f2 = Maths.X.mul(Maths.Y).mul(d);
        
        // integral of X^2 Y^2 from 0 to 1, 0 to 1 = (1/3) * (1/3) = 1/9
        Complex sp = Maths.scalarProduct(f1, f2).toComplex();
        Assertions.assertEquals(1.0/9.0, sp.getReal(), NUMERIC_DELTA);
        Assertions.assertEquals(0.0, sp.getImag(), NUMERIC_DELTA);
    }
    
    @Test
    public void testScalarProductOrthogonal() {
        Domain d = Domain.ofBounds(0, 2*Math.PI);
        // f1 = cos(x)
        Expr f1 = Maths.X.cos().mul(d);
        // f2 = sin(x)
        Expr f2 = Maths.X.sin().mul(d);
        
        System.out.println("f1 = " + f1);
        System.out.println("f2 = " + f2);
        System.out.println("f1 domain = " + f1.getDomain());
        System.out.println("f2 domain = " + f2.getDomain());
        // integral of cos(x)sin(x) from 0 to 2pi = 0
        Complex sp = Maths.scalarProduct(f1, f2).toComplex();
        System.out.println("sp = " + sp);
        Assertions.assertEquals(0.0, sp.getReal(), DELTA);
        Assertions.assertEquals(0.0, sp.getImag(), DELTA);
    }
}
