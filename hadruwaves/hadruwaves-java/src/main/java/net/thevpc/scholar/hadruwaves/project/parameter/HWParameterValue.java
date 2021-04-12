package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableValue;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;

public class HWParameterValue extends AbstractHWParameterElement {

    private final WritableValue<UnitType> type = Props.of("type").valueOf(UnitType.class, UnitType.Double);
    private final WritableValue<ParamUnit> unit = Props.of("unit").valueOf(ParamUnit.class, null);
    private final WritableValue<String> name = Props.of("name").valueOf(String.class, null);
    private final WritableValue<String> description = Props.of("description").valueOf(String.class, null);
    private final WritableValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    private final WritableValue<Boolean> discriminator = Props.of("discriminator").valueOf(Boolean.class, false);

    public HWParameterValue() {
    }

    public HWParameterValue(String name, UnitType type, ParamUnit unit) {
        this.name.set(name);
        this.unit.set(unit);
        this.type.set(type);
    }

    public WritableValue<Boolean> discriminator() {
        return discriminator;
    }

    @Override
    public WritableValue<String> name() {
        return name;
    }

    @Override
    public WritableValue<String> description() {
        return description;
    }

    public WritableValue<String> parentPath() {
        return parentPath;
    }

    public WritableValue<ParamUnit> unit() {
        return unit;
    }

    public WritableValue<UnitType> type() {
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
