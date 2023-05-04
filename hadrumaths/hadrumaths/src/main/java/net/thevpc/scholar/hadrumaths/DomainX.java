package net.thevpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
public final class DomainX extends Domain implements Cloneable {
    private static final long serialVersionUID = 1L;


    public final double xmin;
    public final double xmax;

    DomainX(double xmin, double xmax) {
        super(hashCode0(xmin, xmax));
        this.xmin = xmin;
        this.xmax = xmax;
        if (xwidth() < 0) {
            throw new IllegalArgumentException("xwidth<0");
        }
    }

    private static int hashCode0(double xmin, double xmax) {
        int hash = 1545108161;//DomainX.class.getName().hashCode();
        hash = 43 * hash + Double.hashCode(xmin);
        hash = 43 * hash + Double.hashCode(xmax);
        return hash;
    }

    public Expr simplify(SimplifyOptions options) {
        return ExpressionRewriterFactory.getComputationSimplifier().rewriteOrSame(this, options == null ? null : options.getTargetExprType());
    }


    public Domain intersect(Domain other) {
        if (other == null || other == this) {
            return this;
        }
        int d_t = this.dimension();
        double[] minMax = new double[3];

        switch (other.dimension()) {
            case 1: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                switch ((int) minMax[2]) {
                    case 0:
                        return this;
                    case 1:
                        return other;
                }
                return Domain.ofBounds(minMax[0], minMax[1]);
            }
            case 2: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                return Domain.ofBounds(minMax[0], minMax[1], other.ymin(), other.ymax());
            }
            case 3: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                return Domain.ofBounds(minMax[0], minMax[1], other.ymin(), other.ymax(), other.zmin(), other.zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported domain " + other.dimension());
    }

    public double xmin() {
        return xmin;
    }

    public double xmax() {
        return xmax;
    }

    public int dimension() {
        return 1;
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

    public boolean contains(double x) {
        return x >= xmin && x < xmax;
    }

    public boolean contains(double x, double y) {
        return x >= xmin && x < xmax;
    }

    public boolean contains(double x, double y, double z) {
        return x >= xmin
                && x < xmax
                ;
    }

//    public int hashCode() {
//        int hash = 7;
//        long xminl = Double.doubleToLongBits(this.xmin);
//        long xmaxl = Double.doubleToLongBits(this.xmax);
//        hash = 43 * hash + (int) (xminl ^ (xminl >>> 32));
//        hash = 43 * hash + (int) (xmaxl ^ (xmaxl >>> 32));
//        return hash;
//    }

    public Domain toDomainX() {
        return this;
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

    public int hashCode() {
        return eagerHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomainX other = (DomainX) obj;
        if (this.eagerHashCode != other.eagerHashCode) {
            return false;
        }
        if (this.xmin != other.xmin) {
            return false;
        }
        return this.xmax == other.xmax;
    }

}
