package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.*;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;

public class TestPatchUpper6GHz {
    public static void main(String[] args) {
        net.thevpc.nuts.Nuts.require();
        Maths.Config.setCacheEnabled(false);

        double er = 4.4;
        double h = 1.6 * Maths.MM;
        double W = 13.5 * Maths.MM;
        double L = 9.9 * Maths.MM;
        double wf = 3.1 * Maths.MM;
        double y0 = 3.3 * Maths.MM;
        double gap = 0.8 * Maths.MM;

        Domain subDomain = Domain.ofPoints(-13 * Maths.MM, -11 * Maths.MM, 13 * Maths.MM, 15 * Maths.MM);
        Domain momDomain = subDomain.pad(20 * Maths.MM, 20 * Maths.MM);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(2000);

        double curL = 9.9 * Maths.MM;
        double curY0 = 3.3 * Maths.MM;
        for (double feedExt : new double[]{2.0, 6.0, 12.0}) {
            Domain srcDom = Domain.ofPoints(-wf / 2, -feedExt * Maths.MM, wf / 2, (-feedExt + 1.0) * Maths.MM);
            mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, srcDom, Axis.Y, Complex.of(50))));
            Domain feedGeom = Domain.ofPoints(-wf / 2, -feedExt * Maths.MM, wf / 2, curY0);
            Domain patchGeom = Domain.ofPoints(-W / 2, 0, W / 2, curL);

            ListTestFunctions tf = new ListTestFunctions();
            tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(feedGeom, new DefaultHPolygon(feedGeom)),
                    new UserSinePattern(2, 6, CellBoundaries.UUxUUy, CellBoundaries.UUxUUy),
                    TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));
            tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(patchGeom, new DefaultHPolygon(patchGeom)),
                    new UserSinePattern(4, 6, CellBoundaries.UUxDDy, CellBoundaries.UUxDDy),
                    TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));

            mom.setTestFunctions(tf);
            System.out.println("\n=== Testing feedExt=" + feedExt + "mm ===");
            mom.setFrequency(6.6e9);
            ComplexMatrix matA = mom.matrixA().evalMatrix();
            ComplexMatrix matB = mom.matrixB().evalMatrix();
            System.out.println("UserSinePattern matA max: " + matA.maxAbs() + ", A[0,0]=" + matA.get(0, 0));
            System.out.println("UserSinePattern matB max: " + matB.maxAbs() + ", B norm1=" + matB.norm1());
            for (int i = 0; i < matB.getRowCount(); i++) {
                if (matB.get(i, 0).absDouble() > 1e-9) {
                    System.out.println("  B[" + i + "] = " + matB.get(i, 0));
                }
            }
            ComplexMatrix X = matA.solve(matB);
            Complex Y = matB.transposeHermitian().mul(X).get(0, 0);
            System.out.println("UserSinePattern Y = " + Y + ", 1/Y = " + Y.inv());
            break;
        }
    }
}
