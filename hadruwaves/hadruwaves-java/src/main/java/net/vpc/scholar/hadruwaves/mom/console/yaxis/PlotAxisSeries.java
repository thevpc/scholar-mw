package net.vpc.scholar.hadruwaves.mom.console.yaxis;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.plot.console.*;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.params.XParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.PlotAxis;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.MonitoredAction;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author : vpc
 * @creationtime 5 févr. 2006 15:07:36
 */
public abstract class PlotAxisSeries extends PlotAxis implements Cloneable {
    public String plotTitle = null;

    protected PlotAxisSeries(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeries(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

//    protected NamedVector computeComplexes(MomStructure structure, ParamSet x) {
//        return new NamedVector(toString(), new Complex[]{computeComplexArg(structure, x)}, new double[]{1}, new String[]{toString()});
//    }
//
//    protected Complex computeComplexArg(MomStructure structure, ParamSet x) {
//        return Complex.ZERO;
//    }

    @Override
    public Iterator<ConsoleAction> createConsoleActionIterator(ConsoleActionParams p) {
        Chronometer chronometer = new Chronometer();
        chronometer.start();
//        x1values = getX(direct, modele, x_axis);
//        x2values = getY(direct, modele, x_axis);
        NamedMatrix[] yvalues = compute(p);
        chronometer.stop();

        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        for (NamedMatrix yvalue : yvalues) {
            String _plotTitle = toString() + "-" + yvalue.getName();
            if (_plotTitle.length() > 0 && p.getSerieTitle().toString().length() > 0) {
                _plotTitle += " : ";
            }
            _plotTitle += p.getSerieTitle();
            all.add(new ConsoleActionPlot(
                    yvalue, //values
                    _plotTitle, //title
                    yvalue.getName(), //group
                    getInfiniteValue(), // infiniteValue
                    getPlotType(), //plotType
                    getComplexAsDouble(),
                    new WindowPath(p.getPreferredPath(), getName())//preferredPath
                    , getPreferredLibraries()
            ));
        }
        return all.iterator();
    }

    public NamedMatrix[] compute(ConsoleActionParams p) {
        ArrayList<NamedMatrix> ret = new ArrayList<NamedMatrix>();
        YType[] yTypes = getTypes();
        NamedMatrix referenceMatrix = null;
        NamedMatrix modeledMatrix = null;
        NamedMatrix relativeError = null;
        NamedMatrix absoluteError = null;
        boolean _b_referenceMatrix = false;
        boolean _b_modeledMatrix = false;
        boolean _b_relativeError = false;
        boolean _b_absoluteError = false;
        for (YType yType : yTypes) {
            switch (yType) {
                case REFERENCE: {
                    _b_referenceMatrix |= p.getStructure() != null;
                    break;
                }
                case MODELED: {
                    _b_modeledMatrix |= p.getStructure2() != null;
                    break;
                }
                case RELATIVE_ERROR: {
                    _b_referenceMatrix |= p.getStructure() != null;
                    _b_modeledMatrix |= p.getStructure2() != null;
                    _b_relativeError |= p.getStructure2() != null;
                    break;
                }
                case ABSOLUTE_ERROR: {
                    _b_referenceMatrix |= p.getStructure() != null;
                    _b_modeledMatrix |= p.getStructure2() != null;
                    _b_absoluteError |= p.getStructure2() != null;
                    break;
                }
                case REFERENCE_VS_MODELED: {
                    _b_referenceMatrix |= p.getStructure() != null;
                    _b_modeledMatrix |= p.getStructure2() != null;
                    break;
                }
            }
        }
        EnhancedProgressMonitor[] monitors = ProgressMonitorFactory.split(this, new double[]{9, 9, 1, 1}, new boolean[]{_b_referenceMatrix, _b_modeledMatrix, _b_relativeError, _b_absoluteError});
        ParamSet theX = p.getAxis().getX();
        double xParamMultiplier = theX == null ? 1 : theX.getMultiplier();
        if (_b_referenceMatrix) {
            referenceMatrix = computeValue(p.getStructure(), monitors[0], p).setName("[Ref]");
            if (!monitors[0].isTerminated()) {
                monitors[0].terminate(getName());
            }
            double[] doubles = referenceMatrix.getColumns();
            if (!Double.isNaN(xParamMultiplier)) {
                for (int i = 0; i < doubles.length; i++) {
                    doubles[i] *= xParamMultiplier;
                }
            }
            if (!Double.isNaN(getMultiplier(YType.REFERENCE))) {
                referenceMatrix.mul(getMultiplier(YType.REFERENCE));
            }
        }
        if (_b_modeledMatrix) {
            modeledMatrix = computeValue(p.getStructure2(), monitors[1], p).setName("[Model]");
            if (!monitors[1].isTerminated()) {
                monitors[1].terminate(getName());
            }
            double[] doubles = modeledMatrix.getColumns();
            if (!Double.isNaN(xParamMultiplier)) {
                for (int i = 0; i < doubles.length; i++) {
                    doubles[i] *= xParamMultiplier;
                }
            }
            if (!Double.isNaN(getMultiplier(YType.MODELED))) {
                modeledMatrix.mul(getMultiplier(YType.MODELED));
            }
        }
        if (_b_relativeError) {
            Complex[][] d = referenceMatrix.getMatrix();
            Complex[][] m = modeledMatrix.getMatrix();
            if (d.length == m.length && (d.length == 0 || (d.length > 0 && d[0].length == m[0].length))) {//sinon non comparables
                NamedMatrix finalReferenceMatrix = referenceMatrix;
                relativeError = Maths.invokeMonitoredAction(monitors[2], getPlotTitle() + ":relativeError", new MonitoredAction<NamedMatrix>() {
                    @Override
                    public NamedMatrix process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                        Complex[][] c = new Complex[d.length][];
                        for (int i = 0; i < c.length; i++) {
                            c[i] = new Complex[d[i].length];
                            monitor.setProgress(((double) i) / c.length, "");
                            for (int j = 0; j < c[i].length; j++) {
                                Complex q = d[i][j];
                                if (d[i][j].equals(m[i][j]) || (d[i][j].isNaN() && m[i][j].isNaN()) || (d[i][j].isInfinite() && m[i][j].isInfinite())) {
                                    c[i][j] = Maths.CZERO;
                                } else if (q.isNaN() || q.isInfinite() || q.equals(Maths.CZERO)) {
                                    c[i][j] = (d[i][j].sub(m[i][j]));//TODO ??
                                } else {
                                    c[i][j] = Complex.valueOf(((d[i][j].sub(m[i][j])).absdbl() * 100 / (d[i][j].absdbl())));
                                }
                            }
                        }
                        return (new NamedMatrix(c, finalReferenceMatrix.getColumns(), finalReferenceMatrix.getRows()).setName("[%]"));
                    }
                });
            }
        }
        if (_b_absoluteError) {
            Complex[][] d = referenceMatrix.getMatrix();
            Complex[][] m = modeledMatrix.getMatrix();
            if (d.length == m.length && (d.length == 0 || (d.length > 0 && d[0].length == m[0].length))) {//sinon non comparables
                NamedMatrix finalReferenceMatrix1 = referenceMatrix;
                absoluteError = Maths.invokeMonitoredAction(monitors[3], getPlotTitle() + ":absoluteError", new MonitoredAction<NamedMatrix>() {
                    @Override
                    public NamedMatrix process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                        Complex[][] c0 = new Complex[d.length][];
                        for (int i = 0; i < c0.length; i++) {
                            c0[i] = new Complex[d[i].length];
                            monitors[3].setProgress(((double) i) / c0.length, getName());
                            for (int j = 0; j < c0[i].length; j++) {
                                c0[i][j] = (d[i][j].sub(m[i][j]));
                            }
                        }
                        NamedMatrix absoluteError = (new NamedMatrix(c0, finalReferenceMatrix1.getColumns(), finalReferenceMatrix1.getRows()).setName("[-]"));
                        if (!Double.isNaN(getMultiplier())) {
                            absoluteError.mul(getMultiplier());
                        }
                        return absoluteError;
                    }
                });
            }
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
                    if (modeledMatrix != null && referenceMatrix != null) {
                        ret.add(referenceMatrix.appendRows(modeledMatrix, "[ref]-", "[mod]-"));
                    }
                    break;
                }
            }
        }
        return ret.toArray(new NamedMatrix[ret.size()]);
    }

    protected abstract NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p);


    public String getPlotTitle() {
        return plotTitle;
    }

    public PlotAxisSeries setPlotTitle(String plotTitle) {
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


}
