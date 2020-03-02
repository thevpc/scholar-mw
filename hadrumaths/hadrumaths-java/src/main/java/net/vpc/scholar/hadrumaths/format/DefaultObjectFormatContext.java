package net.vpc.scholar.hadrumaths.format;

import net.vpc.scholar.hadrumaths.FormatFactory;

public class DefaultObjectFormatContext implements ObjectFormatContext {
    private final ObjectFormatParamSet paramSet;
    private final StringBuilder out;

    public DefaultObjectFormatContext(StringBuilder out, ObjectFormatParamSet paramSet) {
        this.paramSet = paramSet;
        this.out = out;
    }
    public long length(){
        return out.length();
    }

    @Override
    public <T extends ObjectFormatParam> T getParam(T param) {
        return paramSet.getParam(param);
    }

    @Override
    public <T extends ObjectFormatParam> T getParam(Class<T> paramClass, boolean required) {
        return paramSet.getParam(paramClass, required);
    }

    @Override
    public boolean containsParam(ObjectFormatParam param) {
        return paramSet.containsParam(param);
    }

    @Override
    public ObjectFormatParamSet getParams() {
        return paramSet;
    }

   @Override
    public ObjectFormatContext append(String o) {
        out.append(o);
        return this;
    }

    @Override
    public ObjectFormatContext append(Object o) {
        out.append(o);
        return this;
    }

    @Override
    public ObjectFormatContext format(Object o) {
        FormatFactory.format(o, this);
        return this;
    }

    @Override
    public ObjectFormatContext format(Object o, ObjectFormatParamSet paramSet) {
        DefaultObjectFormatContext u = new DefaultObjectFormatContext(out, paramSet == null ? this.paramSet : paramSet);
        FormatFactory.format(o, u);
        return this;
    }

    public StringBuilder getOut() {
        return out;
    }

    @Override
    public ObjectFormatContext insert(long initialLength, String s) {
        out.insert((int)initialLength,s);
        return this;
    }
}
