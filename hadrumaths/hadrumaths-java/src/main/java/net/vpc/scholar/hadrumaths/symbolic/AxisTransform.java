/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransform;
import net.vpc.scholar.hadrumaths.transform.ExpressionTransformer;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author vpc
 */
public class AxisTransform extends AbstractVerboseExpr implements Cloneable {
    private static final long serialVersionUID = 1L;

    static {
        ExpressionTransformFactory.setExpressionTransformer(AxisTransform.class, ExpressionTransform.class, new ExpressionTransformer() {

            public Expr transform(Expr expression, ExpressionTransform transform) {
                AxisTransform e = (AxisTransform) expression;
                return new AxisTransform(ExpressionTransformFactory.transform(e.expression, transform), e.axis, e.dim);
            }
        });
    }

    private Expr expression;
    private Axis[] axis;
    private int dim;
    private Domain domain;

    public AxisTransform(Expr expression, Axis[] axis, int dim) {
        this.expression = expression;
        this.axis = axis;
        this.dim = dim;
        domain = expression.getDomain().transform(axis[0], axis[1], axis[2], dim);
    }

    @Override
    public boolean isDoubleTyped() {
        return expression.isDoubleTyped();
    }


    public Domain convertDomain(Domain d) {
        if (d == null) {
            return null;
        }
        return domain.transform(axis[0], axis[1], axis[2], dim);
    }

    public Range convertBackRange(Range r) {
        if (r == null) {
            return null;
        }
        int xindex = axis[0].equals(Axis.X) ? 0 : axis[1].equals(Axis.X) ? 1 : 2;
        int yindex = axis[0].equals(Axis.Y) ? 0 : axis[1].equals(Axis.Y) ? 1 : 2;
        int zindex = axis[0].equals(Axis.Z) ? 0 : axis[1].equals(Axis.Z) ? 1 : 2;
        int xmin = xindex == 0 ? r.xmin : xindex == 1 ? r.ymin : r.zmin;
        int xmax = xindex == 0 ? r.xmax : xindex == 1 ? r.ymax : r.zmax;

        int ymin = yindex == 0 ? r.xmin : yindex == 1 ? r.ymin : r.zmin;
        int ymax = yindex == 0 ? r.xmax : yindex == 1 ? r.ymax : r.zmax;

        int zmin = zindex == 0 ? r.xmin : zindex == 1 ? r.ymin : r.zmin;
        int zmax = zindex == 0 ? r.xmax : zindex == 1 ? r.ymax : r.zmax;
        if (r.dimension == 2) {
            if ((axis[0] == Axis.X && axis[1] == Axis.Y) || (axis[1] == Axis.X && axis[0] == Axis.Y)) {
                return Range.forBounds(xmin, xmax, ymin, ymax);
            }
        }
        return Range.forBounds(xmin, xmax, ymin, ymax, zmin, zmax);
    }

    public double[][][] convertBackXYZValues(double[][][] xyz) {
        int[] count = {xyz[0][0].length, xyz[0].length, xyz.length};
        int xindex = axis[0].equals(Axis.X) ? 0 : axis[1].equals(Axis.X) ? 1 : 2;
        int yindex = axis[0].equals(Axis.Y) ? 0 : axis[1].equals(Axis.Y) ? 1 : 2;
        int zindex = axis[0].equals(Axis.Z) ? 0 : axis[1].equals(Axis.Z) ? 1 : 2;
        double[][][] ret = new double[count[zindex]][count[yindex]][count[xindex]];
        for (int z = 0; z < ret.length; z++) {
            for (int y = 0; y < ret[z].length; y++) {
                for (int x = 0; x < ret[z][y].length; x++) {
                    int[] t = {x, y, z};
                    ret[z][y][x] = xyz[t[zindex]][t[yindex]][t[xindex]];
                }
            }
        }
        return ret;
    }

