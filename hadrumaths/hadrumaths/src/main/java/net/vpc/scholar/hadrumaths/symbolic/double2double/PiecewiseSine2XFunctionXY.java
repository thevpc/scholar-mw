package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.Range;


/**
 * User: taha
 * Date: 3 juil. 2003
 * Time: 17:17:27
 */
public class PiecewiseSine2XFunctionXY extends PieceXFunction {
    private static final long serialVersionUID = 1L;
    private final double crestValue;

    public PiecewiseSine2XFunctionXY(final Domain domain, final Axis axis, final double crestValue, int nbrPeriods, final boolean odd) {
        super(domain, axis, odd, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new SegmentFactory() {

            public DoubleToDouble getFunction(PieceXFunction pieceXFunction, double minx, double maxx, double miny, double maxy, boolean oddX, boolean oddY) {
                switch (axis) {
                    case X: {
                        if (oddX) {
                            double newWidth = (maxx - minx) * 2;
                            return new CosXCosY(crestValue, Maths.PI / newWidth, -minx * Maths.PI / newWidth - Maths.HALF_PI, 0, 0, Domain.ofBounds(minx, maxx, miny, maxy));
                        } else {
                            double newWidth = (maxx - minx) * 2;
                            double decalage = (maxx - minx);
                            return new CosXCosY(crestValue, Maths.PI / newWidth, -minx * Maths.PI / newWidth + decalage * Maths.PI / newWidth - Maths.HALF_PI, 0, 0, Domain.ofBounds(minx, maxx, miny, maxy));
                        }
                    }
                    case Y: {
                        if (oddY) {
                            double newWidth = (maxy - miny) * 2;
                            return new CosXCosY(crestValue, 0, 0, Maths.PI / newWidth, -minx * Maths.PI / newWidth - Maths.HALF_PI, Domain.ofBounds(minx, maxx, miny, maxy));
                        } else {
                            double newWidth = (maxx - minx) * 2;
                            double decalage = (maxx - minx);
                            return new CosXCosY(crestValue, 0, 0, Maths.PI / newWidth, -minx * Maths.PI / newWidth + decalage * Maths.PI / newWidth - Maths.HALF_PI, Domain.ofBounds(minx, maxx, miny, maxy));
                        }
                    }
                }
                return null;
            }
        });
        this.crestValue = crestValue;
    }


    public DoubleToDouble getOddFunction(double minx, double maxx) {
        double newWidth = (maxx - minx) * 2;
        return new CosXCosY(this.crestValue, Maths.PI / newWidth, -minx * Maths.PI / newWidth - Maths.HALF_PI, 0, 0, Domain.ofBounds(minx, maxx, domain.ymin(), domain.ymax()));
    }

    @Override
    public int hashCode() {
        //OK to call hashCode
        int result = super.hashCode();
        result = 31 * result + Double.hashCode(crestValue);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PiecewiseSine2XFunctionXY)) return false;
        if (!super.equals(o)) return false;

        PiecewiseSine2XFunctionXY that = (PiecewiseSine2XFunctionXY) o;

        return Double.compare(that.crestValue, crestValue) == 0;
    }

    @Override
    public double[] evalDouble(double[] x, Domain d0, Out<Range> range) {
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }
}
