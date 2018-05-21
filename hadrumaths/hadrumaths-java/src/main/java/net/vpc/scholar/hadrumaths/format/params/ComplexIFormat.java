package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.FormatParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:21:16
 * To change this template use File | Settings | File Templates.
 */
public class ComplexIFormat implements FormatParam {
    public static ComplexIFormat I = new ComplexIFormat("i");
    public static ComplexIFormat J = new ComplexIFormat("j");
    public static ComplexIFormat I_HAT = new ComplexIFormat("î");
    private String name;
    public ComplexIFormat(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
