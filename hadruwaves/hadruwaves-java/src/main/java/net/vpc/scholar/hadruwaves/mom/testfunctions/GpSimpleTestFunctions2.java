package net.vpc.scholar.hadruwaves.mom.testfunctions;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.util.EnhancedProgressMonitor;
import net.vpc.scholar.hadrumaths.util.ProgressMonitor;
import net.vpc.scholar.hadrumaths.util.MonitoredAction;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;

import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 juil. 2007 23:14:54
 */
public class GpSimpleTestFunctions2 extends TestFunctionsBase implements Cloneable {
    private TestFunctionCell[] cells;
    private Domain globalDomain;

    public GpSimpleTestFunctions2(Domain globalDomain, TestFunctionCell... cells) {
        super();
        this.cells = cells;
        this.globalDomain = globalDomain;
    }

    @Override
    protected DoubleToVector[] gpImpl(ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(monitor, "Gp Detection", new MonitoredAction<DoubleToVector[]>() {
            @Override
            public DoubleToVector[] process(EnhancedProgressMonitor monitor, String messagePrefix) throws Exception {
                ArrayList<DoubleToVector> all = new ArrayList<DoubleToVector>();
                for (int i1 = 0; i1 < cells.length; i1++) {
                    TestFunctionCell cell = cells[i1];
                    GpPattern pattern = cell.getPattern();
                    DoubleToVector[] allGp = pattern.createFunctions(globalDomain, new MeshZone(cell.getDomain()), monitor, null);
                    int maxGp = allGp.length;
                    for (int i = 0; i < maxGp; i++) {
                        DoubleToVector _gp = allGp[i];
                        if (_gp != null) {
                            all.add(_gp);
                        }
                    }
                    monitor.setProgress(1.0 * i1 / cells.length, "Gp Detection");
                }
                return all.toArray(new DoubleToVector[all.size()]);
            }
        });

    }


    @Override
    public Dumper getDumpStringHelper() {
        arr();
        Dumper h = super.getDumpStringHelper();
        h.add("cells", cells);
        return h;
    }
}
