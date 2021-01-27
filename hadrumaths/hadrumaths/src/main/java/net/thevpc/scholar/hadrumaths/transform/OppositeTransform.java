/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Axis;

/**
 * @author vpc
 */
public final class OppositeTransform implements ExpressionTransform {

    private final Axis value;

    public OppositeTransform(Axis value) {
        this.value = value;
    }

    public Axis getValue() {
        return value;
    }

}
