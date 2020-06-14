package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParam;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 24 juil. 2005
 * Time: 12:26:42
 * To change this template use File | Settings | File Templates.
 */
public class ScalarProductObjectFormatParam implements ObjectFormatParam {
    private final Type type;

    public ScalarProductObjectFormatParam(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScalarProductObjectFormatParam that = (ScalarProductObjectFormatParam) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    public enum Type {DBLQUAD, INT}
}
