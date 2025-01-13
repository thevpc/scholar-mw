package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.str.TestFunctionsComparator;

import java.beans.PropertyChangeListener;

/**
 * Created by vpc on 3/16/15.
 */
public interface TestFunctions extends HSerializable {
    MomStructure getStructure();

    void setStructure(MomStructure structure);

    void invalidateCache();

    DoubleToVector get(int p);

    DoubleToVector gp(int p);

    Vector<Expr> list();

    DoubleToVector apply(int index);

    Vector<Expr> toList();

    DoubleToVector[] arr(ProgressMonitor monitor);
    DoubleToVector[] arr();

    DoubleToVector[] arr(ProgressMonitor monitor, ObjectCache objectCache);

    Domain getDomain();

    public TestFunctions clone();

    @Override
    String toString();

    int count();

    int length();

    int size();

    boolean isComplex();

    HintAxisType getAxisType();

    void setAxisType(HintAxisType axisType);

    TestFunctionsComparator getFunctionsComparator();

    void setFunctionsComparator(TestFunctionsComparator functionsComparator);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    Geometry[] getGeometries();

}
