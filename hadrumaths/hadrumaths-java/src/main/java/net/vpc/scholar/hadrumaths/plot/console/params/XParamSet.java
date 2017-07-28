package net.vpc.scholar.hadrumaths.plot.console.params;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.VoidParam;


public class XParamSet extends DoubleArrayParamSet implements Cloneable{
    private double[] y;
    private double[] z;

//    public XParamSet(double value) {
//        super("X", value);
//    }
//
//    public XParamSet(double min, double max, int times) {
//        super("X",min, max, times);
//    }
//
//    public XParamSet(double min, double max, double step) {
//        super("X", min, max, step);
//    }

    public XParamSet(int x, int y) {
        this(x,y,1);
    }
    public XParamSet(int x) {
        this(x,1,1);
    }
    public XParamSet(int x, int y, int z) {
        this(
                Maths.dtimes(0.0, 1.0, x),
                Maths.dtimes(0.0, 1.0, y),
                Maths.dtimes(0.0, 1.0, z)
        );
    }
    public XParamSet(double[] values) {
        super(new VoidParam("X"), values);
    }

    public XParamSet(double[] x, double[] y) {
        super(new VoidParam("XY"),x);
        this.y=y;
    }

    public XParamSet(double[] x, double[] y, double[] z) {
        super(new VoidParam("XYZ"),x);
        this.y=y;
        this.z=z;
    }

    public double[] getY() {
        return y;
    }

    public double[] getZ() {
        return z;
    }

}
