package net.vpc.scholar.hadrumaths.interop.matlab.params;

import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 24 juil. 2005
 * Time: 12:26:42
 * To change this template use File | Settings | File Templates.
 */
public class MatlabScalarProductFormat implements ToMatlabStringParam {
    public static enum Type {DBLQUAD, INT}

    ;
    private Type type;

    public MatlabScalarProductFormat(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
