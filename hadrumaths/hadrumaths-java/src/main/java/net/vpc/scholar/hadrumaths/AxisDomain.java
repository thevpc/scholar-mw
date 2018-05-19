package net.vpc.scholar.hadrumaths;

/**
 * Created by vpc on 5/14/16.
 */
public class AxisDomain {
    //    private long R=10000000000000000000000000000000000L;
//    private long R=1000000000000000000L;
    private Axis axis;
    private Domain domainX;

    public AxisDomain(Axis axis, Domain domainX) {
        this.axis = axis;
        this.domainX = Domain.forBounds(domainX.getXMin(), domainX.getXMax());
        switch (axis) {
            case X: {
                this.domainX = Domain.forBounds(domainX.getXMin(), domainX.getXMax());
            }
            case Y: {
                this.domainX = Domain.forBounds(domainX.getYMin(), domainX.getYMax()
                );
            }
            case Z: {
                this.domainX = Domain.forBounds(domainX.getZMin(), domainX.getZMax());
            }
            default: {
                throw new IllegalArgumentException("Unsupported");
            }
        }

    }

    public Axis getAxis() {
        return axis;
    }

    public double getMin() {
        return domainX.getXMin();
    }

    public double getMax() {
        return domainX.getXMax();
    }

    public Domain getDomain() {
        return domainX;
    }

    public Domain toDomain() {
        switch (axis) {
            case X: {
                return domainX;
            }
            case Y: {
                return Domain.forBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY
                        , domainX.getXMin(), domainX.getXMax()
                );
            }
            case Z: {
                return Domain.forBounds(
                        Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY
                        , Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY
                        , domainX.getXMin(), domainX.getXMax()
                );
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }


    public double[] steps(double xstep) {
        return domainX.xsteps(xstep);
    }


    public double[] times(int xtimes) {
        return domainX.xtimes(xtimes);
    }

}
