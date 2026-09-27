package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.ntexup.extension.mwsimulator.MicrostripCircuitModel;

public class TestDeembed {
    public static void main(String[] args) {
        // At 6.65 GHz: Zin_mom = 240.86 - 40.04j
        // If this is at the delta gap, let's de-embed back through negative length -6mm or -5.3mm
        double h = 1.6e-3;
        double er = 4.4;
        double wf = 3.1e-3;
        double z0 = MicrostripCircuitModel.calcZ0(wf, h, er);
        System.out.printf("Microstrip Z0 = %.2f Ohm%n", z0);

        // Data from TestArrayRWGMom sweep
        double[][] data = {
            {6.350e9, 341.69, -1045.83},
            {6.400e9, 260.42,  -759.91},
            {6.450e9, 211.32,  -533.01},
            {6.500e9, 218.58,  -440.34},
            {6.550e9, 207.03,  -288.14},
            {6.600e9, 215.20,  -161.88},
            {6.650e9, 240.86,   -40.04},
            {6.700e9, 292.83,    86.63},
            {6.750e9, 392.74,   222.20},
            {6.800e9, 585.89,   350.18}
        };

        System.out.println("--- Transmission Line Current De-embedding ---");
        // Effective permittivity from Hammerstad formula:
        double u = wf / h;
        double epsEff = (er + 1.0) / 2.0 + (er - 1.0) / 2.0 / Math.sqrt(1.0 + 12.0 / u);
        double f = 6.65e9;
        double k0 = 2.0 * Math.PI * f / 2.99792458e8;
        double beta = k0 * Math.sqrt(epsEff);
        double y1 = -25.0e-3;
        double y2 = -23.5e-3;
        MicrostripCircuitModel.ComplexNum I1 = new MicrostripCircuitModel.ComplexNum(0.00272, 0.00733);
        MicrostripCircuitModel.ComplexNum I2 = new MicrostripCircuitModel.ComplexNum(0.00305, 0.00373);

        // e1 = exp(-j beta y1), e2 = exp(-j beta y2)
        MicrostripCircuitModel.ComplexNum e1p = new MicrostripCircuitModel.ComplexNum(Math.cos(-beta * y1), Math.sin(-beta * y1));
        MicrostripCircuitModel.ComplexNum e1m = new MicrostripCircuitModel.ComplexNum(Math.cos(beta * y1), Math.sin(beta * y1));
        MicrostripCircuitModel.ComplexNum e2p = new MicrostripCircuitModel.ComplexNum(Math.cos(-beta * y2), Math.sin(-beta * y2));
        MicrostripCircuitModel.ComplexNum e2m = new MicrostripCircuitModel.ComplexNum(Math.cos(beta * y2), Math.sin(beta * y2));

        // Determinant = e1p * e2m - e1m * e2p = exp(-j beta(y1-y2)) - exp(j beta(y1-y2)) = -2j sin(beta(y1-y2))
        MicrostripCircuitModel.ComplexNum det = e1p.mul(e2m).minus(e1m.mul(e2p));
        MicrostripCircuitModel.ComplexNum Iplus = I1.mul(e2m).minus(I2.mul(e1m)).div(det);
        MicrostripCircuitModel.ComplexNum Iminus = e1p.mul(I2).minus(e2p.mul(I1)).div(det);

        System.out.printf("epsEff = %.4f, beta = %.2f rad/m, lambda_g = %.2f mm%n", epsEff, beta, (2 * Math.PI / beta) * 1000);
        System.out.printf("I+ = %s, |I+| = %.4e A%n", Iplus, Iplus.abs());
        System.out.printf("I- = %s, |I-| = %.4e A%n", Iminus, Iminus.abs());
        double s11db_deembed = 20 * Math.log10(Iminus.abs() / Iplus.abs());
        System.out.printf("Standing wave reflection |Gamma| = %.4f, S11 = %.2f dB%n", Iminus.abs() / Iplus.abs(), s11db_deembed);
    }
}
