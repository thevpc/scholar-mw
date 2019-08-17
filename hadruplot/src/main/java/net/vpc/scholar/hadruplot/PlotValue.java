package net.vpc.scholar.hadruplot;

public interface PlotValue {
    int getPriority();
    PlotValueType getType();
    Object getValue();
    void set(String prop,String val);
    String get(String prop);
}
