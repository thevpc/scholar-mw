package net.vpc.scholar.hadruwaves.mom.str.momstr;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.monitors.ProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.monitors.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.str.MWStructure;
import net.vpc.scholar.hadruwaves.str.SourceEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:15:31
 */
public class DefaultSourceEvaluator implements SourceEvaluator {
    public static final DefaultSourceEvaluator INSTANCE = new DefaultSourceEvaluator();

    public Matrix computePlanarSources(MWStructure structure, double[] x, double[] y, Axis axis, ProgressMonitor monitor) {
        MomStructure str = (MomStructure) structure;
        EnhancedProgressMonitor mon = ProgressMonitorFactory.enhance(monitor);
        Sources ss = str.getSources();
        if (ss == null || !(ss instanceof PlanarSources)) {
            throw new IllegalArgumentException();
        }
        String monName = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(mon, monName, new MonitoredAction<Matrix>() {
            @Override
            public Matrix process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                Complex[][] ret = null;
                DoubleToVector[] _g = ((PlanarSources) ss).getSourceFunctions();
                for (int i = 0; i < _g.length; i++) {
                    mon.setProgress(i, _g.length, monName);
                    //            monitor.setProgress(((double) i) / _g.length);
                    DoubleToVector g = _g[i];
                    Complex[][] vals = g.getComponent(axis).toDC().computeComplex(x, y);
                    if (ret == null) {
                        ret = vals;
                    } else {
                        for (int xi = 0; xi < x.length; xi++) {
                            for (int yi = 0; yi < y.length; yi++) {
                                ret[yi][xi] = ret[yi][xi].add(vals[yi][xi]);
                            }
                        }
                    }
                }
                return Maths.matrix(ret);
            }
        });
    }


    @Override
    public String toString() {
        return getClass().getName();
    }

    @Override
    public String dump() {
        return getClass().getName();
    }
}
