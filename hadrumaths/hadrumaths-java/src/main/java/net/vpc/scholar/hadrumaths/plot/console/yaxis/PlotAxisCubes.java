package net.vpc.scholar.hadrumaths.plot.console.yaxis;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.*;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.symbolic.VDiscrete;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author : vpc
 * @creationtime 5 févr. 2006 15:07:36
 */
public abstract class PlotAxisCubes extends PlotAxis implements Cloneable {
    public String plotTitle = null;

    protected PlotAxisCubes(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisCubes(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        Chronometer chronometer = new Chronometer();
        chronometer.start();
//        x1values = getX(direct, modele, x_axis);
//        x2values = getY(direct, modele, x_axis);
        VDiscrete[] yvalues = compute(p);
        chronometer.stop();

        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        for (VDiscrete yvalue : yvalues) {
            String plotTitle = toString() + "-" + yvalue.getTitle();
            if (plotTitle.length() > 0 && p.getSerieTitle().toString().length() > 0) {
                plotTitle += " : ";
            }
            plotTitle += p.getSerieTitle();
            all.add(new ConsoleActionCubesPlot(
                    yvalue, //values
                    plotTitle, //title
                    yvalue.getTitle(), //group
                    getInfiniteValue(), // infiniteValue
                    getPlotType(), //plotType
                    new WindowPath(p.getPreferredPath(), getName())//preferredPath
                    , getPreferredLibraries()
            ));
        }
        return all.iterator();
    }

    public VDiscrete[] compute(ConsoleActionParams p) {
        ArrayList<VDiscrete> ret = new ArrayList<VDiscrete>();
        YType[] yTypes = getTypes();
        VDiscrete referenceMatrix = null;
        VDiscrete modeledMatrix = null;
        VDiscrete relativeError = null;
        VDiscrete absoluteError = null;
        boolean _b_referenceMatrix = false;
        boolean _b_modeledMatrix = false;
        boolean _b_relativeError = false;
        boolean _b_absoluteError = false;
        for (YType yType : yTypes) {
            switch (yType) {
                case REFERENCE: {
                    _b_referenceMatrix = p.getStructure() != null;
                    break;
                }
                case MODELED: {
                    _b_modeledMatrix = p.getStructure2() != null;
                    break;
                }
                case REFERENCE_VS_MODELED:
                case RELATIVE_ERROR: {
                    _b_referenceMatrix = p.getStructure() != null;
                    _b_modeledMatrix = p.getStructure2() != null;
                    _b_relativeError = p.getStructure2() != null;
                    break;
                }
                case ABSOLUTE_ERROR: {
                    _b_referenceMatrix = p.getStructure() != null;
                    _b_modeledMatrix = p.getStructure2() != null;
                    _b_absoluteError = p.getStructure2() != null;
                    break;
                }
            }
        }
        EnhancedProgressMonitor[] monitors = ProgressMonitorFactory.split(this, new double[]{10, 10, 1, 1}, new boolean[]{_b_referenceMatrix, _b_modeledMatrix, _b_relativeError, _b_absoluteError});
        EnhancedProgressMonitor monitor0 = monitors[0];
        EnhancedProgressMonitor monitor1 = monitors[1];
        EnhancedProgressMonitor monitor2 = monitors[2];
        EnhancedProgressMonitor monitor3 = monitors[3];
        ParamSet theX = p.getAxis().getX();
        double xmultiplier = theX == null ? 1 : theX.getMultiplier();
        if (_b_referenceMatrix) {
            referenceMatrix = (VDiscrete) computeValue(p.getStructure(), monitor0, p);//.setTitle("[Ref]");
            if (!monitor0.isTerminated()) {
                monitor0.terminate(getName() + " termination forced!");
            }
            if (!Double.isNaN(getMultiplier(YType.REFERENCE))) {
                referenceMatrix.mul(getMultiplier(YType.REFERENCE));
            }
//            double[] doubles = referenceMatrix.getColumnCount();
//            if (!Double.isNaN(xmultiplier)) {
//                for (int i = 0; i < doubles.length; i++) {
//                    doubles[i] *= xmultiplier;
//                }
//            }
//            if (!Double.isNaN(getMultiplier())) {
//                referenceMatrix.multiply(getMultiplier());
//            }
        }
        if (_b_modeledMatrix) {
            modeledMatrix = (VDiscrete) computeValue(p.getStructure2(), monitor1, p);//.setTitle("[Model]");
            if (!monitor1.isTerminated()) {
                monitor1.terminate(getName() + " termination forced!");
            }
            if (!Double.isNaN(getMultiplier(YType.MODELED))) {
                modeledMatrix.mul(getMultiplier(YType.MODELED));
            }
//            double[] doubles = modeledMatrix.getColumnCount();
//            if (!Double.isNaN(xmultiplier)) {
//                for (int i = 0; i < doubles.length; i++) {
//                    doubles[i] *= xmultiplier;
//                }
//            }
//            if (!Double.isNaN(getMultiplier())) {
//                modeledMatrix.multiply(getMultiplier());
//            }
        }
        if (_b_relativeError) {
            assert referenceMatrix != null;
            relativeError = (VDiscrete) referenceMatrix.relativeError(modeledMatrix);//.setTitle("[%]");
            monitor2.terminate(getName() + " relative error evaluated.");
//            Complex[][] d = referenceMatrix.get();
//            Complex[][] m = modeledMatrix.getMatrix();
//            Complex[][] c = new Complex[d.length][];
//            for (int i = 0; i < c.length; i++) {
//                c[i] = new Complex[d[i].length];
//                progress += (100.0) / c.length;
//                for (int j = 0; j < c[i].length; j++) {
//                    Complex q = d[i][j];
//                    if (d[i][j].equals(m[i][j]) || (d[i][j].isNaN() && m[i][j].isNaN()) || (d[i][j].isInfinite() && m[i][j].isInfinite())) {
//                        c[i][j] = Math2.CZERO;
//                    } else if (q.isNaN() || q.isInfinite() || q.equals(Math2.CZERO)) {
//                        c[i][j] = (d[i][j].substract(m[i][j]));//TODO ??
//                    } else {
//                        c[i][j] = new Complex(((d[i][j].substract(m[i][j])).absdbl() * 100 / (d[i][j].absdbl())));
//                    }
//                }
//            }
//            relativeError = (new NamedMatrix(c, referenceMatrix.getColumnCount(), referenceMatrix.getRowCount()).setName("[%]"));
        }
        if (_b_absoluteError) {
            assert referenceMatrix != null;
            absoluteError = (VDiscrete) referenceMatrix.sub(modeledMatrix);//.setTitle("[<>]");
            if (!Double.isNaN(getMultiplier())) {
                absoluteError.mul(getMultiplier());
            }
            monitor3.terminate(getName() + " relative error evaluated.");
        }
        for (YType yType : yTypes) {
            switch (yType) {
                case REFERENCE: {
                    if (referenceMatrix != null) {
                        ret.add(referenceMatrix);
                    }
                    break;
                }
                case MODELED: {
                    if (modeledMatrix != null) {
                        ret.add(modeledMatrix);
                    }
                    break;
                }
                case RELATIVE_ERROR: {
                    if (relativeError != null) {
                        ret.add(relativeError);
                    }
                    break;
                }
                case ABSOLUTE_ERROR: {
                    if (absoluteError != null) {
                        ret.add(absoluteError);
                    }
                    break;
                }
                case REFERENCE_VS_MODELED: {
                    if (modeledMatrix != null) {
                        ret.add((VDiscrete) new VDiscrete(modeledMatrix.getComponent(Axis.X), modeledMatrix.getComponent(Axis.Y), modeledMatrix.getComponent(Axis.Z))
                                //.setTitle("VS")
                        );
                    }
                    break;
                }
            }
        }
        return ret.toArray(new VDiscrete[ret.size()]);
    }

    protected abstract VDiscrete computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p);


    public String getPlotTitle() {
        return plotTitle;
    }

    public PlotAxisCubes setPlotTitle(String plotTitle) {
        this.plotTitle = plotTitle;
        return this;
    }


    protected Object checkXParam(Class needed, Object instance) {
        if (!(instance instanceof XParamSet)) {
            JOptionPane.showMessageDialog(null, getClass().getSimpleName() + " do needs " + needed.getSimpleName() + " but found " + instance.getClass(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        needed.cast(instance);
        return instance;
    }

    protected NamedVector convertMaytrixToNamedVector(boolean all, Matrix matrix, String name, String shortName) {
        ArrayList<Complex> a1 = new ArrayList<Complex>();
        ArrayList<String> t1 = new ArrayList<String>();
        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                if (all || (i == 0 && j == 0)) {
                    a1.add(matrix.get(i, j));
                    t1.add(shortName + (i + 1) + "" + (j + 1));
                }
            }
        }
        double[] xx = new double[a1.size()];
        for (int i = 0; i < xx.length; i++) {
            xx[i] = i + 1;
        }
        return new NamedVector(name, a1.toArray(new Complex[a1.size()]), xx, t1.toArray(new String[t1.size()]));
    }

}