/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.meshalgo;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * @author vpc
 */
public final class MeshZoneTypeFilterAccepted implements MeshZoneTypeFilter {
    private final TreeSet<MeshZoneType> accepted = new TreeSet<MeshZoneType>();

    public MeshZoneTypeFilterAccepted(MeshZoneType... all) {
        accepted.addAll(Arrays.asList(all));
    }

    @Override
    public boolean accept(MeshZoneType type) {
        return accepted.contains(type);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName())
                .add("accepted", context.elem(accepted))
                .build();
    }
//    public Dumper getDumpStringHelper() {
//        Dumper h = new Dumper(this);
//        h.add("accepted", accepted);
//        return h;
//    }

//    @Override
//    public String dump() {
//        return getDumpStringHelper().toString();
//    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.accepted != null ? this.accepted.hashCode() : 0);
        return hash;
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
        return this.accepted == other.accepted || (this.accepted != null && this.accepted.equals(other.accepted));
    }

    @Override
    public String toString() {
        return dump();
    }


}
