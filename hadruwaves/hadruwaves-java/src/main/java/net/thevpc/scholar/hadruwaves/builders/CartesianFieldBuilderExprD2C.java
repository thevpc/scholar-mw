package net.thevpc.scholar.hadruwaves.builders;

import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.thevpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

public class CartesianFieldBuilderExprD2C implements DoubleToComplex {
    static {
        FormatFactory.register(CartesianFieldBuilderExprD2C.class, new AbstractObjectFormat<CartesianFieldBuilderExprD2C>() {
            @Override
            public void format(CartesianFieldBuilderExprD2C o, ObjectFormatContext context) {
                ObjectFormatParamSet format = context.getParams();
                context.append("CartesianFieldBuilderExprD2C(");
                context.format(o.domain, format.remove(FormatFactory.REQUIRED_PARS));
//                sb.append(", ");
                context.append(",");
                context.format(o.axis, format.remove(FormatFactory.REQUIRED_PARS));
                context.append(")");
            }
        });
    }

    private CartesianFieldBuilder builder;
    private Domain domain;
    private Axis axis;

    public CartesianFieldBuilderExprD2C(CartesianFieldBuilder builder, Axis axis, Domain domain) {
        this.builder = builder;
        this.domain = domain;
        this.axis = axis;
    }

    @Override
    public boolean isInvariant(Axis axis) {
        return false;
    }


    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
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
    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        Out<Range> r = new Out<>();
        Complex[][][] p = evalComplex(x, new double[]{domain.ymin()}, new double[]{domain.zmin()}, d0 == null ? null : d0.toDomain(2), r);
        if (ranges != null) {
            ranges.set(r.get().toDimension(1));
        }
        return p[0][0];
    }

    @Override
    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        Out<Range> r = new Out<>();
        Complex[][] p = evalComplex(x, new double[]{y}, d0 == null ? null : d0.toDomain(2), r);
        if (ranges != null) {
            ranges.set(r.get());
        }
        return p[0];
    }

    @Override
    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        Out<Range> r = new Out<>();
        Complex[][] p = evalComplex(new double[]{x}, y, d0 == null ? null : d0.toDomain(2), r);
        if (ranges != null) {
            ranges.set(r.get());
        }
        Complex[] c = new Complex[y.length];
        for (int i = 0; i < c.length; i++) {
            c[i] = p[i][0];
        }
        return c;
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Complex[][][] values = builder.evalVDiscrete(x, y, z).getComponentDiscrete(axis).getValues();
        Domain domainXY = domain.intersect(d0);
        Range r = Domain.range(domainXY, null, x, y, z);
        if (r != null) {
            BooleanArray3 def0 = BooleanArrays.newArray(z.length, y.length, x.length);
            r.setDefined(def0);
            if (d0 != null || ranges != null) {
                for (int _z = 0; _z < z.length; _z++) {
                    for (int _y = 0; _y < y.length; _y++) {
                        for (int _x = 0; _x < x.length; _x++) {
                            if (!domainXY.contains(x[_x], y[_y], z[_z])) {
                                values[_z][_y][_x] = Complex.ZERO;
                            } else {
                                def0.set(_z, _y, _x);
                            }
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r);
            }
        } else {
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
        return values;
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        Complex[][] values = builder.evalVDiscrete(x, y).getComponentDiscrete(axis).getValues()[0];
        Domain domainXY = domain.intersect(d0);
        Range r = Domain.range(domainXY, null, x, y);
        if (r != null) {
            BooleanArray2 def0 = BooleanArrays.newArray(y.length, x.length);
            r.setDefined(def0);
            if (d0 != null || ranges != null) {
                for (int _y = 0; _y < y.length; _y++) {
                    for (int _x = 0; _x < x.length; _x++) {
                        if (!domainXY.contains(x[_x], y[_y])) {
                            values[_y][_x] = Complex.ZERO;
                        } else {
                            def0.set(_y, _x);
                        }
                    }
                }
            }
            if (ranges != null) {
                ranges.set(r);
            }
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
        return values;
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        if (defined != null && contains(x)) {
            defined.set();
        }
        return builder.evalVector(axis, new double[]{x}, domain.ymin(), domain.zmin()).get(0);
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
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        if (defined != null && contains(x, y)) {
            defined.set();
        }
        return builder.evalVector(axis, new double[]{x}, y, domain.zmin()).get(0);
    }

    @Override
    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        if (defined != null && contains(x, y, z)) {
            defined.set();
        }
        return builder.evalVector(axis, new double[]{x}, y, z).get(0);
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
