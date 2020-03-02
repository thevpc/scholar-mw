package net.vpc.scholar.hadrumaths.symbolic.double2double;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.BooleanMarker;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.random.IgnoreRandomGeneration;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;

import java.util.ArrayList;

/**
 * User: taha
 * Date: 3 juil. 2003
 * Time: 17:17:27
 */
@IgnoreRandomGeneration
public abstract class PieceXFunction extends RefDoubleToDouble {

    private static final long serialVersionUID = 1L;
    protected double periodWidthX;
    protected double periodWidthY;
    protected int nbrPeriodsX;
    protected int nbrPeriodsY;
    protected boolean vPatternX;
    protected boolean vPatternY;
    protected Domain domain;
    protected Plus exprVal;
    private final Axis invariance;

    public PieceXFunction(Domain domain, Axis invariance, boolean vPatternX, boolean vPatternY, int nbrPeriodsX, int nbrPeriodsY, SegmentFactory segmentFactory) {
        this.domain = domain;
        this.invariance = invariance;
        this.periodWidthX = (domain.xwidth()) / nbrPeriodsX;
        this.periodWidthY = (domain.ywidth()) / nbrPeriodsY;
        this.nbrPeriodsX = nbrPeriodsX;
        this.nbrPeriodsY = nbrPeriodsY;
        this.vPatternX = vPatternX;
        this.vPatternY = vPatternY;
        init(segmentFactory);
    }

    protected void init(SegmentFactory segmentFactory) {
        ArrayList<DoubleToDouble> segs = new ArrayList<DoubleToDouble>();
//        if(true){
//            segs.add(
//                    segmentFactory.getFunction(this,
//                    domain.xmin,
//                    domain.xmax,
//                    domain.ymin,
//                    domain.ymax,
//                    vPatternX,
//                    vPatternY));
//            segments = segs.toArray(new DFunctionXY[0]);
//            return;
//        }
        double stepWidthX = periodWidthX / 2;
        double stepWidthY = periodWidthY / 2;

        if (nbrPeriodsX > 0 && nbrPeriodsY <= 0) {
            for (int i = 0; i < nbrPeriodsX; i++) {
                double minx = domain.xmin() + (2 * i) * stepWidthX;
                double maxx = domain.xmin() + (2 * i + 1) * stepWidthX;
                segs.add(
                        segmentFactory.getFunction(this,
                                minx, maxx,
                                domain.ymin(),
                                domain.ymax(),
                                !vPatternX,
                                true));
                minx = domain.xmin() + (2 * i + 1) * stepWidthX;
                maxx = domain.xmin() + (2 * i + 2) * stepWidthX;
                segs.add(
                        segmentFactory.getFunction(this,
                                minx, maxx,
                                domain.ymin(),
                                domain.ymax(),
                                vPatternX,
                                true));
            }
        } else if (nbrPeriodsY > 0 && nbrPeriodsX <= 0) {
            for (int i = 0; i < nbrPeriodsY; i++) {
                double miny = domain.ymin() + (2 * i) * stepWidthY;
                double maxy = domain.ymin() + (2 * i + 1) * stepWidthY;
                segs.add(
                        segmentFactory.getFunction(this,
                                domain.xmin(),
                                domain.xmax(),
                                miny, maxy,
                                true,
                                !vPatternY));
                miny = domain.ymin() + (2 * i + 1) * stepWidthY;
                maxy = domain.ymin() + (2 * i + 2) * stepWidthY;
                segs.add(
                        segmentFactory.getFunction(this,
                                domain.xmin(),
                                domain.xmax(),
                                miny, maxy,
                                true,
                                vPatternY));
            }
        } else if (Double.isInfinite(periodWidthX) && Double.isInfinite(periodWidthY)) {
            segs.add(
                    segmentFactory.getFunction(this,
                            domain.xmin(),
                            domain.xmax(),
                            domain.ymin(),
                            domain.ymax(),
                            vPatternX,
                            vPatternY));
        } else {
            if (invariance != null) {
                switch (invariance) {
                    case X: {
                        for (int j = 0; j < nbrPeriodsY * 2; j++) {
                            double minx = domain.xmin();
                            double maxx = domain.xmax();
                            double miny = domain.ymin() + j * stepWidthY;
                            double maxy = domain.ymin() + (j + 1) * stepWidthY;
                            segs.add(
                                    segmentFactory.getFunction(this,
                                            minx, maxx,
                                            miny,
                                            maxy,
                                            false,
                                            ((j % 2 == 0) == vPatternY)));
                        }
                        break;
                    }
                    case Y: {
                        for (int i = 0; i < nbrPeriodsX * 2; i++) {
                            double minx = domain.xmin() + i * stepWidthX;
                            double maxx = domain.xmin() + (i + 1) * stepWidthX;
                            double miny = domain.ymin();
                            double maxy = domain.ymax();
                            segs.add(
                                    segmentFactory.getFunction(this,
                                            minx, maxx,
                                            miny,
                                            maxy,
                                            ((i % 2 == 0) == vPatternX),
                                            false));
                        }
                    }
                }
            } else {
                for (int i = 0; i < nbrPeriodsX * 2; i++) {
                    for (int j = 0; j < nbrPeriodsY * 2; j++) {
                        double minx = domain.xmin() + i * stepWidthX;
                        double maxx = domain.xmin() + (i + 1) * stepWidthX;
                        double miny = domain.ymin() + j * stepWidthY;
                        double maxy = domain.ymin() + (j + 1) * stepWidthY;
                        segs.add(
                                segmentFactory.getFunction(this,
                                        minx, maxx,
                                        miny,
                                        maxy,
                                        ((i % 2 == 0) == vPatternX),
                                        ((j % 2 == 0) == vPatternY)));
                    }
                }
            }
        }
        if (segs.size() == 0) {
            System.err.println("DPieceXFunctionXY : 0 segments");
        }
        exprVal = Plus.of(segs.toArray(new DoubleToDouble[0]));
    }

