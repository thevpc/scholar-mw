package net.thevpc.scholar.hadrumaths.test.removeme;

//package net.thevpc.scholar.math.integration;
//
//import net.thevpc.scholar.math.IDDx;
//
///**
// * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 18:55:20 To
// * change this template use File | Settings | File Templates.
// */
//public class DRectLowIntegralX implements DIntegralX {
//
//    private int xprecision;
//
//    public DRectLowIntegralX(int xprecision) {
//        this.xprecision = xprecision;
//        if (xprecision <= 0) {
//            throw new IllegalArgumentException("non positive precision");
//        }
//    }
//
//    public double integrateX(IDDx f, double xlo, double xhi) {
//        // Check data.
//        if (xhi <= xlo) {
//            return 0.0;
//        }
//
//        double dx = (xhi - xlo) / xprecision;
//
//        double vol = 0.0;
//
//        double xLo = xlo;
//
//        //double xHi = xlo + dx;
//        for (int i = 0; i < xprecision; i++) {
//            xLo = xlo + i * dx;
//            double h = f.computeDouble(xLo);
//            vol += h * dx;
//        }
//
//        return vol;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 5;
//        hash = 61 * hash + this.xprecision;
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final DRectLowIntegralX other = (DRectLowIntegralX) obj;
//        if (this.xprecision != other.xprecision) {
//            return false;
//        }
//        return true;
//    }
//
//}
