/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.symbolic;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Imag;
import net.thevpc.scholar.hadrumaths.symbolic.conv.Real;

/**
 * @author vpc
 */
public interface DoubleToComplex extends DoubleDomainExpr {

    //DEFAULTS
    default DoubleToDouble getRealDD() {
        return new Real(this);
    }

    default DoubleToDouble getImagDD() {
        return new Imag(this);
    }

    //USER-CENTRIC
    default Complex[] evalComplex(double[] x, Domain d0) {
        return evalComplex(x, d0, null);
    }

    default Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, d0, ranges);
    }

    default Complex[] evalComplex(double[] x, double y, Domain d0) {
        return evalComplex(x, y, d0, null);
    }

    default Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, y, d0, ranges);
    }

    default Complex[] evalComplex(double x, double[] y, Domain d0) {
        return evalComplex(x, y, d0, null);
    }

    default Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, y, d0, ranges);
    }

    default Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0) {
        return evalComplex(x, y, z, d0, null);
    }

    default Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, y, z, d0, ranges);
    }

    default Complex[] evalComplex(double x, double[] y) {
        return evalComplex(x, y, null, null);
    }

    default Complex[] evalComplex(double[] x, double y) {
        return evalComplex(x, y, null, null);
    }

    default Complex[][] evalComplex(double[] x, double[] y, Domain d0) {
        return evalComplex(x, y, d0, null);
    }

    default Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return DoubleToComplexDefaults.evalComplex(this, x, y, d0, ranges);
    }

    default Complex[][][] evalComplex(double[] x, double[] y, double[] z) {
        return evalComplex(x, y, z, null, null);
    }

    default Complex[] apply(double[] x) {
        return evalComplex(x);
    }

    default Complex[] evalComplex(double[] x) {
        return evalComplex(x, (Domain) null, null);
    }

    default Complex[][] apply(double[] x, double[] y) {
        return evalComplex(x, y);
    }

    default Complex[][] evalComplex(double[] x, double[] y) {
        return evalComplex(x, y, (Domain) null, null);
    }

    default Complex apply(double x) {
        return evalComplex(x);

    }

    default Complex evalComplex(double x) {
        return evalComplex(x, NoneOutBoolean.INSTANCE);
    }

    //TODO
    Complex evalComplex(double x, BooleanMarker defined);

    default Complex apply(double x, double y) {
        return evalComplex(x, y);
    }

    default Complex evalComplex(double x, double y) {
        return evalComplex(x, y, NoneOutBoolean.INSTANCE);
    }

    Complex evalComplex(double x, double y, BooleanMarker defined);

    default Complex apply(double x, double y, double z) {
        return evalComplex(x, y, z);
    }

    default Complex evalComplex(double x, double y, double z) {
        return evalComplex(x, y, z, NoneOutBoolean.INSTANCE);
    }

    Complex evalComplex(double x, double y, double z, BooleanMarker defined);

    default ExprType getNarrowType() {
        return getType();
    }

    default DoubleToComplex toDC() {
        return this;
    }

    default DoubleToDouble toDD() {
        return (DoubleToDouble) narrow(ExprType.DOUBLE_DOUBLE);
    }

    default DoubleToVector toDV() {
        return (DoubleToVector) narrow(ExprType.DOUBLE_CVECTOR);
    }

    default DoubleToMatrix toDM() {
        return (DoubleToMatrix) narrow(ExprType.DOUBLE_CMATRIX);
    }

    @Override
    default ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    default ExprType getType(){
        return ExprType.DOUBLE_COMPLEX;
    }
}
