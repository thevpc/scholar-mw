package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;

public abstract class AbstractObjectFormat<T> implements ObjectFormat<T> {
    @Override
    public String format(T o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }
}
