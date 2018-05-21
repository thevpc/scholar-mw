package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.TList;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.str.TestFunctionsComparator;

import java.beans.PropertyChangeListener;

/**
 * Created by vpc on 3/16/15.
 */
public interface TestFunctions {
    MomStructure getStructure();

    void setStructure(MomStructure structure);

    void invalidateCache();

    DoubleToVector get(int p);

    DoubleToVector gp(int p);

    TList<Expr> list();

    DoubleToVector apply(int index);

    TList<Expr> toList();
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

    String dump();

    TestFunctionsComparator getFunctionsComparator();

    void setFunctionsComparator(TestFunctionsComparator functionsComparator);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property,PropertyChangeListener listener);

    void removePropertyChangeListener(String property,PropertyChangeListener listener);

}
