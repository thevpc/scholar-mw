package net.vpc.scholar.hadrumaths;

public class AdaptiveEvent {
    private int index;
    private int count;
    private double error;
    private AdaptiveResult1 samples;
    private int type;

    public AdaptiveEvent(int index, int count, double error, int type, AdaptiveResult1 samples) {
        this.index = index;
        this.error = error;
        this.count = count;
        this.samples = samples;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public double getError() {
        return error;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }

    public AdaptiveResult1 getSamples() {
        return samples;
    }

    public double getX(int index) {
        return samples.x.get(index + this.index);
    }

    public Object getValue(int index) {
        return samples.values.get(index + this.index);
    }

}
