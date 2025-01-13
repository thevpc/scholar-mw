package net.thevpc.scholar.hadruwaves.mom.modes;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadruwaves.ModeIndex;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruwaves.WallBorders;

import java.util.*;
import net.thevpc.jeep.util.IntIterator;
import net.thevpc.jeep.util.IntIteratorBuilder;

public class PositiveModeIterator2 implements Iterator<ModeIndex> {
    private Axis invariance;
    private WallBorders borders;
    private IntIterator ii;
    private ModeType[] modes;
    private LinkedList<ModeIndex> list = new LinkedList<ModeIndex>();

    public PositiveModeIterator2(ModeType[] modes, Axis invariance, WallBorders borders) {
        List<ModeType> nz = new ArrayList<>();
        for (ModeType mode : modes) {
            if (mode == ModeType.TEM) {
                list.add(ModeIndex.mode(ModeType.TEM, 0, 0));
            } else {
                nz.add(mode);
            }
        }
        this.modes = nz.toArray(new ModeType[0]);
        this.invariance = invariance;
        this.borders = borders;
        ii = IntIteratorBuilder.iter().from(0, 0).breadthFirst();
    }

    public static void main(String[] args) {
        PositiveModeIterator2 p = new PositiveModeIterator2(new ModeType[]{ModeType.TE, ModeType.TM}, null, null);
        int max=100;
        while(p.hasNext()){
            System.out.println(p.next());
            max--;
            if(max==0){
                break;
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (list.isEmpty()) {
            while (ii.hasNext()) {
                int[] mn = ii.next();
                if (mn[0] != 0 || mn[1] != 0) {
                    for (ModeType mode : modes) {
                        list.add(ModeIndex.mode(mode, mn[0], mn[1]));
                    }
                    return true;
                }
            }
        }
        return true;
    }

    public ModeIndex next() {
        return list.remove();
    }
}