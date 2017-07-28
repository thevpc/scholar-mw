package net.vpc.scholar.hadruwaves.mom.modes;

//package net.vpc.scholar.tmwlib.mom.fnbase;
//
//import net.vpc.scholar.math.Complex;
//import static net.vpc.scholar.math.Math2.minusOnePower;
//import static net.vpc.scholar.math.Math2.sqr;
//import static net.vpc.scholar.math.Physics.U0;
//import net.vpc.scholar.math.functions.cfxy.CExpFunctionXY;
//import net.vpc.scholar.math.functions.cfxy.CFunctionXY2D;
//
//import static java.lang.Math.PI;
//import static java.lang.Math.sqrt;
//
///**
// * Created by IntelliJ IDEA.
// * User: vpc
// * Date: 5 juil. 2005
// * Time: 23:27:24
// * To change this template use File | Settings | File Templates.
// */
//public class FnPeriodic extends FnBaseFunctions {
//
//
//    public FnPeriodic() {
//        super();
//    }
//
//    public void setFnMax(int fnMax) {
//        setFn(-fnMax,fnMax);
//    }
//
//    public CFunctionXY2D fn(int n) {
//        //TODO fait
//        return new CExpFunctionXY(minusOnePower(n) / sqrt(domain.width), 2 * PI * n / domain.width, 0, domain);
//    }
//
//    public Complex firstGamma(int n) {
//        return new Complex(sqr(2 * n * PI / domain.width) - sqr(K0)).sqrt();
//    }
//
//
//    public Complex zn(int n) {
//        Complex g =new Complex(0, omega * U0).divide(firstGamma(n));
//        return g;
//                //new Complex(0, sqrt(U0 / EPS0) / sqrt(sqr(2 * n * Math.PI / (K0 * domain.width)) - 1));
//    }
//
//}
