package net.thevpc.ntexup.extension.hadruwaves;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.hadruwaves.solvers.*;
import net.thevpc.ntexup.extension.mwsimulator.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.log.NLogger;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.nio.charset.StandardCharsets;

public class MoMStrNTxSimulationPlan extends NTxSimulationPlanImpl {
    public MomStructure str;

    public MoMStrNTxSimulationPlan(String computeName, NTxRendererContext rendererContext, MomStructure str) {
        super(computeName,rendererContext);
        this.str = str;
        Maths.Config.setCacheEnabled(false);
    }

    @Override
    public NTxSolverRun createItem(String computeName, String solverName) {
        switch (NNameFormat.LOWER_KEBAB_CASE.format(solverName)) {
            case "s11":
            case "sparam":
            case "sparams":
            case "s-param":
            case "s-params": {
                return new NTxHwS11NTxSolver(this, computeName, solverName);
            }
            case "zin":
            case "z-in": {
                return new NTxHwZinNTxSolver(this, computeName, solverName);
            }
            case "gp":
            case "gpq":
            case "test-functions":
            case "basis-functions": {
                return new NTxHwTestFunctionsNTxSolver(this, computeName, solverName);
            }
            case "fn":
            case "fm":
            case "fmn":
            case "mode-functions": {
                return new NTxHwModeFunctionsNTxSolver(this, computeName, solverName);
            }
            case "zn":
            case "zm":
            case "zmn":
            case "mode-impedance":
            case "mode-impedances": {
                return new NTxHwModeImpedanceNTxSolver(this, computeName, solverName);
            }
            case "j":
            case "current": {
                return new NTxHwMatrixCurrentNTxSolver(computeName, solverName, this);
            }
            case "jx":
            case "j-x":
            case "current-x": {
                return new NTxHwMatrixCurrentNTxSolver(computeName, solverName, this).add("axis", NElement.ofName("X"));
            }
            case "jy":
            case "j-y":
            case "current-y": {
                return new NTxHwMatrixCurrentNTxSolver(computeName, solverName, this).add("axis", NElement.ofName("Y"));
            }
            case "e":
            case "electric-field":{
                return new NTxHwMatrixElectricFieldNTxSolver(this, computeName, solverName);
            }
            case "ex":
            case "e-x":
            case "electric-field-x": {
                return new NTxHwMatrixElectricFieldNTxSolver(this, computeName, solverName).add("axis", NElement.ofName("X"));
            }
            case "ey":
            case "e-y":
            case "electric-field-y": {
                return new NTxHwMatrixElectricFieldNTxSolver(this, computeName, solverName).add("axis", NElement.ofName("Y"));
            }
            case "sp":
            case "scalar-products":
            case "fn-gp":
            case "fnm-gpq":
            case "f-n-g-p":
            case "f-nm-g-pq": {
                return new NTxHwScalarProductsNTxSolver(this, computeName, solverName);
            }
            case "a":
            case "a-matrix":
            case "matrix-a": {
                return new NTxHwMatrixANTxSolver(this, computeName, solverName);
            }
            case "b":
            case "b-matrix":
            case "matrix-b": {
                return new NTxHwMatrixBNTxSolver(this, computeName, solverName);
            }
            case "x":
            case "x-matrix":
            case "matrix-x": {
                return new NTxHwMatrixXNTxSolver(this, computeName, solverName);
            }
        }
        return null;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        d.addSource(str.toElement().toString().getBytes(StandardCharsets.UTF_8));
        for (NTxSolverRun item : items) {
            d.addSource(item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }
}
