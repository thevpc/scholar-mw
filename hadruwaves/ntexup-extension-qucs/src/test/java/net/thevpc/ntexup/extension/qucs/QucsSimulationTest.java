package net.thevpc.ntexup.extension.qucs;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.Complex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class QucsSimulationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testQucsSimulation() {
        QucsModelInfo info = new QucsModelInfo();
        info.epsilonR = 4.4;
        info.lossTangent = 0.02;

        // Substrate: 60mm x 60mm x 1.6mm
        info.substrateBoxes.add(new QucsModelInfo.QucsBox(-30e-3, -30e-3, -1.6e-3, 30e-3, 30e-3, 0.0, "substrate", "substrate"));
        // Ground
        info.groundBoxes.add(new QucsModelInfo.QucsBox(-30e-3, -30e-3, -1.635e-3, 30e-3, 30e-3, -1.6e-3, "ground", "ground"));
        // Line
        info.antennaBoxes.add(new QucsModelInfo.QucsBox(-1.55e-3, -15e-3, 0.0, 1.55e-3, 15e-3, 0.035e-3, "line", "antenna"));
        // Source
        info.sourceBoxes.add(new QucsModelInfo.QucsBox(-1.55e-3, -15e-3, 0.0, 1.55e-3, -13e-3, 0.035e-3, "source", "source"));

        QucsStrNTxSimulationPlan plan = new QucsStrNTxSimulationPlan("test", "test", null);
        plan.modelInfo = info;

        System.out.println("Freq (GHz) | Zin                            | |S11|      | S11 (dB)");
        System.out.println("------------------------------------------------------------------");
        double minS11Db = 0;
        double minFreq = 0;

        for (double f = 3.0e9; f <= 4.4e9 + 1e-6; f += 0.05e9) {
            Complex zin = plan.computeZin(f);
            Complex s11 = plan.computeS11(f);
            double s11Db = 20 * Math.log10(Math.max(1e-12, s11.absDouble()));
            if (s11Db < minS11Db) {
                minS11Db = s11Db;
                minFreq = f;
            }
            System.out.printf("%-10.3f | %-30s | %-10.4f | %-10.2f dB%n",
                    f / 1e9, zin, s11.absDouble(), s11Db);
        }

        System.out.printf("Minimum S11: %.2f dB at %.3f GHz%n", minS11Db, minFreq / 1e9);
        Assertions.assertTrue(minFreq >= 3.9e9 && minFreq <= 4.1e9, "Expected resonance near 4.0 GHz, got " + (minFreq / 1e9));
    }
}
