package net.thevpc.scholar.hadruplot.libraries.simple.heatmap;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 05:49:39
 */
public interface PlotNormalizer {
    /**
     * return normalized values in [0..1]
     *
     * @param baseValues initial value
     * @return normalized values in [0..1]
     */
    double[][] normalize(double[][] baseValues);
}
