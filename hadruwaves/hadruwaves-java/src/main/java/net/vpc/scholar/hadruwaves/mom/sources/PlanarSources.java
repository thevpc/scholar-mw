/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

/**
 *
 * @author vpc
 */
public interface PlanarSources extends Sources {

    PlanarSources add(PlanarSource source);

    PlanarSource[] getPlanarSources();

    /**
     *
     * @return all functions of getPlanarSources();
     */
    public DoubleToVector[] getSourceFunctions();

}
