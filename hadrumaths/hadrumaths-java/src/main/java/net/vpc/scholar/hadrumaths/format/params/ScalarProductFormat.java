package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.FormatParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 24 juil. 2005
 * Time: 12:26:42
 * To change this template use File | Settings | File Templates.
 */
public class ScalarProductFormat implements FormatParam{
    public static enum Type{DBLQUAD,INT};
    private Type type;

    public ScalarProductFormat(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
