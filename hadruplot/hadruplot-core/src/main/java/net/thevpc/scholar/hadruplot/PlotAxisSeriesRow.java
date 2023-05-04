package net.thevpc.scholar.hadruplot;

import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.util.ArrayUtils;
import net.thevpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.console.yaxis.YType;

import net.thevpc.scholar.hadruplot.console.ConsoleActionParams;
import net.thevpc.scholar.hadruplot.console.xlabels.XLabel;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruplot.console.yaxis.PlotAxisSeries;

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

    protected abstract PlotNamedVector evalComplexes(ConsoleAwareObject structure, ParamSet x, ConsoleActionParams p);

    @Override
    protected PlotMatrix evalValue(ConsoleAwareObject structure, ProgressMonitor monitor, ConsoleActionParams p) {
        return evalMatrix(structure,monitor,p);
    }

    protected final PlotMatrix evalMatrix(ConsoleAwareObject structure, ProgressMonitor cmonitor, ConsoleActionParams p) {
        ProgressMonitor monitor = ProgressMonitors.nonnull(cmonitor);
        ParamSet x = p.getAxis().getX();
        XLabel xlabel = p.getAxis().getXLabel();
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
            zz[index] = evalComplexes(structure, x, p);
            if (xlabel == null) {
                xs[index] = x.getXValue().doubleValue();
            } else {
                Number v = xlabel.getValue(index, x, structure);
                xs[index] = v.doubleValue();
            }
            xtitles[index]=x.getTitle();
            index++;
            ProgressMonitors.setProgress(monitor,index,zz.length, getClass().getSimpleName());
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
