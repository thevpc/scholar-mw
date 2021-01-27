package net.thevpc.scholar.hadruwaves;

import net.thevpc.scholar.hadrumaths.HSerializable;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;

import java.util.Iterator;

public interface ModeIterator extends HSerializable {
    Iterator<ModeIndex> iterator(ModeFunctions fn);

    /**
     * Absolute Iterator will always have return modes in the very same order
     * what ever max modes count is. Absolute Iterator can be optimized
     *
     * @param fn
     * @return
     */
    boolean isAbsoluteIterator(ModeFunctions fn);
}