package net.vpc.scholar.hadruwaves.mom.sources.modal;

import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeIndex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 8 juin 2007 21:16:30
 */
public class CutOffModalSourcesTE2n0 extends AbstractPropagatingModalSources implements Cloneable{
    public CutOffModalSourcesTE2n0(int defaultSourceCount, int... sourceCountPerDimension) {
        super(defaultSourceCount, sourceCountPerDimension);
    }

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount) {
        ModeIndex[] all=new ModeIndex[propagtiveModesCount];
        int index=0;
        for (ModeInfo allMode : allModes) {
            ModeIndex modeIndex = allMode.getMode();
            switch (modeIndex.type()) {
                case TE: {
                    if ((modeIndex.m() % 2) == 0) {
                        all[index] = modeIndex;
                        index++;
                    }
                }
                case TEM: {
                    all[index] = modeIndex;
                    index++;
                }
            }
            if (index == all.length) {
                break;
            }
        }
        return all;
    }




}