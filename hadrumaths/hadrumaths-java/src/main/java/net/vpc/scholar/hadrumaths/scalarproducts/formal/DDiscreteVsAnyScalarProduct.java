package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.symbolic.DDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;


/**
 * User: taha
 * Date: 2 juil. 2003
 * Time: 15:15:16
 */
final class DDiscreteVsAnyScalarProduct implements FormalScalarProductHelper {
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null || !obj.getClass().equals(getClass())){
            return false;
        }
        return true;
    }
//    public double compute0(DomainXY domain, IDDxy f1, IDDxy f2, FormalScalarProductOperator sp) {
//        DDiscrete n = (DDiscrete) f1;
//        return sp.evalDD(domain, n.discretize(), f2);
//    }

    public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
        DDiscrete n = (DDiscrete) f1;
        switch (domain.getDimension()){
            case 1:{
                double d = 0;

//        ArrayList<IDDxy> sum = new ArrayList<IDDxy>();
                Domain dom;
                double[] x = n.getX();
                double dx = n.getDx();
                double[] values = n.getValues()[0][0];
                for (int xi = 0; xi < x.length; xi++) {
                    if (values[xi] != 0) {
                        dom = Domain.forBounds(x[xi], x[xi] + dx);
                        DoubleValue seg = DoubleValue.valueOf(values[xi], dom);
                        d += sp.eval(domain, seg, f2,false).toDouble();
                    }
                }
                return d;
            }
            case 2:{
                double d = 0;

//        ArrayList<IDDxy> sum = new ArrayList<IDDxy>();
                Domain dom;
                double[] x = n.getX();
                double[] y = n.getY();
                double dx = n.getDx();
                double dy = n.getDy();
                double[][] values = n.getValues()[0];
                for (int yi = 0; yi < y.length; yi++) {
                    for (int xi = 0; xi < x.length; xi++) {
                        if (values[yi][xi] != 0) {
                            dom = Domain.forBounds(x[xi], x[xi] + dx, y[yi], y[yi] + dy);
                            DoubleValue seg = DoubleValue.valueOf(values[yi][xi], dom);
                            d += sp.eval(domain, seg, f2,false).toDouble();
                        }
                    }
                }
                return d;
            }
            case 3:{
                double d = 0;
                Domain dom;
                double[] x = n.getX();
                double[] y = n.getY();
                double[] z = n.getZ();
                double dx = n.getDx();
                double dy = n.getDy();
                double dz = n.getDz();
                double[][][] values = n.getValues();
                for (int zi = 0; zi < z.length; zi++) {
                    for (int yi = 0; yi < y.length; yi++) {
                        for (int xi = 0; xi < x.length; xi++) {
                            if (values[zi][yi][xi] != 0) {
                                dom = Domain.forBounds(x[xi], x[xi] + dx, y[yi], y[yi] + dy, z[yi], z[yi] + dz);
                                DoubleValue seg = DoubleValue.valueOf(values[zi][yi][xi], dom);
                                d += sp.eval(domain, seg, f2,false).toDouble();
                            }
                        }
                    }
                }
                return d;
            }
        }
        throw new IllegalArgumentException("Unsupported Dimension "+domain.getDimension());
    }
}