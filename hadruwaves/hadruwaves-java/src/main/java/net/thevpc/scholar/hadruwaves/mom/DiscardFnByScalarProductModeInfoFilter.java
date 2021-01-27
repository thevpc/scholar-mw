package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
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
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("exclude", context.elem(excluded));
        return sb.build();
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }
}
