/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.matlab;

/**
 * @author vpc
 */
public interface ToMatlabString<T> {
    public String toMatlabString(T o, ToMatlabStringParam... format);
}
