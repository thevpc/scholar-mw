package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

/**
 * Created by vpc on 8/18/16.
 */
public class CutoffModeComparator implements ModeInfoComparator {
    public static final CutoffModeComparator INSTANCE=new CutoffModeComparator();
    public int compare(ModeInfo o1, ModeInfo o2) {
        double f1= o1.cutOffFrequency;
        double f2=o2.cutOffFrequency;
        double i1= o1.initialIndex;
        double i2=o2.initialIndex;
        if(f1>f2){
            return 1;
        }else if(f1<f2){
            return -1;
        }else if(i1>i2){
            return 1;
        }else if(i1<i2){
            return -1;
        }else{
            return 0;
        }
    }
    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }
}
