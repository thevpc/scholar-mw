package net.vpc.scholar.hadruplot.console.params;


public class ArrayParamSet<T> extends ParamSet<T> implements Cloneable {
    private T[] values;
    private int index = -1;

    public ArrayParamSet(CParam configurator) {
        super(configurator);
    }

    public ArrayParamSet(CParam configurator, T... values) {
        super(configurator);
        this.values = values;
    }

    public ArrayParamSet init(T... all) {
        values = all;
        return this;
    }

//    public ArrayParamSet dsteps(T value) {
//        values= new T[]{value};
//        return this;
//    }


    @Override
    protected T getValueImpl(int index) {
        return values == null ? null : values[index == -1 ? 0 : index];
    }

    @Override
    protected T getValueImpl() {
        return values == null ? null : values[index == -1 ? 0 : index];
    }

    @Override
    public T getSmallValue() {
        T t = super.getSmallValue();
        return t == null ? (values == null ? null : values[0]) : t;
    }

    @Override
    public T getDefaultValue() {
        T t = super.getDefaultValue();
        return t == null ? (values == null ? null : values[0]) : t;
    }

    @Override
    protected boolean hasNextImpl() {
        return (index + 1) < (values == null ? 0 : values.length);
    }

    @Override
    protected int getSizeImpl() {
        return values == null ? 0 : values.length;
    }

    @Override
    protected void resetImpl() {
        index = -1;
    }

    @Override
    protected T nextImpl() {
        index++;
        return getValueImpl();
    }

    //TODO what if alias?
    public T[] getValues() {
        return values;
    }

}
