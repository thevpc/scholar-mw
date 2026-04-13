package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.nuts.io.NOut;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.geom.HGeometry;
import net.thevpc.scholar.hadrumaths.geom.HGeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshTriangulationOptions;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.uicomponents.AreaComponent;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.ModeInfo;
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
        double V0=1; //1V

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
        str.modeFunctions().setSize(5000);

        // Patch Geometry
        HGeometryList antennaGeom = GeometryFactory.createPolygonList(
                GeometryFactory.createPolygon(Domain.ofBounds(-19 * Maths.MM, 19 * Maths.MM, 0, L)), // Combined Body + Feedline
                GeometryFactory.createPolygon(Domain.ofBounds(-wf / 2, wf / 2, -15 * Maths.MM, 0)),
                GeometryFactory.createPolygon(Domain.ofBounds(-19 * Maths.MM, -(wf / 2 + gap), 0, y0)), // Left Flank
                GeometryFactory.createPolygon(Domain.ofBounds((wf / 2 + gap), 19 * Maths.MM, 0, y0)) // Right Flank
        );

        // Test Functions: RWG
        GpRWG rwg = new GpRWG(antennaGeom, new MeshTriangulationOptions().setMaxArea(100 * Maths.MM * Maths.MM));
        str.setTestFunctions(rwg);

        // Source: planar source at the beginning of the feedline
        Domain sourceDomain = Domain.ofBounds(-wf / 2, wf / 2, -15 * Maths.MM, -14 * Maths.MM);
        // Esource=V0/d
        double Esource=V0/(1*Maths.MM);

        str.setSources(Maths.vector(Maths.expr(0), Maths.expr(Esource, sourceDomain)));
        str.setCircuitType(CircuitType.SERIAL);

        System.out.println("Evaluating Antenna Patch...");
        System.out.println("Test Functions Count: " + str.testFunctions().count());
        Plot.title("mesh").plot(new AreaComponent(str.testFunctions().mesh().stream().map(x -> x.getGeometry()).toArray(HGeometry[]::new)));
        DoubleToVector[] testFunctionsArr = str.testFunctions().toArray();
        ModeInfo[] modesArr = str.modeFunctions().getModes();

        Plot.title("Gp").plot(testFunctionsArr);
        DoubleToVector[] arr = testFunctionsArr;
        for (int i = 0; i < arr.length; i++) {
            DoubleToVector d = arr[i];
            NOut.println(NMsg.ofC("g[%s]=%s", i, d));
        }

        for (int i = 0; i < Math.min(10, modesArr.length); i++) {
            ModeInfo m = modesArr[i];
            NOut.println(NMsg.ofC("f[%s]=%s=%s", i, m, m.fn));
        }

        for (int i = 0; i < Math.min(10, modesArr.length); i++) {
            ModeInfo m = modesArr[i];
            NOut.println(NMsg.ofC("y[%s]=y[%s]=%s", i, m.getMode(), m.impedance.admittance()));
        }

        ComplexMatrix sp = str.getTestModeScalarProducts();

        for (int pq = 0; pq < testFunctionsArr.length; pq++) {
            for (int mn = 0; mn < modesArr.length; mn++) {
                if (mn >= 10) {
                    //just a snapshot!
                    break;
                }
                NOut.println(NMsg.ofC("<f" + mn + ",g" + pq + ">=<" + modesArr[mn].getMode() + ",g" + pq + "> = " + sp.get(pq, mn)));
            }
        }

        Plot.title("Scalar Products").asMatrix().plot(sp);

        Complex zin = str.inputImpedance().evalComplex();
        NOut.println("Zin: " + zin);

        AbsoluteSamples samples = str.getDomain().dtimes(100);
        ComplexMatrix current = str.current().evalMatrix(Axis.X, samples);
        NOut.println(current);
        if (true) {
//            Plot.title("Patch Current Vector Field J").plot(str.current().evalVDiscrete(samples.getX(), samples.getY()));
            Plot.title("Patch Longitudinal Current Jx").plot(current);
            NOut.println("Plotting... closing in 30 seconds");
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
