package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRWG;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AntennaPatchValidationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testAntennaPatch() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);

        double f = 2.4 * Maths.GHZ;
        double er = 4.4;
        double h = 1.6 * Maths.MM;
        
        double W = 38.0 * Maths.MM;
        double L = 29.4 * Maths.MM;
        double wf = 3.1 * Maths.MM;
        double y0 = 10.3 * Maths.MM;
        double gap = 1.5 * Maths.MM;
        
        MomStructure str = new MomStructure();
        // Domain matching .ntx substrate + padding
        Domain substrateDomain = Domain.ofBounds(-29 * Maths.MM, 29 * Maths.MM, -20 * Maths.MM, 39 * Maths.MM);
        double paddingX = 75.0 * Maths.MM;
        double paddingY = 75.0 * Maths.MM;
        str.setDomain(substrateDomain.pad(paddingX, paddingY));
        str.setFrequency(f);
        
        // Lower space: Substrate with ground plane
        str.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate(er), h));
        // Upper space: Vacuum
        str.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        
        str.setProjectType(ProjectType.PLANAR_STRUCTURE);
        str.modeFunctions().setSize(1024);

        // Patch Geometry
        HGeometryList antennaGeom = GeometryFactory.createPolygonList(
                GeometryFactory.createPolygon(Domain.ofBounds(-19 * Maths.MM, 19 * Maths.MM, y0, L)), // Body
                GeometryFactory.createPolygon(Domain.ofBounds(-wf/2, wf/2, -15 * Maths.MM, y0)) // Feedline
        );

        // Test Functions: RWG
        GpRWG rwg = new GpRWG(antennaGeom, new MeshTriangulationOptions().setMaxArea(100 * Maths.MM * Maths.MM));
        str.setTestFunctions(rwg);

        // Source: planar source at the beginning of the feedline
        Domain sourceDomain = Domain.ofBounds(-wf/2, wf/2, -15 * Maths.MM, -14 * Maths.MM);
        str.setSources(Maths.vector(Maths.expr(0), Maths.expr(1, sourceDomain)));

        System.out.println("Evaluating Antenna Patch...");
        System.out.println("Test Functions Count: " + str.testFunctions().count());
        Complex zin = str.inputImpedance().evalComplex();
        System.out.println("Zin: " + zin);
        
        if (Boolean.getBoolean("plot")) {
            AbsoluteSamples samples = (AbsoluteSamples) str.getDomain().dtimes(200);
            Plot.title("Patch Current Vector Field J").plot(str.current().evalVDiscrete(samples.getX(), samples.getY()));
            Plot.title("Patch Longitudinal Current Jx").plot(str.current().evalMatrix(Axis.X, samples.getX(), samples.getY(), 0));
            
            System.out.println("Plotting... closing in 30 seconds");
            Object lock = new Object();
            synchronized (lock) {
                try {
                    lock.wait(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        
        assertNotNull(zin, "Zin should be calculated");
        assertTrue(zin.absdbl() > 0, "Zin should be non-zero");
    }
}
