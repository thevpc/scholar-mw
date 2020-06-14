package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.geom.Triangle;

import java.util.List;

public interface MeshOptions extends HSerializable {
    //*******Return true si le tableau de triangles peut etre encore trianguler***********
    boolean isMeshAllowed(List<Triangle> t, int iteration);

    //******Return le triangle qui peut etre mailler s'il y a sinon return null************
    Triangle selectMeshTriangle(List<Triangle> t, int iteration);
}
