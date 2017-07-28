/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform;

/**
 *
 * @author vpc
 */
public final class DoubleMultiplierTransform implements ExpressionTransform {

    private final double value;

    public DoubleMultiplierTransform(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

}
