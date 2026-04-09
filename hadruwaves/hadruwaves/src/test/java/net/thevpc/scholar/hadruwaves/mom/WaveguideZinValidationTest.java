package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruwaves.Physics;
import net.thevpc.scholar.hadruwaves.mom.ProjectType;
import net.thevpc.scholar.hadruwaves.mom.BoxSpace;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.ListTestFunctions;
import net.thevpc.scholar.hadruwaves.util.Impedance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static net.thevpc.scholar.hadrumaths.Maths.MM;
import static net.thevpc.scholar.hadrumaths.Maths.GHZ;

public class WaveguideZinValidationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testWaveguideZin() {
        Maths.Config.setCacheEnabled(false);
        Maths.Config.setPersistenceCacheMode(CacheMode.DISABLED);

        double a = 100 * MM;
        double b = 50 * MM;
        double f = 2 * GHZ;

        MomStructure str = new MomStructure();
        str.setDomain(Domain.ofBounds(0, a, 0, b));
        str.setFirstBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        str.setSecondBoxSpace(BoxSpace.matchedLoad(Material.VACUUM));
        str.setFrequency(f);
        str.modeFunctions().setSize(100);
        str.setProjectType(ProjectType.PLANAR_STRUCTURE);

        ModeInfo modeTE10 = str.getMode(ModeType.TE, 1, 0);
        str.setSources(modeTE10.fn);
        str.setTestFunctions(new ListTestFunctions().add(modeTE10.fn));

        Complex zin = str.inputImpedance().evalComplex();
        System.out.println("Calculated Zin: " + zin);

        // Theoretical validation:
        // Modal admittance of TE10 at 2GHz:
        // Y_TE10 = gamma / (j * omega * u0)
        // With matched loads on both sides:
        // Y_total = Y_TE10 (left) + Y_TE10 (right) = 2 * Y_TE10
        // Zin = 1 / Y_total = 0.5 * Z_TE10
        // Z_TE10 = Z0 / sqrt(1 - (fc/f)^2) approx 569.56 Ohms
        // Zin approx 284.78 Ohms
        
        Complex zMode = modeTE10.impedance.impedanceValue(); // This already is 1/(Y1+Y2)
        System.out.println("Theoretical Zin: " + zMode);

        Assertions.assertEquals(zMode.getReal(), zin.getReal(), 1e-6);
        Assertions.assertEquals(zMode.getImag(), zin.getImag(), 1e-6);
    }
}
