package net.thevpc.ntexup.extension.hadruwaves.solvers;

import java.util.List;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwNopNTxSolver;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.time.NChronometer;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.uicomponents.MultiAreaComponent;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

public class NTxHwMeshSolver extends NTxHwNopNTxSolver {

    public NTxHwMeshSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName, "test-functions");
    }

    @Override
    protected void nop() {
        MomStructure str = momStructure();
        TestFunctions testFunctions = str.testFunctions();
        List<MeshZone> m = testFunctions.mesh();
        log(NMsg.ofC("------------------"));
        log(NMsg.ofC("[%s] MESH (%s) : ", outputName(), m.size()));
        log(NMsg.ofC("------------------"));
        for (int i = 0; i < m.size(); i++) {
            log(NMsg.ofC("%s : %s", i, m.get(i)));
        }
//        MoMStrNTxSimulationPlan p = (MoMStrNTxSimulationPlan)plan();
//        m.clear();
//        m.add(new MeshZone(p.antennaGeometry));
        NLogger log = plan().rendererContext();
        log.log(NMsg.ofC("MeshRefinementHelper"));
        for (MeshZone triangleAndIteration : m) {
            log.log(NMsg.ofC("%s", triangleAndIteration));
        }
        if (plan().rendererContext().isAnimate()) {
            Plot.cd(fullPath())
                    .title("Mesh")
                    .plot(new MultiAreaComponent(m.stream().map(x -> x.getGeometry()).toArray(HGeometry[]::new)));
        }
    }


}
