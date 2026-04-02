package net.thevpc.scholar.hadruplot.util;


import net.thevpc.nuts.math.NDoubleRange;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 05:52:09
 */
public class DefaultPlotNormalizer implements PlotNormalizer {

    public DefaultPlotNormalizer() {
    }

    public double[][] normalize(double[][] baseValues) {
        NDoubleRange minMax = NDoubleRange.of();
        minMax.add(baseValues);
        double min = minMax.min();
        double max = minMax.max();
        return normalize(baseValues, min, max);
    }

    public double[][] normalize(double[][] baseValues, double min, double max) {
        double[][] matrix = new double[baseValues.length][baseValues.length == 0 ? 0 : baseValues[0].length];
        double base = (max - min);
        if (base == 0) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    double d = baseValues[i][j];
                    if (d == Double.POSITIVE_INFINITY) {
                        matrix[i][j] = Double.POSITIVE_INFINITY;
                    } else if (d == Double.NEGATIVE_INFINITY) {
                        matrix[i][j] = Double.NEGATIVE_INFINITY;
                    } else if (Double.isNaN(d)) {
                        matrix[i][j] = Double.NaN;
                    } else {
                        matrix[i][j] = 0;
                    }
                }
            }
        } else {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    double d = baseValues[i][j];
                    if (d == Double.POSITIVE_INFINITY) {
                        matrix[i][j] = Double.POSITIVE_INFINITY;
                    } else if (d == Double.NEGATIVE_INFINITY) {
                        matrix[i][j] = Double.NEGATIVE_INFINITY;
                    } else if (Double.isNaN(d)) {
                        matrix[i][j] = Double.NaN;
                    } else {
                        matrix[i][j] = ((d - min) / base);
                    }
                }
            }
        }
        return matrix;
    }
}
