package net.thevpc.scholar.hadrumaths.geom;

import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juin 2007 10:43:22
 */
public interface FractalAreaHGeometryList extends HGeometryList, Cloneable, Serializable {

    int getLevel();

    void setLevel(int level);


    HGeometry[] getTransform();

    @Override
    FractalAreaHGeometryList clone();
}
