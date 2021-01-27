package net.thevpc.scholar.hadrumaths.symbolic.double2complex;


import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
import net.thevpc.scholar.hadrumaths.geom.IntPoint;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 mai 2007 22:18:44
 */
public class CDiscrete implements DoubleToComplex, Normalizable {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(CDiscrete.class, new AbstractObjectFormat<CDiscrete>() {
            @Override
            public void format(CDiscrete o, ObjectFormatContext context) {
                context.append("CDiscrete(");
                context.append(Maths.dump(o.values));
                context.append(")");
            }
        });
    }

    private Axis[] axis;
    private Complex[/*Z*/][/*Y*/][/*X*/] values;
    private double[] x;
    private double[] y;
    private double[] z;
    private int zcount;
    private int ycount;
    private int xcount;
    private double dx;
    private double dy;
    private double dz;
    private Domain domain;

    public CDiscrete(Complex[][][] values) {
        this(values, null, Axis.X, Axis.Y, Axis.Z);
    }

    public CDiscrete(Complex[][][] values, Domain domain, Axis axis1, Axis axis2, Axis axis3) {
        init(domain, values, axis1, axis2, axis3);
    }

    private void init(Domain domain, Complex[][][] model, Axis axis1, Axis axis2, Axis axis3) {
        if (axis1 == null || axis2 == null || axis3 == null) {
            if (!(axis1 == null && axis2 == null && axis3 == null)) {
                throw new IllegalArgumentException("Invalid axis");
            }
            axis1 = Axis.X;
            axis2 = Axis.Y;
            axis3 = Axis.Z;
        } else {
            if (axis1.equals(axis2) || axis2.equals(axis3) || axis3.equals(axis1)) {
                throw new IllegalArgumentException();
            }
        }

        if (domain == null) {
            throw new IllegalArgumentException("Invalid domain : null");
//            zcount = model.length;
//            ycount = model.length == 0 ? 0 : model[0].length;
//            xcount = model.length == 0 ? 0 : model[0][0].length;
//            if (xcount == 0) {
//                throw new IllegalArgumentException("Invalid X dimension size " + xcount);
//            }
//            if (ycount == 0) {
//                throw new IllegalArgumentException("Invalid Y dimension size " + ycount);
//            }
//            if (zcount == 0) {
//                throw new IllegalArgumentException("Invalid Z dimension size " + zcount);
//            }
//            this.dx = xcount == 1 ? 1 : (model[0][0][model[0][0].length-1]-model[0][0][0]) / (xcount - 1);
//            this.dy = ycount == 1 ? 1 : (domain.ywidth()) / (ycount - 1);
//            this.dz = zcount == 1 ? 1 : (domain.zwidth()) / (zcount - 1);
//
//            AbsoluteSamples steps = domain.times(xcount, ycount, zcount);
//            this.x = steps.getX();
//            this.y = steps.getY();
//            this.z = steps.getZ();
//
//            this.domain = domain;
        }
        this.domain = domain;

        this.axis = new Axis[]{axis1, axis2, axis3};
        switch (domain.dimension()) {
            case 1: {
                this.values = model;
                zcount = 1;
                ycount = 1;
                this.dy = domain.ywidth();
                this.dz = domain.zwidth();
                xcount = model.length == 0 ? 0 : model[0][0].length;
                if (xcount == 0) {
                    throw new IllegalArgumentException("Invalid X dimension size " + xcount);
                }
                this.dx = this.domain.xwidth() / xcount;
                this.x = domain.steps(dx).getX();
                this.y = new double[]{domain.ymin()};
                this.z = new double[]{domain.zmin()};
                break;
            }
            case 2: {
                this.values = model;
                zcount = 1;
                ycount = model.length == 0 ? 0 : model[0].length;
                xcount = model.length == 0 ? 0 : model[0][0].length;
                if (xcount == 0) {
                    throw new IllegalArgumentException("Invalid X dimension size " + xcount);
                }
                if (ycount == 0) {
                    throw new IllegalArgumentException("Invalid Y dimension size " + ycount);
                }
                this.dx = xcount == 1 ? domain.xwidth() : (domain.xwidth()) / (xcount - 1);
                this.dy = ycount == 1 ? domain.ywidth() : (domain.ywidth()) / (ycount - 1);
                this.dz = 1;

                AbsoluteSamples steps = domain.times(xcount, ycount);
                this.x = steps.getX();
                this.y = steps.getY();
                this.z = new double[]{domain.zmin()};
                break;
            }
            case 3: {
                this.values = model;
                zcount = model.length;
                ycount = model.length == 0 ? 0 : model[0].length;
                xcount = model.length == 0 ? 0 : model[0][0].length;
                if (xcount == 0) {
                    throw new IllegalArgumentException("Invalid X dimension size " + xcount);
                }
                if (ycount == 0) {
                    throw new IllegalArgumentException("Invalid Y dimension size " + ycount);
                }
                if (zcount == 0) {
                    throw new IllegalArgumentException("Invalid Z dimension size " + zcount);
                }
                this.dx = xcount == 1 ? domain.xwidth() : (domain.xwidth()) / (xcount - 1);
                this.dy = ycount == 1 ? domain.ywidth() : (domain.ywidth()) / (ycount - 1);
                this.dz = zcount == 1 ? domain.zwidth() : (domain.zwidth()) / (zcount - 1);

                AbsoluteSamples steps = domain.times(xcount, ycount, zcount);
                this.x = steps.getX();
                this.y = steps.getY();
                this.z = steps.getZ();
                break;
            }
        }

    }

    public CDiscrete(Complex[][][] values, Domain domain) {
        this(values, domain, Axis.X, Axis.Y, Axis.Z);
    }

    public static CDiscrete cst(Domain domain, Complex value, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        AbsoluteSamples steps = domain.steps(dx, dy, dz);
        Complex[][][] model = ArrayUtils.fillArray3Complex(steps.getX().length, steps.getY().length, steps.getZ().length, value);
        return new CDiscrete(model, domain, axis1, axis2, axis3);
    }

    public static CDiscrete discretize(Expr expr, Domain domain, int nx) {
        return discretize(expr, domain, nx, -1, -1);
    }

    public static CDiscrete discretize(Expr expr, Domain domain, int nx, int ny, int nz) {
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (expr.is(ExprDim.SCALAR)) {
            switch (domain.getDimension()) {
                case 1: {
                    if (nx <= 0) {
                        nx = 1;
                    }
                    AbsoluteSamples absoluteSamples = domain.times(nx);
                    Complex[] model = expr.toDC().evalComplex(absoluteSamples.getX(), (Domain) null, null);
                    return CDiscrete.of(domain, model);
                }
                case 2: {
                    if (nx <= 0) {
                        nx = 1;
                    }
                    if (ny <= 0) {
                        ny = 1;
                    }
                    AbsoluteSamples absoluteSamples = domain.times(nx, ny);
                    Complex[][] model = expr.toDC().evalComplex(absoluteSamples.getX(), absoluteSamples.getY());
                    return CDiscrete.of(domain, model);
                }
                case 3: {
                    if (nx <= 0) {
                        nx = 1;
                    }
                    if (ny <= 0) {
                        ny = 1;
                    }
                    if (nz <= 0) {
                        nz = 1;
                    }
                    if (expr.isZero() && domain.isInfinite()) {
                        return CDiscrete.of(
                                Domain.EMPTY(domain.dimension()),
                                new Complex[0][0][0]
                        );
                    }
                    AbsoluteSamples absoluteSamples = domain.times(nx, ny, nz);
                    Complex[][][] model = expr.toDC().evalComplex(absoluteSamples.getX(), absoluteSamples.getY(), absoluteSamples.getZ(), null, null);
                    return CDiscrete.of(domain, model);
                }
            }
            throw new IllegalArgumentException("Unsupported dimension " + domain.getDimension());
        } else {
            throw new IllegalArgumentException("Expression is not scalar");
        }
    }

    public static CDiscrete of(Domain domain, Complex[] model) {
        return new CDiscrete(new Complex[][][]{{model}}, domain, Axis.X, Axis.Y, Axis.Z);
    }

    public static CDiscrete of(Domain domain, Complex[][] model) {
        return new CDiscrete(new Complex[][][]{model}, domain, Axis.X, Axis.Y, Axis.Z);
    }

    public static CDiscrete of(Domain domain, Complex[][][] model) {
        return new CDiscrete(model, domain, Axis.X, Axis.Y, Axis.Z);
    }

    public static CDiscrete discretize(Expr expr, Domain domain, int nx, int ny) {
        return discretize(expr, domain, nx, ny, -1);
    }

    public CDiscrete plus(Complex c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].plus(c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public static CDiscrete of(Domain domain, Complex[][][] model, Axis axis1, Axis axis2, Axis axis3) {
        return new CDiscrete(model, domain, axis1, axis2, axis3);
    }

    public CDiscrete sub(Complex c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].minus(c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public IntPoint getIndices(double x, double y, double z) {
        switch (domain.dimension()) {
            case 1: {
                int i = xIndex(x);
                return IntPoint.create(i);
            }
            case 2: {
                int i = xIndex(x);
                int j = yIndex(y);
                return IntPoint.create(i, j);
            }
        }
        int i = xIndex(x);
        int j = yIndex(y);
        int k = zIndex(z);
        return IntPoint.create(i, j, k);
    }

    protected int xIndex(double x) {
        if (x >= domain.xmax()) {
            return -1;
        }
        int i = (int) Math.floor((x - domain.xmin()) / dx);
        if (i < 0 || i >= xcount) {
            return -1;
        }
        return i;
    }

    protected int yIndex(double y) {
        if (y >= domain.ymax()) {
            return -1;
        }
        int i = (int) Math.floor((y - domain.ymin()) / dy);
        if (i < 0 || i >= ycount) {
            return -1;
        }
        return i;
    }

    protected int zIndex(double zz) {
        if (zz >= domain.zmax()) {
            return -1;
        }
        int i = (int) Math.floor((zz - domain.zmin()) / dz);
        if (i < 0 || i >= zcount) {
            return -1;
        }
        return i;
    }

    public Point getPointAt(IntPoint indices) {
        return getPointAt(indices.x, indices.y, indices.z);
    }

    public Point getPointAt(int xi, int yi, int zi) {
        switch (domain.dimension()) {
            case 1: {
                if (xi < 0 || xi >= x.length) {
                    return null;
                }
                double x = domain.xmin() + dx * xi;
                return Point.create(x);
            }
            case 2: {
                if (xi < 0 || xi >= x.length) {
                    return null;
                }
                double x = domain.xmin() + dx * xi;
                if (yi < 0 || yi >= y.length) {
                    return null;
                }
                double y = domain.ymin() + dy * yi;
                return Point.create(x, y);
            }
        }
        if (xi < 0 || xi >= x.length) {
            return null;
        }
        double x = domain.xmin() + dx * xi;
        if (yi < 0 || yi >= y.length) {
            return null;
        }
        double y = domain.ymin() + dy * yi;
        if (zi < 0 || zi >= z.length) {
            return null;
        }
        double z = domain.zmin() + dz * zi;
        return Point.create(x, y, z);
    }

    public CDiscrete plus(DoubleToComplex other) {
        if (other instanceof CDiscrete) {
            CDiscrete c = (CDiscrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k].plus(c.values[i][j][k]);
                        }
                    }
                }
                return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
            }
            Domain d2 = getDomain().expand(other.getDomain());
            double dx = Math.min(this.dx, c.dx);
            double dy = Math.min(this.dy, c.dy);
            double dz = Math.min(this.dz, c.dz);
            AbsoluteSamples xyz = d2.steps(dx, dy, dz);
            Complex[][][] a1 = evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] a2 = c.evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] d = ArrayUtils.add(a1, a2);
            return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        Complex[][][] a1 = evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] a2 = other.evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] d = ArrayUtils.add(a1, a2);
        return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete relativeError(CDiscrete other) {
        if (getDomain().equals(other.getDomain()) || dx != other.dx || dy != other.dy || dz != other.dz) {
            throw new IllegalArgumentException("Cubes don(t match");
        }
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    Complex a = this.values[i][j][k];
                    Complex b = other.values[i][j][k];
                    Complex c;
                    if (a.equals(b) || (a.isNaN() && b.isNaN()) || (a.isInfinite() && b.isInfinite())) {
                        c = Maths.CZERO;
//                    } else if (b.isNaN() || b.isInfinite() || b.equals(Math2.CZERO)) {
//                        c[i][j] = (a.substract(b));
                    } else {
                        c = Complex.of(((a.minus(b)).absdbl() * 100 / (b.absdbl())));
                    }

                    d[i][j][k] = c;
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public Complex integrate(Domain domain, PlaneAxis axis, double fixedValue) {
        Domain domain0 = getDomain();
        Domain d2 = domain0.intersect(domain);
        switch (axis) {
            case XY: {
                Range range = d2.range(x, y, new double[]{fixedValue});
                Complex v = Complex.ZERO;
                if (range != null) {
                    int zi = zIndex(fixedValue);
                    double delta = dx * dy;
                    for (int i = range.xmin; i <= range.xmax; i++) {
                        for (int j = range.ymin; j < range.ymax; j++) {
                            v = v.plus(values[zi][j][i].mul(delta));
                        }
                    }
                }
                return v;
            }
            case XZ: {
                Range range = d2.range(x, new double[]{fixedValue}, z);
                Complex v = Complex.ZERO;
                if (range != null && getDomain().y().contains(fixedValue)) {
                    int yi = yIndex(fixedValue);
                    double delta = dx * dz;
                    for (int i = range.xmin; i <= range.xmax; i++) {
                        for (int j = range.zmin; j < range.zmax; j++) {
                            v = v.plus(values[j][yi][i].mul(delta));
                        }
                    }
                }
                return v;
            }
            case YZ: {
                Range range = d2.range(new double[]{fixedValue}, y, z);
                Complex v = Complex.ZERO;
                double delta = dy * dz;
                if (range != null) {
                    int xi = xIndex(fixedValue);
                    for (int i = range.ymin; i <= range.ymax; i++) {
                        for (int j = range.zmin; j < range.zmax; j++) {
                            v = v.plus(values[j][i][xi].mul(delta));
                        }
                    }
                }
                return v;
            }
        }
        throw new IllegalArgumentException("Unsupported Plane " + axis);
    }

    public Complex integrate(Domain domain) {
        Domain d2 = getDomain().intersect(domain);
        Range range = d2.range(x, y, z);
        Complex v = Complex.ZERO;
        if (range != null) {
            for (int i = range.xmin; i <= range.xmax; i++) {
                for (int j = range.ymin; j < range.ymax; j++) {
                    for (int k = range.zmin; k < range.zmax; k++) {
                        v = v.plus(values[k][j][i].mul(dx * dy * dz));
                    }
                }
            }
        }
        return v;
    }

    /**
     * differentiate
     *
     * @param axis d/d.axis
     * @return d/d.axis (CCube)
     */
    public CDiscrete diff(Axis axis) {
        switch (getAxisIndex(axis)) {
            case 2: {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        for (int k = 0; k < xcount; k++) {
                            if (i == zcount - 1) {
                                if (i == 0) {
                                    d[i][j][k] = Complex.ZERO;
                                } else {
                                    d[i][j][k] = d[i - 1][j][k];
                                }
                            } else {
                                d[i][j][k] = diff0(values[i + 1][j][k], values[i][j][k], z[i + 1], z[i]);
                            }
                        }
                    }
                }
                return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
            }
            case 1: {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        for (int k = 0; k < xcount; k++) {
                            if (j == ycount - 1) {
                                if (j == 0) {
                                    d[i][j][k] = Complex.ZERO;
                                } else {
                                    d[i][j][k] = d[i][j - 1][k];
                                }
                            } else {
                                d[i][j][k] = diff0(values[i][j + 1][k], values[i][j][k], y[j + 1], y[j]);
                            }
                        }
                    }
                }
                return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
            }
            case 0: {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        for (int k = 0; k < xcount; k++) {
                            if (k == xcount - 1) {
                                if (k == 0) {
                                    d[i][j][k] = Complex.ZERO;
                                } else {
                                    d[i][j][k] = d[i][j][k - 1];
                                }
                            } else {
                                d[i][j][k] = diff0(values[i][j][k + 1], values[i][j][k], x[k + 1], x[k]);
                            }
                        }
                    }
                }
                return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

    public int getAxisIndex(Axis x) {
        for (int i = 0; i < axis.length; i++) {
            Axis axi = axis[i];
            if (axi.equals(x)) {
                return i;
            }
        }
        return -1;
    }

