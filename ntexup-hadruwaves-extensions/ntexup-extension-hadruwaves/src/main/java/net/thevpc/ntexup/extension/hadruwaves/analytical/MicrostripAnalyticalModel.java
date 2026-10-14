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

    public static double calcWFromZ0(double z0, double h, double er) {
        double a = (z0 / 60.0) * Math.sqrt((er + 1.0) / 2.0) + ((er - 1.0) / (er + 1.0)) * (0.23 + 0.11 / er);
        if (a > 1.52) {
            return h * (8.0 * Math.exp(a) / (Math.exp(2.0 * a) - 2.0));
        } else {
            double b = (ETA0 * Math.PI) / (2.0 * z0 * Math.sqrt(er));
            return h * ((2.0 / Math.PI) * (b - 1.0 - Math.log(2.0 * b - 1.0) + ((er - 1.0) / (2.0 * er)) * (Math.log(b - 1.0) + 0.39 - 0.61 / er)));
        }
    }

    public static Complex tline(double z0, double length, double f, double h, double er, Complex zLoad) {
        if (length <= 0) {
            return zLoad;
        }
        double w = calcWFromZ0(z0, h, er);
        double u = w / h;
        double epsEff = epsEff0(u, er);
        double vp = C / Math.sqrt(epsEff);
        double beta = 2.0 * Math.PI * f / vp;
        double bl = beta * length;
        double tanBl = Math.tan(bl);
        Complex num = zLoad.plus(Complex.of(0, z0 * tanBl));
        Complex den = Complex.of(1.0).plus(Complex.of(0, tanBl / z0).mul(zLoad));
        return num.div(den);
    }

    public static Complex computeZin(AnalyticalModelInfo info, double f) {
        if (info.isResonator) {
            double uRes = info.resW / info.height;
            double epsEffRes = epsEff0(uRes, info.epsilonR);
            double dL = deltaLength(info.height, uRes, info.epsilonR, epsEffRes);
            double notchFraction = info.resL > 0 ? (info.insetDepth / info.resL) : 0.0;
            double notchFactor = notchFraction * (1.0 - notchFraction);
            double leff = info.resL + 2.0 * dL + info.insetDepth * notchFactor;
            double fr = C / (2.0 * leff * Math.sqrt(epsEffRes));

            double k0 = 2.0 * Math.PI * fr / C;
            double lam0 = C / fr;
            double grad = (info.resW / (120.0 * lam0)) * (1.0 - Math.pow(k0 * info.height, 2.0) / 24.0);
            double redge = 1.0 / (2.0 * Math.max(1e-6, grad));
            double cosVal = Math.cos(Math.PI * info.insetDepth / info.resL);
            double rin = redge * Math.pow(cosVal, 2.0);

            double qDiel = 1.0 / Math.max(1e-6, info.lossTangent);
            double qCond = info.height * Math.sqrt(Math.PI * fr * 4.0 * Math.PI * 1e-7 * 5.8e7);
            double cPatch = 8.854187817e-12 * info.epsilonR * info.resW * info.resL / (2.0 * info.height);
            double qRad = 2.0 * Math.PI * fr * cPatch * redge;
            double q = 1.0 / ((1.0 / qRad) + (1.0 / qDiel) + (1.0 / qCond));

            double deltaF = (f - fr) / fr;
            Complex zPatch = Complex.of(rin).div(Complex.of(1.0, 2.0 * q * deltaF));

            if (info.feedLength > 0) {
                double uFeed = info.feedWidth / info.height;
                double epsFeed = epsEff0(uFeed, info.epsilonR);
                double vpFeed = C / Math.sqrt(epsFeed);
                double beta = 2.0 * Math.PI * f / vpFeed;
                double bl = beta * info.feedLength;
                double tanBl = Math.tan(bl);
                double z0F = z0QuasiStatic(uFeed, epsFeed);
                Complex num = zPatch.plus(Complex.of(0, z0F * tanBl));
                Complex den = Complex.of(1.0).plus(Complex.of(0, tanBl / z0F).mul(zPatch));
                return num.div(den);
            }
            return zPatch;
        }

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
