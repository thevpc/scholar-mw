package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.FunctionFactory;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.ModeType;
import static net.vpc.scholar.hadrumaths.Maths.*;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 15 mai 2007 21:42:59
 */
public final class EMEMPattern extends AbstractGpFnPatternPQ {


    public EMEMPattern(int complexity, ModeType... modes) {
        super(complexity, modes);
    }

    protected boolean doAcceptIndexes(ModeType mode, int m, int n) {
        if (!doAcceptMode(mode)) {
            return false;
        }
        if (mode == ModeType.TM) {
            return n != 0;
        } else if (mode == ModeType.TE) {
            return m != 0;
        } else if (mode == ModeType.TEM) {
            return m == 0 && n == 0;
        } else {
            throw new RuntimeException("impossible");
        }
    }

    public DoubleToVector createFunction(int index, ModeType mode, int p, int q, Domain domain, Domain globalDomain) {
        int n = q;
//            Mode mode = i.mode;
        int m = p;
        if (!doAcceptIndexes(mode, m, n)) {
            return null;
        }
        double a = domain.xwidth();
        double b = domain.ywidth();
        DoubleToVector fn;
        if (m == 0 && n == 0) {//mode TEM
            fn = Maths.vector(

                    (FunctionFactory.DZEROXY),
                    (expr(1.0 / Math.sqrt(a * b), domain))
            );
            fn=(DoubleToVector) fn.setTitle("EMEM" + mode + m + "" + n);
        } else {
            double u = 1;
            if (m != 0 && n != 0) {
                u = 2;
            }

//        double cached_PI_divide_WIDTH = Math.PI / a;
//        double cached_PI_divide_HEIGHT = Math.PI / b;
            double n_bAmp = 0;
            double m_aAmp = 0;
            if (m != 0 || n != 0) {
                double n_b = n / b;
                double m_a = m / a;
                double coeff = Math.sqrt(
                        2 * u /
                                (a * b * (n_b * n_b + m_a * m_a)))/PI;
                n_bAmp = n_b * coeff;
                m_aAmp = m_a * coeff;
            }
            double nx;
            double ny;
            if (mode == ModeType.TE) {
                nx = n_bAmp;
                ny = -m_aAmp;
            } else {
                nx = -m_aAmp;
                ny = -n_bAmp;
            }
            DoubleToDouble fx = FunctionFactory.sinXsinY(
                    nx,
                    /**cos**/(m * Math.PI / a), -(m * Math.PI / a) * domain.xmin(),
                    /**cos**/(n * Math.PI / b), -(n * Math.PI / b) * domain.ymin(),
                    domain
            ).setTitle("nx.sin(" + m + "PIx/a)sin(" + n + "PIx/b)").toDD();

            DoubleToDouble fy = FunctionFactory.cosXcosY(
                    ny,
                    /**cos**/(m * Math.PI / a), -(m * Math.PI / a) * domain.xmin(),
                    /**cos**/(n * Math.PI / b), -(n * Math.PI / b) * domain.ymin(),
                    domain
            ).setTitle("nx.cos(" + m + "PIx/a)cos(" + n + "PIx/b)").toDD();

            //if(fx.isNull() && fy.isNull()){
            //    System.out.println("null for "+i);
            //}
            fn = Maths.vector(fx, fy);
            fn=(DoubleToVector) fn.setTitle("EMEM" + m + "" + n);
        }
        return fn;
    }

    public boolean isAttachX() {
        return false;
    }

    public boolean isAttachY() {
        return false;
    }
}
