package net.vpc.scholar.hadrumaths.format;

public interface ObjectFormatContext {
    <T extends ObjectFormatParam> T getParam(T param);

    <T extends ObjectFormatParam> T getParam(Class<T> paramClass, boolean required);

    boolean containsParam(ObjectFormatParam param);

    ObjectFormatParamSet getParams();

    ObjectFormatContext append(String o);

    ObjectFormatContext append(Object o);

    ObjectFormatContext format(Object o);

    ObjectFormatContext format(Object o, ObjectFormatParamSet paramSet);

    long length();

    ObjectFormatContext insert(long initialLength, String s);
}
