package net.thevpc.ntexup.extension.scuffem;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.Complex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ScuffEMSimulationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testScuffEMLineSimulation() {
        ScuffEMModelInfo info = new ScuffEMModelInfo();
        info.epsilonR = 4.4;
        info.meshResolution = 2.0; // mm

        // Substrate: 60mm x 60mm x 1.6mm
        info.substrateBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-30e-3, -30e-3, -1.6e-3, 30e-3, 30e-3, 0.0, "substrate", "substrate"));
        // Ground
        info.groundBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-30e-3, -30e-3, -1.635e-3, 30e-3, 30e-3, -1.6e-3, "ground", "ground"));
        // Line: w=3.1mm, l=30mm (from y=-15mm to y=+15mm)
        info.antennaBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-1.55e-3, -15e-3, 0.0, 1.55e-3, 15e-3, 0.035e-3, "line", "antenna"));
        // Source port at y=-15mm
        info.sourceBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-1.55e-3, -15e-3, 0.0, 1.55e-3, -13e-3, 0.035e-3, "source", "source"));

        ScuffEMStrNTxSimulationPlan plan = new ScuffEMStrNTxSimulationPlan("line-test", "line-test", null);
        plan.modelInfo = info;

        // Register frequency sweep
        double[] sweepFreqs = new double[15];
        for (int i = 0; i < 15; i++) {
            sweepFreqs[i] = 3.0e9 + i * 0.1e9;
        }
        plan.recordRequestedFrequencies(sweepFreqs);

        System.out.println("SCUFF-EM Line Simulation (Method of Moments / BEM):");
        System.out.println("Freq (GHz) | Zin                            | |Zin| (Ohm)");
        System.out.println("---------------------------------------------------------");
        double minZin = Double.MAX_VALUE;
        double minFreq = 0;

        for (double f : sweepFreqs) {
            Complex zin = plan.computeZin(f);
            double absZin = zin.absDouble();
            if (absZin < minZin) {
                minZin = absZin;
                minFreq = f;
            }
            System.out.printf("%-10.3f | %-30s | %-10.2f%n",
                    f / 1e9, zin, absZin);
        }

        System.out.printf("SCUFF-EM Quarter-Wave Resonance: Minimum |Zin| = %.2f Ohm at %.3f GHz%n", minZin, minFreq / 1e9);
        Assertions.assertTrue(minFreq >= 3.9e9 && minFreq <= 4.1e9, "Expected quarter-wave resonance near 4.0 GHz, got " + (minFreq / 1e9));
    }

    @Test
    public void testScuffEMPatchSimulation() {
        ScuffEMModelInfo info = new ScuffEMModelInfo();
        info.epsilonR = 4.4;
        info.meshResolution = 4.0; // mm for fast test

        // Substrate: 58mm x 59mm x 1.6mm
        info.substrateBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-29e-3, -20e-3, -1.6e-3, 29e-3, 39e-3, 0.0, "substrate", "substrate"));
        // Ground
        info.groundBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-29e-3, -20e-3, -1.635e-3, 29e-3, 39e-3, -1.6e-3, "ground", "ground"));
        // Patch boxes:
        info.antennaBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-19e-3, 0.0, 0.0, -3.05e-3, 10.3e-3, 0.035e-3, "left-flank", "antenna"));
        info.antennaBoxes.add(new ScuffEMModelInfo.ScuffEMBox(3.05e-3, 0.0, 0.0, 19e-3, 10.3e-3, 0.035e-3, "right-flank", "antenna"));
        info.antennaBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-19e-3, 10.3e-3, 0.0, 19e-3, 29.4e-3, 0.035e-3, "patch", "antenna"));
        info.antennaBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-1.55e-3, -15e-3, 0.0, 1.55e-3, 10.3e-3, 0.035e-3, "feedline", "antenna"));
        // Source
        info.sourceBoxes.add(new ScuffEMModelInfo.ScuffEMBox(-1.55e-3, -15e-3, 0.0, 1.55e-3, -14e-3, 0.035e-3, "source", "source"));

        ScuffEMStrNTxSimulationPlan plan = new ScuffEMStrNTxSimulationPlan("patch-test", "patch-test", null);
        plan.modelInfo = info;
        plan.recordRequestedFrequencies(new double[]{2.4e9});

        Complex zin = plan.computeZin(2.4e9);
        System.out.println("SCUFF-EM Patch Simulation at 2.4 GHz: Zin = " + zin);
        Assertions.assertNotNull(zin);
    }
}
