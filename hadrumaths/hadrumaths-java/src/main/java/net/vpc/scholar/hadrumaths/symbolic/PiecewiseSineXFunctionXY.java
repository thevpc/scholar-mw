package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Out;


/**
 * User: taha
 * Date: 3 juil. 2003
 * Time: 17:17:27
 */
public class PiecewiseSineXFunctionXY extends PieceXFunction implements Cloneable {
    private static final long serialVersionUID = 1L;
    //    public double crestValue;
    private double factor;


    public PiecewiseSineXFunctionXY(final Domain domain, final Axis axis, final double amp, final double factor, int nbrPeriods, final boolean startWithCrest) {
        super(domain, axis, startWithCrest, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new MySegmentFactory(amp, factor, axis, false));
        if (axis != Axis.X && axis != Axis.Y) {
            throw new IllegalArgumentException("Unsupported Axis " + axis);
        }
        this.factor = factor;
//        name=("PiecewiseSine" + nbrPeriods);
    }

    public PiecewiseSineXFunctionXY(final Domain domain, final Axis axis, final double factor, int nbrPeriods, final boolean startWithCrest) {
        super(domain, axis, startWithCrest, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new MySegmentFactory(0, factor, axis, true));
        if (axis != Axis.X && axis != Axis.Y) {
            throw new IllegalArgumentException("Unsupported Axis " + axis);
        }
        this.factor = factor;
//        name=("PiecewiseSine" + nbrPeriods);
    }

    public PiecewiseSineXFunctionXY(final Domain domain, final Axis axis, final double factor) {
        this(domain, axis, factor, 1, false);
    }

    public double getFactor() {
        return factor;
    }

    @Override
    public PieceXFunction getSymmetricX() {
        return this;
    }

    @Override
    public PieceXFunction getSymmetricY() {
        return this;
    }

    private final static class MySegmentFactory implements SegmentFactory {
        private final double factor;
        private final double amp;
        private final Axis axis;
        private final boolean autoAmp;

        public MySegmentFactory(double amp, double factor, Axis axis, boolean autoAmp) {
            this.amp = amp;
            this.autoAmp = autoAmp;
            this.factor = factor;
            this.axis = axis;
        }

        public DoubleToDouble getFunction(PieceXFunction pieceXFunction, double minx, double maxx, double miny, double maxy, boolean oddX, boolean oddY) {
            switch (axis) {
                case X: {
                    double w = maxx - minx;
                    double fac2 = factor * Maths.HALF_PI / w;
                    double amp2 = autoAmp ? Maths.sin2(fac2 * w / 2) : amp;
                    if (oddX) {
                        return new CosXCosY(amp2, fac2, -fac2 * (minx) - (Maths.HALF_PI), 0, 0, Domain.forBounds(minx, maxx, miny, maxy));
                    } else {
                        return new CosXCosY(amp2, -fac2, fac2 * (minx + w) - (Maths.HALF_PI), 0, 0, Domain.forBounds(minx, maxx, miny, maxy));
                    }
                }
                case Y: {
                    double w = maxy - miny;
                    double fac2 = factor * Maths.HALF_PI / w;
                    double amp2 = autoAmp ? Maths.sin2(fac2 * w / 2) : amp;
                    if (oddY) {
                        return new CosXCosY(amp2, 0, 0, fac2, -fac2 * (miny) - (Maths.HALF_PI), Domain.forBounds(minx, maxx, miny, maxy));
                    } else {
                        return new CosXCosY(amp2, 0, 0, -fac2, fac2 * (miny + w) - (Maths.HALF_PI), Domain.forBounds(minx, maxx, miny, maxy));
                    }
                }
            }
            return null;

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PiecewiseSineXFunctionXY)) return false;
        if (!super.equals(o)) return false;

        PiecewiseSineXFunctionXY that = (PiecewiseSineXFunctionXY) o;

        if (Double.compare(that.factor, factor) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(factor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public int getDomainDimension() {
        return 2;
    }

    @Override
    public double[] computeDouble(double[] x, Domain d0, Out<Range> range) {
        throw new IllegalArgumentException("Missing y");
    }
}
