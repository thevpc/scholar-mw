package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.WallBorders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class PositiveModeIterator implements Iterator<ModeIndex> {
    private ModeType[] zeroModes;
    private ModeType[] nonZeroModes;
    private LinkedList<ModeIndex> list = new LinkedList<ModeIndex>();
    private int index = 0;
    private Axis invariance;
    private WallBorders borders;

    public PositiveModeIterator(ModeType[] modes, Axis invariance, WallBorders borders) {
        this.invariance = invariance;
        this.borders = borders;
        ArrayList<ModeType> zeroModesList = new ArrayList<ModeType>(modes.length);
        ArrayList<ModeType> nonZeroModesList = new ArrayList<ModeType>(modes.length);
        for (ModeType mode : modes) {
            switch (mode) {
                case TEM: {
                    zeroModesList.add(mode);
                    break;
                }
                case TE: {
                    zeroModesList.add(mode);//??????????? TODO verify it
                    nonZeroModesList.add(mode);
                    break;
                }
                case TM: {
                    zeroModesList.add(mode);//??????????? TODO verify it
                    nonZeroModesList.add(mode);
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported Mode " + mode);
                }
            }
        }
        this.zeroModes = zeroModesList.toArray(new ModeType[0]);
        this.nonZeroModes = nonZeroModesList.toArray(new ModeType[0]);
    }

    @Override
    public boolean hasNext() {
        while (list.size() == 0) {
            nextModeInfos();
            index++;
        }
        return !list.isEmpty();
    }

    public ModeIndex next() {
        ModeIndex m = list.getFirst();
        list.removeFirst();
        return m;
    }

    protected void nextModeInfos() {
        boolean useInvariance = true;
        if (useInvariance) {
            if (invariance != null) {
                switch (invariance) {
                    case X: {
                        switch (borders) {
                            case EMEM:
                            case MEME:
                            case PPPP:
                            {
                                if (index == 0) {
                                    list.addLast(ModeType.TEM.index(0, 0));
                                } else {
                                    list.addLast(ModeType.TM.index(0, index));
                                    list.addLast(ModeType.TE.index(0, index));
                                }
                                return;
                            }
                            default:{
                                list.addLast(ModeType.TM.index(0, index));
                                list.addLast(ModeType.TE.index(0, index));
                                return;
                            }
                        }
                    }
                    case Y: {
                        switch (borders) {
                            case EMEM:
                            case MEME:
                            case PPPP:
                            {
                                if (index == 0) {
                                    list.addLast(ModeType.TEM.index(0, 0));
                                } else {
                                    list.addLast(ModeType.TM.index(index,0));
                                    list.addLast(ModeType.TE.index(index,0));
                                }
                                return;
                            }
                            default:{
                                list.addLast(ModeType.TM.index(index,0));
                                list.addLast(ModeType.TE.index(index,0));
                                return;
                            }
                        }
                    }
                }
                System.err.println(getClass().getSimpleName()+ " : use Invariance by could not resolve  (invariance=" + invariance+", borders="+borders+")");
            }
        }
        int min = 0;
        if (index == min) {
            for (ModeType mode : zeroModes) {
                ModeIndex mi = mode.index(0, 0);
                list.addLast(mi);
            }
        } else {
            for (int i = min; i < index; i++) {
                for (ModeType mode : nonZeroModes) {
                    ModeIndex mi = mode.index(i, index);
                    list.addLast(mi);
                }
                for (ModeType mode : nonZeroModes) {
                    ModeIndex mi = mode.index(index, i);
                    list.addLast(mi);
                }
            }
            for (ModeType mode : nonZeroModes) {
                ModeIndex mi = mode.index(index, index);
                list.addLast(mi);
            }
        }
    }
}