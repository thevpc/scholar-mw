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

import net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GetDPStrNTxSimulationPlan extends NTxSimulationPlanImpl {
    public GetDPModelInfo modelInfo;
    private boolean femSolved = false;
    private MicrostripCircuitModel circuitModel;

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
        NTxMwSimulationUtils.addDigestSource(d, "GetDPFEM-lines".getBytes(StandardCharsets.UTF_8));
        if (modelInfo != null) {
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.frequency).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.epsilonR).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, String.valueOf(modelInfo.lossTangent).getBytes(StandardCharsets.UTF_8));
            NTxMwSimulationUtils.addDigestSource(d, (modelInfo.meshResolution + "," + modelInfo.meshDomainFactor).getBytes(StandardCharsets.UTF_8));
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

    /**
     * Solves the quasi-static cross-section of every conductor width and hands the
     * resulting Z0 and effective permittivity to the circuit model, in place of its
     * closed-form line formulas. The FEM sees cross-sections only: the patch is still
     * the cavity RLC, derived from the permittivity solved for its width.
     */
    public synchronized void ensureFemSolved() {
        if (femSolved) {
            return;
        }
        if (modelInfo == null) {
            modelInfo = new GetDPModelInfo();
        }
        MicrostripCircuitModel model = getCircuitModel();
        if (model.conductorWidths().isEmpty()) {
            throw new IllegalStateException("GetDP: the geometry has no conductor to solve");
        }
        String hash = computeHash();
        boolean dockerOk = GetDPProvisioner.ensureDocker(modelInfo.dockerImage, rendererContext(), hash);
        if (!dockerOk) {
            throw new IllegalStateException("GetDP: Docker image '" + modelInfo.dockerImage + "' is not available");
        }
        NPath workDir = NPath.ofTempFolder("getdp-sim-");
        Map<Double, MicrostripCircuitModel.LineParams> params = new HashMap<>();
        int index = 0;
        for (double width : model.conductorWidths()) {
            MicrostripCircuitModel.LineParams line = solveCrossSection(workDir.resolve("w" + index++), width, model.substrateH, hash);
            params.put(width, line);
            if (rendererContext() != null) {
                rendererContext().log(NMsg.ofC("[GetDP][%s] FEM w=%.3f mm: Z0=%.2f Ohm, eps_eff=%.4f (closed form Z0=%.2f Ohm, eps_eff=%.4f)",
                        hash, width * 1e3, line.z0, line.epsEff,
                        MicrostripCircuitModel.calcZ0(width, model.substrateH, modelInfo.epsilonR),
                        MicrostripCircuitModel.calcEpsEff(width, model.substrateH, modelInfo.epsilonR)));
            }
        }
        model.useLineParams(params);
        femSolved = true;
    }

    private MicrostripCircuitModel.LineParams solveCrossSection(NPath workDir, double width, double h, String hash) {
        workDir.mkdirs();
        workDir.resolve("mesh.geo").writeString(generateGmshGeo(width, h));
        workDir.resolve("fem.pro").writeString(generateGetDPPro(modelInfo.epsilonR, "energy.dat"));
        workDir.resolve("fem_air.pro").writeString(generateGetDPPro(1.0, "energy_air.dat"));

        int gmshCode = GetDPProvisioner.runInDocker(
                modelInfo.dockerImage,
                workDir,
                "/sim",
                Arrays.asList("gmsh", "-2", "-format", "msh2", "/sim/mesh.geo", "-o", "/sim/mesh.msh"),
                rendererContext(),
                hash
        );
        if (gmshCode != 0) {
            throw new IllegalStateException("GetDP: gmsh mesh generation failed with exit code " + gmshCode);
        }
        double substrateEnergy = solveEnergy(workDir, "fem.pro", "energy.dat", hash);
        double airEnergy = solveEnergy(workDir, "fem_air.pro", "energy_air.dat", hash);

        // Energy per unit length at 1 V gives the capacitance with and without the substrate
        double c = 2.0 * substrateEnergy;
        double c0 = 2.0 * airEnergy;
        double epsEff = c / c0;
        double z0 = 1.0 / (Maths.C * Math.sqrt(c * c0));
        if (!(epsEff >= 1.0 && epsEff <= modelInfo.epsilonR && z0 > 0)) {
            throw new IllegalStateException("GetDP: non-physical line at w=" + width + " m: eps_eff=" + epsEff + ", Z0=" + z0);
        }
        return new MicrostripCircuitModel.LineParams(z0, epsEff);
    }

    private double solveEnergy(NPath workDir, String proFile, String energyFile, String hash) {
        int code = GetDPProvisioner.runInDocker(
                modelInfo.dockerImage,
                workDir,
                "/sim",
                Arrays.asList("getdp", "/sim/" + proFile, "-msh", "/sim/mesh.msh", "-solve", "Analysis", "-pos", "Map"),
                rendererContext(),
                hash
        );
        if (code != 0) {
            throw new IllegalStateException("GetDP: " + proFile + " failed with exit code " + code);
        }
        NPath file = workDir.resolve(energyFile);
        if (!file.exists()) {
            throw new IllegalStateException("GetDP: did not produce " + energyFile);
        }
        String[] tokens = new String(file.readBytes(), StandardCharsets.UTF_8).trim().split("\\s+");
        return Double.parseDouble(tokens[tokens.length - 1]);
    }

    public synchronized MicrostripCircuitModel getCircuitModel() {
        if (circuitModel == null) {
            if (modelInfo == null) {
                modelInfo = new GetDPModelInfo();
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

    public Complex computeZin(double freq) {
        ensureFemSolved();
        MicrostripCircuitModel model = getCircuitModel();
        MicrostripCircuitModel.ComplexNum z = model.computeZin(freq);
        return Complex.of(z.re, z.im);
    }

    public Complex computeS11(double freq) {
        Complex zin = computeZin(freq);
        double z0Val = modelInfo != null ? modelInfo.z0Ref : 50.0;
        Complex z0 = Complex.of(z0Val);
        return zin.minus(z0).div(zin.plus(z0));
    }

    /**
     * The domain walls are natural boundaries, so the domain has to be large against the
     * line: the default 20 (w + h) stays within about 1% of a domain and mesh refined further.
     * The mesh is sized from the strip, since the field is singular at its edges.
     */
    private String generateGmshGeo(double width, double height) {
        double w = width * 1e3;
        double h = height * 1e3;
        double boxW = modelInfo.meshDomainFactor * (w + h);
        double airH = boxW / 2.0;
        double bulk = modelInfo.meshResolution * h / 2.0;
        double edge = bulk / 16.0;
        double far = boxW / 16.0;

        return String.format(Locale.US,
                "w = %.4f;\n" +
                "h = %.4f;\n" +
                "box_w = %.4f;\n" +
                "air_h = %.4f;\n" +
                "bulk = %.4f;\n" +
                "edge = %.4f;\n" +
                "far = %.4f;\n\n" +
                "x0 = -box_w/2;\n" +
                "x1 = -w/2;\n" +
                "x2 = w/2;\n" +
                "x3 = box_w/2;\n\n" +
                "y0 = -h;\n" +
                "y1 = 0;\n" +
                "y2 = air_h;\n\n" +
                "Point(1) = {x0, y0, 0, far};\n" +
                "Point(2) = {x3, y0, 0, far};\n" +
                "Point(3) = {x3, y1, 0, far};\n" +
                "Point(4) = {x2, y1, 0, edge};\n" +
                "Point(5) = {x1, y1, 0, edge};\n" +
                "Point(6) = {x0, y1, 0, far};\n" +
                "Point(7) = {x3, y2, 0, far};\n" +
                "Point(8) = {x0, y2, 0, far};\n\n" +
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
                "Physical Curve(4) = {4};\n\n" +   // Strip
                "Field[1] = Distance;\n" +
                "Field[1].PointsList = {4, 5};\n" +
                "Field[2] = Threshold;\n" +
                "Field[2].InField = 1;\n" +
                "Field[2].SizeMin = edge;\n" +
                "Field[2].SizeMax = bulk;\n" +
                "Field[2].DistMin = 0;\n" +
                "Field[2].DistMax = h;\n" +
                "Field[3] = Distance;\n" +
                "Field[3].CurvesList = {4};\n" +
                "Field[3].NumPointsPerCurve = 400;\n" +
                "Field[4] = Threshold;\n" +
                "Field[4].InField = 3;\n" +
                "Field[4].SizeMin = bulk;\n" +
                "Field[4].SizeMax = far;\n" +
                "Field[4].DistMin = 2*h;\n" +
                "Field[4].DistMax = box_w/2;\n" +
                "Field[5] = Min;\n" +
                "Field[5].FieldsList = {2, 4};\n" +
                "Background Field = 5;\n" +
                "Mesh.MeshSizeFromPoints = 0;\n" +
                "Mesh.MeshSizeExtendFromBoundary = 0;\n" +
                "Mesh.MeshSizeFromCurvature = 0;\n",
                w, h, boxW, airH, bulk, edge, far
        );
    }

    private String generateGetDPPro(double er, String energyFile) {
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
                er, energyFile
        );
    }
}
