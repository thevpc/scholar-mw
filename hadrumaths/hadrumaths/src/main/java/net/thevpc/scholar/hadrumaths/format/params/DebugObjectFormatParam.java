package net.thevpc.scholar.hadrumaths.format.params;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatParam;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 23 juil. 2005
 * Time: 11:21:16
 * To change this template use File | Settings | File Templates.
 */
public class DebugObjectFormatParam implements ObjectFormatParam {
    public static DebugObjectFormatParam INSTANCE = new DebugObjectFormatParam();

    public DebugObjectFormatParam() {
    }
    @Override
    public boolean equals(Object obj) {
        return getClass().getName().equals(obj.getClass().getName());
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }}
