package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableBoolean;
import net.thevpc.common.props.WritableString;
import net.thevpc.common.props.WritableValue;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;

public class HWParameterValue extends AbstractHWParameterElement {

    private final WritableValue<UnitType> type = Props.of("type").valueOf(UnitType.class, UnitType.Double);
    private final WritableValue<ParamUnit> unit = Props.of("unit").valueOf(ParamUnit.class, null);
    private final WritableString name = Props.of("name").stringOf(null);
    private final WritableString description = Props.of("description").stringOf(null);
    private final WritableString parentPath = Props.of("parentPath").stringOf(null);
    private final WritableBoolean discriminator = Props.of("discriminator").booleanOf(false);

    public HWParameterValue() {
    }

    public HWParameterValue(String name, UnitType type, ParamUnit unit) {
        this.name.set(name);
        this.unit.set(unit);
        this.type.set(type);
    }

    public WritableBoolean discriminator() {
        return discriminator;
    }

    @Override
    public WritableString name() {
        return name;
    }

    @Override
    public WritableString description() {
        return description;
    }

    public WritableString parentPath() {
        return parentPath;
    }

    public WritableValue<ParamUnit> unit() {
        return unit;
    }

    public WritableValue<UnitType> type() {
        return type;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder obj = NElement.ofObjectBuilder("Parameter")
                .add("name", name().get());
        if (description().get() != null) {
            obj.add("description", description().get());
        }
        if (type().get() != null) {
            obj.add("type", type().get().toElement());
        }
        if (unit().get() != null) {
            obj.add("unit", NElement.ofEnum((Enum) unit().get()));
        }
        return obj.build();
    }
}
