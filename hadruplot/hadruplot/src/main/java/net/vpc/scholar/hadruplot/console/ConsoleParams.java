/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.console;

import net.vpc.scholar.hadruplot.console.params.ArrayParamSet;
import net.vpc.scholar.hadruplot.console.params.BooleanArrayParamSet;
import net.vpc.scholar.hadruplot.console.params.CParam;
import net.vpc.scholar.hadruplot.console.params.DoubleArrayParamSet;
import net.vpc.scholar.hadruplot.console.params.FloatArrayParamSet;
import net.vpc.scholar.hadruplot.console.params.IntArrayParamSet;
import net.vpc.scholar.hadruplot.console.params.LongArrayParamSet;
import net.vpc.scholar.hadruplot.console.params.XParamSet;

/**
 *
 * @author vpc
 */
public class ConsoleParams {

    public static DoubleArrayParamSet doubleParamSet(CParam param) {
        return new DoubleArrayParamSet(param);
    }

    public static DoubleArrayParamSet paramSet(CParam param, double[] values) {
        return new DoubleArrayParamSet(param, values);
    }

    public static FloatArrayParamSet paramSet(CParam param, float[] values) {
        return new FloatArrayParamSet(param, values);
    }

    public static FloatArrayParamSet floatParamSet(CParam param) {
        return new FloatArrayParamSet(param);
    }

    public static LongArrayParamSet paramSet(CParam param, long[] values) {
        return new LongArrayParamSet(param, values);
    }

    public static LongArrayParamSet longParamSet(CParam param) {
        return new LongArrayParamSet(param);
    }

    public static <T> ArrayParamSet<T> paramSet(CParam param, T[] values) {
        return new ArrayParamSet<T>(param, values);
    }

    public static <T> ArrayParamSet<T> objectParamSet(CParam param) {
        return new ArrayParamSet<T>(param);
    }

    public static IntArrayParamSet paramSet(CParam param, int[] values) {
        return new IntArrayParamSet(param, values);
    }

    public static IntArrayParamSet intParamSet(CParam param) {
        return new IntArrayParamSet(param);
    }

    public static BooleanArrayParamSet paramSet(CParam param, boolean[] values) {
        return new BooleanArrayParamSet(param, values);
    }

    public static BooleanArrayParamSet boolParamSet(CParam param) {
        return new BooleanArrayParamSet(param);
    }

    public static XParamSet xParamSet(int xsamples) {
        return new XParamSet(xsamples);
    }

    public static XParamSet xyParamSet(int xsamples, int ysamples) {
        return new XParamSet(xsamples, ysamples);
    }

    public static XParamSet xyzParamSet(int xsamples, int ysamples, int zsamples) {
        return new XParamSet(xsamples, ysamples, zsamples);
    }

    public static DoubleArrayParamSet dtimes(CParam param, double min, double max, int times) {
        return new DoubleArrayParamSet(param, min, max, times);
    }

    public static DoubleArrayParamSet dsteps(CParam param, double min, double max, double step) {
        return new DoubleArrayParamSet(param, min, max, step);
    }

    public static IntArrayParamSet itimes(CParam param, int min, int max, int times) {
        return new IntArrayParamSet(param, min, max, times);
    }

    public static IntArrayParamSet isteps(CParam param, int min, int max, int step) {
        return new IntArrayParamSet(param, min, max, (double) step);
    }

    public static FloatArrayParamSet ftimes(CParam param, int min, int max, int times) {
        return new FloatArrayParamSet(param, min, max, times);
    }

    public static FloatArrayParamSet fsteps(CParam param, int min, int max, int step) {
        return new FloatArrayParamSet(param, min, max, (float) step);
    }

    public static LongArrayParamSet ltimes(CParam param, int min, int max, int times) {
        return new LongArrayParamSet(param, min, max, times);
    }

    public static LongArrayParamSet lsteps(CParam param, int min, int max, long step) {
        return new LongArrayParamSet(param, min, max, step);
    }

}
