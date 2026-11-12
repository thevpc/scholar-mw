package net.thevpc.ntexup.extension.palace;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.mwsimulator.NTxDockerProvisioner;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.ntexup.extension.mwsimulator.NTxSweep;
import net.thevpc.ntexup.extension.palace.solvers.NTxPalaceComplexNTxSolver;
import net.thevpc.ntexup.extension.palace.solvers.NTxPalaceS11Solver;
import net.thevpc.ntexup.extension.palace.solvers.NTxPalaceZinSolver;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Palace (awslabs, MFEM) full-wave FEM, driven from the ntx. The extension writes the gmsh model
 * (geometry, materials' regions, mesh sizes) and Palace's JSON config; S11 is Palace's own, from
 * its wave port (a 2D mode solve on the port face) de-embedded by its Offset to the reference
 * plane, read back from postpro/port-S.csv.
 */
public class PalaceStrNTxSimulationPlan extends NTxSimulationPlanImpl {

    public PalaceModelInfo modelInfo;
    private double[] sF;
    private Complex[] sS11;

    public PalaceStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
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
                return new NTxPalaceS11Solver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxPalaceZinSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "Palace-wave".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, generateGeo().getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, generateConfig(sweepBand()).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.processes).getBytes(StandardCharsets.UTF_8));
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    /**
     * The feed, read from the source box: it runs along x (alongX) or y, towards the antenna
     * (sign), and the reference plane is the source box's outer edge.
     */
    private static class Feed {
        boolean alongX;
        double sign;
        double reference;
        double w1, w2;
    }

    private Feed feed() {
        PalaceModelInfo.PalaceBox src = modelInfo.sourceBoxes.get(0);
        Feed f = new Feed();
        f.alongX = (src.x2 - src.x1) < (src.y2 - src.y1);
        double u1 = f.alongX ? src.x1 : src.y1;
        double u2 = f.alongX ? src.x2 : src.y2;
        double lo = Double.MAX_VALUE, hi = -Double.MAX_VALUE;
        for (PalaceModelInfo.PalaceBox b : modelInfo.antennaBoxes) {
            lo = Math.min(lo, f.alongX ? b.x1 : b.y1);
            hi = Math.max(hi, f.alongX ? b.x2 : b.y2);
        }
        f.sign = (lo + hi) / 2.0 >= (u1 + u2) / 2.0 ? 1.0 : -1.0;
        f.reference = f.sign > 0 ? u1 : u2;
        f.w1 = f.alongX ? src.y1 : src.x1;
        f.w2 = f.alongX ? src.y2 : src.x2;
        return f;
    }

    /**
     * gmsh model, mm. The substrate and ground run to the domain's side faces (infinite ground:
     * the ground is the PEC bottom face); the metal is zero-thickness sheets on the substrate; the
     * feed is extended from the reference plane to the port face, where Palace's wave port sits.
     * Physical groups: 1 air, 2 substrate, 3 ground, 4 metal, 5 port, 6 absorbing boundary.
     */
    String generateGeo() {
        PalaceModelInfo m = modelInfo;
        PalaceModelInfo.PalaceBox sub = m.substrateBoxes.get(0);
        double zg = sub.z1, zt = sub.z2, h = zt - zg;
        Feed f = feed();
        double mx1 = Double.MAX_VALUE, my1 = Double.MAX_VALUE, mx2 = -Double.MAX_VALUE, my2 = -Double.MAX_VALUE;
        List<PalaceModelInfo.PalaceBox> metal = new ArrayList<>(m.antennaBoxes);
        metal.addAll(m.sourceBoxes);
        for (PalaceModelInfo.PalaceBox b : metal) {
            mx1 = Math.min(mx1, b.x1);
            my1 = Math.min(my1, b.y1);
            mx2 = Math.max(mx2, b.x2);
            my2 = Math.max(my2, b.y2);
        }
        double x0 = mx1 - m.padding, x1 = mx2 + m.padding, y0 = my1 - m.padding, y1 = my2 + m.padding;
        double portFace = f.reference - f.sign * m.portExtension;
        if (f.alongX) {
            if (f.sign > 0) x0 = portFace; else x1 = portFace;
        } else {
            if (f.sign > 0) y0 = portFace; else y1 = portFace;
        }
        // the feed extension, from the port face to the reference plane
        double e1 = Math.min(portFace, f.reference), e2 = Math.max(portFace, f.reference);
        List<double[]> rects = new ArrayList<>();
        for (PalaceModelInfo.PalaceBox b : m.antennaBoxes) {
            rects.add(new double[]{b.x1, b.y1, b.x2, b.y2});
        }
        rects.add(f.alongX ? new double[]{e1, f.w1, e2, f.w2} : new double[]{f.w1, e1, f.w2, e2});

        StringBuilder sb = new StringBuilder();
        sb.append("// written by the ntexup Palace extension from the ntx scene (mm)\n");
        sb.append("SetFactory(\"OpenCASCADE\");\nMesh.MshFileVersion = 2.2;\n");
        sb.append(String.format(Locale.US, "x0 = %.6f; x1 = %.6f; y0 = %.6f; y1 = %.6f; zg = %.6f; zt = %.6f; top = %.6f; e = 1e-3;\n",
                x0, x1, y0, y1, zg, zt, zt + m.airHeight));
        sb.append("Box(1) = {x0, y0, zg, x1 - x0, y1 - y0, zt - zg};\n");
        sb.append("Box(2) = {x0, y0, zt, x1 - x0, y1 - y0, top - zt};\n");
        int tag = 101;
        StringBuilder surfs = new StringBuilder();
        for (double[] r : rects) {
            sb.append(String.format(Locale.US, "Rectangle(%d) = {%.6f, %.6f, zt, %.6f, %.6f};\n", tag, r[0], r[1], r[2] - r[0], r[3] - r[1]));
            surfs.append(surfs.length() == 0 ? "" : ", ").append(tag++);
        }
        sb.append("BooleanFragments{ Volume{1, 2}; Delete; }{ Surface{").append(surfs).append("}; Delete; }\n");
        sb.append("vSub() = Volume In BoundingBox{x0 - e, y0 - e, zg - e, x1 + e, y1 + e, zt + e};\n");
        sb.append("vAir() = Volume In BoundingBox{x0 - e, y0 - e, zt - e, x1 + e, y1 + e, top + e};\n");
        // each metal sheet's fragments, picked by its own rectangle
        sb.append("sMetal() = {};\n");
        for (double[] r : rects) {
            sb.append(String.format(Locale.US, "sMetal() += Surface In BoundingBox{%.6f - e, %.6f - e, zt - e, %.6f + e, %.6f + e, zt + e};\n", r[0], r[1], r[2], r[3]));
        }
        sb.append("sMetal() = Unique(sMetal());\n");
        sb.append("sGround() = Surface In BoundingBox{x0 - e, y0 - e, zg - e, x1 + e, y1 + e, zg + e};\n");
        String face;
        if (f.alongX) {
            face = f.sign > 0 ? "x0 - e, y0 - e, zg - e, x0 + e, y1 + e, top + e" : "x1 - e, y0 - e, zg - e, x1 + e, y1 + e, top + e";
        } else {
            face = f.sign > 0 ? "x0 - e, y0 - e, zg - e, x1 + e, y0 + e, top + e" : "x0 - e, y1 - e, zg - e, x1 + e, y1 + e, top + e";
        }
        sb.append("sPort() = Surface In BoundingBox{").append(face).append("};\n");
        sb.append("sAbc() = CombinedBoundary{ Volume{vSub(), vAir()}; };\nsAbc() -= sGround();\nsAbc() -= sPort();\n");
        sb.append("Physical Volume(\"Air\", 1) = vAir();\nPhysical Volume(\"Substrate\", 2) = vSub();\n");
        sb.append("Physical Surface(\"Ground\", 3) = sGround();\nPhysical Surface(\"Metal\", 4) = sMetal();\n");
        sb.append("Physical Surface(\"Port\", 5) = sPort();\nPhysical Surface(\"Abc\", 6) = sAbc();\n");
        sb.append(String.format(Locale.US, "Field[1] = Distance; Field[1].SurfacesList = {sMetal()};\n"
                        + "Field[2] = Threshold; Field[2].InField = 1; Field[2].SizeMin = %.6f; Field[2].SizeMax = %.6f;\n"
                        + "Field[2].DistMin = 0.5; Field[2].DistMax = 20;\n"
                        + "Field[3] = Box; Field[3].VIn = %.6f; Field[3].VOut = %.6f;\n"
                        + "Field[3].XMin = x0; Field[3].XMax = x1; Field[3].YMin = y0; Field[3].YMax = y1; Field[3].ZMin = zg; Field[3].ZMax = zt;\n"
                        + "Field[4] = Min; Field[4].FieldsList = {2, 3};\nBackground Field = 4;\n"
                        + "Mesh.MeshSizeFromPoints = 0; Mesh.MeshSizeExtendFromBoundary = 0; Mesh.MeshSizeFromCurvature = 0;\n",
                // at least one element through the substrate; Field[2] already refines it near the
                // metal. h/2 over the whole board tripled the mesh (0.89M elements, 56 GB on Sheen).
                m.meshMetal, m.meshAir, Math.min(m.meshSubstrate, h), m.meshAir));
        return sb.toString();
    }

    /** {from, to, step} in Hz, covering every result's frequencies */
    private double[] sweepBand() {
        double lo = Double.MAX_VALUE, hi = -Double.MAX_VALUE, step = Double.MAX_VALUE;
        for (NTxSolverRun item : items) {
            NTxSweep s = item instanceof NTxPalaceComplexNTxSolver ? ((NTxPalaceComplexNTxSolver) item).sweep() : null;
            if (s == null) {
                continue;
            }
            double[] v = s.doubleValues();
            for (int k = 0; k < v.length; k++) {
                lo = Math.min(lo, v[k]);
                hi = Math.max(hi, v[k]);
                if (k > 0 && v[k] > v[k - 1]) {
                    step = Math.min(step, v[k] - v[k - 1]);
                }
            }
        }
        if (lo > hi) {
            lo = hi = modelInfo.frequency;
        }
        if (step == Double.MAX_VALUE) {
            step = Math.max(1e6, (hi - lo) / 50);
        }
        return new double[]{lo, hi, step};
    }

    String generateConfig(double[] band) {
        PalaceModelInfo m = modelInfo;
        return String.format(Locale.US, "{\n"
                        + "  \"Problem\": { \"Type\": \"Driven\", \"Verbose\": 2, \"Output\": \"postpro\" },\n"
                        + "  \"Model\": { \"Mesh\": \"model.msh\", \"L0\": 1.0e-3 },\n"
                        + "  \"Domains\": {\n"
                        + "    \"Materials\": [\n"
                        + "      { \"Attributes\": [1], \"Permeability\": 1.0, \"Permittivity\": 1.0, \"LossTan\": 0.0 },\n"
                        + "      { \"Attributes\": [2], \"Permeability\": 1.0, \"Permittivity\": %.6g, \"LossTan\": %.6g }\n"
                        + "    ]\n"
                        + "  },\n"
                        + "  \"Boundaries\": {\n"
                        + "    \"PEC\": { \"Attributes\": [3, 4] },\n"
                        + "    \"Absorbing\": { \"Attributes\": [6], \"Order\": 1 },\n"
                        + "    \"WavePort\": [\n"
                        + "      { \"Index\": 1, \"Attributes\": [5], \"Mode\": %d, \"Excitation\": true, \"Offset\": %.6f }\n"
                        + "    ]\n"
                        + "  },\n"
                        + "  \"Solver\": {\n"
                        + "    \"Order\": %d,\n"
                        + "    \"Device\": \"CPU\",\n"
                        + "    \"Driven\": {\n"
                        + "      \"Samples\": [ { \"Type\": \"Linear\", \"MinFreq\": %.9g, \"MaxFreq\": %.9g, \"FreqStep\": %.9g } ],\n"
                        + "      \"AdaptiveTol\": %.3g\n"
                        + "    },\n"
                        + "    \"Linear\": { \"Type\": \"%s\", \"KSPType\": \"GMRES\", \"Tol\": 1.0e-8, \"MaxIts\": 400 }\n"
                        + "  }\n"
                        + "}\n",
                m.epsilonR, m.lossTangent, m.portMode, m.portExtension, m.order,
                band[0] / 1e9, band[1] / 1e9, band[2] / 1e9, m.adaptiveTol, m.linearSolver);
    }

    public synchronized void ensureSolved() {
        if (sF != null) {
            return;
        }
        String hash = computeHash();
        if (!PalaceProvisioner.ensureDocker(rendererContext(), hash)) {
            throw new IllegalStateException("Palace: needs Docker and the images " + PalaceProvisioner.GMSH_DOCKER_IMAGE + ", " + PalaceProvisioner.PALACE_DOCKER_IMAGE);
        }
        NPath workDir = NPath.ofTempFolder("palace-sim-");
        workDir.resolve("model.geo").writeString(generateGeo());
        workDir.resolve("palace.json").writeString(generateConfig(sweepBand()));
        // created here, not by the container: a root-owned postpro/ could not be deleted with the temp folder
        workDir.resolve("postpro").mkdirs();
        log("[Palace][%s] meshing with gmsh (%s)", hash, PalaceProvisioner.GMSH_DOCKER_IMAGE);
        int code = NTxDockerProvisioner.runInDocker(PalaceProvisioner.GMSH_DOCKER_IMAGE, workDir, "/sim",
                Arrays.asList("gmsh", "-3", "model.geo", "-o", "model.msh"), rendererContext(), "Palace][" + hash);
        if (code != 0 || !workDir.resolve("model.msh").exists()) {
            throw new IllegalStateException("Palace: gmsh failed (exit " + code + ") in " + workDir);
        }
        log("[Palace][%s] solving with %s processes (%s)", hash, modelInfo.processes, PalaceProvisioner.PALACE_DOCKER_IMAGE);
        code = NTxDockerProvisioner.runInDocker(PalaceProvisioner.PALACE_DOCKER_IMAGE, workDir, "/sim",
                Arrays.asList("palace", "-np", String.valueOf(modelInfo.processes), "palace.json"), rendererContext(), "Palace][" + hash);
        NPath csv = workDir.resolve("postpro").resolve("port-S.csv");
        NTxMwSimulationUtils.keepSolverInputs(rendererContext(), name, workDir);
        if (code != 0 || !csv.exists()) {
            throw new IllegalStateException("Palace failed (exit " + code + ") in " + workDir);
        }
        List<double[]> rows = new ArrayList<>();
        for (String line : csv.readString().split("\n")) {
            String[] t = line.trim().split("\\s*,\\s*");
            if (t.length < 3) {
                continue;
            }
            try {
                rows.add(new double[]{Double.parseDouble(t[0]) * 1e9, Double.parseDouble(t[1]), Double.parseDouble(t[2])});
            } catch (NumberFormatException header) {
                // the column titles
            }
        }
        if (rows.isEmpty()) {
            throw new IllegalStateException("Palace: no S-parameters in " + csv);
        }
        double[] f = new double[rows.size()];
        Complex[] s = new Complex[rows.size()];
        for (int k = 0; k < rows.size(); k++) {
            double mag = Math.pow(10, rows.get(k)[1] / 20.0);
            double arg = Math.toRadians(rows.get(k)[2]);
            f[k] = rows.get(k)[0];
            s[k] = Complex.of(mag * Math.cos(arg), mag * Math.sin(arg));
        }
        sS11 = s;
        sF = f;
    }

    private void log(String format, Object... args) {
        if (rendererContext() != null) {
            rendererContext().log(NMsg.ofC(format, args));
        }
    }

    public Complex computeS11(double freq) {
        ensureSolved();
        if (sF.length == 1) {
            return sS11[0];
        }
        double tol = 1e-6 * freq;
        if (freq < sF[0] - tol || freq > sF[sF.length - 1] + tol) {
            throw new IllegalArgumentException(String.format(Locale.US, "Palace: %.4f GHz is outside the solved band %.4f-%.4f GHz",
                    freq / 1e9, sF[0] / 1e9, sF[sF.length - 1] / 1e9));
        }
        int k = 0;
        while (k < sF.length - 2 && sF[k + 1] < freq) {
            k++;
        }
        double t = Math.max(0, Math.min(1, (freq - sF[k]) / (sF[k + 1] - sF[k])));
        return sS11[k].mul(1 - t).plus(sS11[k + 1].mul(t));
    }

    /** referred to 50 ohm; Palace's S11 is normalized to the port mode, of about 50 ohm here */
    public Complex computeZin(double freq) {
        Complex s = computeS11(freq);
        return Complex.of(50.0).mul(Complex.ONE.plus(s)).div(Complex.ONE.minus(s));
    }
}
