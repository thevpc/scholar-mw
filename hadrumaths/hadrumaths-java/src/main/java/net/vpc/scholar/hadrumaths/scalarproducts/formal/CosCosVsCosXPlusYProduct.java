package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.CosXPlusY;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static net.vpc.scholar.hadrumaths.Maths.sin;

/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:13:30
 */
final class CosCosVsCosXPlusYProduct implements FormalScalarProductHelper {

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        return true;
    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        CosXCosY f1ok = (CosXCosY) f1;
        CosXPlusY f2ok = (CosXPlusY) f2;
        double d = computeTolerant(domain, f1ok, f2ok, 1E-15);
        if (Double.isNaN(d)) {
            System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<??????>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            double d0 = compute(domain, f1ok, f2ok);
            d = d0;
        }
        return d;
    }
//    public double compute(Domain domain, DFunction f1, DFunction f2) {
//        return new CosCosVsCosCosScalarProductOld2().compute(domain, f1, f2);
//    }


    static double compute(Domain domain, CosXCosY f, CosXPlusY g) {
        double a1 = g.getA();
        double b1 = g.getB();
        double c1 = g.getC();
        double a2 = f.getA();
        double b2 = f.getB();
        double a3 = f.getC();
        double b3 = f.getD();
        double xmin = domain.xmin();
        double xmax = domain.xmax();
        double ymin = domain.ymin();
        double ymax = domain.ymax();

        double v = 0.0;
        if (a1 == 0 && a2 == 0) {
            if (b1 == 0 && a3 == 0) {
                v = cos(c1) * cos(b2) * cos(b3) * (xmax - xmin) * (ymax - ymin);
            } else if (b1 == a3) {
                v = cos(b2) * (xmax - xmin) * (((1.0 / 2.0) * cos(c1 - b3) * (ymax - ymin)) +
                        ((1.0 / (4 * b1)) * (sin(2 * b1 * ymax + c1 + b3) - sin(2 * b1 * ymax + c1 + b3))));

            } else {
                v = (cos(b2) * (xmax - xmin)) * (((1.0 / (2.0 * (b1 - a3))) * (sin((b1 - a3) * ymax + c1 - b3) - sin((b1 - a3) * ymin + c1 - b3))) + ((1.0 / (2.0 * (b1 + a3))) * (sin((b1 + a3) * ymax + c1 + b3) - sin((b1 + a3) * ymin + c1 + b3))));
            }
        } else if (a1 == a2) {
            if (b1 == 0 && a3 == 0) {
                v = ((1.0 / 2.0) * cos(b3) * (ymax - ymin) * (xmax - xmin) * cos(c1 - b2)) +
                        (((1.0 / (4 * a1)) * (cos(b3) * (ymax - ymin))) * (sin(2 * a1 * xmax + c1 + b2) - sin(2 * a1 * xmin + c1 + b2)));
            } else if (b1 == a3) {
                v = ((1.0 / 4.0) * (xmax - xmin) * (ymax - ymin) * cos(b3 - c1 + b2)) +
                        ((1.0 / (8.0 * b1)) * (xmax - xmin) * (sin(2.0 * b1 * ymax + c1 - b2 + b3) - sin(2.0 * b1 * ymin + c1 - b2 + b3))) -
                        ((1.0 / (8.0 * a1)) * (ymax - ymin) * (sin(b3 - 2.0 * a1 * xmax - c1 - b2) - sin(b3 - 2.0 * a1 * xmin - c1 - b2))) +
                        ((1.0 / (16.0 * a1 * b1)) * (
                                cos(2.0 * b1 * ymin + 2.0 * a1 * xmax + b3 + c1 + b2) -
                                        cos(2.0 * b1 * ymax + 2.0 * a1 * xmax + b3 + c1 + b2) -
                                        cos(2.0 * b1 * ymin + 2.0 * a1 * xmin + b3 + c1 + b2) +
                                        cos(2.0 * b1 * ymax + 2.0 * a1 * xmin + b3 + c1 + b2)));

            } else {
                if ((b1 + a3) == 0) {
                    v = ((1.0 / 4.0) * (xmax - xmin) * (ymax - ymin) * cos(b3 + c1 - b2)) -
                            ((1.0 / (8.0 * b1)) * (xmax - xmin) * (sin(b3 - 2.0 * b1 * ymax - c1 + b2) - sin(b3 - 2.0 * b1 * ymin - c1 + b2))) +
                            ((1.0 / (8.0 * a1)) * (ymax - ymin) * (sin(b3 + 2.0 * a1 * xmax + c1 + b2) - sin(b3 + 2.0 * a1 * xmin + c1 + b2))) -
                            ((1.0 / (16.0 * a1 * b1)) * (cos(b3 - 2.0 * b1 * ymax - 2.0 * a1 * xmax - c1 - b2) - cos(b3 - 2.0 * b1 * ymin - 2.0 * a1 * xmax - c1 - b2))) +
                            ((1.0 / (16.0 * a1 * b1)) * (cos(b3 - 2.0 * b1 * ymax - 2.0 * a1 * xmin - c1 - b2) - cos(b3 - 2.0 * b1 * ymin - 2.0 * a1 * xmin - c1 - b2)));

                } else {
                    v = (((1.0 / (4.0 * (a3 - b1))) * (xmax - xmin)) * (sin((a3 - b1) * ymax + b3 - c1 + b2) - sin((a3 - b1) * ymin + b3 - c1 + b2))) +
                            (((1.0 / (4.0 * (a3 + b1))) * (xmax - xmin)) * (sin((a3 + b1) * ymax + b3 + c1 - b2) - sin((a3 + b1) * ymin + b3 + c1 - b2))) -
                            ((1.0 / (8.0 * a1 * (a3 + b1))) * (cos((a3 + b1) * ymax + b3 + 2.0 * a1 * xmax + c1 + b2) - cos((a3 + b1) * ymin + b3 + 2.0 * a1 * xmax + c1 + b2))) +
                            ((1.0 / (8.0 * a1 * (a3 - b1))) * (cos((a3 - b1) * ymax + b3 - 2.0 * a1 * xmax - c1 - b2) - cos((a3 - b1) * ymin + b3 - 2.0 * a1 * xmax - c1 - b2))) +
                            ((1.0 / (8.0 * a1 * (a3 + b1))) * (cos((a3 + b1) * ymax + b3 + 2.0 * a1 * xmin + c1 + b2) - cos((a3 + b1) * ymin + b3 + 2.0 * a1 * xmin + c1 + b2))) -
                            ((1.0 / (8.0 * a1 * (a3 - b1))) * (cos((a3 - b1) * ymax + b3 - 2.0 * a1 * xmin - c1 - b2) - cos((a3 - b1) * ymin + b3 - 2.0 * a1 * xmin - c1 - b2)));
                }
            }
        } else {
            if (b1 == 0 && a3 == 0) {
                v = ((cos(b3) * (ymax - ymin) / (2.0 * (a1 - a2))) * (sin((a1 - a2) * xmax + c1 - b2) - sin((a1 - a2) * xmin + c1 - b2))) +
                        ((cos(b3) * (ymax - ymin) / (2.0 * (a1 + a2))) * (sin((a1 + a2) * xmax + c1 + b2) - sin((a1 + a2) * xmin + c1 + b2)));
            } else if (b1 == a3) {
                if ((a1 + a2) == 0) {
                    v = ((1.0 / 4.0) * (xmax - xmin) * (ymax - ymin) * cos(b3 - c1 - b2)) +
                            ((1.0 / (8.0 * b1)) * (xmax - xmin) * (sin(2.0 * b1 * ymax + b3 + c1 + b2) - sin(2.0 * b1 * ymin + b3 + c1 + b2))) -
                            ((1.0 / (8.0 * a1)) * (ymax - ymin) * (sin(b3 - 2.0 * a1 * xmax - c1 + b2) - sin(b3 - 2.0 * a1 * xmin - c1 + b2))) -
                            ((1.0 / (16.0 * a1 * b1)) * (cos(2.0 * b1 * ymax + b3 + 2.0 * a1 * xmax + c1 - b2) - cos(2.0 * b1 * ymin + b3 + 2.0 * a1 * xmax + c1 - b2))) +
                            ((1.0 / (16.0 * a1 * b1)) * (cos(2.0 * b1 * ymax + b3 + 2.0 * a1 * xmin + c1 - b2) - cos(2.0 * b1 * ymin + b3 + 2.0 * a1 * xmin + c1 - b2)));

                } else {
                    v = ((-1.0 / (8.0 * b1 * (a1 - a2))) * (cos(2.0 * b1 * ymax + b3 + (a1 - a2) * xmax + c1 - b2) - cos(2.0 * b1 * ymin + b3 + (a1 - a2) * xmax + c1 - b2))) +
                            (((-1.0 * (ymax - ymin)) / (4.0 * (a1 - a2))) * (sin(b3 - (a1 - a2) * xmax - c1 + b2))) +
                            ((1.0 / (8.0 * b1 * (a1 - a2))) * (cos(2.0 * b1 * ymax + b3 + (a1 - a2) * xmin + c1 - b2) - cos(2.0 * b1 * ymin + b3 + (a1 - a2) * xmin + c1 - b2))) +
                            (((1.0 * (ymax - ymin)) / (4.0 * (a1 - a2))) * (sin(b3 - (a1 - a2) * xmin - c1 + b2))) +
                            ((-1.0 / (8.0 * b1 * (a1 + a2))) * (cos(2.0 * b1 * ymax + b3 + (a1 + a2) * xmax + c1 + b2) - cos(2.0 * b1 * ymin + b3 + (a1 + a2) * xmax + c1 + b2))) +
                            (((-1.0 * (ymax - ymin)) / (4.0 * (a1 + a2))) * (sin(b3 - (a1 + a2) * xmax - c1 - b2))) +
                            ((1.0 / (8.0 * b1 * (a1 + a2))) * (cos(2.0 * b1 * ymax + b3 + (a1 + a2) * xmin + c1 + b2) - cos(2.0 * b1 * ymin + b3 + (a1 + a2) * xmin + c1 + b2))) +
                            (((1.0 * (ymax - ymin)) / (4.0 * (a1 + a2))) * (sin(b3 - (a1 + a2) * xmin - c1 - b2)));


//                    v=((-1.0/(8.0*b1*(a1-a2)))*( cos(2.0*b1*ymax+(a1-a2)*xmax+c1-b2) - cos(2.0*b1*ymin+(a1-a2)*xmax+c1-b2) )) +
//                            (((-1.0*(ymax-ymin))/(4.0*(a1-a2)))*(sin(b3-(a1-a2)*xmax-c1+b2))) +
//                            ((1.0/(8.0*b1*(a1-a2)))*( cos(2.0*b1*ymax+(a1-a2)*xmin+c1-b2) - cos(2.0*b1*ymin+(a1-a2)*xmin+c1-b2) )) +
//                            (((1.0*(ymax-ymin))/(4.0*(a1-a2)))*(sin(b3-(a1-a2)*xmin-c1+b2))) +
//                            ((-1.0/(8.0*b1*(a1+a2)))*( cos(2.0*b1*ymax+(a1+a2)*xmax+c1+b2) - cos(2.0*b1*ymin+(a1+a2)*xmax+c1+b2) )) +
//                            (((-1.0*(ymax-ymin))/(4.0*(a1+a2)))*(sin(b3-(a1+a2)*xmax-c1-b2))) +
//                            ((1.0/(8.0*b1*(a1+a2)))*( cos(2.0*b1*ymax+(a1+a2)*xmin+c1+b2) - cos(2.0*b1*ymin+(a1+a2)*xmin+c1+b2) )) +
//                            (((1.0*(ymax-ymin))/(4.0*(a1+a2)))*(sin(b3-(a1+a2)*xmin-c1-b2)));
                }

            } else {
                if ((b1 + a3) == 0) {
                    if ((a1 + a2) == 0) {
                        //v=0.0;
                        v = ((1.0 / (8.0 * a1)) * (ymax - ymin) * (sin(b3 + 2.0 * a1 * xmax + c1 - b2) - sin(b3 + 2.0 * a1 * xmin + c1 - b2))) -
                                ((1.0 / (16.0 * a1 * b1)) * (cos(b3 - 2.0 * b1 * ymax - 2.0 * a1 * xmax - c1 + b2) - cos(b3 - 2.0 * b1 * ymin - 2.0 * a1 * xmax - c1 + b2))) +
                                ((1.0 / (16.0 * a1 * b1)) * (cos(b3 - 2.0 * b1 * ymax - 2.0 * a1 * xmin - c1 + b2) - cos(b3 - 2.0 * b1 * ymin - 2.0 * a1 * xmin - c1 + b2))) -
                                ((1.0 / (8.0 * b1)) * (xmax - xmin) * (sin(b3 - 2.0 * b1 * ymax - c1 - b2) - sin(b3 - 2.0 * b1 * ymin - c1 - b2))) +
                                ((1.0 / (4.0)) * (xmax - xmin) * (ymax - ymin) * cos(b3 + c1 + b2));
                    } else {
                        v = ((1.0 / (4.0 * (a1 - a2))) * (ymax - ymin) * (sin(b3 + (a1 - a2) * xmax + c1 - b2))) -
                                ((1.0 / (8.0 * b1 * (a1 - a2))) * (cos(b3 - 2.0 * b1 * ymax - (a1 - a2) * xmax - c1 + b2) - cos(b3 - 2.0 * b1 * ymin - (a1 - a2) * xmax - c1 + b2))) -
                                ((1.0 / (4.0 * (a1 - a2))) * (ymax - ymin) * (sin(b3 + (a1 - a2) * xmin + c1 - b2))) +
                                ((1.0 / (8.0 * b1 * (a1 - a2))) * (cos(b3 - 2.0 * b1 * ymax - (a1 - a2) * xmin - c1 + b2) - cos(b3 - 2.0 * b1 * ymin - (a1 - a2) * xmin - c1 + b2))) +
                                ((1.0 / (4.0 * (a1 + a2))) * (ymax - ymin) * (sin(b3 + (a1 + a2) * xmax + c1 + b2))) -
                                ((1.0 / (8.0 * b1 * (a1 + a2))) * (cos(b3 - 2.0 * b1 * ymax - (a1 + a2) * xmax - c1 - b2) - cos(b3 - 2.0 * b1 * ymin - (a1 + a2) * xmax - c1 - b2))) -
                                ((1.0 / (4.0 * (a1 + a2))) * (ymax - ymin) * (sin(b3 + (a1 + a2) * xmin + c1 + b2))) +
                                ((1.0 / (8.0 * b1 * (a1 + a2))) * (cos(b3 - 2.0 * b1 * ymax - (a1 + a2) * xmin - c1 - b2) - cos(b3 - 2.0 * b1 * ymin - (a1 + a2) * xmin - c1 - b2)))
                        ;
                    }


                } else {
                    if ((a1 + a2) == 0) {
                        v = ((-1.0 / (8.0 * a1 * (a3 + b1))) * (cos((a3 + b1) * ymax + b3 + 2.0 * a1 * xmax + c1 - b2) - cos((a3 + b1) * ymin + b3 + 2.0 * a1 * xmax + c1 - b2))) +
                                ((1.0 / (8.0 * a1 * (a3 - b1))) * (cos((a3 - b1) * ymax + b3 - 2.0 * a1 * xmax - c1 + b2) - cos((a3 - b1) * ymin + b3 - 2.0 * a1 * xmax - c1 + b2))) +
                                ((1.0 / (8.0 * a1 * (a3 + b1))) * (cos((a3 + b1) * ymax + b3 + 2.0 * a1 * xmin + c1 - b2) - cos((a3 + b1) * ymin + b3 + 2.0 * a1 * xmin + c1 - b2))) +
                                ((-1.0 / (8.0 * a1 * (a3 - b1))) * (cos((a3 - b1) * ymax + b3 - 2.0 * a1 * xmin - c1 + b2) - cos((a3 - b1) * ymin + b3 - 2.0 * a1 * xmin - c1 + b2))) +
                                ((1.0 / (4.0 * (a3 - b1))) * (xmax - xmin) * (sin((a3 - b1) * ymax + b3 - c1 - b2) - sin((a3 - b1) * ymin + b3 - c1 - b2))) +
                                ((1.0 / (4.0 * (a3 + b1))) * (xmax - xmin) * (sin((a3 + b1) * ymax + b3 + c1 + b2) - sin((a3 + b1) * ymin + b3 + c1 + b2)));
                    } else {
                        v = (((-1.0) / (4.0 * (a1 - a2) * (a3 + b1))) * (cos((a3 + b1) * ymax + (a1 - a2) * xmax + c1 - b2 + b3) - cos((a3 + b1) * ymin + (a1 - a2) * xmax + c1 - b2 + b3))) +
                                (((1.0) / (4.0 * (a1 - a2) * (a3 - b1))) * (cos((a3 - b1) * ymax - (a1 - a2) * xmax - c1 + b2 + b3) - cos((a3 - b1) * ymin - (a1 - a2) * xmax - c1 + b2 + b3))) +
                                (((1.0) / (4.0 * (a1 - a2) * (a3 + b1))) * (cos((a3 + b1) * ymax + (a1 - a2) * xmin + c1 - b2 + b3) - cos((a3 + b1) * ymin + (a1 - a2) * xmin + c1 - b2 + b3))) +
                                (((-1.0) / (4.0 * (a1 - a2) * (a3 - b1))) * (cos((a3 - b1) * ymax - (a1 - a2) * xmin - c1 + b2 + b3) - cos((a3 - b1) * ymin - (a1 - a2) * xmin - c1 + b2 + b3))) +
                                (((-1.0) / (4.0 * (a1 + a2) * (a3 + b1))) * (cos((a3 + b1) * ymax + (a1 + a2) * xmax + c1 + b2 + b3) - cos((a3 + b1) * ymin + (a1 + a2) * xmax + c1 + b2 + b3))) +
                                (((1.0) / (4.0 * (a1 + a2) * (a3 - b1))) * (cos((a3 - b1) * ymax - (a1 + a2) * xmax - c1 - b2 + b3) - cos((a3 - b1) * ymin - (a1 + a2) * xmax - c1 - b2 + b3))) +
                                (((1.0) / (4.0 * (a1 + a2) * (a3 + b1))) * (cos((a3 + b1) * ymax + (a1 + a2) * xmin + c1 + b2 + b3) - cos((a3 + b1) * ymin + (a1 + a2) * xmin + c1 + b2 + b3))) +
                                (((-1.0) / (4.0 * (a1 + a2) * (a3 - b1))) * (cos((a3 - b1) * ymax - (a1 + a2) * xmin - c1 - b2 + b3) - cos((a3 - b1) * ymin - (a1 + a2) * xmin - c1 - b2 + b3)));

                    }

                }
            }
        }
        return v * f.getAmp() * g.getAmp();
    }


    private static boolean tolerantEqual(double a, double b, double tolerance) {
        double diff = (a - b);
        return
                diff == 0 ||
//                (diff != 0 ? (absdbl((diff) / a) < tolerance) : (absdbl((diff) / b) < tolerance))
                        (diff < 0 ? (abs((diff) / a) < tolerance) : (abs((diff) / b) < tolerance))
                ;
    }

    private static double computeTolerant(Domain domain, CosXCosY f, CosXPlusY g, double tolerance) {
        return compute(domain, f, g);
//        if (tolerantEqual(f.a, g.a, tolerance) && tolerantEqual(f.c, g.c, tolerance)) {
//            return primi_cos4_fa_fc(domain, f, g);
//        } else if (tolerantEqual(f.a, g.a, tolerance) && tolerantEqual(f.c, -g.c, tolerance)) {
//            return primi_cos4_fa_cf(domain, f, g);
//        } else if (tolerantEqual(f.a, -g.a, tolerance) && tolerantEqual(f.c, g.c, tolerance)) {
//            return primi_cos4_af_fc(domain, f, g);
//        } else if (tolerantEqual(f.a, -g.a, tolerance) && tolerantEqual(f.c, -g.c, tolerance)) {
//            return primi_cos4_af_cf(domain, f, g);
//        } else if (tolerantEqual(f.a, g.a, tolerance)) {
//            return primi_cos4_fa_fgc(domain, f, g);
//        } else if (tolerantEqual(f.a, -g.a, tolerance)) {
//            return primi_cos4_af_fgc(domain, f, g);
//        } else if (tolerantEqual(f.c, g.c, tolerance)) {
//            return primi_cos4_fga_fc(domain, f, g);
//        } else if (tolerantEqual(f.c, -g.c, tolerance)) {
//            return primi_cos4_fga_cf(domain, f, g);
//        } else {
//            return primi_cos4_fga_fgc(domain, f, g);
//        }
    }

}