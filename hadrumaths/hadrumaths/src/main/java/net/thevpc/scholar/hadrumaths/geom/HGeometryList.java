package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.scholar.hadrumaths.Domain;

import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juin 2007 10:41:27
 */
public interface HGeometryList extends Cloneable, Iterable<HGeometry>, HGeometry {

//    Dumper getDumpStringHelper();

    HGeometry set(int index, HGeometry element);

    HGeometry remove(int index);

    void add(int index, HGeometry element);

    boolean add(HGeometry o);

    boolean remove(Object o);

    void clear();

    boolean addAll(HGeometryList c);

    boolean addAll(Collection<? extends HGeometry> c);

    boolean addAll(int index, Collection<? extends HGeometry> c);

    Domain getDomain(Domain rectangle2D, Domain domain);

    Domain getBounds();

    HGeometryList clone();


    int size();

    HGeometry get(int i);

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    HGeometryList getDual();

    Collection<HGeometry> toCollection();

    Domain getSmallestBounds();

    void setDomain(Domain domain);
}
