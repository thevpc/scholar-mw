package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.*;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;

public class TestPatchNTxMom {
    public static void main(String[] args) {
        net.thevpc.nuts.Nuts.require();
        Maths.Config.setCacheEnabled(false);

        double er = 4.4;
        double h = 1.6 * Maths.MM;
        double W = 13.5 * Maths.MM;
        double wf = 3.1 * Maths.MM;

        Domain subDomain = Domain.ofPoints(-13 * Maths.MM, -11 * Maths.MM, 13 * Maths.MM, 15 * Maths.MM);
        Domain momDomain = subDomain.pad(20 * Maths.MM, 20 * Maths.MM);

        for (double y0_val : new double[]{2.8, 3.0, 3.2, 3.4}) {
            double L = 9.75 * Maths.MM;
            double y0 = y0_val * Maths.MM;
            double feedStart = -8.5;

            Domain feedGeom = Domain.ofPoints(-wf / 2, feedStart * Maths.MM, wf / 2, y0);
            Domain sourceDomain = Domain.ofPoints(-wf / 2, feedStart * Maths.MM, wf / 2, (feedStart + 1.0) * Maths.MM);
            Domain patchFullGeom = Domain.ofPoints(-6.75 * Maths.MM, 0, 6.75 * Maths.MM, L);

            MomStructure mom = new MomStructure();
            mom.setDomain(momDomain);
            mom.setBorders(WallBorders.EEEE);
            mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
            mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
            mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
            mom.setCircuitType(CircuitType.SERIAL);
            mom.modeFunctions().setSize(2000);
            mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, sourceDomain, Axis.Y, Complex.of(50))));

            ListTestFunctions tf = new ListTestFunctions();
            tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(feedGeom, new DefaultHPolygon(feedGeom)),
                    new UserSinePattern(2, 6, CellBoundaries.UUxUUy, CellBoundaries.UUxUUy),
                    TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));
            tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(patchFullGeom, new DefaultHPolygon(patchFullGeom)),
                    new UserSinePattern(4, 6, CellBoundaries.UUxDDy, CellBoundaries.UUxDDy),
                    TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));

            mom.setTestFunctions(tf);
            System.out.printf("%n=== MOM: L=%.2f mm, y0=%.2f mm, feedStart=%.1f mm ===%n", L / Maths.MM, y0 / Maths.MM, feedStart);
            for (double f = 6.75e9; f <= 6.90e9; f += 0.025e9) {
                mom.setFrequency(f);
                Complex zin = mom.inputImpedance().evalComplex();
                Complex s11 = mom.sparameters().evalComplex();
                double s11db = 20 * Math.log10(Math.max(1e-9, s11.absdbl()));
                System.out.printf("  f=%-6.3f GHz | Zin=%-25s | R=%-6.1f | X=%-6.1f | S11=%-6.2f dB%n",
                        f / 1e9, zin, zin.realdbl(), zin.imagdbl(), s11db);
            }
        }
    }
}
