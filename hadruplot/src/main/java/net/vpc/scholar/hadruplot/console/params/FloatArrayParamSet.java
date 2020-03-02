package net.vpc.scholar.hadruplot.console.params;

import net.vpc.common.util.ArrayUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2005 10:23:32
 */
public class FloatArrayParamSet extends ParamSet<Float> implements Cloneable {
    private float[] values;
    private int index = -1;

    public FloatArrayParamSet(CParam configurator) {
        this(configurator, new float[0]);
    }

    public FloatArrayParamSet(CParam configurator, float value) {
        this(configurator, new float[]{value});
    }

    public FloatArrayParamSet(CParam configurator, float min, float max, int times) {
        this(configurator, ArrayUtils.ftimes(min, max, times));
    }

    public FloatArrayParamSet(CParam configurator, float min, float max, float step) {
        this(configurator, ArrayUtils.fsteps(min, max, step));
    }

    public FloatArrayParamSet init(float value) {
        this.values = new float[]{value};
        return this;
    }

    public FloatArrayParamSet ftimes(float min, float max, int times) {
        values = ArrayUtils.ftimes(min, max, times);
        return this;
    }

    public FloatArrayParamSet fsteps(float min, float max, float step) {
        values = ArrayUtils.fsteps(min, max, step);
        return this;
    }

    public FloatArrayParamSet(CParam configurator, float[] values) {
        super(configurator);
        this.values = values;
    }

    protected Float getValueImpl() {
        return values[index == -1 ? 0 : index];
    }

    protected Float getValueImpl(int index) {
        return values[index == -1 ? 0 : index];
    }

    protected boolean hasNextImpl() {
        return (index + 1) < values.length;
    }

    protected int getSizeImpl() {
        return values.length;
    }

    protected void resetImpl() {
        index = -1;
    }

    protected Float nextImpl() {
        index++;
        Float v = getValueImpl();
        return v;
    }

    public float[] getValues() {
        return values;
    }

    public Float getValue() {
        return (Float) super.getValue();
    }
}