    public Axis getInvariance() {
        return invariance;
    }

    public int getPeriodCount() {
        return getSegmentsSize() / 2;
    }

    private int getSegmentsSize() {
        return exprVal.getChildren().size();
    }

    @Override
    public DoubleToDouble getReference() {
        return exprVal.toDD();
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        DoubleToDouble f = getSegment(x);
        return f != null ? f.evalDouble(x, y, defined) : 0;
    }

    public DoubleToDouble getSegment(double x) {
        int h = (int) (2 * (x - domain.xmin()) / periodWidthX);
        if (h < 0 || h >= getSegmentsSize()) {
            return null;
        } else {
            return exprVal.getChild(h).toDD();
        }
    }

    public DoubleToDouble getSegmentAt(int index) {
        return exprVal.getChild(index).toDD();
    }

    //    @Override
    public DoubleToDouble[] getSegments(Domain inter) {
        int i1 = getSegmentIndex(inter.xmin());
        int i2 = getSegmentIndex(inter.xmax());
        if (i2 < 0) {
            i2 = getSegmentsSize() - 1;
        }
        if (i1 >= 0 && i2 >= 0) {
            DoubleToDouble[] f = new DoubleToDouble[i2 - i1 + 1];
            System.arraycopy(exprVal.getChildren().toArray(new DoubleToDouble[0]), i1, f, 0, f.length);
            return f;
        }
        return new DoubleToDouble[0];
    }

    public int getSegmentIndex(double x) {
        int h = (int) (2 * (x - domain.xmin()) / periodWidthX);
        if (h < 0 || h >= getSegmentsSize()) {
            return -1;
        } else {
            return h;
        }
    }

//    public double leftScalarProduct(DomainXY someDomain, DFunctionXY other) {
//        DomainXY inter = this.intersect(other, someDomain);
//        if (inter.isEmpty()) {
//            return 0;
//        }
//        int i1 = getSegmentIndex(inter.xmin);
//        int i2 = getSegmentIndex(inter.xmax);
//        if (i2 < 0) {
//            i2 = segments.length - 1;
//        }
//        double ps = 0;
//        if (i1 >= 0 && i2 >= 0) {
//            for (int i = i1; i <= i2; i++) {
//                ps += segments[i].leftScalarProduct(inter, other);
//            }
//        }
//        return ps;
//    }
//
//    public double rightScalarProduct(DomainXY someDomain, DFunctionXY other) {
//        DomainXY inter = this.intersect(other, someDomain);
//        if (inter.isEmpty()) {
//            return 0;
//        }
//        int i1 = getSegmentIndex(inter.xmin);
//        int i2 = getSegmentIndex(inter.xmax);
//        if (i2 < 0) {
//            i2 = segments.length - 1;
//        }
//        double ps = 0;
//        if (i1 >= 0 && i2 >= 0) {
//            for (int i = i1; i <= i2; i++) {
//                ps += other.leftScalarProduct(inter, segments[i]);
//            }
//        }
//        return ps;

    //    }

//    @Override
//    public DDxyPieceXFunction getSymmetricX() {
//        DDxyPieceXFunction p = this.clone();
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[fs.length - i - 1] = segments[i].getSymmetricX(this.domain);
//        }
//        p.segments = fs;
//        return p;
//    }
//
//    @Override
//    public DDxyPieceXFunction getSymmetricY() {
//        DDxyPieceXFunction p = this.clone();
//        DDxy[] fs = new DDxy[segments.length];
//        for (int i = 0; i < fs.length; i++) {
//            fs[fs.length - i - 1] = segments[i].getSymmetricY(this.domain);
//        }
//        p.segments = fs;
//        return p;
//    }

    @Override
    public DoubleToDouble getSymmetricX(double x0) {
        return ((AbstractDoubleToDouble) getSymmetricX()).translate(2 * (x0 - ((domain.xmin() + domain.xmax()) / 2)), 0);
    }

    @Override
    public DoubleToDouble getSymmetricY(double y0) {
        return ((AbstractDoubleToDouble) getSymmetricY()).translate(0, 2 * (y0 - ((domain.ymin() + domain.ymax()) / 2)));
    }

//    @Override
//    public PieceXFunction clone() {
//        return (PieceXFunction) .clone();
//    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        long temp;
        result = 31 * result + (invariance != null ? invariance.hashCode() : 0);
        result = 31 * result + Double.hashCode(periodWidthX);
        result = 31 * result + Double.hashCode(periodWidthY);
        result = 31 * result + nbrPeriodsX;
        result = 31 * result + nbrPeriodsY;
        result = 31 * result + (vPatternX ? 1 : 0);
        result = 31 * result + (vPatternY ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PieceXFunction)) return false;

        PieceXFunction that = (PieceXFunction) o;

        if (nbrPeriodsX != that.nbrPeriodsX) return false;
        if (nbrPeriodsY != that.nbrPeriodsY) return false;
        if (Double.compare(that.periodWidthX, periodWidthX) != 0) return false;
        if (Double.compare(that.periodWidthY, periodWidthY) != 0) return false;
        if (vPatternX != that.vPatternX) return false;
        if (vPatternY != that.vPatternY) return false;
        return invariance == that.invariance;
    }

    protected interface SegmentFactory {

        DoubleToDouble getFunction(PieceXFunction pieceXFunction,
                                   double minx,
                                   double maxx,
                                   double miny,
                                   double maxy,
                                   boolean oddX,
                                   boolean oddY);
    }
}
