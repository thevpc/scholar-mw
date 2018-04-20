package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.geom.Geometry;
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
        return property == null ? null : property.intValue();
    }

    @Override
    public Long getLongProperty(String name) {
        Number property = (Number) getProperty(name);
        return property == null ? null : property.longValue();
    }

    @Override
    public Double getDoubleProperty(String name) {
        Number property = (Number) getProperty(name);
        return property == null ? null : property.doubleValue();
    }

    @Override
    public String getStringProperty(String name) {
        Object property = getProperty(name);
        return property == null ? null : property.toString();
    }

    @Override
    public Expr clone() {
        try {
            return (Expr) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Expr setProperties(Map<String, Object> map) {
        return setProperties(map,false);
    }

    @Override
    public Expr setMergedProperties(Map<String, Object> map) {
        return setProperties(map,true);
    }

    @Override
    public Expr setProperties(Map<String, Object> map,boolean merge) {
        if(map==null||map.isEmpty()){
            return this;
        }
        return new Any(this,null,map);
    }

    @Override
    public Expr setProperty(String name, Object value) {
        HashMap<String, Object> m = new HashMap<>(1);
        m.put(name, value);
        return setProperties(m,true);
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
        if (name != null) {
            return new Any(this,name,null);
        } else {
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

    @Override
    public boolean isDoubleValue() {
        return false;
    }

    @Override
    public boolean isComplexValue() {
        return false;
    }

    public Expr multiply(int other) {
        return mul(other);
    }

    public Expr multiply(double other) {
        return mul(other);
    }

    public Expr multiply(Expr other) {
        return mul(other);
    }

    public Expr divide(int other) {
        return div(other);
    }

    public Expr divide(double other) {
        return div(other);
    }

    public Expr divide(Expr other) {
        return div(other);
    }

    public Expr subtract(int other) {
        return sub(other);
    }

    public Expr subtract(double other) {
        return sub(other);
    }

    public Expr subtract(Expr other) {
        return sub(other);
    }

    public Expr mul(int other) {
        return Maths.mul(this, other);
    }

    public Expr mul(double other) {
        return Maths.mul(this, other);
    }

    public Expr mul(Expr other) {
        return Maths.mul(this, other);
    }

    public Expr add(int other) {
        return Maths.add(this, other);
    }

    public Expr add(double other) {
        return Maths.add(this, other);
    }

    public Expr add(Expr other) {
        return Maths.add(this, other);
    }

    public Expr div(int other) {
        return Maths.div(this, other);
    }

    public Expr div(double other) {
        return Maths.div(this, other);
    }

    public Expr div(Expr other) {
        return Maths.div(this, other);
    }

    public Expr sub(int other) {
        return Maths.sub(this, other);
    }

    public Expr sub(double other) {
        return Maths.sub(this, other);
    }

    public Expr sub(Expr other) {
        return Maths.sub(this, other);
    }

    public Expr mul(Domain domain) {
//        return mul(Maths.expr(domain));
        return mul((Expr)domain);
    }

    public Expr mul(Geometry domain) {
        return mul(Maths.expr(domain));
    }

    public Expr multiply(Domain domain) {
        return mul(domain);
    }

    public Expr multiply(Geometry domain) {
        return mul(domain);
    }

    public Expr negate() {
        return neg();
    }

    public Expr neg() {
        return Maths.neg(this);
    }

}
