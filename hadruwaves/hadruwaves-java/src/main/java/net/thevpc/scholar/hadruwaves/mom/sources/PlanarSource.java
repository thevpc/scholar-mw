/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.sources;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 * @author vpc
 */
public interface PlanarSource extends Source, HSerializable, Cloneable {

    Complex getCharacteristicImpedance();

    Geometry getGeometry();

    DoubleToVector getFunction();

    PlanarSource clone();
}
