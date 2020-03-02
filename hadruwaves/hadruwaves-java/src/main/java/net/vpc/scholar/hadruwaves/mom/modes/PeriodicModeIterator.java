package net.vpc.scholar.hadruwaves.mom.modes;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.ModeType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class PeriodicModeIterator implements Iterator<ModeIndex> {
    private ModeType[] zeroModes;
    private ModeType[] nonZeroModes;
    private LinkedList<ModeIndex> list = new LinkedList<ModeIndex>();
    private int index = 0;
    private Axis axis = Axis.X;

    public PeriodicModeIterator(ModeType[] modes, Axis axis) {
        this.axis = axis == null ? Axis.X : axis;
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
        if (list.size() == 0) {
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
        ModeType[] modes = axis == Axis.X ? new ModeType[]{ModeType.TM, ModeType.TE} : new ModeType[]{ModeType.TE, ModeType.TM};// TE prioritaire

        for (int h = 0; h < 2; h++) {
            for (int i = 0; i < index; i++) {
                for (int s1 = -1; s1 <= 1; s1 += 2) {
                    if (i == 0 && s1 == -1) {
                        continue;
                    }
                    for (int s2 = -1; s2 <= 1; s2 += 2) {
                        if (index == 0 && s2 == -1) {
                            continue;
                        }
                        for (ModeType mode : modes) {
                            ModeIndex o = h == 0 ?
                                    mode.index(s1 * i, index * s2)
                                    : mode.index(index * s2, s1 * i);
                            list.add(o);
                        }
                    }
                }
            }
        }
        for (int s1 = -1; s1 <= 1; s1 += 2) {
            if (index == 0 && s1 == -1) {
                continue;
            }
            for (int s2 = -1; s2 <= 1; s2 += 2) {
                if (index == 0 && s2 == -1) {
                    continue;
                }
                for (ModeType mode : modes) {
                    list.add(mode.index(s1 * index, s2 * index));
                }
            }
        }
    }
}