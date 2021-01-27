package net.thevpc.scholar.hadruwaves.props;

import net.thevpc.common.props.PropertyVetos;

public interface WritablePExpression<T> extends PExpression<T> {
    void set(String v);

    PExpression<T> readOnly();

    PropertyVetos vetos();
}
