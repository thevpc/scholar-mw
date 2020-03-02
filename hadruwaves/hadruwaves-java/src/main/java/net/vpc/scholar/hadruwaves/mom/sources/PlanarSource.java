/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 * @author vpc
 */
public interface PlanarSource extends Source, HSerializable, Cloneable {

    Complex getCharacteristicImpedance();

    DoubleToVector getFunction();

    PlanarSource clone();
}
