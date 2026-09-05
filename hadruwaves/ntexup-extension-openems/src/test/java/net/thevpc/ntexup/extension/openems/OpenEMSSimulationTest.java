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
        Assertions.assertNotNull(xml);
        Assertions.assertTrue(xml.contains("<openEMS>"));
        Assertions.assertTrue(xml.contains("<ContinuousStructure"));
        Assertions.assertTrue(xml.contains("<RectilinearGrid"));
        Assertions.assertTrue(xml.contains("<LumpedElement"));
        Assertions.assertTrue(xml.contains("<ProbeBox"));
    }

    @Test
    public void testOpenEMSRunIfAvailable() {
        File bin = new File("/usr/bin/openEMS");
        if (!bin.exists()) {
            System.out.println("openEMS binary not found, skipping execution test");
            return;
        }

        OpenEMSParser.OpenEMSModelInfo info = new OpenEMSParser.OpenEMSModelInfo();
        info.frequency = 2.4e9;
        info.fc = 2.0e9;
        info.numberOfTimesteps = 500;

        info.groundBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.03, -0.03, -0.001635,
                0.03, 0.03, -0.0016,
                "ground", "ground"
        ));

        info.substrateBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.03, -0.03, -0.0016,
                0.03, 0.03, 0.0,
                "substrate", "substrate"
        ));

        info.antennaBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.0015, -0.015, 0.0,
                0.0015, 0.015, 0.000035,
                "antenna", "antenna"
        ));

        info.sourceBoxes.add(new OpenEMSParser.OpenEMSBox(
                -0.0015, -0.015, 0.0,
                0.0015, -0.013, 0.000035,
                "source", "source"
        ));

        OpenEMSStrNTxSimulationPlan plan = new OpenEMSStrNTxSimulationPlan("test1", "openems-test", null);
        plan.modelInfo = info;

        OpenEMSStrNTxSimulationPlan.OpenEMSRunData runData = plan.runSimulation();
        Assertions.assertNotNull(runData);
        Assertions.assertTrue(runData.v.length > 0);
        Assertions.assertTrue(runData.i.length > 0);

        Complex s11 = plan.computeS11(2.4e9);
        Complex zin = plan.computeZin(2.4e9);
        System.out.println("Computed S11: " + s11 + " (|S11| = " + s11.absdbl() + ")");
        System.out.println("Computed Zin: " + zin);

        Assertions.assertNotNull(s11);
        Assertions.assertNotNull(zin);
        Assertions.assertFalse(Double.isNaN(s11.absdbl()));
        Assertions.assertFalse(Double.isNaN(zin.absdbl()));
    }
}
