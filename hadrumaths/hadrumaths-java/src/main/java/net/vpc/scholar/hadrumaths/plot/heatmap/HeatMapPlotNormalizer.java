package net.vpc.scholar.hadrumaths.plot.heatmap;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 05:49:39
 */
public interface HeatMapPlotNormalizer {
    /**
     * return normalized values in [0..1]
     * @param baseValues initial value
     * @return normalized values in [0..1]
     */
    double[][] normalize(double[][] baseValues);
}
