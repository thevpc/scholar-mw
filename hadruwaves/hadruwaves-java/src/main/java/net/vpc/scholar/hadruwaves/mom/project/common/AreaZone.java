package net.vpc.scholar.hadruwaves.mom.project.common;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectItem;

/**
 * User: vpc
 * Date: 23 juin 2005
 * Time: 12:42:47
 */
public interface AreaZone extends MomProjectItem{
    public String getName();
    public Domain getDomain();
    public void setWidth(double d);
    public void setHeight(double d);
    public void setX(double d);
    public void setY(double d);
    public AreaZone clone();
    public AreaGroup getTopLevelGroup();
    public AreaGroup getParentGroup();
}
