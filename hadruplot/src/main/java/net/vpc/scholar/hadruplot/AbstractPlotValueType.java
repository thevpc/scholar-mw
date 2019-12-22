package net.vpc.scholar.hadruplot;

public abstract class AbstractPlotValueType implements PlotValueType {
    private String name;

    public AbstractPlotValueType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object toValue(Object value, Class cls) {
        return cls.cast(getValue(value));
    }
}











