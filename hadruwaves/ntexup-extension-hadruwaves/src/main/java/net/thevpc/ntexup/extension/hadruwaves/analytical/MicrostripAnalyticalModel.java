package net.thevpc.ntexup.extension.hadruwaves.analytical;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.Maths;

public class MicrostripAnalyticalModel {
    private static final double ETA0 = 376.730313668; // Impedance of free space (ohms)
    private static final double C = Maths.C; // Speed of light in vacuum (m/s)

    public static double epsEff0(double u, double er) {
        if (u <= 1.0) {
            double a = 1.0 + (1.0 / 49.0) * Math.log((Math.pow(u, 4) + Math.pow(u / 52.0, 2)) / (Math.pow(u, 4) + 0.432))
                    + (1.0 / 18.7) * Math.log(1.0 + Math.pow(u / 18.1, 3));
            double b = 0.564 * Math.pow((er - 0.9) / (er + 3.0), 0.053);
            return (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) * Math.pow(1.0 + 10.0 / u, -a * b);
        } else {
            return (er + 1.0) / 2.0 + ((er - 1.0) / 2.0) / Math.sqrt(1.0 + 12.0 / u);
        }
    }

    public static double z0QuasiStatic(double u, double epsEff) {
        if (u <= 1.0) {
            return (ETA0 / (2.0 * Math.PI * Math.sqrt(epsEff))) * Math.log(8.0 / u + 0.25 * u);
        } else {
            return (ETA0 / Math.sqrt(epsEff)) / (u + 1.393 + 0.667 * Math.log(u + 1.444));
        }
    }

    public static double epsEffDispersion(double f, double h, double u, double er, double epsEff0) {
        double fn = (f * 1e-9) * (h * 1e3); // GHz * mm
        double p1 = 0.27488 + (0.06315 + 0.525 / Math.pow(1.0 + 0.0157 * fn, 20)) * u
                - 0.065683 * Math.exp(-8.7513 * u);
        double p2 = 0.33622 * (1.0 - Math.exp(-0.03442 * er));
        double p3 = 0.0363 * Math.exp(-4.6 * u) * (1.0 - Math.exp(-Math.pow(fn / 3.87, 4.97)));
        double p4 = 1.0 + 2.751 * (1.0 - Math.exp(-Math.pow(er / 15.916, 8)));
        double p = p1 * p2 * Math.pow((0.1844 + p3 * p4) * 10.0 * fn, 1.5763);
        return er - (er - epsEff0) / (1.0 + p);
    }

    public static double deltaLength(double h, double u, double er, double epsEff) {
        return 0.412 * h * ((epsEff + 0.3) / (epsEff - 0.258)) * ((u + 0.264) / (u + 0.8));
    }

    public static Complex computeZin(AnalyticalModelInfo info, double f) {
        double u = info.width / info.height;
        double eps0 = epsEff0(u, info.epsilonR);
        double epsF = info.dispersion ? epsEffDispersion(f, info.height, u, info.epsilonR, eps0) : eps0;
        double z0F = z0QuasiStatic(u, epsF);
        double dL = deltaLength(info.height, u, info.epsilonR, epsF);
        double lTot = info.stubLength + dL;

        double beta = 2.0 * Math.PI * f * Math.sqrt(epsF) / C;
        double X = -z0F / Math.tan(beta * lTot);
        double Rrad = info.z0Ref;

        return Complex.of(Rrad, X);
    }

    public static Complex computeS11(AnalyticalModelInfo info, double f) {
        Complex zin = computeZin(info, f);
        Complex z0 = Complex.of(info.z0Ref);
        return zin.minus(z0).div(zin.plus(z0));
    }
}
