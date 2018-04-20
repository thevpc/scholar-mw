/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format;

/**
 * @author vpc
 */
public interface Formatter<T> {

    void format(StringBuilder sb, T o, FormatParamSet format);

    String format(T o, FormatParamSet format);
}
