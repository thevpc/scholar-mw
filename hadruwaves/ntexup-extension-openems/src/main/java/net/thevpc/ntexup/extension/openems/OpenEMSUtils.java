package net.thevpc.ntexup.extension.openems;

import net.thevpc.scholar.hadrumaths.Complex;

public class OpenEMSUtils {

    public static Complex fourierTransform(double[] t, double[] signal, double dt, double freq) {
        double real = 0;
        double imag = 0;
        for (int k = 0; k < t.length; k++) {
            double angle = -2.0 * Math.PI * freq * t[k];
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            real += signal[k] * cos;
            imag += signal[k] * sin;
        }
        return Complex.of(real * dt * 2.0, imag * dt * 2.0);
    }

    public static Complex computeVf(OpenEMSStrNTxSimulationPlan.OpenEMSRunData d, double freq) {
        return fourierTransform(d.tv, d.v, d.dt, freq);
    }

    public static Complex computeIf(OpenEMSStrNTxSimulationPlan.OpenEMSRunData d, double freq) {
        double dtI = d.ti.length > 1 ? d.ti[1] - d.ti[0] : d.dt;
        return fourierTransform(d.ti, d.i, dtI, freq);
    }
}
