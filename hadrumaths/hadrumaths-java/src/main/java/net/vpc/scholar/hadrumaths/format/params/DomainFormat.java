package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.FormatParam;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 23 juil. 2005 Time: 11:21:16 To
 * change this template use File | Settings | File Templates.
 */
public class DomainFormat implements FormatParam {

    public enum Type {

        NONE, GATE
    };
    private Type type;
    private boolean ignoreFull;

    public DomainFormat(Type type,boolean ignoreFull) {
        this.type = type;
        this.ignoreFull = ignoreFull;
    }

    public boolean isIgnoreFull() {
        return ignoreFull;
    }

    public Type getType() {
        return type;
    }
}
