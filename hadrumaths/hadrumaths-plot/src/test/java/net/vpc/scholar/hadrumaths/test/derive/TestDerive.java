package net.vpc.scholar.hadrumaths.test.derive;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TestDerive {

    @Test
    public void test1() {
        Expr e = Maths.expr("sin(cos(X))");
        Expr d = Maths.derive(e, Axis.X).simplify();
        System.out.println(d);
        Assertions.assertEquals("(-1) * sin(X) * cos(cos(X))", d.toString());
    }

    @Test
    public void test2() {
        Expr e = Maths.expr("sin(X*cos(X)*sqrt(X)+Y+1)");
        System.out.println(e);
        Expr d = Maths.derive(e, Axis.X).simplify();
        System.out.println(d);
        Assertions.assertEquals("((X * cos(X) * (1 / (2 * sqrt(X)))) + (((X * (-1) * sin(X)) + cos(X)) * sqrt(X))) * cos((X * cos(X) * sqrt(X)) + Y + 1)", d.toString());
    }
}
