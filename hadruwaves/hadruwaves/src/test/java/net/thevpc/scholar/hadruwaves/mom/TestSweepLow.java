package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;

public class TestSweepLow {
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
        mom.build();

        Complex z0 = Complex.of(50);
        System.out.printf("%-10s %-25s %-12s%n", "Freq(GHz)", "Zin(Ohm)", "S11(dB)");

        for (double f = 6.00; f <= 6.6001; f += 0.05) {
            mom.setFrequency(f * Maths.GHZ);
            ComplexMatrix matA = mom.matrixA().evalMatrix();
            ComplexMatrix matB = mom.matrixB().evalMatrix();
            int n = matA.getRowCount();

            ComplexMatrix Ah = matA.transposeHermitian();
            ComplexMatrix AhA = Ah.mul(matA);
            double maxDiag = 0;
            for (int i = 0; i < n; i++) {
                maxDiag = Math.max(maxDiag, AhA.get(i, i).absDouble());
            }

            double reg = 1e-5;
            ComplexMatrix regI = Maths.identityMatrix(n).mul(Complex.of(reg * maxDiag));
            ComplexMatrix regInv = AhA.add(regI).inv().mul(Ah);
            ComplexMatrix cMat = matB.transposeHermitian().mul(regInv).mul(matB);
            Complex zin = cMat.get(0, 0).inv();
            Complex s11 = zin.minus(z0).div(zin.plus(z0));
            double s11_db = 20 * Math.log10(s11.absDouble());

            System.out.printf("%-10.3f %-25s %-12.2f%n",
                    f, String.format("%.2f %+.2fj", zin.getReal(), zin.getImag()), s11_db);
        }
    }
}
