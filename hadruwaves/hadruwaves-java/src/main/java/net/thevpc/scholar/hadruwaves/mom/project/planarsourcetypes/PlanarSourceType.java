/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.project.planarsourcetypes;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadruwaves.mom.project.gpmesher.*;
import net.thevpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

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
