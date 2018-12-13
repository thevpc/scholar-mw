package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 mai 2007 00:00:48
 */
public interface MeshAlgo extends Serializable, Dumpable {
    Collection<MeshZone> meshPolygon(Geometry polygon);

    MeshAlgo clone();

}
