package net.vpc.scholar.hadruwaves.str;

import net.vpc.common.mon.ProgressMonitorCreator;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadruwaves.builders.*;
import net.vpc.scholar.hadruwaves.mom.ElectricFieldPart;
import net.vpc.scholar.hadruwaves.mom.MomStructure;

/**
 * Created by vpc on 4/25/15.
 */
public interface MWStructure {
    Domain domain();

    CurrentBuilder current();

    SourceBuilder source();

    TestFieldBuilder testField();

    ElectricFieldBuilder electricField();

    ElectricFieldBuilder electricField(ElectricFieldPart part);

    FarFieldBuilder farField();

    MagneticFieldBuilder magneticField();

    CapacityBuilder capacity();

    SelfBuilder self();

    InputImpedanceBuilder inputImpedance();

    SParametersBuilder sparameters();

    PoyntingVectorBuilder poyntingVector();

    MWStructure setParameter(String name);

    MWStructure setParameterNotNull(String name, Object value);

    MWStructure setParameter(String name, Object value);

    MWStructure removeParameter(String name);

    Object getParameter(String name);

    Object getParameter(String name, Object defaultValue);

    Number getParameterNumber(String name, Number defaultValue);

    boolean containsParameter(String name);

    boolean isParameter(String name, boolean defaultValue);

    boolean isParameter(String name);

    PersistenceCache getPersistentCache();

    Object getUserObject(String name, Object defaultValue);

    Object getUserObject(String name);

    MWStructure removeUserObject(String name);

    MWStructure setUserObject(String name, Object value);

    ProgressMonitorCreator getMonitor();

    ProgressMonitorCreator monitor();

    MWStructure setMonitor(ProgressMonitorCreator monitors);

    MWStructure monitor(ProgressMonitorCreator monitors);

    ObjectCache getCurrentCache(boolean autoCreate);

    String getName();

    MWStructure setName(String name);
}
