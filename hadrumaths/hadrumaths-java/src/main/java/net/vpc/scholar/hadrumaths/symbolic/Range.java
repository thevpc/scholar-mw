package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.BooleanArray1;
import net.vpc.scholar.hadrumaths.BooleanArray2;
import net.vpc.scholar.hadrumaths.BooleanArray3;

/**
 * Created by vpc on 4/17/14.
 */
public class Range {

    public final int xmin;
    public final int xmax;
    public final int ymin;
    public final int ymax;
    public final int zmin;
    public final int zmax;
    public final int xwidth;
    public final int ywidth;
    public final int zwidth;
    public final int dimension;
    private Object defined;

    public Range(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax, int dim) {
        this.dimension = dim;
        this.xmin = xmin;
        this.xmax = xmax;
        switch (dimension) {
            case 1: {
                this.ymin = Integer.MIN_VALUE;
                this.ymax = Integer.MAX_VALUE;
                this.zmin = Integer.MIN_VALUE;
                this.zmax = Integer.MAX_VALUE;
                this.xwidth = xmax - xmin;
                this.ywidth = Integer.MAX_VALUE;
                this.zwidth = Integer.MAX_VALUE;
                if (xwidth < 0) {
                    throw new IllegalArgumentException("xwidth<0");
                }
                break;
            }
            case 2: {
                this.ymin = ymin;
                this.ymax = ymax;
                this.zmin = Integer.MIN_VALUE;
                this.zmax = Integer.MAX_VALUE;
                this.xwidth = xmax - xmin;
                this.ywidth = ymax - ymin;
                this.zwidth = Integer.MAX_VALUE;
                if (xwidth < 0 || ywidth < 0) {
                    throw new IllegalArgumentException("xwidth<0 || ywidth<0");
                }
                break;
            }
            case 3: {
                this.ymin = ymin;
                this.ymax = ymax;
                this.zmin = zmin;
                this.zmax = zmax;
                this.xwidth = xmax - xmin;
                this.ywidth = ymax - ymin;
                this.zwidth = zmax - zmin;
                if (xwidth < 0 || ywidth < 0 || zwidth < 0) {
                    throw new IllegalArgumentException("xwidth<0 || ywidth<0 || zwidth<0");
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid dimension");
            }

        }
        if (xwidth < 0 || ywidth < 0 || zwidth < 0) {
            throw new IllegalArgumentException("xwidth<0 || ywidth<0 || zwidth<0");
        }
    }

    public static Range forBounds(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
        return new Range(xmin, xmax, ymin, ymax, zmin, zmax, 3);
    }

    public static Range forBounds(int xmin, int xmax, int ymin, int ymax) {
        return new Range(xmin, xmax, ymin, ymax, Integer.MIN_VALUE, Integer.MAX_VALUE, 2);
    }

    public static Range forBounds(int xmin, int xmax) {
        return new Range(xmin, xmax, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
    }

    public Range union(Range other) {
        if (other.includes(this)) {
            return other;
        }
        if (this.includes(other)) {
            return this;
        }
        int x1 = Math.min(xmin, other.xmin);
        int x2 = Math.max(xmax, other.xmax);
        int y1 = Math.min(ymin, other.ymin);
        int y2 = Math.max(ymax, other.ymax);
        int z1 = Math.min(zmin, other.zmin);
        int z2 = Math.max(zmax, other.zmax);
        int dim = Math.max(dimension, other.dimension);
        return new Range(x1, x2, y1, y2, z1, z2, dim);
    }

    public BooleanArray3 getDefined3() {
        return (BooleanArray3) defined;
    }

    public BooleanArray2 getDefined2() {
        return (BooleanArray2) defined;
    }

    public BooleanArray1 getDefined1() {
        return (BooleanArray1) defined;
    }

    public Object getDefined() {
        switch (dimension){
            case 1:{
                return (BooleanArray1)defined;
            }
            case 2:{
                return (BooleanArray2)defined;
            }
            case 3:{
                return (BooleanArray3)defined;
            }
        }
        throw new IllegalArgumentException("Invalid Dimension");
    }

    public void setDefined(BooleanArray3 defined) {
        switch (dimension){
            case 3:{
                this.defined=defined;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid Dimension. Expected "+dimension+" but got "+3);
    }

    public void setDefined(BooleanArray2 defined) {
        switch (dimension){
            case 2:{
                this.defined=defined;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid Dimension. Expected "+dimension+" but got "+2);
    }

    public void setDefined(BooleanArray1 defined) {
        switch (dimension){
            case 1:{
                this.defined=defined;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid Dimension. Expected "+dimension+" but got "+1);
    }

    public Range intersect(Range other) {
        if (other.includes(this)) {
            return other;
        }
        if (this.includes(other)) {
            return this;
        }
        int x1 = Math.max(xmin, other.xmin);
        int x2 = Math.min(xmax, other.xmax);
        if(x2<x1){
            return null;
        }
        int y1 = Math.max(ymin, other.ymin);
        int y2 = Math.min(ymax, other.ymax);
        if(y2<y1){
            return null;
        }
        int z1 = Math.max(zmin, other.zmin);
        int z2 = Math.min(zmax, other.zmax);
        if(z2<z1){
            return null;
        }
        int dim = Math.max(dimension, other.dimension);
        return new Range(x1, x2, y1, y2, z1, z2, dim);
    }

    public boolean includes(Range other) {
        int dim = Math.max(dimension, other.dimension);
        switch (dim) {
            case 1: {
                return other.isEmpty()
                        || (other.xmin >= xmin
                        && other.xmax <= xmax
                );
            }
            case 2: {
                return other.isEmpty()
                        || (other.xmin >= xmin
                        && other.xmax <= xmax
                        && other.ymin >= ymin
                        && other.ymax <= ymax);
            }
        }
        return other.isEmpty()
                || (other.xmin >= xmin
                && other.xmax <= xmax
                && other.ymin >= ymin
                && other.ymax <= ymax
                && other.zmin >= zmin
                && other.zmax <= zmax
        );
    }

    public boolean isValid() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        switch (dimension) {
            case 1: {
                return xmin > xmax;
            }
            case 2: {
                return xmin > xmax || ymin > ymax;
            }
        }

        return xmin > xmax || ymin > ymax || zmin > zmax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Range)) return false;

        Range domainXYi = (Range) o;

        if (dimension != domainXYi.dimension) return false;
        if (xmax != domainXYi.xmax) return false;
        if (xmin != domainXYi.xmin) return false;
        if (xwidth != domainXYi.xwidth) return false;
        if (ymax != domainXYi.ymax) return false;
        if (ymin != domainXYi.ymin) return false;
        if (ywidth != domainXYi.ywidth) return false;
        if (zmax != domainXYi.zmax) return false;
        if (zmin != domainXYi.zmin) return false;
        if (zwidth != domainXYi.zwidth) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = xmin;
        result = 31 * result + xmax;
        result = 31 * result + ymin;
        result = 31 * result + ymax;
        result = 31 * result + zmin;
        result = 31 * result + zmax;
        result = 31 * result + xwidth;
        result = 31 * result + ywidth;
        result = 31 * result + zwidth;
        result = 31 * result + dimension;
        return result;
    }
}
