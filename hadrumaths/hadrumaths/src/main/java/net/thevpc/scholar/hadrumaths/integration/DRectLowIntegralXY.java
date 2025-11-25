package net.thevpc.scholar.hadrumaths.integration;


import net.thevpc.nuts.elem.NElement;

import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;

/**
 * Created by IntelliJ IDEA. User: vpc Date: 29 juil. 2005 Time: 18:55:20 To
 * change this template use File | Settings | File Templates.
 */
public class DRectLowIntegralXY implements DIntegralXY {
    private static final long serialVersionUID = 1L;

    private int xprecision;
    private int yprecision;
    private int zprecision;

    public DRectLowIntegralXY(int xprecision, int yprecision, int zprecision) {
        this.xprecision = xprecision;
        if (xprecision <= 0) {
            if (xprecision == 0) {
                this.xprecision = 1000;
            } else {
                throw new IllegalArgumentException("non positive precision");
            }
        }
        this.yprecision = yprecision;
        if (yprecision <= 0) {
            if (yprecision == 0) {
                this.yprecision = 1000;
            } else {
                throw new IllegalArgumentException("non positive precision");
            }
        }
        this.zprecision = zprecision;
        if (zprecision <= 0) {
            if (zprecision == 0) {
                this.zprecision = 1000;
            } else {
                throw new IllegalArgumentException("non positive precision");
            }
        }
    }

    @Override
    public double integrateX(DoubleToDouble f, double xmin, double xmax) {
        return integrateX(f, 0, xmin, xmax);
    }

    @Override
    public double integrateX(DoubleToDouble f, double y, double xmin, double xmax) {
// Check data.
        if (xmax <= xmin) {
            return 0.0;
        }
        double dx = (xmax - xmin) / xprecision;
        double vol = 0.0;
        double[] xx = new double[xprecision];
        for (int i = 0; i < xprecision; i++) {
            xx[i] = xmin + i * dx;
        }
        double[] h = f.evalDouble(xx, y, null, null);
        for (double v : h) {
            vol += v;
        }
        vol *= dx;
        return vol;
    }

    @Override
    public double integrateY(DoubleToDouble f, double x, double ymin, double ymax) {
// Check data.
        if (ymax <= ymin) {
            return 0.0;
        }
        double dy = (ymax - ymin) / xprecision;
        double vol = 0.0;
        double[] yy = new double[xprecision];
        for (int i = 0; i < xprecision; i++) {
            yy[i] = ymin + i * dy;
        }
        double[] h = f.evalDouble(x, yy, null, null);
        for (double v : h) {
            vol += v;
        }
        vol *= dy;
        return vol;
    }

    public double integrateXY(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax) {
        // Check data.
        if (xmax <= xmin || ymax <= ymin) {
            return 0.0;
        }

        double dx = (xmax - xmin) / xprecision;
        double dy = (ymax - ymin) / yprecision;

        double vol = 0.0;

        double[] xx = new double[xprecision];
        for (int i = 0; i < xprecision; i++) {
            xx[i] = xmin + i * dx;
        }

        double[] yy = new double[yprecision];
        for (int i = 0; i < xprecision; i++) {
            yy[i] = ymin + i * dy;
        }


        double dxy = dx * dy;

        double[][] h = f.evalDouble(xx, yy, null, null);
        for (double[] hx : h) {
            for (double hxy : hx) {
                vol += hxy;
            }
        }
        vol *= dxy;
        return vol;
    }

    public double integrateXYZ(DoubleToDouble f, double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        // Check data.
        if (xmax <= xmin || ymax <= ymin || zmax <= zmin) {
            return 0.0;
        }

        double dx = (xmax - xmin) / xprecision;
        double dy = (ymax - ymin) / yprecision;
        double dz = (zmax - zmin) / zprecision;

        double vol = 0.0;

        double[] xx = new double[xprecision];
        for (int i = 0; i < xprecision; i++) {
            xx[i] = xmin + i * dx;
        }

        double[] yy = new double[yprecision];
        for (int i = 0; i < xprecision; i++) {
            yy[i] = ymin + i * dy;
        }

        double[] zz = new double[zprecision];
        for (int i = 0; i < zprecision; i++) {
            zz[i] = zmin + i * dz;
        }


        double dxy = dx * dy * dz;

        double[][][] h = f.evalDouble(xx, yy, zz, null, null);
        for (double[][] hz : h) {
            for (double[] hx : hz) {
                for (double hxy : hx) {
                    vol += hxy;
                }
            }
        }
        vol *= dxy;
        return vol;
    }

    @Override
    public NElement toElement() {
        return NElement.ofUplet(getClass().getSimpleName(), NElement.ofInt(xprecision), NElement.ofInt(yprecision), NElement.ofInt(zprecision))
                ;
    }

//    @Override
//    public String dump() {
//        Dumper h = new Dumper(getClass().getSimpleName());
//        h.add("xprecision", xprecision);
//        h.add("yprecision", yprecision);
//        h.add("zprecision", zprecision);
//        return h.toString();
//    }

    @Override
    public int hashCode() {
        int result = xprecision;
        result = 31 * result + yprecision;
        result = 31 * result + zprecision;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DRectLowIntegralXY)) return false;

        DRectLowIntegralXY that = (DRectLowIntegralXY) o;

        if (xprecision != that.xprecision) return false;
        if (yprecision != that.yprecision) return false;
        return zprecision == that.zprecision;
    }

    @Override
    public String toString() {
        return "DRectLowIntegralXY";
    }

}
