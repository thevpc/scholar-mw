package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public abstract class DDxyAbstractSum extends AbstractDoubleToDouble {

    private static final long serialVersionUID = 1L;
    protected DoubleToDouble[] segments;

    protected DDxyAbstractSum(Domain domain) {
        super(domain);
    }

    public int getSegmentCount() {
        return segments.length;
    }

    public DoubleToDouble getSegmentAt(int i) {
        return segments[i];
    }

    public boolean isZeroImpl() {
        for (DoubleToDouble segment : segments) {
            if (!segment.isZero()) {
                return false;
            }
        }
        return true;
    }

    public boolean isNaNImpl() {
        for (DoubleToDouble segment : segments) {
            if (segment.isNaN()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInfiniteImpl() {
        for (DoubleToDouble segment : segments) {
            if (segment.isInfinite()) {
                return true;
            }
        }
        return false;
    }

    //    public DDxy translate(double deltaX, double deltaY) {
//        DDxyAbstractSum fct = this.clone();
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[i] = segments[i].translate(deltaX, deltaY);
//        }
//        fct.domain = fct.domain.translate(deltaX, deltaY);
//        fct.setSegments(fs);
//        return fct;
//    }
//    public DDxyAbstractSum getSymmetricX() {
//        DDxyAbstractSum fct = this.clone();
//        if(fct.segments.length==0){
//            return fct;
//        }
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[i] = segments[i].getSymmetricX();
//        }
//        fct.setSegments(fs);
//        return fct;
//    }
//
//    public DDxyAbstractSum getSymmetricY() {
//        DDxyAbstractSum fct = this.clone();
//        if(fct.segments.length==0){
//            return fct;
//        }
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[i] = segments[i].getSymmetricY();
//        }
//        fct.setSegments(fs);
//        return fct;
//    }
//
//    public DDxyAbstractSum toXOpposite() {
//        DDxyAbstractSum fct = this.clone();
//        if(fct.segments.length==0){
//            return fct;
//        }
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[i] = segments[i].toXOpposite();
//        }
//        fct.setSegments(fs);
//        return fct;
//    }
//
//    public DDxyAbstractSum toYOpposite() {
//        DDxyAbstractSum fct = this.clone();
//        if(fct.segments.length==0){
//            return fct;
//        }
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[i] = segments[i].toYOpposite();
//        }
//        fct.setSegments(fs);
//        return fct;
//    }
//    public double leftScalarProduct(DomainXY domain, DFunctionXY other) {
//        DomainXY inter = this.intersect(other);
//        if (inter.isEmpty()) {
//            return 0;
//        }
//        double r = 0;
//        for (int i = 0; i < segments.length; i++) {
//            r += segments[i].leftScalarProduct(inter, other);
//        }
//        return r;
//    }
//
//    public double rightScalarProduct(DomainXY someDomain, DFunctionXY other) {
//        DomainXY inter = this.intersect(other, someDomain);
//        if (inter.isEmpty()) {
//            return 0;
//        }
//        double r = 0;
//
//        for (int i = 0; i < segments.length; i++) {
//            r += segments[i].rightScalarProduct(inter, other);
//        }
//        return r;
//    }
    public double computeDouble(double x, double y, BooleanMarker defined) {
        double r = 0;
        if (contains(x, y)) {
            for (DoubleToDouble f : segments) {
                //            if (f.contains(x, y)) {
                r += f.computeDouble(x, y, defined);
//            }
            }
        }
        return r;
    }

    @Override
    protected double computeDouble0(double x, BooleanMarker defined) {
        double r = 0;
        for (DoubleToDouble f : segments) {
            r += f.computeDouble(x, defined);
        }
        return r;
    }

    @Override
    protected double computeDouble0(double x, double y, BooleanMarker defined) {
        double r = 0;
        for (DoubleToDouble f : segments) {
            r += f.computeDouble(x, y, defined);
        }
        return r;
    }

    @Override
    protected double computeDouble0(double x, double y, double z, BooleanMarker defined) {
        double r = 0;
        for (DoubleToDouble f : segments) {
            r += f.computeDouble(x, y, z, defined);
        }
        return r;
    }

    @Override
    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        double[][] r = new double[y.length][x.length];
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
        if (currRange != null) {
            BooleanArray2 b = BooleanArrays.newArray(y.length, x.length);
            currRange.setDefined(b);
            for (DoubleToDouble f : segments) {
                Out<Range> r2 = new Out<Range>();
                double[][] r0 = f.computeDouble(x, y, d0, r2);
                Range abcd = r2.get();
                if (abcd != null) {
                    BooleanArray2 b2 = abcd.getDefined2();
                    int ax = abcd.xmin;
                    int bx = abcd.xmax;
                    int cy = abcd.ymin;
                    int dy = abcd.ymax;
                    for (int xIndex = ax; xIndex <= bx; xIndex++) {
                        for (int yIndex = cy; yIndex <= dy; yIndex++) {
                            if (b2.get(yIndex, xIndex)) {
                                r[yIndex][xIndex] += r0[yIndex][xIndex];
                                b.set(yIndex, xIndex);
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    @Override
    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        double[][][] r = new double[z.length][y.length][x.length];
        Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y, z);
        if (currRange != null) {
            BooleanArray3 b = BooleanArrays.newArray(z.length, y.length, x.length);
            currRange.setDefined(b);
            for (DoubleToDouble f : segments) {
                Out<Range> r2 = new Out<Range>();
                double[][][] r0 = f.computeDouble(x, y, z, d0, r2);
                Range abcd = r2.get();
                if (abcd != null) {
                    BooleanArray3 b2 = abcd.getDefined3();
                    int ax = abcd.xmin;
                    int bx = abcd.xmax;
                    int cy = abcd.ymin;
                    int dy = abcd.ymax;
                    for (int i = abcd.zmin; i <= abcd.zmax; i++) {
                        for (int xIndex = ax; xIndex <= bx; xIndex++) {
                            for (int yIndex = cy; yIndex <= dy; yIndex++) {
                                if (b2 != null && b2.get(i, yIndex, xIndex)) {
                                    r[i][yIndex][xIndex] += r0[i][yIndex][xIndex];
                                    b.set(i, yIndex, xIndex);
                                }
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(currRange);
            }
        }
        return r;
    }

    //    public double[] compute(double[] x, double y, DomainXY d0) {
//        double[] r = new double[x.length];
//        for (DFunctionXY f : segments) {
//            double[] r0 = f.compute(x, y, d0);
//            int[] abcd = (d0 == null ? f.domain : f.domain.intersect(d0)).rangesArray(x, new double[]{y});
//            if (abcd != null) {
//                int ax = abcd[0];
//                int bx = abcd[1];
//                for (int xIndex = ax; xIndex <= bx; xIndex++) {
//                    r[xIndex] += r0[xIndex];
//                }
//            }
//        }
//        return r;
//    }
    public double[] computeDouble(double x, double[] y, Domain d0) {
        double[] r = new double[y.length];
        for (DoubleToDouble f : segments) {
            double[] r0 = f.computeDouble(x, y, d0, null);

            Range abcd = (d0 == null ? f.getDomain() : f.getDomain().intersect(d0)).range(new double[]{x}, y);
            if (abcd != null) {
                int cy = abcd.ymin;
                int dy = abcd.ymax;
                for (int yIndex = cy; yIndex <= dy; yIndex++) {
                    r[yIndex] += r0[yIndex];
                }
            }
        }
        return r;
    }

    public double[] computeDouble(double[] x, double y, Domain d0) {
        double[] r = new double[x.length];
        for (DoubleToDouble f : segments) {
            double[] r0 = f.computeDouble(x, y, d0, null);

            Range abcd = (d0 == null ? f.getDomain() : f.getDomain().intersect(d0)).range(x, new double[]{y});
            if (abcd != null) {
                int cy = abcd.ymin;
                int dy = abcd.ymax;
                for (int xIndex = cy; xIndex <= dy; xIndex++) {
                    r[xIndex] += r0[xIndex];
                }
            }
        }
        return r;
    }

    public DDxyAbstractSum clone() {
        return (DDxyAbstractSum) super.clone();
    }

    public DoubleToDouble[] getSegments() {
        return segments;
    }

    //    @Override
//    public boolean isInvariant(Axis axis) {
//        for (IDDxy segment : segments) {
//            if (!segment.isInvariant(axis)) {
//                return false;
//            }
//        }
//        return true;
//    }
//    public DDxy multiply(double factor, DomainXY newDomain) {
//        DDxyAbstractSum fct = this.clone();
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[i] = segments[i].multiply(factor, newDomain);
//        }
//        if (newDomain != null) {
//            fct.domain = domain.intersect(newDomain);
//        }
//        fct.setSegments(fs);
//        return fct;
//    }
    protected void setSegments(DoubleToDouble[] segments) {

        this.segments = segments;
    }

    public DoubleToDouble[] getSegments(Domain domain) {
        ArrayList<DoubleToDouble> list = new ArrayList<DoubleToDouble>(segments.length);
        for (DoubleToDouble segment : segments) {
            if (!segment.getDomain().intersect(domain).isEmpty()) {
                list.add(segment);
            }
        }
        return list.toArray(new DoubleToDouble[list.size()]);
    }

    public List<Expr> getSubExpressions() {
        return (List) Arrays.asList(segments);
    }


    @Override
    public Expr setParam(String name, double value) {
        return setParam(name, DoubleValue.valueOf(value, Domain.FULLX));
    }

    @Override
    public Expr setParam(String name, Expr value) {
        DoubleToDouble[] segments2 = new DoubleToDouble[segments.length];
        boolean changed = false;
        for (int i = 0; i < segments2.length; i++) {
            DoubleToDouble s1 = segments[i];
            DoubleToDouble s2 = (DoubleToDouble) s1.setParam(name, value);
            if (s1 != s2) {
                changed = true;
            }
            segments2[i] = s2;
        }
        if (changed) {
            DDxyAbstractSum cloned = clone();
            cloned.setSegments(segments2);
            return cloned;
        }
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DDxyAbstractSum)) return false;
        if (!super.equals(o)) return false;

        DDxyAbstractSum that = (DDxyAbstractSum) o;

        if (!Arrays.equals(segments, that.segments)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (segments != null ? Arrays.hashCode(segments) : 0);
        return result;
    }

}
