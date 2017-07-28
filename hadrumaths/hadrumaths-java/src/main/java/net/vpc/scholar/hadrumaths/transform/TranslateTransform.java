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
public final class TranslateTransform implements ExpressionTransform {

    private final double valueX;
    private final double valueY;

    public TranslateTransform(double deltaX, double deltaY) {
        this.valueX = deltaX;
        this.valueY = deltaY;
    }

    public double getValueX() {
        return valueX;
    }

    public double getValueY() {
        return valueY;
    }

}
