package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

import java.io.Serializable;
import java.util.List;

/**
 * @author : vpc
 * @creationtime 28 janv. 2006 15:54:28
 */
public interface GpPattern extends Serializable, Dumpable {
     int getCount();

     DoubleToVector[] createFunctions(Domain globalDomain, MeshZone zone, ProgressMonitor monitor, MomStructure str);

     List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds);
}
