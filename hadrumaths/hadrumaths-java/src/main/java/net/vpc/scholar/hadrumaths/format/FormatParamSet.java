package net.vpc.scholar.hadrumaths.format;

import java.util.*;

public class FormatParamSet {
    public static final FormatParamSet EMPTY=new FormatParamSet();
    private Map<Class, FormatParam> values = new HashMap<>();

    public FormatParamSet(FormatParam... values) {
        for (FormatParam value : values) {
            add0(value);
        }
    }

    public FormatParamSet(Collection<FormatParam> values) {
        for (FormatParam value : values) {
            add0(value);
        }
    }

    private FormatParamSet(Set<FormatParam> values) {
        this.values = new HashMap<>();
        for (FormatParam value : values) {
           add0(value);
        }
    }

    private void remove0(FormatParam a) {
        values.remove(a.getClass());
    }

    private void add0(FormatParam a) {
        if (a != null) {
            values.put(a.getClass(), a);
        }
    }

    public FormatParamSet add(FormatParam a) {
        if (a != null) {
            if(!values.containsKey(a.getClass())) {
                FormatParamSet rr = new FormatParamSet(values.values());
                rr.add0(a);
                return rr;
            }else if(!values.get(a.getClass()).equals(a)){
                FormatParamSet rr = new FormatParamSet(values.values());
                rr.add0(a);
                return rr;
            }
        }
        return this;
    }

    public FormatParamSet remove(Class a) {
        if (a != null) {
            if(values.containsKey(a)) {
                FormatParamSet rr = new FormatParamSet(values.values());
                rr.values.remove(a);
                return rr;
            }
        }
        return this;
    }

    public FormatParamSet remove(FormatParam a) {
        if (a != null) {
            if(values.containsKey(a.getClass())) {
                FormatParamSet rr = new FormatParamSet(values.values());
                rr.remove0(a);
                return rr;
            }
        }
        return this;
    }

    public FormatParamSet add(FormatParam... all) {
        Set<FormatParam> values0 = null;
        for (FormatParam a : all) {
            if (a != null) {
                if(values0!=null){
                    values0.add(a);
                }else if(!values.containsKey(a.getClass())){
                    values0 = new HashSet<>(values.values());
                    values0.add(a);
                }else if(!values.get(a.getClass()).equals(a)){
                    values0 = new HashSet<>(values.values());
                    values0.add(a);
                }
            }
        }
        if (values0 != null) {
            return new FormatParamSet(values0);
        }
        return this;
    }

    public boolean containsParam(FormatParam param) {
        Object pp = getParam(param.getClass(), false);
        return pp !=null && pp.equals(param);
    }

    public <T extends FormatParam> T getParam(T param) {
        FormatParam p = getParam(param.getClass(), false);
        return (T) (p == null ? param : p);
    }

    public <T extends FormatParam> T getParam(Class<T> paramClass, boolean required) {
        FormatParam p = values.get(paramClass);
        if (p == null && required) {
            throw new NoSuchElementException(paramClass.getSimpleName() + " is requiered");
        }
        return (T) p;
    }
}
