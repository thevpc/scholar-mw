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

    @Override
    public String computeHash() {
        NDigest d = NDigest.of();
        NTxMwSimulationUtils.addDigestSource(d, "GetDPFEM".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.width).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.stubLength).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.height).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.meshResolution).getBytes(StandardCharsets.UTF_8));
            if (modelInfo.geometryId != null) {
                NTxMwSimulationUtils.addDigestSource(d, modelInfo.geometryId.getBytes(StandardCharsets.UTF_8));
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
                computeApproximation();
                return;
            }

            NPath geoFile = workDir.resolve("mesh.geo");
            geoFile.writeString(generateGmshGeo());

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

                double u = modelInfo.width / modelInfo.height;
                double dL = 0.412 * modelInfo.height * ((epsEffFem + 0.3) / (epsEffFem - 0.258)) * ((u + 0.264) / (u + 0.8));
                lTot = modelInfo.stubLength + dL;

                if (rendererContext() != null) {
                    rendererContext().log(NMsg.ofC("[GetDP][%s] FEM Parameters: eps_eff=%.4f, Z0=%.2f Ohm, v_p=%.3e m/s, dL=%.3f mm",
                            hash, epsEffFem, z0Fem, vpFem, dL * 1e3));
                }
            } catch (Exception ex) {
                if (rendererContext() != null) {
                    rendererContext().log(NMsg.ofC("[GetDP][%s] Error parsing FEM energy results: %s. Using approximation.", hash, ex.getMessage()));
                }
                computeApproximation();
            }
        } else {
            computeApproximation();
        }
    }

    private void computeApproximation() {
        double u = modelInfo.width / modelInfo.height;
        epsEffFem = (modelInfo.epsilonR + 1.0) / 2.0 + ((modelInfo.epsilonR - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
        z0Fem = 48.20;
        vpFem = Maths.C / Math.sqrt(epsEffFem);
        double dL = 0.412 * modelInfo.height * ((epsEffFem + 0.3) / (epsEffFem - 0.258)) * ((u + 0.264) / (u + 0.8));
        lTot = modelInfo.stubLength + dL;
    }

    public Complex computeZin(double freq) {
        ensureFemSolved();
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

    private String generateGmshGeo() {
        double w = modelInfo.width * 1e3;
        double h = modelInfo.height * 1e3;
        double boxW = Math.max(40.0, w * 12.0);
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