    public Complex[][][] convertBackXYZValues(Complex[][][] xyz) {
        int[] count = {xyz[0][0].length, xyz[0].length, xyz.length};
        int xindex = axis[0].equals(Axis.X) ? 0 : axis[1].equals(Axis.X) ? 1 : 2;
        int yindex = axis[0].equals(Axis.Y) ? 0 : axis[1].equals(Axis.Y) ? 1 : 2;
        int zindex = axis[0].equals(Axis.Z) ? 0 : axis[1].equals(Axis.Z) ? 1 : 2;
        Complex[][][] ret = new Complex[count[zindex]][count[yindex]][count[xindex]];
        for (int z = 0; z < ret.length; z++) {
            for (int y = 0; y < ret[z].length; y++) {
                for (int x = 0; x < ret[z][y].length; x++) {
                    int[] t = {x, y, z};
                    ret[z][y][x] = xyz[t[zindex]][t[yindex]][t[xindex]];
                }
            }
        }
        return ret;
    }

    public Matrix[][][] convertBackXYZValues(Matrix[][][] xyz) {
        int[] count = {xyz[0][0].length, xyz[0].length, xyz.length};
        int xindex = axis[0].equals(Axis.X) ? 0 : axis[1].equals(Axis.X) ? 1 : 2;
        int yindex = axis[0].equals(Axis.Y) ? 0 : axis[1].equals(Axis.Y) ? 1 : 2;
        int zindex = axis[0].equals(Axis.Z) ? 0 : axis[1].equals(Axis.Z) ? 1 : 2;
        Matrix[][][] ret = new Matrix[count[zindex]][count[yindex]][count[xindex]];
        for (int z = 0; z < ret.length; z++) {
            for (int y = 0; y < ret[z].length; y++) {
                for (int x = 0; x < ret[z][y].length; x++) {
                    int[] t = {x, y, z};
                    ret[z][y][x] = xyz[t[zindex]][t[yindex]][t[xindex]];
                }
            }
        }
        return ret;
    }

    public double[][] convertXYZAxis(double[] x, double[] y, double[] z) {
        double[][] r = new double[3][];
        for (int i = 0; i < axis.length; i++) {
            switch (axis[i]) {
                case X: {
                    r[i] = x;
                    break;
                }
                case Y: {
                    r[i] = y;
                    break;
                }
                case Z: {
                    r[i] = z;
                    break;
                }
            }
        }
        return r;
    }

    public Axis convert(Axis axis) {
        return this.axis[axis.ordinal()];
    }

    public boolean isInvariantImpl(Axis axis) {
        if (!expression.isInvariant(convert(axis))) {
            return false;
        }
        return true;
    }

    public boolean isZeroImpl() {
        if (!expression.isInfinite()) {
            return false;
        }
        return true;
    }

    public boolean isNaNImpl() {
        if (!expression.isNaN()) {
            return true;
        }
        return false;
    }

    public boolean isInfiniteImpl() {
        if (!expression.isZero()) {
            return true;
        }
        return false;
    }

    public Expr getExpression() {
        return expression;
    }


    public boolean isDCImpl() {
        return expression.isDC();
    }

//    public boolean isDDx() {
//        return expression.isDDx();
//    }

    public boolean isDDImpl() {
        return expression.isDC();
    }

    public boolean isDMImpl() {
        return expression.isDM();
    }

    public DoubleToComplex toDC() {
        if (!isDC()) {
            throw new ClassCastException();
        }
        return this;
    }

    public DoubleToDouble toDD() {
        if (!isDD()) {
            throw new ClassCastException();
        }
        return this;
    }

//    public IDDx toDDx() {
//        if (!isDDx()) {
//            throw new ClassCastException();
//        }
//        return this;
//    }

    public DoubleToMatrix toDM() {
        if (!isDM()) {
            throw new ClassCastException();
        }
        return this;
    }

    @Override
    public Domain getDomainImpl() {
        return domain;
    }

//    @Override
//    public Domain getDomain() {
//        Domain d = Domain.NaNX;
//        if (isDDx()) {
//            return expression.toDDx().getDomain();
//        } else {
//            throw new ClassCastException();
//        }
//    }

    @Override
    public ComponentDimension getComponentDimension() {
        return expression.getComponentDimension();
    }

    public Expr getComponent(int row, int col) {
        if (isDM()) {
            if (isScalarExpr() && (row != col || col != 0)) {
                return FunctionFactory.DZEROXY;
            }
            return new AxisTransform(expression.toDM().getComponent(row, col), axis, dim);
        } else {
            throw new ClassCastException();
        }
    }


