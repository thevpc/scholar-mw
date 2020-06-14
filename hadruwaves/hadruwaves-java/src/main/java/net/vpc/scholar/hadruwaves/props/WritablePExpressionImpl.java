package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyType;
import net.vpc.common.props.PropertyUpdate;
import net.vpc.common.props.impl.AbstractProperty;

import java.util.Objects;
import net.vpc.scholar.hadrumaths.units.UnitType;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class WritablePExpressionImpl<T> extends AbstractProperty implements WritablePExpression<T> {

    private String expression;
    private PExpression<T> ro;
    private PropertyType valueType;
    private T defaultValue;
    private UnitType unitType;

    public WritablePExpressionImpl(String name, PropertyType type, T defaultValue, UnitType unitType) {
        super(name, PropertyType.of(String.class));
        if (type.getArgs().length > 0) {
            throw new IllegalArgumentException("Unsupported");
        }
        this.valueType = type;
        this.unitType = unitType;
        this.defaultValue = defaultValue;
    }

    public UnitType unitType() {
        return unitType;
    }

    @Override
    public PropertyType valueType() {
        return valueType;
    }

    @Override
    public String get() {
        return expression;
    }

    @Override
    public T eval(HWConfigurationRun configuration) {
        return (T) configuration.evalResult(expression, unitType, null, defaultValue).getValueOrError();
    }

    @Override
    public void set(String v) {
        String old = this.expression;
        if (!Objects.equals(old, v)) {
            PropertyEvent event = new PropertyEvent(this, null, old, v, null, PropertyUpdate.UPDATE);
            event = adjusters.firePropertyUpdated(event);
            if (event != null) {
                vetos.firePropertyUpdated(event);
                this.expression = v;
                listeners.firePropertyUpdated(event);
            }
        }
    }

    @Override
    public PExpression<T> readOnly() {
        if (ro == null) {
            ro = new ReadOnlyPExpression<>(this);
        }
        return ro;
    }

    @Override
    public boolean isWritable() {
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(expression);
    }

}
