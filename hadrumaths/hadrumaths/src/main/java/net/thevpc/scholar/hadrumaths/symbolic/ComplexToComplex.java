///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.thevpc.scholar.hadrumaths.symbolic;
//
//import net.thevpc.scholar.hadrumaths.*;
//import net.thevpc.scholar.hadrumaths.symbolic.conv.Imag;
//import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;
//
///**
// * @author vpc
// */
//public interface ComplexToComplex extends ComplexDomainExpr {
//
//    //USER-CENTRIC
//    default Complex[] computeComplex(Complex[] x, Domain d0) {
//        return computeComplex(x, d0, null);
//    }
//
//    default Complex[] computeComplex(Complex[] x, Domain d0, Out<Range> ranges) {
//        return ComplexToComplexDefaults.computeComplex(this, x, d0, ranges);
//    }
//
//    default Complex[] computeComplex(Complex[] x, Complex y, Domain d0) {
//        return computeComplex(x, y, d0, null);
//    }
//
//    default Complex[] computeComplex(Complex[] x, Complex y, Domain d0, Out<Range> ranges) {
//        return ComplexToComplexDefaults.computeComplex(this, x, y, d0, ranges);
//    }
//
//    default Complex[] computeComplex(Complex x, Complex[] y, Domain d0) {
//        return computeComplex(x, y, d0, null);
//    }
//
//    default Complex[] computeComplex(Complex x, Complex[] y, Domain d0, Out<Range> ranges) {
//        return ComplexToComplexDefaults.computeComplex(this, x, y, d0, ranges);
//    }
//
//    default Complex[][][] computeComplex(Complex[] x, Complex[] y, Complex[] z, Domain d0) {
//        return computeComplex(x, y, z, d0, null);
//    }
//
//    default Complex[][][] computeComplex(Complex[] x, Complex[] y, Complex[] z, Domain d0, Out<Range> ranges) {
//        return ComplexToComplexDefaults.computeComplex(this, x, y, z, d0, ranges);
//    }
//
//    default Complex[] computeComplex(Complex x, Complex[] y) {
//        return computeComplex(x, y, null, null);
//    }
//
//    default Complex[] computeComplex(Complex[] x, Complex y) {
//        return computeComplex(x, y, null, null);
//    }
//
//    default Complex[][] computeComplex(Complex[] x, Complex[] y, Domain d0) {
//        return computeComplex(x, y, d0, null);
//    }
//
//    default Complex[][] computeComplex(Complex[] x, Complex[] y, Domain d0, Out<Range> ranges) {
//        return ComplexToComplexDefaults.computeComplex(this, x, y, d0, ranges);
//    }
//
//    default Complex[][][] computeComplex(Complex[] x, Complex[] y, Complex[] z) {
//        return computeComplex(x, y, z, null, null);
//    }
//
//    default Complex[] apply(Complex[] x) {
//        return computeComplex(x);
//    }
//
//    default Complex[] computeComplex(Complex[] x) {
//        return computeComplex(x, (Domain) null, null);
//    }
//
//    default Complex[][] apply(Complex[] x, Complex[] y) {
//        return computeComplex(x, y);
//    }
//
//    default Complex[][] computeComplex(Complex[] x, Complex[] y) {
//        return computeComplex(x, y, (Domain) null, null);
//    }
//
//    default Complex apply(Complex x) {
//        return computeComplex(x);
//
//    }
//
//    default Complex computeComplex(Complex x) {
//        return computeComplex(x, NoneOutBoolean.INSTANCE);
//    }
//
//    //TODO
//    Complex computeComplex(Complex x, BooleanMarker defined);
//
//    default Complex apply(Complex x, Complex y) {
//        return computeComplex(x, y);
//    }
//
//    default Complex computeComplex(Complex x, Complex y) {
//        return computeComplex(x, y, NoneOutBoolean.INSTANCE);
//    }
//
//    Complex computeComplex(Complex x, Complex y, BooleanMarker defined);
//
//    default Complex apply(Complex x, Complex y, Complex z) {
//        return computeComplex(x, y, z);
//    }
//
//    default Complex computeComplex(Complex x, Complex y, Complex z) {
//        return computeComplex(x, y, z, NoneOutBoolean.INSTANCE);
//    }
//
//    Complex computeComplex(Complex x, Complex y, Complex z, BooleanMarker defined);
//
//}
