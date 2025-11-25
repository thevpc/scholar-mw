package net.thevpc.scholar.hadruwaves.mom;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeInfoFilter;

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

    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.add("exclude", NElementHelper.elem(excluded));
        return sb.build();
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }
}
