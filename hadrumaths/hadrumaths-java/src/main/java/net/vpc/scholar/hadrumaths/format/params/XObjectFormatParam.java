package net.vpc.scholar.hadrumaths.format.params;

import net.vpc.scholar.hadrumaths.format.ObjectFormatParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:21:16
 * To change this template use File | Settings | File Templates.
 */
public class XObjectFormatParam implements ObjectFormatParam {
    private String name;

    public XObjectFormatParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}