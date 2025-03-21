package net.thevpc.scholar.hadrumaths.symbolic.double2double;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.impl.AbstractObjectFormat;
import net.thevpc.scholar.hadrumaths.geom.IntPoint;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.Range;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: taha Date: 2 juil. 2003 Time: 11:51:13
 */

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 mai 2007 22:18:44
 */
public class DDiscrete extends AbstractDoubleToDouble {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(DDiscrete.class, new AbstractObjectFormat<DDiscrete>() {
            @Override
            public void format(DDiscrete o, ObjectFormatContext context) {
                context.append("DDiscrete(");
                context.append(Maths.dump(o.values));
                context.append(")");
            }
        });
    }

    private Axis[] axis;
    private double[/*Z*/][/*Y*/][/*X*/] values;
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

    public DDiscrete(Domain domain, double[][][] values, Axis axis1, Axis axis2, Axis axis3) {
        init(domain, values, axis1, axis2, axis3);
    }

    private void init(Domain domain, double[][][] model, Axis axis1, Axis axis2, Axis axis3) {
        this.domain = domain;
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

    public static DDiscrete cst(Domain domain, double value, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        AbsoluteSamples steps = domain.steps(dx, dy, dz);
        double[][][] model = ArrayUtils.fillArray3Double(steps.getX().length, steps.getY().length, steps.getZ().length, value);
        return new DDiscrete(domain, model, axis1, axis2, axis3);
    }

    public static DDiscrete of(Expr expr, Domain domain, int xSamples, int ySamples, int zSamples) {
        return of(expr, domain, xSamples, ySamples, zSamples, 3);
    }

    public static DDiscrete of(Expr expr, Domain domain, int xSamples, int ySamples, int zSamples, int dim) {
        if (expr == null) {
            throw new NullPointerException("Null Expression");
        }
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (domain == null) {
            throw new NullPointerException("Null Domain");
        }
        if (expr.is(ExprDim.SCALAR) && expr.is(ExprType.DOUBLE_DOUBLE)) {
            switch (dim) {
                case 1: {
                    AbsoluteSamples samples = domain.times(xSamples);
                    double[] model = expr.toDD().evalDouble(samples.getX(), (Domain) null, null);
                    return DDiscrete.of(domain, model);
                }
                case 2: {
                    AbsoluteSamples samples = domain.times(xSamples, ySamples);
                    double[][] model = expr.toDD().evalDouble(samples.getX(), samples.getY(), null, null);
                    return DDiscrete.of(domain, model);
                }
                case 3: {
                    AbsoluteSamples samples = domain.times(xSamples, ySamples, zSamples);
                    double[][][] model = expr.toDD().evalDouble(samples.getX(), samples.getY(), samples.getZ(), null, null);
                    return DDiscrete.of(domain, model);
                }
            }
            throw new IllegalArgumentException("Unsupported dimension " + dim);
        } else {
            throw new IllegalArgumentException("Expression is either not double or not scalar");
        }
    }

    public static DDiscrete of(Domain domain, double[] model) {
        return new DDiscrete(domain, new double[][][]{{model}}, Axis.X, Axis.Y, Axis.Z);
    }

    public static DDiscrete of(Domain domain, double[][] model) {
        return new DDiscrete(domain, new double[][][]{model}, Axis.X, Axis.Y, Axis.Z);
    }

    public static DDiscrete of(Domain domain, double[][][] model) {
        return new DDiscrete(domain, model, Axis.X, Axis.Y, Axis.Z);
    }

    public static DDiscrete of(Expr expr, Domain domain, int xSamples, int ySamples) {
        return of(expr, domain, xSamples, ySamples, 1, 2);
    }

    public static DDiscrete of(Expr expr, Domain domain, int xSamples) {
        return of(expr, domain, xSamples, 1, 1, 1);
    }

    public static DDiscrete of(Expr expr, int xSamples, int ySamples, int zSamples) {
        return of(expr, null, xSamples, ySamples, zSamples, 3);
    }

    public static DDiscrete of(Expr expr, int xSamples, int ySamples) {
        return of(expr, null, xSamples, ySamples, 1, 2);
    }

    public static DDiscrete of(Expr expr, int xSamples) {
        return of(expr, null, xSamples, 1, 1, 1);
    }

    public static DDiscrete of(Expr expr, Domain domain, Samples samples) {
        if (expr == null) {
            throw new NullPointerException("Null Expression");
        }
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (domain == null) {
            throw new NullPointerException("Null Domain");
        }
        if (samples == null) {
            throw new NullPointerException("Null Samples");
        }
        int dim = samples.getDimension();
        AbsoluteSamples absoluteSamples = Samples.toAbsoluteSamples(samples, domain);
        if (expr.is(ExprDim.SCALAR) && expr.is(ExprType.DOUBLE_DOUBLE)) {
            switch (dim) {
                case 1: {
//                    if(expr.isDouble() && domain.isInfinite()){
//                        double[] vv = new double[xSamples];
//                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples],domain.xmin()) : domain.xtimes(xSamples);
//                        return DDiscrete.create(
//                                domain,
//                                vv,
//                                xx);
//                    }
                    double[] model = expr.toDD().evalDouble(absoluteSamples.getX(), (Domain) null, null);
                    return DDiscrete.of(domain, model);
                }
                case 2: {
//                    if(expr.isDouble() && domain.isInfinite()){
//                        double[][] vv = new double[ySamples][xSamples];
//                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples],domain.xmin()) : domain.xtimes(xSamples);
//                        double[] yy = domain.isInfiniteY() ? ArrayUtils.fill(new double[ySamples],domain.ymin()) : domain.xtimes(ySamples);
//                        return DDiscrete.create(
//                                domain,
//                                vv,
//                                xx,
//                                yy);
//                    }
                    double[][] model = expr.toDD().evalDouble(absoluteSamples.getX(), absoluteSamples.getY(), null, null);
                    return DDiscrete.of(domain, model);
                }
                case 3: {
//                    if(expr.isDouble() && domain.isInfinite()){
//                        double[][][] vv = new double[zSamples][ySamples][xSamples];
//                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples],domain.xmin()) : domain.xtimes(xSamples);
//                        double[] yy = domain.isInfiniteY() ? ArrayUtils.fill(new double[ySamples],domain.ymin()) : domain.ytimes(ySamples);
//                        double[] zz = domain.isInfiniteZ() ? ArrayUtils.fill(new double[zSamples],domain.zmin()) : domain.ztimes(zSamples);
//                        return DDiscrete.create(
//                                domain,
//                                vv,
//                                xx,
//                                yy,
//                                zz);
//                    }
                    double[][][] model = expr.toDD().evalDouble(absoluteSamples.getX(), absoluteSamples.getY(), absoluteSamples.getZ(), null, null);
                    return DDiscrete.of(domain, model);
                }
            }
            throw new IllegalArgumentException("Unsupported dimension " + dim);
        } else {
            throw new IllegalArgumentException("Expression si either not double or not scalar");
        }
    }

    public DDiscrete mul(DDiscrete other) {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k] * (other.values[i][j][k]);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public static DDiscrete of(Domain domain, double[][][] model, Axis axis1, Axis axis2, Axis axis3) {
        return new DDiscrete(domain, model, axis1, axis2, axis3);
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

    public DDiscrete add(DoubleToDouble other) {
        if (other instanceof DDiscrete) {
            DDiscrete c = (DDiscrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k] + (c.values[i][j][k]);
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
            double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] a2 = c.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] d = ArrayUtils.add(a1, a2);
            return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] a2 = other.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] d = ArrayUtils.add(a1, a2);
        return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public List<Expr> getChildren() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Domain getDomain() {
        return domain;
    }

    public DDiscrete sub(DoubleToDouble other) {
        if (other instanceof DDiscrete) {
            DDiscrete c = (DDiscrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k] - (c.values[i][j][k]);
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
            double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] a2 = c.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] d = ArrayUtils.sub(a1, a2);
            return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] a2 = other.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] d = ArrayUtils.sub(a1, a2);
        return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public DDiscrete mul(DoubleToDouble other) {
        if (other instanceof DDiscrete) {
            DDiscrete c = (DDiscrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k] + (c.values[i][j][k]);
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
            double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] a2 = c.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] d = ArrayUtils.mul(a1, a2);
            return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] a2 = other.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] d = ArrayUtils.mul(a1, a2);
        return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public DDiscrete div(DoubleToDouble other) {
        if (other instanceof DDiscrete) {
            DDiscrete c = (DDiscrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k] + (c.values[i][j][k]);
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
            double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] a2 = c.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            double[][][] d = ArrayUtils.div(a1, a2);
            return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        double[][][] a1 = evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] a2 = other.evalDouble(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        double[][][] d = ArrayUtils.div(a1, a2);
        return of(d2, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public DDiscrete relativeError(DDiscrete other) {
        if (getDomain().equals(other.getDomain()) || dx != other.dx || dy != other.dy || dz != other.dz) {
            throw new IllegalArgumentException("Cubes don(t match");
        }
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    double a = this.values[i][j][k];
                    double b = other.values[i][j][k];
                    double c;
                    if (a == b || (Double.isNaN(a) && Double.isNaN(b)) || (Double.isInfinite(a) && Double.isInfinite(b))) {
                        c = 0;
//                    } else if (b.isNaN() || b.isInfinite() || b.equals(Math2.CZERO)) {
//                        c[i][j] = (a.substract(b));
                    } else {
                        c = Math.abs(((a - b) * 100 / (b)));
                    }

                    d[i][j][k] = c;
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public double integrate(Domain domain, PlaneAxis axis, double fixedValue) {
        Domain domain0 = getDomain();
        Domain d2 = domain0.intersect(domain);
        switch (axis) {
            case XY: {
                Range range = d2.range(x, y, new double[]{fixedValue});
                double v = 0;
                if (range != null) {
                    int zi = zIndex(fixedValue);
                    double delta = dx * dy;
                    for (int i = range.xmin; i <= range.xmax; i++) {
                        for (int j = range.ymin; j < range.ymax; j++) {
                            v += values[zi][j][i] * (delta);
                        }
                    }
                }
                return v;
            }
            case XZ: {
                Range range = d2.range(x, new double[]{fixedValue}, z);
                double v = 0;
                if (range != null && getDomain().y().contains(fixedValue)) {
                    int yi = yIndex(fixedValue);
                    double delta = dx * dz;
                    for (int i = range.xmin; i <= range.xmax; i++) {
                        for (int j = range.zmin; j < range.zmax; j++) {
                            v += (values[j][yi][i] * (delta));
                        }
                    }
                }
                return v;
            }
            case YZ: {
                Range range = d2.range(new double[]{fixedValue}, y, z);
                double v = 0;
                double delta = dy * dz;
                if (range != null) {
                    int xi = xIndex(fixedValue);
                    for (int i = range.ymin; i <= range.ymax; i++) {
                        for (int j = range.zmin; j < range.zmax; j++) {
                            v += (values[j][i][xi] * (delta));
                        }
                    }
                }
                return v;
            }
        }
        throw new IllegalArgumentException("Unsupported Plane " + axis);
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

    public double integrate(Domain domain) {
        Domain d2 = getDomain().intersect(domain);
        Range range = d2.range(x, y, z);
        double v = 0;
        if (range != null) {
            for (int i = range.xmin; i <= range.xmax; i++) {
                for (int j = range.ymin; j < range.ymax; j++) {
                    for (int k = range.zmin; k < range.zmax; k++) {
                        v += (values[k][j][i] * (dx * dy * dz));
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
    public DDiscrete diff(Axis axis) {
        switch (getAxisIndex(axis)) {
            case 2: {
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        for (int k = 0; k < xcount; k++) {
                            if (i == zcount - 1) {
                                if (i == 0) {
                                    d[i][j][k] = 0;
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
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        for (int k = 0; k < xcount; k++) {
                            if (j == ycount - 1) {
                                if (j == 0) {
                                    d[i][j][k] = 0;
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
                double[][][] d = new double[zcount][ycount][xcount];
                for (int i = 0; i < zcount; i++) {
                    for (int j = 0; j < ycount; j++) {
                        for (int k = 0; k < xcount; k++) {
                            if (k == xcount - 1) {
                                if (k == 0) {
                                    d[i][j][k] = 0;
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

//    public CCube(Complex[][][] model) {
//        this(model, Axis.X, Axis.Y, Axis.Z);
//    }

    public int getAxisIndex(Axis x) {
        for (int i = 0; i < axis.length; i++) {
            Axis axi = axis[i];
            if (axi.equals(x)) {
                return i;
            }
        }
        return -1;
    }

    private double diff0(double b, double a, double x2, double x1) {
        if (x1 == x2) {
            return 0;
        }
        return (a - b) / (x2 - x1);
    }

    public double[] getVector(Axis axis, Axis fixedAxis1, int index1, Axis fixedAxis2, int index2) {
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
                    throw new MissingAxisException(Axis.Z);
                }
                if (z == null) {
                    throw new MissingAxisException(Axis.Z);
                }
                if (z.intValue() < 0 || z.intValue() >= getCountZ()) {
                    throw new MissingAxisException(Axis.Z);
                }
                double[] r = new double[getCountX()];
                for (int x = 0; x < r.length; x++) {
                    r[x] = values[z][y][x];
                }
                return r;
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
                double[] r = new double[getCountX()];
                for (int y = 0; y < r.length; y++) {
                    r[y] = values[z][y][x];
                }
                return r;
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
                    throw new MissingAxisException(Axis.X);
                }
                double[] r = new double[getCountZ()];
                for (int z = 0; z < r.length; z++) {
                    r[z] = values[z][y][x];
                }
                return r;
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

    public double[][][] getValues() {
        return values;
    }

    public CDiscrete toDDiscrete() {
        return CDiscrete.of(domain, ArrayUtils.toComplex(values), axis[0], axis[1], axis[2]);
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

    public double[][] getMatrix(PlaneAxis plane, int index) {
        return getMatrix(plane.getNormalAxis(), index);
    }

    public double[][] getMatrix(Axis fixedAxis, int index) {
        switch (getAxisIndex(fixedAxis)) {
            case 2: {
                double[][] d = new double[ycount][xcount];
                for (int j = 0; j < ycount; j++) {
                    System.arraycopy(values[index][j], 0, d[j], 0, xcount);
                }
                return d;
            }
            case 1: {
                double[][] d = new double[zcount][xcount];
                for (int i = 0; i < zcount; i++) {
                    System.arraycopy(values[i][index], 0, d[i], 0, xcount);
                }
                return d;
            }
            case 0: {
                double[][] d = new double[zcount][ycount];
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

    public double norm() {
        double f = 0;
        for (int j = 0; j < values.length; j++) {
            f = Math.max(f, Maths.matrix(values[j]).norm1());
        }
        return f;
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
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfinite() {
        return false;
    }

    @Override
    public Expr mul(Complex other) {
        if (other.isReal()) {
            return mul(other.getReal());
        }
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = other.mul(this.values[i][j][k]);
                }
            }
        }
        return new CDiscrete(d, domain, this.axis[0], this.axis[1], this.axis[2]);
    }

    public DDiscrete plus(double c) {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k] + (c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public boolean isSmartMulDouble() {
        return true;
    }

    @Override
    public boolean isSmartMulComplex() {
        return true;
    }

//    public Complex computeComplex(double x, double y, double z) {
//        return computeComplex(x,y,z,new BooleanMarker());
//    }

//    public Complex computeComplex(double x, double y, double z, BooleanMarker defined) {
//        switch (dimension) {
//            case 1: {
//                if (contains(x)) {
//                    int xi = (int) ((x - domain.xmin()) / dx);
//                    defined.set();
//                    return Complex.valueOf(values[0][0][xi]);
//                }
//                break;
//            }
//            case 2: {
//                if (contains(x, y)) {
//                    int xi = (int) ((x - domain.xmin()) / dx);
//                    int yi = (int) ((y - domain.ymin()) / dy);
//                    defined.set();
//                    return Complex.valueOf(values[0][yi][xi]);
//                }
//                break;
//            }
//        }
//        if (contains(x, y)) {
//            int xi = (int) ((x - domain.xmin()) / dx);
//            int yi = (int) ((y - domain.ymin()) / dy);
//            int zi = (int) ((z - domain.zmin()) / dz);
//            defined.set();
//            return Complex.valueOf(values[zi][yi][xi]);
//        }
//        return Complex.ZERO;
//    }

    @Override
    public boolean isSmartMulDomain() {
        return true;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.ofObj(getClass().getSimpleName());
        sb.add("axis", context.elem(axis));
        sb.add("domain", context.elem(domain));
        sb.add("size", context.elem(new int[]{xcount, ycount, zcount}));
        sb.add("step", context.elem(new double[]{dx, dy, dz}));
        sb.add("values", context.elem(values));
        sb.add("hash", context.elem(Integer.toHexString(hashCode()).toUpperCase()));
        return sb.build();
    }

//    @Override
//    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        switch (dimension) {
//            case 1: {
//                Complex[][] r = new Complex[y.length][x.length];
//                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
//                if (currRange != null) {
//                    int xmin = currRange.xmin;
//                    int xmax = currRange.xmax;
//                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                        double xx = xmin + xIndex * dx;
//                        if (contains(xx)) {
//                            int xi = (int) ((xx - domain.xmin()) / dx);
//                            int xi0 = xi < xcount ? xi : (xcount - 1);
//                            for (int yIndex = 0; yIndex < y.length; yIndex++) {
//                                r[yIndex][xIndex] = Complex.valueOf(values[0][0][xi0]);
//                            }
//                        }
//
//                    }
//                }
//                ArrayUtils.fillArray2ZeroComplex(r, currRange);
//                if (ranges != null) {
//                    ranges.set(currRange);
//                }
//                return r;
//            }
//            case 2: {
//                Complex[][] r = new Complex[y.length][x.length];
//                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
//                if (currRange != null) {
//                    int xmin = currRange.xmin;
//                    int xmax = currRange.xmax;
//                    int ymin = currRange.ymin;
//                    int ymax = currRange.ymax;
//                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                        double xx = xmin + xIndex * dx;
//                        for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
//                            double yy = ymin + yIndex * dy;
//                            if (contains(xx, yy)) {
//                                int xi = (int) ((xx - domain.xmin()) / dx);
//                                int yi = (int) ((yy - domain.ymin()) / dy);
//                                int yi0 = yi < ycount ? yi : (ycount - 1);
//                                int xi0 = xi < xcount ? xi : (xcount - 1);
//                                r[yIndex][xIndex] = Complex.valueOf(values[0][yi0][xi0]);
//                            }
//                        }
//                    }
//                }
//                ArrayUtils.fillArray2ZeroComplex(r, currRange);
//                if (ranges != null) {
//                    ranges.set(currRange);
//                }
//                return r;
//            }
//        }
//        throw new MissingAxisException(Axis.Z);
//    }

//    @Override
//    public double[][] computeDouble(double[] x, double[] y, Domain d0, Out<Range> ranges) {
//        switch (dimension) {
//            case 1: {
//                double[][] r = new double[y.length][x.length];
//                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
//                if (currRange != null) {
//                    int xmin = currRange.xmin;
//                    int xmax = currRange.xmax;
//                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                        double xx = xmin + xIndex * dx;
//                        if (contains(xx)) {
//                            int xi = (int) ((xx - domain.xmin()) / dx);
//                            int xi0 = xi < xcount ? xi : (xcount - 1);
//                            for (int yIndex = 0; yIndex < y.length; yIndex++) {
//                                r[yIndex][xIndex] = values[0][0][xi0];
//                            }
//                        }
//
//                    }
//                }
////                ArrayUtils.fillArray2ZeroComplex(r, currRange);
//                if (ranges != null) {
//                    ranges.set(currRange);
//                }
//                return r;
//            }
//            case 2: {
//                double[][] r = new double[y.length][x.length];
//                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x, y);
//                if (currRange != null) {
//                    int xmin = currRange.xmin;
//                    int xmax = currRange.xmax;
//                    int ymin = currRange.ymin;
//                    int ymax = currRange.ymax;
//                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                        double xx = xmin + xIndex * dx;
//                        for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
//                            double yy = ymin + yIndex * dy;
//                            if (contains(xx, yy)) {
//                                int xi = (int) ((xx - domain.xmin()) / dx);
//                                int yi = (int) ((yy - domain.ymin()) / dy);
//                                int yi0 = yi < ycount ? yi : (ycount - 1);
//                                int xi0 = xi < xcount ? xi : (xcount - 1);
//                                r[yIndex][xIndex] = values[0][yi0][xi0];
//                            }
//                        }
//                    }
//                }
////                ArrayUtils.fillArray2ZeroComplex(r, currRange);
//                if (ranges != null) {
//                    ranges.set(currRange);
//                }
//                return r;
//            }
//        }
//        throw new MissingAxisException(Axis.Z);
//    }
//
//    @Override
//    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        Complex[][][] r = new Complex[z.length][y.length][x.length];
//        Domain domain1 = d0 == null ? domain : domain.intersect(d0);
//        Range currRange = domain1.range(x, y, z);
//        if (currRange != null) {
//            int xmin = currRange.xmin;
//            int xmax = currRange.xmax;
//            int ymin = currRange.ymin;
//            int ymax = currRange.ymax;
//            int zmin = currRange.zmin;
//            int zmax = currRange.zmax;
//            for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                double xx = x[xIndex];// xmin + xIndex * dx;
//                int xi = (int) ((xx - domain.xmin()) / dx);
//                for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
////                    double yy = ymin + yIndex * dy;
//                    double yy = y[yIndex];//ymin + yIndex * dy;
//                    int yi = (int) ((yy - domain.ymin()) / dy);
//                    for (int zIndex = zmin; zIndex <= zmax; zIndex++) {
//                        double zz = z[zIndex];//zmin + zIndex * dz;
//                        int zi = (int) ((zz - domain.zmin()) / dz);
//                        r[zIndex][yIndex][xIndex] = Complex.valueOf(values[zi][yi][xi]);
//                    }
//                }
//            }
//            ArrayUtils.fillArray3ZeroComplex(r, currRange);
//            if (ranges != null) {
//                ranges.set(currRange);
//            }
//        } else {
//            Complex[][][] c = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//        return r;
//    }
//
//    @Override
//    public double[][][] computeDouble(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
//        double[][][] r = new double[z.length][y.length][x.length];
//        Domain domain1 = d0 == null ? domain : domain.intersect(d0);
//        Range currRange = domain1.range(x, y, z);
//        if (currRange != null) {
//            int xmin = currRange.xmin;
//            int xmax = currRange.xmax;
//            int ymin = currRange.ymin;
//            int ymax = currRange.ymax;
//            int zmin = currRange.zmin;
//            int zmax = currRange.zmax;
//            for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                double xx = x[xIndex];// xmin + xIndex * dx;
//                for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
////                    double yy = ymin + yIndex * dy;
//                    double yy = y[yIndex];//ymin + yIndex * dy;
//                    for (int zIndex = zmin; zIndex <= zmax; zIndex++) {
//                        double zz = z[zIndex];//zmin + zIndex * dz;
//                        IntPoint indices = getIndices(xx, yy, zz);
//                        if (indices != null) {
//                            r[zIndex][yIndex][xIndex] = values[indices.z][indices.y][indices.x];
//                        }
//                    }
//                }
//            }
////            ArrayUtils.fillArray3ZeroComplex(r, currRange);
//            if (ranges != null) {
//                ranges.set(currRange);
//            }
//        } else {
//            double[][][] c = ArrayUtils.fillArray3Double(x.length, y.length, z.length, 0);
//            if (ranges != null) {
//                ranges.set(null);
//            }
//            return c;
//        }
//        return r;
//    }


//    @Override
//    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeComplex(this, x, y, d0, ranges);
//    }
//
//    @Override
//    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
//        return Expressions.computeComplex(this, x, y, d0, ranges);
//    }
//
//    @Override
//    public Complex computeComplex(double x, double y, BooleanMarker defined) {
//        switch (dimension) {
//            case 1: {
//                if (contains(x)) {
//                    IntPoint ii = getIndices(x, 0, 0);
//                    if (ii != null) {
//                        return Complex.valueOf(values[0][0][ii.x]);
//                    }
//                    defined.set();
//                }
//                return Complex.ZERO;
//            }
//            case 2: {
//                if (contains(x, y)) {
//                    IntPoint ii = getIndices(x, y, 0);
//                    if (ii != null) {
//                        return Complex.valueOf(values[0][ii.y][ii.x]);
//                    }
//                    defined.set();
//                }
//                return Complex.ZERO;
//            }
//        }
//        throw new MissingAxisException(Axis.Z);
//    }

//    @Override
//    public Complex computeComplex(double x, BooleanMarker defined) {
//        switch (dimension) {
//            case 1: {
//                if (contains(x)) {
//                    IntPoint ii = getIndices(x, 0, 0);
//                    if (ii != null) {
//                        return Complex.valueOf(values[0][0][ii.x]);
//                    }
//                    defined.set();
//                }
//                return Complex.ZERO;
//            }
//        }
//        throw new MissingAxisException(Axis.Y);
//    }

    @Override
    public Expr mul(Domain domain) {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k] * domain.evalDouble(x[k], y[j], z[i]);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

//    @Override
//    public DoubleToDouble getRealDD() {
//        return this;
//    }
//
//    @Override
//    public DoubleToDouble getImagDD() {
//        return FunctionFactory.DZERO(getDomainDimension());
//    }

    public DDiscrete mul(double c) {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k] * (c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    public DDiscrete sub(double c) {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k] - (c);
                }
            }
        }
        return of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public Expr newInstance(Expr... subExpressions) {
        return this;
    }

    @Override
    public int hashCode() {
        int result = getClass().getName().hashCode();
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
        if (!(o instanceof DDiscrete)) return false;

        DDiscrete discrete = (DDiscrete) o;

        if (Double.compare(discrete.dx, dx) != 0) return false;
        if (Double.compare(discrete.dy, dy) != 0) return false;
        if (Double.compare(discrete.dz, dz) != 0) return false;
        if (xcount != discrete.xcount) return false;
        if (ycount != discrete.ycount) return false;
        if (zcount != discrete.zcount) return false;
        if (!Arrays.equals(axis, discrete.axis)) return false;
        if (domain != null ? !domain.equals(discrete.domain) : discrete.domain != null) return false;
        if (!ArrayUtils.equals(values, discrete.values)) return false;
        if (!Arrays.equals(x, discrete.x)) return false;
        if (!Arrays.equals(y, discrete.y)) return false;
        return Arrays.equals(z, discrete.z);
    }

    public double evalDouble(double x, double y, double z, BooleanMarker defined) {
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
                    defined.set();
                    int xi = xIndex(x);
                    int yi = yIndex(y);
                    return values[0][yi][xi];
                }
                break;
            }
        }
        if (contains(x, y)) {
            defined.set();
            int xi = xIndex(x);
            int yi = yIndex(y);
            int zi = zIndex(z);
            return values[zi][yi][xi];
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, BooleanMarker defined) {
        if (contains(x)) {
            defined.set();
            int xi = xIndex(x);
            return values[0][0][xi];
        }
        return 0;
    }

    @Override
    public double evalDouble(double x, double y, BooleanMarker defined) {
        IntPoint ii = getIndices(x, y, domain.zmin());
        if (ii != null) {
            defined.set();
            double v = values[0][ii.y][ii.x];
//            System.out.println(v + " <== " + x + "  " + y);
            return v;
        }
//        System.out.println("(0) <== " + x + "  " + y);
        return 0;
    }

//    @Override
//    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
//        switch (dimension) {
//            case 1: {
//                Complex[] r = new Complex[x.length];
//                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
//                if (currRange != null) {
//                    int xmin = currRange.xmin;
//                    int xmax = currRange.xmax;
//                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
//                        double xx = xmin + xIndex * dx;
//                        if (contains(xx)) {
//                            int xi = (int) ((xx - domain.xmin()) / dx);
//                            int xi0 = xi < xcount ? xi : (xcount - 1);
//                            r[xIndex] = Complex.valueOf(values[0][0][xi0]);
//                        }
//
//                    }
//                }
//                ArrayUtils.fillArray1ZeroComplex(r, currRange);
//                if (ranges != null) {
//                    ranges.set(currRange);
//                }
//                return r;
//            }
//        }
//        throw new MissingAxisException(Axis.Z);
//    }

    public IntPoint getIndices(double x, double y, double z) {
        switch (domain.dimension()) {
            case 1: {
                int i = xIndex(x);
                if (i < 0 || i >= xcount) {
                    return null;
                }
                return IntPoint.create(i);
            }
            case 2: {
                int i = xIndex(x);
                if (i < 0 || i >= xcount - 1) {
                    return null;
                }
                int j = yIndex(y);
                if (j < 0 || j >= ycount) {
                    return null;
                }
                return IntPoint.create(i, j);
            }
        }
        int i = xIndex(x);
        if (i < 0 || i >= xcount) {
            return null;
        }
        int j = yIndex(y);
        if (j < 0 || j >= ycount) {
            return null;
        }
        int k = zIndex(z);
        if (k < 0 || k >= zcount) {
            return null;
        }
        return IntPoint.create(i, j, k);
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

    public DDiscrete inv() {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = 1 / this.values[i][j][k];
                }
            }
        }
        return DDiscrete.of(domain, d, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public String toLatex() {
        throw new UnsupportedOperationException("Not Implemented toLatex for "+getClass().getName());
    }

}
