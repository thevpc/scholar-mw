package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

import java.io.Serializable;

public interface ModeIteratorFactory extends Dumpable, Serializable {
    ModeIterator iterator(ModeFunctions fn);

    /**
     * Absolute Iterator will always have return modes in the very same order
     * what ever max modes count is. Absolute Iterator can be optimized
     * @param fn
     * @return
     */
    boolean isAbsoluteIterator(ModeFunctions fn);
}