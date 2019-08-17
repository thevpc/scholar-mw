package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DM;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DMHelper;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DV;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Collections;
import java.util.List;

public class CartesianFieldBuilderExprD2C extends AbstractExprPropertyAware implements DoubleToComplex {
    private CartesianFieldBuilder builder;
    private Domain domain;
    private Axis axis;
    private DC2DMHelper dc2dm;
    public CartesianFieldBuilderExprD2C(CartesianFieldBuilder builder, Axis axis, Domain domain) {
        this.builder = builder;
        this.domain = domain;
        this.axis = axis;
        dc2dm=new DC2DMHelper(this);
    }

    @Override
    protected Domain getDomainImpl() {
        return domain;
    }

    @Override
    protected boolean isInvariantImpl(Axis axis) {
        return false;
    }

    @Override
    protected boolean isZeroImpl() {
        return false;
    }

    @Override
    protected boolean isInfiniteImpl() {
        return false;
    }

    @Override
    protected boolean isNaNImpl() {
        return false;
    }

    @Override
    public List<Expr> getSubExpressions() {
        return Collections.emptyList();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
//        switch (domain.dimension()){
//            case 1:{
//                return ComponentDimension.SCALAR;
//            }
//            case 2:{
//                return ComponentDimension.VECTOR2;
//            }
//            case 3:{
//                return ComponentDimension.VECTOR3;
//            }
//        }
//        return ComponentDimension.VECTOR3;
    }

    @Override
    protected boolean isDDImpl() {
        return false;
    }

    @Override
    protected boolean isDCImpl() {
        return true;
    }

    @Override
    protected boolean isDVImpl() {
        return true;
    }

    @Override
    protected boolean isDMImpl() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return new DC2DV(this);
    }

    @Override
    public DoubleToMatrix toDM() {
        return new DC2DM(this);
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if(defined!=null && contains(x,y)){
            defined.set();
        }
        return builder.computeVector(axis,new double[]{x},y,domain.zmin()).get(0);
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if(defined!=null && contains(x,y,z)){
            defined.set();
        }
        return builder.computeVector(axis,new double[]{x},y,z).get(0);
    }

    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if(defined!=null && contains(x)){
            defined.set();
        }
        return builder.computeVector(axis,new double[]{x},domain.ymin(),domain.zmin()).get(0);
    }

    @Override
    public DoubleToDouble getRealDD() {
        return Maths.real(this).toDD();
    }

    @Override
    public DoubleToDouble getImagDD() {
        return Maths.imag(this).toDD();
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        Out<Range> r=new Out<>();
        Complex[][][] p = computeComplex(x, new double[]{domain.ymin()}, new double[]{domain.zmin()}, d0==null?null:d0.toDomain(2), r);
        if(ranges!=null){
            ranges.set(r.get().toDimension(1));
        }
        return p[0][0];
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        Out<Range> r=new Out<>();
        Complex[][] p = computeComplex(x, new double[]{y}, d0==null?null:d0.toDomain(2), r);
        if(ranges!=null){
            ranges.set(r.get());
        }
        return p[0];
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        Out<Range> r=new Out<>();
        Complex[][] p = computeComplex(new double[]{x}, y, d0==null?null:d0.toDomain(2), r);
        if(ranges!=null){
            ranges.set(r.get());
        }
        Complex[] c=new Complex[y.length];
        for (int i = 0; i < c.length; i++) {
            c[i]=p[i][0];
        }
        return c;
    }

    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Complex[][] values = builder.computeVDiscrete(x, y).getComponent(axis).getValues()[0];
        Domain domainXY = domain.intersect(d0);
        Range r = Domain.range(domainXY, null, x, y);
        if (r != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r.setDefined(def0);
            if(d0!=null || ranges!=null){
                for (int _y = 0; _y < y.length; _y++) {
                    for (int _x = 0; _x < x.length; _x++) {
                        if(!domainXY.contains(x[_x],y[_y])){
                            values[_y][_x]=Complex.ZERO;
                        }else{
                            def0.set(_y, _x);
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r);
            }
        }else{
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
        return values;
    }


//    @Override
//    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        Out<Range> r=new Out<>();
//        Complex[][][] p = computeComplex(x, y, new double[]{domain.zmin()}, d0==null?null:d0.toDomain(2), r);
//        if(ranges!=null){
//            ranges.set(r.get().toDimension(2));
//        }
//        return p[0];
//    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Complex[][][] values = builder.computeVDiscrete(x, y, z).getComponent(axis).getValues();
        Domain domainXY = domain.intersect(d0);
        Range r = Domain.range(domainXY, null, x, y, z);
        if (r != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r.setDefined(def0);
            if(d0!=null || ranges!=null){
                for (int _z = 0; _z < z.length; _z++) {
                    for (int _y = 0; _y < y.length; _y++) {
                        for (int _x = 0; _x < x.length; _x++) {
                            if(!domainXY.contains(x[_x],y[_y],z[_z])){
                                values[_z][_y][_x]=Complex.ZERO;
                            }else{
                                def0.set(_z, _y, _x);
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r);
            }
        }else{
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
        return values;
    }

}
