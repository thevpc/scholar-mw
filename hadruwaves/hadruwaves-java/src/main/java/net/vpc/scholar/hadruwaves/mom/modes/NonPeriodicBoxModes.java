package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.mom.BoxSpace;

import java.util.Iterator;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static net.vpc.scholar.hadrumaths.Maths.sqr;

/**
 * Created by vpc on 5/20/17.
 */
public abstract class NonPeriodicBoxModes extends BoxModes {
    public NonPeriodicBoxModes(WallBorders borders, Domain domain, Axis axis) {
        super(borders, domain, axis);
    }


    public Complex getGamma(ModeIndex i, double freq, BoxSpace space) {
        //pour le mode fondamental firstBoxSpaceGamma=w/c=omega*sqrt(mu0*epsilon0)
        double m = this.ma * i.getM() + this.mb;
        double n = this.na * i.getN() + this.nb;
        double a = domain.xwidth();
        double b = domain.ywidth();
//        return Complex.valueOf(sqr(m * PI / a) + sqr(n * PI / b) - Maths.sqr(Physics.K0(freq)) * space.getEpsr()).sqrt();
        Complex eps = space.getEpsrc(freq);
        double mu = Maths.U0;
        return eps.mul(mu *Maths.sqr(2*PI*freq)).neg().plus(sqr(m * PI / a) + sqr(n * PI / b))
               .sqrt()
               ;
    }


    public double getCutoffFrequency(ModeIndex i) {
        double m = this.ma * i.m + this.mb;
        double n = this.na * i.n + this.nb;
        //Mode mode = i.mode;
        double a = domain.xwidth();
        double b = domain.ywidth();
        double k0Coupure = sqrt(sqr(m * PI / a) + sqr(n * PI / b));
        return Physics.freqByK0(k0Coupure);
    }

    public Iterator<ModeIndex> iterator() {
        return new PositiveModeIterator2(allowedModes, axis, borders);
    }


    public DoubleToVector getFunction(ModeIndex i) {
        double m = this.ma * i.getM() + this.mb;
        double n = this.na * i.getN() + this.nb;
        if (n < 0) {
            throw new IllegalArgumentException(borders + " : n < 0 " + n + " for " + i);
        }
        if (m < 0) {
            throw new IllegalArgumentException(borders + " : m < 0 " + m + " for " + i);
        }
        ModeType mode = i.getModeType();
        double a = domain.xwidth();
        double b = domain.ywidth();

        Expr fx = null;
        Expr fy = null;
        switch (mode) {
            case TEM: {
                if (m == 0 && n == 0) {//mode TEM
                    fx = getFctXTEM(domain);
                    fy = getFctYTEM(domain);
                } else {
                    throw new IllegalArgumentException("Unsupported " + i + " Mode for " + borders);
                }
                break;
            }
            case TE: {
                double u = 1;
                if (m != 0 && n != 0) {
                    u = 2;
                }
                //UPDATE 2017 07 27 (missing PI)
                //UPDATE 2017 08 11 THERE IS NO MISSING PI !!!
                double n_b = n / b;
                double m_a = m / a;
                if (n == 0 && m == 0) {
                    System.err.println("How come " + borders + "(" + i.getModeType() + "," + i.getM() + "," + i.getN() + ") is NaN");
                }
                double coeff = sqrt(2 * u / (a * b * (n_b * n_b + m_a * m_a))) ;
                double n_bAmp = n_b * coeff;
                double m_aAmp = m_a * coeff;
                double nx;
                double ny;

                nx = n_bAmp;
                ny = -m_aAmp;
                fx = nx == 0 ? Maths.DZEROXY : getFctX(domain, m, n, a, b, nx, ny);
                fy = ny == 0 ? Maths.DZEROXY : getFctY(domain, m, n, a, b, nx, ny);
                break;
            }
            case TM: {
                double u = 1;
                if (m != 0 && n != 0) {
                    u = 2;
                }
                //UPDATE 2017 07 27 (missing PI)
                //UPDATE 2017 08 11 THERE IS NO MISSING PI !!!
                double n_b = n / b;
                double m_a = m / a;
                double coeff = sqrt(2 * u / (a * b * (n_b * n_b + m_a * m_a))) ;
                if (n == 0 && m == 0) {
                    System.err.println("How come " + borders + "(" + i.getModeType() + "," + i.getM() + "," + i.getN() + ") is NaN");
                }
                double n_bAmp = n_b * coeff;
                double m_aAmp = m_a * coeff;
                double nx;
                double ny;

                nx = -m_aAmp;
                ny = -n_bAmp;
                fx = nx == 0 ? Maths.DZEROXY : getFctX(domain, m, n, a, b, nx, ny);
                fy = ny == 0 ? Maths.DZEROXY : getFctY(domain, m, n, a, b, nx, ny);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported " + i + " Mode for " + borders);
            }
        }

        if (fx.isZero() && fy.isZero()) {
            System.err.println("How come " + borders + "(" + i.getModeType() + "," + i.getM() + "," + i.getN() + ") is zero");
        }
        if ((fx.isNaN() || fy.isNaN())) {
            System.err.println("How come " + borders + "(" + i.getModeType() + "," + i.getM() + "," + i.getN() + ") is NaN");
        }
        if ((fx.isInfinite() || fy.isInfinite())) {
            System.err.println("How come " + borders + "(" + i.getModeType() + "," + i.getM() + "," + i.getN() + ") is Infinite");
        }
        DoubleToVector fn = null;
        fn = Maths.vector(fx, fy).setTitle(borders.toString() + "/" + i).toDV();
        return fn;
    }

    protected abstract Expr getFctY(Domain domain, double m, double n, double a, double b, double nx, double ny);
//        {
//            return cosXsinY(ny, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
//        }

    protected abstract Expr getFctX(Domain domain, double m, double n, double a, double b, double nx, double ny);
//        {
//            return sinXcosY(nx, m * PI / a, -(m * PI / a) * domain.xmin(), n * PI / b, -(n * PI / b) * domain.ymin(), domain);
//        }

    protected Expr getFctXTEM(Domain domain) {
        return Maths.DZEROXY;
    }
//        {
//            double a = domain.xwidth();
//            double b = domain.ywidth();
//
//        }

    protected Expr getFctYTEM(Domain domain) {
        return Maths.DZEROXY;
    }
//        {
//            double a = domain.xwidth();
//            double b = domain.ywidth();
//
//        }
}
