package net.vpc.scholar.hadrumaths;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vpc on 4/7/17.
 */
public class ComplexExt extends Complex {
    private String name;
    private Map<String, Object> properties;

    public ComplexExt(double real, double imag, String name,Map<String, Object> properties) {
        super(real, imag);
        this.name = name;
        this.properties = properties==null?null:new HashMap<>(properties);
    }

    @Override
    public Expr setName(String name) {
        return new ComplexExt(getReal(), getImag(), name,properties);
    }

    @Override
    public Complex setProperties(Map<String, Object> map) {
        Map<String, Object> properties2 = null;
        if(map!=null || properties!=null){
            properties2=new HashMap<>((properties==null?0:properties.size())+(map==null?0:map.size())+1);
            if(properties!=null) {
                properties2.putAll(properties);
            }
            if(map!=null) {
                properties2.putAll(map);
            }
        }
        return new ComplexExt(getReal(), getImag(), name,properties2);
    }

    @Override
    public Complex setProperty(String name, Object value) {
        Map<String, Object> properties2 = properties==null?new HashMap<>(2):new HashMap<>(properties);
        properties2.put(name,value);
        return new ComplexExt(getReal(), getImag(), name,properties2);
    }

    @Override
    public String getName() {
        return name;
    }

    public Object getProperty(String name) {
        return properties != null ? properties.get(name) : null;
    }

    public boolean hasProperties() {
        return properties != null && properties.size() > 0;
    }

    @Override
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<String, Object>();
        }
        return properties;
    }
}
