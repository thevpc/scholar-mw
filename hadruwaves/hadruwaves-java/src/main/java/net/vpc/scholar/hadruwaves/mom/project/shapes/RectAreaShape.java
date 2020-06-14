/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.shapes;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.GeometryFactory;
import net.vpc.scholar.hadrumaths.geom.DefaultGeometryList;
import net.vpc.scholar.hadrumaths.geom.DefaultPolygon;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadruwaves.mom.project.AbstractMomProjectItem;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadrumaths.util.config.Configuration;

/**
 *
 * @author vpc
 */
public class RectAreaShape extends AbstractMomProjectItem implements AreaShape{
    public static final int CELL_PIXELS_PRECISION=1000;
    public static final double CELL_PIXELS_THRESHOLD=0.8;

    private String xExpression;
    private String yExpression;
    private String widthExpression;
    private String heightExpression;

    public RectAreaShape() {
        xExpression = "0";
        yExpression = "0";
        widthExpression = "0";
        heightExpression = "0";
    }

    public String getId() {
        return "RectAreaShape";
    }

    public void load(Configuration conf, String key) {
        xExpression = conf.getString(key + ".x", "0");
        yExpression = conf.getString(key + ".y", "0");
        widthExpression = conf.getString(key + ".width", "0");
        heightExpression = conf.getString(key + ".height", "0");
    }

    public Domain getDomain() {
        MomProject context=getContext();
        double _x = context.evaluateDimension(xExpression);
        double _y = context.evaluateDimension(yExpression);
        double _width = context.evaluateDimension(widthExpression);
        double _height = context.evaluateDimension(heightExpression);
        return (Domain.ofBounds(_x, _x + _width, _y, _y + _height));

    }

    public String getHeightExpression() {
        return heightExpression;
    }

    public void setHeightExpression(String heightExpression) {
        this.heightExpression = heightExpression;
    }

    public String getWidthExpression() {
        return widthExpression;
    }

    public void setWidthExpression(String widthExpression) {
        this.widthExpression = widthExpression;
    }

    public String getXExpression() {
        return xExpression;
    }

    public void setXExpression(String xExpression) {
        this.xExpression = xExpression;
    }

    public String getYExpression() {
        return yExpression;
    }

    public void setYExpression(String yExpression) {
        this.yExpression = yExpression;
    }

    public void store(Configuration c, String key) {
        c.setString(key + ".x", xExpression);
        c.setString(key + ".y", yExpression);
        c.setString(key + ".width", widthExpression);
        c.setString(key + ".height", heightExpression);
    }

    public void setWidth(double d) {
        MomProject c = getContext();
        this.widthExpression = String.valueOf(d / c.getDimensionUnit());
    }

    public void setHeight(double d) {
        MomProject c = getContext();
        this.heightExpression = String.valueOf(d / c.getDimensionUnit());
    }

    public void setX(double d) {
        MomProject c = getContext();
        this.xExpression = String.valueOf(d / c.getDimensionUnit());
    }
    public void setY(double d) {
        MomProject c = getContext();
        this.yExpression = String.valueOf(d / c.getDimensionUnit());
    }

    public GeometryList getPolygons(Domain globalDomain) {
//            DomainXY polygonsDomain0 = globalDomain;
//            double precision = CELL_PIXELS_PRECISION;
//            double precisionThreshold = precision * CELL_PIXELS_THRESHOLD;
//            double xfactor = 1;
//            double yfactor = 1;
//            if (polygonsDomain0.width < (precisionThreshold)) {
//                xfactor = precision / polygonsDomain0.width;
//            }
//            if (polygonsDomain0.height < precisionThreshold) {
//                yfactor = precision / polygonsDomain0.height;
//            }
//            DomainXY polygonsDomain = polygonsDomain0;
//            if (xfactor > 1 || yfactor>1) {
//                polygonsDomain = new DomainXY(0, 0, precision, precision, DomainXY.Type.LENGTH);
//            }
            Domain dd = getDomain();
//            if (xfactor > 1 || yfactor>1) {
//                dd = new DomainXY((dd.xmin - polygonsDomain0.xmin) * xfactor, (dd.ymin - polygonsDomain0.ymin) * yfactor, dd.width * xfactor, dd.height * yfactor, DomainXY.Type.LENGTH);
//            }
            Polygon pp = GeometryFactory.createPolygon(dd);
            return new DefaultGeometryList(globalDomain,pp);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + (this.xExpression != null ? this.xExpression.hashCode() : 0);
        hash = 73 * hash + (this.yExpression != null ? this.yExpression.hashCode() : 0);
        hash = 73 * hash + (this.widthExpression != null ? this.widthExpression.hashCode() : 0);
        hash = 73 * hash + (this.heightExpression != null ? this.heightExpression.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RectAreaShape other = (RectAreaShape) obj;
        if (this.xExpression != other.xExpression && (this.xExpression == null || !this.xExpression.equals(other.xExpression))) {
            return false;
        }
        if (this.yExpression != other.yExpression && (this.yExpression == null || !this.yExpression.equals(other.yExpression))) {
            return false;
        }
        if (this.widthExpression != other.widthExpression && (this.widthExpression == null || !this.widthExpression.equals(other.widthExpression))) {
            return false;
        }
        if (this.heightExpression != other.heightExpression && (this.heightExpression == null || !this.heightExpression.equals(other.heightExpression))) {
            return false;
        }
        return true;
    }
    

    
}
