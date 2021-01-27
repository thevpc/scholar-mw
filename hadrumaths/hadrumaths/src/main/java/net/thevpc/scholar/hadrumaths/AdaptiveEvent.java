package net.thevpc.scholar.hadrumaths;

public class AdaptiveEvent {
    private final int index;
    private final int count;
    private final double error;
    private final AdaptiveResult1 samples;
    private final int type;

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
