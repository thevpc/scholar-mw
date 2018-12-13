package net.vpc.scholar.hadruwaves.mom.sources.modal;

import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import net.vpc.scholar.hadruwaves.mom.sources.Sources;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 21 nov. 2006 13:49:53
 */
public interface ModalSources extends  Dumpable, Sources {

    public int getSourceCountForDimensions(ModeFunctions fn);

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount);

}
