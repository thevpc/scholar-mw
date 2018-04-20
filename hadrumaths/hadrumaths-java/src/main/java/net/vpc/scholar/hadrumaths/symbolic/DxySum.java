package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Out;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class Description
 *
 * @author Taha BEN SALAH (taha@ant-inter.com)
 *         User: taha
 *         Date: 14-aout-2004
 *         Time: 22:01:08
 */
@Deprecated
public class DxySum extends DDxyAbstractSum implements Cloneable {

    private static final long serialVersionUID = -1010101010101001043L;

    public DxySum(DoubleToDouble... sum) {
        super(null);
        setFunctions(sum);
    }

    public void setFunctions(DoubleToDouble... sum) {
        if (sum.length == 0) {
            System.err.println("DxySum : 0 segments");
        }
        ArrayList<DoubleToDouble> a = new ArrayList<DoubleToDouble>(sum.length);
        linearize(sum, a);
        if (a.size() == 0) {
            System.err.println("DxySum : 0 segments");
        }
        this.segments = a.toArray(new DoubleToDouble[a.size()]);
        double xmin = Double.NaN;
        double xmax = Double.NaN;
        double ymin = Double.NaN;
        double ymax = Double.NaN;
        for (DoubleToDouble dFunction : sum) {
            Domain d = dFunction.getDomain();
            if (Double.isNaN(xmin) || d.xmin() < xmin) {
                xmin = d.xmin();
            }
            if (Double.isNaN(ymin) || d.ymin() < ymin) {
                ymin = d.ymin();
            }
            if (Double.isNaN(xmax) || d.xmax() > xmax) {
                xmax = d.xmax();
            }
            if (Double.isNaN(ymax) || d.ymax() > ymax) {
                ymax = d.ymax();
            }
        }
        this.domain = Domain.forBounds(xmin, xmax, ymin, ymax);
    }

    private void linearize(DoubleToDouble[] sum, ArrayList<DoubleToDouble> putInto) {
        for (DoubleToDouble dFunction : sum) {
            if (dFunction instanceof DxySum) {
                linearize(((DxySum) dFunction).segments, putInto);
            } else {
                if (!dFunction.isZero()) {
                    putInto.add(dFunction);
                }
            }
        }
    }

    public DoubleToDouble add(DoubleToDouble... sum) {
        ArrayList<DoubleToDouble> a = new ArrayList<DoubleToDouble>(sum.length);
        a.addAll(Arrays.asList(this.segments));
        linearize(sum, a);
        if (a.size() == 0) {
            System.err.println("DSumFunctionXY : 0 segments");
        }
        this.segments = a.toArray(new DoubleToDouble[a.size()]);
        double xmin = Double.NaN;
        double xmax = Double.NaN;
        double ymin = Double.NaN;
        double ymax = Double.NaN;
        for (DoubleToDouble dFunction : sum) {
            Domain d = dFunction.getDomain();
            if (Double.isNaN(xmin) || d.xmin() < xmin) {
                xmin = d.xmin();
            }
            if (Double.isNaN(ymin) || d.ymin() < ymin) {
                ymin = d.ymin();
            }
            if (Double.isNaN(xmax) || d.xmax() > xmax) {
                xmax = d.xmax();
            }
            if (Double.isNaN(ymax) || d.ymax() > ymax) {
                ymax = d.ymax();
            }
        }
        this.domain = this.domain.expand(Domain.forBounds(xmin, xmax, ymin, ymax));
        return this;
    }

    @Override
    public DxySum clone() {
        return (DxySum) super.clone();
    }

    @Override
    protected void setSegments(DoubleToDouble[] segments) {
        setFunctions(segments);
    }

//    @Override
//    public IDDxy simplify() {
//        DDxyAbstractSum fct = this.clone();
//        ArrayList<DDxy> a = new ArrayList<DDxy>(segments.length);
//        for (IDDxy segment1 : segments) {
//            DDxy segment = segment1.simplify();
//            if (!segment.isZero() && !segment.getDomain().isEmpty()) {
//                a.add(segment);
//            }
//        }
//        if (a.size() == 0) {
//            return FunctionFactory.DZEROXY;
//        }
//        fct.setSegments(a.toArray(new DDxy[a.size()]));
//        return fct;
//    }

//    @Override
//    public String toString() {
//        StringBuilder sb=new StringBuilder();
//        for (DFunctionXY segment : segments) {
//            if(sb.length()>0){
//                sb.append(" + ");
//            }
//            sb.append(segment);
//        }
//        return sb.toString();
//    }

//    @Override
//    public boolean isInvariant(Axis axis) {
//        for (IDDxy dFunctionXY : segments) {
//            if (!dFunctionXY.isInvariant(axis)) {
//                return false;
//            }
//        }
//        return true;
//    }




    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return true;
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
