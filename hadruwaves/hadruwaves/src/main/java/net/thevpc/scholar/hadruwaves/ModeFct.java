package net.thevpc.scholar.hadruwaves;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;

import java.io.Serializable;

public final class ModeFct implements Serializable,Cloneable {
    public final int m;
    public final int n;
    public int index;
    public final ModeType mode;
    public DoubleToVector fn;

    public ModeFct(ModeIndex mode, DoubleToVector fn) {
        this.m = mode.m();
        this.n = mode.n();
        this.mode = mode.type();
        this.fn = fn;
    }

    public ModeIndex modeIndex(){
        return mode.index(m,n);
    }

    @Override
    public String toString() {
        return mode.toString()+m+"."+n+"::"+fn.toString();
    }
}
