/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.project.shapes;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectItem;

/**
 * @author vpc
 */
public interface AreaShape extends MomProjectItem {

    public Domain getDomain();

    public GeometryList getPolygons(Domain globalDomain);

    public void setWidth(double d);

    public void setHeight(double d);

    public void setX(double d);

    public void setY(double d);

}
