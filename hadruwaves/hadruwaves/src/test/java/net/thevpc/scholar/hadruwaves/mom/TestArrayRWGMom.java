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

        List<net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.SubMesh> subMeshes = new ArrayList<>();
        subMeshes.add(new net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.SubMesh(
                new DefaultHPolygon(HPoint.create(-35e-3, -28e-3), HPoint.create(35e-3, -28e-3), HPoint.create(35e-3, 0), HPoint.create(-35e-3, 0)),
                2.0e-3
        ));
        MeshTriangulationOptions opts = new MeshTriangulationOptions()
                .setMaxEdgeLength(3.0e-3)
                .setSubMeshes(subMeshes)
                .setAdaptive(false);

        System.out.println("Step 1a: initial JTS triangulation of antenna geometry...");
        long t_mesh0 = System.currentTimeMillis();
        List<HTriangle> rawTriangles = net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshRefinementHelper.triangulate(antenna);
        System.out.printf("Step 1a done in %d ms: %d initial triangles.%n", (System.currentTimeMillis() - t_mesh0), rawTriangles.size());

        System.out.println("Step 1b: refineTriangles with sub-meshes...");
        long t_ref0 = System.currentTimeMillis();
        net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshRefinement r = new net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshRefinement()
                .maxWidth(opts.getMaxEdgeLength())
                .subMeshes(opts.getSubMeshes())
                .adaptive(false);
        List<HTriangle> refined = net.thevpc.scholar.hadrumaths.meshalgo.tri.MeshRefinementHelper.refineTriangles(rawTriangles, r);
        System.out.printf("Step 1b done in %d ms: %d refined triangles.%n", (System.currentTimeMillis() - t_ref0), refined.size());

        System.out.println("Step 2: pairing RWG test functions...");
        long t_rwg0 = System.currentTimeMillis();
        TestFunctions rwgTf = TestFunctionsFactory.createRWG(antenna, opts);
        DoubleToVector[] tfArr = rwgTf.toArray();
        System.out.printf("Step 2 done in %d ms: %d RWG basis functions generated.%n", (System.currentTimeMillis() - t_rwg0), tfArr.length);

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
        mom.setSolverType(MomSolverType.SPATIAL_MPIE);
        mom.setFrequency(6.5e9);
        mom.modeFunctions().setSize(500);
        mom.setTestFunctions(rwgTf);

        double wf = 3.1e-3;
        Domain srcDom = Domain.ofPoints(-wf / 2, -27e-3, wf / 2, -26e-3);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, srcDom, Axis.Y, Complex.of(50))));

        System.out.println("Starting MoM solve at 6.5 GHz...");
        ComplexMatrix matB = mom.matrixB().evalMatrix();
        DoubleToVector[] tfs = mom.testFunctions().toArray();
        for (int i = 0; i < matB.getRowCount(); i++) {
            Complex bval = matB.get(i, 0);
            if (!bval.isZero()) {
                net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG rwg = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
                HPoint mid = rwg.getSharedEdgeMidpoint();
                HTriangle tri1 = rwg.getTriangle1();
                System.out.printf("  Port edge [%4d]: B = %s, mid = (%.3f, %.3f)mm, edge=(%.3f,%.3f)->(%.3f,%.3f)%n",
                        i, bval, mid.x / Maths.MM, mid.y / Maths.MM,
                        tri1.p2().x / Maths.MM, tri1.p2().y / Maths.MM,
                        tri1.p3().x / Maths.MM, tri1.p3().y / Maths.MM);
            }
        }
        Complex Z0 = Complex.of(50);
        mom.setFrequency(6.65e9);
        ComplexMatrix matA = mom.matrixA().evalMatrix();
        ComplexMatrix vecB = mom.matrixB().evalMatrix();
        System.out.println("=== ALL RWG EDGES NEAR FEED PORT (y < -26 mm) ===");
        for (int i = 0; i < tfs.length; i++) {
            net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG rwg = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
            if (rwg != null) {
                HPoint mid = rwg.getSharedEdgeMidpoint();
                if (mid.y < -26e-3 && Math.abs(mid.x) <= 1.6e-3) {
                    HTriangle t1 = rwg.getTriangle1();
                    System.out.printf("RWG[%4d]: mid=(%.3f, %.3f)mm, edge=(%.3f,%.3f)->(%.3f,%.3f), span=[%.3f,%.3f], gamma=%.4e%n",
                            i, mid.x / Maths.MM, mid.y / Maths.MM,
                            t1.p2().x / Maths.MM, t1.p2().y / Maths.MM,
                            t1.p3().x / Maths.MM, t1.p3().y / Maths.MM,
                            Math.min(t1.p2().x, t1.p3().x) / Maths.MM, Math.max(t1.p2().x, t1.p3().x) / Maths.MM,
                            rwg.deltaGapGamma(Axis.Y));
                }
            }
        }
        System.out.println("==================================================");
        ComplexMatrix vecX = matA.solve(vecB);
        double curPatch1 = 0, curPatch2 = 0, curPatch3 = 0, curPatch4 = 0, curFeed = 0;
        Complex sumBX = Complex.ZERO;
        for (int i = 0; i < vecX.getRowCount(); i++) {
            Complex bval = matB.get(i, 0);
            if (!bval.isZero()) {
                Complex xval = vecX.get(i, 0);
                Complex contrib = bval.mul(xval);
                sumBX = sumBX.plus(contrib);
                System.out.printf("  Port edge [%4d]: B = %s, X = %s, B*X = %s%n", i, bval, xval, contrib);
            }
            net.thevpc.scholar.hadrumaths.symbolic.double2double.RWG rwg = RWGDeltaGapBMatrix.tryUnwrapRWG(tfs[i]);
            HPoint mid = rwg.getSharedEdgeMidpoint();
            double mag = vecX.get(i, 0).absDouble();
            if (mid.y < -22e-3 && Math.abs(mid.x) < 2e-3) {
                System.out.printf("  Feedline edge at y=%.2f mm: mid=(%.3f, %.3f), X = %s, gamma=%.4e%n",
                        mid.y / Maths.MM, mid.x / Maths.MM, mid.y / Maths.MM, vecX.get(i, 0), rwg.deltaGapGamma(Axis.Y));
            }
            if (mid.y >= 0) {
                if (mid.x < -22e-3) curPatch1 += mag;
                else if (mid.x < 0) curPatch2 += mag;
                else if (mid.x < 22e-3) curPatch3 += mag;
                else curPatch4 += mag;
            } else {
                curFeed += mag;
            }
        }
        System.out.printf("Sum(B*X) = %s, 1/Sum(B*X) = %s%n", sumBX, sumBX.inv());
        System.out.printf("Current distribution at 6.65 GHz: Feed=%.4f, Patch1=%.4f, Patch2=%.4f, Patch3=%.4f, Patch4=%.4f%n",
                curFeed, curPatch1, curPatch2, curPatch3, curPatch4);

        double h_sub = 1.6e-3;
        double er_sub = 4.4;
        double u_sub = wf / h_sub;
        double epsEff_sub = (er_sub + 1.0) / 2.0 + (er_sub - 1.0) / 2.0 / Math.sqrt(1.0 + 12.0 / u_sub);
        System.out.printf("Microstrip epsEff = %.4f%n", epsEff_sub);

        double f = 6.65e9;
        mom.setFrequency(f);
        Complex Zin_eval = mom.inputImpedance().evalComplex();
        Complex s11_eval = mom.sparameters().evalComplex();
        System.out.printf("Single point 6.65 GHz: Zin = %s, S11 = %s (%.2f dB)%n",
                Zin_eval, s11_eval, 20 * Math.log10(s11_eval.abs().toDouble()));
    }
}
