/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.meshalgo;

import net.vpc.scholar.hadrumaths.dump.Dumper;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author vpc
 */
public final class MeshZoneTypeFilterAccepted implements MeshZoneTypeFilter {
    private TreeSet<MeshZoneType> accepted = new TreeSet<MeshZoneType>();

    public MeshZoneTypeFilterAccepted(MeshZoneType... all) {
        accepted.addAll(Arrays.asList(all));
    }

    @Override
    public boolean accept(MeshZoneType type) {
        return accepted.contains(type);
    }

    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(this);
        h.add("accepted", accepted);
        return h;
    }

    @Override
    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MeshZoneTypeFilterAccepted other = (MeshZoneTypeFilterAccepted) obj;
        if (this.accepted != other.accepted && (this.accepted == null || !this.accepted.equals(other.accepted))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.accepted != null ? this.accepted.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return dump();
    }


}
