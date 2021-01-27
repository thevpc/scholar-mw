package net.thevpc.scholar.hadruwaves.mom.sources.modal;

import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeIndex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 8 juin 2007 21:16:30
 */
public class CutOffModalSourcesTM02n extends AbstractPropagatingModalSources implements Cloneable{
    public CutOffModalSourcesTM02n(int defaultSourceCount, int... sourceCountPerDimension) {
        super(defaultSourceCount, sourceCountPerDimension);
    }

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount) {
        ModeIndex[] all=new ModeIndex[propagtiveModesCount];
        int index=0;
        for (ModeInfo allMode : allModes) {
            ModeIndex modeIndex = allMode.getMode();
            switch (modeIndex.type()) {
                case TM: {
                    if ((modeIndex.n() % 2) == 0) {
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