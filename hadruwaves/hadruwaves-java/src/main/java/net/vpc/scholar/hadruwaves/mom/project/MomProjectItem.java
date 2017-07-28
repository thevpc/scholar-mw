/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project;

import net.vpc.scholar.hadruwaves.mom.project.common.ExpressionAware;
import net.vpc.scholar.hadrumaths.util.Configuration;

/**
 *
 * @author vpc
 */
public interface MomProjectItem extends ExpressionAware{
    public void load(Configuration conf, String key);
    public void store(Configuration c, String key);
    public MomProjectItem create();
    public String getId();
}
