package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
public final class DomainX extends Domain implements Cloneable {


    public final double xmin;
    public final double xmax;

    DomainX(double xmin, double xmax) {
        this.xmin = xmin;
        this.xmax = xmax;
        if (xwidth() < 0) {
            throw new IllegalArgumentException("xwidth<0");
        }
    }


    public double xmin() {
        return xmin;
    }

    public double xmax() {
        return xmax;
    }

    public double ymin() {
        return Double.NEGATIVE_INFINITY;
    }

    public double ymax() {
        return Double.POSITIVE_INFINITY;
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
        return Double.POSITIVE_INFINITY;
    }

    public double zwidth() {
        return Double.POSITIVE_INFINITY;
    }

    public int dimension() {
        return 1;
    }


    public int hashCode() {
        int hash = 7;
        long xminl = Double.doubleToLongBits(this.xmin);
        long xmaxl = Double.doubleToLongBits(this.xmax);
        hash = 43 * hash + (int) (xminl ^ (xminl >>> 32));
        hash = 43 * hash + (int) (xmaxl ^ (xmaxl >>> 32));
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
        return true;
    }
}
