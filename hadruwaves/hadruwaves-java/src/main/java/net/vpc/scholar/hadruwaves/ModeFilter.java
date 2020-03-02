package net.vpc.scholar.hadruwaves;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;

import java.util.EnumSet;
import java.util.Arrays;

public final class ModeFilter implements ModeIndexFilter {
    private EnumSet<ModeType> cachedHintFnModeTypes;

    public ModeFilter(ModeType[] fnModeTypes) {
        if(fnModeTypes==null || fnModeTypes.length==0){
            throw new IllegalArgumentException();
        }
        cachedHintFnModeTypes = EnumSet.copyOf(Arrays.asList(fnModeTypes));
    }

    public boolean acceptModeIndex(ModeIndex mode) {
        return cachedHintFnModeTypes.contains(mode.getModeType());
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("cachedHintFnModeTypes", context.elem(cachedHintFnModeTypes));
        return sb.build();
    }
//    public String dump() {
//        Dumper h=new Dumper(this);
//        h.add(cachedHintFnModeTypes);
//        return h.toString();
//    }

    public ModeType[] getAcceptedModes(){
        return cachedHintFnModeTypes.toArray(new ModeType[0]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModeFilter that = (ModeFilter) o;

        return cachedHintFnModeTypes != null ? cachedHintFnModeTypes.equals(that.cachedHintFnModeTypes) : that.cachedHintFnModeTypes == null;
    }

    @Override
    public int hashCode() {
        return cachedHintFnModeTypes != null ? cachedHintFnModeTypes.hashCode() : 0;
    }

    @Override
    public boolean isFrequencyDependent() {
        return false;
    }
}
