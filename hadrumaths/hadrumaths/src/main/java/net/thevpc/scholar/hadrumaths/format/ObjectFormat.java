/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.format;

/**
 * @author vpc
 */
public interface ObjectFormat<T> {

    default String format(T o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();
    }

    void format(T o, ObjectFormatContext context);

}
