/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;

import java.awt.geom.Path2D;
import java.io.Serializable;

/**
 * @author vpc
 */
public interface Geometry extends Serializable {

    Path2D.Double getPath();

    Domain getDomain();

    boolean isRectangular();

    boolean isPolygonal();

    boolean isTriangular();

    boolean isSingular();

    boolean isEmpty();

    Geometry translateGeometry(double x, double y);

    boolean contains(double x, double y);

    Geometry clone();

    Surface toSurface();

    Polygon toPolygon();

    Triangle toTriangle();

    Geometry scale(Domain newDomain);

    Geometry scale(int width, int height);

    Geometry intersectGeometry(Geometry geometry);

    Geometry subtractGeometry(Geometry geometry);

    Geometry addGeometry(Geometry geometry);

    Geometry exclusiveOrGeometry(Geometry geometry);

}
