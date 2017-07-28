package net.vpc.scholar.hadrumaths.plot.console.params;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.IndexSelectionStrategy;
import net.vpc.scholar.hadrumaths.Param;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2005 10:23:32
 */
public class IntArrayParamSet<T extends IntArrayParamSet> extends ParamSet implements Cloneable {
    private int[] values;
    private int index = -1;

    public IntArrayParamSet(Param configurator, int value) {
        this(configurator, new int[]{value});
    }
    public IntArrayParamSet(Param configurator) {
        this(configurator, new int[0]);
    }

    public IntArrayParamSet(Param configurator, int min, int max, int times) {
        this(configurator, Maths.itimes(min, max, times));
    }

    public IntArrayParamSet(Param configurator, int min, int max, double step) {
        this(configurator, Maths.isteps(min, max, (int) step));
    }

    public IntArrayParamSet(Param configurator, int[] values) {
        super(configurator);
        this.values = values;
    }

    public T init(int[] values) {
        this.values = values;
        index = -1;
        return (T) this;
    }

    public T init(int value) {
        this.values = new int[]{value};
        return (T) this;
    }

    public T itimes(int min, int max, int times) {
        values = Maths.itimes(min, max, times);
        return (T) this;
    }

    public T itimes(int min, int max, int times,int maxTimes, IndexSelectionStrategy strategy) {
        values = Maths.itimes(min, max, times, maxTimes, strategy);
        return (T) this;
    }

    public T isteps(int min, int max, int step) {
        values = Maths.isteps(min, max, step);
        return (T) this;
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
