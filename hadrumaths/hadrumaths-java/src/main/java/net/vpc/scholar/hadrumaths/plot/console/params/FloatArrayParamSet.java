package net.vpc.scholar.hadrumaths.plot.console.params;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Param;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2005 10:23:32
 */
public class FloatArrayParamSet extends ParamSet implements Cloneable {
    private float[] values;
    private int index = -1;

    public FloatArrayParamSet(Param configurator) {
        this(configurator, new float[0]);
    }
    public FloatArrayParamSet(Param configurator, float value) {
        this(configurator, new float[]{value});
    }

    public FloatArrayParamSet(Param configurator, float min, float max, int times) {
        this(configurator, Maths.ftimes(min, max, times));
    }

    public FloatArrayParamSet(Param configurator, float min, float max, float step) {
        this(configurator, Maths.fsteps(min, max, step));
    }

    public FloatArrayParamSet init(float value) {
        this.values = new float[]{value};
        return this;
    }

    public FloatArrayParamSet ftimes(float min, float max, int times) {
        values = Maths.ftimes(min, max, times);
        return this;
    }

    public FloatArrayParamSet fsteps(float min, float max, float step) {
        values = Maths.fsteps(min, max, step);
        return this;
    }

    public FloatArrayParamSet(Param configurator, float[] values) {
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
