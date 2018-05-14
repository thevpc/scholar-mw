//package net.vpc.scholar.hadrumaths.symbolic;
//
//import net.vpc.scholar.hadrumaths.Complex;
//import net.vpc.scholar.hadrumaths.BooleanMarker;
//
//public interface DoubleToComplexX extends DoubleToComplex {
//    default Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
//        if (contains(x, y, z)) {
//            return computeComplex(x, defined);
//        }
//        return Complex.ZERO;
//    }
//
//    default Complex computeComplex(double x, double y, BooleanMarker defined) {
//        if (contains(x, y)) {
//            return computeComplex(x, defined);
//        }
//        return Complex.ZERO;
//    }
//
//}
