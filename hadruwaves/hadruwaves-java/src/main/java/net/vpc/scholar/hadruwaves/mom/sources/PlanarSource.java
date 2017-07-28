/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

import java.io.Serializable;

/**
 * @author vpc
 */
public interface PlanarSource extends Source, Serializable, Cloneable {

    Complex getCharacteristicImpedance();

    DoubleToVector getFunction();

    PlanarSource clone();
}
