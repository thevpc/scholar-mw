package net.thevpc.scholar.hadrumaths;

import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadrumaths.util.dump.Dumpable;

import java.io.Serializable;

public interface HSerializable extends TsonSerializable, Serializable, Dumpable {

    @Override
    String toString();

    @Override
    default String dump() {
        return toTsonElement().toString(false);
    }

}
