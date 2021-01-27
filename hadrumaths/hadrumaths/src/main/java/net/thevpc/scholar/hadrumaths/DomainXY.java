package net.thevpc.scholar.hadrumaths;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:31:19
 */
final class DomainXY extends Domain implements Cloneable {
    private static final long serialVersionUID = 1L;


    public final double xmin;
    public final double xmax;
    public final double ymin;
    public final double ymax;

    DomainXY(double xmin, double xmax, double ymin, double ymax) {
        super(hashCode(xmin, xmax, ymin, ymax));
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

    private static int hashCode(double xmin, double xmax, double ymin, double ymax) {
        int hash = 653712824;//DomainXY.class.getName().hashCode();
        hash = 43 * hash + Double.hashCode(xmin);
        hash = 43 * hash + Double.hashCode(xmax);
        hash = 43 * hash + Double.hashCode(ymin);
        hash = 43 * hash + Double.hashCode(ymax);
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
                    case 1:
                        return this;
                    case 2:
                        return Domain.ofBounds(minMax[0], minMax[1], ymin, ymax);
                }
                return Domain.ofBounds(minMax[0], minMax[1], ymin, ymax);
            }
            case 2: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                double xmin = minMax[0];
                double xmax = minMax[1];
                int xu = (int) minMax[2];
                Expressions.domainIntersectHelper(ymin, ymax, other.ymin(), other.ymax(), minMax);
                if (xu == minMax[2]) {
                    switch (xu) {
                        case 1:
                            return this;
                        case 2:
                            return other;
                    }
                }
                return Domain.ofBounds(xmin, xmax, minMax[0], minMax[1]);
            }
            case 3: {
                Expressions.domainIntersectHelper(xmin, xmax, other.xmin(), other.xmax(), minMax);
                double xmin = minMax[0];
                double xmax = minMax[1];
                Expressions.domainIntersectHelper(ymin, ymax, other.ymin(), other.ymax(), minMax);
                return Domain.ofBounds(xmin, xmax, minMax[0], minMax[1], other.zmin(), other.zmax());
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
        return 2;
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

    public boolean contains(double x) {
        switch (dimension()) {
            case 1:
                return x >= xmin() && x < xmax();
        }
        throw new MissingAxisException(Axis.Y);
    }

    public boolean contains(double x, double y) {
        return x >= xmin
                && x < xmax
                && y >= ymin
                && y < ymax;
    }

    public boolean contains(double x, double y, double z) {
        return x >= xmin
                && x < xmax
                && y >= ymin
                && y < ymax;
    }

    public Domain toDomainXY() {
        return this;
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

    public int hashCode() {
        return eagerHashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DomainXY other = (DomainXY) obj;
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
        return this.ymax == other.ymax;
    }
}
