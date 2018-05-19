/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;

/**
 * @author vpc
 */
final class MeshZoneTypeFilterAll implements MeshZoneTypeFilter {
    public MeshZoneTypeFilterAll() {
    }

    @Override
    public boolean accept(MeshZoneType type) {
        return true;
    }

    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(this);
        return h;
    }

    @Override
    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MeshZoneTypeFilterAll other = (MeshZoneTypeFilterAll) obj;
        return true;
    }


}
