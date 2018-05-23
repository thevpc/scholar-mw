package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.RightArrowUplet2;
import net.vpc.scholar.hadrumaths.symbolic.*;
import org.junit.Test;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class TestFormat2 {
    @Test
    public void testOne() {
        Config.setCacheExpressionPropertiesEnabled(false);
        Expr k = param("k");
        Expr r1 = î.mul(4.0).mul(Y);
        Expr r2 = cos(î).mul(4.0).mul(Y);
//        î.multiply()
        Expr e =
                add(
                        transformAxis(add(cos(X), mul(X, complex(2 / 4.0)), neg(any(Y.mul(î)))), Axis.Y, Axis.X, Axis.Z)

                        , DomainExpr.forBounds(k, add(k, 1))
                        ,
                        mul(
                                xdomain(1, 9)
                                , neg(new Linear(8, 6, 9, xdomain(1, 9)))
                                , div(inv(real(r2)), pow(imag(r2), expr(5)))
                        )
                        ,
                        neg(abs(new CosXCosY(8, 8, 9, 9, 8, xdomain(1, 9))))
                        ,
                        mul(new ComplexValue(î, domain(new RightArrowUplet2.Double(1, 9), new RightArrowUplet2.Double(-1, 9)))
//                + new CExp(-5,-2,-3,domain(1,9,-1,9))
                                , new DCxy(expr(2).toDD(), expr(2).toDD()))
                );
//        Expr e = new CosXCosY(8,0,0,9,8,xdomain(1,9))
        ;
        System.out.println(e.toString());
        System.out.println(FormatFactory.format(e));
    }
}