//    public CCube(Complex[][][] model) {
//        this(model, Axis.X, Axis.Y, Axis.Z);
//    }

    private Complex diff0(Complex b, Complex a, double x2, double x1) {
        if (x1 == x2) {
            return Complex.ZERO;
        }
        return a.minus(b).div(x2 - x1);
    }

    public ComplexVector getVector(Axis axis) {
        switch (axis) {
            case X: {
                return getVector(Axis.X, Axis.Y, 0, Axis.Z, 0);
            }
            case Y: {
                return getVector(Axis.Y, Axis.X, 0, Axis.Z, 0);
            }
            case Z: {
                return getVector(Axis.Z, Axis.X, 0, Axis.Y, 0);
            }
        }
        throw new IllegalArgumentException("Invalid Axis " + axis);
    }

    public ComplexVector getVector(Axis axis, Axis fixedAxis1, int index1, Axis fixedAxis2, int index2) {
        switch (axis) {
            case X: {
                Integer y = null;
                Integer z = null;
                if (fixedAxis1 == Axis.Y) {
                    y = index1;
                } else if (fixedAxis1 == Axis.Z) {
                    z = index1;
                }
                if (fixedAxis2 == Axis.Y) {
                    y = index2;
                } else if (fixedAxis2 == Axis.Z) {
                    z = index2;
                }
                if (y == null) {
                    throw new MissingAxisException(Axis.Y);
                }
                if (y.intValue() < 0 || y.intValue() >= getCountY()) {
                    throw new MissingAxisException(Axis.Y);
                }
                if (z == null) {
                    throw new MissingAxisException(Axis.Z);
                }
                if (z.intValue() < 0 || z.intValue() >= getCountZ()) {
                    throw new MissingAxisException(Axis.Z);
                }
                Complex[] r = new Complex[getCountX()];
                for (int x = 0; x < r.length; x++) {
                    r[x] = values[z][y][x];
                }
                return Maths.columnVector(r);
            }
            case Y: {
                Integer x = null;
                Integer z = null;
                if (fixedAxis1 == Axis.X) {
                    x = index1;
                } else if (fixedAxis1 == Axis.Z) {
                    z = index1;
                }
                if (fixedAxis2 == Axis.X) {
                    x = index2;
                } else if (fixedAxis2 == Axis.Z) {
                    z = index2;
                }
                if (x == null) {
                    throw new MissingAxisException(Axis.X);
                }
                if (x.intValue() < 0 || x.intValue() >= getCountX()) {
                    throw new MissingAxisException(Axis.X);
                }
                if (z == null) {
                    throw new MissingAxisException(Axis.Z);
                }
                if (z.intValue() < 0 || z.intValue() >= getCountZ()) {
                    throw new MissingAxisException(Axis.Z);
                }
                Complex[] r = new Complex[getCountY()];
                for (int y = 0; y < r.length; y++) {
                    r[y] = values[z][y][x];
                }
                return Maths.columnVector(r);
            }
            case Z: {
                Integer x = null;
                Integer y = null;
                if (fixedAxis1 == Axis.X) {
                    x = index1;
                } else if (fixedAxis1 == Axis.Y) {
                    y = index1;
                }
                if (fixedAxis2 == Axis.X) {
                    x = index2;
                } else if (fixedAxis2 == Axis.Y) {
                    y = index2;
                }
                if (x == null) {
                    throw new MissingAxisException(Axis.X);
                }
                if (x.intValue() < 0 || x.intValue() >= getCountX()) {
                    throw new MissingAxisException(Axis.X);
                }
                if (y == null) {
                    throw new MissingAxisException(Axis.Y);
                }
                if (y.intValue() < 0 || y.intValue() >= getCountY()) {
                    throw new MissingAxisException(Axis.Y);
                }
                Complex[] r = new Complex[getCountZ()];
                for (int z = 0; z < r.length; z++) {
                    r[z] = values[z][y][x];
                }
                return Maths.columnVector(r);
            }
        }
        throw new IllegalArgumentException("Invalid axis");
    }

    public int getCountY() {
        if (values.length == 0) {
            return 0;
        }
        return values[0].length;
    }

    public int getCountZ() {
        return values.length;
    }

    public int getCountX() {
        if (values.length == 0) {
            return 0;
        }
        if (values[0].length == 0) {
            return 0;
        }
        return values[0][0].length;
    }

    public Complex[][][] getValues() {
        return values;
    }

    public Axis[] getAxis() {
        return axis;
    }

    public int getCount(Axis ax) {
        int i = getAxisIndex(ax);
        switch (i) {
            case 0: {
                return xcount;
            }
            case 1: {
                return ycount;
            }
            case 2: {
                return zcount;
            }
        }
        return 0;
    }

    public ComplexMatrix getMatrix(PlaneAxis plane, int index) {
        return Maths.matrix(getArray(plane, index));
    }

    public Complex[][] getArray(PlaneAxis plane, int index) {
        return getArray(plane.getNormalAxis(), index);
    }

    public Complex[][] getArray(Axis fixedAxis, int index) {
        switch (getAxisIndex(fixedAxis)) {
            case 2: {
                Complex[][] d = new Complex[ycount][xcount];
                for (int j = 0; j < ycount; j++) {
                    System.arraycopy(values[index][j], 0, d[j], 0, xcount);
                }
                return d;
            }
            case 1: {
                Complex[][] d = new Complex[zcount][xcount];
                for (int i = 0; i < zcount; i++) {
                    System.arraycopy(values[i][index], 0, d[i], 0, xcount);
                }
                return d;
            }
            case 0: {
                Complex[][] d = new Complex[zcount][ycount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        d[i][j] = values[i][j][index];
                    }
                }
                return d;
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

    public ComplexMatrix getMatrix(Axis fixedAxis, int index) {
        return Maths.matrix(getArray(fixedAxis, index));
    }

    @Override
    public boolean isInvariant(Axis axis) {
        switch (axis) {
            case X: {
                return false;
            }
            case Y: {
                return domain.dimension() < 2;
            }
            case Z: {
                return domain.dimension() < 3;
            }
        }
        return false;
    }

    public CDiscrete inv() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].inv();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
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
    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }
    //    public CCube changeAxis(Axis axis1, Axis axis2, Axis axis3) {
