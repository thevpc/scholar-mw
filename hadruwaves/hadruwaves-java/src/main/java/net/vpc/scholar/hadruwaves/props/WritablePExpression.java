package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.prpbind.PValue;
import net.vpc.common.prpbind.PropertyVetos;

public interface WritablePExpression<T> extends PExpression<T> {
    void set(String v);

    PExpression<T> readOnly();

    PropertyVetos vetos();
}
