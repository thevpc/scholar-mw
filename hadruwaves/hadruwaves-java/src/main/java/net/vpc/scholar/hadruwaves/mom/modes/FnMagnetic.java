//package net.vpc.scholar.tmwlib.mom.modes;
//
////package net.vpc.scholar.tmwlib.mom.fnbase;
////
////import net.vpc.scholar.math.Complex;
////import static net.vpc.scholar.math.Math2.sqr;
////import net.vpc.scholar.math.Physics;
////import static net.vpc.scholar.math.Physics.U0;
////import net.vpc.scholar.math.functions.cfxy.CFunctionXY;
////import net.vpc.scholar.math.functions.cfxy.CFunctionXY2D;
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
////public class FnMagnetic extends FnBaseFunctions{
////    public FnMagnetic() {
////        super();
////    }
////
////    public void setFnMax(int fnMax) {
////        setFn(0,fnMax);
////    }
////
////    public CFunctionXY2D fn(int n) {
////        return new CFunctionXY(Physics.fnMagnetic(n, domain));
//////        return n == 0 ?
////////                cst(sqrt(1 / a), domain)
//////                cosX(sqrt(1 / a), 0, 0, domain)
//////                :
//////                (DFunction) cosX(sqrt(2 / a),
//////                        2 *
//////                n * PI / a,
//////                        2 * n * PI / 2,
//////                        domain);
//////                return cosX(
//////                        sqrt(2 / domain.width),
//////                        n * PI / domain.width,
//////                        -(n * PI / domain.width)*domain.xmin,
//////                        domain);
////    }
////
////    public Complex firstBoxSpaceGamma(int n) {
////        return new Complex(sqr(n * PI / domain.width) - sqr(K0)).sqrt();
////    }
////
////
////    public Complex zn(int n) {
////        Complex g =new Complex(0, omega * U0).divide(firstBoxSpaceGamma(n));
//////        if(n==1){
//////            System.out.println("a/lamda="+(domain.width/lambda(f))+" f="+f+": z("+n+") = "+g);
//////        }
////        return g;
////    }
////
////
////}
