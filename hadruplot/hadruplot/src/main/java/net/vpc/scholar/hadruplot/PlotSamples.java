/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot;

import net.vpc.common.util.ArrayUtils;

/**
 * @author vpc
 */
public abstract class PlotSamples {


    public static AbsolutePlotSamples absolute(double[] x, double[] y, double[] z) {
        return new AbsolutePlotSamples(x, y, z);
    }

    public static AbsolutePlotSamples absolute(double[] x, double[] y) {
        return new AbsolutePlotSamples(x, y);
    }

    public static AbsolutePlotSamples absolute(double[] x) {
        return new AbsolutePlotSamples(x);
    }

    public static RelativePlotSamples relative(double[] x, double[] y, double[] z) {
        return new RelativePlotSamples(false, x, y, z);
    }

    public static RelativePlotSamples relative(double[] x, double[] y) {
        return new RelativePlotSamples(false, x, y);
    }

    public static RelativePlotSamples relative(double[] x) {
        return new RelativePlotSamples(false, x);
    }

    public static RelativePlotSamples relative(int x, int y, int z) {
        return new RelativePlotSamples(false, ArrayUtils.dtimes(0, 1, x), ArrayUtils.dtimes(0, 1, y), ArrayUtils.dtimes(0, 1, z));
    }

    public static RelativePlotSamples relative(int x, int y) {
        return new RelativePlotSamples(false, ArrayUtils.dtimes(0, 1, x), ArrayUtils.dtimes(0, 1, y));
    }

    public static RelativePlotSamples relative(int x) {
        return new RelativePlotSamples(false, ArrayUtils.dtimes(0, 1, x));
    }

    public abstract int getDimension();


    public static AdaptivePlotSamples adaptive() {
        return new AdaptivePlotSamples();
    }

    public static AdaptivePlotSamples adaptive(int min, int max) {
        return new AdaptivePlotSamples().setMinimumXSamples(min).setMaximumXSamples(max);
    }

    public static AbsolutePlotSamples toAbsoluteSamples(PlotSamples samples, PlotDomain domain) {
        if (samples instanceof RelativePlotSamples) {
            if (domain == null) {
                throw new IllegalArgumentException("Missing Domain to evaluate Relative Samples");
            }
            return ((RelativePlotSamples) samples).toAbsolute(domain);
        }
        return (AbsolutePlotSamples) samples;
    }
}
