package net.thevpc.scholar.hadruwaves.props;

import net.thevpc.common.props.Property;
import net.thevpc.common.props.PropertyType;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface PExpression<T> extends Property {

    UnitType unitType();

    PropertyType valueType();

    String get();

    T eval(HWConfigurationRun configuration);
}
