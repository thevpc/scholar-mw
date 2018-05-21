package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.dump.Dumper;

public abstract class SimpleModeIndexFilter implements ModeIndexFilter {
    private boolean frequencyDependent;

    public SimpleModeIndexFilter(boolean frequencyDependent) {
        this.frequencyDependent = frequencyDependent;
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }

    public Dumper getDumpStringHelper() {
        Dumper h = new Dumper(this);
        h.add("frequencyDependent", frequencyDependent);
        return h;
    }
    @Override
    public String dump() {
        return getDumpStringHelper().toString();
    }
}
