//package net.vpc.scholar.hadrumaths.test;
//
//import net.vpc.scholar.hadrumaths.Axis;
//import net.vpc.scholar.hadrumaths.Expr;
//import net.vpc.scholar.hadrumaths.FormatFactory;
//import net.vpc.scholar.hadrumaths.Maths;
//import net.vpc.scholar.hadrumaths.symbolic.*;
//import org.junit.Test;
//
//import static net.vpc.scholar.hadrumaths.Maths.*;
//
//public class TestFormat {
//    @Test
//    public void testOne() {
//        Maths.Config.setCacheExpressionPropertiesEnabled(false);
//        Expr k=param("k");
//        Expr r1=î*4.0*Y;
//        Expr r2=cos(î*4.0*Y);
////        î.multiply()
//        Expr e = transformAxis(cos(X) + X * 2 / 4 - any(Y*î), Axis.Y, Axis.X, Axis.Z)
//                + DomainExpr.forBounds(k,k+1)+xdomain(1,9)
//                * neg(new Linear(8,6,9,xdomain(1,9)))
//                *inv(real(r2)) / pow(imag(r2),expr(5))
//                - abs(new CosXCosY(8,8,9,9,8,xdomain(1,9)))
//                + new ComplexValue(î,domain(1,9,-1,9))
////                + new CExp(-5,-2,-3,domain(1,9,-1,9))
//                * new DefaultDoubleToComplex(expr(2).toDD(),expr(2).toDD())
//                ;
////        Expr e = new CosXCosY(8,0,0,9,8,xdomain(1,9))
//                ;
//        System.out.println(e.toString());
//        System.out.println(FormatFactory.format(e));
//    }
//}
