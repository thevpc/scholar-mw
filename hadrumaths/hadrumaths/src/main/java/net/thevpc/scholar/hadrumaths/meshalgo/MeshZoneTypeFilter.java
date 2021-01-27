/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.meshalgo;

import net.thevpc.scholar.hadrumaths.HSerializable;

/**
 * @author vpc
 */
public interface MeshZoneTypeFilter extends HSerializable {
    boolean accept(MeshZoneType type);
}
