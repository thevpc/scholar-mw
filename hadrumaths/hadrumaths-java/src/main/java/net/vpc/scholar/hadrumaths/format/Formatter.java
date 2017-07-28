/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.format;

/**
 *
 * @author vpc
 */
public interface Formatter<T> {

    public String format(T o, FormatParam... format);
}
