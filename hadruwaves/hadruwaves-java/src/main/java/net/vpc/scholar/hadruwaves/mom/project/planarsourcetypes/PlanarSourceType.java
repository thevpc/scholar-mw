/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.planarsourcetypes;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.*;
import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 * @author vpc
 */
public abstract class PlanarSourceType extends AbstractMomProjectItem implements GpMesher {

    public PlanarSourceType() {
    }

    public DoubleToVector createFunctions(Domain globalDomain, MeshZone zone, MomStructure str) {
        return null;
    }
}
