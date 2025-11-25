package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadrumaths.util.dump.Dumpable;

import java.io.Serializable;

public interface HSerializable extends NToElement, Serializable, Dumpable {

    @Override
    String toString();

    @Override
    default String dump() {
        return toElement().toString(false);
    }

}
