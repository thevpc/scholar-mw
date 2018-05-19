package net.vpc.scholar.hadrumaths.format;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 23 juil. 2005 Time: 11:07:07 To
 * change this template use File | Settings | File Templates.
 */
public class FormatParamArray implements Cloneable {
    private static final FormatParam[] EMPTY_AA = new FormatParam[0];

    private Map<Class, FormatParam> paramsTable;

    public FormatParamArray(FormatParam[] array) {
        for (int i = 0; i < array.length; i++) {
            FormatParam pp = array[i];
            if (paramsTable != null && paramsTable.containsKey(pp.getClass())) {
                throw new IllegalArgumentException("Param " + pp.getClass().getSimpleName());
            }
            if (paramsTable == null) {
                paramsTable = new HashMap<>(array.length);
            }
            paramsTable.put(pp.getClass(), pp);
        }
    }

    public FormatParamArray remove(Class paramClass) {
        if (paramsTable != null) {
            paramsTable.remove(paramClass);
        }
        return this;
    }

    public FormatParamArray remove(FormatParam param) {
        if (paramsTable != null) {
            paramsTable.remove(param.getClass());
        }
        return this;
    }

    public FormatParamArray set(FormatParam param) {
        if (paramsTable == null) {
            paramsTable = new HashMap<>();
        }
        paramsTable.put(param.getClass(), param);
        return this;
    }

    public <T extends FormatParam> T getParam(T param) {
        FormatParam p = getParam(param.getClass(), false);
        return (T) (p == null ? param : p);
    }

    public <T extends FormatParam> T getParam(Class<T> paramClass, boolean required) {
        FormatParam p = paramsTable == null ? null : paramsTable.get(paramClass);
        if (p == null && required) {
            throw new NoSuchElementException(paramClass.getSimpleName() + " is requiered");
        }
        return (T) p;
    }

    public FormatParam[] toArray() {
        return ((paramsTable == null || paramsTable.size() == 0) ? EMPTY_AA : paramsTable.values().toArray(new FormatParam[paramsTable.size()]));
    }

    public FormatParamArray copy() {
        try {
            return (FormatParamArray) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public FormatParamArray clone() {
        try {
            return (FormatParamArray) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
