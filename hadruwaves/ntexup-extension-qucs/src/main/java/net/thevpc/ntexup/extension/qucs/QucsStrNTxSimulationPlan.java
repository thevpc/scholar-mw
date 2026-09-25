package net.thevpc.ntexup.extension.qucs;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.ntexup.extension.qucs.solvers.NTxQucsS11Solver;
import net.thevpc.ntexup.extension.qucs.solvers.NTxQucsZinSolver;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class QucsStrNTxSimulationPlan extends NTxSimulationPlanImpl {
    public QucsModelInfo modelInfo;
    private final NavigableMap<Double, Complex> s11Map = new TreeMap<>();
    private final NavigableMap<Double, Complex> zinMap = new TreeMap<>();
    private boolean qucsSolved = false;
    private MicrostripCircuitModel circuitModel;

    public QucsStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
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
                return new NTxQucsS11Solver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxQucsZinSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    public synchronized MicrostripCircuitModel getCircuitModel() {
        if (circuitModel == null) {
            if (modelInfo == null) {
                modelInfo = new QucsModelInfo();
            }
            circuitModel = MicrostripCircuitModel.parse(
                    modelInfo.sceneNode,
                    modelInfo.resolutionContext,
                    modelInfo.epsilonR,
                    modelInfo.lossTangent,
                    modelInfo.z0Ref
            );
        }
        return circuitModel;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "QucsSimulationPlan".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.z0Ref).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    public synchronized void ensureQucsSolved(double fmin, double fmax, int count) {
        if (qucsSolved) {
            return;
        }
        qucsSolved = true;

        if (modelInfo == null) {
            modelInfo = new QucsModelInfo();
        }

        MicrostripCircuitModel model = getCircuitModel();
        String hash = computeHash();
        NPath workDir = NPath.ofTempFolder("qucs-sim-");
        workDir.mkdirs();

        NPath netFile = workDir.resolve("circuit.net");
        NPath datFile = workDir.resolve("circuit.dat");

        if (!datFile.exists() || datFile.contentLength() == 0) {
            String netContent = model.generateQucsNetlist(fmin, fmax, count);
            netFile.writeString(netContent);

            QucsProvisioner.ensureDocker(modelInfo.dockerImage, rendererContext(), hash);

            QucsProvisioner.runInDocker(
                    modelInfo.dockerImage,
                    workDir,
                    "/sim",
                    Arrays.asList("qucsator", "-i", "/sim/circuit.net", "-o", "/sim/circuit.dat"),
                    rendererContext(),
                    hash
            );
        }

        if (datFile.exists() && datFile.contentLength() > 0) {
            parseQucsDataset(datFile);
        } else {
            if (rendererContext() != null) {
                rendererContext().log(NMsg.ofC("[Qucs][%s] Dataset not found. Falling back to analytical model.", hash));
            }
            computeFallbackSweep(fmin, fmax, count);
        }
    }

    private void parseQucsDataset(NPath datFile) {
        try {
            String content = new String(datFile.readBytes(), StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(new StringReader(content));
            String line;
            List<Double> freqs = new ArrayList<>();
            List<Complex> s11List = new ArrayList<>();

            boolean inFreq = false;
            boolean inS11 = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (line.startsWith("<indep frequency")) {
                    inFreq = true;
                    continue;
                }
                if (line.startsWith("</indep>")) {
                    inFreq = false;
                    continue;
                }
                if (line.startsWith("<dep S[1,1]")) {
                    inS11 = true;
                    continue;
                }
                if (line.startsWith("</dep>")) {
                    inS11 = false;
                    continue;
                }

                if (inFreq) {
                    try {
                        freqs.add(Double.parseDouble(line));
                    } catch (Exception ignored) {
                    }
                } else if (inS11) {
                    try {
                        s11List.add(parseComplex(line));
                    } catch (Exception ignored) {
                    }
                }
            }

            double z0 = modelInfo != null ? modelInfo.z0Ref : 50.0;
            Complex cZ0 = Complex.of(z0);

            for (int i = 0; i < Math.min(freqs.size(), s11List.size()); i++) {
                double f = freqs.get(i);
                Complex s11 = s11List.get(i);
                s11Map.put(f, s11);

                Complex one = Complex.ONE;
                Complex zin = cZ0.mul(one.plus(s11)).div(one.minus(s11));
                zinMap.put(f, zin);
            }
        } catch (Exception ex) {
            if (rendererContext() != null) {
                rendererContext().log(NMsg.ofC("[Qucs] Error parsing dataset: %s", ex.getMessage()));
            }
        }
    }

    public static Complex parseComplex(String s) {
        s = s.trim();
        int jIdx = s.indexOf('j');
        if (jIdx < 0) {
            return Complex.of(Double.parseDouble(s));
        }
        String reStr = s.substring(0, jIdx).trim();
        char sign = '+';
        if (reStr.endsWith("+")) {
            sign = '+';
            reStr = reStr.substring(0, reStr.length() - 1);
        } else if (reStr.endsWith("-")) {
            sign = '-';
            reStr = reStr.substring(0, reStr.length() - 1);
        }
        double re = Double.parseDouble(reStr);
        double im = Double.parseDouble(s.substring(jIdx + 1).trim());
        if (sign == '-') {
            im = -im;
        }
        return Complex.of(re, im);
    }

    private void computeFallbackSweep(double fmin, double fmax, int count) {
        double step = count > 1 ? (fmax - fmin) / (count - 1) : 0;
        MicrostripCircuitModel model = getCircuitModel();

        for (int i = 0; i < count; i++) {
            double f = fmin + i * step;
            MicrostripCircuitModel.ComplexNum s11C = model.computeS11(f);
            MicrostripCircuitModel.ComplexNum zinC = model.computeZin(f);
            s11Map.put(f, Complex.of(s11C.re, s11C.im));
            zinMap.put(f, Complex.of(zinC.re, zinC.im));
        }
    }

    public Complex computeS11(double freq) {
        double fCenter = (modelInfo != null && modelInfo.frequency > 0) ? modelInfo.frequency : freq;
        double fmin = fCenter * 0.8;
        double fmax = fCenter * 1.2;
        ensureQucsSolved(fmin, fmax, 41);
        return interpolate(s11Map, freq);
    }

    public Complex computeZin(double freq) {
        double fCenter = (modelInfo != null && modelInfo.frequency > 0) ? modelInfo.frequency : freq;
        double fmin = fCenter * 0.8;
        double fmax = fCenter * 1.2;
        ensureQucsSolved(fmin, fmax, 41);
        return interpolate(zinMap, freq);
    }

    private Complex interpolate(NavigableMap<Double, Complex> map, double freq) {
        if (map.isEmpty()) {
            return Complex.ZERO;
        }
        Map.Entry<Double, Complex> exact = map.floorEntry(freq);
        if (exact != null && Math.abs(exact.getKey() - freq) < 1e-3) {
            return exact.getValue();
        }
        Map.Entry<Double, Complex> lower = map.floorEntry(freq);
        Map.Entry<Double, Complex> higher = map.ceilingEntry(freq);
        if (lower == null && higher != null) {
            return higher.getValue();
        }
        if (higher == null && lower != null) {
            return lower.getValue();
        }
        if (lower != null && higher != null) {
            double f1 = lower.getKey();
            double f2 = higher.getKey();
            if (Math.abs(f2 - f1) < 1e-6) {
                return lower.getValue();
            }
            double t = (freq - f1) / (f2 - f1);
            Complex c1 = lower.getValue();
            Complex c2 = higher.getValue();
            return Complex.of(
                    c1.realdbl() + t * (c2.realdbl() - c1.realdbl()),
                    c1.imagdbl() + t * (c2.imagdbl() - c1.imagdbl())
            );
        }
        return Complex.ZERO;
    }
}
