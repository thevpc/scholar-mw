package net.vpc.scholar.hadrumaths.interop.matlab.params;

import net.vpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:21:16
 * To change this template use File | Settings | File Templates.
 */
public class MatlabXFormat implements ToMatlabStringParam {
    private final String name;

    public MatlabXFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
