package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePValue;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruwaves.project.UnitTypes;

public class HWSParameterValue implements TsonSerializable, HWSParameterElement {
    private WritablePValue<UnitTypes.UTypeBase> unit = Props.of("type").valueOf(UnitTypes.UTypeBase.class, UnitTypes.Dimension.m);
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);

    public HWSParameterValue() {
    }

    public HWSParameterValue(String name, UnitTypes.UTypeBase unit) {
        this.name.set(name);
        this.unit.set(unit);
    }

    @Override
    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public WritablePValue<String> description() {
        return description;
    }

    public WritablePValue<String> parentPath() {
        return parentPath;
    }

    public WritablePValue<UnitTypes.UTypeBase> unit() {
        return unit;
    }

    public TsonElement toTsonElement() {
        return Tson.obj("configuration")
                .add("name", name().get())
                .add("description", description().get())
                .add("unit", (Enum) unit().get())
                .build();
    }
}
