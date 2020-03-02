package net.vpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
final class DomainXYZ extends Domain {
    private static final long serialVersionUID = 1L;

    public final double xmin;
    public final double xmax;
    public final double ymin;
    public final double ymax;
    public final double zmin;
    public final double zmax;

    DomainXYZ(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        super(hashCode(xmin, xmax, ymin, ymax, zmin, zmax));
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

    private static int hashCode(double xmin, double xmax, double ymin, double ymax, double zmin, double zmax) {
        int hash = -1209738846;//DomainXYZ.class.getName().hashCode();
        hash = 43 * hash + Double.hashCode(xmin);
        hash = 43 * hash + Double.hashCode(xmax);
        hash = 43 * hash + Double.hashCode(ymin);
        hash = 43 * hash + Double.hashCode(ymax);
        hash = 43 * hash + Double.hashCode(zmin);
        hash = 43 * hash + Double.hashCode(zmax);
        return hash;
    }

    public Expr simplify(SimplifyOptions options) {
        return ExpressionRewriterFactory.getComputationSimplifier().rewriteOrSame(this, options == null ? null : options.getTargetExprType());
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


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomainXYZ other = (DomainXYZ) obj;
        if (this.eagerHashCode != other.eagerHashCode) {
            return false;
        }
        if (this.xmin != other.xmin) {
            return false;
        }
        if (this.xmax != other.xmax) {
            return false;
        }
        if (this.ymin != other.ymin) {
            return false;
        }
        if (this.ymax != other.ymax) {
            return false;
        }
        if (this.zmin != other.zmin) {
            return false;
        }
        return this.zmax == other.zmax;
    }

    public int hashCode() {
        return eagerHashCode;
    }

    public boolean contains(double x, double y, double z) {
        return x >= xmin
                && x < xmax
                && y >= ymin
                && y < ymax
                && z >= zmin
                && z < zmax;
    }


    public Domain toDomainXYZ() {
        return this;
    }

//    public Domain intersect2(Domain other) {
//        if (other == null || other == this) {
//            return this;
//        }
//        int d_t = this.dimension();
//        int d_o = other.dimension();
//        int dim = Math.max(d_t, d_o);
//        double x1_t = xmin();
//        double x2_t = xmax();
//        double x1_o = other.xmin();
//        double x2_o = other.xmax();
//
//        double x1 = max(x1_t, x1_o);
//        double x2 = min(x2_t, x2_o);
//        // some workaround
//        double delta = x1 - x2;
//        if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//            x1 = x2 = 0.0D;
//        }
//
//        switch (dim) {
//            case 1: {
//                if (d_t == 1 && x1 == x1_t && x2 == x2_t) {
//                    return this;
//                }
//                if (d_o == 1 && x1 == x1_o && x2 == x2_o) {
//                    return other;
//                }
//                return ofBounds(x1, x2);
//            }
//            case 2: {
//                double y1_t = ymin();
//                double y1_o = other.ymin();
//                double y2_t = ymax();
//                double y2_o = other.ymax();
//                double y1 = max(y1_t, y1_o);
//                double y2 = min(y2_t, y2_o);
//                x2 = max(x2, x1);
//                y2 = max(y2, y1);
//                delta = y1 - y2;
//                if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//                    y1 = y2 = 0.0D;
//                }
//
//                if (d_t == 2 && x1 == x1_t && x2 == x2_t && y1 == y1_t && y2 == y2_t) {
//                    return this;
//                }
//                if (d_o == 2 && x1 == x1_o && x2 == x2_o && y1 == y1_o && y2 == y2_o) {
//                    return other;
//                }
//
//                return ofBounds(x1, x2, y1, y2);
//            }
//            case 3: {
//                double y1_t = ymin();
//                double y1_o = other.ymin();
//                double y2_t = ymax();
//                double y2_o = other.ymax();
//                double y1 = max(y1_t, y1_o);
//                double y2 = min(y2_t, y2_o);
//
//
//                x2 = max(x2, x1);
//                y2 = max(y2, y1);
//                delta = y1 - y2;
//                if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//                    y1 = y2 = 0.0D;
//                }
//
//                double z1_t = zmin();
//                double z1_o = other.zmin();
//                double z2_t = zmax();
//                double z2_o = other.zmax();
//                double z1 = max(z1_t, z1_o);
//                double z2 = min(z2_t, z2_o);
//
//                z2 = max(z2, z1);
//                delta = z1 - z2;
//                if ((delta < 0.0D && -delta < epsilon) || (delta > 0.0D && delta < epsilon)) {
//                    z1 = z2 = 0.0D;
//                }
//
//                if (d_t == 3 && x1 == x1_t && x2 == x2_t && y1 == y1_t && y2 == y2_t && z1 == z1_t && z2 == z2_t) {
//                    return this;
//                }
//                if (d_o == 3 && x1 == x1_o && x2 == x2_o && y1 == y1_o && y2 == y2_o && z1 == z1_o && z2 == z2_o) {
//                    return other;
//                }
//
//                return ofBounds(x1, x2, y1, y2, z1, z2);
//            }
//        }
//        throw new IllegalArgumentException("Unsupported domain " + dim);
//    }

    public Domain intersect(Domain other) {
        if (other == null || other == this) {
            return this;
        }
        int d_t = this.dimension();
        double[] minMax = new double[3];

        switch (other.dimension()) {
            case 1: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                if ((int) minMax[2] == 1) {
                    return this;
                }
                return Domain.ofBounds(minMax[0], minMax[1], ymin, ymax, zmin, zmax);
            }
            case 2: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                double xmin = minMax[0];
                double xmax = minMax[1];
                int xu = (int) minMax[2];
                Expressions.domainIntersectHelper(ymin, ymax, other.ymin(), other.ymax(), minMax);
                if (xu == minMax[2] && xu == 1) {
                    return this;
                }
                return Domain.ofBounds(xmin, xmax, minMax[0], minMax[1], zmin, zmax);
            }
            case 3: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                double xmin = minMax[0];
                double xmax = minMax[1];
                int xu = (int) minMax[2];
                Expressions.domainIntersectHelper(ymin, ymax, other.ymin(), other.ymax(), minMax);
                double ymin = minMax[0];
                double ymax = minMax[1];
                int yu = (int) minMax[2];
                Expressions.domainIntersectHelper(ymin, ymax, other.ymin(), other.ymax(), minMax);
                if (xu == yu && xu==minMax[2] && xu == 1) {
                    return this;
                }
                return Domain.ofBounds(xmin,xmax,ymin, ymax, minMax[0], minMax[1]);
            }
        }
        throw new IllegalArgumentException("Unsupported domain " + other.dimension());
    }
    public boolean contains(double x) {
        throw new MissingAxisException(Axis.Y);
    }

    //    @Override
    public boolean contains(double x, double y) {
        throw new MissingAxisException(Axis.Z);
    }

}
