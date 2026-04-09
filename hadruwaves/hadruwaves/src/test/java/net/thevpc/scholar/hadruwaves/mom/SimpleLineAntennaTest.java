package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.io.NOut;
import net.thevpc.nuts.text.NMsg;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.CellBoundaries;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.UserSinePattern;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import net.thevpc.nuts.Nuts;

import static net.thevpc.scholar.hadrumaths.Maths.GHZ;
import static net.thevpc.scholar.hadrumaths.Maths.MM;

import net.thevpc.scholar.hadruwaves.Material;

import static net.thevpc.scholar.hadruwaves.mom.BoxSpace.matchedLoad;
import static net.thevpc.scholar.hadruwaves.mom.BoxSpace.shortCircuit;
import static net.thevpc.scholar.hadruwaves.mom.SourceFactory.createPlanarSource;

public class SimpleLineAntennaTest {

    static {
        Nuts.require();
    }

    @Test
    public void testLineAntenna() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);
        
        // Dimensions
        double a = 100 * MM;
        double b = 100 * MM;
        double f = 1 * GHZ;
        int modes = 10000;

        Domain box = Domain.ofBounds(0, a, 0, b);

        // Line antenna: 20mm long, 1mm wide, centered
        double L = 20 * MM;
        double W = 1 * MM;
        Domain lineBox = Domain.ofBounds((a - L) / 2, (a + L) / 2, (b - W) / 2, (b + W) / 2);

        // Source: first 2mm of the line
        double s = 2 * MM;
        Domain sourceBox = Domain.ofBounds((a - L) / 2, (a - L) / 2 + s, (b - W) / 2, (b + W) / 2);

        // MoM Structure: EEEE (closed box)
        // bottom: short circuit (metallic ground) at 1.6mm
        // top: matched load (infinite vacuum)
        MomStructure str = MomStructure.EEEE(box, f, modes,
                shortCircuit(Material.substrate("substrate", 2.2), 1.6 * MM),
                matchedLoad(Material.VACUUM));

        // Set Source
        str.setSources(createPlanarSource(1, Complex.of(50), Axis.X, sourceBox));

        // Set Test Functions (Basis functions)
        UserSinePattern p = new UserSinePattern(5, CellBoundaries.DDxDDy, null);
        GpAdaptiveMesh am = new GpAdaptiveMesh(GeometryFactory.createPolygonList(lineBox.toGeometry()), p, null, new MeshAlgoRect());

        str.setTestFunctions(am);

        ModeInfo[] boxModes = str.modes();
        DoubleToVector[] basisFunctions = str.testFunctions().arr();

        int index = 0;
        for (ModeInfo mode : boxModes) {
            NOut.println(NMsg.ofC("[%s] mode %s, function %s ", index, mode, mode.fn));
            index++;
            if (index > 10) {
                break;
            }
        }

        index = 0;
        for (DoubleToVector gp : basisFunctions) {
            NOut.println(NMsg.ofC("[%s] basis %s ", index, gp));
            index++;
        }
        ComplexMatrix sp = str.getTestModeScalarProducts();
        for (int pq = 0; pq < basisFunctions.length; pq++) {
            if (pq >= 10) {
                //just a snapshot!
                break;
            }
            for (int mn = 0; mn < boxModes.length; mn++) {
                if (mn >= 10) {
                    //just a snapshot!
                    break;
                }
                NOut.println(NMsg.ofC("<f" + mn + ",g" + pq + ">=<" + boxModes[mn].getMode() + ",g" + pq + "> = " + sp.get(pq, mn)));
            }
        }

        NOut.println(NMsg.ofC("matrix A : %s",str.matrixA().evalMatrix()));
        NOut.println(NMsg.ofC("matrix B : %s",str.matrixB().evalMatrix()));

        // Evaluate Input Impedance (Zin)
        Complex zin = str.inputImpedance().evalComplex();

        System.out.println("Zin = " + zin);

        // Validate Zin is not NaN or Zero
        Assertions.assertNotNull(zin);
        Assertions.assertFalse(zin.isNaN(), "Zin should not be NaN");
        Assertions.assertTrue(zin.absdbl() > 0, "Zin magnitude should be greater than zero");
        Plot.title("Jx").plot(str.current().evalMatrix(Axis.X, box.dtimes(100)));
        Object o=new Object();
        synchronized (o){
            try {
                o.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
