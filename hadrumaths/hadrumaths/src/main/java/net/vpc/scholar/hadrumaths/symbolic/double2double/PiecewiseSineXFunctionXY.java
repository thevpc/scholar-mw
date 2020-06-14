package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.Range;


/**
 * User: taha
 * Date: 3 juil. 2003
 * Time: 17:17:27
 */
public class PiecewiseSineXFunctionXY extends PieceXFunction {
    private static final long serialVersionUID = 1L;
    //    public double crestValue;
    private final double factor;


    public PiecewiseSineXFunctionXY(final Domain domain, final Axis axis, final double amp, final double factor, int nbrPeriods, final boolean startWithCrest) {
        super(domain, axis, startWithCrest, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new MySegmentFactory(amp, factor, axis, false));
        if (axis != Axis.X && axis != Axis.Y) {
            throw new IllegalArgumentException("Unsupported Axis " + axis);
        }
        this.factor = factor;
//        name=("PiecewiseSine" + nbrPeriods);
    }

    public PiecewiseSineXFunctionXY(final Domain domain, final Axis axis, final double factor) {
        this(domain, axis, factor, 1, false);
    }

    public PiecewiseSineXFunctionXY(final Domain domain, final Axis axis, final double factor, int nbrPeriods, final boolean startWithCrest) {
        super(domain, axis, startWithCrest, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new MySegmentFactory(0, factor, axis, true));
        if (axis != Axis.X && axis != Axis.Y) {
            throw new IllegalArgumentException("Unsupported Axis " + axis);
        }
        this.factor = factor;
//        name=("PiecewiseSine" + nbrPeriods);
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

    @Override
    public int hashCode() {
        //TOK to call super
        int result = super.hashCode();
        result = 31 * result + Double.hashCode(factor);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PiecewiseSineXFunctionXY)) return false;
        //OK super
        if (!super.equals(o)) return false;

        PiecewiseSineXFunctionXY that = (PiecewiseSineXFunctionXY) o;

        return Double.compare(that.factor, factor) == 0;
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
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
                        return new CosXCosY(amp2, fac2, -fac2 * (minx) - (Maths.HALF_PI), 0, 0, Domain.ofBounds(minx, maxx, miny, maxy));
                    } else {
                        return new CosXCosY(amp2, -fac2, fac2 * (minx + w) - (Maths.HALF_PI), 0, 0, Domain.ofBounds(minx, maxx, miny, maxy));
                    }
                }
                case Y: {
                    double w = maxy - miny;
                    double fac2 = factor * Maths.HALF_PI / w;
                    double amp2 = autoAmp ? Maths.sin2(fac2 * w / 2) : amp;
                    if (oddY) {
                        return new CosXCosY(amp2, 0, 0, fac2, -fac2 * (miny) - (Maths.HALF_PI), Domain.ofBounds(minx, maxx, miny, maxy));
                    } else {
                        return new CosXCosY(amp2, 0, 0, -fac2, fac2 * (miny + w) - (Maths.HALF_PI), Domain.ofBounds(minx, maxx, miny, maxy));
                    }
                }
            }
            return null;

        }
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
