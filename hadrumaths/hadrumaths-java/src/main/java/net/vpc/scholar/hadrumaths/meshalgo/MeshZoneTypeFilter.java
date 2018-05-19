/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

/**
 * @author vpc
 */
public interface MeshZoneTypeFilter extends Dumpable {
    boolean accept(MeshZoneType type);
}
