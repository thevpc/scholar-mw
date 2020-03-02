/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.util;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.ComplexMatrix;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadruwaves.mom.MomCache;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadruwaves.mom.str.RequiredRebuildException;

import java.util.Collection;

/**
 * @author vpc
 */
public interface MomStrHelper extends HSerializable {

    void init(MomProject str);

    MomProject getStructureConfig();

    MomStructure getStructure();

    void checkBuildIsRequired() throws RequiredRebuildException;

    ComplexMatrix evalXMatrix(ProgressMonitor monitor);

    ComplexMatrix evalZin(final ProgressMonitor monitor);

    ComplexMatrix evalCapacity(final ProgressMonitor monitor);

    ComplexMatrix evalS(ProgressMonitor monitor);

    ComplexMatrix evalAMatrix(final ProgressMonitor monitor);

    ComplexMatrix evalBMatrix(final ProgressMonitor monitor);

    VDiscrete evalTestField(double[] x, double[] y, final ProgressMonitor monitor);

    VDiscrete evalCurrent(double[] x, double[] y, final ProgressMonitor monitor);

    VDiscrete evalElectricField(double[] x, double[] y, double[] z, final ProgressMonitor monitor);

    VDiscrete buildSrc(double[] x, double[] y, final ProgressMonitor monitor);

    int estimateMn(int max_mn, double delta, int thresholdLength, int trialCount, final ProgressMonitor monitor);

    void recompile();

    long getLoggedStatistics(String statName);

    ObjectCache getCurrentCache(boolean autoCreate);

    Collection<MomCache> getSimilarCaches(String property);

    Collection<MomCache> getAllCaches();

    String dump();
}
