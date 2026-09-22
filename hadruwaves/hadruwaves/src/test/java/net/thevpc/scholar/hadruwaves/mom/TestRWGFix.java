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
        double L = 9.4 * Maths.MM;
        double wf = 3.1 * Maths.MM;
        double g = 0.4 * Maths.MM;
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
        options.setMaxArea(4.0 * Maths.MM * Maths.MM);
        options.setMaxCount(40);
        GpRWG rwgTF = TestFunctionsFactory.createRWG(antenna, options);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(1000);
        mom.setSources(Maths.vector(Maths.expr(0), Maths.expr(Esource, sourceDomain)));
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

        // Also evaluate input impedance at 6.0 GHz to verify matrix solver and condition number
        ComplexMatrix matA = mom.matrixA().evalMatrix();
        ComplexMatrix matB = mom.matrixB().evalMatrix();
        System.out.println("Matrix A size: " + matA.getRowCount() + "x" + matA.getColumnCount());
        System.out.println("Matrix B size: " + matB.getRowCount() + "x" + matB.getColumnCount());
        System.out.println("Matrix B norm: " + matB.norm1() + ", max: " + matB.maxAbs());

        System.out.println("\n--- Frequency Sweep 5.5 to 7.5 GHz ---");
        System.out.printf("%-10s %-25s %-12s%n", "Freq(GHz)", "Zin(Ohm)", "S11(dB)");
        Complex z0 = Complex.of(50);
        double minS11 = 0;
        double fRes = 0;
        for (double f = 5.5; f <= 7.5; f += 0.1) {
            mom.setFrequency(f * Maths.GHZ);
            ComplexMatrix zMat = mom.inputImpedance().evalMatrix();
            Complex zin = zMat.get(0, 0);
            Complex s11 = zin.minus(z0).div(zin.plus(z0));
            double s11_db = 20 * Math.log10(s11.absDouble());
            System.out.printf("%-10.2f %-25s %-12.2f%n", f, String.format("%.2f %+.2fj", zin.getReal(), zin.getImag()), s11_db);
            if (s11_db < minS11) {
                minS11 = s11_db;
                fRes = f;
            }
        }
        System.out.printf("%nBest Resonance: %.2f GHz (S11 = %.2f dB)%n", fRes, minS11);
    }
}
