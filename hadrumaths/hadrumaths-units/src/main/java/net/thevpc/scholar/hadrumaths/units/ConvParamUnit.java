/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.units;

import net.thevpc.scholar.hadrumaths.Complex;

/**
 *
 * @author vpc
 * @param <T> Type
 */
public interface ConvParamUnit<T extends ConvParamUnit> extends ParamUnit {

    ConvParamUnit<T> SI();

    double toSI(double value);

    Complex toSI(Complex value);

    double to(double value, T other);

    public Complex to(Complex value, T to);
}
