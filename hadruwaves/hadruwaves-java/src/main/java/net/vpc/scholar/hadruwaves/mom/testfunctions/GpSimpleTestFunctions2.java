package net.vpc.scholar.hadruwaves.mom.testfunctions;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;

import java.util.ArrayList;
import java.util.List;

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
            public DoubleToVector[] process(ProgressMonitor monitor, String messagePrefix) throws Exception {
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
                return all.toArray(new DoubleToVector[0]);
            }
        });

    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder h = super.toTsonElement(context).toObject().builder();
        h.add("cells", context.elem(cells));
        return h.build();
    }
    @Override
    public Geometry[] getGeometries() {
        List<Geometry> all = new ArrayList<>();
        for (TestFunctionCell cell : cells) {
            all.add(cell.getAreaGeometryList());
        }
        return all.toArray(new Geometry[0]);
    }

}
