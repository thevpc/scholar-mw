package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.hadruwaves.solvers.*;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.nio.charset.StandardCharsets;
import java.util.List;

import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;

public class MoMStrNTxSimulationPlan extends NTxSimulationPlanImpl {

    public MomStructure str;
    public List<HGeometry> antennaGeometries;

    public MoMStrNTxSimulationPlan(String id, String name,NTxRendererContext rendererContext, MomStructure str) {
        super(id, name,rendererContext);
        this.str = str;
        Maths.Config.setCacheEnabled(false);
        addSolverListener(new NTxSolverListener() {
            @Override
            public void onStart(NTxRendererContext rendererContext, NTxSimulationPlan plan) {
                MoMStrNTxSimulationPlan p = (MoMStrNTxSimulationPlan) plan;
                rendererContext.log(NMsg.ofC("==== MOM STR DUMP"));
                rendererContext.log(NMsg.ofC("%s", p.str.toElement().toPrettyString()));
                rendererContext.log(NMsg.ofC("==========================="));
            }
        });
    }

    @Override
    public NTxSolverRun createItem(String computeName, String solverName) {
        switch (NNameFormat.LOWER_KEBAB_CASE.format(solverName)) {
            case "s11":
            case "sparam":
            case "sparams":
            case "s-param":
            case "s-params": {
                return new NTxHwS11Solver(this, computeName, solverName);
            }
            case "zin":
            case "z-in": {
                return new NTxHwZinSolver(this, computeName, solverName);
            }
            case "gp":
            case "gpq":
            case "test-functions":
            case "basis-functions": {
                return new NTxHwTestFunctionsSolver(this, computeName, solverName);
            }
            case "mesh": {
                return new NTxHwMeshSolver(this, computeName, solverName);
            }
            case "fn":
            case "fm":
            case "fmn":
            case "mode-functions": {
                return new NTxHwModeFunctionsSolver(this, computeName, solverName);
            }
            case "zn":
            case "zm":
            case "zmn":
            case "mode-impedance":
            case "mode-impedances": {
                return new NTxHwModeImpedanceSolver(this, computeName, solverName);
            }
            case "j":
            case "current": {
                return new NTxHwMatrixCurrentSolver(computeName, solverName, this);
            }
            case "jx":
            case "j-x":
            case "current-x": {
                return new NTxHwMatrixCurrentSolver(computeName, solverName, this).add("axis", NElement.ofName("X"));
            }
            case "jy":
            case "j-y":
            case "current-y": {
                return new NTxHwMatrixCurrentSolver(computeName, solverName, this).add("axis", NElement.ofName("Y"));
            }
            case "e":
            case "electric-field": {
                return new NTxHwMatrixElectricFieldSolver(this, computeName, solverName);
            }
            case "ex":
            case "e-x":
            case "electric-field-x": {
                return new NTxHwMatrixElectricFieldSolver(this, computeName, solverName).add("axis", NElement.ofName("X"));
            }
            case "ey":
            case "e-y":
            case "electric-field-y": {
                return new NTxHwMatrixElectricFieldSolver(this, computeName, solverName).add("axis", NElement.ofName("Y"));
            }
            case "sp":
            case "scalar-products":
            case "fn-gp":
            case "fnm-gpq":
            case "f-n-g-p":
            case "f-nm-g-pq": {
                return new NTxHwScalarProductsSolver(this, computeName, solverName);
            }
            case "a":
            case "a-matrix":
            case "matrix-a": {
                return new NTxHwMatrixASolver(this, computeName, solverName);
            }
            case "b":
            case "b-matrix":
            case "matrix-b": {
                return new NTxHwMatrixBSolver(this, computeName, solverName);
            }
            case "x":
            case "x-matrix":
            case "matrix-x": {
                return new NTxHwMatrixXSolver(this, computeName, solverName);
            }
            case "time":
            case "chrono":
            case "chronometer":
            {
                return new NTxHwChronometerSolver(this, computeName, solverName);
            }
        }
        return null;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, str.toElement().toString().getBytes(StandardCharsets.UTF_8));
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }
}
