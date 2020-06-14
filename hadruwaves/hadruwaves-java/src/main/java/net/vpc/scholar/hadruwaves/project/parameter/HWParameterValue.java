package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadrumaths.units.ParamUnit;

public class HWParameterValue extends AbstractHWParameterElement {

    private final WritablePValue<UnitType> type = Props.of("type").valueOf(UnitType.class, UnitType.Double);
    private final WritablePValue<ParamUnit> unit = Props.of("unit").valueOf(ParamUnit.class, null);
    private final WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private final WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private final WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    private final WritablePValue<Boolean> discriminator = Props.of("discriminator").valueOf(Boolean.class, false);

    public HWParameterValue() {
    }

    public HWParameterValue(String name, UnitType type, ParamUnit unit) {
        this.name.set(name);
        this.unit.set(unit);
        this.type.set(type);
    }

    public WritablePValue<Boolean> discriminator() {
        return discriminator;
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

    public WritablePValue<ParamUnit> unit() {
        return unit;
    }

    public WritablePValue<UnitType> type() {
        return type;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder obj = Tson.obj("Parameter")
                .add("name", name().get());
        if (description().get() != null) {
            obj.add("description", description().get());
        }
        if (type().get() != null) {
            obj.add("type", type().get().toTsonElement(context));
        }
        if (unit().get() != null) {
            obj.add("unit", (Enum) unit().get());
        }
        return obj.build();
    }
}
