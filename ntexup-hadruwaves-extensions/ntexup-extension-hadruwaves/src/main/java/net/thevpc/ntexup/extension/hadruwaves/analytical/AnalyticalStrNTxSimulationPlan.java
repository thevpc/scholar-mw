package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;

import java.nio.charset.StandardCharsets;

public class AnalyticalStrNTxSimulationPlan extends NTxSimulationPlanImpl {
    public AnalyticalModelInfo modelInfo;

    public AnalyticalStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
        super(id, name, rendererContext);
    }

    @Override
    public NTxSolverRun createItem(String computeName, String solverName) {
        switch (NNameFormat.LOWER_KEBAB_CASE.format(solverName)) {
            case "s11":
            case "sparam":
            case "sparams":
            case "s-param":
            case "s-params": {
                return new NTxAnalyticalS11Solver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxAnalyticalZinSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "AnalyticalQuasiTEM".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.width).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.stubLength).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.height).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    private net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel circuitModel;

    public synchronized net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel getCircuitModel() {
        if (circuitModel == null) {
            if (modelInfo == null) {
                modelInfo = new AnalyticalModelInfo();
            }
            circuitModel = net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel.parse(
                    modelInfo.sceneNode,
                    modelInfo.resolutionContext,
                    modelInfo.epsilonR,
                    modelInfo.lossTangent,
                    modelInfo.z0Ref
            );
        }
        return circuitModel;
    }

    public Complex computeZin(double freq) {
        net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel model = getCircuitModel();
        if (model.patches.isEmpty() && model.lines.isEmpty()) {
            if (modelInfo == null) {
                modelInfo = new AnalyticalModelInfo();
            }
            return MicrostripAnalyticalModel.computeZin(modelInfo, freq);
        }
        net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel.ComplexNum z = model.computeZin(freq);
        return Complex.of(z.re, z.im);
    }

    public Complex computeS11(double freq) {
        net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel model = getCircuitModel();
        if (model.patches.isEmpty() && model.lines.isEmpty()) {
            if (modelInfo == null) {
                modelInfo = new AnalyticalModelInfo();
            }
            return MicrostripAnalyticalModel.computeS11(modelInfo, freq);
        }
        net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel.ComplexNum s = model.computeS11(freq);
        return Complex.of(s.re, s.im);
    }
}
