/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.geom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.HSerializable;

import java.awt.geom.Path2D;
import java.util.Map;

/**
 * @author vpc
 */
public interface Geometry extends HSerializable {

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

    Polygon[] toPolygons();

    Polygon toPolygon();

    Triangle toTriangle();

    Geometry scale(Domain newDomain);

    Geometry scale(int width, int height);

    boolean containsDomain(Domain geometry);

    Map<String, Object> getProperties();

    boolean containsGeometry(Geometry geometry);

    Geometry intersectGeometry(Geometry geometry);

    Geometry subtractGeometry(Geometry geometry);

    Geometry addGeometry(Geometry geometry);

    Geometry exclusiveOrGeometry(Geometry geometry);

    Expr toExpr();

    Expr mul(int value);

    Expr mul(double value);

    Expr mul(Expr value);

}
