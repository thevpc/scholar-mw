/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;

/**
 * @author vpc
 */
public final class SymmetryTransform implements ExpressionTransform {

    private final Axis axis;
    private final Domain domain;

    public SymmetryTransform(Axis axis, Domain domain) {
        this.axis = axis;
        this.domain = domain;
    }

    public Domain getDomain() {
        return domain;
    }

    public Axis getAxis() {
        return axis;
    }

}
