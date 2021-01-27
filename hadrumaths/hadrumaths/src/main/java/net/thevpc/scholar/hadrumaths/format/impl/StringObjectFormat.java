/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;

/**
 * @author vpc
 */
public class StringObjectFormat extends AbstractObjectFormat<String> {

    @Override
    public void format(String o, ObjectFormatContext context) {
        context.append(o);
    }

}
