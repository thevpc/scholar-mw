package net.thevpc.scholar.hadruwaves.mom.sources.modal;

import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeIndex;

import java.util.ArrayList;

/**
 * this selector make it possible to include automatically all modes that have the same cutoff frequency
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 8 juin 2007 21:16:30
 */
public class GroupedCutOffModalSources extends AbstractPropagatingModalSources implements Cloneable{
    public GroupedCutOffModalSources(int defaultSourceCount, int... sourceCountPerDimension) {
        super(defaultSourceCount, sourceCountPerDimension);
    }

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount) {
        ArrayList<ModeIndex> all=new ArrayList<ModeIndex>(propagtiveModesCount);
        for (ModeInfo allMode : allModes) {
            if (allMode.index < propagtiveModesCount) {
                all.add(allMode.getMode());
            } else if (allMode.cutOffFrequency <= allModes[propagtiveModesCount - 1].cutOffFrequency) {
                all.add(allMode.getMode());
            } else {
                break;
            }
        }
        return all.toArray(new ModeIndex[all.size()]);
    }


}