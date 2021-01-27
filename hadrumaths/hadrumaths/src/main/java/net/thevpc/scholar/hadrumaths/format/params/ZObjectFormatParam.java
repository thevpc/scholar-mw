package net.thevpc.scholar.hadrumaths.format.params;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatParam;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:21:16
 * To change this template use File | Settings | File Templates.
 */
public class ZObjectFormatParam implements ObjectFormatParam {
    private final String name;

    public ZObjectFormatParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZObjectFormatParam that = (ZObjectFormatParam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
