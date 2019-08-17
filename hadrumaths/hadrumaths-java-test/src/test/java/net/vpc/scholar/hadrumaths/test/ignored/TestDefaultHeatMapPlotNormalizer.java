package net.vpc.scholar.hadrumaths.test.ignored;

import net.vpc.scholar.hadruplot.backends.simple.heatmap.DefaultPlotNormalizer;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 20 juil. 2007 06:16:31
 */
public class TestDefaultHeatMapPlotNormalizer {
    public static void main(String[] args) {
        DefaultPlotNormalizer m=new DefaultPlotNormalizer();
        double[][] matrix = m.normalize(new double[][]{{1, 2}, {3, 4}});
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                double d = matrix[i][j];
                System.out.println("d = " + d);
            }
        }
    }
}
