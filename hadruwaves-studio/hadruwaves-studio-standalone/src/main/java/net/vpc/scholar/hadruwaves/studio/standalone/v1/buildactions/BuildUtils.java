package net.vpc.scholar.hadruwaves.studio.standalone.v1.buildactions;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;


/**
 * @author Taha Ben Salah (thevpc@walla.com)
 * @creationtime 17 juil. 2007 15:33:58
 */
public final class BuildUtils {
    private BuildUtils() {
    }

    /**
     * inOutMatrix[i][j]+=multCoeff*f(x[j],y[i])
     *
     * @param x
     * @param y
     * @param inOutMatrix
     * @param multCoeff
     */
    public static void updateMatrixAddFxyMultCoeff(DoubleToDouble ff, Complex[][] inOutMatrix, double[] x, double[] y, Complex multCoeff) {
//        double[][] r = new double[x.length][y.length];
        Domain domain=ff.getDomain();
        double min_x = domain.getXMin();
        double max_x = domain.getXMax();
        double min_y = domain.getYMin();
        double max_y = domain.getYMax();

        if (multCoeff.getImag() == 0) {
            double d = multCoeff.getReal();
            for (int xIndex = 0; xIndex < x.length; xIndex++) {
                double xi = x[xIndex];
                for (int yIndex = 0; yIndex < y.length; yIndex++) {
                    double yj = y[yIndex];
                    if (xi >= min_x && xi <= max_x && yj >= min_y && yj <= max_y) {
                        inOutMatrix[yIndex][xIndex] =
                                inOutMatrix[yIndex][xIndex].add(d * ff.evalDouble(x[xIndex], y[yIndex]));

                    }
                }
            }
        } else {
            for (int xIndex = 0; xIndex < x.length; xIndex++) {
                double xi = x[xIndex];
                for (int yIndex = 0; yIndex < y.length; yIndex++) {
                    double yj = y[yIndex];
                    if (xi >= min_x && xi <= max_x && yj >= min_y && yj <= max_y) {
                        inOutMatrix[yIndex][xIndex] =
                                inOutMatrix[yIndex][xIndex].add(multCoeff.mul(ff.evalDouble(x[xIndex], y[yIndex])));

                    }
                }
            }
        }
    }

    /**
     * inOutMatrix[i][j]+=multCoeff*f(x[j],y[i])
     *
     * @param x
     * @param y
     * @param inOutMatrix
     */
    public static void updateMatrixAddFxyMultCoeffOrdredXY(DoubleToDouble ff,double[][] inOutMatrix, double[] x, double[] y, double multCoeff) {
//        double[][] r = new double[x.length][y.length];
        Domain domain=ff.getDomain();
        int[] abx = Maths.rangeCC(x, domain.getXMin(), domain.getXMax());
        if (abx == null) {
            return;
        }
        int[] aby = Maths.rangeCC(y, domain.getYMin(), domain.getYMax());
        if (aby == null) {
            return;
        }

        int xIndexMin = abx[0];
        int xIndexMax = abx[1];
        int yIndexMin = aby[0];
        int yIndexMax = aby[1];
        for (int yIndex = yIndexMin; yIndex <= yIndexMax; yIndex++) {
            double yj = y[yIndex];
            double[] line = inOutMatrix[yIndex];
            for (int xIndex = xIndexMin; xIndex <= xIndexMax; xIndex++) {
                double xi = x[xIndex];
                //if (xi >= xmin && xi <= xmax && yj >= ymin && yj <= ymax) {
                line[xIndex] += (multCoeff * (ff.evalDouble(xi, yj)));
                //}
            }
        }
    }

    /**
     * inOutMatrix[i][j]+=multCoeff*f(x[j],y[i])
     *
     * @param x
     * @param y
     * @param inOutMatrix
     */
    public static void updateMatrixAddFxyMultCoeffOrdredXY(DoubleToDouble ff,Complex[][] inOutMatrix, double[] x, double[] y, Complex multCoeff) {
        Domain domain=ff.getDomain();
        int[] abx = Maths.rangeCC(x, domain.getXMin(), domain.getXMax());
        if (abx == null) {
            return;
        }
        int[] aby = Maths.rangeCC(y, domain.getYMin(), domain.getYMax());
        if (aby == null) {
            return;
        }

        int xIndexMin = abx[0];
        int xIndexMax = abx[1];
        int yIndexMin = aby[0];
        int yIndexMax = aby[1];
        for (int yIndex = yIndexMin; yIndex <= yIndexMax; yIndex++) {
            double yj = y[yIndex];
            Complex[] line = inOutMatrix[yIndex];
            for (int xIndex = xIndexMin; xIndex <= xIndexMax; xIndex++) {
                double xi = x[xIndex];
                line[xIndex] = line[xIndex].add(multCoeff.mul(ff.evalDouble(xi, yj)));
            }
        }
    }

    /**
     * inOutMatrix[i][j]+=multCoeff*f(x[j],y[i])
     *
     * @param x
     * @param y
     * @param inOutMatrix
     * @param multCoeff
     */
    public static void updateMatrixAddFxyMultCoeff(DoubleToDouble ff,double[][] inOutMatrix, double[] x, double[] y, double multCoeff) {
//        double[][] r = new double[x.length][y.length];
        Domain domain=ff.getDomain();
        double min_x = domain.getXMin();
        double max_x = domain.getXMax();
        double min_y = domain.getYMin();
        double max_y = domain.getYMax();
        int xIndexMax = x.length;
        int yIndexMax = y.length;
        for (int xIndex = 0; xIndex < xIndexMax; xIndex++) {
            double xi = x[xIndex];
            for (int yIndex = 0; yIndex < yIndexMax; yIndex++) {
                double yj = y[yIndex];
                if (xi >= min_x && xi <= max_x && yj >= min_y && yj <= max_y) {
                    inOutMatrix[yIndex][xIndex] += (multCoeff * (ff.evalDouble(x[xIndex], y[yIndex])));
                }
            }
        }
    }

    /**
     * inOutMatrix[i][j]+=f(x[j],y[i])
     *
     * @param x
     * @param y
     * @param inOutMatrix
     */
    public static void updateMatrixAddFxy(DoubleToDouble ff,double[][] inOutMatrix, double[] x, double[] y) {
//        double[][] r = new double[x.length][y.length];
        Domain domain=ff.getDomain();
        double min_x = domain.getXMin();
        double max_x = domain.getXMax();
        double min_y = domain.getYMin();
        double max_y = domain.getYMax();

        for (int xIndex = 0; xIndex < x.length; xIndex++) {
            double xi = x[xIndex];
            for (int yIndex = 0; yIndex < y.length; yIndex++) {
                double yj = y[yIndex];
                if (xi >= min_x && xi <= max_x && yj >= min_y && yj <= max_y) {
                    inOutMatrix[yIndex][xIndex] += (ff.evalDouble(x[xIndex], y[yIndex]));
                }
            }
        }
    }
}
