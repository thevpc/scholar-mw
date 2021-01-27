package net.thevpc.scholar.hadrumaths.meshalgo;

import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.geom.Geometry;

import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 22 mai 2007 00:00:48
 */
public interface MeshAlgo extends HSerializable {
    Collection<MeshZone> meshPolygon(Geometry polygon);

    MeshAlgo clone();

}
