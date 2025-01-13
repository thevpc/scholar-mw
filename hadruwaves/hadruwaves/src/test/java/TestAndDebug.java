//package test;
//
//import net.thevpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
//import net.thevpc.scholar.math.functions.DomainXY;
//import net.thevpc.scholar.math.Math2;
//
///**
// * Created by IntelliJ IDEA.
// * User: vpc
// * Date: 8 avr. 2007
// * Time: 15:48:13
// * To change this template use File | Settings | File Templates.
// */
//public class TestAndDebug {
//    public static void main(String[] args) {
//        DCosCosFunctionXY x1=new DCosCosFunctionXY(1,0,0,0.03,-1.5708096509060077,new DomainXY(-4.996541666666667E-4,2.220685100472768E-4,4.996541666666667E-4,2.7758563967690645E-4));
//        DCosCosFunctionXY x2=new DCosCosFunctionXY(1,0,0,0.03,-Math.PI,new DomainXY(-4.996541666666667E-4,2.220685100472768E-4,4.996541666666667E-4,2.7758563967690645E-4));
//        System.out.println(1.5708096509060077-(Math.PI/2));
//        sp(x1);
//        sp(x2);
//
//    }
//    private static void sp(DCosCosFunctionXY x0){
//        double v1 = Math2.FORMAL_SCALAR_PRODUCT.scalarProduct(x0, x0);
//        double v2 = Math2.NUMERIC_SCALAR_PRODUCT.scalarProduct(x0, x0);
//        double v3 = Math2.NUMERIC_SIMP_SCALAR_PRODUCT.scalarProduct(x0, x0);
//        System.out.println("FORMAL = " + v1);
//        System.out.println("NUMERIC = " + v2);
//        System.out.println("NUMERIC_SIMP = " + v3);
//    }
//}
