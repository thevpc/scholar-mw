package net.vpc.scholar.hadruwaves.str;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadruplot.console.ConsoleLogger;
import net.vpc.scholar.hadruwaves.builders.*;
import net.vpc.scholar.hadruwaves.mom.str.MWStructureErrorHandler;

import java.util.Map;

/**
 * Created by vpc on 4/25/15.
 */
public interface MWStructure extends HSerializable {
    Domain domain();

    CurrentBuilder current();

    SourceBuilder source();

    TestFieldBuilder testField();

    ElectricFieldBuilder electricField();

    PoyntingVectorBuilder poyntingVector();

    DirectivityBuilder directivity();

    FarFieldBuilder farField();

    MagneticFieldBuilder magneticField();

    CapacityBuilder capacity();

    SelfBuilder self();

    InputImpedanceBuilder inputImpedance();

    SParametersBuilder sparameters();

    ConsoleLogger getLog();

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

    Map<String, Object> getParameters();

    PersistenceCache getPersistentCache();

    Object getUserObject(String name, Object defaultValue);

    Object getUserObject(String name);

    MWStructure removeUserObject(String name);

    MWStructure setUserObject(String name, Object value);

    ProgressMonitorFactory getMonitorFactory();

    MWStructure setMonitorFactory(ProgressMonitorFactory monitors);

    ProgressMonitorFactory monitorFactory();

    MWStructure monitorFactory(ProgressMonitorFactory monitors);

    ObjectCache getCurrentCache(boolean autoCreate);

    String getName();

    MWStructure setName(String name);

    CacheKey getKey();

    Map<String, Object> getUserObjects();

    MWStructureErrorHandler getErrorHandler();

    MWStructureHintsManager getHintsManager();
}
