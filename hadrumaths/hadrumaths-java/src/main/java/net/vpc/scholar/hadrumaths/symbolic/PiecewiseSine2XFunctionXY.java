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
public class PiecewiseSine2XFunctionXY extends PieceXFunction implements Cloneable {
    private static final long serialVersionUID = 1L;
    private double crestValue;

    public PiecewiseSine2XFunctionXY(final Domain domain, final Axis axis, final double crestValue, int nbrPeriods, final boolean odd) {
        super(domain, axis, odd, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new SegmentFactory() {

            public DoubleToDouble getFunction(PieceXFunction pieceXFunction, double minx, double maxx, double miny, double maxy, boolean oddX, boolean oddY) {
                switch (axis) {
                    case X: {
                        if (oddX) {
                            double newWidth = (maxx - minx) * 2;
                            return new CosXCosY(crestValue, Maths.PI / newWidth, -minx * Maths.PI / newWidth - Maths.HALF_PI, 0, 0, Domain.forBounds(minx, maxx, miny, maxy));
                        } else {
                            double newWidth = (maxx - minx) * 2;
                            double decalage = (maxx - minx);
                            return new CosXCosY(crestValue, Maths.PI / newWidth, -minx * Maths.PI / newWidth + decalage * Maths.PI / newWidth - Maths.HALF_PI, 0, 0, Domain.forBounds(minx, maxx, miny, maxy));
                        }
                    }
                    case Y: {
                        if (oddY) {
                            double newWidth = (maxy - miny) * 2;
                            return new CosXCosY(crestValue, 0, 0, Maths.PI / newWidth, -minx * Maths.PI / newWidth - Maths.HALF_PI, Domain.forBounds(minx, maxx, miny, maxy));
                        } else {
                            double newWidth = (maxx - minx) * 2;
                            double decalage = (maxx - minx);
                            return new CosXCosY(crestValue, 0, 0, Maths.PI / newWidth, -minx * Maths.PI / newWidth + decalage * Maths.PI / newWidth - Maths.HALF_PI, Domain.forBounds(minx, maxx, miny, maxy));
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
        return new CosXCosY(this.crestValue, Maths.PI / newWidth, -minx * Maths.PI / newWidth - Maths.HALF_PI, 0, 0, Domain.forBounds(minx, maxx, domain.ymin(), domain.ymax()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PiecewiseSine2XFunctionXY)) return false;
        if (!super.equals(o)) return false;

        PiecewiseSine2XFunctionXY that = (PiecewiseSine2XFunctionXY) o;

        if (Double.compare(that.crestValue, crestValue) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(crestValue);
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
