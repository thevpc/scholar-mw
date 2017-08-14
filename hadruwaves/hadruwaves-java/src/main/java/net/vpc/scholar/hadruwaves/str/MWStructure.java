package net.vpc.scholar.hadruwaves.str;

import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadruwaves.builders.*;
import net.vpc.scholar.hadruwaves.mom.ElectricFieldPart;

/**
 * Created by vpc on 4/25/15.
 */
public interface MWStructure {
    //    public final ConvergenceResult<CMatrix> computeConvergenceZinByFnMax(int[] fnMaxSuite,ConvergenceConfig<CMatrix> convPars) {
//        MomStructure copy = this.clone();
//        double serr = Double.NaN;
//        double err = Double.NaN;
//        CMatrix old = null;
//        CMatrix sold = null;
//        int index = 0;
//        int security = convPars.getStabilityIterations();
//        Hashtable<String, Object> pars = new Hashtable<String, Object>();
//        Hashtable<String, Object> spars = new Hashtable<String, Object>();
//        while (index < fnMaxSuite.length) {
//            copy.setModeFunctionsCount(fnMaxSuite[index]);
//            pars.put("fn", copy.getModeFunctionsCount());
//            pars.put("fnIndex", index);
//            CMatrix n = copy.computeZin();
//            if (old != null) {
//                err = (n.substract(old).divide(old).norm1());
//            }
//            old = n;
//            convPars.progress("fnMax", err ,security,old,pars);
////            System.out.println("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
////            System.out.println(copy.dump());
////            System.out.println("¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤");
//            if (err < convPars.getThreshold()) {
//                if (security == convPars.getStabilityIterations()) {
//                    spars = (Hashtable<String, Object>) pars.clone();
//                    sold = old;
//                }
//                security--;
//                if (security == 0) {
//                    break;
//                }
//            } else {
//                security = convPars.getStabilityIterations();
//                spars = (Hashtable<String, Object>) pars.clone();
//                serr = err;
//                sold = old;
//            }
//            index++;
//        }
//        spars.put("fnIndex",((Integer)spars.get("fnIndex"))-1);
//        spars.put("fn",fnMaxSuite[((Integer)spars.get("fnIndex"))]);
//        return new ConvergenceResult<CMatrix>(sold, serr, spars, convPars.getThreshold());
//    }
    //    public final ConvergenceResult<CMatrix> computeConvergenceZinByFnMaxGp(GpTestFunctions[] gp, int[] fns, ConvergenceConfig<CMatrix> convPars) {
//        MomStructure copy = this.clone();
//        double serr = Double.NaN;
//        double err = Double.NaN;
//        CMatrix old = null;
//        CMatrix sold = null;
//        int index = 0;
//        int security = convPars.getStabilityIterations();
//        Hashtable<String, Object> pars = new Hashtable<String, Object>();
//        Hashtable<String, Object> spars = new Hashtable<String, Object>();
//        int fnIndex = 0;
//        while (index < gp.length) {
//            copy.setGpTestFunctions(gp[index]);
//            pars.put("gp", copy.getGpTestFunctions());
//            pars.put("gpIndex", index);
//            int[] newFn = new int[fns.length - fnIndex];
//            System.arraycopy(fns, fnIndex, newFn, 0, newFn.length);
//            ConvergenceConfig cp = convPars.clone();
//            cp.setLabel(cp.getLabel() + "\t");
//            ConvergenceResult<CMatrix> convergenceResult = copy.computeConvergenceZinByFnMax(newFn, cp);
//            pars.put("fn", convergenceResult.getParameters().get("fn"));
//            fnIndex = ((Integer) convergenceResult.getParameters().get("fnIndex")) + fnIndex;
//            CMatrix n = convergenceResult.getValue();
//            if (old != null) {
//                err = (n.substract(old).divide(old).norm1());
//            }
//            old = n;
//            convPars.progress("gp", err, security, old, pars);
//            if (err < convPars.getThreshold()) {
//                if (security == convPars.getStabilityIterations()) {
//                    spars = (Hashtable<String, Object>) pars.clone();
//                    serr = err;
//                    sold = old;
//                }
//                security--;
//                if (security == 0) {
//                    break;
//                }
//            } else {
//                security = convPars.getStabilityIterations();
//                spars = (Hashtable<String, Object>) pars.clone();
//                serr = err;
//                sold = old;
//            }
//            index++;
//        }
//        return new ConvergenceResult<CMatrix>(sold, serr, spars, convPars.getThreshold());
    //    }
//    Matrix computeZin();
//
//    Matrix computeZin(ProgressMonitor monitor);
//
//    Matrix computeS();
//
//    Matrix computeS(ProgressMonitor monitor);
//
//    Matrix computeCapacity();
//
//    Matrix computeCapacity(ProgressMonitor monitor);
//
//    Matrix computeInductance();
//
//    Matrix computeInductance(ProgressMonitor monitor);
//
//    Complex[][] computeElectricField(double[] x, double[] y, double z, Axis axis);
//
//    Complex[][] computeElectricField(double[] x, double[] y, double z, Axis axis, ProgressMonitor monitor);
//
//    VDiscrete computeElectricField(Samples samples);
//
//    VDiscrete computeElectricField(Samples samples, ProgressMonitor monitor);
//
//    VDiscrete computeElectricField(double[] x, double[] y, double[] z);
//
//    VDiscrete computeElectricField(double[] x, double[] y, double[] z, ProgressMonitor monitor);
//
//    Complex[][] computeMagneticField(double[] x, double[] y, double z, Axis axis);
//
//    Complex[][] computeMagneticField(double[] x, double[] y, double z, Axis axis, ProgressMonitor monitor);
//
//    VDiscrete computeMagneticField(double[] x, double[] y, double[] z);
//
//    VDiscrete computeMagneticField(double[] x, double[] y, double[] z, ProgressMonitor monitor);
//
//    VDiscrete computeMagneticField(Samples sampler);
//
//    VDiscrete computeMagneticField(Samples sampler, ProgressMonitor monitor);
//
//    VDiscrete computePoyntingVector(double[] x, double[] y, double[] z, ProgressMonitor monitor);
//
//    VDiscrete computePoyntingVector(Samples sampler, ProgressMonitor monitor);

//    Complex[][] computeCurrent(double[] x, double[] y, Axis axis);

//    Complex[][] computeCurrent(double[] x, double[] y, Axis axis, ProgressMonitor monitor);

//    VDiscrete computeCurrent(double[] x, double[] y, ProgressMonitor monitor);

//    VDiscrete computeCurrent(Samples sampler);

//    VDiscrete computeCurrent(Samples sampler, ProgressMonitor monitor);

    CurrentBuilder current();
    public SourceBuilder source();
    public TestFieldBuilder testField();
    ElectricFieldBuilder electricField();
    ElectricFieldBuilder electricField(ElectricFieldPart part);
    MagneticFieldBuilder magneticField();
    CapacityBuilder capacity();
    SelfBuilder self();
    InputImpedanceBuilder inputImpedance();
    SParametersBuilder sparameters();
    PoyntingVectorBuilder poyntingVector();

    void setParameter(String name);

    void setParameterNotNull(String name, Object value);

    void setParameter(String name, Object value);

    void removeParameter(String name);

    Object getParameter(String name);

    Object getParameter(String name, Object defaultValue);

    Number getParameterNumber(String name, Number defaultValue);

    boolean containsParameter(String name);

    boolean isParameter(String name, boolean defaultValue);

    boolean isParameter(String name);

    PersistenceCache getPersistentCache();

    Object getUserObject(String name, Object defaultValue);

    Object getUserObject(String name);

    void removeUserObject(String name);

    MWStructure setUserObject(String name, Object value);

    ObjectCache getCurrentCache(boolean autoCreate);

    String getName();

    MWStructure setName(String name);
}
