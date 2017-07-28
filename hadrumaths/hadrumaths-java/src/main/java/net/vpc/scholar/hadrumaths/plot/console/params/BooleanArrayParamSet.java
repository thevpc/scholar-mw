package net.vpc.scholar.hadrumaths.plot.console.params;

import net.vpc.scholar.hadrumaths.Param;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 17 juil. 2005 10:23:32
 */
public class BooleanArrayParamSet<T> extends ParamSet implements Cloneable {
    private boolean[] values;
    private int index = -1;

    public T init(boolean ... values) {
        this.values = values;
        return (T) this;
    }
    public BooleanArrayParamSet(Param configurator, boolean... values) {
        super(configurator);
        this.values = values;
    }

    protected Boolean getValueImpl() {
        return values[index == -1 ? 0 : index];
    }

    protected Boolean getValueImpl(int index) {
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

    protected Boolean nextImpl() {
        index++;
        return getValueImpl();
    }
    //TODO what if alias?
    public boolean[] getValues() {
        return values;
    }

    public Boolean getValue() {
        return (Boolean) super.getValue();
    }
}
