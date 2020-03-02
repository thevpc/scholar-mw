package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParam;

import java.util.Objects;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:21:16
 * To change this template use File | Settings | File Templates.
 */
public class ComplexIObjectFormatParam implements ObjectFormatParam {
    public static ComplexIObjectFormatParam I = new ComplexIObjectFormatParam("i");
    public static ComplexIObjectFormatParam J = new ComplexIObjectFormatParam("j");
    public static ComplexIObjectFormatParam I_HAT = new ComplexIObjectFormatParam("î");
    private final String name;

    public ComplexIObjectFormatParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexIObjectFormatParam that = (ComplexIObjectFormatParam) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
