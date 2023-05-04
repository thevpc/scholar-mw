package net.thevpc.scholar.hadruplot.console.params;

import net.thevpc.common.util.ArrayUtils;
import net.thevpc.common.util.IndexSelectionStrategy;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2005 10:23:32
 */
public class IntArrayParamSet extends ParamSet<Integer> implements Cloneable {
    private int[] values;
    private int index = -1;

    public IntArrayParamSet(CParam configurator, int value) {
        this(configurator, new int[]{value});
    }

    public IntArrayParamSet(CParam configurator) {
        this(configurator, new int[0]);
    }

    public IntArrayParamSet(CParam configurator, int min, int max, int times) {
        this(configurator, ArrayUtils.itimes(min, max, times));
    }

    public IntArrayParamSet(CParam configurator, int min, int max, double step) {
        this(configurator, ArrayUtils.isteps(min, max, (int) step));
    }

    public IntArrayParamSet(CParam configurator, int[] values) {
        super(configurator);
        this.values = values;
    }

    public IntArrayParamSet init(int[] values) {
        this.values = values;
        index = -1;
        return this;
    }

    public IntArrayParamSet init(int value) {
        this.values = new int[]{value};
        return this;
    }

    public IntArrayParamSet itimes(int min, int max, int times) {
        values = net.thevpc.common.util.ArrayUtils.itimes(min, max, times);
        return this;
    }

    public IntArrayParamSet itimes(int min, int max, int times, int maxTimes, IndexSelectionStrategy strategy) {
        values = net.thevpc.common.util.ArrayUtils.itimes(min, max, times, maxTimes, strategy);
        return this;
    }

    public IntArrayParamSet isteps(int min, int max, int step) {
        values = net.thevpc.common.util.ArrayUtils.isteps(min, max, step);
        return this;
    }

    protected Integer getValueImpl() {
        return values[index == -1 ? 0 : index];
    }

    protected Integer getValueImpl(int index) {
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

    protected Integer nextImpl() {
        index++;
        return getValueImpl();
    }

    //TODO What is alias?
    public int[] getValues() {
        return values;
    }

    public Integer getValue() {
        return (Integer) super.getValue();
    }

//    public String toString() {
//        return super.toString()+"["+index+"]";
//    }
}
