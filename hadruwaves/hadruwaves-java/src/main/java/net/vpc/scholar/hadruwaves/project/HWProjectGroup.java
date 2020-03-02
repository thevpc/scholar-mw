package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;

public class HWProjectGroup implements TsonSerializable {
    public TsonElement toTsonElement() {
        return Tson.obj("solution").build();
    }
}
