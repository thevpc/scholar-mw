package net.thevpc.scholar.mentoring.extra.java;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.symbolic.CustomDDFunctionX;
import net.thevpc.scholar.hadruplot.Plot;

import java.lang.Double;

import static net.thevpc.scholar.hadrumaths.Maths.*;

public class TAG20200912 implements Cloneable {
    double f = 3E9;
    double h = 0.1197E-3;
    double epsr = 10.2;
    double Zcref = 15.656;
    double c1ref = 0.1197E-3;

    double c1_lowerBound = 1E-18;
    int c1_upperBound = 3;
    double c1_step = 1E-7;
    double zc_precision = 1E-2;

    public static void main(String[] args) {
        TAG20200912 t = new TAG20200912();
        t.plot();
//        System.out.println("z=" + t.zdirect(t.c1ref)+" expected "+t.Zcref);
//        double c1_=t.findC(t.Zcref);
//        System.out.println("c1=" + t.findC(t.Zcref)+" expected "+t.c1ref+" that gives z="+t.zdirect(c1_));
        System.out.printf("%-8s;%-8s\n","Zc","w");
        for (double z0 : new double[]{79.16,48.79,28.18,10.35,10.62,202.25,113.99,4446.3,496.16,54952,14676,41133,5757,50}) {
            double c0=t.findC(z0);
            System.out.printf("%-8s;%-8s\n",z0,c0);
        }
    }

    double findC(double z) {
        return findFirstRootByStep(
                x -> zdirect(x) - z, c1_lowerBound, c1_upperBound, c1_step, zc_precision
        );
    }
    private static int sign(double x) {
        return (x < 0.0) ? -1 : (x > 0.0) ? 1 : 0;
    }

    public static double findFirstRootByCount(D2DFunction f, double lowerBound,
                                       double upperBound, int steps, double precision) {
        double step = (upperBound - lowerBound) / steps;
        return findFirstRootByStep(f,lowerBound,upperBound,step,precision);
    }

    public static double findFirstRootByStep(D2DFunction f, double lowerBound,
                                       double upperBound, double step, double precision) {
        double x = lowerBound, ox = x;
        double y = f.f(x), oy = y;
        int s = sign(y), os = s;
        int cc=0;
        for (; x <= upperBound; x = lowerBound+cc*step) {
            s = sign(y = f.f(x));
            if (s == 0 || abs(y) < precision) {
                return x;
            } else if (s != os) {
                double dx = x - ox;
                double dy = y - oy;
                //double cx = x - dx * (y / dy);
                //System.out.println("~" + cx);
            }
            ox = x;
            oy = y;
            os = s;
            cc++;
        }
        return Double.NaN;
    }

    void plot() {
        Plot.xsamples(20000)
                .title("Zc(w)")
                .plot(mul(
                        define("zdirect", (CustomDDFunctionX) this::zdirect)
//                        , Domain.ofBounds(c1_lowerBound, c1_upperBound))
                        , Domain.ofBounds(1E-8, 1E-2))
                );
    }


    double zdirect(double c1) {
        double epse = 0;
        double lambda0 = C / f;
        double Zci = Double.NaN;
        try {
            if (c1 <= h) {
                epse = (epsr + 1) / 2 + (epsr - 1) / 2 * ((1 + 12 * h / c1) + 0.04 * sqr(1 - c1 / h));
                Zci = 60 / sqrt(epse) * log(8 * h / c1 + c1 / (4 * h));
            } else {
                epse = (epsr + 1) / 2 + (epsr - 1) / 2 * (1 / sqrt(((1 + 12 * h / c1))));
                Zci = 120 * PI / sqrt(epse) / (c1 / h + 1.393 + 0.667 * log(c1 / h + 1.444));
            }
        } catch (Exception ME) {
            Zci = Double.NaN;
        }
        return Zci;
    }

    public interface D2DFunction {
        public double f(double x);
    }
}
