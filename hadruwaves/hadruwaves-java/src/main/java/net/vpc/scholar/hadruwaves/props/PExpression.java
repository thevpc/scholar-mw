package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.props.Property;
import net.vpc.common.props.PropertyType;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public interface PExpression<T> extends Property {

    UnitType unitType();

    PropertyType valueType();

    String get();

    T eval(HWConfigurationRun configuration);
}
