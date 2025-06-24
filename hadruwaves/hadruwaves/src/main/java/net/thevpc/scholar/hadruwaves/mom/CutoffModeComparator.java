package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.mom.str.ModeInfoComparator;

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
        return Tson.ofUplet(getClass().getSimpleName()).build();
    }
}
