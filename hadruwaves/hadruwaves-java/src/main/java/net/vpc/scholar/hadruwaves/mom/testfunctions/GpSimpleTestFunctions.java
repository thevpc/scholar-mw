package net.vpc.scholar.hadruwaves.mom.testfunctions;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.common.mon.ProgressMonitor;

import net.vpc.common.mon.MonitoredAction;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 10 juil. 2007 23:14:54
 */
public class GpSimpleTestFunctions extends TestFunctionsBase implements Cloneable{
    private TestFunctionCell[] cells;

    public GpSimpleTestFunctions(TestFunctionCell... cells) {
        super();
        this.cells = cells;
    }

    @Override
    protected DoubleToVector[] gpImpl(ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(monitor, "Gp Detection", new MonitoredAction<DoubleToVector[]>() {
            @Override
            public DoubleToVector[] process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ArrayList<DoubleToVector> all=new ArrayList<DoubleToVector>();
                for (int i1 = 0, cellsLength = cells.length; i1 < cellsLength; i1++) {
                    TestFunctionCell cell = cells[i1];
                    GpPattern pattern = cell.getPattern();
                    DoubleToVector[] allGp = pattern.createFunctions(getStructure().getDomain(), new MeshZone(cell.getDomain()), monitor, getStructure());
                    int maxGp = allGp.length;
                    for (int i = 0; i < maxGp; i++) {
                        DoubleToVector _gp = allGp[i];
                        if (_gp != null) {
                            all.add(_gp);
                        }
                    }
                    monitor.setProgress(1.0*i1/cells.length,"Gp Detection");
                }
                return all.toArray(new DoubleToVector[all.size()]);
            }
        });
    }


    @Override
    public Dumper getDumpStringHelper() {
        arr();
        Dumper h = super.getDumpStringHelper();
        h.add("cells",cells);
        return h;
    }
}
