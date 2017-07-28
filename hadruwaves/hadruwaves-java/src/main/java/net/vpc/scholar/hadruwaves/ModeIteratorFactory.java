package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;

import java.io.Serializable;

public interface ModeIteratorFactory extends Dumpable, Serializable {
    ModeIterator iterator(ModeFunctions fn) ;
}