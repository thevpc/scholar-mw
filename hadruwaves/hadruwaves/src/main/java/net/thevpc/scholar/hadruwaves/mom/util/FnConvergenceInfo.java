package net.thevpc.scholar.hadruwaves.mom.util;

import java.io.Serializable;

/**
 * @author Taha BEN SALAH (taha.bensalah@gmail.com)
 * @creationtime 21 nov. 2006 13:31:13
 */
public class FnConvergenceInfo implements Serializable {
    private boolean enabled;
    private double maxError;
    private int maxCount;
    private int step;

    public FnConvergenceInfo(boolean convEnabled, double error, int maxFn, int step) {
        this.enabled = convEnabled;
        this.maxError = error;
        this.maxCount = maxFn;
        this.step = step;
        if(convEnabled && (error<=0 || Double.isNaN(error))){
            throw new IllegalArgumentException("FnConvergenceEnabled and FnConvergenceError<=0");
        }
        if(convEnabled && maxFn<=0){
            throw new IllegalArgumentException("FnConvergenceEnabled and FnConvergenceMax<=0");
        }
        if(convEnabled && step<=0){
            throw new IllegalArgumentException("FnConvergenceEnabled and FnConvergenceStep<=0");
        }
    }


    public boolean isEnabled() {
        return enabled;
    }

    public double getMaxError() {
        return maxError;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getStep() {
        return step;
    }

    public String toString() {
        return "FnConvergenceInfo(enabled="+enabled+",maxError="+maxError+",maxCount="+maxCount+",step="+step+")";
    }
}
