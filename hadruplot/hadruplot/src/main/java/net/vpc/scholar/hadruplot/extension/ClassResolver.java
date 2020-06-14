/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot.extension;

/**
 *
 * @author vpc
 */
public interface ClassResolver<T> {
    T resolve(Object o);
}
