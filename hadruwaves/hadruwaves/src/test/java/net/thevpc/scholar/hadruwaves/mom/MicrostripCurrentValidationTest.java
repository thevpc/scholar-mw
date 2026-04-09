package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.mom.ProjectType;
import net.thevpc.scholar.hadruwaves.mom.BoxSpace;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.CellBoundaries;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.UserSinePattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MicrostripCurrentValidationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testMicrostripCurrent() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);

        double a = 10 * Maths.MM;
        double b = 10 * Maths.MM;
        double f = 2 * Maths.GHZ;
        double h = 1 * Maths.MM;
        double w = 2 * Maths.MM;

        MomStructure str = new MomStructure();
        str.setDomain(Domain.ofBounds(0, a, -b/2, b/2));
        str.setFrequency(f);
        
        // Lower space: Substrate with ground plane
        str.setFirstBoxSpace(BoxSpace.shortCircuit(Material.substrate(2.2), h));
        // Upper space: Vacuum
        str.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        
        str.setProjectType(ProjectType.PLANAR_STRUCTURE);
        str.modeFunctions().setSize(5000);

        // Strip geometry: from x=2.5 to x=7.5, centered in y
        Domain stripDomain = Domain.ofBounds(2.5 * Maths.MM, 7.5 * Maths.MM, -w/2, w/2);
        
        // Test functions on the strip (Basis functions)
        UserSinePattern p = new UserSinePattern(5, CellBoundaries.DDxUUy, null);
        GpAdaptiveMesh am = new GpAdaptiveMesh(GeometryFactory.createPolygonList(stripDomain.toGeometry()), p, null, new MeshAlgoRect());
        str.setTestFunctions(am);

        // Source: planar source at the beginning of the strip
        Domain sourceDomain = Domain.ofBounds(2.5 * Maths.MM, 3.0 * Maths.MM, -w/2, w/2);
        str.setSources(Maths.expr(1, sourceDomain));

        System.out.println("Evaluating Current...");
        Complex zin = str.inputImpedance().evalComplex();
        System.out.println("Zin: " + zin);
        
        // Tendency Check: Evaluate current along the strip and check concentration
        System.out.println("Current scan along the strip center (y=0):");
        double maxAbsOnStrip = 0;
        for (double x = 2.6; x <= 7.5; x += 0.5) {
            Complex c = str.current().evalVector(Axis.X, new double[]{x * Maths.MM}, 0, 0).get(0);
            double abs = c.absdbl();
            System.out.println("  x=" + x + "mm: " + c + " (abs=" + abs + ")");
            maxAbsOnStrip = Math.max(maxAbsOnStrip, abs);
        }
        
        System.out.println("Current scan outside the strip (y=4mm):");
        double maxAbsOffStrip = 0;
        for (double x = 2.6; x <= 7.5; x += 1.0) {
            Complex c = str.current().evalVector(Axis.X, new double[]{x * Maths.MM}, 4 * Maths.MM, 0).get(0);
            double abs = c.absdbl();
            System.out.println("  x=" + x + "mm: " + c + " (abs=" + abs + ")");
            maxAbsOffStrip = Math.max(maxAbsOffStrip, abs);
        }

        System.out.println("Current symmetry check (y=0.5 vs y=-0.5 at x=5mm):");
        Complex currentPos = str.current().evalVector(Axis.X, new double[]{5 * Maths.MM}, 0.5 * Maths.MM, 0).get(0);
        Complex currentNeg = str.current().evalVector(Axis.X, new double[]{5 * Maths.MM}, -0.5 * Maths.MM, 0).get(0);
        System.out.println("  y=0.5mm: " + currentPos);
        System.out.println("  y=-0.5mm: " + currentNeg);

        assertTrue(maxAbsOnStrip > 0, "Current on strip should be non-zero");
        assertTrue(maxAbsOnStrip > maxAbsOffStrip * 10, "Current should be specialized on the strip (ratio > 10)");
        assertEquals(currentPos.absdbl(), currentNeg.absdbl(), currentPos.absdbl() * 1e-6, "Current should be symmetric with respect to y=0");
        
        System.out.println("Validation Success: Current is concentrated and symmetric.");

        // Visual Validation
        if (true ||Boolean.getBoolean("plot")) {
            AbsoluteSamples samples = (AbsoluteSamples) str.getDomain().dtimes(100);
            Plot.title("Longitudinal Current 0").domain(str.getDomain()).plot(stripDomain);
            Plot.title("Longitudinal Current Jx").plot(str.current().evalMatrix(Axis.X, samples.getX(), samples.getY(), 0));

            System.out.println("Plotting... closing in 10 seconds");
            Object lock = new Object();
            synchronized (lock) {
                try {
                    lock.wait(30000); // Wait 10s
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
