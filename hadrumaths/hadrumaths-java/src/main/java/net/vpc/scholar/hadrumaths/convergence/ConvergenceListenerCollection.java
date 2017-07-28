/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.convergence;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author vpc
 */
public class ConvergenceListenerCollection implements ConvergenceListener{
    private ArrayList<ConvergenceListener> others=new ArrayList<ConvergenceListener>();

    public ConvergenceListenerCollection(ConvergenceListener ... others) {
        this.others.addAll(Arrays.asList(others));
    }
    
    public void add(ConvergenceListener ... others){
        this.others.addAll(Arrays.asList(others));
    }

    @Override
    public void progress(ConvergenceResult result) {
        for (ConvergenceListener other : others) {
            other.progress(result);
        }
    }
    
    
}
