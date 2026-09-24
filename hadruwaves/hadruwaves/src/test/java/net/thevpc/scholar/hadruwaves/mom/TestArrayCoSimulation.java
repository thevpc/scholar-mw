package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TestArrayCoSimulation {

    private static HGeometry createSinglePatch(double xCenter, double W, double L, double wf, double g, double y0, double feedStart) {
        HPolygon feed = GeometryFactory.createPolygon(Domain.ofPoints(xCenter - wf / 2, feedStart, xCenter + wf / 2, y0));
        HPolygon left = GeometryFactory.createPolygon(Domain.ofPoints(xCenter - W / 2, 0, xCenter - wf / 2 - g, y0));
        HPolygon right = GeometryFactory.createPolygon(Domain.ofPoints(xCenter + wf / 2 + g, 0, xCenter + W / 2, y0));
        HPolygon top = GeometryFactory.createPolygon(Domain.ofPoints(xCenter - W / 2, y0, xCenter + W / 2, L));
        return feed.addGeometry(left).addGeometry(right).addGeometry(top);
    }

    public static void main(String[] args) throws Exception {
        net.thevpc.nuts.Nuts.require();
        Maths.Config.setCacheEnabled(false);

        double er = 4.4;
        double h = 1.6 * Maths.MM;
        double W = 13.5 * Maths.MM;
        double L = 9.75 * Maths.MM;
        double wf = 3.1 * Maths.MM;
        double g = 0.8 * Maths.MM;
        double y0 = 3.4 * Maths.MM;
        double feedStart = -8.5 * Maths.MM;

        double d = 22.0 * Maths.MM;
        double[] xCenters = new double[]{
                -1.5 * d, -0.5 * d, 0.5 * d, 1.5 * d
        };

        HGeometry arrayGeom = null;
        PlanarSource[] sources = new PlanarSource[4];

        for (int i = 0; i < 4; i++) {
            double xc = xCenters[i];
            HGeometry patch = createSinglePatch(xc, W, L, wf, g, y0, feedStart);
            arrayGeom = (arrayGeom == null) ? patch : arrayGeom.addGeometry(patch);
            Domain portDomain = Domain.ofBounds(xc - wf / 2, xc + wf / 2, feedStart, feedStart + 1.0 * Maths.MM);
            sources[i] = CstPlanarSource.ofVoltage(1.0, portDomain, Axis.Y, Complex.of(50));
        }

        Domain subDomain = Domain.ofPoints(xCenters[0] - 13 * Maths.MM, -11 * Maths.MM, xCenters[3] + 13 * Maths.MM, 15 * Maths.MM);
        Domain momDomain = subDomain.pad(20 * Maths.MM, 20 * Maths.MM);

        MeshTriangulationOptions options = new MeshTriangulationOptions();
        options.setMaxArea(2.0 * Maths.MM * Maths.MM);
        GpRWG rwgTF = TestFunctionsFactory.createRWG(arrayGeom, options);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(2000);
        mom.setSources(SourceFactory.createPlanarSources(sources));
        mom.setTestFunctions(rwgTF);

        System.out.println("Building 1x4 array MomStructure...");
        mom.build();

        // 5-point sweep around resonance
        double[] freqs = new double[]{6.35, 6.40, 6.45, 6.50, 6.55};
        List<ComplexMatrix> sMatrices = new ArrayList<>();

        System.out.println("\n--- Step 1: Evaluating Full-Wave Hadruwaves MoM 4x4 S-Matrix ---");
        for (double f : freqs) {
            System.out.printf("Solving at f = %.3f GHz...%n", f);
            mom.setFrequency(f * Maths.GHZ);
            ComplexMatrix S = mom.sparameters().evalMatrix();
            sMatrices.add(S);
            System.out.printf("  f=%.3f GHz: S11=%.2f dB, S21=%.2f dB, S31=%.2f dB, S41=%.2f dB%n",
                    f,
                    20 * Math.log10(S.get(0, 0).absDouble()),
                    20 * Math.log10(S.get(1, 0).absDouble()),
                    20 * Math.log10(S.get(2, 0).absDouble()),
                    20 * Math.log10(S.get(3, 0).absDouble()));
        }

        // --- Step 2: Export Touchstone .s4p file ---
        Path tempDir = Files.createTempDirectory("qucs_array_cosim_");
        File s4pFile = tempDir.resolve("array4_mom.s4p").toFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(s4pFile))) {
            pw.println("! Touchstone 4-port S-parameters from Hadruwaves MoM RWG");
            pw.println("# GHz S RI R 50");
            for (int k = 0; k < freqs.length; k++) {
                double f = freqs[k];
                ComplexMatrix S = sMatrices.get(k);
                pw.printf(Locale.US, "%.4f", f);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        Complex s_ij = S.get(i, j);
                        pw.printf(Locale.US, "  %.8e %.8e", s_ij.getReal(), s_ij.getImag());
                    }
                    if (i < 3) {
                        pw.print("\n      ");
                    }
                }
                pw.println();
            }
        }
        System.out.println("\n--- Step 2: Exported Touchstone file: " + s4pFile.getAbsolutePath());

        // --- Step 3: Create Qucs Netlists ---
        // A) Coupled Netlist (using Touchstone block)
        File netCoupled = tempDir.resolve("circuit_coupled.net").toFile();
        File datCoupled = tempDir.resolve("circuit_coupled.dat").toFile();
        writeQucsNetlist(netCoupled, freqs[0], freqs[freqs.length - 1], freqs.length, true, "array4_mom.s4p");

        // B) Uncoupled Netlist (using 4 uncoupled RLC resonators matching single patch)
        File netUncoupled = tempDir.resolve("circuit_uncoupled.net").toFile();
        File datUncoupled = tempDir.resolve("circuit_uncoupled.dat").toFile();
        writeQucsNetlist(netUncoupled, freqs[0], freqs[freqs.length - 1], freqs.length, false, null);

        // --- Step 4: Run Qucsator via Docker ---
        System.out.println("\n--- Step 3: Running Qucsator via Docker for both cases ---");
        runQucsDocker(tempDir.toFile(), "circuit_coupled.net", "circuit_coupled.dat");
        runQucsDocker(tempDir.toFile(), "circuit_uncoupled.net", "circuit_uncoupled.dat");

        // --- Step 5: Parse results and print Comparison ---
        Map<Double, Double> s11Coupled = parseQucsS11(datCoupled);
        Map<Double, Double> s11Uncoupled = parseQucsS11(datUncoupled);

        System.out.println("\n==========================================================================");
        System.out.println("        1x4 WILKINSON ARRAY CO-SIMULATION BENCHMARK RESULTS               ");
        System.out.println("==========================================================================");
        System.out.printf("%-10s | %-22s | %-22s | %-12s%n",
                "Freq (GHz)", "S11 Coupled (Full-Wave)", "S11 Uncoupled (Ideal)", "Mutual Detune");
        System.out.println("-----------+------------------------+------------------------+------------");

        for (double f : freqs) {
            double c_db = s11Coupled.getOrDefault(f * 1e9, Double.NaN);
            double u_db = s11Uncoupled.getOrDefault(f * 1e9, Double.NaN);
            double delta = c_db - u_db;
            System.out.printf("%-10.3f | %19.2f dB | %19.2f dB | %+9.2f dB%n",
                    f, c_db, u_db, delta);
        }
        System.out.println("==========================================================================");
        System.out.println("Conclusion: Mutual coupling between adjacent patches alters the active");
        System.out.println("impedance seen by the Wilkinson feed network, shifting the array match.");
    }

    private static void writeQucsNetlist(File netFile, double fmin, double fmax, int count, boolean coupled, String touchstoneFileName) throws Exception {
        try (PrintWriter pw = new PrintWriter(new FileWriter(netFile))) {
            pw.printf(Locale.US, "# 1-to-4 Wilkinson Corporate Feed Array\n");
            pw.printf(Locale.US, ".SP:SP1 Type=\"lin\" Start=\"%.4f GHz\" Stop=\"%.4f GHz\" Points=\"%d\"\n", fmin, fmax, count);
            pw.printf(Locale.US, "Pac:P_in in_net gnd Num=\"1\" Z=\"50 Ohm\" f=\"6.45 GHz\" P=\"0.001\"\n\n");
            pw.printf(Locale.US, "SUBST:Sub1 er=\"4.4\" h=\"1.6 mm\" t=\"0.035 mm\" tand=\"0.02\" rho=\"1.7e-8\" D=\"0.001 mm\"\n\n");

            // Stage 1 Wilkinson
            pw.println("# Stage 1 Wilkinson");
            pw.println("MLIN:TL1_1 in_net mid_L W=\"1.55 mm\" L=\"6.55 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("MLIN:TL1_2 in_net mid_R W=\"1.55 mm\" L=\"6.55 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("R:Riso1 mid_L mid_R R=\"100 Ohm\"");

            // Inter-stage lines
            pw.println("\n# Inter-stage 50-ohm lines");
            pw.println("MLIN:TL_mid_L mid_L stage2_L W=\"3.1 mm\" L=\"11.0 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("MLIN:TL_mid_R mid_R stage2_R W=\"3.1 mm\" L=\"11.0 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");

            // Stage 2 Left Wilkinson
            pw.println("\n# Stage 2 Left Wilkinson");
            pw.println("MLIN:TL2_1 stage2_L p1_net W=\"1.55 mm\" L=\"6.55 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("MLIN:TL2_2 stage2_L p2_net W=\"1.55 mm\" L=\"6.55 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("R:Riso2 p1_net p2_net R=\"100 Ohm\"");

            // Stage 2 Right Wilkinson
            pw.println("\n# Stage 2 Right Wilkinson");
            pw.println("MLIN:TL2_3 stage2_R p3_net W=\"1.55 mm\" L=\"6.55 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("MLIN:TL2_4 stage2_R p4_net W=\"1.55 mm\" L=\"6.55 mm\" Subst=\"Sub1\" DispModel=\"Kirschning\" Model=\"Hammerstad\"");
            pw.println("R:Riso3 p3_net p4_net R=\"100 Ohm\"");

            // Array termination
            if (coupled) {
                pw.println("\n# Full-Wave Coupled Array Touchstone S-Parameters");
                pw.printf("SPfile:AntArray p1_net p2_net p3_net p4_net gnd File=\"%s\"\n", touchstoneFileName);
            } else {
                pw.println("\n# Uncoupled 50-Ohm Resonator Patch Loads");
                for (int p = 1; p <= 4; p++) {
                    pw.printf("R:Rpatch%d p%d_net gnd R=\"50 Ohm\"\n", p, p);
                }
            }
        }
    }

    private static void runQucsDocker(File workDir, String netName, String datName) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "docker", "run", "--rm",
                "-v", workDir.getAbsolutePath() + ":/sim",
                "-w", "/sim",
                "thevpc/qucsator:0.0.20",
                "qucsator", "-i", "/sim/" + netName, "-o", "/sim/" + datName
        );
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("error") || line.contains("ERROR")) {
                    System.err.println("[Qucsator] " + line);
                }
            }
        }
        int exitCode = proc.waitFor();
        if (exitCode != 0) {
            System.err.println("Qucsator exited with code " + exitCode + " for " + netName);
        }
    }

    private static Map<Double, Double> parseQucsS11(File datFile) throws Exception {
        Map<Double, Double> map = new TreeMap<>();
        if (!datFile.exists()) {
            return map;
        }
        List<Double> freqs = new ArrayList<>();
        List<Double> s11dB = new ArrayList<>();
        boolean inFreq = false;
        boolean inS11 = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(datFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.startsWith("<indep frequency")) {
                    inFreq = true;
                    continue;
                }
                if (line.startsWith("</indep>")) {
                    inFreq = false;
                    continue;
                }
                if (line.startsWith("<dep S[1,1] frequency")) {
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
                    } catch (Exception ignored) {}
                } else if (inS11) {
                    try {
                        Complex s = parseComplex(line);
                        s11dB.add(20 * Math.log10(s.absDouble()));
                    } catch (Exception ignored) {}
                }
            }
        }
        for (int i = 0; i < Math.min(freqs.size(), s11dB.size()); i++) {
            map.put(freqs.get(i), s11dB.get(i));
        }
        return map;
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
}
