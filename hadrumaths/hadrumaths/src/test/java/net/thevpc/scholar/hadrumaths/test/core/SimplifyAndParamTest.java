package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Simplification must preserve values, composition must substitute, and parameters must
 * substitute / inflate correctly. These are the transformations the rest of the library
 * silently relies on.
 */
public class SimplifyAndParamTest {

    static final double EPS = 1e-9;

    static void assertSameValues(Expr a, Expr b, String what) {
        double[] xs = {-1.3, -0.2, 0.0, 0.4, 1.7};
        double[] ys = {0.3, -0.8, 0.0, 1.1, 0.5};
        for (int i = 0; i < xs.length; i++) {
            Complex va = a.toDC().evalComplex(xs[i], ys[i]);
            Complex vb = b.toDC().evalComplex(xs[i], ys[i]);
            Assertions.assertEquals(va.getReal(), vb.getReal(), EPS, what + " re at (" + xs[i] + "," + ys[i] + ")");
            Assertions.assertEquals(va.getImag(), vb.getImag(), EPS, what + " im at (" + xs[i] + "," + ys[i] + ")");
        }
    }

    @Test
    public void simplifyPreservesValues() {
        Domain d = Domain.ofBounds(-2, 2, 0, 1);
        Expr[] exprs = {
                Maths.X.mul(2).plus(Maths.Y).sub(1),
                Maths.X.plus(0),
                Maths.X.mul(1),
                Maths.X.mul(0),
                Maths.X.plus(Maths.X),
                Maths.X.sub(Maths.X),
                Maths.X.mul(Maths.X).div(Maths.X.plus(3)),
                Maths.X.sin().sqr().plus(Maths.X.cos().sqr()),
                FunctionFactory.cosXcosY(1.5, 1.0, 0.2, 0.5, -0.1, d),
                Maths.expr(2.0, d).plus(Maths.expr(3.0, d)),
        };
        for (Expr e : exprs) {
            Expr s = e.simplify();
            assertSameValues(e, s, "simplify(" + e + ")");
        }
    }

    @Test
    public void reciprocalSurvivesSimplification() {
        // InvSimplifyRule used to rebuild inv(x) as -x whenever the child changed
        Expr inv = Maths.Y.sqr().plus(1).inv();
        for (double y : new double[]{-1.3, 0.0, 0.4, 1.7}) {
            Assertions.assertEquals(1.0 / (y * y + 1),
                    inv.simplify().toDC().evalComplex(0.5, y).getReal(), 1e-12, "1/(y^2+1) at " + y);
            Assertions.assertEquals(1.0 / (y * y + 1) - 1,
                    inv.plus(-1).simplify().toDC().evalComplex(0.5, y).getReal(), 1e-12, "1/(y^2+1)-1 at " + y);
        }
    }

    @Test
    public void productWithASumKeepsAllDenominators() {
        // a spurious 1* multiplier around inv(...) used to make the product simplifier
        // drop a denominator (value changed from 0.02075 to -0.03756)
        Expr t = Maths.Y.mul(Maths.X.mul(2)).div(Maths.X.sqr().plus(1).sqr())
                .mul(Maths.Y.div(Maths.X.sqr().plus(1)).sin());
        Expr h = Maths.Y.sqr().plus(1).inv().plus(-1);
        double x = -1.7, y = 0.9;
        Expr p = t.mul(h);
        Assertions.assertEquals(p.toDC().evalComplex(x, y).getReal(),
                p.simplify().toDC().evalComplex(x, y).getReal(), 1e-10, "simplify must preserve the value");
    }

    @Test
    public void obviousSimplifications() {
        Assertions.assertTrue(Maths.X.mul(0).simplify().isZero(), "x*0 is zero");
        // x-x: the simplifier may keep the node, the value is what matters
        Assertions.assertEquals(0.0, Maths.X.sub(Maths.X).simplify().toDD().evalDouble(1.7), EPS, "x-x is zero");
        Assertions.assertEquals(1.0, Maths.X.mul(2).simplify().toDD().evalDouble(0.5), EPS);
        // x + x -> 2x (value check, not structure)
        Assertions.assertEquals(2.0, Maths.X.plus(Maths.X).simplify().toDD().evalDouble(1.0), EPS);
    }

    @Test
    public void composeSubstitutesVariables() {
        Expr f = Maths.X.mul(Maths.Y).plus(Maths.X);
        Expr g = f.compose(Maths.expr(2.0), Maths.expr(3.0));
        Assertions.assertEquals(8.0, g.toDD().evalDouble(0.0, 0.0), EPS, "2*3+2");

        // substituting only X leaves Y alone
        Expr h = Maths.X.mul(Maths.Y).compose(Maths.expr(2.0), null);
        Assertions.assertEquals(6.0, h.toDD().evalDouble(0.0, 3.0), EPS, "2*3");
    }

    @Test
    public void setParamSubstitutesByName() {
        Expr p = Maths.param("p");
        Expr f = Maths.X.plus(p);
        Assertions.assertEquals(3.0, f.setParam("p", 2).toDD().evalDouble(1.0), EPS, "x+p with p=2");
        // unknown parameter: p stays symbolic, and a symbolic parameter has no numeric value
        Expr stillSymbolic = f.setParam("other", 5);
        Assertions.assertTrue(stillSymbolic.hasParams(), "p is still a parameter");
        Assertions.assertThrows(RuntimeException.class, () -> stillSymbolic.toDD().evalDouble(2.0),
                "evaluating an unassigned parameter must fail, not return a wrong number");
    }

    @Test
    public void inflateGeneratesOneExpressionPerParameterValue() {
        DoubleParam p = Maths.param("p");
        Expr f = Maths.X.plus(p);
        ExprVector v = f.inflate(p.times(0, 2, 3));
        Assertions.assertEquals(3, v.size(), "three values: 0,1,2");
        double[] values = new double[v.size()];
        for (int i = 0; i < v.size(); i++) {
            values[i] = v.get(i).toDD().evalDouble(0.0);
        }
        java.util.Arrays.sort(values);
        Assertions.assertArrayEquals(new double[]{0.0, 1.0, 2.0}, values, 1e-9);
    }

    @Test
    public void paramIsAnExpressionAndCanBeEvaluated() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        DoubleParam p = Maths.param("p");
        Expr f = Maths.X.mul(p).mul(d);
        Assertions.assertEquals(2.5, f.setParam("p", 5).toDD().evalDouble(0.5, 0.5), EPS);
        Assertions.assertEquals(0.0, f.toDD().evalDouble(2.0, 0.5), EPS, "outside support");
    }
}
