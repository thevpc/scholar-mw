/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.util;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.ComputationMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadruwaves.mom.str.RequiredRebuildException;

import java.io.File;
import java.util.Collection;

/**
 * @author vpc
 */
public class MomStrHelperImpl implements MomStrHelper {
    private MomProject str;
    private MomStructure momStructure;

    public MomProject getStructureConfig() {
        return str;
    }


    public MomStructure getStructure() {
        return momStructure;
    }


    public void init(MomProject str) {
        this.str = str;
        momStructure = new MomStructure();
        momStructure.getPersistentCache().setRootFolder(new File(str.getWorkDir(), "cache"));
        momStructure.loadProject(str);
    }


    public Matrix computeTestcoeff(ComputationMonitor monitor) {
        return momStructure.matrixX().monitor(monitor).computeMatrix();
    }


    public VDiscrete computeElectricField(double[] x, double[] y, double[] z, ComputationMonitor monitor) {
        return momStructure.electricField().monitor(monitor).computeVDiscrete(x, y, new double[]{0});
    }


    public VDiscrete computeCurrent(double[] x, double[] y, ComputationMonitor monitor) {
        return momStructure.current().monitor(monitor).computeVDiscrete(x, y);
    }


    public VDiscrete computeTestField(double[] x, double[] y, ComputationMonitor monitor) {
        return momStructure.testField().monitor(monitor).computeVDiscrete(x, y);
    }


    public VDiscrete buildSrc(double[] x, double[] y, ComputationMonitor monitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public Matrix computeZin(ComputationMonitor monitor) {
        return momStructure.inputImpedance().monitor(monitor).computeMatrix();
    }


    public Matrix computeS(ComputationMonitor monitor) {
        return momStructure.sparameters().monitor(monitor).computeMatrix();
    }


    public Matrix computeAMatrix(ComputationMonitor monitor) {
        return momStructure.matrixA().monitor(monitor).computeMatrix();
    }


    public Matrix computeBMatrix(ComputationMonitor monitor) {
        return momStructure.matrixB()
                .monitor(monitor).computeMatrix();
    }


    public void checkBuildIsRequired() throws RequiredRebuildException {
        momStructure.checkBuildIsRequired();
    }


    public int estimateMn(int max_mn, double delta, int thresholdLength, int trialCount, ComputationMonitor monitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public long getLoggedStatistics(String statName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void recompile() {
        momStructure.loadProject(str);
    }


    public ObjectCache getCurrentCache(boolean autoCreate) {
        return momStructure.getCurrentCache(autoCreate);
    }


    public Collection<ObjectCache> getSimilarCaches(String property) {
        return momStructure.getSimilarCaches(property);
    }


    public Collection<ObjectCache> getAllCaches() {
        return momStructure.getAllCaches();
    }

    public Matrix computeCapacity(ComputationMonitor monitor) {
        return momStructure
                .capacity().monitor(monitor).computeMatrix();
    }

    public String getDump() {
        return momStructure.dump();
    }


}
