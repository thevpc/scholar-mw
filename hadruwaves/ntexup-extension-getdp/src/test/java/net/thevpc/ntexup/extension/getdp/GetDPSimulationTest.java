package net.thevpc.ntexup.extension.getdp;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.Complex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GetDPSimulationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testGetDPSimulation() {
        GetDPModelInfo info = new GetDPModelInfo();
        info.width = 3.1e-3;
        info.length = 30e-3;
        info.height = 1.6e-3;
        info.stubLength = 30e-3;
        info.epsilonR = 4.4;
        info.meshResolution = 0.5;

        GetDPStrNTxSimulationPlan plan = new GetDPStrNTxSimulationPlan("test", "test", null);
        plan.modelInfo = info;

        // Compute S11 across 3.0 to 4.4 GHz
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

    @Test
    public void testGetDPPatchSimulation() {
        GetDPModelInfo info = new GetDPModelInfo();
        info.isPatch = true;
        info.patchWidth = 38.0e-3;
        info.patchLength = 29.4e-3;
        info.height = 1.6e-3;
        info.feedWidth = 3.1e-3;
        info.feedLength = 15.0e-3;
        info.insetDepth = 10.3e-3;
        info.insetGap = 1.5e-3;
        info.epsilonR = 4.4;
        info.meshResolution = 0.5;

        GetDPStrNTxSimulationPlan plan = new GetDPStrNTxSimulationPlan("patch-test", "patch-test", null);
        plan.modelInfo = info;

        System.out.println("Patch Freq (GHz) | Zin                            | |S11|      | S11 (dB)");
        System.out.println("------------------------------------------------------------------------");
        double minS11Db = 0;
        double minFreq = 0;

        for (double f = 2.3e9; f <= 2.5e9 + 1e-6; f += 0.02e9) {
            Complex zin = plan.computeZin(f);
            Complex s11 = plan.computeS11(f);
            double s11Db = 20 * Math.log10(Math.max(1e-12, s11.absDouble()));
            if (s11Db < minS11Db) {
                minS11Db = s11Db;
                minFreq = f;
            }
            System.out.printf("%-16.3f | %-30s | %-10.4f | %-10.2f dB%n",
                    f / 1e9, zin, s11.absDouble(), s11Db);
        }

        System.out.printf("Patch Minimum S11: %.2f dB at %.3f GHz%n", minS11Db, minFreq / 1e9);
        Assertions.assertTrue(minFreq >= 2.36e9 && minFreq <= 2.44e9, "Expected patch resonance near 2.40 GHz, got " + (minFreq / 1e9));
    }
}
