package net.thevpc.ntexup.extension.qucs;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
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

    public static class RebuiltGeometry {
        public double h = 1.6e-3;
        public double thickness = 0.035e-3;
        public double feedW = 3.1e-3;
        public double portY = 0.0;
        public double lineLength = 30e-3;
        public boolean isResonator = false;
        public double resW = 38e-3;
        public double resL = 29.4e-3;
        public double feedLength = 15e-3;
        public double insetDepth = 0.0;
    }

    public RebuiltGeometry rebuildGeometry() {
        RebuiltGeometry geom = new RebuiltGeometry();
        if (modelInfo == null) {
            return geom;
        }
        for (QucsModelInfo.QucsBox sb : modelInfo.substrateBoxes) {
            double sh = sb.height();
            if (sh > 0.0001) {
                geom.h = sh;
            }
        }
        for (QucsModelInfo.QucsBox ab : modelInfo.antennaBoxes) {
            double st = ab.height();
            if (st > 1e-6) {
                geom.thickness = st;
            }
        }

        QucsModelInfo.QucsBox src = modelInfo.sourceBoxes.isEmpty() ? null : modelInfo.sourceBoxes.get(0);
        if (src != null) {
            geom.feedW = src.width();
            geom.portY = src.yMin();
        }

        QucsModelInfo.QucsBox feedBox = null;
        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;

        for (QucsModelInfo.QucsBox b : modelInfo.antennaBoxes) {
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
        } else if (geom.feedW <= 0) {
            geom.feedW = 3.1e-3;
        }

        double totalLength = antYmax > antYmin ? (antYmax - geom.portY) : 30e-3;
        geom.lineLength = totalLength;

        double widthThreshold = 1.25 * geom.feedW;
        double wideXmin = Double.MAX_VALUE, wideXmax = -Double.MAX_VALUE;
        double wideYmin = Double.MAX_VALUE, wideYmax = -Double.MAX_VALUE;
        boolean hasWideSection = false;

        for (QucsModelInfo.QucsBox b : modelInfo.antennaBoxes) {
            if (b.width() > widthThreshold) {
                hasWideSection = true;
                wideXmin = Math.min(wideXmin, b.xMin());
                wideXmax = Math.max(wideXmax, b.xMax());
                wideYmin = Math.min(wideYmin, b.yMin());
                wideYmax = Math.max(wideYmax, b.yMax());
            }
        }

        if (hasWideSection) {
            geom.isResonator = true;
            geom.resW = wideXmax - wideXmin;
            geom.resL = wideYmax - wideYmin;
            geom.feedLength = Math.max(0.0, wideYmin - geom.portY);

            if (feedBox != null && feedBox.yMax() > wideYmin) {
                geom.insetDepth = Math.min(geom.resL, feedBox.yMax() - wideYmin);
            } else {
                for (QucsModelInfo.QucsBox b : modelInfo.antennaBoxes) {
                    if (b != feedBox && b.width() < (geom.resW * 0.45) && b.yMin() <= wideYmin + 1e-6) {
                        geom.insetDepth = Math.max(geom.insetDepth, b.length());
                    }
                }
            }
        } else {
            geom.isResonator = false;
        }

        return geom;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "QucsCore".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.z0Ref).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.dispModel).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.model).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
            for (QucsModelInfo.QucsBox b : modelInfo.antennaBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
            for (QucsModelInfo.QucsBox b : modelInfo.sourceBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
        }
        return d.computeString();
    }

    public synchronized void ensureQucsSolved(double fmin, double fmax, int count) {
        if (qucsSolved && !s11Map.isEmpty()) {
            if (s11Map.firstKey() <= fmin && s11Map.lastKey() >= fmax) {
                return;
            }
        }
        qucsSolved = true;

        if (modelInfo == null) {
            modelInfo = new QucsModelInfo();
        }

        RebuiltGeometry geom = rebuildGeometry();
        String hash = computeHash();
        NPath workDir = NPath.ofTempFolder("qucs-sim-");
        workDir.mkdirs();

        NPath netFile = workDir.resolve("circuit.net");
        NPath datFile = workDir.resolve("circuit.dat");

        if (!datFile.exists() || datFile.contentLength() == 0) {
            String netContent = generateNetlist(geom, fmin, fmax, count);
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
            computeFallbackSweep(geom, fmin, fmax, count);
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

    private void computeFallbackSweep(RebuiltGeometry geom, double fmin, double fmax, int count) {
        double step = count > 1 ? (fmax - fmin) / (count - 1) : 0;
        double z0 = modelInfo != null ? modelInfo.z0Ref : 50.0;
        double er = modelInfo != null ? modelInfo.epsilonR : 4.4;

        if (geom.isResonator) {
            double uRes = geom.resW / geom.h;
            double epsEffRes = (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / uRes);
            double dLRes = 0.412 * geom.h * ((epsEffRes + 0.3) / (epsEffRes - 0.258)) * ((uRes + 0.264) / (uRes + 0.8));
            double dLNotch = geom.insetDepth > 0 ? geom.insetDepth * 0.135 : 0.0;
            double leff = geom.resL + 2.0 * dLRes + dLNotch;
            double c = 2.99792458e8;
            double patchFr = c / (2.0 * leff * Math.sqrt(epsEffRes));
            double patchRin = 50.0;
            double patchQ = 35.0;

            double uFeed = geom.feedW / geom.h;
            double epsFeed = (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / uFeed);
            double vpFeed = c / Math.sqrt(epsFeed);

            for (int i = 0; i < count; i++) {
                double f = fmin + i * step;
                double deltaF = (f - patchFr) / patchFr;
                Complex zPatch = Complex.of(patchRin).div(Complex.of(1.0, 2.0 * patchQ * deltaF));
                Complex zin = zPatch;
                if (geom.feedLength > 0) {
                    double beta = 2.0 * Math.PI * f / vpFeed;
                    double bl = beta * geom.feedLength;
                    double tanBl = Math.tan(bl);
                    Complex num = zPatch.plus(Complex.of(0, z0 * tanBl));
                    Complex den = Complex.of(1.0).plus(Complex.of(0, tanBl / z0).mul(zPatch));
                    zin = num.div(den);
                }
                Complex s11 = zin.minus(Complex.of(z0)).div(zin.plus(Complex.of(z0)));
                s11Map.put(f, s11);
                zinMap.put(f, zin);
            }
            return;
        }

        double u = geom.feedW / geom.h;
        double epsEff = (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
        double c = 2.99792458e8;
        double vp = c / Math.sqrt(epsEff);
        double lTot = geom.lineLength + 0.625e-3;

        for (int i = 0; i < count; i++) {
            double f = fmin + i * step;
            double beta = 2.0 * Math.PI * f / vp;
            double X = -z0 / Math.tan(beta * lTot);
            Complex zin = Complex.of(z0, X);
            Complex s11 = zin.minus(Complex.of(z0)).div(zin.plus(Complex.of(z0)));
            s11Map.put(f, s11);
            zinMap.put(f, zin);
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

    private String generateNetlist(RebuiltGeometry geom, double fmin, double fmax, int count) {
        double w = geom.feedW;
        double l = geom.lineLength;
        double h = geom.h;
        double t = geom.thickness;
        double er = modelInfo.epsilonR;
        double tand = modelInfo.lossTangent;
        double z0 = modelInfo.z0Ref;
        double rho = modelInfo.rho;
        double rough = modelInfo.roughness;
        String dispModel = modelInfo.dispModel != null ? modelInfo.dispModel : "Kirschning";
        String model = modelInfo.model != null ? modelInfo.model : "Hammerstad";

        if (geom.isResonator) {
            double wFeed = geom.feedW * 1e3;
            double lFeed = Math.max(1.0, geom.feedLength * 1e3);
            double uRes = geom.resW / geom.h;
            double epsEffRes = (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / uRes);
            double dLRes = 0.412 * geom.h * ((epsEffRes + 0.3) / (epsEffRes - 0.258)) * ((uRes + 0.264) / (uRes + 0.8));
            double dLNotch = geom.insetDepth > 0 ? geom.insetDepth * 0.135 : 0.0;
            double leff = geom.resL + 2.0 * dLRes + dLNotch;
            double c = 2.99792458e8;
            double patchFr = c / (2.0 * leff * Math.sqrt(epsEffRes));
            double patchRin = 50.0;
            double patchQ = 35.0;

            double w0 = 2.0 * Math.PI * patchFr;
            double C_val = patchQ / (w0 * patchRin);
            double L_val = 1.0 / (w0 * w0 * C_val);

            return String.format(Locale.US,
                    "# Qucs Netlist for Planar Resonator\n" +
                    ".SP:SP1 Type=\"lin\" Start=\"%.6eHz\" Stop=\"%.6eHz\" Points=\"%d\"\n" +
                    "Pac:P1 _net0 gnd Num=\"1\" Z=\"%.2fOhm\" P=\"0.001\" f=\"1e+09Hz\" Temp=\"26.85\"\n" +
                    "MLIN:Feed _net0 _net1 W=\"%.4f mm\" L=\"%.4f mm\" Subst=\"Sub1\" DispModel=\"%s\" Model=\"%s\"\n" +
                    "R:R1 _net1 gnd R=\"%.2fOhm\" Temp=\"26.85\"\n" +
                    "L:L1 _net1 gnd L=\"%.6eH\"\n" +
                    "C:C1 _net1 gnd C=\"%.6eF\"\n" +
                    "SUBST:Sub1 er=\"%.4f\" h=\"%.4f mm\" t=\"%.4f mm\" tand=\"%.4f\" rho=\"%.3e\" D=\"%.3e\"\n",
                    fmin, fmax, count,
                    z0,
                    wFeed, lFeed, dispModel, model,
                    patchRin,
                    L_val,
                    C_val,
                    er, h * 1e3, t * 1e3, tand, rho, rough
            );
        }

        return String.format(Locale.US,
                "# Qucs Netlist\n" +
                ".SP:SP1 Type=\"lin\" Start=\"%.6eHz\" Stop=\"%.6eHz\" Points=\"%d\"\n" +
                "Pac:P1 _net0 gnd Num=\"1\" Z=\"%.2fOhm\" P=\"0.001\" f=\"1e+09Hz\" Temp=\"26.85\"\n" +
                "R:Rrad _net0 _net1 R=\"%.2fOhm\" Temp=\"26.85\"\n" +
                "MLIN:Line1 _net1 _net2 W=\"%.4f mm\" L=\"%.4f mm\" Subst=\"Sub1\" DispModel=\"%s\" Model=\"%s\"\n" +
                "MOPEN:Open1 _net2 W=\"%.4f mm\" Subst=\"Sub1\" MSDispModel=\"%s\" MSModel=\"%s\" Model=\"Kirschning\"\n" +
                "SUBST:Sub1 er=\"%.4f\" h=\"%.4f mm\" t=\"%.4f mm\" tand=\"%.4f\" rho=\"%.3e\" D=\"%.3e\"\n",
                fmin, fmax, count,
                z0,
                z0,
                w * 1e3, l * 1e3, dispModel, model,
                w * 1e3, dispModel, model,
                er, h * 1e3, t * 1e3, tand, rho, rough
        );
    }
}
