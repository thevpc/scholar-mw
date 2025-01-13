package net.thevpc.scholar.hadruwaves.mom.sources.modal;

import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeIndex;

import net.thevpc.scholar.hadruwaves.mom.sources.Sources;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 21 nov. 2006 13:49:53
 */
public interface ModalSources extends  Sources {

    public int getSourceCountForDimensions(ModeFunctions fn);

    public ModeIndex[] getPropagatingModes(ModeFunctions functions, ModeInfo[] allModes, int propagtiveModesCount);

}
