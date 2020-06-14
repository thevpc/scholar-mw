package net.vpc.scholar.hadruplot.console.params;


import net.vpc.common.util.ArrayUtils;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2005 10:23:32
 */
public class LongArrayParamSet extends ParamSet implements Cloneable {
    private long[] values;
    private int index = -1;

    public LongArrayParamSet(CParam configurator) {
        this(configurator, new long[0]);
    }

    public LongArrayParamSet(CParam configurator, long value) {
        this(configurator, new long[]{value});
    }

    public LongArrayParamSet(CParam configurator, long min, long max, int times) {
        this(configurator, ArrayUtils.ltimes(min, max, times));
    }

    public LongArrayParamSet(CParam configurator, long min, long max, long step) {
        this(configurator, ArrayUtils.lsteps(min, max, step));
    }

    public LongArrayParamSet init(long value) {
        this.values = new long[]{value};
        return this;
    }

    public LongArrayParamSet ltimes(long min, long max, int times) {
        values = ArrayUtils.ltimes(min, max, times);
        return this;
    }

    public LongArrayParamSet lsteps(long min, long max, long step) {
        values = ArrayUtils.lsteps(min, max, step);
        return this;
    }

    public LongArrayParamSet(CParam configurator, long[] values) {
        super(configurator);
        this.values = values;
    }

    protected Long getValueImpl() {
        return values[index == -1 ? 0 : index];
    }

    protected Long getValueImpl(int index) {
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

    protected Long nextImpl() {
        index++;
        Long v = getValueImpl();
        return v;
    }

    public long[] getValues() {
        return values;
    }

    public Long getValue() {
        return (Long) super.getValue();
    }
}
