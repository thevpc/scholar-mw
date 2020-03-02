/*
 * ModeInfoFilter.java
 * 
 * Created on 3 oct. 2007, 18:12:46
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.HSerializable;

/**
 * @author vpc
 */
public interface ModeIndexFilter extends HSerializable {
    boolean acceptModeIndex(ModeIndex mode);

    boolean isFrequencyDependent();
}