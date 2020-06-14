package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParam;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 23 juil. 2005 Time: 11:21:16 To
 * change this template use File | Settings | File Templates.
 */
public class DomainObjectFormatParam implements ObjectFormatParam {

    private final Type type;

    private final boolean ignoreFull;
    public DomainObjectFormatParam(Type type, boolean ignoreFull) {
        this.type = type;
        this.ignoreFull = ignoreFull;
    }

    public boolean isIgnoreFull() {
        return ignoreFull;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainObjectFormatParam that = (DomainObjectFormatParam) o;
        return ignoreFull == that.ignoreFull &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, ignoreFull);
    }

    public enum Type {

        NONE, GATE
    }
}
