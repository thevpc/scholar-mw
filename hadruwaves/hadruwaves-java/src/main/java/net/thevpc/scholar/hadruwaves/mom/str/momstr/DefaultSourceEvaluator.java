package net.thevpc.scholar.hadruwaves.mom.str.momstr;

import net.thevpc.common.mon.MonitoredAction;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.str.SourceEvaluator;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 août 2007 09:15:31
 */
public class DefaultSourceEvaluator implements SourceEvaluator {
    public static final DefaultSourceEvaluator INSTANCE = new DefaultSourceEvaluator();

    public ComplexMatrix evalPlanarSources(MWStructure structure, final double[] x, final double[] y, final Axis axis, ProgressMonitor monitor) {
        MomStructure str = (MomStructure) structure;
        final ProgressMonitor mon = ProgressMonitors.nonnull(monitor);
        final Sources ss = str.getSources();
        if (ss == null || !(ss instanceof PlanarSources)) {
            throw new IllegalArgumentException();
        }
        final String monName = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(mon, monName, new MonitoredAction<ComplexMatrix>() {
            @Override
            public ComplexMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                Complex[][] ret = null;
                DoubleToVector[] _g = ((PlanarSources) ss).getSourceFunctions();
                for (int i = 0; i < _g.length; i++) {
                    mon.setProgress(i, _g.length, monName);
                    //            monitor.setProgress(((double) i) / _g.length);
                    DoubleToVector g = _g[i];
                    Complex[][] vals = g.getComponent(axis).toDC().evalComplex(x, y);
                    if (ret == null) {
                        ret = vals;
                    } else {
                        for (int xi = 0; xi < x.length; xi++) {
                            for (int yi = 0; yi < y.length; yi++) {
                                ret[yi][xi] = ret[yi][xi].plus(vals[yi][xi]);
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
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofFunction(getClass().getSimpleName()).build();
    }
}
