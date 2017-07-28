package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.ExprList;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
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

    ExprList list();

    DoubleToVector apply(int index);

    ExprList toList();
    DoubleToVector[] arr();

    DoubleToVector[] arr(ComputationMonitor monitor, ObjectCache objectCache);

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
