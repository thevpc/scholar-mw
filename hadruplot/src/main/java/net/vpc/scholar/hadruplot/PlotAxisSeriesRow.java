package net.vpc.scholar.hadruplot;

import net.vpc.common.util.ArrayUtils;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.params.ParamSet;
import net.vpc.scholar.hadruplot.console.yaxis.YType;

import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.xlabels.XLabel;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 31 juil. 2007 23:00:27
 */
public abstract class PlotAxisSeriesRow extends PlotAxisSeries {

    protected PlotAxisSeriesRow(String name, YType... type) {
        super(name, type);
    }

    protected PlotAxisSeriesRow(String name, YType[] type, PlotType graphix) {
        super(name, type, graphix);
    }

    protected abstract PlotNamedVector computeComplexes(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p);

    @Override
    protected PlotMatrix computeValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return computeMatrix(structure,monitor,p);
    }

    protected final PlotMatrix computeMatrix(ConsoleAwareObject structure, ProgressMonitor cmonitor, ConsoleActionParams p) {
        ProgressMonitor monitor = ProgressMonitorFactory.nonnull(cmonitor);
        ParamSet x = p.getAxis().getX();
        XLabel xlabel = p.getAxis().getXLabel();
//        Complex[][] z = new Complex[x.getSize()][];
//        String[][] t = new String[x.getSize()][];
        double[] xs = new double[x.getSize()];
        String[] xtitles = new String[x.getSize()];
        double[] ys;
        int index = 0;
        x.reset();
        PlotNamedVector[] zz = new PlotNamedVector[x.getSize()];
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
        Object[][] rr = new Object[zz[0].getIndexes().length][zz.length];
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
        ys = ArrayUtils.dtimes(0.0, rr[0].length - 1, 1);

        return new PlotMatrix(rr, xs, ys).setCellTitles(tt).setRowTitles(t).setColumnTitles(xtitles);
    }
}