//        if (axis1.equals(axis2) || axis2.equals(axis3) || axis3.equals(axis1)) {
//            throw new IllegalArgumentException();
//        }
//        Axis[] axisbis = new Axis[]{axis1, axis2, axis3};
//        int[] transf = new int[3];
//        for (int i = 0; i < axis.length; i++) {
//            Axis a = axis[i];
//            for (int j = 0; j < axisbis.length; j++) {
//                if (a.equals(axisbis[j])) {
//                    transf[i] = j;
//                    break;
//                }
//            }
//        }
//        if (transf[0] == 0 && transf[1] == 1 && transf[2] == 2) {
//            return this;
//        }
//        Complex[][][] model2 = null;
//        for (int i = 0; i < model.length; i++) {
//            Complex[][] ml1 = model[i];
//            for (int j = 0; j < ml1.length; j++) {
//                Complex[] ml2 = ml1[j];
//                for (int k = 0; k < ml2.length; k++) {
//                    model2[transf[0]][transf[1]][transf[2]] = model[i][j][k];
//                }
//            }
//        }
//        return new CCube(model2, axis1, axis2, axis3);
//    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    public CDiscrete mul(Complex c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public Expr plus(Expr other) {
        if (other.isNarrow(ExprType.DOUBLE_EXPR)) {
            double dbl = other.toDouble();
            Domain dom = other.getDomain();
            if (dom.equals(getDomain())) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values[i][j][k].div(dbl);
                        }
                    }
                }
                return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other.isNarrow(ExprType.COMPLEX_EXPR)) {
            Complex dbl = other.toComplex();
            Domain dom = other.getDomain();
            if (dom.equals(getDomain())) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values[i][j][k].div(dbl);
                        }
                    }
                }
                return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other instanceof CDiscrete && isSameSampling((CDiscrete) other)) {
            CDiscrete dother = (CDiscrete) other;
            Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
            for (int i = 0; i < values2.length; i++) {
                for (int j = 0; j < values2[i].length; j++) {
                    for (int k = 0; k < values2[i][j].length; k++) {
                        values2[i][j][k] = values[i][j][k].plus(dother.values[i][j][k]);
                    }
                }
            }
            return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
        }
        return ExprDefaults.div(this, other);
    }

