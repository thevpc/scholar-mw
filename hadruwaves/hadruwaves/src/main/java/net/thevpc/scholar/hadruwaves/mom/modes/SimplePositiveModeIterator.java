package net.thevpc.scholar.hadruwaves.mom.modes;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadruwaves.ModeIndex;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruwaves.WallBorders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class SimplePositiveModeIterator implements Iterator<ModeIndex> {
    private ModeType[] zeroModes;
    private ModeType[] nonZeroModes;
    private LinkedList<ModeIndex> list = new LinkedList<ModeIndex>();
    private long index = 0;
    private long maxIndex = 0;
    private Axis invariance;
    private WallBorders borders;
    private int window;

    public SimplePositiveModeIterator(ModeType[] modes, Axis invariance, WallBorders borders, int max) {
        this.invariance = invariance;
        this.borders = borders;
        this.maxIndex = max / 2;
        this.window = (int) Math.sqrt(this.maxIndex);
        if (this.window <= 0) {
            throw new IllegalArgumentException("Unsupported");
        }
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
        this.zeroModes = zeroModesList.toArray(new ModeType[zeroModesList.size()]);
        this.nonZeroModes = nonZeroModesList.toArray(new ModeType[nonZeroModesList.size()]);
    }

    public boolean hasNext() {
        if (list.size() == 0) {
            nextModeInfos();
        }
        return !list.isEmpty();
    }

    public ModeIndex next() {
        ModeIndex m = list.getFirst();
        list.removeFirst();
        return m;
    }

    protected void nextModeInfos() {
        if (index < maxIndex) {
            int m = (int) (index / window);
            int n = (int) (index % window);
            if (borders == WallBorders.EMEM || borders == WallBorders.MEME) {
                list.addLast(ModeType.TEM.index(m, n));
            }
            list.addLast(ModeType.TE.index(m, n));
            list.addLast(ModeType.TM.index(m, n));
            index++;
        }
//
//
//        boolean useInvariance = true;
//        if (useInvariance) {
//            if (invariance != null) {
//                switch (invariance) {
//                    case X: {
//                        switch (borders) {
//                            case EMEM:
//                            case MEME:
//                            case PPPP:
//                            {
//                                if (index == 0) {
//                                    list.addLast(ModeType.TEM.index(0, 0));
//                                } else {
//                                    list.addLast(ModeType.TM.index(0, index));
//                                    list.addLast(ModeType.TE.index(0, index));
//                                }
//                                return;
//                            }
//                            default:{
//                                list.addLast(ModeType.TM.index(0, index));
//                                list.addLast(ModeType.TE.index(0, index));
//                                return;
//                            }
//                        }
//                    }
//                    case Y: {
//                        switch (borders) {
//                            case EMEM:
//                            case MEME:
//                            case PPPP:
//                            {
//                                if (index == 0) {
//                                    list.addLast(ModeType.TEM.index(0, 0));
//                                } else {
//                                    list.addLast(ModeType.TM.index(index,0));
//                                    list.addLast(ModeType.TE.index(index,0));
//                                }
//                                return;
//                            }
//                            default:{
//                                list.addLast(ModeType.TM.index(index,0));
//                                list.addLast(ModeType.TE.index(index,0));
//                                return;
//                            }
//                        }
//                    }
//                }
//                System.err.println(getClass().getSimpleName()+ " : use Invariance by could not resolve  (invariance=" + invariance+", borders="+borders+")");
//            }
//        }
//        int min = 0;
//        if (index == min) {
//            for (ModeType mode : zeroModes) {
//                ModeIndex mi = mode.index(0, 0);
//                list.addLast(mi);
//            }
//        } else {
//            for (int i = min; i < index; i++) {
//                for (ModeType mode : nonZeroModes) {
//                    ModeIndex mi = mode.index(i, index);
//                    list.addLast(mi);
//                }
//                for (ModeType mode : nonZeroModes) {
//                    ModeIndex mi = mode.index(index, i);
//                    list.addLast(mi);
//                }
//            }
//            for (ModeType mode : nonZeroModes) {
//                ModeIndex mi = mode.index(index, index);
//                list.addLast(mi);
//            }
//        }
    }
}