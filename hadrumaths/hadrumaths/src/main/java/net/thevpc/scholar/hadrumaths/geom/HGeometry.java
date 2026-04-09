/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.meshalgo.tri.JTSHelper;

import java.awt.geom.Path2D;
import java.util.Map;

/**
 * @author vpc
 */
public interface HGeometry extends HSerializable {
    static HGeometry fromPath(Path2D.Double path) {
        return JTSHelper.fromJtsGeometry(JTSHelper.fromPath2D(path));
    }

    Path2D.Double getPath();

    Domain getDomain();

    boolean isRectangular();

    boolean isPolygonal();

    boolean isTriangular();

    boolean isSingular();

    boolean isEmpty();

    HGeometry translate(double x, double y);

    double area();

    boolean contains(double x, double y);

    HGeometry clone();

    HPolygon[] toPolygons();

    HPolygon toPolygon();

    HTriangle toTriangle();

    HGeometry scale(Domain newDomain);

    HGeometry scale(int width, int height);

    boolean containsDomain(Domain geometry);

    Map<String, NElement> getProperties();

    boolean containsGeometry(HGeometry geometry);

    HGeometry intersectGeometry(HGeometry geometry);

    HGeometry subtractGeometry(HGeometry geometry);

    HGeometry addGeometry(HGeometry geometry);

    HGeometry exclusiveOrGeometry(HGeometry geometry);

    Expr toExpr();

    Expr mul(int value);

    Expr mul(double value);

    Expr mul(Expr value);

}
