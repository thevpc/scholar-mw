package net.thevpc.scholar.hadruwaves.mom.sources.modal;

import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeIndex;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 8 juin 2007 21:16:30
 */
public class InitialIndexModalSources extends AbstractPropagatingModalSources implements Cloneable{
    public InitialIndexModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        super(defaultSourceCount, sourceCountPerDimension);
    }

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount) {
        ModeIndex[] all=new ModeIndex[propagtiveModesCount];
        for (int i = 0; i < all.length; i++) {
            all[i]=allModes[i].getMode();

        }
        return all;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


}
