package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Out;


/**
 * User: taha
 * Date: 3 juil. 2003
 * Time: 17:17:27
 */
final public class RooftopXFunctionXY extends PieceXFunction implements Cloneable {
    private static final long serialVersionUID = -1010101010101001054L;
    public double crestValue;
    private Axis axis;

    public RooftopXFunctionXY(Domain domain, Axis axis, double crestValue, int nbrPeriods, final boolean startWithCrest) {
        super(domain, axis, startWithCrest, false, axis == Axis.X ? nbrPeriods : 0, axis == Axis.Y ? nbrPeriods : 0, new MySegmentFactory(crestValue, axis));
        if (axis != Axis.X && axis != Axis.Y) {
            throw new IllegalArgumentException("Unsupported Axis " + axis);
        }
        this.crestValue = crestValue;
        this.axis = axis;
//        name=("RoofTop" + nbrPeriods);
    }

    public RooftopXFunctionXY getSymmetricX() {
        switch (axis) {
            case X:
                return this;
        }
        return (RooftopXFunctionXY) super.getSymmetricX();
    }

//    public RooftopXFunctionXY getSymmetricY() {
//        switch(axis){
//            case Y: return this;
//        }
//        return (RooftopXFunctionXY) super.getSymmetricY();
//    }
//
//    public RooftopXFunction toTranslation(double xDelta){
//        RooftopXFunction c=(RooftopXFunction) this.clone();
//        c.domain=c.domain.translate(xDelta,0);
//        DFunction[] fx1=c.segments;
//        DFunction[] fx2=new DFunction[fx1.length];
//        for (int i = 0; i < fx2.length; i++) {
//            fx2[i]=((DLinearFunction)fx1[i]).toTranslation(xDelta);
//        }
//        c.segments=fx2;
//        return c;
//    }

    private final static class MySegmentFactory implements SegmentFactory {
        private final double crestValue;
        private final Axis axis;

        public MySegmentFactory(double crestValue, Axis axis) {
            this.crestValue = crestValue;
            this.axis = axis;
        }

        public AbstractDoubleToDouble getFunction(PieceXFunction pieceXFunction, double minx, double maxx, double miny, double maxy, boolean oddX, boolean oddY) {
            switch (axis) {
                case X: {
                    if (oddX) {
                        double a2 = crestValue / (maxx - minx);
                        double b2 = -a2 * minx;
                        return new Linear(
                                a2,
                                0,
                                b2, Domain.forBounds(minx, maxx, miny, maxy));
                    } else {
                        double a1 = crestValue / (minx - maxx);
                        double b1 = -a1 * maxx;
                        return new Linear(
                                a1,
                                0,
                                b1,
                                Domain.forBounds(minx, maxx, miny, maxy));
                    }
                }
                case Y: {
                    if (oddY) {
                        double a2 = crestValue / (maxy - miny);
                        double b2 = -a2 * miny;
                        return new Linear(
                                0,
                                a2,
                                b2, Domain.forBounds(minx, maxx, miny, maxy));
                    } else {
                        double a1 = crestValue / (miny - maxy);
                        double b1 = -a1 * maxy;
                        return new Linear(
                                0,
                                a1,
                                b1,
                                Domain.forBounds(minx, maxx, miny, maxy));
                    }
                }
            }
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RooftopXFunctionXY)) return false;
        if (!super.equals(o)) return false;

        RooftopXFunctionXY that = (RooftopXFunctionXY) o;

        if (Double.compare(that.crestValue, crestValue) != 0) return false;
        if (axis != that.axis) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(crestValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (axis != null ? axis.hashCode() : 0);
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
