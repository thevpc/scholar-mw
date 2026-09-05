package net.thevpc.ntexup.extension.openems;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.ntexup.extension.openems.solvers.NTxHwS11NTxSolver;
import net.thevpc.ntexup.extension.openems.solvers.NTxHwZinNTxSolver;
import net.thevpc.nuts.command.NExec;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OpenEMSStrNTxSimulationPlan extends NTxSimulationPlanImpl {

    public OpenEMSParser.OpenEMSModelInfo modelInfo;
    private OpenEMSRunData runData;

    public static class OpenEMSRunData {
        public double[] tv;
        public double[] v;
        public double[] ti;
        public double[] i;
        public double dt;
    }

    public OpenEMSStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
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
                return new NTxHwS11NTxSolver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxHwZinNTxSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "OpenEMS".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.numberOfTimesteps).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.xmlPath != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.xmlPath.getBytes(StandardCharsets.UTF_8));
            }
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    public synchronized OpenEMSRunData runSimulation() {
        if (runData != null) {
            return runData;
        }
        if (modelInfo == null) {
            modelInfo = new OpenEMSParser.OpenEMSModelInfo();
        }

        NPath workDir = NPath.ofTempFolder("openems-sim-");
        try {
            NPath xmlFile = workDir.resolve("simulation.xml");
            if (modelInfo.xmlPath != null && !modelInfo.xmlPath.trim().isEmpty()) {
                NPath srcXml = NPath.of(modelInfo.xmlPath);
                if (!srcXml.isAbsolute() && rendererContext != null && rendererContext.document() != null && rendererContext.document().source() != null && rendererContext.document().source().path().isPresent()) {
                    srcXml = rendererContext.document().source().path().get().parent().resolve(srcXml);
                }
                srcXml.copyTo(xmlFile);
            } else {
                String xmlContent = OpenEMSParser.generateOpenEMSXml(modelInfo);
                xmlFile.writeString(xmlContent);
            }

            boolean hasDocker = false;
            try {
                hasDocker = NExec.ofSystem("docker").which() != null;
            } catch (Exception ex) {
                hasDocker = false;
            }

            if (hasDocker) {
                String dockerImage = "thevpc/openems:0.0.36";
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Running openEMS simulation in Docker (%s)", id, dockerImage));
                }
                NExec cmd = NExec.ofSystem("docker", "run", "--rm",
                        "-v", workDir.toAbsolute().normalize().toString() + ":/simulations",
                        "-w", "/simulations",
                        dockerImage,
                        "openEMS", xmlFile.name(),
                        "--numThreads=" + modelInfo.numThreads,
                        "--disable-dumps"
                ).directory(workDir);
                cmd.run();
            } else {
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Running native openEMS simulation in %s", id, workDir));
                }
                NExec cmd = NExec.ofSystem("openEMS", xmlFile.name(), "--numThreads=" + modelInfo.numThreads, "--disable-dumps")
                        .directory(workDir);
                cmd.run();
            }

            NPath vFile = workDir.resolve("port1_V");
            NPath iFile = workDir.resolve("port1_I");
            if (!vFile.exists() || !iFile.exists()) {
                throw new IllegalStateException("OpenEMS failed to generate probe files port1_V or port1_I in " + workDir);
            }

            double[][] vData = readProbeData(vFile);
            double[][] iData = readProbeData(iFile);

            runData = new OpenEMSRunData();
            runData.tv = vData[0];
            runData.v = vData[1];
            runData.ti = iData[0];
            runData.i = iData[1];
            runData.dt = runData.tv.length > 1 ? runData.tv[1] - runData.tv[0] : 1e-12;
            return runData;
        } catch (Exception ex) {
            throw new RuntimeException("OpenEMS simulation error: " + ex.getMessage(), ex);
        }
    }

    private static double[][] readProbeData(NPath file) throws IOException {
        List<String> lines = java.nio.file.Files.readAllLines(java.nio.file.Paths.get(file.toString()));
        List<Double> tList = new ArrayList<>();
        List<Double> vList = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("%")) {
                continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                try {
                    tList.add(Double.parseDouble(parts[0]));
                    vList.add(Double.parseDouble(parts[1]));
                } catch (NumberFormatException ignored) {
                }
            }
        }
        double[] t = new double[tList.size()];
        double[] val = new double[vList.size()];
        for (int i = 0; i < t.length; i++) {
            t[i] = tList.get(i);
            val[i] = vList.get(i);
        }
        return new double[][]{t, val};
    }

    public Complex computeVf(double freq) {
        OpenEMSRunData d = runSimulation();
        double real = 0;
        double imag = 0;
        for (int k = 0; k < d.tv.length; k++) {
            double angle = -2.0 * Math.PI * freq * d.tv[k];
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            real += d.v[k] * cos;
            imag += d.v[k] * sin;
        }
        return Complex.of(real * d.dt * 2.0, imag * d.dt * 2.0);
    }

    public Complex computeIf(double freq) {
        OpenEMSRunData d = runSimulation();
        double real = 0;
        double imag = 0;
        double dtI = d.ti.length > 1 ? d.ti[1] - d.ti[0] : d.dt;
        for (int k = 0; k < d.ti.length; k++) {
            double angle = -2.0 * Math.PI * freq * d.ti[k];
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            real += d.i[k] * cos;
            imag += d.i[k] * sin;
        }
        return Complex.of(real * dtI * 2.0, imag * dtI * 2.0);
    }

    public Complex computeZin(double freq) {
        Complex vf = computeVf(freq);
        Complex ifVal = computeIf(freq);
        if (ifVal.isZero()) {
            return Complex.of(1e9, 0);
        }
        return vf.div(ifVal);
    }

    public Complex computeS11(double freq) {
        Complex vf = computeVf(freq);
        Complex ifVal = computeIf(freq);
        Complex z0I = ifVal.mul(50.0);
        Complex num = vf.minus(z0I);
        Complex den = vf.plus(z0I);
        if (den.isZero()) {
            return Complex.ONE;
        }
        return num.div(den);
    }
}
