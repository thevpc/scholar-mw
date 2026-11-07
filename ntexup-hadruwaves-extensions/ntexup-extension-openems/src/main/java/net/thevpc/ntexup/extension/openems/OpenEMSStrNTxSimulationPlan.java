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

import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.util.*;

public class OpenEMSStrNTxSimulationPlan extends NTxSimulationPlanImpl {

    public OpenEMSParser.OpenEMSModelInfo modelInfo;
    private OpenEMSRunData runData;

    public static class OpenEMSRunData {
        public double[] tv;
        public double[] v;
        public double[] ti;
        public double[] i;
        public double dt;
        // de-embedding probes by name (port1_V1..3, port1_I1..2): {t, value}
        public Map<String, double[][]> probes = new HashMap<>();
        public OpenEMSParser.DeembedProbes layout;
        // sqrt(eps_eff) = c0 + c1 f + c2 f^2 (f in GHz) and alpha per mm, fitted over the excitation band
        public double[] sqrtEpsFit;
        public double alphaFit;
        public double sqrtEpsMin, sqrtEpsMax;
        // msl port: S11 from openEMS's CalcPort, on a frequency grid
        public double[] mslF;
        public Complex[] mslS11;
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
            NTxMwSimulationUtils.addDigestSource(d, (modelInfo.meshRefinement + "," + modelInfo.meshSubstrateCells + "," + modelInfo.meshMaxCellX + "," + modelInfo.meshMaxCellY
                    + "," + modelInfo.meshMaxCellZ + "," + modelInfo.meshMaxCellOutside + "," + modelInfo.meshMinCell).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, ("port:" + modelInfo.portDeembed + "," + modelInfo.portType + "," + modelInfo.portMeasureBefore
                    + ",ground:" + (modelInfo.infiniteGround ? "infinite" : "board")).getBytes(StandardCharsets.UTF_8));
            // the geometry id alone is not enough: an edited box under the same id must not reuse the cached run
            if (modelInfo.xmlPath == null) {
                NTxMwSimulationUtils.addDigestSource(d, OpenEMSParser.generateOpenEMSXml(modelInfo).getBytes(StandardCharsets.UTF_8));
            }
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
        if (modelInfo.portType.equals("msl")) {
            runData = runMslPort(workDir);
            return runData;
        }
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

            String dockerImage = OpenEMSProvisioner.DEFAULT_DOCKER_IMAGE;
            boolean useDocker = OpenEMSProvisioner.ensureDocker(dockerImage, rendererContext, id);

            if (useDocker) {
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
                NPath nativeBin = OpenEMSProvisioner.ensureNativeBinary(rendererContext, id);
                if (rendererContext != null) {
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] Running native openEMS simulation (%s) in %s", id, nativeBin, workDir));
                }
                NExec cmd = NExec.ofSystem(nativeBin.toString(), xmlFile.name(), "--numThreads=" + modelInfo.numThreads, "--disable-dumps")
                        .directory(workDir);
                cmd.run();
            }

            NTxMwSimulationUtils.keepSolverInputs(rendererContext, name, workDir);
            NPath vFile = workDir.resolve("port1_V");
            NPath iFile = workDir.resolve("port1_I");
            if (!vFile.exists() || !iFile.exists()) {
                throw new IllegalStateException("OpenEMS failed to generate probe files port1_V or port1_I in " + workDir);
            }

            double[][] vData = OpenEMSParser.readProbeData(vFile);
            double[][] iData = OpenEMSParser.readProbeData(iFile);

