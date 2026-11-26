/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.util.NArrays;

/**
 * @author vpc
 */
public abstract class Samples {


    public static AbsoluteSamples absolute(double[] x, double[] y, double[] z) {
        return new AbsoluteSamples(x, y, z);
    }

    public static AbsoluteSamples absolute(double[] x, double[] y) {
        return new AbsoluteSamples(x, y);
    }

    public static AbsoluteSamples absolute(double[] x) {
        return new AbsoluteSamples(x);
    }

    public static RelativeSamples relative(double[] x, double[] y, double[] z) {
        return new RelativeSamples(false, x, y, z);
    }

    public static RelativeSamples relative(double[] x, double[] y) {
        return new RelativeSamples(false, x, y);
    }

    public static RelativeSamples relative(double[] x) {
        return new RelativeSamples(false, x);
    }

    /**
     * Relative cell centres: (i + 0.5) / n for i in [0, n).
     * <p>
     * Domains are half-open ([min, max)), so the natural representatives of their cells are
     * the centres. A closed grid [0..1] would place the last sample exactly on max, where it
     * is outside the domain (and evaluates to zero, silently dropping the last column).
     * Cell centres keep every generated sample strictly inside, and the closed interval
     * [0,1] can still be requested explicitly with {@code relative(new double[]{...})}.
     */
    private static double[] cells(int n) {
        if (n <= 0) {
            return new double[0];
        }
        double[] r = new double[n];
        for (int i = 0; i < n; i++) {
            r[i] = (i + 0.5) / n;
        }
        return r;
    }

    public static RelativeSamples relative(int x, int y, int z) {
        return new RelativeSamples(false, cells(x), cells(y), cells(z));
    }

    public static RelativeSamples relative(int x, int y) {
        return new RelativeSamples(false, cells(x), cells(y));
    }

    public static RelativeSamples relative(int x) {
        return new RelativeSamples(false, cells(x));
    }

    public abstract int getDimension();


    public static AdaptiveSamples adaptive() {
        return new AdaptiveSamples();
    }

    public static AdaptiveSamples adaptive(int min, int max) {
        return new AdaptiveSamples().setMinimumXSamples(min).setMaximumXSamples(max);
    }

    public static AbsoluteSamples toAbsoluteSamples(Samples samples, Domain domain) {
        if (samples instanceof RelativeSamples) {
            if (domain == null) {
                throw new IllegalArgumentException("Missing Domain to evaluate Relative Samples");
            }
            return ((RelativeSamples) samples).toAbsolute(domain);
        }
        return (AbsoluteSamples) samples;
    }
}
