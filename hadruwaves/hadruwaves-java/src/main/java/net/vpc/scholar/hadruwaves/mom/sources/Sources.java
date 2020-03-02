/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources;

import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 * @author vpc
 */
public interface Sources extends HSerializable, Cloneable {

    Sources clone();

    void setStructure(MomStructure str);
}
