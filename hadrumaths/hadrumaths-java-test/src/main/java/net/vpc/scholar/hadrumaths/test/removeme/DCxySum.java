//package net.vpc.scholar.math.functions.cfxy;
//
//import java.util.ArrayList;
//
//import java.util.Collection;
//import net.vpc.scholar.math.Complex;
//import net.vpc.scholar.math.IDCxy;
//import net.vpc.scholar.math.IDDxy;
//import net.vpc.scholar.math.functions.DomainXY;
//import net.vpc.scholar.math.functions.FunctionFactory;
//import net.vpc.scholar.math.functions.dfxy.DxySum;
//
///**
// * Class Description
// *
// * @author Taha BEN SALAH (taha@ant-inter.com) User: taha Date: 14-aout-2004
// * Time: 22:01:08
// */
//@Deprecated
//public class DCxySum extends DCxyAbstractSum implements Cloneable {
//
//    private static final long serialVersionUID = -1010101010101001030L;
//
//    public DCxySum(Collection<IDCxy> sum) {
//        this(sum.toArray(new IDCxy[sum.size()]));
//    }
//
//    public DCxySum(IDCxy... sum) {
//        super(sum.length > 0 ? sum[0].getDomain() : null);
//        if (sum.length == 0) {
//            this.segments = new IDCxy[]{FunctionFactory.CZEROXY};
//            this.real = FunctionFactory.DZEROXY;
//            this.imag = FunctionFactory.DZEROXY;
//            this.domain = FunctionFactory.DZEROXY.getDomain();
//        } else {
//            ArrayList<IDCxy> a = new ArrayList<IDCxy>(sum.length);
//            linearize(sum, a);
//            this.segments = a.toArray(new IDCxy[a.size()]);
//            double xmin = Double.NaN;
//            double xmax = Double.NaN;
//            double ymin = Double.NaN;
//            double ymax = Double.NaN;
//            for (int i = 0; i < sum.length; i++) {
//                IDCxy dFunctionXY = sum[i];
//                DomainXY d = dFunctionXY.getDomain();
//                if (Double.isNaN(xmin) || d.xmin < xmin) {
//                    xmin = d.xmin;
//                }
//                if (Double.isNaN(ymin) || d.ymin < ymin) {
//                    ymin = d.ymin;
//                }
//                if (Double.isNaN(xmax) || d.xmax > xmax) {
//                    xmax = d.xmax;
//                }
//                if (Double.isNaN(ymax) || d.ymax > ymax) {
//                    ymax = d.ymax;
//                }
//            }
//            this.domain = new DomainXY(xmin, ymin, xmax, ymax);
//
//            ArrayList<IDDxy> r = new ArrayList<IDDxy>();
//            ArrayList<IDDxy> i = new ArrayList<IDDxy>();
//            for (int j = 0; j < sum.length; j++) {
//                IDCxy cFunctionXY = sum[j];
//                r.add(cFunctionXY.getReal());
//                i.add(cFunctionXY.getImag());
//            }
//            this.real = new DxySum(r.toArray(new IDDxy[r.size()])).simplify();
//            this.imag = new DxySum(i.toArray(new IDDxy[i.size()])).simplify();
//        }
//    }
//
////    @Override
////    public DD2DC simplify() {
////        return isZero()?FunctionFactory.CZEROXY: new DD2DC(this.imag.simplify(), this.real.simplify());
////    }
//    private void linearize(IDCxy[] sum, ArrayList<IDCxy> putInto) {
//        for (int i = 0; i < sum.length; i++) {
//            IDCxy dFunctionXY = sum[i];
//            if (dFunctionXY instanceof DCxySum) {
//                linearize(((DCxySum) dFunctionXY).segments, putInto);
//            } else {
//                putInto.add(dFunctionXY);
//            }
//        }
//    }
//
////    public Complex leftScalarProduct(DomainXY domain, CFunctionXY other) {
////        Complex r=Complex.CZERO;
////        DomainXY inter=null;
////        for (int i = 0; i < segments.length; i++) {
////            CFunctionXY f=segments[i];
////            if(!(inter=f.intersect(other)).isEmpty()){
////                r=r.add(segments[i].leftScalarProduct(inter,other));
////            }
////        }
////        return r;
////    }
////
////    public Complex rightScalarProduct(DomainXY domain, CFunctionXY other) {
////        Complex r=Complex.CZERO;
////        DomainXY inter=null;
////        for (int i = 0; i < segments.length; i++) {
////            CFunctionXY f=segments[i];
////            if(!(inter=f.intersect(other)).isEmpty()){
////                r=r.add(f.rightScalarProduct(inter,other));
////            }
////        }
////        return r;
////    }
//    public Complex computeComplexArg(double x, double y) {
//        Complex r = Complex.CZERO;
//        for (int i = 0; i < segments.length; i++) {
//            IDCxy f = segments[i];
//            r = r.add(f.computeComplexArg(x, y));
//        }
//        return r;
//    }
//}
