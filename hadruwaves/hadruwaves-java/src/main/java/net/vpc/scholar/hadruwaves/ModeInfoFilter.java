/*
 * ModeInfoFilter.java
 * 
 * Created on 3 oct. 2007, 18:12:46
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves;

import java.io.Serializable;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

/**
 *
 * @author vpc
 */
public interface ModeInfoFilter extends Serializable, Dumpable{
    boolean acceptModeInfo(ModeInfo info);
}
