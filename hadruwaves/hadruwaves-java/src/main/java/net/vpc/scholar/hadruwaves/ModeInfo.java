package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;

import java.io.Serializable;

public final class ModeInfo implements Serializable,Cloneable {
    public int initialIndex;
    public int index;
    public final ModeIndex mode;
    public double cutOffFrequency;
    public Complex firstBoxSpaceGamma;
    public Complex secondBoxSpaceGamma;
    public Complex impedance;
    public boolean propagating;
    public DoubleToVector fn;


    public ModeInfo(ModeIndex mode) {
        this.mode = mode;
    }
    public ModeInfo(ModeType _mode, int m, int n) {
        this(ModeIndex.mode(_mode,m,n));
    }

    public ModeInfo(ModeType _mode, int m, int n, int index) {
        this.mode = ModeIndex.mode(_mode,m,n);
        this.initialIndex = index;
    }

    public ModeInfo copy(){
        try {
            ModeInfo mi=(ModeInfo) this.clone();
            mi.fn=(DoubleToVector) mi.fn.clone();
            return mi;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Never");
        }
    }

    public static DoubleToVector[] fn(ModeInfo[] indexes){
        DoubleToVector[] r=new DoubleToVector[indexes.length];
        for (int i = 0; i < r.length; i++) {
            r[i]=indexes[i].fn;
        }
        return r;
    }
    
    @Override
    public String toString() {
        return "["+mode+";fc="+ cutOffFrequency +";#"+(initialIndex +1)+"]";
    }

    public ModeIndex getMode() {
        return mode;
    }

    //    public int compareTo(ModeInfo o) {
//        if(o==null){
//            return 1;
//        }
//        double f0= cutOffFrequency;
//        double i0= initialIndex;
//        double f=o.cutOffFrequency;
//        double i=o.initialIndex;
//        if(f0>f || (f0==f && i0>i)){
//            return 1;
//        }else if(f0<f || (f0==f && i0<i)){
//            return -1;
//        }else{
//            return 1;
//        }
//    }
}
