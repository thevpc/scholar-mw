package net.vpc.scholar.hadruwaves;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;

public abstract class SimpleModeIndexFilter implements ModeIndexFilter {
    private boolean frequencyDependent;

    public SimpleModeIndexFilter(boolean frequencyDependent) {
        this.frequencyDependent = frequencyDependent;
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("frequencyDependent", context.elem(frequencyDependent));
        return sb.build();
    }
//    public Dumper getDumpStringHelper() {
//        Dumper h = new Dumper(this);
//        h.add("frequencyDependent", frequencyDependent);
//        return h;
//    }
//    @Override
//    public String dump() {
//        return getDumpStringHelper().toString();
//    }
}
