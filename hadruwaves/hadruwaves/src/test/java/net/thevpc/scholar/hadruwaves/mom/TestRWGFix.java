package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;

public class TestRWGFix {
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

        Domain subDomain = Domain.ofPoints(-13 * Maths.MM, -11 * Maths.MM, 13 * Maths.MM, 15 * Maths.MM);
        Domain momDomain = subDomain.pad(20 * Maths.MM, 20 * Maths.MM);

        HPolygon feed = GeometryFactory.createPolygon(Domain.ofPoints(-wf / 2, feedStart, wf / 2, y0));
        HPolygon left = GeometryFactory.createPolygon(Domain.ofPoints(-W / 2, 0, -wf / 2 - g, y0));
        HPolygon right = GeometryFactory.createPolygon(Domain.ofPoints(wf / 2 + g, 0, W / 2, y0));
        HPolygon top = GeometryFactory.createPolygon(Domain.ofPoints(-W / 2, y0, W / 2, L));
        HGeometry antenna = feed.addGeometry(left).addGeometry(right).addGeometry(top);

        Domain sourceDomain = Domain.ofBounds(-wf / 2, wf / 2, feedStart, feedStart + 1.0 * Maths.MM);
        double V0 = 1.0;
        double Esource = V0 / (1.0 * Maths.MM);

        MeshTriangulationOptions options = new MeshTriangulationOptions();
        options.setMaxArea(1.0 * Maths.MM * Maths.MM);
        GpRWG rwgTF = TestFunctionsFactory.createRWG(antenna, options);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(2000);
        mom.setSources(CstPlanarSource.ofVoltage(1.0, sourceDomain, Axis.Y, Complex.of(50)));
        mom.setTestFunctions(rwgTF);

        long t0 = System.currentTimeMillis();
        System.out.println("Building structure...");
        mom.build();
        long t1 = System.currentTimeMillis();
        System.out.printf("MomStructure built in %.2f s%n", (t1 - t0) / 1000.0);

        net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector[] tfs = mom.testFunctions().toArray();
        System.out.println("Total test functions: " + tfs.length);

        int nanCount = 0;
        long t2 = System.currentTimeMillis();
        for (int i = 0; i < tfs.length; i++) {
            Complex sp = Maths.scalarProduct(tfs[i], mom.modeFunctions().getMode(0).fn).toComplex();
            if (sp.isNaN()) {
                nanCount++;
                if (nanCount <= 5) {
                    System.out.println("NaN at tf[" + i + "]: " + tfs[i]);
                }
            }
        }
        long t3 = System.currentTimeMillis();
        System.out.printf("Scalar products evaluated in %.2f s (%.1f ms/sp)%n",
                (t3 - t2) / 1000.0, (double)(t3 - t2) / tfs.length);
        System.out.println("Total NaNs in scalar product: " + nanCount + " / " + tfs.length);

        // Also evaluate input impedance to verify matrix solver and condition number
        mom.setFrequency(6.8 * Maths.GHZ);
        ComplexMatrix matA = mom.matrixA().evalMatrix();
        ComplexMatrix matB = mom.matrixB().evalMatrix();
        System.out.println("Matrix A size: " + matA.getRowCount() + "x" + matA.getColumnCount());
        System.out.println("Matrix B size: " + matB.getRowCount() + "x" + matB.getColumnCount());
        System.out.println("Matrix B norm: " + matB.norm1() + ", max: " + matB.maxAbs());
        int nonZeroB = 0;
        for (int i = 0; i < matB.getRowCount(); i++) {
            Complex val = matB.get(i, 0);
            if (!val.isZero()) {
                nonZeroB++;
                System.out.println("  B[" + i + "] = " + val);
            }
        }
        System.out.println("Non-zero entries in B: " + nonZeroB + " / " + matB.getRowCount());

        System.out.println("\n--- Native Hadruwaves Zin Evaluation at 6.45 GHz ---");
        mom.setFrequency(6.45 * Maths.GHZ);
        ComplexMatrix zinNative = mom.inputImpedance().evalMatrix();
        Complex zinVal = zinNative.get(0, 0);
        Complex z0 = Complex.of(50);
        Complex s11 = zinVal.minus(z0).div(zinVal.plus(z0));
        System.out.println("  Native Zin at 6.45 GHz: " + zinVal);
        System.out.printf("  Native S11 at 6.45 GHz: %.2f dB%n", 20 * Math.log10(s11.absDouble()));
    }
}
