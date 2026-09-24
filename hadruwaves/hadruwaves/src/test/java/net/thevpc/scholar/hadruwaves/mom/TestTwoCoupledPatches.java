package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;

public class TestTwoCoupledPatches {

    private static HGeometry createSinglePatch(double xCenter, double W, double L, double wf, double g, double y0, double feedStart) {
        HPolygon feed = GeometryFactory.createPolygon(Domain.ofPoints(xCenter - wf / 2, feedStart, xCenter + wf / 2, y0));
        HPolygon left = GeometryFactory.createPolygon(Domain.ofPoints(xCenter - W / 2, 0, xCenter - wf / 2 - g, y0));
        HPolygon right = GeometryFactory.createPolygon(Domain.ofPoints(xCenter + wf / 2 + g, 0, xCenter + W / 2, y0));
        HPolygon top = GeometryFactory.createPolygon(Domain.ofPoints(xCenter - W / 2, y0, xCenter + W / 2, L));
        return feed.addGeometry(left).addGeometry(right).addGeometry(top);
    }

    public static void main(String[] args) {
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

        // Separation between elements d = 22 mm (~0.5 lambda0 at 6.8 GHz)
        double d = 22.0 * Maths.MM;
        double x1 = -d / 2;
        double x2 = d / 2;

        HGeometry patch1 = createSinglePatch(x1, W, L, wf, g, y0, feedStart);
        HGeometry patch2 = createSinglePatch(x2, W, L, wf, g, y0, feedStart);
        HGeometry arrayGeom = patch1.addGeometry(patch2);

        Domain port1Domain = Domain.ofBounds(x1 - wf / 2, x1 + wf / 2, feedStart, feedStart + 1.0 * Maths.MM);
        Domain port2Domain = Domain.ofBounds(x2 - wf / 2, x2 + wf / 2, feedStart, feedStart + 1.0 * Maths.MM);

        // Simulation domain enclosing both patches with 20 mm margin
        Domain subDomain = Domain.ofPoints(x1 - 13 * Maths.MM, -11 * Maths.MM, x2 + 13 * Maths.MM, 15 * Maths.MM);
        Domain momDomain = subDomain.pad(20 * Maths.MM, 20 * Maths.MM);

        MeshTriangulationOptions options = new MeshTriangulationOptions();
        options.setMaxArea(1.5 * Maths.MM * Maths.MM);
        GpRWG rwgTF = TestFunctionsFactory.createRWG(arrayGeom, options);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(2000);

        CstPlanarSource src1 = CstPlanarSource.ofVoltage(1.0, port1Domain, Axis.Y, Complex.of(50));
        CstPlanarSource src2 = CstPlanarSource.ofVoltage(1.0, port2Domain, Axis.Y, Complex.of(50));
        mom.setSources(SourceFactory.createPlanarSources(src1, src2));
        mom.setTestFunctions(rwgTF);
        mom.setFrequency(6.45 * Maths.GHZ);

        System.out.println("Building 2-element array MomStructure...");
        mom.build();

        System.out.println("Evaluating Matrix B (should be N x 2)...");
        ComplexMatrix matB = mom.matrixB().evalMatrix();
        System.out.println("Matrix B dimensions: " + matB.getRowCount() + " x " + matB.getColumnCount());

        System.out.println("\nEvaluating 2x2 Input Impedance Matrix Z...");
        ComplexMatrix matZ = mom.inputImpedance().evalMatrix();
        System.out.println("Matrix Z dimensions: " + matZ.getRowCount() + " x " + matZ.getColumnCount());
        System.out.printf("  Z11: %s%n", matZ.get(0, 0));
        System.out.printf("  Z12: %s%n", matZ.get(0, 1));
        System.out.printf("  Z21: %s%n", matZ.get(1, 0));
        System.out.printf("  Z22: %s%n", matZ.get(1, 1));

        System.out.println("\nEvaluating 2x2 S-Parameters Matrix S...");
        ComplexMatrix matS = mom.sparameters().evalMatrix();
        System.out.println("Matrix S dimensions: " + matS.getRowCount() + " x " + matS.getColumnCount());
        double s11_db = 20 * Math.log10(matS.get(0, 0).absDouble());
        double s12_db = 20 * Math.log10(matS.get(0, 1).absDouble());
        double s21_db = 20 * Math.log10(matS.get(1, 0).absDouble());
        double s22_db = 20 * Math.log10(matS.get(1, 1).absDouble());

        System.out.printf("  S11: %s (%.2f dB)%n", matS.get(0, 0), s11_db);
        System.out.printf("  S12: %s (%.2f dB)%n", matS.get(0, 1), s12_db);
        System.out.printf("  S21: %s (%.2f dB)%n", matS.get(1, 0), s21_db);
        System.out.printf("  S22: %s (%.2f dB)%n", matS.get(1, 1), s22_db);

        // Verification checks
        boolean symS = Math.abs(s11_db - s22_db) < 0.5;
        boolean reciprocity = Math.abs(s12_db - s21_db) < 0.1;
        System.out.println("\n--- Sanity Checks ---");
        System.out.println("Symmetry S11 == S22: " + (symS ? "PASS" : "FAIL"));
        System.out.println("Reciprocity S12 == S21: " + (reciprocity ? "PASS" : "FAIL"));
        System.out.println("Coupling S21 < -10 dB: " + (s21_db < -10 ? "PASS" : "FAIL"));
    }
}
