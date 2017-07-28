//package net.vpc.scholar.tmwlib.mom.modes;
//
////package net.vpc.scholar.tmwlib.mom.fnbase;
////
////import net.vpc.scholar.math.Complex;
////import static net.vpc.scholar.math.Math2.sqr;
////import static net.vpc.scholar.math.Physics.U0;
////import net.vpc.scholar.math.functions.cfxy.CFunctionXY;
////import net.vpc.scholar.math.functions.cfxy.CFunctionXY2D;
////import net.vpc.scholar.math.functions.dfxy.DCosCosFunctionXY;
////
////import static java.lang.Math.PI;
////
/////**
//// * Created by IntelliJ IDEA.
//// * User: vpc
//// * Date: 5 juil. 2005
//// * Time: 23:27:24
//// * To change this template use File | Settings | File Templates.
//// */
////public class FnMagneticPaireVariation2 extends FnBaseFunctions implements Cloneable {
////    public FnMagneticPaireVariation2() {
////
////    }
////
////    public void setFnMax(int fnMax) {
////        setFn(0, fnMax);
////    }
////
////    public CFunctionXY2D fn(int n) {
////
////
////        double s = domain.width * domain.height;
////        double w = domain.width;
////        int k = 2 * n;
////        double amp = k == 0 ? Math.sqrt(1 / s) : Math.sqrt(2 / s);
////        return new CFunctionXY(
////                new DCosCosFunctionXY(amp *((n%2)==0?1:-1), Math.PI * k / w,
////                -Math.PI * k * domain.xmin / w,
////                0, 0, domain)
////        );
////    }
////
////    public Complex firstGamma(int n) {
////        return new Complex(sqr(2 * n * PI / domain.width) - sqr(K0)).sqrt();
////    }
////
////    public Complex zn(int n) {
////        Complex g = new Complex(0, omega * U0).divide(firstGamma(n));
//////        if(n==1){
//////            System.out.println("a/lamda="+(domain.width/lambda(f))+" f="+f+": z("+n+") = "+g);
//////        }
////        return g;
////    }
////
////}
