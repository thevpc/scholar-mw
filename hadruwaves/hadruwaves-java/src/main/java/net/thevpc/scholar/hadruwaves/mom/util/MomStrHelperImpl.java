/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwaves.mom.util;

import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.io.HadrumathsIOUtils;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.str.RequiredRebuildException;

import java.util.Collection;
import net.thevpc.scholar.hadruwaves.mom.MomCache;

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
        momStructure.getPersistentCache().setRootFolder(HadrumathsIOUtils.createHFile(str.getWorkDir().getPath()+"/cache"));
        momStructure.loadProject(str);
    }


    public ComplexMatrix evalXMatrix(ProgressMonitor monitor) {
        return momStructure.matrixX().monitor(monitor).evalMatrix();
    }


    public VDiscrete evalElectricField(double[] x, double[] y, double[] z, ProgressMonitor monitor) {
        return momStructure.electricField().monitor(monitor).cartesian().evalVDiscrete(x, y, new double[]{0});
    }


    public VDiscrete evalCurrent(double[] x, double[] y, ProgressMonitor monitor) {
        return momStructure.current().monitor(monitor).evalVDiscrete(x, y);
    }


    public VDiscrete evalTestField(double[] x, double[] y, ProgressMonitor monitor) {
        return momStructure.testField().monitor(monitor).evalVDiscrete(x, y);
    }


    public VDiscrete buildSrc(double[] x, double[] y, ProgressMonitor monitor) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public ComplexMatrix evalZin(ProgressMonitor monitor) {
        return momStructure.inputImpedance().monitor(monitor).evalMatrix();
    }


    public ComplexMatrix evalS(ProgressMonitor monitor) {
        return momStructure.sparameters().monitor(monitor).evalMatrix();
    }


    public ComplexMatrix evalAMatrix(ProgressMonitor monitor) {
        return momStructure.matrixA().monitor(monitor).evalMatrix();
    }


    public ComplexMatrix evalBMatrix(ProgressMonitor monitor) {
        return momStructure.matrixB()
                .monitor(monitor).evalMatrix();
    }


    public void checkBuildIsRequired() throws RequiredRebuildException {
        momStructure.checkBuildIsRequired();
    }


    public int estimateMn(int max_mn, double delta, int thresholdLength, int trialCount, ProgressMonitor monitor) {
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


    public Collection<MomCache> getSimilarCaches(String property) {
        return momStructure.getSimilarCaches(property);
    }


    public Collection<MomCache> getAllCaches() {
        return momStructure.getAllCaches();
    }

    public ComplexMatrix evalCapacity(ProgressMonitor monitor) {
        return momStructure
                .capacity().monitor(monitor).evalMatrix();
    }

    public String dump() {
        return momStructure.dump();
    }

    public TsonElement toTsonElement(TsonObjectContext context){
        return momStructure.toTsonElement(context);
    }


}
