/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.meshalgo;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;

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

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofFunction(getClass().getSimpleName())
                .build();
    }
//    public Dumper getDumpStringHelper() {
//        Dumper h = new Dumper(this);
//        return h;
//    }
//
//    @Override
//    public String dump() {
//        return getDumpStringHelper().toString();
//    }

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

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


}
