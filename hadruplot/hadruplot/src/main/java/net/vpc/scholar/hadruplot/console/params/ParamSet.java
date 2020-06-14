package net.vpc.scholar.hadruplot.console.params;


import java.io.Serializable;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 16 juil. 2005 11:04:59
 */
public abstract class ParamSet<T> implements Serializable, Cloneable, Comparable<ParamSet> {

    private ParamTarget target = ParamTarget.BOTH;
    private ParamSet<T> alias;
    //    private ParamTarget alias;
    private boolean aliasRead = false;
    private int priority = 0;
    private int index = 0;
    private XValueProvider xValueProvider;
    private ParamTitleProvider titlesProvider;
    private double multiplier = Double.NaN;

    /**
     * une valeur faible lorsque le paramètre n'a pas de vakleur ajoutée
     * exemple : Fn=100 lorsque FN n'est pas nécessaire
     */
    private T smallValue;

    /**
     * une valeur par défaut
     * exemple : Fn=nbre a la convergence
     */
    private T defaultValue;
    private CParam param;

    protected ParamSet(CParam param) {

        this.param = param;
    }

    public CParam getParam() {
        return param;
    }

    public String getName() {
        return param.getName();
    }

    public T getSmallValue() {
        return smallValue;
    }

    public ParamSet<T> setSmallValue(T smallValue) {
        this.smallValue = smallValue;
        return this;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public ParamSet<T> setDefaultValue(T defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public T getValue() {
        if (alias != null) {
            return alias.getValue();
        }
        return this.getValueImpl();
    }

    public T getValue(int index) {
        if (alias != null) {
            return alias.getValue(index);
        }
        return this.getValueImpl(index);
    }

    public boolean hasNext() {
        if (alias != null) {
            return !aliasRead;
        }
        return this.hasNextImpl();
    }

    public int getSize() {
        if (alias != null) {
            return 1;
        }
        return this.getSizeImpl();
    }

    public Object next() {
        if (alias != null) {
            aliasRead = true;
            return alias.getValue();
        }
        return this.nextImpl();
    }

    public void reset() {
        if (alias != null) {
            aliasRead = false;
            //do nothing
            return;
        }
        this.resetImpl();
    }

    protected abstract T getValueImpl(int index);

    protected abstract T getValueImpl();

    protected abstract boolean hasNextImpl();

    protected abstract T nextImpl();

    protected abstract int getSizeImpl();

    protected abstract void resetImpl();

    @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
    @Override
    public ParamSet<T> clone() {
        try {
            return (ParamSet<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDefaultTitle() {
        return getName() + "=" + getValue();
    }

    public final String getTitle() {
        if (titlesProvider != null) {
            return titlesProvider.getTitle(this);
        }
        return getDefaultTitle();
    }

    public final String toString() {
        return getTitle();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * sets the current value of the parameter for the given stucture
     *
     * @param source
     */
    public void setParameter(Object source) {
        param.configure(source, getValue());
    }

    public ParamSet getAlias() {
        return alias;
    }

    public ParamSet setAlias(ParamSet alias) {
        this.alias = alias;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public ParamSet resetPriority() {
        setPriority(0);
        return this;
    }

    public ParamSet setPriority(int priority) {
        this.priority = priority;
        return this;
    }


    public ParamTarget getTarget() {
        return target;
    }

    public void setTarget(ParamTarget target) {
        this.target = target;
    }

    //    public int compareTo(Object o) {
//    	return compareTo((ParamSet)o);
//    }
    public int compareTo(ParamSet o) {
        if (o == null) {
            return 1;
        }
        if (o == this) {
            return 0;
        }
        int i = o.getPriority() - getPriority();//param with highest priority is first!!
        if (i != 0) {
            return i;
        }
        i = getIndex() - o.getIndex();
        if (i != 0) {
            return i;
        }
        i = getSize() - o.getSize();
        if (i != 0) {
            return i;
        }
        i = getClass().getName().compareTo(o.getClass().getName());
        if (i != 0) {
            return i;
        }
        i = getTarget().compareTo(o.getTarget());
        if (i != 0) {
            return i;
        }
        return 0;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public ParamSet<T> setMultiplier(double multiplier) {
        this.multiplier = multiplier;
        return this;
    }

    public ParamSet<T> setTitlesProvider(ParamTitleProvider titlesProvider) {
        this.titlesProvider = titlesProvider;
        return this;
    }

    public ParamSet<T> setXValueProvider(XValueProvider xValueProvider) {
        this.xValueProvider = xValueProvider;
        return this;
    }

    public ParamSet<T> setTitles(String... titles) {
        return setTitlesProvider(new SimpleParamTitleProvider(titles));
    }

    public Number getXValue() {
        T value = getValue();
        if (xValueProvider != null) {
            return xValueProvider.getXValue(this);
        }
        if (value instanceof Number) {
            return ((Number) value);
        }
        return getIndex() + 1;
    }


}
