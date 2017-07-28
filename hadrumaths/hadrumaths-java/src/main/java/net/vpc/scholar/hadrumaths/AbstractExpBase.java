package net.vpc.scholar.hadrumaths;

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
    public Domain domain() {
        return getDomain();
    }



}
