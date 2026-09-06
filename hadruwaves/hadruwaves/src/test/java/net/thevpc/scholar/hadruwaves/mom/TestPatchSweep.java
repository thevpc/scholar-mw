package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.*;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.*;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.*;

public class TestPatchSweep {
    public static void main(String[] args) {
        net.thevpc.nuts.Nuts.require();
        Maths.Config.setCacheEnabled(false);

        double er = 4.4;
        double h = 1.6 * Maths.MM;
        double W = 38.0 * Maths.MM;
        double L = 29.4 * Maths.MM;
        double wf = 3.1 * Maths.MM;
        double y0 = 10.3 * Maths.MM;
        double gap = 1.5 * Maths.MM;

        Domain subDomain = Domain.ofPoints(-29 * Maths.MM, -20 * Maths.MM, 29 * Maths.MM, 39 * Maths.MM);
        Domain momDomain = subDomain.pad(60 * Maths.MM, 60 * Maths.MM);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(2000);

        Domain sourceDomain = Domain.ofPoints(-wf / 2, -15 * Maths.MM, wf / 2, -14 * Maths.MM);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, sourceDomain, Axis.Y, Complex.of(50))));

        Domain feedGeom = Domain.ofPoints(-wf / 2, -15 * Maths.MM, wf / 2, y0);
        Domain patchModeGeom = Domain.ofPoints(-W / 2, 0, W / 2, L);
        Domain leftFlank = Domain.ofPoints(-W / 2, 0, -(wf / 2 + gap), y0);
        Domain rightFlank = Domain.ofPoints((wf / 2 + gap), 0, W / 2, y0);

        ListTestFunctions tf = new ListTestFunctions();
        // Feedline
        tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(feedGeom, new DefaultHPolygon(feedGeom)),
                new UserSinePattern(4, 4, CellBoundaries.DDxUUy, CellBoundaries.DDxUUy),
                TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));
        // Flanks
        tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(leftFlank, new DefaultHPolygon(leftFlank)),
                new UserSinePattern(4, 4, CellBoundaries.DDxDUy, CellBoundaries.DDxDUy),
                TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));
        tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(rightFlank, new DefaultHPolygon(rightFlank)),
                new UserSinePattern(4, 4, CellBoundaries.DDxDUy, CellBoundaries.DDxDUy),
                TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));
        // Patch mode
        tf.add(new GpAdaptiveMesh(new DefaultHGeometryList(patchModeGeom, new DefaultHPolygon(patchModeGeom)),
                new UserSinePattern(6, 6, CellBoundaries.UUxDDy, CellBoundaries.UUxDDy),
                TestFunctionsSymmetry.NO_SYMMETRY, new net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect(GridPrecision.LEAST_PRECISION)));

        mom.setTestFunctions(tf);

        System.out.println("Total test functions: " + mom.testFunctions().count());
        System.out.printf("%-10s | %-25s | %-10s | %-10s | %-10s%n", "Freq (GHz)", "Zin (Ohm)", "R (Ohm)", "X (Ohm)", "|S11|");
        System.out.println("--------------------------------------------------------------------------------");

        for (double f = 2.0e9; f <= 2.6e9; f += 0.05e9) {
            mom.setFrequency(f);
            Complex zin = mom.inputImpedance().evalComplex();
            Complex s11 = mom.sparameters().evalComplex();
            System.out.printf("%-10.3f | %-25s | %-10.2f | %-10.2f | %-10.4f%n",
                    f / 1e9, zin, zin.realdbl(), zin.imagdbl(), s11.absdbl());
        }
    }
}
