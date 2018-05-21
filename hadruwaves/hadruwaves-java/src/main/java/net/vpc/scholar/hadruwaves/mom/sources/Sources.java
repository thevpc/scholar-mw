/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.sources;

import java.io.Serializable;
import net.vpc.scholar.hadrumaths.dump.Dumpable;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 *
 * @author vpc
 */
public interface Sources extends Serializable, Dumpable, Cloneable {

    Sources clone();

    void setStructure(MomStructure str);
}
