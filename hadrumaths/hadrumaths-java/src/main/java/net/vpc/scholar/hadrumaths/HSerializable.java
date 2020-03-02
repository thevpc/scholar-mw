package net.vpc.scholar.hadrumaths;

import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.io.Serializable;

public interface HSerializable extends TsonSerializable, Serializable, Dumpable {

    @Override
    String toString();

    @Override
    default String dump() {
        return toTsonElement().toString(false);
    }

}
