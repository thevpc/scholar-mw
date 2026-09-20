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

        Domain sourceDomain = Domain.ofPoints(-wf / 2, feedStart, wf / 2, feedStart + 1.0 * Maths.MM);

        MeshTriangulationOptions options = new MeshTriangulationOptions();
        GpRWG rwgTF = TestFunctionsFactory.createRWG(antenna, options);

        MomStructure mom = new MomStructure();
        mom.setDomain(momDomain);
        mom.setBorders(WallBorders.EEEE);
        mom.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate("FR4", er, 0.02), h));
        mom.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        mom.setProjectType(ProjectType.PLANAR_STRUCTURE);
        mom.setCircuitType(CircuitType.SERIAL);
        mom.modeFunctions().setSize(50);
        mom.setSources(new DefaultPlanarSources(CstPlanarSource.ofVoltage(1.0, sourceDomain, Axis.Y, Complex.of(50))));
        mom.setTestFunctions(rwgTF);

        System.out.println("Building structure...");
        mom.build();

        net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector[] tfs = mom.testFunctions().toArray();
        System.out.println("Total test functions: " + tfs.length);

        int nanCount = 0;
        for (int i = 0; i < tfs.length; i++) {
            Complex sp = Maths.scalarProduct(tfs[i], mom.modeFunctions().getMode(0).fn).toComplex();
            if (sp.isNaN()) {
                nanCount++;
                if (nanCount <= 5) {
                    System.out.println("NaN at tf[" + i + "]: " + tfs[i]);
                }
            }
        }
        System.out.println("Total NaNs in scalar product: " + nanCount + " / " + tfs.length);
    }
}
