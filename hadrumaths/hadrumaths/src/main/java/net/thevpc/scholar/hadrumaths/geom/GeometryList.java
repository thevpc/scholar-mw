package net.thevpc.scholar.hadrumaths.geom;

import net.thevpc.scholar.hadrumaths.Domain;

import java.util.Collection;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 juin 2007 10:41:27
 */
public interface GeometryList extends Cloneable, Iterable<Geometry>, Geometry {

//    Dumper getDumpStringHelper();

    Geometry set(int index, Geometry element);

    Geometry remove(int index);

    void add(int index, Geometry element);

    boolean add(Geometry o);

    boolean remove(Object o);

    void clear();

    boolean addAll(GeometryList c);

    boolean addAll(Collection<? extends Geometry> c);

    boolean addAll(int index, Collection<? extends Geometry> c);

    Domain getDomain(Domain rectangle2D, Domain domain);

    Domain getBounds();

    GeometryList clone();


    int size();

    Geometry get(int i);

    void setAttribute(String name, Object value);

    Object getAttribute(String name);

    GeometryList getDual();

    Collection<Geometry> toCollection();

    Domain getSmallestBounds();

    void setDomain(Domain domain);
}
