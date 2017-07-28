package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
public final class DomainXY extends Domain implements Cloneable {


    public final double xmin;
    public final double xmax;
    public final double ymin;
    public final double ymax;

    DomainXY(double xmin, double xmax,double ymin,double ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        if (xwidth() < 0) {
            throw new IllegalArgumentException("xwidth<0");
        }
        this.ymin = ymin;
        this.ymax = ymax;
        if (ywidth() < 0) {
            throw new IllegalArgumentException("ywidth<0");
        }
    }


    public double xmin() {
        return xmin;
    }

    public double xmax() {
        return xmax;
    }

    public double ymin() {
        return ymin;
    }

    public double ymax() {
        return ymax;
    }

    public double zmin() {
        return Double.NEGATIVE_INFINITY;
    }

    public double zmax() {
        return Double.POSITIVE_INFINITY;
    }

    public double xwidth() {
        return xmax - xmin;
    }

    public double ywidth() {
        return ymax - ymin;
    }

    public double zwidth() {
        return Double.POSITIVE_INFINITY;
    }

    public int dimension() {
        return 2;
    }

    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmin()) ^ (Double.doubleToLongBits(this.xmin()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.xmax()) ^ (Double.doubleToLongBits(this.xmax()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymin()) ^ (Double.doubleToLongBits(this.ymin()) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.ymax()) ^ (Double.doubleToLongBits(this.ymax()) >>> 32));
        return hash;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Domain other = (Domain) obj;
        if (this.xmin() != other.xmin()) {
            return false;
        }
        if (this.xmax() != other.xmax()) {
            return false;
        }
        if (this.ymin() != other.ymin()) {
            return false;
        }
        if (this.ymax() != other.ymax()) {
            return false;
        }
        return true;
    }
}
