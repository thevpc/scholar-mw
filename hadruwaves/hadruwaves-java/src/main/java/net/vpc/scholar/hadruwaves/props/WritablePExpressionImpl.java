package net.vpc.scholar.hadruwaves.props;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyType;
import net.vpc.common.prpbind.PropertyUpdate;
import net.vpc.common.prpbind.WithListeners;
import net.vpc.common.prpbind.impl.AbstractProperty;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

import java.util.Objects;

public class WritablePExpressionImpl<T> extends AbstractProperty implements WritablePExpression<T> {
    private String expression;
    private PExpression<T> ro;
    private PropertyType valueType;
    private T defaultValue;

    public WritablePExpressionImpl(String name, PropertyType type,T defaultValue) {
        super(name, PropertyType.of(String.class));
        if (type.getArgs().length > 0) {
            throw new IllegalArgumentException("Unsupported");
        }
        valueType = type;
        this.defaultValue = defaultValue;
    }

    @Override
    public PropertyType getValueType() {
        return valueType;
    }

    @Override
    public String get() {
        return expression;
    }

    @Override
    public T eval(HWProjectEnv env) {
        switch (getValueType().getName()) {
            case "java.lang.String": {
                return (T) env.evalString(expression);
            }
            case "double":
            case "java.lang.Double": {
                return (T) env.evalExprDouble(expression, (Double)defaultValue);
            }
            case "net.vpc.scholar.hadrumaths.Expr": {
                return (T) env.evalExpr(expression);
            }
            case "net.vpc.scholar.hadrumaths.Complex": {
                return (T) env.evalExprComplex(expression, (Complex) defaultValue);
            }
            case "int":
            case "java.lang.Integer": {
                Number defaultValue = (Number) this.defaultValue;
                return (T) (Integer) (((Number) env.evalExprDouble(expression, defaultValue==null?null:defaultValue.doubleValue())).intValue());
            }
        }
        throw new IllegalArgumentException("Unsupported type " + getType().getName());
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
    public boolean isRefWritable() {
        return true;
    }

    @Override
    public boolean isValueWritable() {
        return true;
    }

    @Override
    public String toString() {
        return "WritablePExpression{" +
                "name='" + getPropertyName() + '\'' +
                ", type=" + getType() +
                " value='" + expression + '\'' +
                '}';
    }

}
