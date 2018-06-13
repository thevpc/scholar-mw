package net.vpc.scholar.hadrumaths.format;

import java.util.*;

public class ObjectFormatParamSet {
    public static final ObjectFormatParamSet EMPTY = new ObjectFormatParamSet();
    private Map<Class, ObjectFormatParam> values = new HashMap<>();

    public ObjectFormatParamSet(ObjectFormatParam... values) {
        for (ObjectFormatParam value : values) {
            add0(value);
        }
    }

    public ObjectFormatParamSet(Collection<ObjectFormatParam> values) {
        for (ObjectFormatParam value : values) {
            add0(value);
        }
    }

    private ObjectFormatParamSet(Set<ObjectFormatParam> values) {
        this.values = new HashMap<>();
        for (ObjectFormatParam value : values) {
            add0(value);
        }
    }

    private void remove0(ObjectFormatParam a) {
        values.remove(a.getClass());
    }

    private void add0(ObjectFormatParam a) {
        if (a != null) {
            values.put(a.getClass(), a);
        }
    }

    public ObjectFormatParamSet add(ObjectFormatParam a) {
        if (a != null) {
            if (!values.containsKey(a.getClass())) {
                ObjectFormatParamSet rr = new ObjectFormatParamSet(values.values());
                rr.add0(a);
                return rr;
            } else if (!values.get(a.getClass()).equals(a)) {
                ObjectFormatParamSet rr = new ObjectFormatParamSet(values.values());
                rr.add0(a);
                return rr;
            }
        }
        return this;
    }

    public ObjectFormatParamSet remove(Class a) {
        if (a != null) {
            if (values.containsKey(a)) {
                ObjectFormatParamSet rr = new ObjectFormatParamSet(values.values());
                rr.values.remove(a);
                return rr;
            }
        }
        return this;
    }

    public ObjectFormatParamSet remove(ObjectFormatParam a) {
        if (a != null) {
            if (values.containsKey(a.getClass())) {
                ObjectFormatParamSet rr = new ObjectFormatParamSet(values.values());
                rr.remove0(a);
                return rr;
            }
        }
        return this;
    }

    public ObjectFormatParamSet add(ObjectFormatParam... all) {
        Set<ObjectFormatParam> values0 = null;
        for (ObjectFormatParam a : all) {
            if (a != null) {
                if (values0 != null) {
                    values0.add(a);
                } else if (!values.containsKey(a.getClass())) {
                    values0 = new HashSet<>(values.values());
                    values0.add(a);
                } else if (!values.get(a.getClass()).equals(a)) {
                    values0 = new HashSet<>(values.values());
                    values0.add(a);
                }
            }
        }
        if (values0 != null) {
            return new ObjectFormatParamSet(values0);
        }
        return this;
    }

    public boolean containsParam(ObjectFormatParam param) {
        Object pp = getParam(param.getClass(), false);
        return pp != null && pp.equals(param);
    }

    public <T extends ObjectFormatParam> T getParam(T param) {
        ObjectFormatParam p = getParam(param.getClass(), false);
        return (T) (p == null ? param : p);
    }

    public <T extends ObjectFormatParam> T getParam(Class<T> paramClass, boolean required) {
        ObjectFormatParam p = values.get(paramClass);
        if (p == null && required) {
            throw new NoSuchElementException(paramClass.getSimpleName() + " is requiered");
        }
        return (T) p;
    }

    public <T extends ObjectFormatParam> T getParam(Class<T> paramClass, T defaultValue) {
        ObjectFormatParam p = values.get(paramClass);
        if (p == null ) {
            return defaultValue;
        }
        return (T) p;
    }
}
