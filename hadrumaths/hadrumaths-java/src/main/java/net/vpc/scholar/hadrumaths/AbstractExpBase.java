package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.ParamExpr;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExpBase implements Expr {
    @Override
    public Integer getIntProperty(String name) {
        Number property = (Number) getProperty(name);
        return property==null?null:property.intValue();
    }

    @Override
    public Long getLongProperty(String name) {
        Number property = (Number) getProperty(name);
        return property==null?null:property.longValue();
    }

    @Override
    public Double getDoubleProperty(String name) {
        Number property = (Number) getProperty(name);
        return property==null?null:property.doubleValue();
    }

    @Override
    public String getStringProperty(String name) {
        Object property = getProperty(name);
        return property==null?null:property.toString();
    }

    @Override
    public Expr clone() {
        try {
            return (Expr)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        if(map!=null && !map.isEmpty()){
            Any any = new Any(this);
            return any.setProperties(map);
        }
        return this;
    }

    @Override
    public boolean hasProperties() {
        return false;
    }

    @Override
    public Object getProperty(String name) {
        return null;
    }

    @Override
    public Expr setProperty(String name, Object value) {
        HashMap<String, Object> m=new HashMap<>(1);
        m.put(name,value);
        return setProperties(m);
    }

    @Override
    public Map<String, Object> getProperties() {
        return Collections.EMPTY_MAP;
    }


    @Override
    public Domain domain() {
        return getDomain();
    }

    @Override
    public double toDouble() {
        return toComplex().toDouble();
    }


    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Expr setTitle(String name) {
        if(name!=null) {
            Any a = new Any(this);
            a.setTitle(name);
            return a;
        }else{
            return this;
        }
    }
    @Override
    public Expr setParam(ParamExpr paramExpr, double value) {
        return setParam(paramExpr.getParamName(), value);
    }

    @Override
    public Expr setParam(ParamExpr paramExpr, Expr value) {
        return setParam(paramExpr.getParamName(), value);
    }


    public Expr setParam(String name, double value) {
        return setParam(name, DoubleValue.valueOf(value, Domain.FULL(getDomainDimension())));
    }

}
