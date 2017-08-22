package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeInfoFilter;

import java.util.Collection;
import java.util.TreeSet;

/**
 * @author taha.bensalah@gmail.com on 7/17/16.
 */ //chercher adresse Pentee
class DiscardFnByScalarProductModeInfoFilter implements ModeInfoFilter {

    private TreeSet<Integer> excluded;

    public DiscardFnByScalarProductModeInfoFilter(Collection<Integer> excluded) {
        this.excluded = new TreeSet<Integer>(excluded);
    }

    public boolean acceptModeInfo(ModeInfo info) {
        return (!excluded.contains(info.index));
    }

    public String dump() {
        Dumper h = new Dumper(this, Dumper.Type.SIMPLE);
        h.add("exclude", excluded);
        return h.toString();
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }
}
