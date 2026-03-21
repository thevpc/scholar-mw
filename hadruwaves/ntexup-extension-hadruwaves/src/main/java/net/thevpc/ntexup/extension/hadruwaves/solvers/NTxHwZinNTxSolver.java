package net.thevpc.ntexup.extension.hadruwaves.solvers;

import net.thevpc.ntexup.extension.hadruwaves.MoMStrNTxSimulationPlan;
import net.thevpc.ntexup.extension.hadruwaves.base.NTxHwComplexNTxSolver;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

public class NTxHwZinNTxSolver extends NTxHwComplexNTxSolver {

    public NTxHwZinNTxSolver(MoMStrNTxSimulationPlan moMStrSimulationQuery, String computeName, String solverName) {
        super(moMStrSimulationQuery, computeName, solverName,"input-impedance");
    }

    protected Complex evalComplex(MomStructure str) {
        return str.inputImpedance().evalComplex();
    }


}
