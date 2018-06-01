package net.vpc.scholar.hadrumaths;

public class DoubleHolder {
    private Double value;

    public DoubleHolder() {

    }

    public DoubleHolder(Double value) {
        this.value = value;
    }

    public Double get() {
        return value;
    }

    public double get(double defaultValue) {
        return value == null ? defaultValue : value;
    }

    public void set(Double value) {
        this.value = value;
    }

    public void setIfNull(double value) {
        if (this.value == null) {
            this.value = value;
        }
    }
}