            runData = new OpenEMSRunData();
            runData.tv = vData[0];
            runData.v = vData[1];
            runData.ti = iData[0];
            runData.i = iData[1];
            runData.dt = runData.tv.length > 1 ? runData.tv[1] - runData.tv[0] : 1e-12;
            if (modelInfo.xmlPath == null) {
                runData.layout = OpenEMSParser.deembedProbes(modelInfo);
            }
            if (runData.layout != null) {
                for (String n : new String[]{"port1_V1", "port1_V2", "port1_V3", "port1_I1", "port1_I2"}) {
                    NPath f = workDir.resolve(n);
                    if (!f.exists()) {
                        throw new IllegalStateException("OpenEMS failed to generate de-embedding probe " + n + " in " + workDir);
                    }
                    runData.probes.put(n, OpenEMSParser.readProbeData(f));
                }
                fitPropagation(runData);
                Line l = lineAt(modelInfo.frequency);
                if (rendererContext != null) {
                    double s0 = polyval(runData.sqrtEpsFit, modelInfo.frequency / 1e9);
                    rendererContext.log(NMsg.ofC("[OpenEMS][%s] de-embedded port at %s GHz: Z0=%s, eps_eff=%s fitted (per-frequency %s to %s), alpha=%s Np/m, reference %s mm past the probes",
                            id, String.format(Locale.US, "%.3f", modelInfo.frequency / 1e9),
                            String.format(Locale.US, "%.2f%+.2fj", l.z0.getReal(), l.z0.getImag()),
                            String.format(Locale.US, "%.4f", s0 * s0),
                            String.format(Locale.US, "%.3f", runData.sqrtEpsMin * runData.sqrtEpsMin),
                            String.format(Locale.US, "%.3f", runData.sqrtEpsMax * runData.sqrtEpsMax),
                            String.format(Locale.US, "%.2f", runData.alphaFit / 1e-3),
                            String.format(Locale.US, "%.3f", runData.layout.refDistance)));
                }
            }
            return runData;
        } catch (Exception ex) {
            throw new RuntimeException("OpenEMS simulation error: " + ex.getMessage(), ex);
        }
    }

    /**
     * openEMS's own microstrip port, through its python interface: the extension writes the
     * geometry and mesh (geometry.xml) and the port's placement (driver.json); msl_driver.py,
     * fixed, calls AddMSLPort and CalcPort and writes s11.dat.
     */
    private OpenEMSRunData runMslPort(NPath workDir) {
        try {
            workDir.resolve("geometry.xml").writeString(OpenEMSParser.generateGeometryXml(modelInfo));
            Map<String, Object> drv = new LinkedHashMap<>(OpenEMSParser.mslPortLayout(modelInfo));
            drv.put("timesteps", modelInfo.numberOfTimesteps * modelInfo.meshRefinement);
            drv.put("endcriteria", modelInfo.endCriteria);
            drv.put("f0", modelInfo.frequency);
            drv.put("fc", modelInfo.fc);
            drv.put("boundaries", Arrays.asList("PML_8", "PML_8", "PML_8", "PML_8", "PEC", "PML_8"));
            drv.put("threads", modelInfo.numThreads);
            drv.put("ref_impedance", 50.0);
            drv.put("f_min", Math.max(0.1e9, modelInfo.frequency - 1.5 * modelInfo.fc));
            drv.put("f_max", modelInfo.frequency + 1.5 * modelInfo.fc);
            drv.put("f_count", 1201);
            workDir.resolve("driver.json").writeString(toJson(drv));
            try (InputStream in = OpenEMSStrNTxSimulationPlan.class.getResourceAsStream("msl_driver.py")) {
                workDir.resolve("msl_driver.py").writeBytes(in.readAllBytes());
            }
            if (!OpenEMSProvisioner.ensurePythonDocker(rendererContext, id)) {
                throw new IllegalStateException("OpenEMS: port type msl needs Docker and the image " + OpenEMSProvisioner.PYTHON_DOCKER_IMAGE);
            }
            if (rendererContext != null) {
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] Running openEMS with its own MSL port in Docker (%s)", id, OpenEMSProvisioner.PYTHON_DOCKER_IMAGE));
            }
            NExec.ofSystem("docker", "run", "--rm",
                    "-v", workDir.toAbsolute().normalize().toString() + ":/simulations",
                    "-w", "/simulations",
                    OpenEMSProvisioner.PYTHON_DOCKER_IMAGE,
                    "python3", "msl_driver.py"
            ).directory(workDir).run();
            NTxMwSimulationUtils.keepSolverInputs(rendererContext, name, workDir);
            NPath out = workDir.resolve("s11.dat");
            if (!out.exists()) {
                throw new IllegalStateException("OpenEMS msl port: no s11.dat in " + workDir);
            }
            List<double[]> rows = new ArrayList<>();
            for (String line : out.readString().split("\n")) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] t = line.split("\\s+");
                rows.add(new double[]{Double.parseDouble(t[0]), Double.parseDouble(t[1]), Double.parseDouble(t[2]), Double.parseDouble(t[3]), Double.parseDouble(t[4])});
            }
            OpenEMSRunData d = new OpenEMSRunData();
            d.mslF = new double[rows.size()];
            d.mslS11 = new Complex[rows.size()];
            for (int k = 0; k < rows.size(); k++) {
                d.mslF[k] = rows.get(k)[0];
                d.mslS11[k] = Complex.of(rows.get(k)[1], rows.get(k)[2]);
            }
            if (rendererContext != null) {
                double[] r = rows.get(nearest(d.mslF, modelInfo.frequency));
                rendererContext.log(NMsg.ofC("[OpenEMS][%s] msl port: openEMS reads the feed line at %s ohm at %s GHz",
                        id, String.format(Locale.US, "%.2f%+.2fj", r[3], r[4]), String.format(Locale.US, "%.3f", r[0] / 1e9)));
            }
            return d;
        } catch (Exception ex) {
            throw new RuntimeException("OpenEMS simulation error: " + ex.getMessage(), ex);
        }
    }

    private static int nearest(double[] xs, double x) {
        int best = 0;
        for (int k = 1; k < xs.length; k++) {
            if (Math.abs(xs[k] - x) < Math.abs(xs[best] - x)) {
                best = k;
            }
        }
        return best;
    }

    /** linear interpolation on the msl port's frequency grid; outside it there is no excitation to read */
    private Complex mslS11(double freq) {
        OpenEMSRunData d = runSimulation();
        double[] f = d.mslF;
        if (freq < f[0] || freq > f[f.length - 1]) {
            throw new IllegalArgumentException(String.format(Locale.US, "OpenEMS msl port: %.4f GHz is outside the excited band %.4f-%.4f GHz (set fc)",
                    freq / 1e9, f[0] / 1e9, f[f.length - 1] / 1e9));
        }
        int k = Math.min(f.length - 2, Math.max(0, (int) ((freq - f[0]) / (f[1] - f[0]))));
        double t = (freq - f[k]) / (f[k + 1] - f[k]);
        return d.mslS11[k].mul(1 - t).plus(d.mslS11[k + 1].mul(t));
    }

    private static String toJson(Map<String, Object> m) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, Object> e : m.entrySet()) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append('"').append(e.getKey()).append("\": ").append(toJsonValue(e.getValue()));
        }
        return sb.append("}\n").toString();
    }

    private static String toJsonValue(Object v) {
        if (v instanceof String) {
            return "\"" + v + "\"";
        }
        if (v instanceof List) {
            StringBuilder sb = new StringBuilder("[");
            for (Object o : (List<?>) v) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(toJsonValue(o));
            }
            return sb.append("]").toString();
        }
        if (v instanceof Double) {
            return String.format(Locale.US, "%.10g", (Double) v);
        }
        return String.valueOf(v);
    }

    public Complex computeVf(double freq) {
        return OpenEMSUtils.computeVf(runSimulation(), freq);
    }

    public Complex computeIf(double freq) {
        return OpenEMSUtils.computeIf(runSimulation(), freq);
    }

    /**
     * Feed line read from the probes at one frequency: gamma per mm (e^{+jwt}, so Im > 0 for a
     * wave running towards the antenna), Z0, and the forward and backward waves A, B at the
     * centre probe.
     */
    private static class Line {
        Complex gamma;
        Complex z0;
        Complex a;
        Complex b;
    }

    /**
     * gamma from three equally spaced V probes, V(s) = A e^{-gs} + B e^{gs}, so
     * V1 + V3 = 2 V2 cosh(g dx). Alone it is noisy: near a voltage node V2 is small, and the
     * near field of the port and the patch leak into the probes, which can even give a negative alpha.
     */
    private static Complex rawGamma(OpenEMSRunData d, double freq) {
        Complex v1 = probeF(d, "port1_V1", freq);
        Complex v2 = probeF(d, "port1_V2", freq);
        Complex v3 = probeF(d, "port1_V3", freq);
        Complex g = v1.plus(v3).div(v2.mul(2.0)).acosh().div(d.layout.dx);
        return g.getImag() < 0 ? g.neg() : g;
    }

    /**
     * Smooths gamma over the excitation band: a quadratic in f for sqrt(eps_eff) and one alpha,
     * each sample weighted by |V2| / max(|V1|, |V3|) so that samples near a voltage node count less.
     * On the 100 mm antenna board this gives the same null as gamma from Kirschning-Jansen to within 40 MHz.
     */
    private void fitPropagation(OpenEMSRunData d) {
        int n = 60;
        double fLo = Math.max(0.1e9, modelInfo.frequency - 0.8 * modelInfo.fc);
        double fHi = modelInfo.frequency + 0.8 * modelInfo.fc;
        double[][] ata = new double[3][3];
        double[] atb = new double[3];
        double aSum = 0, wSum = 0;
        d.sqrtEpsMin = Double.MAX_VALUE;
        d.sqrtEpsMax = 0;
        for (int k = 0; k < n; k++) {
            double f = fLo + (fHi - fLo) * k / (n - 1);
            Complex g = rawGamma(d, f);
            double k0 = 2 * Math.PI * f / 299792458.0 * 1e-3;
            double se = g.getImag() / k0;
            double v1 = probeF(d, "port1_V1", f).absDouble();
            double v2 = probeF(d, "port1_V2", f).absDouble();
            double v3 = probeF(d, "port1_V3", f).absDouble();
            double w = v2 / Math.max(1e-30, Math.max(v1, v3));
            double fg = f / 1e9;
            double[] row = {1, fg, fg * fg};
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    ata[i][j] += w * w * row[i] * row[j];
                }
                atb[i] += w * w * row[i] * se;
            }
            aSum += w * g.getReal();
            wSum += w;
            d.sqrtEpsMin = Math.min(d.sqrtEpsMin, se);
            d.sqrtEpsMax = Math.max(d.sqrtEpsMax, se);
        }
        d.sqrtEpsFit = solve3(ata, atb);
        // a passive line cannot gain: a negative mean is probe noise
        d.alphaFit = Math.max(0, aSum / wSum);
    }

    private static double polyval(double[] c, double x) {
        return c[0] + c[1] * x + c[2] * x * x;
    }

    private static double[] solve3(double[][] a, double[] b) {
        double[][] m = new double[3][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(a[i], 0, m[i], 0, 3);
            m[i][3] = b[i];
        }
        for (int c = 0; c < 3; c++) {
            int p = c;
            for (int r = c + 1; r < 3; r++) {
                if (Math.abs(m[r][c]) > Math.abs(m[p][c])) {
                    p = r;
                }
            }
            double[] t = m[c];
            m[c] = m[p];
            m[p] = t;
            for (int r = 0; r < 3; r++) {
                if (r != c) {
                    double q = m[r][c] / m[c][c];
                    for (int k = c; k < 4; k++) {
                        m[r][k] -= q * m[c][k];
                    }
                }
            }
        }
        return new double[]{m[0][3] / m[0][0], m[1][3] / m[1][1], m[2][3] / m[2][2]};
    }

    private Line lineAt(double freq) {
        OpenEMSRunData d = runSimulation();
        double dx = d.layout.dx;
        Complex[] v = {probeF(d, "port1_V1", freq), probeF(d, "port1_V2", freq), probeF(d, "port1_V3", freq)};
        Complex i1 = probeF(d, "port1_I1", freq);
        Complex i2 = probeF(d, "port1_I2", freq);
        Line l = new Line();
        double k0 = 2 * Math.PI * freq / 299792458.0 * 1e-3;
        Complex g = Complex.of(d.alphaFit, k0 * polyval(d.sqrtEpsFit, freq / 1e9));
        l.gamma = g;
        // with gamma fixed, A and B are a least-squares fit to the three V probes at s = -dx, 0, dx
        Complex h00 = Complex.of(0), h01 = Complex.of(0), h11 = Complex.of(0), r0 = Complex.of(0), r1 = Complex.of(0);
        for (int k = 0; k < 3; k++) {
            double s = (k - 1) * dx;
            Complex m0 = g.mul(-s).exp();
            Complex m1 = g.mul(s).exp();
            h00 = h00.plus(m0.conj().mul(m0));
            h01 = h01.plus(m0.conj().mul(m1));
            h11 = h11.plus(m1.conj().mul(m1));
            r0 = r0.plus(m0.conj().mul(v[k]));
            r1 = r1.plus(m1.conj().mul(v[k]));
        }
        Complex det = h00.mul(h11).minus(h01.mul(h01.conj()));
        l.a = h11.mul(r0).minus(h01.mul(r1)).div(det);
        l.b = h00.mul(r1).minus(h01.conj().mul(r0)).div(det);
        // I(s) = (A e^{-gs} - B e^{gs}) / Z0 at s = -+dx/2
        Complex hp = g.mul(dx / 2.0).exp();
        Complex hm = g.mul(-dx / 2.0).exp();
        Complex z0a = l.a.mul(hp).minus(l.b.mul(hm)).div(i1);
        Complex z0b = l.a.mul(hm).minus(l.b.mul(hp)).div(i2);
        l.z0 = z0a.plus(z0b).div(2.0);
        return l;
    }

    private static Complex probeF(OpenEMSRunData d, String name, double freq) {
        double[][] p = d.probes.get(name);
        double dt = p[0].length > 1 ? p[0][1] - p[0][0] : d.dt;
        return OpenEMSUtils.fourierTransform(p[0], p[1], dt, freq);
    }

    private Complex deembeddedZin(double freq) {
        Line l = lineAt(freq);
        double s = runSimulation().layout.refDistance;
        Complex fwd = l.a.mul(l.gamma.mul(-s).exp());
        Complex bwd = l.b.mul(l.gamma.mul(s).exp());
        return l.z0.mul(fwd.plus(bwd)).div(fwd.minus(bwd));
    }

    public Complex computeZin(double freq) {
        if (runSimulation().mslF != null) {
            Complex s = mslS11(freq);
            return Complex.of(50.0).mul(Complex.ONE.plus(s)).div(Complex.ONE.minus(s));
        }
        if (runSimulation().layout != null) {
            return deembeddedZin(freq);
        }
        Complex vf = computeVf(freq);
        Complex ifVal = computeIf(freq);
        if (ifVal.isZero()) {
            return Complex.of(1e9, 0);
        }
        return vf.div(ifVal);
    }

    public Complex computeS11(double freq) {
        if (runSimulation().mslF != null) {
            return mslS11(freq);
        }
        if (runSimulation().layout != null) {
            Complex z = deembeddedZin(freq);
            return z.minus(Complex.of(50.0)).div(z.plus(50.0));
        }
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
