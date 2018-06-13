package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParam;

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
    private String name;
    public ComplexIObjectFormatParam(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
