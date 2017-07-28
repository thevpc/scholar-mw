/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Domain;

/**
 *
 * @author vpc
 */
public final class DomainXYMultiplierTransform implements ExpressionTransform {

    private final Domain value;

    public DomainXYMultiplierTransform(Domain value) {
        this.value = value;
    }

    public Domain getValue() {
        return value;
    }

}
