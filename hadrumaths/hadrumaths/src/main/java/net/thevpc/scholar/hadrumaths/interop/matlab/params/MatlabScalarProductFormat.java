package net.thevpc.scholar.hadrumaths.interop.matlab.params;

import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 24 juil. 2005
 * Time: 12:26:42
 * To change this template use File | Settings | File Templates.
 */
public class MatlabScalarProductFormat implements ToMatlabStringParam {
    private final Type type;

    public MatlabScalarProductFormat(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type {DBLQUAD, INT}
}
