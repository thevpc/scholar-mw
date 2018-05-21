/*
 * ModeInfoFilter.java
 * 
 * Created on 3 oct. 2007, 18:12:46
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.dump.Dumpable;

import java.io.Serializable;

/**
 * @author vpc
 */
public interface ModeIndexFilter extends Serializable, Dumpable {
    boolean acceptModeIndex(ModeIndex mode);

    boolean isFrequencyDependent();
}