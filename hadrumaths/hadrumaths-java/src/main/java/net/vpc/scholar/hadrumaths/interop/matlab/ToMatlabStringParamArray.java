package net.vpc.scholar.hadrumaths.interop.matlab;

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
public class ToMatlabStringParamArray implements Cloneable {
    private Map<Class, ToMatlabStringParam> paramsTable;

    public ToMatlabStringParamArray(ToMatlabStringParam[] array) {
        paramsTable = new HashMap<Class, ToMatlabStringParam>(array.length);
        for (int i = 0; i < array.length; i++) {
            ToMatlabStringParam toMatlabStringParam = array[i];
            if (paramsTable.containsKey(toMatlabStringParam.getClass())) {
                throw new IllegalArgumentException("Param " + toMatlabStringParam.getClass().getSimpleName());
            }
            paramsTable.put(toMatlabStringParam.getClass(), toMatlabStringParam);
        }
    }

    public ToMatlabStringParamArray remove(Class paramClass) {
        paramsTable.remove(paramClass);
        return this;
    }

    public ToMatlabStringParamArray remove(ToMatlabStringParam param) {
        paramsTable.remove(param.getClass());
        return this;
    }

    public ToMatlabStringParamArray set(ToMatlabStringParam param) {
        paramsTable.put(param.getClass(), param);
        return this;
    }

    public <T extends ToMatlabStringParam> T getParam(T param) {
        ToMatlabStringParam p = getParam(param.getClass(), false);
        return (T) (p == null ? param : p);
    }

    public ToMatlabStringParam getParam(Class paramClass, boolean required) {
        ToMatlabStringParam p = paramsTable.get(paramClass);
        if (p == null && required) {
            throw new NoSuchElementException(paramClass.getSimpleName() + " is requiered");
        }
        return p;
    }

    public ToMatlabStringParam[] toArray() {
        return (ToMatlabStringParam[]) paramsTable.values().toArray(new ToMatlabStringParam[paramsTable.size()]);
    }

    public ToMatlabStringParamArray clone() {
        try {
            return (ToMatlabStringParamArray) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
