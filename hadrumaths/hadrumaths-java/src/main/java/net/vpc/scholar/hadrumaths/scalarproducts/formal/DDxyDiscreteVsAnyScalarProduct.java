//package net.vpc.scholar.math.scalarproducts.formal;
//
//import net.vpc.scholar.math.IDDxy;
//import net.vpc.scholar.math.functions.Domain;
//import net.vpc.scholar.math.functions.dfxy.DDxyDiscrete;
//import net.vpc.scholar.math.functions.dfxy.DoubleXY;
//
//
///**
// * User: taha
// * Date: 2 juil. 2003
// * Time: 15:15:16
// */
//final class DDxyDiscreteVsAnyScalarProduct implements FormalScalarProductHelper {
//    @Override
//    public int hashCode() {
//        return getClass().getName().hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null || !obj.getClass().equals(getClass())) {
//            return false;
//        }
//        return true;
//    }
//
//    public double compute(Domain domain, IDDxy f1, IDDxy f2, FormalScalarProductOperator sp) {
//        DDxyDiscrete n = (DDxyDiscrete) f1;
//        double d = 0;
//        Domain dom;
//        double[][] values = n.getValues();
//        double[] x = n.getX();
//        double[] y = n.getY();
//        double dx = n.getDx();
//        double dy = n.getDy();
//        for (int yi = 0; yi < y.length; yi++) {
//            for (int xi = 0; xi < x.length; xi++) {
//                if (values[yi][xi] != 0) {
//                    dom = Domain.forBounds(x[xi], x[xi] + dx, y[yi], y[yi] + dy);
//                    DoubleXY seg = new DoubleXY(values[yi][xi], dom);
//                    d += sp.process(domain, seg, f2).toDouble();
//                }
//            }
//        }
//        return d;
//    }
//}