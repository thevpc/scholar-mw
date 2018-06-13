/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format;

/**
 * @author vpc
 */
public interface ObjectFormat<T> {

    void format(StringBuilder sb, T o, ObjectFormatParamSet format);

    default String format(T o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();
    }

}
