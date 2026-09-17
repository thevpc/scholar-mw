package net.thevpc.ntexup.extension.openems;

import net.thevpc.nuts.Nuts;
import net.thevpc.scholar.hadrumaths.Complex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class OpenEMSSimulationTest {

    @BeforeAll
    public static void init() {
        Nuts.require();
    }

    @Test
    public void testXmlGeneration() {
        OpenEMSParser.OpenEMSModelInfo info = new OpenEMSParser.OpenEMSModelInfo();
        info.frequency = 2.4e9;
        info.fc = 2.0e9;
        info.numberOfTimesteps = 500;

        // Ground (-50mm to +50mm, Y: -50mm to +50mm, Z: -1.635mm to -1.6mm)
        info.groundBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.05, -0.05, -0.001635,
                0.05, 0.05, -0.0016,
                "ground", "ground"
        ));

        // Substrate (-50mm to +50mm, Y: -50mm to +50mm, Z: -1.6mm to 0)
        info.substrateBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.05, -0.05, -0.0016,
                0.05, 0.05, 0.0,
                "substrate", "substrate"
        ));

        // Antenna line (X: -1.5mm to 1.5mm, Y: -20mm to 20mm, Z: 0 to 0.035mm)
        info.antennaBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.0015, -0.02, 0.0,
                0.0015, 0.02, 0.000035,
                "antenna", "antenna"
        ));

        // Source port at start of line (X: -1.5mm to 1.5mm, Y: -20mm to -18mm, Z: 0 to 0.035mm)
        info.sourceBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.0015, -0.02, 0.0,
                0.0015, -0.018, 0.000035,
                "source", "source"
        ));

        String xml = OpenEMSParser.generateOpenEMSXml(info);
        System.out.println("=== XML START ===");
        System.out.println(xml);
        System.out.println("=== XML END ===");
        Assertions.assertNotNull(xml);
        Assertions.assertTrue(xml.contains("<openEMS>"));
        Assertions.assertTrue(xml.contains("<ContinuousStructure"));
        Assertions.assertTrue(xml.contains("<RectilinearGrid"));
        Assertions.assertTrue(xml.contains("<LumpedElement"));
        Assertions.assertTrue(xml.contains("<ProbeBox"));
    }

    @Test
    public void testOpenEMSRunIfAvailable() {
        OpenEMSParser.OpenEMSModelInfo info = new OpenEMSParser.OpenEMSModelInfo();
        info.frequency = 3.4e9;
        info.fc = 2.0e9;
        info.numberOfTimesteps = 25000;
        info.endCriteria = 1e-4;

        double w = 0.0031;
        double l = 0.030;
        double h = 0.0016;
        double h0 = 0.000035;

        info.groundBoxes.add(new OpenEMSParser.OpenEMSBox(
                -30, -30, -1.635,
                30, 30, -1.6,
                "ground", "ground"
        ));

        info.substrateBoxes.add(new OpenEMSParser.OpenEMSBox(
                -30, -30, -1.6,
                30, 30, 0.0,
                "substrate", "substrate"
        ));

        info.antennaBoxes.add(new OpenEMSParser.OpenEMSBox(
                -1.55, -15.0, 0.0,
                1.55, 15.0, 0.035,
                "antenna", "antenna"
        ));

        info.sourceBoxes.add(new OpenEMSParser.OpenEMSBox(
                -1.55, -15.0, 0.0,
                1.55, -13.0, 0.035,
                "source", "source"
        ));

        String xml = OpenEMSParser.generateOpenEMSXml(info);
        System.out.println("=== GENERATED XML ===");
        System.out.println(xml);
        System.out.println("=== END XML ===");

        OpenEMSStrNTxSimulationPlan plan = new OpenEMSStrNTxSimulationPlan("test1", "openems-test", null);
        plan.modelInfo = info;

        System.out.println("Running OpenEMS simulation...");
        long t0 = System.currentTimeMillis();
        OpenEMSStrNTxSimulationPlan.OpenEMSRunData runData = plan.runSimulation();
        System.out.println("Finished OpenEMS simulation in " + (System.currentTimeMillis() - t0) + " ms");
        Assertions.assertNotNull(runData);

        System.out.printf("%-10s | %-30s | %-10s | %-10s%n", "Freq (GHz)", "Zin", "|S11|", "S11 (dB)");
        System.out.println("------------------------------------------------------------------");
        for (double f = 2.0e9; f <= 5.0e9; f += 0.2e9) {
            Complex zin = plan.computeZin(f);
            Complex s11 = plan.computeS11(f);
            double abs = s11.absdbl();
            double db = 20 * Math.log10(Math.max(1e-12, abs));
            System.out.printf("%-10.3f | %-30s | %-10.4f | %-10.2f dB%n", f / 1e9, zin, abs, db);
        }
    }
}
