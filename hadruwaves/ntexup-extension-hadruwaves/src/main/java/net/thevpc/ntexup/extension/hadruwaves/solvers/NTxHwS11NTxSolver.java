package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwComplexNTxSolver;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwS11NTxSolver extends NTxHwComplexNTxSolver {

    public NTxHwS11NTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery,computeName, solverName,"s-parameters");
    }

    protected Complex evalComplex(MomStructure str) {
        return str.sparameters().evalComplex();
    }

}