//    @Override
//    public boolean isDDx() {
//        return false;
//    }
//
//    @Override
//    public IDDx toDDx() {
//        throw new IllegalArgumentException();
//    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        sb.add("axis", context.elem(axis));
        sb.add("domain", context.elem(domain));
        sb.add("size", context.elem(new int[]{xcount, ycount, zcount}));
        sb.add("step", context.elem(new double[]{dx, dy, dz}));
        sb.add("values", context.elem(values));
        sb.add("hash", context.elem(Integer.toHexString(hashCode()).toUpperCase()));
        return sb.build();
    }

    @Override
    public CDiscrete mul(Domain domain) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(domain.evalDouble(x[k], y[j], z[i]));
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete mul(double c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public Expr mul(Expr other) {
        if (other instanceof Domain) {
            return mul((Domain) other);
        }
        if (other.isNarrow(ExprType.DOUBLE_EXPR)) {
            if (other.getDomain().isUnbounded()) {
                return mul(other.toDouble());
            }
            return mul(other.toDouble()).mul(other.getDomain());
        }
        if (other.isNarrow(ExprType.COMPLEX_EXPR)) {
            if (other.getDomain().isUnbounded()) {
                return mul(other.toComplex());
            }
            return mul(other.toComplex()).mul(other.getDomain());
        }
        if (other instanceof CDiscrete) {
            return mul((CDiscrete) other);
        }
        if (other.isNarrow(ExprType.DOUBLE_DOUBLE)) {
            return mul(other.toDD());
        }
        return ExprDefaults.mul(this, other);
    }

    public CDiscrete mul(CDiscrete other) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(other.values[i][j][k]);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    //@Override
    public CDiscrete mul(DoubleToDouble other) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(other.evalDouble(x[k], y[j], z[i]));
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public Expr div(Expr other) {
        if (other.isNarrow(ExprType.DOUBLE_EXPR)) {
            double dbl = other.toDouble();
            Domain dom = other.getDomain();
            if (dom.isUnbounded()) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values[i][j][k].div(dbl);
                        }
                    }
                }
                return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
            } else {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].div(dbl) : Complex.ZERO;
                        }
                    }
                }
                return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
            }
        } else if (other.isNarrow(ExprType.COMPLEX_EXPR)) {
            Complex dbl = other.toComplex();
            Domain dom = other.getDomain();
            if (dom.isUnbounded()) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values[i][j][k].div(dbl);
                        }
                    }
                }
                return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
            } else {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].div(dbl) : Complex.ZERO;
                        }
                    }
                }
                return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
            }
        }
        return ExprDefaults.div(this, other);
    }

    @Override
    public Expr sub(Expr other) {
        if (other.isNarrow(ExprType.DOUBLE_EXPR)) {
            double dbl = other.toDouble();
            Domain dom = other.getDomain();
            if (dom.equals(getDomain())) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values2[i][j][k].sub(dbl);
                        }
                    }
                }
                return new CDiscrete(values, this.domain, Axis.X, Axis.Y, Axis.Z);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other.isNarrow(ExprType.COMPLEX_EXPR)) {
            Complex dbl = other.toComplex();
            Domain dom = other.getDomain();
            if (dom.equals(getDomain())) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values2[i][j][k].minus(dbl);
                        }
                    }
                }
                return new CDiscrete(values, this.domain, Axis.X, Axis.Y, Axis.Z);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other instanceof CDiscrete && isSameSampling((CDiscrete) other)) {
            CDiscrete dother = (CDiscrete) other;
            Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
            for (int i = 0; i < values2.length; i++) {
                for (int j = 0; j < values2[i].length; j++) {
                    for (int k = 0; k < values2[i][j].length; k++) {
                        values2[i][j][k] = values[i][j][k].minus(dother.values[i][j][k]);
                    }
                }
            }
            return new CDiscrete(values2, this.domain, Axis.X, Axis.Y, Axis.Z);
        }
        return ExprDefaults.div(this, other);
    }

    public CDiscrete neg() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].neg();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete conj() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].conj();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete cos() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].cos();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete sin() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sin();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public double norm() {
        double f = 0;
        for (int j = 0; j < values.length; j++) {
            f = Math.max(f, Maths.matrix(values[j]).norm1());
        }
        return f;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    public CDiscrete sqrt() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sqrt();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete sqr() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sqr();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete log() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].log();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    //@Override
    public CDiscrete abs() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].abs();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete exp() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].exp();
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
        long temp;
        result = 31 * result + (axis != null ? Arrays.hashCode(axis) : 0);
        result = 31 * result + (values != null ? ArrayUtils.hashCode(values) : 0);
        result = 31 * result + (x != null ? Arrays.hashCode(x) : 0);
        result = 31 * result + (y != null ? Arrays.hashCode(y) : 0);
        result = 31 * result + (z != null ? Arrays.hashCode(z) : 0);
        result = 31 * result + zcount;
        result = 31 * result + ycount;
        result = 31 * result + xcount;
        result = 31 * result + Double.hashCode(dx);
        result = 31 * result + Double.hashCode(dy);
        result = 31 * result + Double.hashCode(dz);
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CDiscrete)) return false;

        CDiscrete CDiscrete = (CDiscrete) o;

        if (Double.compare(CDiscrete.dx, dx) != 0) return false;
        if (Double.compare(CDiscrete.dy, dy) != 0) return false;
        if (Double.compare(CDiscrete.dz, dz) != 0) return false;
        if (xcount != CDiscrete.xcount) return false;
        if (ycount != CDiscrete.ycount) return false;
        if (zcount != CDiscrete.zcount) return false;
        if (!Arrays.equals(axis, CDiscrete.axis)) return false;
        if (domain != null ? !domain.equals(CDiscrete.domain) : CDiscrete.domain != null) return false;
        if (!ArrayUtils.equals(values, CDiscrete.values)) return false;
        if (!Arrays.equals(x, CDiscrete.x)) return false;
        if (!Arrays.equals(y, CDiscrete.y)) return false;
        return Arrays.equals(z, CDiscrete.z);
    }

    @Override
    public String toString() {
        return ExprDefaults.toString(this);
    }

    private boolean isSameSampling(CDiscrete other) {
        if (other == null) {
            return false;
        }
        if (!other.getDomain().equals(getDomain())) {
            return false;
        }
        if (other.getCountX() != getCountX()) {
            return false;
        }
        if (other.getCountY() != getCountY()) {
            return false;
        }
        if (other.getCountZ() != getCountZ()) {
            return false;
        }
        if (!Arrays.equals(other.getX(), getX())) {
            return false;
        }
        if (!Arrays.equals(other.getY(), getY())) {
            return false;
        }
        return Arrays.equals(other.getZ(), getZ());
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public double[] getZ() {
        return z;
    }

    @Override
    public DDiscrete getRealDD() {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].getReal();
                }
            }
        }
        return DDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public DDiscrete getImagDD() {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].getImag();
                }
            }
        }
        return DDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public Complex[] evalComplex(double[] x, Domain d0, Out<Range> ranges) {
        switch (domain.dimension()) {
            case 1: {
                Complex[] r = new Complex[x.length];
                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
                if (currRange != null) {
                    int xmin = currRange.xmin;
                    int xmax = currRange.xmax;
                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
                        double xx = xmin + xIndex * dx;
                        if (contains(xx)) {
                            int xi = xIndex(xx);
                            int xi0 = xi < xcount ? xi : (xcount - 1);
                            r[xIndex] = values[0][0][xi0];
                        }

                    }
                }
                ArrayUtils.fillArray1ZeroComplex(r, currRange);
                if (ranges != null) {
                    ranges.set(currRange);
                }
                return r;
            }
        }
        throw new MissingAxisException(Axis.Z);
    }

    @Override
    public Complex[] evalComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.evalComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex[] evalComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.evalComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex[][][] evalComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        Complex[][][] r = new Complex[z.length][y.length][x.length];
        Domain domain1 = d0 == null ? domain : domain.intersect(d0);
        Range currRange = domain1.range(x, y, z);
        if (currRange != null) {
            int xmin = currRange.xmin;
            int xmax = currRange.xmax;
            int ymin = currRange.ymin;
            int ymax = currRange.ymax;
            int zmin = currRange.zmin;
            int zmax = currRange.zmax;
            for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
                double xx = x[xIndex];// xmin + xIndex * dx;
                int xi = xIndex(xx);
                for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
//                    double yy = ymin + yIndex * dy;
                    double yy = y[yIndex];//ymin + yIndex * dy;
                    int yi = yIndex(yy);
                    for (int zIndex = zmin; zIndex <= zmax; zIndex++) {
                        double zz = z[zIndex];//zmin + zIndex * dz;
                        int zi = zIndex(zz);
                        r[zIndex][yIndex][xIndex] = values[zi][yi][xi];
//                        if (contains(xx, yy, zz)) {
//                            int xi = (int) Math.floor((xx - domain.xmin) / dx);
//                            int yi = (int) Math.floor((yy - domain.ymin) / dy);
//                            int zi = (int) Math.floor((zz - domain.ymin) / dz);
//                            int zi0 = zi < zcount ? zi : (zcount - 1);
//                            int yi0 = yi < ycount ? yi : (ycount - 1);
//                            int xi0 = xi < xcount ? xi : (xcount - 1);
//                        }else{
//                            boolean d = contains(xx, yy, zz);
//                            boolean d1 = domain1.contains(xx, yy, zz);
//                            throw new IllegalArgumentException("??");
//                        }
                    }
                }
            }
            ArrayUtils.fillArray3ZeroComplex(r, currRange);
            if (ranges != null) {
                ranges.set(currRange);
            }
        } else {
            if (ranges != null) {
                ranges.set(null);
            }
            return ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
        }
        return r;
    }

    @Override
    public Complex[][] evalComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        switch (domain.dimension()) {
            case 1: {
                Complex[][] r = new Complex[y.length][x.length];
                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
                if (currRange != null) {
                    int xmin = currRange.xmin;
                    int xmax = currRange.xmax;
                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
                        double xx = xmin + xIndex * dx;
                        if (contains(xx)) {
                            int xi = xIndex(xx);
                            int xi0 = xi < xcount ? xi : (xcount - 1);
                            for (int yIndex = 0; yIndex < y.length; yIndex++) {
                                r[yIndex][xIndex] = values[0][0][xi0];
                            }
                        }

                    }
                }
                ArrayUtils.fillArray2ZeroComplex(r, currRange);
                if (ranges != null) {
                    ranges.set(currRange);
                }
                return r;
            }
            case 2: {
                Complex[][] r = new Complex[y.length][x.length];
                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
                if (currRange != null) {
                    int xmin = currRange.xmin;
                    int xmax = currRange.xmax;
                    int ymin = currRange.ymin;
                    int ymax = currRange.ymax;
                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
                        double xx = x[xIndex];
                        for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
                            double yy = y[yIndex];
                            if (contains(xx, yy)) {
                                int xi = xIndex(xx);
                                int yi = yIndex(yy);
                                int yi0 = yi < ycount ? yi : (ycount - 1);
                                int xi0 = xi < xcount ? xi : (xcount - 1);
                                r[yIndex][xIndex] = values[0][yi0][xi0];
                            }
                        }
                    }
                    ArrayUtils.fillArray2ZeroComplex(r, currRange);
                    if (ranges != null) {
                        ranges.set(currRange);
                    }
                } else {
                    if (ranges != null) {
                        ranges.set(currRange);
                    }
                    return ArrayUtils.fillArray2Complex(x.length, y.length, Complex.ZERO);
                }
                return r;
            }
        }
        throw new MissingAxisException(Axis.Z);
    }

    @Override
    public Complex evalComplex(double x, BooleanMarker defined) {
        switch (domain.dimension()) {
            case 1: {
                if (contains(x)) {
                    int xi = xIndex(x);
                    defined.set();
                    return values[0][0][xi];
                }
                return Complex.ZERO;
            }
        }
        throw new MissingAxisException(Axis.Y);
    }

    @Override
    public Complex evalComplex(double x, double y, BooleanMarker defined) {
        switch (domain.dimension()) {
            case 1: {
                if (contains(x)) {
                    int xi = xIndex(x);
                    defined.set();
                    return values[0][0][xi];
                }
                return Complex.ZERO;
            }
            case 2: {
                if (contains(x, y)) {
                    int xi = xIndex(x);
                    int yi = yIndex(y);
                    if (xi < 0 || yi < 0) {
                        xi = xIndex(x);
                        yi = yIndex(y);
                    }
                    defined.set();
                    return values[0][yi][xi];
                }
                return Complex.ZERO;
            }
        }
        throw new MissingAxisException(Axis.Z);
    }

    public Complex evalComplex(double x, double y, double z, BooleanMarker defined) {
        switch (domain.dimension()) {
            case 1: {
                if (contains(x)) {
                    int xi = xIndex(x);
                    defined.set();
                    return values[0][0][xi];
                }
                break;
            }
            case 2: {
                if (contains(x, y)) {
                    int xi = xIndex(x);
                    int yi = yIndex(y);
                    defined.set();
                    return values[0][yi][xi];
                }
                break;
            }
        }
        if (contains(x, y)) {
            int xi = xIndex(x);
            int yi = yIndex(y);
            int zi = zIndex(z);
            defined.set();
            return values[zi][yi][xi];
        }
        return Complex.ZERO;
    }

