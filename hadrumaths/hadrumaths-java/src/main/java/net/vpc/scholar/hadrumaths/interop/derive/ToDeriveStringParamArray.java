package net.vpc.scholar.hadrumaths.interop.derive;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:07:07
 * To change this template use File | Settings | File Templates.
 */
public class ToDeriveStringParamArray implements Cloneable {
    private final Map<Class, ToDeriveStringParam> paramsTable;

    public ToDeriveStringParamArray(ToDeriveStringParam[] array) {
        paramsTable = new HashMap<Class, ToDeriveStringParam>(array.length);
        for (int i = 0; i < array.length; i++) {
            ToDeriveStringParam toMatlabStringParam = array[i];
            if (paramsTable.containsKey(toMatlabStringParam.getClass())) {
                throw new IllegalArgumentException("CParam " + toMatlabStringParam.getClass().getSimpleName());
            }
            paramsTable.put(toMatlabStringParam.getClass(), toMatlabStringParam);
        }
    }

    public ToDeriveStringParamArray remove(Class paramClass) {
        paramsTable.remove(paramClass);
        return this;
    }

    public ToDeriveStringParamArray remove(ToDeriveStringParam param) {
        paramsTable.remove(param.getClass());
        return this;
    }

    public ToDeriveStringParamArray set(ToDeriveStringParam param) {
        paramsTable.put(param.getClass(), param);
        return this;
    }

    public ToDeriveStringParam getParam(ToDeriveStringParam param) {
        ToDeriveStringParam p = getParam(param.getClass(), false);
        return p == null ? param : p;
    }

    public ToDeriveStringParam getParam(Class paramClass, boolean required) {
        ToDeriveStringParam p = paramsTable.get(paramClass);
        if (p == null && required) {
            throw new NoSuchElementException(paramClass.getSimpleName() + " is requiered");
        }
        return p;
    }

    public ToDeriveStringParam[] toArray() {
        return paramsTable.values().toArray(new ToDeriveStringParam[0]);
    }

    public ToDeriveStringParamArray clone() {
        try {
            return (ToDeriveStringParamArray) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
