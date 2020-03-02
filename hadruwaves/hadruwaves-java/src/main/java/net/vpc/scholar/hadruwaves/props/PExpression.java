package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.prpbind.Property;
import net.vpc.common.prpbind.PropertyType;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

public interface PExpression<T> extends Property {
    PropertyType getValueType();

    String get();

    T eval(HWProjectEnv env);
}