//    @Override
//    public Complex computeComplex(double x, BooleanMarker defined) {
//        Out<Range> ranges = new Out<>();
//        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
//        defined.set(ranges.get().getDefined1().get(0));
//        return complex;
//    }

    @Override
    public DoubleToComplex toDC() {
        return this;
    }

    @Override
    public DoubleToDouble toDD() {
        throw new IllegalArgumentException();
    }

    @Override
    public DoubleToVector toDV() {
        return new VDiscrete(this);
    }

//    @Override
//    public Expr mul(Expr other) {
//        if (other.isDouble()) {
//            double dbl = other.toDouble();
//            Domain dom = other.getDomain();
//            if (dom.isUnconstrained()) {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = values[i][j][k].mul(dbl);
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].mul(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            }
//        } else if (other.isComplex()) {
//            Complex dbl = other.toComplex();
//            Domain dom = other.getDomain();
//            if (dom.isUnconstrained()) {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = values[i][j][k].mul(dbl);
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].mul(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new CDiscrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            }
//        }
//        return super.mul(other);
//    }

    @Override
    public DoubleToMatrix toDM() {
        return toDV().toDM();
    }

    @Override
    public ComponentDimension getComponentDimension() {
        return ComponentDimension.SCALAR;
    }

    //@Override
    public DoubleToDouble absdbl() {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].absdbl();
                }
            }
        }
        return DDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public DoubleToDouble absdblsqr() {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].absdblsqr();
                }
            }
        }
        return DDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public CDiscrete abssqr() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = Complex.of(this.values[i][j][k].absdblsqr());
                }
            }
        }
        return CDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public double getDz() {
        return dz;
    }

    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof CDiscrete) {
            CDiscrete o = (CDiscrete) other;
            return (this.sub(o)).norm() / o.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
    }

    public CDiscrete sub(DoubleToComplex other) {
        if (other instanceof CDiscrete) {
            CDiscrete c = (CDiscrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k].minus(c.values[i][j][k]);
                        }
                    }
                }
                return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
            }
            Domain d2 = getDomain().expand(other.getDomain());
            double dx = Math.min(this.dx, c.dx);
            double dy = Math.min(this.dy, c.dy);
            double dz = Math.min(this.dz, c.dz);
            AbsoluteSamples xyz = d2.steps(dx, dy, dz);
            Complex[][][] a1 = evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] a2 = c.evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] d = ArrayUtils.sub(a1, a2);
            return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        Complex[][][] a1 = evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] a2 = other.evalComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] d = ArrayUtils.sub(a1, a2);
        return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public Complex sum() {
        MutableComplex c = MutableComplex.Zero();
        int len1 = values.length;
        for (int i = 0; i < len1; i++) {
            Complex[][] v2 = values[i];
            int len2 = v2.length;
            for (int j = 0; j < len2; j++) {
                Complex[] v3 = v2[j];
                int len3 = v3.length;
                for (int k = 0; k < len3; k++) {
                    c.add(v3[k]);
                }
            }
        }
        return c.toComplex();
    }

    public Complex avg() {
        MutableComplex c = MutableComplex.Zero();
        int len1 = values.length;
        int count = 0;
        for (int i = 0; i < len1; i++) {
            Complex[][] v2 = values[i];
            int len2 = v2.length;
            for (int j = 0; j < len2; j++) {
                Complex[] v3 = v2[j];
                int len3 = v3.length;
                count += len3;
                for (int k = 0; k < len3; k++) {
                    c.add(v3[k]);
                }
            }
        }
        return c.toComplex().div(count);
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
