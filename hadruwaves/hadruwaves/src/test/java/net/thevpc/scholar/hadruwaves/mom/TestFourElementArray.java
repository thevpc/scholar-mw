package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;

public class TestFourElementArray {

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

        // Separation between adjacent elements d = 22 mm (~0.5 lambda0 at 6.8 GHz)
        double d = 22.0 * Maths.MM;
        double[] xCenters = new double[]{
                -1.5 * d, // Element 1: -33 mm
                -0.5 * d, // Element 2: -11 mm
                 0.5 * d, // Element 3: +11 mm
                 1.5 * d  // Element 4: +33 mm
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
        mom.setFrequency(6.45 * Maths.GHZ);

        System.out.println("Building 1x4 array MomStructure...");
        long t0 = System.currentTimeMillis();
        mom.build();
        long t1 = System.currentTimeMillis();
        System.out.printf("MomStructure built in %.2f s%n", (t1 - t0) / 1000.0);

        System.out.println("Evaluating Matrix B (should be N x 4)...");
        ComplexMatrix matB = mom.matrixB().evalMatrix();
        System.out.println("Matrix B dimensions: " + matB.getRowCount() + " x " + matB.getColumnCount());

        System.out.println("\nEvaluating 4x4 Input Impedance Matrix Z...");
        long tz0 = System.currentTimeMillis();
        ComplexMatrix matZ = mom.inputImpedance().evalMatrix();
        long tz1 = System.currentTimeMillis();
        System.out.printf("Matrix Z evaluated in %.2f s%n", (tz1 - tz0) / 1000.0);
        System.out.println("Matrix Z dimensions: " + matZ.getRowCount() + " x " + matZ.getColumnCount());

        System.out.println("\nEvaluating 4x4 S-Parameters Matrix S...");
        ComplexMatrix matS = mom.sparameters().evalMatrix();
        System.out.println("Matrix S dimensions: " + matS.getRowCount() + " x " + matS.getColumnCount());

        System.out.println("\n=== 4x4 S-Parameter Magnitude Matrix (dB) ===");
        System.out.print("      ");
        for (int j = 1; j <= 4; j++) {
            System.out.printf("  Port %d    ", j);
        }
        System.out.println();
        for (int i = 0; i < 4; i++) {
            System.out.printf("Port %d:", i + 1);
            for (int j = 0; j < 4; j++) {
                double s_db = 20 * Math.log10(matS.get(i, j).absDouble());
                System.out.printf("  %7.2f dB", s_db);
            }
            System.out.println();
        }

        System.out.println("\n=== Physical Sanity Checks ===");
        double s11 = 20 * Math.log10(matS.get(0, 0).absDouble());
        double s22 = 20 * Math.log10(matS.get(1, 1).absDouble());
        double s33 = 20 * Math.log10(matS.get(2, 2).absDouble());
        double s44 = 20 * Math.log10(matS.get(3, 3).absDouble());

        double s21 = 20 * Math.log10(matS.get(1, 0).absDouble());
        double s32 = 20 * Math.log10(matS.get(2, 1).absDouble());
        double s43 = 20 * Math.log10(matS.get(3, 2).absDouble());

        double s31 = 20 * Math.log10(matS.get(2, 0).absDouble());
        double s42 = 20 * Math.log10(matS.get(3, 1).absDouble());

        double s41 = 20 * Math.log10(matS.get(3, 0).absDouble());

        System.out.printf("Outer elements return loss: S11=%.2f dB, S44=%.2f dB (diff=%.2f dB)%n", s11, s44, Math.abs(s11 - s44));
        System.out.printf("Inner elements return loss: S22=%.2f dB, S33=%.2f dB (diff=%.2f dB)%n", s22, s33, Math.abs(s22 - s33));
        System.out.printf("Adjacent coupling (d=22mm):   S21=%.2f dB, S32=%.2f dB, S43=%.2f dB%n", s21, s32, s43);
        System.out.printf("Next-adjacent (d=44mm):       S31=%.2f dB, S42=%.2f dB%n", s31, s42);
        System.out.printf("Far-end coupling (d=66mm):    S41=%.2f dB%n", s41);

        boolean passMonotonic = (s21 > s31) && (s31 > s41);
        System.out.println("Monotonic coupling drop with distance (|S21| > |S31| > |S41|): " + (passMonotonic ? "PASS" : "FAIL"));
    }
}
