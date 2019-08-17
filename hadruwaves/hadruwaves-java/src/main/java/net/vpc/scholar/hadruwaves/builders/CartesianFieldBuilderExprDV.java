package net.vpc.scholar.hadruwaves.builders;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DMHelper3;
import net.vpc.scholar.hadrumaths.symbolic.conv.DC2DVHelper3;
import net.vpc.scholar.hadrumaths.symbolic.conv.DV2DM;

import java.util.Collections;
import java.util.List;

public class CartesianFieldBuilderExprDV extends AbstractExprPropertyAware implements DoubleToVector {
    private CartesianFieldBuilder builder;
    private Domain domain;
    private DoubleToComplex[] items;
    private DC2DVHelper3 dc2dv;
//    private Axis axis = Axis.X;
//    private DC2DMHelper dc2dm;
    public CartesianFieldBuilderExprDV(CartesianFieldBuilder builder, Domain domain) {
        this.builder = builder;
        this.domain = domain;
        items=new DoubleToComplex[]{
                new CartesianFieldBuilderExprD2C(builder,Axis.X,domain),
                new CartesianFieldBuilderExprD2C(builder,Axis.Y,domain),
                new CartesianFieldBuilderExprD2C(builder,Axis.Z,domain),
        };
        dc2dv=new DC2DVHelper3(items[0],items[1],items[2]);
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
        return ComponentDimension.VECTOR3;
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
        return false;
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
    public DoubleToMatrix toDM() {
        return new DV2DM(this);
    }

    @Override
    public Expr getComponent(Axis a) {
        return dc2dv.getComponent(a);
    }

    @Override
    public int getComponentSize() {
        return dc2dv.getComponentSize();
    }

    @Override
    public Vector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        return dc2dv.computeVector(x, y, z, d0, ranges);
    }

    @Override
    public Vector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        return dc2dv.computeVector(x,y,d0,ranges);
    }

    @Override
    public Vector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        return dc2dv.computeVector(x,d0,ranges);
    }

    @Override
    public Vector computeVector(double x, double y, double z, BooleanMarker defined) {
        return dc2dv.computeVector(x,y,defined);
    }

    @Override
    public Vector computeVector(double x, double y, BooleanMarker defined) {
        return dc2dv.computeVector(x,y,defined);
    }

    @Override
    public Vector computeVector(double x, BooleanMarker defined) {
        return dc2dv.computeVector(x,defined);
    }
}
