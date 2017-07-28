/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.util;

import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import java.util.Collection;
import net.vpc.scholar.hadruwaves.mom.project.*;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.str.RequiredRebuildException;

/**
 *
 * @author vpc
 */
public interface MomStrHelper {

    void init(MomProject str);

    MomProject getStructureConfig();

    MomStructure getStructure();

    void checkBuildIsRequired() throws RequiredRebuildException;

    public Matrix computeTestcoeff(ComputationMonitor monitor);

    public Matrix computeZin(final ComputationMonitor monitor);

    public Matrix computeCapacity(final ComputationMonitor monitor);

    public Matrix computeS(ComputationMonitor monitor);

    public Matrix computeAMatrix(final ComputationMonitor monitor);

    public Matrix computeBMatrix(final ComputationMonitor monitor);

    public VDiscrete computeTestField(double[] x, double[] y, final ComputationMonitor monitor);

    public VDiscrete computeCurrent(double[] x, double[] y, final ComputationMonitor monitor);

    public VDiscrete computeElectricField(double[] x, double[] y, double[] z, final ComputationMonitor monitor);

    public VDiscrete buildSrc(double[] x, double[] y, final ComputationMonitor monitor);

    public int estimateMn(int max_mn, double delta, int thresholdLength, int trialCount, final ComputationMonitor monitor);

    public void recompile();

    public long getLoggedStatistics(String statName);

    public ObjectCache getCurrentCache(boolean autoCreate);

    public Collection<ObjectCache> getSimilarCaches(String property);

    public Collection<ObjectCache> getAllCaches();

    public String getDump();
}
