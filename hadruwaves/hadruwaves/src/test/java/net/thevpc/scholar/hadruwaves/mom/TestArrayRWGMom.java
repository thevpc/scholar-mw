package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestArrayRWGMom {

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

    public static HGeometry buildAntennaGeometry() {
        double W = 13.5e-3;
        double L = 9.40e-3;
        double wf = 3.1e-3;
        double w70 = 1.55e-3;
        double y0 = 3.4e-3;
        double gap = 0.8e-3;
        double feed_in_y_start = -28.0e-3;
        double feed_in_y_end = -22.0e-3;

        List<HGeometry> parts = new ArrayList<>();

        // 4 Patches
        double[] xcs = {-33.0e-3, -11.0e-3, 11.0e-3, 33.0e-3};
        for (double xc : xcs) {
            double notchs = (W / 2) - (wf / 2 + gap);
            parts.add(createBox(xc - W / 2, 0, notchs, y0));
            parts.add(createBox(xc + wf / 2 + gap, 0, notchs, y0));
            parts.add(createBox(xc - W / 2, y0, W, L - y0));
            parts.add(createBox(xc - wf / 2, 0, wf, y0));
        }

        // Corporate feed network
        // Main input line
        parts.add(createBox(-wf / 2, feed_in_y_start, wf, 6.0e-3));
        // Stage 1 Wilkinson Horizontal Split
        parts.add(createBox(-22.0e-3, feed_in_y_end, 22.0e-3, w70));
        parts.add(createBox(0.0, feed_in_y_end, 22.0e-3, w70));
        // Inter-stage vertical trunks
        parts.add(createBox(-22.0e-3 - wf / 2, -22.0e-3, wf, 11.0e-3));
        parts.add(createBox(22.0e-3 - wf / 2, -22.0e-3, wf, 11.0e-3));
        // Stage 2 Wilkinson Horizontal Splits
        parts.add(createBox(-33.0e-3, -11.0e-3, 11.0e-3, w70));
        parts.add(createBox(-22.0e-3, -11.0e-3, 11.0e-3, w70));
        parts.add(createBox(11.0e-3, -11.0e-3, 11.0e-3, w70));
        parts.add(createBox(22.0e-3, -11.0e-3, 11.0e-3, w70));
        // Final feeds to patches
        parts.add(createBox(-33.0e-3 - wf / 2, -11.0e-3, wf, 11.0e-3));
        parts.add(createBox(-11.0e-3 - wf / 2, -11.0e-3, wf, 11.0e-3));
        parts.add(createBox(11.0e-3 - wf / 2, -11.0e-3, wf, 11.0e-3));
        parts.add(createBox(33.0e-3 - wf / 2, -11.0e-3, wf, 11.0e-3));

        HGeometry total = parts.get(0);
        for (int i = 1; i < parts.size(); i++) {
            total = total.addGeometry(parts.get(i));
        }
        return total;
    }

    public static void main(String[] args) {
        new TestArrayRWGMom().testArrayRWG();
    }

    @Test
    public void testArrayRWG() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);

        HGeometry antenna = buildAntennaGeometry();
        System.out.println("Antenna geometry created.");

        MeshTriangulationOptions opts = new MeshTriangulationOptions()
                .setMaxEdgeLength(2.0e-3)
                .setAdaptive(true);

        TestFunctions rwgTf = TestFunctionsFactory.createRWG(antenna, opts);
        DoubleToVector[] tfArr = rwgTf.toArray();
        System.out.println("Number of RWG basis functions: " + tfArr.length);

        double sub_x_min = -45.0e-3;
        double sub_y_min = -30.0e-3;
        double box_w = 90.0e-3;
        double box_l = 45.0e-3;
        Domain box = Domain.ofBounds(sub_x_min - 20e-3, sub_x_min + box_w + 20e-3, sub_y_min - 20e-3, sub_y_min + box_l + 20e-3);

        MomStructure mom = new MomStructure();
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setDomain(box);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", 4.4, 0.02), 1.6e-3));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setCircuitType(CircuitType.SERIAL);
        mom.setFrequency(6.5e9);
        mom.modeFunctions().setSize(2000);
        mom.setTestFunctions(rwgTf);

        double wf = 3.1e-3;
        Domain srcDom = Domain.ofPoints(-wf / 2, -28e-3, wf / 2, -27e-3);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, srcDom, Axis.Y, Complex.of(50))));

        System.out.println("Starting MoM solve at 6.5 GHz...");
        long t0 = System.currentTimeMillis();
        Complex Zin = mom.inputImpedance().evalComplex();
        long dt = System.currentTimeMillis() - t0;
        Complex Z0 = Complex.of(50);
        Complex s11 = Zin.minus(Z0).div(Zin.plus(Z0));
        double s11db = 20 * Math.log10(s11.abs().toDouble());
        System.out.printf("Solved in %d ms! Zin = %s, S11 = %s (%.2f dB)%n", dt, Zin, s11, s11db);
    }
}
