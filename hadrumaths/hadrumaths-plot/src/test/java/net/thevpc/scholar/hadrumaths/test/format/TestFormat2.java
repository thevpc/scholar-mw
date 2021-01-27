package net.thevpc.scholar.hadrumaths.test.format;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.RightArrowUplet2;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.conv.DefaultDoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.Linear;
import org.junit.jupiter.api.Test;

import static net.thevpc.scholar.hadrumaths.Maths.*;

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
                        (add(cos(X), mul(X, complex(2 / 4.0)), neg(any(Y.mul(î))))).compose(Y,X,Z)

                        , DomainExpr.ofBounds(k, plus(k, 1))
                        ,
                        prod(
                                xdomain(1, 9)
                                , neg(new Linear(8, 6, 9, xdomain(1, 9)))
                                , div(inv(real(r2)), pow(imag(r2), expr(5)))
                        )
                        ,
                        neg(abs(new CosXCosY(8, 8, 9, 9, 8, xdomain(1, 9))))
                        ,
                        mul(new DefaultComplexValue(î, domain(new RightArrowUplet2.Double(1, 9), new RightArrowUplet2.Double(-1, 9)))
//                + new CExp(-5,-2,-3,domain(1,9,-1,9))
                                , new DefaultDoubleToComplex(expr(2).toDD(), expr(2).toDD()))
                );
//        Expr e = new CosXCosY(8,0,0,9,8,xdomain(1,9))
        ;
        System.out.println(e.toString());
        System.out.println(FormatFactory.format(e));
    }
}
