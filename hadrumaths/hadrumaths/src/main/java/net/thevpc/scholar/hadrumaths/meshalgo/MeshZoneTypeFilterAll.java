/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.meshalgo;


import net.thevpc.nuts.elem.NElement;


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
    public NElement toElement() {
        return NElement.ofUplet(getClass().getSimpleName())
                ;
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
