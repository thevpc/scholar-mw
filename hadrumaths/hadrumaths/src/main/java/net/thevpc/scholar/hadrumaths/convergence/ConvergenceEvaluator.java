/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package net.thevpc.scholar.hadrumaths.convergence;

/**
 *
 * @author vpc
 */
public interface ConvergenceEvaluator<T> {

    T next(long iteration);
    
}
