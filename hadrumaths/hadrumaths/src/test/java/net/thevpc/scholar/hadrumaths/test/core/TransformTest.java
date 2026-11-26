package net.thevpc.scholar.hadrumaths.test.core;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * C4: Expressions.crossDomainXY / translated / symmetric / opposite.
 * Before the fix these all threw NoSuchElementException (dispatch was keyed by the concrete
 * transform class while handlers were registered under ExpressionTransform.class).
 */
public class TransformTest {

    @BeforeAll
    static void init() {
        Nuts.require();
    }

    static final double COS_HALF = Math.cos(0.5);

    static Expr cosX(Domain d) {
        return FunctionFactory.cosXcosY(1.0, 1.0, 0.0, 0.0, 0.0, d);
    }

    // ------------------------------------------------------------------ crossDomainXY
    @Test
    public void crossDomainRestrictsTheSupport() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr e = Expressions.crossDomainXY(Maths.X, d);
        Assertions.assertEquals(0.0, e.getDomain().xmin(), 1e-12);
        Assertions.assertEquals(1.0, e.getDomain().xmax(), 1e-12);
        Assertions.assertEquals(0.5, e.toDD().evalDouble(0.5, 0.5), 1e-12, "value inside");
        Assertions.assertEquals(0.0, e.toDD().evalDouble(1.5, 0.5), 1e-12, "outside the restriction");
    }

    @Test
    public void crossDomainKeepsAProductAProduct() {
        // C5 end-to-end: the Mul structure transformer used to rebuild a sum
        Expr m = Mul.of(Maths.expr(2.0), Maths.X);
        Expr r = Expressions.crossDomainXY(m, Domain.ofBounds(0, 1, 0, 1));
        Assertions.assertTrue(r instanceof Mul, "must stay a product, got " + r.getClass().getSimpleName());
        Assertions.assertEquals(1.0, r.toDD().evalDouble(0.5, 0.5), 1e-12, "2*x at x=0.5");
        Assertions.assertEquals(0.0, r.toDD().evalDouble(2.0, 0.5), 1e-12, "outside");
    }

    // ------------------------------------------------------------------ translation
    @Test
    public void translateCoordinateLeaf() {
        Expr t = Expressions.translated(Maths.X, 2, 0);
        Assertions.assertEquals(0.5, t.toDD().evalDouble(2.5, 0.0), 1e-12, "x -> x-2");
        Assertions.assertEquals(-2.0, t.toDD().evalDouble(0.0, 0.0), 1e-12);
    }

    @Test
    public void translateShapeLeafMovesDomainAndPhase() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr t = Expressions.translated(cosX(d), 2, 0);
        Assertions.assertEquals(2.0, t.getDomain().xmin(), 1e-12, "support moved");
        Assertions.assertEquals(3.0, t.getDomain().xmax(), 1e-12);
        Assertions.assertEquals(COS_HALF, t.toDD().evalDouble(2.5, 0.5), 1e-9, "g(x)=f(x-2)");
        Assertions.assertEquals(0.0, t.toDD().evalDouble(0.5, 0.5), 1e-12, "old support is empty now");
    }

    @Test
    public void translateCompositeAppliesOnce() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr e = cosX(d).plus(cosX(d));
        Expr t = Expressions.translated(e, 2, 0);
        // if translation were applied once per tree level the value would drift
        Assertions.assertEquals(2 * COS_HALF, t.toDD().evalDouble(2.5, 0.5), 1e-9, "sum translated once");
        Assertions.assertEquals(2.0, t.getDomain().xmin(), 1e-12);
    }

    // ------------------------------------------------------------------ symmetry
    @Test
    public void symmetricAboutTargetDomain() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Domain target = Domain.ofBounds(0, 2, 0, 1);
        Expr s = Expressions.symmetric(cosX(d), Axis.X, target);
        Assertions.assertEquals(1.0, s.getDomain().xmin(), 1e-12, "mirrored support [1,2]");
        Assertions.assertEquals(2.0, s.getDomain().xmax(), 1e-12);
        Assertions.assertEquals(COS_HALF, s.toDD().evalDouble(1.5, 0.5), 1e-9, "f(2-x)");
    }

    @Test
    public void symmetricConstantOnlyMovesTheDomain() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr c = Maths.expr(3.0, d);
        Expr s = Expressions.symmetric(c, Axis.X, Domain.ofBounds(0, 2, 0, 1));
        Assertions.assertEquals(1.0, s.getDomain().xmin(), 1e-12);
        Assertions.assertEquals(2.0, s.getDomain().xmax(), 1e-12);
        Assertions.assertEquals(3.0, s.toDD().evalDouble(1.5, 0.5), 1e-12, "constant unchanged");
    }

    @Test
    public void symmetricOnYAxis() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr f = FunctionFactory.cosXcosY(1.0, 0.0, 0.0, 1.0, 0.0, d);
        Expr s = Expressions.symmetric(f, Axis.Y, Domain.ofBounds(0, 1, 0, 2));
        Assertions.assertEquals(1.0, s.getDomain().ymin(), 1e-12);
        Assertions.assertEquals(2.0, s.getDomain().ymax(), 1e-12);
        Assertions.assertEquals(Math.cos(0.5), s.toDD().evalDouble(0.5, 1.5), 1e-9);
    }

    @Test
    public void oppositeIsMirrorAboutZero() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Expr o = Expressions.opposite(cosX(d), Axis.X);
        Assertions.assertEquals(-1.0, o.getDomain().xmin(), 1e-12, "support [-1,0]");
        Assertions.assertEquals(0.0, o.getDomain().xmax(), 1e-12);
        Assertions.assertEquals(COS_HALF, o.toDD().evalDouble(-0.5, 0.5), 1e-9, "f(-x)");
    }

    @Test
    public void symmetricVectorIsComponentWise() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        DoubleToVector v = Maths.vector(cosX(d), cosX(d).mul(2)).toDV();
        Expr s = Expressions.symmetric(v, Axis.X, Domain.ofBounds(0, 2, 0, 1));
        Assertions.assertEquals(1.0, s.getDomain().xmin(), 1e-12);
        Assertions.assertEquals(2.0, s.getDomain().xmax(), 1e-12);
        // both components survive (the vector compose used to drop all but the first)
        DoubleToVector sv = s.toDV();
        Assertions.assertEquals(COS_HALF, sv.getComponent(Axis.X).toDD().evalDouble(1.5, 0.5), 1e-9, "component X");
        Assertions.assertEquals(2 * COS_HALF, sv.getComponent(Axis.Y).toDD().evalDouble(1.5, 0.5), 1e-9, "component Y");
    }

    @Test
    public void isXSymmetricRecognisesAnInvariantFunction() {
        Domain d = Domain.ofBounds(0, 1, 0, 1);
        Assertions.assertTrue(Expressions.isXSymmetric(Maths.expr(2.0, d)), "constant is X-symmetric");
        Assertions.assertFalse(Expressions.isXSymmetric(cosX(d)), "cos(x) on [0,1] is not X-symmetric");
    }
}
