/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.project.shapes;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadruwaves.mom.project.MomProjectItem;

/**
 * @author vpc
 */
public interface AreaShape extends MomProjectItem {

    public Domain getDomain();

    public HGeometryList getPolygons(Domain globalDomain);

    public void setWidth(double d);

    public void setHeight(double d);

    public void setX(double d);

    public void setY(double d);

}
