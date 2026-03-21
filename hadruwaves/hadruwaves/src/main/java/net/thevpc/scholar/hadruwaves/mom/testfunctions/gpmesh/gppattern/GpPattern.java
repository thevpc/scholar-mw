package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.nuts.util.NCopiable;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.List;

/**
 * @author : vpc
 * @creationtime 28 janv. 2006 15:54:28
 */
public interface GpPattern extends HSerializable, NCopiable {
     int getCount();

     DoubleToVector[] createFunctions(Domain globalDomain, MeshZone zone, ProgressMonitor monitor, MomStructure str, NLogger logger);

     List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds);
}
