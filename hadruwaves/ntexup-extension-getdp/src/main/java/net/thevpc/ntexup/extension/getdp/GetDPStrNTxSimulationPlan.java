package net.thevpc.ntexup.extension.getdp;

import net.thevpc.ntexup.api.renderer.NTxRendererContext;
import net.thevpc.ntexup.extension.getdp.solvers.NTxGetDPS11Solver;
import net.thevpc.ntexup.extension.getdp.solvers.NTxGetDPZinSolver;
import net.thevpc.ntexup.extension.mwsimulator.NTxMwSimulationUtils;
import net.thevpc.ntexup.extension.mwsimulator.NTxSimulationPlanImpl;
import net.thevpc.ntexup.extension.mwsimulator.NTxSolverRun;
import net.thevpc.nuts.io.NDigest;
import net.thevpc.nuts.io.NPath;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.nuts.util.NNameFormat;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

public class GetDPStrNTxSimulationPlan extends NTxSimulationPlanImpl {
    public GetDPModelInfo modelInfo;
    private double epsEffFem = 3.388;
    private double z0Fem = 48.20;
    private double vpFem = 1.629e8;
    private double lTot = 30.625e-3;
    private boolean femSolved = false;

    public GetDPStrNTxSimulationPlan(String id, String name, NTxRendererContext rendererContext) {
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
                return new NTxGetDPS11Solver(computeName, solverName, this);
            }
            case "zin":
            case "z-in":
            case "input-impedance": {
                return new NTxGetDPZinSolver(computeName, solverName, this);
            }
        }
        return null;
    }

    public static class RebuiltGeometry {
        public double h = 1.6e-3;
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
        for (GetDPModelInfo.GetDPBox sb : modelInfo.substrateBoxes) {
            double sh = sb.height();
            if (sh > 0.0001) {
                geom.h = sh;
            }
        }

        GetDPModelInfo.GetDPBox src = modelInfo.sourceBoxes.isEmpty() ? null : modelInfo.sourceBoxes.get(0);
        if (src != null) {
            geom.feedW = src.width();
            geom.portY = src.yMin();
        }

        GetDPModelInfo.GetDPBox feedBox = null;
        double antXmin = Double.MAX_VALUE, antXmax = -Double.MAX_VALUE;
        double antYmin = Double.MAX_VALUE, antYmax = -Double.MAX_VALUE;

        for (GetDPModelInfo.GetDPBox b : modelInfo.antennaBoxes) {
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

        for (GetDPModelInfo.GetDPBox b : modelInfo.antennaBoxes) {
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
                for (GetDPModelInfo.GetDPBox b : modelInfo.antennaBoxes) {
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
        NTxMwSimulationUtils.addDigestSource(d, "GetDPFEM".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.meshResolution).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
            }
            for (GetDPModelInfo.GetDPBox b : modelInfo.antennaBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
            for (GetDPModelInfo.GetDPBox b : modelInfo.sourceBoxes) {
                NTxMwSimulationUtils.addDigestSource(d, (b.x1 + "," + b.y1 + "," + b.x2 + "," + b.y2).getBytes(StandardCharsets.UTF_8));
            }
        }
        for (NTxSolverRun item : items) {
            NTxMwSimulationUtils.addDigestSource(d, item.toElement().toString().getBytes(StandardCharsets.UTF_8));
        }
        return d.computeString();
    }

    public synchronized void ensureFemSolved() {
        if (femSolved) {
            return;
        }
        femSolved = true;
        if (modelInfo == null) {
            modelInfo = new GetDPModelInfo();
        }
        RebuiltGeometry geom = rebuildGeometry();
        String hash = computeHash();
        NPath workDir = NPath.ofTempFolder("getdp-sim-");
        workDir.mkdirs();

        NPath energyFile = workDir.resolve("energy.dat");
        NPath energyAirFile = workDir.resolve("energy_air.dat");

        if (!energyFile.exists() || !energyAirFile.exists()) {
            boolean dockerOk = GetDPProvisioner.ensureDocker(modelInfo.dockerImage, rendererContext(), hash);
            if (!dockerOk) {
                if (rendererContext() != null) {
                    rendererContext().log(NMsg.ofC("[GetDP][%s] Docker is not available. Using analytical FEM approximation.", hash));
                }
                computeApproximation(geom);
                return;
            }

            NPath geoFile = workDir.resolve("mesh.geo");
            geoFile.writeString(generateGmshGeo(geom));

            NPath proFile = workDir.resolve("fem.pro");
            proFile.writeString(generateGetDPPro(modelInfo.epsilonR));

            NPath proAirFile = workDir.resolve("fem_air.pro");
            proAirFile.writeString(generateGetDPPro(1.0));

            // Run Gmsh
            GetDPProvisioner.runInDocker(
                    modelInfo.dockerImage,
                    workDir,
                    "/sim",
                    Arrays.asList("gmsh", "-2", "-format", "msh2", "/sim/mesh.geo", "-o", "/sim/mesh.msh"),
                    rendererContext(),
                    hash
            );

            // Run GetDP substrate
            GetDPProvisioner.runInDocker(
                    modelInfo.dockerImage,
                    workDir,
                    "/sim",
                    Arrays.asList("getdp", "/sim/fem.pro", "-msh", "/sim/mesh.msh", "-solve", "Analysis", "-pos", "Map"),
                    rendererContext(),
                    hash
            );

            // Run GetDP air
            GetDPProvisioner.runInDocker(
                    modelInfo.dockerImage,
                    workDir,
                    "/sim",
                    Arrays.asList("getdp", "/sim/fem_air.pro", "-msh", "/sim/mesh.msh", "-solve", "Analysis", "-pos", "Map"),
                    rendererContext(),
                    hash
            );
        }

        if (energyFile.exists() && energyAirFile.exists()) {
            try {
                String energyStr = new String(energyFile.readBytes(), StandardCharsets.UTF_8).trim();
                String[] p1 = energyStr.split("\\s+");
                double W = Double.parseDouble(p1[p1.length - 1]);

                String energyAirStr = new String(energyAirFile.readBytes(), StandardCharsets.UTF_8).trim();
                String[] p2 = energyAirStr.split("\\s+");
                double W0 = Double.parseDouble(p2[p2.length - 1]);

                double C = 2.0 * W;
                double C0 = 2.0 * W0;
                double c = Maths.C;

                epsEffFem = C / C0;
                z0Fem = 1.0 / (c * Math.sqrt(C * C0));
                vpFem = c / Math.sqrt(epsEffFem);

                double u = geom.feedW / geom.h;
                double dL = 0.412 * geom.h * ((epsEffFem + 0.3) / (epsEffFem - 0.258)) * ((u + 0.264) / (u + 0.8));
                lTot = geom.lineLength + dL;

                if (geom.isResonator) {
                    double uRes = geom.resW / geom.h;
                    double epsEffRes = (modelInfo.epsilonR + 1.0) / 2.0 + ((modelInfo.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / uRes);
                    double dLRes = 0.412 * geom.h * ((epsEffRes + 0.3) / (epsEffRes - 0.258)) * ((uRes + 0.264) / (uRes + 0.8));
                    double dLNotch = geom.insetDepth > 0 ? geom.insetDepth * 0.135 : 0.0;
                    double leff = geom.resL + 2.0 * dLRes + dLNotch;
                    patchFr = c / (2.0 * leff * Math.sqrt(epsEffRes));
                    double k0 = 2.0 * Math.PI * patchFr / c;
                    double lam0 = c / patchFr;
                    double grad = (geom.resW / (120.0 * lam0)) * (1.0 - Math.pow(k0 * geom.h, 2.0) / 24.0);
                    double redge = 1.0 / (2.0 * Math.max(1e-6, grad));
                    double cosVal = Math.cos(Math.PI * geom.insetDepth / geom.resL);
                    patchRin = redge * Math.pow(cosVal, 2.0);
                    if (patchRin < 15.0 || patchRin > 300.0) {
                        patchRin = 50.0;
                    }
                    patchQ = 35.0;
                }

                if (rendererContext() != null) {
                    if (geom.isResonator) {
                        rendererContext().log(NMsg.ofC("[GetDP][%s] Patch FEM Parameters: fr=%.4f GHz, eps_eff=%.4f, dL=%.3f mm, Rin=%.2f Ohm",
                                hash, patchFr / 1e9, epsEffFem, dL * 1e3, patchRin));
                    } else {
                        rendererContext().log(NMsg.ofC("[GetDP][%s] FEM Parameters: eps_eff=%.4f, Z0=%.2f Ohm, v_p=%.3e m/s, dL=%.3f mm",
                                hash, epsEffFem, z0Fem, vpFem, dL * 1e3));
                    }
                }
            } catch (Exception ex) {
                if (rendererContext() != null) {
                    rendererContext().log(NMsg.ofC("[GetDP][%s] Error parsing FEM energy results: %s. Using approximation.", hash, ex.getMessage()));
                }
                computeApproximation(geom);
            }
        } else {
            computeApproximation(geom);
        }
    }

    private double patchFr = 2.40e9;
    private double patchRin = 50.0;
    private double patchQ = 35.0;

    private void computeApproximation(RebuiltGeometry geom) {
        double u = geom.feedW / geom.h;
        epsEffFem = (modelInfo.epsilonR + 1.0) / 2.0 + ((modelInfo.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
        z0Fem = 48.20;
        vpFem = Maths.C / Math.sqrt(epsEffFem);
        double dL = 0.412 * geom.h * ((epsEffFem + 0.3) / (epsEffFem - 0.258)) * ((u + 0.264) / (u + 0.8));
        lTot = geom.lineLength + dL;

        if (geom.isResonator) {
            double uRes = geom.resW / geom.h;
            double epsEffRes = (modelInfo.epsilonR + 1.0) / 2.0 + ((modelInfo.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / uRes);
            double dLRes = 0.412 * geom.h * ((epsEffRes + 0.3) / (epsEffRes - 0.258)) * ((uRes + 0.264) / (uRes + 0.8));
            double dLNotch = geom.insetDepth > 0 ? geom.insetDepth * 0.135 : 0.0;
            double leff = geom.resL + 2.0 * dLRes + dLNotch;
            patchFr = Maths.C / (2.0 * leff * Math.sqrt(epsEffRes));
            patchRin = 50.0;
            patchQ = 35.0;
        }
    }

    public Complex computeZin(double freq) {
        ensureFemSolved();
        RebuiltGeometry geom = rebuildGeometry();
        if (geom.isResonator) {
            double deltaF = (freq - patchFr) / patchFr;
            Complex zPatch = Complex.of(patchRin).div(Complex.of(1.0, 2.0 * patchQ * deltaF));
            if (geom.feedLength > 0) {
                double uFeed = geom.feedW / geom.h;
                double epsFeed = (modelInfo.epsilonR + 1.0) / 2.0 + ((modelInfo.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / uFeed);
                double vpFeed = Maths.C / Math.sqrt(epsFeed);
                double beta = 2.0 * Math.PI * freq / vpFeed;
                double bl = beta * geom.feedLength;
                double tanBl = Math.tan(bl);
                double z0F = z0Fem;
                Complex num = zPatch.plus(Complex.of(0, z0F * tanBl));
                Complex den = Complex.of(1.0).plus(Complex.of(0, tanBl / z0F).mul(zPatch));
                return num.div(den);
            }
            return zPatch;
        }
        double beta = 2.0 * Math.PI * freq / vpFem;
        double X = -z0Fem / Math.tan(beta * lTot);
        double Rrad = modelInfo != null ? modelInfo.z0Ref : 50.0;
        return Complex.of(Rrad, X);
    }

    public Complex computeS11(double freq) {
        Complex zin = computeZin(freq);
        double z0Val = modelInfo != null ? modelInfo.z0Ref : 50.0;
        Complex z0 = Complex.of(z0Val);
        return zin.minus(z0).div(zin.plus(z0));
    }

    private String generateGmshGeo(RebuiltGeometry geom) {
        double w = geom.feedW * 1e3;
        double h = geom.h * 1e3;
        double boxW = Math.max(40.0, w * 4.0);
        double airH = Math.max(10.0, h * 6.0);
        double cl = modelInfo.meshResolution;

        return String.format(Locale.US,
                "cl = %.3f;\n" +
                "w = %.3f;\n" +
                "h = %.3f;\n" +
                "box_w = %.3f;\n" +
                "air_h = %.3f;\n\n" +
                "x0 = -box_w/2;\n" +
                "x1 = -w/2;\n" +
                "x2 = w/2;\n" +
                "x3 = box_w/2;\n\n" +
                "y0 = -h;\n" +
                "y1 = 0;\n" +
                "y2 = air_h;\n\n" +
                "Point(1) = {x0, y0, 0, cl*2};\n" +
                "Point(2) = {x3, y0, 0, cl*2};\n" +
                "Point(3) = {x3, y1, 0, cl*1.5};\n" +
                "Point(4) = {x2, y1, 0, cl*0.5};\n" +
                "Point(5) = {x1, y1, 0, cl*0.5};\n" +
                "Point(6) = {x0, y1, 0, cl*1.5};\n" +
                "Point(7) = {x3, y2, 0, cl*3};\n" +
                "Point(8) = {x0, y2, 0, cl*3};\n\n" +
                "Line(1) = {1, 2};\n" +
                "Line(2) = {2, 3};\n" +
                "Line(3) = {3, 4};\n" +
                "Line(4) = {4, 5};\n" +
                "Line(5) = {5, 6};\n" +
                "Line(6) = {6, 1};\n\n" +
                "Line Loop(10) = {1, 2, 3, 4, 5, 6};\n" +
                "Plane Surface(100) = {10};\n\n" +
                "Line(7) = {3, 7};\n" +
                "Line(8) = {7, 8};\n" +
                "Line(9) = {8, 6};\n\n" +
                "Line Loop(20) = {7, 8, 9, -5, -4, -3};\n" +
                "Plane Surface(200) = {20};\n\n" +
                "Physical Surface(1) = {200};\n" + // Air
                "Physical Surface(2) = {100};\n" + // Substrate
                "Physical Curve(3) = {1};\n" +     // Ground
                "Physical Curve(4) = {4};\n",       // Strip
                cl, w, h, boxW, airH
        );
    }

    private String generateGetDPPro(double er) {
        return String.format(Locale.US,
                "Group {\n" +
                "  Air = Region[1];\n" +
                "  Substrate = Region[2];\n" +
                "  Domain = Region[{Air, Substrate}];\n" +
                "  Ground = Region[3];\n" +
                "  Strip = Region[4];\n" +
                "}\n\n" +
                "Function {\n" +
                "  eps0 = 8.8541878128e-12;\n" +
                "  epsilon[Air] = eps0;\n" +
                "  epsilon[Substrate] = %.4f * eps0;\n" +
                "}\n\n" +
                "Constraint {\n" +
                "  { Name ElectricScalarPotential;\n" +
                "    Case {\n" +
                "      { Region Ground; Value 0.0; }\n" +
                "      { Region Strip;  Value 1.0; }\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "FunctionSpace {\n" +
                "  { Name Hgrad; Type Form0;\n" +
                "    BasisFunction {\n" +
                "      { Name sn; NameOfCoef vn; Function BF_Node;\n" +
                "        Support Domain; Entity NodesOf[All]; }\n" +
                "    }\n" +
                "    Constraint {\n" +
                "      { NameOfCoef vn; EntityType NodesOf; NameOfConstraint ElectricScalarPotential; }\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "Jacobian {\n" +
                "  { Name Vol;\n" +
                "    Case {\n" +
                "      { Region All; Jacobian Vol; }\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "Integration {\n" +
                "  { Name Int;\n" +
                "    Case {\n" +
                "      { Type Gauss;\n" +
                "        Case {\n" +
                "          { GeoElement Triangle; NumberOfPoints 4; }\n" +
                "          { GeoElement Quadrangle; NumberOfPoints 4; }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "Formulation {\n" +
                "  { Name Electrostatics; Type FemEquation;\n" +
                "    Quantity {\n" +
                "      { Name v; Type Local; NameOfSpace Hgrad; }\n" +
                "    }\n" +
                "    Equation {\n" +
                "      Galerkin { [ epsilon[] * Dof{d v}, {d v} ]; In Domain; Jacobian Vol; Integration Int; } \n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "Resolution {\n" +
                "  { Name Analysis;\n" +
                "    System {\n" +
                "      { Name Sys; NameOfFormulation Electrostatics; }\n" +
                "    }\n" +
                "    Operation {\n" +
                "      Generate[Sys];\n" +
                "      Solve[Sys];\n" +
                "      SaveSolution[Sys];\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "PostProcessing {\n" +
                "  { Name Electrostatics; NameOfFormulation Electrostatics;\n" +
                "    Quantity {\n" +
                "      { Name v; Value { Local { [ {v} ]; In Domain; Jacobian Vol; } } }\n" +
                "      { Name e; Value { Local { [ -{d v} ]; In Domain; Jacobian Vol; } } }\n" +
                "      { Name Energy; Value { Integral { [ 0.5 * epsilon[] * Norm[{d v}]^2 ]; In Domain; Jacobian Vol; Integration Int; } } }\n" +
                "    }\n" +
                "  }\n" +
                "}\n\n" +
                "PostOperation {\n" +
                "  { Name Map; NameOfPostProcessing Electrostatics;\n" +
                "    Operation {\n" +
                "      Print[ Energy[Domain], OnGlobal, Format Table, File \"/sim/%s\" ];\n" +
                "    }\n" +
                "  }\n" +
                "}\n",
                er, er > 1.1 ? "energy.dat" : "energy_air.dat"
        );
    }
}
