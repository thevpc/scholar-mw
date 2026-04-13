package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.mom.str.TestFunctionsComparator;

import java.beans.PropertyChangeListener;
import java.util.List;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;

/**
 * Created by vpc on 3/16/15.
 */
public interface TestFunctions extends HSerializable {

    MomStructure getStructure();

    List<MeshZone> mesh();

    void setStructure(MomStructure structure);

    void invalidateCache();

    DoubleToVector get(int p);

    DoubleToVector gp(int p);

    DoubleToVector apply(int index);

    Vector<Expr> toVector();

    DoubleToVector[] toArray(ProgressMonitor monitor);

    DoubleToVector[] toArray();

    DoubleToVector[] toArray(ProgressMonitor monitor, ObjectCache objectCache);

    List<DoubleToVector> toList();

    List<DoubleToVector> toList(ProgressMonitor monitor);
    List<DoubleToVector> toList(ProgressMonitor monitor, ObjectCache objectCache);

    Domain getDomain();

    public TestFunctions clone();

    @Override
    String toString();

    int count();

    boolean isComplex();

    HintAxisType getAxisType();

    void setAxisType(HintAxisType axisType);

    TestFunctionsComparator getFunctionsComparator();

    void setFunctionsComparator(TestFunctionsComparator functionsComparator);

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);

    void addPropertyChangeListener(String property, PropertyChangeListener listener);

    void removePropertyChangeListener(String property, PropertyChangeListener listener);

    HGeometry[] getGeometries();

    NLogger log();

    TestFunctions setLog(NLogger log);

}
