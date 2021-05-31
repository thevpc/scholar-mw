package net.thevpc.scholar.hadruwavesstudio.standalone.v2.util;

import net.thevpc.common.props.PropertyType;
import net.thevpc.scholar.hadruwaves.props.PExpression;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;
import net.thevpc.echo.impl.AbstractPropertiesNode;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.echo.api.AppPropertiesNodeItem;

public class PValueViewPropertyE extends AbstractPropertiesNode implements AppPropertiesNodeItem {

    final PExpression expr;
    Object evaluated;
    String newName;
    String type;

    public PValueViewPropertyE(String type, PExpression wv) {
        this(type, wv, null);
    }

    public PValueViewPropertyE(String type, PExpression wv, String newName) {
        super(type);
        this.type = type;
        this.expr = wv;
        this.newName = newName == null ? wv.propertyName() : newName;
    }

    public UnitType unitType() {
        return expr.unitType();
    }

    @Override
    public String name() {
        return newName;
    }

    @Override
    public Object object() {
        return expr.get();
    }

    @Override
    public Object[] getValues() {
        return null;
    }

    @Override
    public boolean isEditable() {
        return expr instanceof WritablePExpression;
    }

    @Override
    public void setValue(Object aValue) {
        ((WritablePExpression) expr).set(aValue == null ? null : String.valueOf(aValue));
    }

    public Class getExpressionType() {
        PropertyType vp = expr.valueType();
        return vp.getTypeClass();
    }

    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public Object getEvaluatedValue() {
        return evaluated;
    }

    public void setEvaluatedValue(Object evaluated) {
        this.evaluated = evaluated;
    }

    @Override
    public String toString() {
        return String.valueOf(name());
    }
}
