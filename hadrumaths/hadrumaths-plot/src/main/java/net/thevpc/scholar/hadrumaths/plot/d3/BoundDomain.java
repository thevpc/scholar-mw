/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.plot.d3;

import net.thevpc.common.util.MinMax;
import net.thevpc.scholar.hadrumaths.BoundValue;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3DPolygon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d.Element3DCurve2;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.element3d.Element3DParallelipiped;

/**
 *
 * @author vpc
 */
public class BoundDomain {

    private final BoundValue xmin = new BoundValue();
    private final BoundValue xmax = new BoundValue();
    private final BoundValue ymin = new BoundValue();
    private final BoundValue ymax = new BoundValue();
    private final BoundValue zmin = new BoundValue();
    private final BoundValue zmax = new BoundValue();

    public BoundDomain expand(Domain d) {
        xmin.min(d.xmin());
        xmax.max(d.xmax());
        ymin.min(d.ymin());
        ymax.max(d.ymax());
        zmin.min(d.zmin());
        zmax.max(d.zmax());
        return this;
    }

    public void include(Element3D elem) {
        if (elem instanceof Element3DCurve2) {
            include(((Element3DCurve2) elem).getPoints());
        } else if (elem instanceof Element3DParallelipiped) {
            include(((Element3DParallelipiped) elem).getPoints());
        } else if (elem instanceof Element3DPolygon) {
            include(((Element3DPolygon) elem).getPoints());
        } else {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    public void include(Point3D... points) {
        for (Point3D point : points) {
            include(point);
        }
    }

    public void include(Vector3D... points) {
        for (Vector3D point : points) {
            include(point);
        }
    }

    public void include(Point... points) {
        for (Point point : points) {
            include(point);
        }
    }

    public void include(Point3D p) {
        xmin.min(p.getX());
        xmax.max(p.getX());
        ymin.min(p.getY());
        ymax.max(p.getY());
        zmin.min(p.getZ());
        zmax.max(p.getZ());
    }

    public void include(Vector3D p) {
        xmin.min(p.getX());
        xmax.max(p.getX());
        ymin.min(p.getY());
        ymax.max(p.getY());
        zmin.min(p.getZ());
        zmax.max(p.getZ());
    }

    public void include(Point p) {
        xmin.min(p.getX());
        xmax.max(p.getX());
        ymin.min(p.getY());
        ymax.max(p.getY());
        zmin.min(p.getZ());
        zmax.max(p.getZ());
    }

    public Domain toRealDomain() {
        double _xmin = this.xmin.getRealMinValue();
        double _xmax = this.xmax.getRealMaxValue();
        double _ymin = this.ymin.getRealMinValue();
        double _ymax = this.ymax.getRealMaxValue();
        double _zmin = this.zmin.getRealMinValue();
        double _zmax = this.zmax.getRealMaxValue();
        return Domain.ofBounds(_xmin, _xmax, _ymin, _ymax, _zmin, _zmax);
    }

    public Domain toDomain() {
        return toDomain(0.1f);
    }

    public Domain toDomain(float infiniteExpansion) {
        double _xmin = this.xmin.getValue();
        double _xmax = this.xmax.getValue();
        double _ymin = this.ymin.getValue();
        double _ymax = this.ymax.getValue();
        double _zmin = this.zmin.getValue();
        double _zmax = this.zmax.getValue();
        MinMax mm=new MinMax();
        mm.setInfinite(false);
        mm.registerValue(_xmin);
        mm.registerValue(_xmax);
        mm.registerValue(_ymin);
        mm.registerValue(_ymax);
        mm.registerValue(_zmin);
        mm.registerValue(_zmax);
        Domain inf=Domain.ofBounds(mm.getMin(), mm.getMax()).expandPercent(infiniteExpansion);
        double negInfinity=inf.xmin();
        double posInfinity=inf.xmax();
        if(negInfinity==Double.NEGATIVE_INFINITY){
            negInfinity=-1;
        }
        if(posInfinity==Double.POSITIVE_INFINITY){
            posInfinity=1;
        }
        if (this.xmin.isNegativeInfinity()) {
            _xmin = negInfinity;
        }
        if (this.xmax.isPositiveInfinity()) {
            _xmax = posInfinity;
        }
        if (this.ymin.isNegativeInfinity()) {
            _ymin = negInfinity;
        }
        if (this.ymax.isPositiveInfinity()) {
            _ymax = posInfinity;
        }
        if (this.zmin.isNegativeInfinity()) {
            _zmin = negInfinity;
        }
        if (this.zmax.isPositiveInfinity()) {
            _zmax = posInfinity;
        }
        return Domain.ofBounds(_xmin, _xmax, _ymin, _ymax, _zmin, _zmax);
    }
}
