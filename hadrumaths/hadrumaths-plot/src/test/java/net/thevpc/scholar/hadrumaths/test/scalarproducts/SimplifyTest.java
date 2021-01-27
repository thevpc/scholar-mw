package net.thevpc.scholar.hadrumaths.test.scalarproducts;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static net.thevpc.scholar.hadrumaths.Maths.*;
import net.thevpc.scholar.hadrumaths.ScalarProductOperatorFactory;
import net.thevpc.scholar.hadrumaths.Complex;

public class SimplifyTest {
    @Test
    public void test2() {
        System.out.println(Maths.simplify(Complex.of(3)).getType());
        System.out.println(FORMAL_SP.getSimplifier().rewriteOrSame(Complex.of(3),null).getType());;
    }
    @Test
    public void test1() {
        FormalScalarProductOperator o = (FormalScalarProductOperator) ScalarProductOperatorFactory.formal();
        Expr simplfied = o.getSimplifier().rewrite(sin(X), null).getValue();
        String s = simplfied.toString();
        System.out.println("simplified : " + s);
        Expr f1 = prod(X, expr(3), expr(6), Y);
        Expr f2 = mul(X, new Linear(0, 18, 0, Domain.FULLXY));
        Expr f1_simplified = simplify(f1);
        System.out.println(f1);
        System.out.println(f1_simplified);
        System.out.println(f2);
        Assertions.assertEquals("X * (18.0 * Y)", f2.toString());
        Assertions.assertEquals("18 * X * Y", f1_simplified.toString());
    }
}