    @Override
    public Complex[][] computeComplex(double[] x0, double[] y0, Domain domain, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, y0, new double[0]);
        double[] x = xyz[0];
        double[] y = xyz[1];
        Domain d0 = convertDomain(domain);
        Domain domainXY = getDomain();
        d0 = domainXY.intersect(d0);
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            Complex[][] ret = expression.toDC().computeComplex(x, y, d0, r2);
            ret = convertBackXYZValues(new Complex[][][]{ret})[0];
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            Complex[][] ret = ArrayUtils.fillArray2Complex(x0.length, y0.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public Complex[] computeComplex(double[] x0, Domain domain, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, new double[0], new double[0]);
        double[] x = xyz[0];
        Domain d0 = convertDomain(domain);
        Domain domainXY = getDomain();
        d0 = domainXY.intersect(d0);
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            Complex[] ret = expression.toDC().computeComplex(x, d0, r2);
            ret = convertBackXYZValues(new Complex[][][]{{ret}})[0][0];
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            Complex[] ret = ArrayUtils.fillArray1Complex(x0.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public Complex[][][] computeComplex(double[] x0, double[] y0, double[] z0, Domain d00, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, y0, z0);
        double[] x = xyz[0];
        double[] y = xyz[1];
        double[] z = xyz[2];
        Domain d0 = convertDomain(d00);
        Domain domainXY = getDomain();
        d0 = domainXY.intersect(d0);
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            Complex[][][] ret = expression.toDC().computeComplex(x, y, z, d0, r2);
            ret = convertBackXYZValues(ret);
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            Complex[][][] ret = ArrayUtils.fillArray3Complex(x0.length, y0.length, z0.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public double[][] computeDouble(double[] x0, double[] y0, Domain domain, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, y0, new double[0]);
        double[] x = xyz[0];
        double[] y = xyz[1];
        Domain domainXY = getDomain();
        Domain d0 = convertDomain(domain);
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            double[][] ret = expression.toDD().computeDouble(x, y, d0, r2);
            ret = convertBackXYZValues(new double[][][]{ret})[0];
            if (ranges != null) {
                //eval r
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            double[][] ret = new double[y0.length][x0.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public double[][][] computeDouble(double[] x0, double[] y0, double[] z0, Domain d00, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, y0, z0);
        double[] x = xyz[0];
        double[] y = xyz[1];
        double[] z = xyz[2];
        Domain domainXY = getDomain();
        Domain d0 = convertDomain(d00);
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            double[][][] ret = expression.toDD().computeDouble(x, y, z, d0, r2);
            ret = convertBackXYZValues(ret);
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            double[][][] ret = new double[z0.length][y0.length][x0.length];
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public Matrix[][] computeMatrix(double[] x0, double[] y0, Domain d00, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, y0, new double[0]);
        double[] x = xyz[0];
        double[] y = xyz[1];
        Domain domainXY = getDomain();
        Domain d0 = convertDomain(d00);
        Range r = Domain.range(domainXY, d0, x, y);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            Matrix[][] ret = expression.toDM().computeMatrix(x, y, d0, r2);
            ret = convertBackXYZValues(new Matrix[][][]{ret})[0];
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            ComponentDimension d = getComponentDimension();
            Matrix[][] ret = ArrayUtils.fillArray2Matrix(x0.length, y0.length, Maths.zerosMatrix(d.rows, d.columns));
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public Matrix[] computeMatrix(double[] x0, Domain d00, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, new double[0], new double[0]);
        double[] x = xyz[0];
        Domain domainXY = getDomain();
        Domain d0 = convertDomain(d00);
        Range r = Domain.range(domainXY, d0, x);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            Matrix[] ret = expression.toDM().computeMatrix(x, d0, r2);
            ret = convertBackXYZValues(new Matrix[][][]{{ret}})[0][0];
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            ComponentDimension d = getComponentDimension();
            Matrix[] ret = ArrayUtils.fill(new Matrix[x0.length], Maths.zerosMatrix(d.rows, d.columns));
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public Matrix[][][] computeMatrix(double[] x0, double[] y0, double[] z0, Domain d00, Out<Range> ranges) {
        double[][] xyz = convertXYZAxis(x0, y0, z0);
        double[] x = xyz[0];
        double[] y = xyz[1];
        double[] z = xyz[2];
        Domain domainXY = getDomain();
        Domain d0 = convertDomain(d00);
        Range r = Domain.range(domainXY, d0, x, y, z);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            Matrix[][][] ret = expression.toDM().computeMatrix(x, y, z, d0, r2);
            ret = convertBackXYZValues(ret);
            if (ranges != null) {
                ranges.set(convertBackRange(r));
            }
            return ret;
        } else {
            ComponentDimension d = getComponentDimension();
            Matrix[][][] ret = ArrayUtils.fillArray3Matrix(x0.length, y0.length, z0.length, Maths.zerosMatrix(d.rows, d.columns));
            if (ranges != null) {
                ranges.set(null);
            }
            return ret;
        }
    }

    @Override
    public double[] computeDouble(double[] x0, Domain d00, Out<Range> range) {
        double[][] xyz = convertXYZAxis(x0, new double[0], new double[0]);
        double[] x = xyz[0];
        Domain domainX = getDomain();
        Domain d0 = convertDomain(d00);
        Range r = Domain.range(domainX, d0, x);
        if (r != null) {
            Out<Range> r2 = new Out<Range>();
            double[] ret = expression.toDD().computeDouble(x, d0, r2);
            ret = convertBackXYZValues(new double[][][]{{ret}})[0][0];
            if (range != null) {
                range.set(r);
            }
            return ret;
        } else {
            double[] ret = new double[x0.length];
            if (range != null) {
                range.set(null);
            }
            return ret;
        }
    }

    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    public Matrix[] computeMatrix(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    public Matrix[] computeMatrix(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeMatrix(this, x, y, d0, ranges);
    }

    @Override
    public Matrix computeMatrix(double x) {
        return computeMatrix(new double[]{x}, (Domain) null, null)[0];
    }

    public Matrix computeMatrix(double x, double y) {
        return Expressions.computeMatrix(this, x, y);
    }

    public double[] computeDouble(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double[] computeDouble(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeDouble(this, x, y, d0, ranges);
    }

    public double computeDouble(double x, double y, BooleanMarker defined) {
        return Expressions.computeDouble(this, x, y, defined);
    }

//    public double computeDouble(double x) {
//        return Expressions.computeDouble(this, x);
//    }


    @Override
    public DoubleToDouble getRealDD() {
        Expr a = expression;
        if (a.isDC() && a.toDC().getImagDD().isZero()) {
            return this;
        }
        return new Real(toDC());
    }

    @Override
    public DoubleToDouble getImagDD() {
        Expr a = expression;
        if (a.isDC() && a.toDC().getImagDD().isZero()) {
            return FunctionFactory.DZERO(dim);
        }
        return new Imag(toDC());
    }

    public Expr clone() {
        AxisTransform cloned = (AxisTransform) super.clone();
        cloned.expression = expression.clone();
        return cloned;
    }

    public List<Expr> getSubExpressions() {
        return Arrays.asList(expression);
    }

    @Override
    public Complex toComplex() {
        return expression.toComplex();
    }

    @Override
    public double toDouble() {
        return expression.toDouble();
    }

    @Override
    public Matrix toMatrix() {
        return expression.toMatrix();
    }

    @Override
    public Expr setParam(String name, Expr value) {
        Expr updated = expression.setParam(name, value);
        if (updated != expression) {
            Expr e = new AxisTransform(updated, axis, dim);
            e = Any.copyProperties(this, e);
            return Any.updateTitleVars(e, name, value);
        }
        return this;
    }

    @Override
    public Expr composeX(Expr xreplacement) {
        Expr updated = expression.composeX(xreplacement);
        if (updated != expression) {
            Expr e = new AxisTransform(updated, axis, dim);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public Expr composeY(Expr yreplacement) {
        Expr updated = expression.composeY(yreplacement);
        if (updated != expression) {
            Expr e = new AxisTransform(updated, axis, dim);
            e = Any.copyProperties(this, e);
            return e;
        }
        return this;
    }

    @Override
    public String getComponentTitle(int row, int col) {
        return Expressions.getMatrixExpressionTitleByChildren(this, row, col);
    }

    @Override
    public boolean isScalarExprImpl() {
        return expression.isScalarExpr();
    }

    @Override
    public DoubleToComplex getComponent(Axis a) {
        switch (a) {
            case X: {
                return getComponent(0, 0).toDC();
            }
            case Y: {
                return getComponent(1, 0).toDC();
            }
            case Z: {
                return getComponent(2, 0).toDC();
            }
        }
        throw new IllegalArgumentException("Illegal axis");
    }


    @Override
    public DoubleToVector toDV() {
        if (!isDV()) {
            throw new ClassCastException();
        }
        return this;
    }

    @Override
    public int getDomainDimension() {
        return expression.getDomainDimension();
    }


    @Override
    public Complex computeComplex(double x, BooleanMarker defined) {
        if (contains(x)) {
            if (true) {
                throw new IllegalArgumentException("Missing  Y and Y");
            }
            double[][] xyz = convertXYZAxis(new double[]{x}, new double[]{0}, new double[]{0});
            x = xyz[0][0];
            return expression.toDC().computeComplex(x, defined);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, BooleanMarker defined) {
        if (contains(x, y)) {
            if (true) {
                throw new IllegalArgumentException("Missing  Y and Y");
            }
            double[][] xyz = convertXYZAxis(new double[]{x}, new double[]{y}, new double[]{0});
            x = xyz[0][0];
            y = xyz[1][0];
            return expression.toDC().computeComplex(x, y, defined);
        }
        return Complex.ZERO;
    }

    @Override
    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            double[][] xyz = convertXYZAxis(new double[]{x}, new double[]{y}, new double[]{z});
            x = xyz[0][0];
            y = xyz[1][0];
            z = xyz[2][0];
            return expression.toDC().computeComplex(x, y, z, defined);
        }
        return Complex.ZERO;
    }

    @Override
    public double computeDouble(double x, double y, double z, BooleanMarker defined) {
        if (contains(x, y, z)) {
            double[][] xyz = convertXYZAxis(new double[]{x}, new double[]{y}, new double[]{z});
            x = xyz[0][0];
            y = xyz[1][0];
            z = xyz[2][0];
            return expression.toDD().computeDouble(x, y, z, defined);
        }
        return 0;
    }

    @Override
    public Matrix computeMatrix(double x, double y, double z) {
        double[][] xyz = convertXYZAxis(new double[]{x}, new double[]{y}, new double[]{z});
        x = xyz[0][0];
        y = xyz[1][0];
        z = xyz[2][0];
        return expression.toDM().computeMatrix(x, y, z);
    }

//    @Override
//    public Complex computeComplex(double x,BooleanMarker defined) {
//        double[][] xyz = convertXYZAxis(new double[]{x}, new double[0], new double[0]);
//        x = xyz[0][0];
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    public Axis[] getAxis() {
        return axis;
    }


    @Override
    public boolean isDoubleImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDouble()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isComplexImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isComplex()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDoubleExpr()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isMatrixImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isMatrix()) {
                return false;
            }
        }
        return true;
    }


//    @Override
//    public boolean isDDx() {
//        for (Expr e : getSubExpressions()) {
//            if(!e.isDDx()){
//                return false;
//            }
//        }
//        return true;
//    }


    @Override
    public boolean isDVImpl() {
        for (Expr e : getSubExpressions()) {
            if (!e.isDV()) {
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AxisTransform)) return false;
        if (!super.equals(o)) return false;

        AxisTransform that = (AxisTransform) o;

        if (dim != that.dim) return false;
        if (!Arrays.equals(axis, that.axis)) return false;
        if (domain != null ? !domain.equals(that.domain) : that.domain != null) return false;
        if (expression != null ? !expression.equals(that.expression) : that.expression != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        result = 31 * result + (axis != null ? Arrays.hashCode(axis) : 0);
        result = 31 * result + dim;
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

//    @Override
//    public double computeDouble(double x) {
//        throw new IllegalArgumentException("Missing Y");
//    }

    @Override
    public double computeDouble(double x, BooleanMarker defined) {
        throw new IllegalArgumentException("Missing Y");
    }

}
