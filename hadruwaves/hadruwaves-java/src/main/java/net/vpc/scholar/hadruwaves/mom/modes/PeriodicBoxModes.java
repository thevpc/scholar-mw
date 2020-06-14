package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CExp;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.BoxSpace;

import java.util.Iterator;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static net.vpc.scholar.hadrumaths.Maths.*;
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
        //   return Complex.valueOf(Maths.sqr(2 * i.getM() * Math.PI / domain.xwidth()) + Maths.sqr(2 * i.getN() * Math.PI / domain.ywidth()) - Maths.sqr(Physics.K0(freq)) * space.getEpsr()).sqrt();
        double m = i.getM();
        double n = i.getN();
        double a = domain.xwidth();
        double b = domain.ywidth();

        Complex eps = space.getEpsrc(freq);
        double mu = U0;
        return eps.mul(mu * sqr(2 * PI * freq)).neg().plus(sqr(2 * m * PI / a) + sqr(2 * n * PI / b))
                .sqrt()
                ;
//        return Complex.valueOf(Maths.sqr(2 * i.getM() * Math.PI / domain.xwidth()) + Maths.sqr(2 * i.getN() * Math.PI / domain.ywidth()) - Maths.sqr(Physics.K0(freq)) * space.getEpsr()).sqrt();
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
                Expr ff = vector(
                        Maths.CZEROXY,
                        (expr(-1 / sqrt_ab, domain)));
                return ff.setTitle(name).toDV();
            } else {
                //TODO verifier ca stp
                Expr ff = vector(
                        new CExp(By / sqrt_ab, -bx, -by, domain),
                        new CExp(-Bx / sqrt_ab, -bx, -by, domain));
                return ff.setTitle(name).toDV();
//                return new CFunctionXY2D(name,
//                        new CFunctionXY(new DCstFunctionXY(1 / sqrt_ab, domain)),
//                        CFunctionXY.ZERO
//                );
            }
        } else {
            if (m == n && n == 0) {
                Expr ff = vector(
                        (expr(1 / sqrt_ab, domain)),
                        Maths.CZEROXY);
                return ff.setTitle(name).toDV();
            } else {
                Expr ff = vector(
                        new CExp(-Bx / sqrt_ab + alphax, -bx, -by, domain),
                        new CExp(-By / sqrt_ab + betay, -bx, -by, domain));
                return ff.setTitle(name).toDV();
            }
        }
    }

    public boolean accept(ModeIndex i) {
        return true;
    }

    public Iterator<ModeIndex> iterator() {
        return new PeriodicModeIterator(allowedModes, axis);
    }
}
