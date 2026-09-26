package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestMpieMicrostripPatch {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    private static HGeometry createBox(double x, double y, double w, double h) {
        return new DefaultHPolygon(
                HPoint.create(x, y),
                HPoint.create(x + w, y),
                HPoint.create(x + w, y + h),
                HPoint.create(x, y + h)
        );
    }

    @Test
    public void testPatchMpie() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);

        double W = 13.5e-3;
        double L = 9.9e-3;
        double wf = 3.1e-3;
        double feedLength = 8.0e-3;

        HGeometry feed = createBox(-wf / 2, -feedLength, wf, feedLength);
        HGeometry patch = createBox(-W / 2, 0, W, L);
        HGeometry geom = feed.addGeometry(patch);

        MeshTriangulationOptions opts = new MeshTriangulationOptions()
                .setMaxEdgeLength(2.0e-3)
                .setAdaptive(false);

        TestFunctions rwgTf = TestFunctionsFactory.createRWG(geom, opts);
        int numEdges = rwgTf.toArray().length;
        System.out.printf("Mesh generated: %d RWG basis functions%n", numEdges);
        Assertions.assertTrue(numEdges > 0, "Must have RWG basis functions");

        Domain box = Domain.ofBounds(-30e-3, 30e-3, -30e-3, 30e-3);

        MomStructure mom = new MomStructure();
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setDomain(box);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", 4.4, 0.02), 1.6e-3));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setCircuitType(CircuitType.SERIAL);
        mom.setSolverType(MomSolverType.SPATIAL_MPIE);
        mom.setFrequency(6.6e9);
        mom.setTestFunctions(rwgTf);

        Domain srcDom = Domain.ofPoints(-wf / 2, -feedLength, wf / 2, -feedLength + 1.5e-3);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, srcDom, Axis.Y, Complex.of(50))));

        long t0 = System.currentTimeMillis();
        ComplexMatrix matA = mom.matrixA().evalMatrix();
        long tA = System.currentTimeMillis() - t0;
        System.out.printf("Matrix A (%dx%d) computed in %d ms%n", matA.getRowCount(), matA.getColumnCount(), tA);

        ComplexMatrix matB = mom.matrixB().evalMatrix();
        System.out.printf("Matrix B norm1: %.4e, non-zeros: %d%n", matB.norm1(), countNonZero(matB));

        for (double f = 5.0e9; f <= 9.0e9; f += 0.4e9) {
            mom.setFrequency(f);
            Complex zin = mom.inputImpedance().evalComplex();
            Complex Z0 = Complex.of(50);
            Complex s11 = zin.minus(Z0).div(zin.plus(Z0));
            double s11db = 20 * Math.log10(s11.abs().toDouble());
            System.out.printf("f = %.2f GHz: Zin = %s, S11 = %.2f dB%n", f / 1e9, zin, s11db);
        }
    }

    private static int countNonZero(ComplexMatrix m) {
        int cnt = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            if (!m.get(i, 0).isZero()) cnt++;
        }
        return cnt;
    }
}
