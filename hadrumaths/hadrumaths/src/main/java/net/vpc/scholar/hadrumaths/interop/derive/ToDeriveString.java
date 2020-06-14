/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.interop.derive;

/**
 * @author vpc
 */
public interface ToDeriveString<T> {
    String toDeriveString(T o, ToDeriveStringParam... format);
}
