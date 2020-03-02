package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

/**
 * Created by vpc on 8/18/16.
 */
public class InitialIndexModeComparator implements ModeInfoComparator {
    public static final InitialIndexModeComparator INSTANCE=new InitialIndexModeComparator();
    public int compare(ModeInfo o1, ModeInfo o2) {
        double i0= o1.initialIndex;
        double i=o2.initialIndex;
        if(i0>i){
            return 1;
        }else if(i0<i){
            return -1;
        }else{
            return 1;
        }
    }
//    public String dump() {
//        return getClass().getSimpleName();
//    }
    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }
}
