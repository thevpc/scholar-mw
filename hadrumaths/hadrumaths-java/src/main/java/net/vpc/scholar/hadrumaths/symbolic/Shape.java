package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.Polygon;
import net.vpc.scholar.hadrumaths.Expr;

/**
 * User: taha Date: 2 juil. 2003 Time: 14:29:58
 */
public final class Shape extends AbstractDoubleToDouble implements Cloneable{

    private static final long serialVersionUID = -1010101010101001006L;
    public static final Shape ZERO = new Shape(0, Domain.EMPTYXY);

//    public static final int CODE = 1;
    public double value;
    private Geometry geometry;

    public Shape(double cst, Domain domain) {
        this(cst, new Polygon(domain));
    }

    public Shape(Domain domain) {
        this(Math.sqrt(domain.xwidth() * domain.ywidth()), new Polygon(domain));
    }

    public Shape(double cst, Geometry geometry) {
        super(cst == 0 ? Domain.EMPTYXY : geometry.getDomain());
        this.value = cst;
        this.geometry = geometry;
    }


    @Override
    public boolean isInvariantImpl(Axis axis) {
        return true;
    }

    @Override
    protected boolean contains(double x) {
        throw new IllegalArgumentException("Missing Y");
    }

    @Override
    protected boolean contains(double x, double y) {
        return geometry.contains(x, y);
    }

    @Override
    protected boolean contains(double x, double y, double z) {
        return geometry.contains(x, y);
    }

    @Override
    protected double computeDouble0(double x) {
        throw new IllegalArgumentException("Missing y");
    }

    @Override
    protected double computeDouble0(double x, double y) {
        return value;
    }

    @Override
    protected double computeDouble0(double x, double y, double z) {
        return value;
    }

    @Override
    public AbstractDoubleToDouble mul(double factor, Domain newDomain) {
        return new Shape(factor * value,
                newDomain == null ? domain : domain.intersect(newDomain)
        );
    }

    @Override
    public AbstractDoubleToDouble getSymmetricX() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble getSymmetricY() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble translate(double deltaX, double deltaY) {
        return new Shape(value, domain.translate(deltaX, deltaY));
    }

    @Override
    public AbstractDoubleToDouble toXOpposite() {
        return this;
    }

    @Override
    public AbstractDoubleToDouble toYOpposite() {
        return this;
    }

    @Override
    public boolean isZeroImpl() {
        return value == 0;
    }

    @Override
    public boolean isNaNImpl() {
        return Double.isNaN(value);
    }

    @Override
    public boolean isInfiniteImpl() {
        return Double.isInfinite(value);
    }

    /**
     * redefined for performance issues
     *
     * @param x all x values
     * @param y all y values
     * @param d0 sub-domain
     * @return values for x,y
     */
    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0,Out<Range> ranges) {
        double[][] r = new double[y.length][x.length];
        if (value != 0) {
            Range abcd = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
            if (abcd != null) {
                BooleanArray2 def0 = BooleanArrays.newArray(y.length,x.length);
                abcd.setDefined(def0);
                int ax = abcd.xmin;
                int bx = abcd.xmax;
                int cy = abcd.ymin;
                int dy = abcd.ymax;
                if (geometry.isRectangular()) {
                    for (int xIndex = ax; xIndex <= bx; xIndex++) {
                        for (int yIndex = cy; yIndex <= dy; yIndex++) {
                            def0.set(yIndex,xIndex);//
                            r[yIndex][xIndex] = value;//
                        }
                    }
                } else {
                    for (int xIndex = ax; xIndex <= bx; xIndex++) {
                        for (int yIndex = cy; yIndex <= dy; yIndex++) {
                            if (geometry.contains(x[xIndex], y[yIndex])) {
                                def0.set(yIndex,xIndex);//
                                r[yIndex][xIndex] = value;//
                            }
                        }
                    }
                }
                if(ranges!=null){
                    ranges.set(abcd);
                }
            }
        }
        return r;
    }

    public double getValue() {
        return value;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public boolean isSymmetric(AxisXY axis) {
        return true;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        if (!super.equals(o)) return false;

        Shape that = (Shape) o;

        if (Double.compare(that.value, value) != 0) return false;
        if (geometry != null ? !geometry.equals(that.geometry) : that.geometry != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(value);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (geometry != null ? geometry.hashCode() : 0);
        return result;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return true;
    }

    @Override
    public boolean isDoubleImpl() {
        return false;
    }

    @Override
    public int getDomainDimension() {
        return 2;
    }

    @Override
    public Expr mul(Geometry domain) {
        return new Shape(value,getGeometry().intersectGeometry(domain));
    }

    @Override
    public Expr mul(Domain domain) {
        return new Shape(value,getGeometry().intersectGeometry(domain.toGeometry()));
    }

    @Override
    public Expr mul(double other) {
        return new Shape(value*other,getGeometry());
    }

    @Override
    public Expr mul(Complex other) {
        if(other.isReal()){
            return mul(other.getReal());
        }
        return super.mul(other);
    }
}
