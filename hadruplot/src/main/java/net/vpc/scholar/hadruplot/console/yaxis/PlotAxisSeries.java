package net.vpc.scholar.hadruplot.console.yaxis;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.common.util.Chronometer;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.console.*;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.params.XParamSet;
import net.vpc.scholar.hadruplot.util.PlotUtils;

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
        Chronometer chronometer = Chronometer.start();
//        x1values = getX(direct, modele, x_axis);
//        x2values = getY(direct, modele, x_axis);
        PlotMatrix[] yvalues = eval(p);
        chronometer.stop();

        ArrayList<ConsoleAction> all = new ArrayList<ConsoleAction>();
        for (PlotMatrix yvalue : yvalues) {
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
                    getDoubleConverter(),
                    new WindowPath(p.getPreferredPath(), getName())//preferredPath
                    , getLibraries()
            ));
        }
        return all.iterator();
    }

    public PlotMatrix[] eval(ConsoleActionParams p) {
        ArrayList<PlotMatrix> ret = new ArrayList<PlotMatrix>();
        YType[] yTypes = getTypes();
        PlotMatrix referenceMatrix = null;
        PlotMatrix modeledMatrix = null;
        PlotMatrix relativeError = null;
        PlotMatrix absoluteError = null;
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
        final net.vpc.common.mon.ProgressMonitor[] monitors = ProgressMonitors.split(this, new double[]{9, 9, 1, 1}, new boolean[]{_b_referenceMatrix, _b_modeledMatrix, _b_relativeError, _b_absoluteError});
        ParamSet theX = p.getAxis().getX();
        double xParamMultiplier = theX == null ? 1 : theX.getMultiplier();
        if (_b_referenceMatrix) {
            referenceMatrix = evalValue(p.getStructure(), monitors[0], p).setName("[Ref]");
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
            modeledMatrix = evalValue(p.getStructure2(), monitors[1], p).setName("[Model]");
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
            final Object[][] d = referenceMatrix.getMatrix();
            final Object[][] m = modeledMatrix.getMatrix();
            if (d.length == m.length && (d.length == 0 || (d.length > 0 && d[0].length == m[0].length))) {//sinon non comparables
                final PlotMatrix finalReferenceMatrix = referenceMatrix;
                relativeError = ProgressMonitors.invokeMonitoredAction(monitors[2], getPlotTitle() + ":relativeError", new MonitoredAction<PlotMatrix>() {
                    @Override
                    public PlotMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        Object[][] c = PlotUtils.relativeError(d,m);
                        return (new PlotMatrix(c, finalReferenceMatrix.getColumns(), finalReferenceMatrix.getRows()).setName("[%]"));
                    }
                });
            }
        }
        if (_b_absoluteError) {
            final Object[][] d = referenceMatrix.getMatrix();
            final Object[][] m = modeledMatrix.getMatrix();
            if (d.length == m.length && (d.length == 0 || (d.length > 0 && d[0].length == m[0].length))) {//sinon non comparables
                final PlotMatrix finalReferenceMatrix1 = referenceMatrix;
                absoluteError = ProgressMonitors.invokeMonitoredAction(monitors[3], getPlotTitle() + ":absoluteError", new MonitoredAction<PlotMatrix>() {
                    @Override
                    public PlotMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                        PlotMatrix absoluteError = (new PlotMatrix(PlotUtils.sub(d,m), finalReferenceMatrix1.getColumns(), finalReferenceMatrix1.getRows()).setName("[-]"));
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
        return ret.toArray(new PlotMatrix[0]);
    }

    protected abstract PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p);


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
