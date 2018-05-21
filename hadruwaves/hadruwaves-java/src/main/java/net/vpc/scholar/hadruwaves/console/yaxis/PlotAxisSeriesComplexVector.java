package net.vpc.scholar.hadruwaves.console.yaxis;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleAwareObject;
import net.vpc.scholar.hadrumaths.plot.console.params.ParamSet;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedMatrix;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.NamedVector;
import net.vpc.scholar.hadrumaths.plot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadrumaths.plot.console.ConsoleActionParams;
import net.vpc.scholar.hadrumaths.plot.console.xlabels.XLabel;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.plot.PlotType;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.PlotAxisSeries;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:00:27
 */
public abstract class PlotAxisSeriesComplexVector extends PlotAxisSeries {

    protected PlotAxisSeriesComplexVector(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeriesComplexVector(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    protected abstract NamedVector computeComplexes(MomStructure structure, ParamSet x, ConsoleActionParams p);

    @Override
    protected NamedMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix((MomStructure) structure,monitor,p);
    }

    protected final NamedMatrix computeMatrix(MomStructure structure, ProgressMonitor cmonitor, ConsoleActionParams p) {
        EnhancedProgressMonitor monitor = ProgressMonitorFactory.enhance(cmonitor);
        ParamSet x = p.getAxis().getX();
        XLabel xlabel = p.getAxis().getXLabel();
//        Complex[][] z = new Complex[x.getSize()][];
//        String[][] t = new String[x.getSize()][];
        double[] xs = new double[x.getSize()];
        String[] xtitles = new String[x.getSize()];
        double[] ys;
        int index = 0;
        x.reset();
        NamedVector[] zz = new NamedVector[x.getSize()];
        monitor.setProgress(0, getClass().getSimpleName());
        while (x.hasNext()) {
            x.next();
            x.setParameter(structure);
            zz[index] = computeComplexes(structure, x, p);
            if (xlabel == null) {
                xs[index] = x.getXValue().doubleValue();
            } else {
                Number v = xlabel.getValue(index, x, structure);
                xs[index] = v.doubleValue();
            }
            xtitles[index]=x.getTitle();
            index++;
            ProgressMonitorFactory.setProgress(monitor,index,zz.length, getClass().getSimpleName());
        }
        monitor.setProgress(1, getClass().getSimpleName());
        Complex[][] rr = new Complex[zz[0].getIndexes().length][zz.length];
        String[][] tt = new String[zz[0].getIndexes().length][zz.length];
        String[] t = new String[zz[0].getIndexes().length];
        for (int i = 0; i < rr.length; i++) {
            for (int j = 0; j < rr[i].length; j++) {
                rr[i][j] = zz[j].getMatrix()[i];
                tt[i][j] = zz[j].getTitles()[i];
            }
        }
        for (int i = 0; i < rr.length; i++) {
            t[i] = zz[0].getTitles()[i];
        }
        ys = Maths.dtimes(0.0, rr[0].length - 1, 1);

        return new NamedMatrix(rr, xs, ys).setCellTitles(tt).setRowTitles(t).setColumnTitles(xtitles);
    }
}
