package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
public final class DomainXYZ extends Domain implements Cloneable {


    public final double xmin;
    public final double xmax;
    public final double ymin;
    public final double ymax;
    public final double zmin;
    public final double zmax;

    DomainXYZ(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
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
        this.zmin = zmin;
        this.zmax = zmax;
        if (zwidth() < 0) {
            throw new IllegalArgumentException("zwidth<0");
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
        return zmin;
    }

    public double zmax() {
        return zmax;
    }

    public double xwidth() {
        return xmax - xmin;
    }

    public double ywidth() {
        return ymax - ymin;
    }

    public double zwidth() {
        return zmax - zmin;
    }

    public int dimension() {
        return 3;
    }

    public int hashCode() {
        int hash = 7;
        long xminl = Double.doubleToLongBits(this.xmin);
        long xmaxl = Double.doubleToLongBits(this.xmax);
        long yminl = Double.doubleToLongBits(this.ymin);
        long ymaxl = Double.doubleToLongBits(this.ymax);
        long zminl = Double.doubleToLongBits(this.zmin);
        long zmaxl = Double.doubleToLongBits(this.zmax);

        hash = 43 * hash + (int) (xminl ^ (xminl >>> 32));
        hash = 43 * hash + (int) (xmaxl ^ (xmaxl >>> 32));
        hash = 43 * hash + (int) (yminl ^ (yminl >>> 32));
        hash = 43 * hash + (int) (ymaxl ^ (ymaxl >>> 32));
        hash = 43 * hash + (int) (zminl ^ (zminl >>> 32));
        hash = 43 * hash + (int) (zmaxl ^ (zmaxl >>> 32));
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
        if (this.zmin() != other.zmin()) {
            return false;
        }
        if (this.zmax() != other.zmax()) {
            return false;
        }
        return true;
    }

    public boolean contains(double x, double y, double z) {
        return x >= xmin()
                && x < xmax()
                && y >= ymin()
                && y < ymax()
                && z >= zmin()
                && z < zmax();
    }


}
