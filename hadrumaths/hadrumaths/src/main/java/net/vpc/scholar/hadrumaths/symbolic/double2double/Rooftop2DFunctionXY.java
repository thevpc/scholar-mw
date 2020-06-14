package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.Range;

/**
 * User: taha
 * Date: 3 juil. 2003
 * Time: 17:17:27
 */
final public class Rooftop2DFunctionXY extends PieceXFunction {

    private static final long serialVersionUID = 1L;
    public double crestValue;
    private final RooftopType type;

    public Rooftop2DFunctionXY(Domain domain, Axis invariance, RooftopType type) {
        this(domain, invariance, 1, type);
    }

    public Rooftop2DFunctionXY(Domain domain, Axis invariance, double crestValue, RooftopType type) {
        super(domain, invariance, false, false,
                (!RooftopType.FULL.equals(type) && Axis.X.equals(invariance)) ? 0 : RooftopType.FULL.equals(type) || RooftopType.NORTH.equals(type) || RooftopType.SOUTH.equals(type) ? 1 : 0,
                (!RooftopType.FULL.equals(type) && Axis.Y.equals(invariance)) ? 0 : RooftopType.FULL.equals(type) || RooftopType.EAST.equals(type) || RooftopType.WEST.equals(type) ? 1 : 0,
                new MySegmentFactory(crestValue, invariance, type));
        if (invariance != null && invariance != Axis.X && invariance != Axis.Y) {
            throw new IllegalArgumentException("Unsupported Axis " + invariance);
        }
        this.crestValue = crestValue;
        this.type = type;
//        if(Double.isNaN(crestValue) || crestValue==0){
//            crestValue=Math2.scalarProduct(this,this);
//        }
//        name=("RoofTop2D(" + type + ")");
    }

    @Override
    public int hashCode() {
        //Ok to call super
        int result = super.hashCode();
        result = 31 * result +Double.hashCode(crestValue);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rooftop2DFunctionXY)) return false;
        if (!super.equals(o)) return false;

        Rooftop2DFunctionXY that = (Rooftop2DFunctionXY) o;

        if (Double.compare(that.crestValue, crestValue) != 0) return false;
        return type == that.type;
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

        private final double crestValue;
        private final Axis invariance;
        private final RooftopType type;

        public MySegmentFactory(double crestValue, Axis axis, RooftopType type) {
            this.crestValue = crestValue;
            this.invariance = axis;
            this.type = type;
        }

        public AbstractDoubleToDouble getFunction(PieceXFunction pieceXFunction, double minx, double maxx, double miny, double maxy, boolean oddX, boolean oddY) {
            if (invariance != null) {
                switch (invariance) {
                    case Y: {
                        boolean one = true;
                        switch (type) {
                            case EAST: {
                                one = false;
                                break;
                            }
                            case WEST: {
                                one = true;
                                break;
                            }
                            case NORTH:
                            case SOUTH: {
                                one = !oddX;
                                break;
                            }
                            case FULL: {
                                one = oddX;
                                break;
                            }
                        }
                        if (one) {
                            double a1 = crestValue / (minx - maxx);
                            double b1 = -a1 * maxx;
                            return new Linear(
                                    a1,
                                    0,
                                    b1,
                                    Domain.ofBounds(minx, maxx, miny, maxy));
                        } else {
                            double a2 = crestValue / (maxx - minx);
                            double b2 = -a2 * minx;
                            return new Linear(
                                    a2,
                                    0,
                                    b2, Domain.ofBounds(minx, maxx, miny, maxy));
                        }
                    }
                    case X: {
                        if (oddY) {
                            double a2 = crestValue / (maxy - miny);
                            double b2 = -a2 * miny;
                            return new Linear(
                                    0,
                                    a2,
                                    b2, Domain.ofBounds(minx, maxx, miny, maxy));
                        } else {
                            double a1 = crestValue / (miny - maxy);
                            double b1 = -a1 * maxy;
                            return new Linear(
                                    0,
                                    a1,
                                    b1,
                                    Domain.ofBounds(minx, maxx, miny, maxy));
                        }
                    }
                }
            }
            int number = -1;
            switch (type) {
                case FULL: {
                    if (oddX && oddY) {
                        number = 0;
                    } else if (!oddX && oddY) {
                        number = 1;
                    } else if (oddX && !oddY) {
                        number = 2;
                    } else if (!oddX && !oddY) {
                        number = 3;
                    }
                    break;
                }
                case WEST: {
                    if (oddY) {
                        number = 2;
                    } else {
                        number = 0;
                    }
                    break;
                }
                case EAST: {
                    if (oddY) {
                        number = 3;
                    } else {
                        number = 1;
                    }
                    break;
                }
                case NORTH: {
                    if (oddX) {
                        number = 1;
                    } else {
                        number = 0;
                    }
                    break;
                }
                case SOUTH: {
                    if (oddX) {
                        number = 3;
                    } else {
                        number = 2;
                    }
                    break;
                }
            }

            switch (number) {
                case 0: {
                    double ax1 = crestValue / (minx - maxx);
                    double ay1 = crestValue / (miny - maxy);
                    double bx1 = -ax1 * maxx;
                    double by1 = -ay1 * maxy;
                    return new Linear(
                            ax1,
                            ay1,
                            bx1 + by1,
                            Domain.ofBounds(minx, maxx, miny, maxy));
                }
                case 1: {
                    double ax1 = crestValue / (-minx + maxx);
                    double ay1 = crestValue / (miny - maxy);
                    double bx1 = -ax1 * minx;
                    double by1 = -ay1 * maxy;
                    return new Linear(
                            ax1,
                            ay1,
                            bx1 + by1,
                            Domain.ofBounds(minx, maxx, miny, maxy));
                }
                case 2: {
                    double ax1 = crestValue / (minx - maxx);
                    double ay1 = crestValue / (-miny + maxy);
                    double bx1 = -ax1 * maxx;
                    double by1 = -ay1 * miny;
                    return new Linear(
                            ax1,
                            ay1,
                            bx1 + by1,
                            Domain.ofBounds(minx, maxx, miny, maxy));
                }
                case 3: {
                    double ax1 = crestValue / (-minx + maxx);
                    double ay1 = crestValue / (-miny + maxy);
                    double bx1 = -ax1 * minx;
                    double by1 = -ay1 * miny;
                    return new Linear(
                            ax1,
                            ay1,
                            bx1 + by1,
                            Domain.ofBounds(minx, maxx, miny, maxy));

                }
            }
            throw new UnsupportedOperationException();

        }
    }
    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
