package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.CExp;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.BoxSpace;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static net.vpc.scholar.hadrumaths.Maths.sqr;

/**
 * Created by vpc on 5/20/17.
 */
public class PeriodicBoxModes extends BoxModes {
    double alphax;
    double betay;

    public PeriodicBoxModes(WallBorders border, Domain domain, Axis axis, double alphax, double betay) {
        super(border, domain, axis);
        this.alphax = alphax;
        this.betay = betay;
        this.ma = 1;
        this.mb = 0;
        this.na = 1;
        this.nb = 1;
        this.allowedModes = new ModeType[]{ModeType.TM, ModeType.TE, ModeType.TEM};
    }

    public Complex getGamma(ModeIndex i, double freq, BoxSpace space) {
        return Complex.valueOf(Maths.sqr(2 * i.getM() * Math.PI / domain.xwidth()) + Maths.sqr(2 * i.getN() * Math.PI / domain.ywidth()) - Maths.sqr(Physics.K0(freq)) * space.getEpsr()).sqrt();
    }

    public double getCutoffFrequency(ModeIndex i) {
        int m = i.m;
        int n = i.n;
        if (m == 0 && n == 0) {
            return 0;
        }
        //Mode mode = i.mode;
        double a = domain.xwidth();
        double b = domain.ywidth();
        double k0 = sqrt(sqr(2 * m * PI / a) + sqr(2 * n * PI / b));
        return Physics.freqByK0(k0);
    }

    @Override
    public DoubleToVector getFunction(ModeIndex i) {
        int m = i.getM();
        int n = i.getN();
        ModeType mode = i.getModeType();
        double a = domain.xwidth();
        double b = domain.ywidth();
        double bx = 2 * m * PI / a;
        double by = 2 * n * PI / b;
        double Bx = bx / sqrt(bx * bx + by * by);
        double By = by / sqrt(bx * bx + by * by);
        double sqrt_ab = sqrt(a * b);
//        double c = 1 / (sqrt(a * b * ((m * m) / (a * a) + (n * n) / (b * b))));
        String name = "Period" + i;
        if (mode == ModeType.TE) {
            if (m == n && n == 0) {
                DoubleToVector ff = Maths.vector(
                        FunctionFactory.CZEROXY,
                        (new DoubleValue(-1 / sqrt_ab, domain)));
                ff = (DoubleToVector) ff.setName(name);
                return ff;
            } else {
                //TODO verifier ca stp
                DoubleToVector ff = Maths.vector(
                        new CExp(By / sqrt_ab, -bx, -by, domain),
                        new CExp(-Bx / sqrt_ab, -bx, -by, domain));
                ff = (DoubleToVector) ff.setName(name);
                return ff;
//                return new CFunctionXY2D(name,
//                        new CFunctionXY(new DCstFunctionXY(1 / sqrt_ab, domain)),
//                        CFunctionXY.ZERO
//                );
            }
        } else {
            if (m == n && n == 0) {
                DoubleToVector ff = Maths.vector(
                        (new DoubleValue(1 / sqrt_ab, domain)),
                        FunctionFactory.CZEROXY);
                ff = (DoubleToVector) ff.setName(name);
                return ff;
            } else {
                DoubleToVector ff = Maths.vector(
                        new CExp(-Bx / sqrt_ab + alphax, -bx, -by, domain),
                        new CExp(-By / sqrt_ab + betay, -bx, -by, domain));
                ff = (DoubleToVector) ff.setName(name);
                return ff;
            }
        }
    }

    public boolean accept(ModeIndex i) {
        return true;
    }

    public ModeIterator iterator() {
        return new PeriodicModeIterator(allowedModes, axis);
    }
}
