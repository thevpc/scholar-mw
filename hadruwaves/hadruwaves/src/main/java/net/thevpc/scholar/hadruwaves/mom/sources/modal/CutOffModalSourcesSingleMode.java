package net.thevpc.scholar.hadruwaves.mom.sources.modal;

import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeIndex;

import java.util.HashSet;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 8 juin 2007 21:16:30
 */
public class CutOffModalSourcesSingleMode extends AbstractPropagatingModalSources implements Cloneable{
    public CutOffModalSourcesSingleMode(int defaultSourceCount, int... sourceCountPerDimension) {
        super(defaultSourceCount, sourceCountPerDimension);
    }

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount) {
        HashSet<String> added=new HashSet<String>();
        ModeIndex[] all=new ModeIndex[propagtiveModesCount];
        int index=0;
        for (ModeInfo allMode : allModes) {
            ModeIndex modeIndex = allMode.getMode();
            String sidx = modeIndex.m() + "," + modeIndex.n();
            if(!added.contains(sidx)){
                added.add(sidx);
                all[index] = modeIndex;
                index++;
            }else{
                //System.out.println(getClass().getSimpleName() +" : skipped mode "+modeIndex);
            }
            if (index == all.length) {
                break;
            }
        }
        return all;
    }




}