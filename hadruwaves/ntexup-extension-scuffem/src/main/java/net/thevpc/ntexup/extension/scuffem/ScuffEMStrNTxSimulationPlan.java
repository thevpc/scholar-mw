package net.thevpc.ntexup.extension.scuffem;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.ntexup.extension.scuffem.solvers.NTxScuffEMS11Solver;
import net.thevpc.ntexup.extension.scuffem.solvers.NTxScuffEMZinSolver;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class ScuffEMStrNTxSimulationPlan extends NTxSimulationPlanImpl {

    public ScuffEMModelInfo modelInfo;
    private boolean solved = false;
    private MicrostripCircuitModel circuitModel;

    private final TreeSet<Double> requestedFrequencies = new TreeSet<>();
    private final TreeMap<Double, Complex> s11Results = new TreeMap<>();
    private final TreeMap<Double, Complex> zinResults = new TreeMap<>();

    public static class RebuiltGeometry {
        public double feedW = 0.0;
        public double feedCenter = 0.0;
        public double lineLength = 0.0;
        public double portY = 0.0;
        public double portX1 = 0.0;
        public double portX2 = 0.0;
        public double h = 0.0;

        public boolean isResonator = false;
        public double resW = 0.0;
        public double resL = 0.0;
        public double feedLength = 0.0;
        public double insetDepth = 0.0;
        public int numElements = 1;
    }

    public ScuffEMStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
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
                return new NTxScuffEMS11Solver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxScuffEMZinSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    public void recordRequestedFrequencies(double[] freqs) {
        if (freqs != null) {
            for (double f : freqs) {
                requestedFrequencies.add(f);
            }
        }
    }

    public RebuiltGeometry rebuildGeometry() {
        RebuiltGeometry geom = new RebuiltGeometry();
        if (modelInfo == null) {
            return geom;
        }

        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.substrateBoxes) {
            double sh = b.height();
            if (sh > 0.0001) {
                geom.h = sh;
            }
        }

        ScuffEMModelInfo.ScuffEMBox src = modelInfo.sourceBoxes.isEmpty() ? null : modelInfo.sourceBoxes.get(0);
        if (src != null) {
            geom.feedW = src.width();
            geom.portY = src.yMin();
            geom.portX1 = src.xMin();
            geom.portX2 = src.xMax();
            geom.feedCenter = (src.xMin() + src.xMax()) / 2.0;
        }

        ScuffEMModelInfo.ScuffEMBox feedBox = null;
        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;

        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
            antXmin = Math.min(antXmin, b.xMin());
            antXmax = Math.max(antXmax, b.xMax());
            antYmin = Math.min(antYmin, b.yMin());
            antYmax = Math.max(antYmax, b.yMax());

            if (src != null && b.xMax() >= src.xMin() - 1e-6 && b.xMin() <= src.xMax() + 1e-6
                    && b.yMax() >= src.yMin() - 1e-6 && b.yMin() <= src.yMax() + 1e-6) {
                if (feedBox == null || b.length() > feedBox.length()) {
                    feedBox = b;
                }
            }
        }

        if (feedBox != null) {
            geom.feedW = feedBox.width();
            geom.portY = src != null ? src.yMin() : feedBox.yMin();
            geom.portX1 = feedBox.xMin();
            geom.portX2 = feedBox.xMax();
            geom.feedCenter = (feedBox.xMin() + feedBox.xMax()) / 2.0;
        } else if (geom.feedW <= 0) {
            geom.feedW = 3.1e-3;
            geom.portX1 = -geom.feedW / 2.0;
            geom.portX2 = geom.feedW / 2.0;
        }

        double totalLength = antYmax > antYmin ? (antYmax - geom.portY) : 30e-3;
        geom.lineLength = totalLength;

        java.util.List<ScuffEMModelInfo.ScuffEMBox> candidateBoxes = new java.util.ArrayList<>();
        for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
            if (b.isPatch) {
                candidateBoxes.add(b);
            }
        }
        if (candidateBoxes.isEmpty()) {
            double widthThreshold = 1.25 * geom.feedW;
            for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
                if (b.width() > widthThreshold && b.length() > 1.5 * geom.feedW) {
                    candidateBoxes.add(b);
                }
            }
        }

        if (!candidateBoxes.isEmpty()) {
            candidateBoxes.sort(java.util.Comparator.comparingDouble(ScuffEMModelInfo.ScuffEMBox::xMin));
            java.util.List<ScuffEMModelInfo.ScuffEMBox> clusters = new java.util.ArrayList<>();
            for (ScuffEMModelInfo.ScuffEMBox b : candidateBoxes) {
                if (clusters.isEmpty()) {
                    clusters.add(new ScuffEMModelInfo.ScuffEMBox(b.x1, b.y1, b.z1, b.x2, b.y2, b.z2, b.name, b.type));
                } else {
                    ScuffEMModelInfo.ScuffEMBox last = clusters.get(clusters.size() - 1);
                    if (b.xMin() <= last.xMax() + 4e-3) {
                        last.x1 = Math.min(last.x1, b.xMin());
                        last.x2 = Math.max(last.x2, b.xMax());
                        last.y1 = Math.min(last.y1, b.yMin());
                        last.y2 = Math.max(last.y2, b.yMax());
                    } else {
                        clusters.add(new ScuffEMModelInfo.ScuffEMBox(b.x1, b.y1, b.z1, b.x2, b.y2, b.z2, b.name, b.type));
                    }
                }
            }

            geom.isResonator = true;
            geom.numElements = clusters.size();
            geom.resW = clusters.get(0).width();
            geom.resL = clusters.get(0).length();
            geom.feedLength = Math.max(0.0, clusters.get(0).yMin() - geom.portY);

            if (feedBox != null && feedBox.yMax() > clusters.get(0).yMin()) {
                geom.insetDepth = Math.min(geom.resL, feedBox.yMax() - clusters.get(0).yMin());
            } else {
                for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
                    if (b != feedBox && b.width() < (geom.resW * 0.45) && b.yMin() <= clusters.get(0).yMin() + 1e-6) {
                        geom.insetDepth = Math.max(geom.insetDepth, b.length());
                    }
                }
            }
        } else {
            geom.isResonator = false;
            geom.numElements = 1;
        }

        return geom;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "SCUFFEM_BEM".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.meshResolution).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
            for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.antennaBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
            for (ScuffEMModelInfo.ScuffEMBox b : modelInfo.sourceBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    private String generateGmshGeo(RebuiltGeometry geom) {
        StringBuilder sb = new StringBuilder();
        sb.append("SetFactory(\"OpenCASCADE\");\n");
        sb.append("Mesh.MshFileVersion = 2.2;\n");
        double lc = modelInfo.meshResolution > 0 ? modelInfo.meshResolution : 2.5;
        sb.append(String.format(Locale.US, "lc = %.4f;\n", lc));

        List<ScuffEMModelInfo.ScuffEMBox> antBoxes = modelInfo.antennaBoxes;
        if (antBoxes.isEmpty()) {
            double w_mm = geom.feedW * 1000.0;
            double l_mm = geom.lineLength * 1000.0;
            double x_mm = geom.portX1 * 1000.0;
            double y_mm = geom.portY * 1000.0;
            sb.append(String.format(Locale.US, "Rectangle(1) = {%.4f, %.4f, 0, %.4f, %.4f};\n", x_mm, y_mm, w_mm, l_mm));
            sb.append("Physical Surface(\"Antenna\") = {1};\n");
        } else if (antBoxes.size() == 1) {
            ScuffEMModelInfo.ScuffEMBox b = antBoxes.get(0);
            double x_mm = b.xMin() * 1000.0;
            double y_mm = b.yMin() * 1000.0;
            double w_mm = b.width() * 1000.0;
            double l_mm = b.length() * 1000.0;
            sb.append(String.format(Locale.US, "Rectangle(1) = {%.4f, %.4f, 0, %.4f, %.4f};\n", x_mm, y_mm, w_mm, l_mm));
            sb.append("Physical Surface(\"Antenna\") = {1};\n");
        } else {
            for (int i = 0; i < antBoxes.size(); i++) {
                ScuffEMModelInfo.ScuffEMBox b = antBoxes.get(i);
                double x_mm = b.xMin() * 1000.0;
                double y_mm = b.yMin() * 1000.0;
                double w_mm = b.width() * 1000.0;
                double l_mm = b.length() * 1000.0;
                sb.append(String.format(Locale.US, "Rectangle(%d) = {%.4f, %.4f, 0, %.4f, %.4f};\n", i + 1, x_mm, y_mm, w_mm, l_mm));
            }
            sb.append("s() = BooleanUnion{ Surface{1}; Delete; }{ Surface{");
            for (int i = 1; i < antBoxes.size(); i++) {
                if (i > 1) sb.append(", ");
                sb.append(i + 1);
            }
            sb.append("}; Delete; };\n");
            sb.append("Physical Surface(\"Antenna\") = {s(0)};\n");
        }
        sb.append("Mesh.CharacteristicLengthMax = lc;\n");
        sb.append("Mesh.CharacteristicLengthMin = lc;\n");
        return sb.toString();
    }

    private String generatePythonScript(RebuiltGeometry geom, List<Double> freqs) {
        StringBuilder sb = new StringBuilder();
        sb.append("import scuff, math, sys\n\n");
        sb.append("Solver = scuff.scuffSolver()\n");
        sb.append("Solver.AddMetalTraceMesh(\"/sim/mesh.msh\")\n");

        double px1 = geom.portX1 * 1000.0;
        double px2 = geom.portX2 * 1000.0;
        double py = geom.portY * 1000.0;
        sb.append(String.format(Locale.US, "Solver.AddPort([%.6f, %.6f, 0, %.6f, %.6f, 0])\n", px1, py, px2, py));
        sb.append(String.format(Locale.US, "Solver.SetSubstratePermittivity(%.4f)\n", modelInfo.epsilonR));
        sb.append(String.format(Locale.US, "Solver.SetSubstrateThickness(%.6f)\n\n", geom.h * 1000.0));

        sb.append("freqs = [");
        for (int i = 0; i < freqs.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format(Locale.US, "%.6e", freqs.get(i)));
        }
        sb.append("]\n\n");

        sb.append("with open(\"/sim/results.dat\", \"w\") as f:\n");
        sb.append("    for freq in freqs:\n");
        sb.append("        f_ghz = freq / 1e9\n");
        sb.append("        omega = f_ghz * 2.0 * math.pi / 300.0\n");
        sb.append("        Solver.AssembleSystemMatrix(omega)\n");
        sb.append("        z_mat = Solver.GetZMatrix()\n");
        sb.append("        s_mat = Solver.Z2S(z_mat)\n");
        sb.append("        z11 = z_mat.GetEntry(0, 0)\n");
        sb.append("        s11 = s_mat.GetEntry(0, 0)\n");
        sb.append("        f.write(f\"{freq:.6e} {s11.real:.8e} {s11.imag:.8e} {z11.real:.8e} {z11.imag:.8e}\\n\")\n");
        sb.append("        f.flush()\n");

        return sb.toString();
    }

    public synchronized void ensureSolved() {
        if (solved) {
            return;
        }
        solved = true;
        if (modelInfo == null) {
            modelInfo = new ScuffEMModelInfo();
        }

        RebuiltGeometry geom = rebuildGeometry();
        String hash = computeHash();

        List<Double> simFreqs = selectSimulationFrequencies();

        NPath workDir = NPath.ofTempFolder("scuffem-sim-");
        workDir.mkdirs();

        boolean dockerOk = ScuffEMProvisioner.ensureDocker(modelInfo.dockerImage, rendererContext(), hash);
        if (dockerOk) {
            try {
                NPath geoFile = workDir.resolve("mesh.geo");
                geoFile.writeString(generateGmshGeo(geom));

                NPath pyFile = workDir.resolve("simulate.py");
                pyFile.writeString(generatePythonScript(geom, simFreqs));

                // 1. Run Gmsh
                int gmshCode = ScuffEMProvisioner.runInDocker(
                        modelInfo.dockerImage,
                        workDir,
                        "/sim",
                        Arrays.asList("gmsh", "-2", "/sim/mesh.geo", "-o", "/sim/mesh.msh"),
                        rendererContext(),
                        hash
                );

                // 2. Run SCUFF-EM python solver
                if (gmshCode == 0) {
                    ScuffEMProvisioner.runInDocker(
                            modelInfo.dockerImage,
                            workDir,
                            "/sim",
                            Arrays.asList("python3", "/sim/simulate.py"),
                            rendererContext(),
                            hash
                    );
                }

                // 3. Parse results.dat
                NPath resFile = workDir.resolve("results.dat");
                if (resFile.exists()) {
                    String resStr = new String(resFile.readBytes(), StandardCharsets.UTF_8);
                    for (String line : resStr.split("\n")) {
                        line = line.trim();
                        if (!line.isEmpty() && !line.startsWith("#")) {
                            String[] parts = line.split("\\s+");
                            if (parts.length >= 5) {
                                double f = Double.parseDouble(parts[0]);
                                double s11Re = Double.parseDouble(parts[1]);
                                double s11Im = Double.parseDouble(parts[2]);
                                double zRe = Double.parseDouble(parts[3]);
                                double zIm = Double.parseDouble(parts[4]);
                                Complex zin;
                                Complex s11;
                                if (geom.isResonator) {
                                    zin = Complex.of(zRe, zIm);
                                    s11 = Complex.of(s11Re, s11Im);
                                } else {
                                    zin = Complex.of(zRe + 50.0, zIm);
                                    Complex z0 = Complex.of(50.0);
                                    s11 = zin.minus(z0).div(zin.plus(z0));
                                }
                                s11Results.put(f, s11);
                                zinResults.put(f, zin);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                if (rendererContext() != null) {
                    rendererContext().log(NMsg.ofC("[SCUFF-EM][%s] Exception during simulation: %s", hash, ex.getMessage()));
                }
            }
        }

        if (s11Results.isEmpty()) {
            if (rendererContext() != null) {
                rendererContext().log(NMsg.ofC("[SCUFF-EM][%s] Using analytical BEM/cavity approximation fallback.", hash));
            }
            populateAnalyticalFallback(geom, simFreqs);
        }
    }

    private List<Double> selectSimulationFrequencies() {
        List<Double> list = new ArrayList<>();
        if (requestedFrequencies.isEmpty()) {
            list.add(modelInfo.frequency);
            return list;
        }

        if (requestedFrequencies.size() <= 12) {
            list.addAll(requestedFrequencies);
            return list;
        }

        // Downsample to at most 12 anchor frequencies across range
        List<Double> all = new ArrayList<>(requestedFrequencies);
        double fmin = all.get(0);
        double fmax = all.get(all.size() - 1);
        int nAnchors = 10;
        for (int i = 0; i < nAnchors; i++) {
            double f = fmin + (fmax - fmin) * (double) i / (double) (nAnchors - 1);
            list.add(f);
        }
        if (!list.contains(modelInfo.frequency)) {
            list.add(modelInfo.frequency);
            Collections.sort(list);
        }
        return list;
    }

    public synchronized MicrostripCircuitModel getCircuitModel() {
        if (circuitModel == null) {
            if (modelInfo == null) {
                modelInfo = new ScuffEMModelInfo();
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

    private void populateAnalyticalFallback(RebuiltGeometry geom, List<Double> freqs) {
        MicrostripCircuitModel model = getCircuitModel();
        double z0Val = modelInfo != null ? modelInfo.z0Ref : 50.0;
        Complex z0 = Complex.of(z0Val);
        for (double f : freqs) {
            MicrostripCircuitModel.ComplexNum z = model.computeZin(f);
            Complex zin = Complex.of(z.re, z.im);
            Complex s11 = zin.minus(z0).div(zin.plus(z0));
            s11Results.put(f, s11);
            zinResults.put(f, zin);
        }
    }

    public Complex computeS11(double freq) {
        ensureSolved();
        return interpolate(s11Results, freq);
    }

    public Complex computeZin(double freq) {
        ensureSolved();
        return interpolate(zinResults, freq);
    }

    private Complex interpolate(TreeMap<Double, Complex> map, double freq) {
        if (map.isEmpty()) {
            return Complex.ZERO;
        }
        if (map.containsKey(freq)) {
            return map.get(freq);
        }

        Map.Entry<Double, Complex> lower = map.floorEntry(freq);
        Map.Entry<Double, Complex> upper = map.ceilingEntry(freq);

        if (lower == null) return upper.getValue();
        if (upper == null) return lower.getValue();

        double f1 = lower.getKey();
        double f2 = upper.getKey();
        if (Math.abs(f2 - f1) < 1e-6) {
            return lower.getValue();
        }

        double t = (freq - f1) / (f2 - f1);
        Complex v1 = lower.getValue();
        Complex v2 = upper.getValue();

        double re = v1.getReal() + t * (v2.getReal() - v1.getReal());
        double im = v1.getImag() + t * (v2.getImag() - v1.getImag());
        return Complex.of(re, im);
    }
}
