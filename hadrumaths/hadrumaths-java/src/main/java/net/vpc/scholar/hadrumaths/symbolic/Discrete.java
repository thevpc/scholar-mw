package net.vpc.scholar.hadrumaths.symbolic;


import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.format.impl.AbstractFormatter;
import net.vpc.scholar.hadrumaths.geom.IntPoint;
import net.vpc.scholar.hadrumaths.geom.Point;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 14 mai 2007 22:18:44
 */
public class Discrete extends AbstractDoubleToComplex implements Dumpable, Cloneable, Normalizable {
    private static final long serialVersionUID = 1L;

    static {
        FormatFactory.register(Discrete.class, new AbstractFormatter<Discrete>() {
            @Override
            public void format(StringBuilder sb, Discrete o, FormatParamSet format) {
                sb.append("Discrete(");
                sb.append(Maths.dump(o.values));
                sb.append(")");
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
    private int dimension;
    private Domain domain;


//    public Cube(Complex value, int x, int y, int z) {
//        Complex[][][] _model = new Complex[z][y][x];
//        for (Complex[][] complexes : _model) {
//            for (Complex[] complex : complexes) {
//                for (int i = 0; i < complex.length; i++) {
//                    complex[i] = value;
//                }
//            }
//        }
//        double[] xx = Maths.dtimes(0.0, x - 1, x);
//        double[] yy = Maths.dtimes(0.0, y - 1, y);
//        double[] zz = Maths.dtimes(0.0, z - 1, z);
//        double dx = 1;
//        double dy = 1;
//        double dz = 1;
//        dsteps(_model, xx, yy, zz, dx, dy, dz, Axis.X, Axis.Y, Axis.Z);
//    }
//
//    public Cube(Complex[][][] model, double[] xvalues, double[] yvalues, double[] zvalues) {
//        this(model, xvalues, yvalues, zvalues,
//                xvalues.length>2?xvalues[1]-xvalues[0]:1,
//                yvalues.length>2?yvalues[1]-yvalues[0]:1,
//                zvalues.length>2?zvalues[1]-zvalues[0]:1
//                );
//    }
//    public Cube(Complex[][][] model, double[] xvalues, double[] yvalues, double[] zvalues, double dx, double dy, double dz) {
//        this(model, xvalues, yvalues, zvalues, dx, dy, dz, Axis.X, Axis.Y, Axis.Z);
//    }
//
//    public Cube(Complex[][][] model, double[] xvalues, double[] yvalues, double[] zvalues, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
//        dsteps(model, xvalues, yvalues, zvalues, dx, dy, dz, axis1, axis2, axis3);
//    }

    public Discrete(Domain domain, Complex[][][] values, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3, int dim) {
        init(domain, values, x, y, z, dx, dy, dz, axis1, axis2, axis3, dim);
    }

    public static Discrete cst(Domain domain, Complex value, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        AbsoluteSamples steps = domain.steps(dx, dy, dz);
        Complex[][][] model = ArrayUtils.fillArray3Complex(steps.getX().length, steps.getY().length, steps.getZ().length, value);
        return new Discrete(domain, model, null, null, null, dx, dy, dz, axis1, axis2, axis3, 3);
    }

    public static Discrete create(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3, int dimension) {
        return new Discrete(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3, dimension);
    }

    public static Discrete create(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3) {
        return new Discrete(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3, 3);
    }

    public static Discrete create(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z, double dx, double dy, double dz) {
        return new Discrete(domain, model, x, y, z, dx, dy, dz, Axis.X, Axis.Y, Axis.Z, 3);
    }

    public static Discrete create(Domain domain, Complex[][][] model, double[] x, double[] y, double[] z) {
        return new Discrete(domain, model, x, y, z, -1, -1, -1, Axis.X, Axis.Y, Axis.Z, 3);
    }

    public static Discrete create(Domain domain, Complex[][][] model, double dx, double dy, double dz) {
        return new Discrete(domain, model, null, null, null, dx, dy, dz, Axis.X, Axis.Y, Axis.Z, 3);
    }

    public static Discrete create(Complex[][][] model, double[] x, double[] y, double[] z) {
        Domain domain = Domain.forArray(x, y, z);
        return new Discrete(domain, model, x, y, z, -1, -1, -1, Axis.X, Axis.Y, Axis.Z, 3);
    }

    public static Discrete create(Domain domain, Complex[][] model, double[] x, double[] y) {
        double[] z = {0};
        return new Discrete(domain, new Complex[][][]{model}, x, y, z, -1, -1, -1, Axis.X, Axis.Y, Axis.Z, 2);
    }

    public static Discrete create(Complex[][] model, double[] x, double[] y) {
        double[] z = {0};
        Domain domain = Domain.forArray(x, y, z);
        return new Discrete(domain, new Complex[][][]{model}, x, y, z, -1, -1, -1, Axis.X, Axis.Y, Axis.Z, 2);
    }

    public static Discrete create(Domain domain, Complex[] model, double[] x) {
        double[] z = {0};
        double[] y = {0};
        return new Discrete(domain, new Complex[][][]{{model}}, x, y, z, -1, -1, -1, Axis.X, Axis.Y, Axis.Z, 1);
    }

    public static Discrete create(Complex[] model, double[] x) {
        double[] z = {0};
        double[] y = {0};
        Domain domain = Domain.forArray(x, y, z);
        return new Discrete(domain, new Complex[][][]{{model}}, x, y, z, -1, -1, -1, Axis.X, Axis.Y, Axis.Z, 1);
    }

    public String dump() {
        return Maths.dump(values);
    }

//    @Override
//    public String toString() {
//        return Maths.dump(values);
//    }

    private void init(Domain domain, Complex[][][] model, double[] xvalues, double[] yvalues, double[] zvalues, double dx, double dy, double dz, Axis axis1, Axis axis2, Axis axis3, int dim) {
        this.domain = domain.toDomain(dim);
        this.dimension = dim;
        switch (dim) {
            case 1: {
                if (dx <= 0 && xvalues == null) {
                    throw new IllegalArgumentException("Invalid dx or xvalues");
                } else if (dx <= 0) {
                    if (xvalues.length < 2) {
                        dx = 1;
                    } else {
                        dx = xvalues[1] - xvalues[0];
                    }
                }

                this.values = model;
                this.dx = dx;
                this.dy = dy;
                this.dz = dz;
                zcount = 1;
                ycount = 1;
                xcount = model.length == 0 ? 0 : model[0][0].length;
                this.axis = new Axis[]{axis1, axis2, axis3};

                this.x = xvalues != null ? xvalues : domain.steps(dx).getX();
                this.y = new double[]{0};
                this.z = new double[]{0};
                break;
            }
            case 2: {
                if (dx <= 0 && xvalues == null) {
                    throw new IllegalArgumentException("Invalid dx or xvalues");
                } else if (dx <= 0) {
                    if (xvalues.length < 2) {
                        dx = 1;
                    } else {
                        dx = xvalues[1] - xvalues[0];
                    }
                }
                if (dy <= 0 && yvalues == null) {
                    throw new IllegalArgumentException("Invalid dy or yvalues");
                } else if (dy <= 0) {
                    if (yvalues.length < 2) {
                        dy = 1;
                    } else {
                        dy = yvalues[1] - yvalues[0];
                    }
                }

                this.values = model;
                this.dx = dx;
                this.dy = dy;
                this.dz = dz;
                zcount = 1;
                ycount = model.length == 0 ? 0 : model[0].length;
                xcount = model.length == 0 ? 0 : model[0][0].length;
                this.axis = new Axis[]{axis1, axis2, axis3};
                AbsoluteSamples steps = (xvalues == null || yvalues == null) ? domain.steps(dx, dy) : null;
                this.x = xvalues != null ? xvalues : steps.getX();
                this.y = yvalues != null ? yvalues : steps.getY();
                this.z = new double[]{0};
                break;
            }
            case 3: {
                if (dx <= 0 && xvalues == null) {
                    throw new IllegalArgumentException("Invalid dx or xvalues");
                } else if (dx <= 0) {
                    if (xvalues.length < 2) {
                        dx = 1;
                    } else {
                        dx = xvalues[1] - xvalues[0];
                    }
                }
                if (dy <= 0 && yvalues == null) {
                    throw new IllegalArgumentException("Invalid dy or yvalues");
                } else if (dy <= 0) {
                    if (yvalues.length < 2) {
                        dy = 1;
                    } else {
                        dy = yvalues[1] - yvalues[0];
                    }
                }
                if (dz <= 0 && zvalues == null) {
                    throw new IllegalArgumentException("Invalid dz or zvalues");
                } else if (dz <= 0) {
                    if (zvalues.length < 2) {
                        dz = 1;
                    } else {
                        dz = zvalues[1] - zvalues[0];
                    }
                }
                this.values = model;
                this.dx = dx;
                this.dy = dy;
                this.dz = dz;
                zcount = model.length;
                ycount = model.length == 0 ? 0 : model[0].length;
                xcount = model.length == 0 ? 0 : model[0][0].length;
                this.axis = new Axis[]{axis1, axis2, axis3};
                AbsoluteSamples steps = (xvalues == null || yvalues == null || zvalues == null) ? domain.steps(dx, dy, dz) : null;
                this.x = xvalues != null ? xvalues : steps.getX();
                this.y = yvalues != null ? yvalues : steps.getY();
                this.z = zvalues != null ? zvalues : steps.getZ();
                break;
            }
        }

        if (axis1.equals(axis2) || axis2.equals(axis3) || axis3.equals(axis1)) {
            throw new IllegalArgumentException();
        }
    }

    public Discrete add(Complex c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].add(c);
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete sub(Complex c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sub(c);
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete mul(Complex c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(c);
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete mul(double c) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(c);
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    @Override
    public Discrete mul(Domain domain) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(domain.computeDouble(x[k], y[j], z[i]));
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    //@Override
    public Discrete mul(DoubleToDouble other) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(other.computeDouble(x[k], y[j], z[i]));
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete mul(Discrete other) {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].mul(other.values[i][j][k]);
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public IntPoint getIndices(double x, double y, double z) {
        switch (dimension) {
            case 1: {
                int i = (int) ((x - domain.xmin()) / dx);
                return IntPoint.create(i);
            }
            case 2: {
                int i = (int) ((x - domain.xmin()) / dx);
                int j = (int) ((y - domain.ymin()) / dy);
                return IntPoint.create(i, j);
            }
        }
        int i = (int) ((x - domain.xmin()) / dx);
        int j = (int) ((y - domain.ymin()) / dy);
        int k = (int) ((z - domain.zmin()) / dz);
        return IntPoint.create(i, j, k);
    }

    public Point getPointAt(IntPoint indices) {
        return getPointAt(indices.x, indices.y, indices.z);
    }

    public Point getPointAt(int xi, int yi, int zi) {
        switch (dimension) {
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

    public Discrete add(DoubleToComplex other) {
        if (other instanceof Discrete) {
            Discrete c = (Discrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k].add(c.values[i][j][k]);
                        }
                    }
                }
                return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
            }
            Domain d2 = getDomain().expand(other.getDomain());
            double dx = Math.min(this.dx, c.dx);
            double dy = Math.min(this.dy, c.dy);
            double dz = Math.min(this.dz, c.dz);
            AbsoluteSamples xyz = d2.steps(dx, dy, dz);
            Complex[][][] a1 = computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] a2 = c.computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] d = ArrayUtils.add(a1, a2);
            return create(d2, d, null, null, null, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        Complex[][][] a1 = computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] a2 = other.computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] d = ArrayUtils.add(a1, a2);
        return create(d2, d, null, null, null, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete sub(DoubleToComplex other) {
        if (other instanceof Discrete) {
            Discrete c = (Discrete) other;
            if (getDomain().equals(other.getDomain()) && dx == c.dx && dy == c.dy && dz == c.dz) {
                Complex[][][] d = new Complex[zcount][ycount][xcount];
                for (int i = 0; i < this.zcount; i++) {
                    for (int j = 0; j < this.ycount; j++) {
                        for (int k = 0; k < this.xcount; k++) {
                            d[i][j][k] = this.values[i][j][k].sub(c.values[i][j][k]);
                        }
                    }
                }
                return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
            }
            Domain d2 = getDomain().expand(other.getDomain());
            double dx = Math.min(this.dx, c.dx);
            double dy = Math.min(this.dy, c.dy);
            double dz = Math.min(this.dz, c.dz);
            AbsoluteSamples xyz = d2.steps(dx, dy, dz);
            Complex[][][] a1 = computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] a2 = c.computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
            Complex[][][] d = ArrayUtils.sub(a1, a2);
            return create(d2, d, null, null, null, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
        }
        Domain d2 = getDomain().expand(other.getDomain());
        AbsoluteSamples xyz = d2.steps(dx, dy, dz);
        Complex[][][] a1 = computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] a2 = other.computeComplex(xyz.getX(), xyz.getY(), xyz.getZ(), null, null);
        Complex[][][] d = ArrayUtils.sub(a1, a2);
        return create(d2, d, null, null, null, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete relativeError(Discrete other) {
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
                        c = Complex.valueOf(((a.sub(b)).absdbl() * 100 / (b.absdbl())));
                    }

                    d[i][j][k] = c;
                }
            }
        }
        return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

//    public CCube(Complex[][][] model) {
//        this(model, Axis.X, Axis.Y, Axis.Z);
//    }

    public Complex integrate(Domain domain, PlaneAxis axis, double fixedValue) {
        Domain domain0 = getDomain();
        Domain d2 = domain0.intersect(domain);
        switch (axis) {
            case XY: {
                Range range = d2.range(x, y, new double[]{fixedValue});
                Complex v = Complex.ZERO;
                if (range != null) {
                    int zi = (int) ((fixedValue - domain0.zmin()) / dz);
                    double delta = dx * dy;
                    for (int i = range.xmin; i <= range.xmax; i++) {
                        for (int j = range.ymin; j < range.ymax; j++) {
                            v = v.add(values[zi][j][i].mul(delta));
                        }
                    }
                }
                return v;
            }
            case XZ: {
                Range range = d2.range(x, new double[]{fixedValue}, z);
                Complex v = Complex.ZERO;
                if (range != null && getDomain().y().contains(fixedValue)) {
                    int yi = (int) ((fixedValue - domain0.ymin()) / dy);
                    double delta = dx * dz;
                    for (int i = range.xmin; i <= range.xmax; i++) {
                        for (int j = range.zmin; j < range.zmax; j++) {
                            v = v.add(values[j][yi][i].mul(delta));
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
                    int xi = (int) ((fixedValue - domain0.xmin()) / dx);
                    for (int i = range.ymin; i <= range.ymax; i++) {
                        for (int j = range.zmin; j < range.zmax; j++) {
                            v = v.add(values[j][i][xi].mul(delta));
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
                        v = v.add(values[k][j][i].mul(dx * dy * dz));
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
    public Discrete diff(Axis axis) {
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
                return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
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
                return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
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
                return create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
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

    public Vector getVector(Axis axis) {
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

    public Vector getVector(Axis axis, Axis fixedAxis1, int index1, Axis fixedAxis2, int index2) {
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
                    throw new IllegalArgumentException("Missing Y axis index");
                }
                if (y.intValue() < 0 || y.intValue() >= getCountY()) {
                    throw new IllegalArgumentException("Invalid Y axis index");
                }
                if (z == null) {
                    throw new IllegalArgumentException("Missing Z axis index");
                }
                if (z.intValue() < 0 || z.intValue() >= getCountZ()) {
                    throw new IllegalArgumentException("Invalid Z axis index");
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
                    throw new IllegalArgumentException("Missing X axis index");
                }
                if (x.intValue() < 0 || x.intValue() >= getCountX()) {
                    throw new IllegalArgumentException("Invalid X axis index");
                }
                if (z == null) {
                    throw new IllegalArgumentException("Missing Z axis index");
                }
                if (z.intValue() < 0 || z.intValue() >= getCountZ()) {
                    throw new IllegalArgumentException("Invalid Z axis index");
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
                    throw new IllegalArgumentException("Missing X axis index");
                }
                if (x.intValue() < 0 || x.intValue() >= getCountX()) {
                    throw new IllegalArgumentException("Invalid X axis index");
                }
                if (y == null) {
                    throw new IllegalArgumentException("Missing Y axis index");
                }
                if (y.intValue() < 0 || y.intValue() >= getCountY()) {
                    throw new IllegalArgumentException("Invalid Y axis index");
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

    public int getCountY() {
        if (values.length == 0) {
            return 0;
        }
        return values[0].length;
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

    public Matrix getMatrix(PlaneAxis plane, int index) {
        return Maths.matrix(getArray(plane, index));
    }

    public Matrix getMatrix(Axis fixedAxis, int index) {
        return Maths.matrix(getArray(fixedAxis, index));
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

    public double[] getY() {
        return y;
    }

    public double[] getZ() {
        return z;
    }

    @Override
    public boolean isScalarExprImpl() {
        return true;
    }

    @Override
    public boolean isInvariantImpl(Axis axis) {
        switch (axis) {
            case X: {
                return false;
            }
            case Y: {
                return dimension<2;
            }
            case Z: {
                return dimension<3;
            }
        }
        return false;
    }

    @Override
    public boolean isDoubleExprImpl() {
        return false;
    }

    @Override
    public boolean isDDImpl() {
        return false;
    }

    @Override
    public DoubleToDouble toDD() {
        throw new IllegalArgumentException();
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
    public DoubleToVector toDV() {
        return new VDiscrete(this);
    }

    @Override
    public DoubleToMatrix toDM() {
        return new VDiscrete(this);
    }

    @Override
    public boolean isZeroImpl() {
        return false;
    }

    @Override
    public boolean isNaNImpl() {
        return false;
    }

    @Override
    public Expr setParam(String name, Expr value) {
        return this;
    }

    @Override
    public boolean isInfiniteImpl() {
        return false;
    }

    @Override
    public List<Expr> getSubExpressions() {
        return Collections.EMPTY_LIST;
    }


    public Complex computeComplex(double x, double y, double z, OutBoolean defined) {
        switch (dimension) {
            case 1: {
                if (contains(x)) {
                    int xi = (int) ((x - domain.xmin()) / dx);
                    defined.set();
                    return values[0][0][xi];
                }
                break;
            }
            case 2: {
                if (contains(x, y)) {
                    int xi = (int) ((x - domain.xmin()) / dx);
                    int yi = (int) ((y - domain.ymin()) / dy);
                    defined.set();
                    return values[0][yi][xi];
                }
                break;
            }
        }
        if (contains(x, y)) {
            int xi = (int) ((x - domain.xmin()) / dx);
            int yi = (int) ((y - domain.ymin()) / dy);
            int zi = (int) ((z - domain.zmin()) / dz);
            defined.set();
            return values[zi][yi][xi];
        }
        return Complex.ZERO;
    }

    @Override
    public Complex[] computeComplex(double[] x, Domain d0, Out<Range> ranges) {
        switch (dimension) {
            case 1: {
                Complex[] r = new Complex[x.length];
                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
                if (currRange != null) {
                    int xmin = currRange.xmin;
                    int xmax = currRange.xmax;
                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
                        double xx = xmin + xIndex * dx;
                        if (contains(xx)) {
                            int xi = (int) ((xx - domain.xmin()) / dx);
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
        throw new IllegalArgumentException("Missing z");
    }


    @Override
    public Complex[][] computeComplex(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        switch (dimension) {
            case 1: {
                Complex[][] r = new Complex[y.length][x.length];
                Range currRange = (d0 == null ? domain : domain.intersect(d0)).range(x);
                if (currRange != null) {
                    int xmin = currRange.xmin;
                    int xmax = currRange.xmax;
                    for (int xIndex = xmin; xIndex <= xmax; xIndex++) {
                        double xx = xmin + xIndex * dx;
                        if (contains(xx)) {
                            int xi = (int) ((xx - domain.xmin()) / dx);
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
                        double xx = xmin + xIndex * dx;
                        for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
                            double yy = ymin + yIndex * dy;
                            if (contains(xx, yy)) {
                                int xi = (int) ((xx - domain.xmin()) / dx);
                                int yi = (int) ((yy - domain.ymin()) / dy);
                                int yi0 = yi < ycount ? yi : (ycount - 1);
                                int xi0 = xi < xcount ? xi : (xcount - 1);
                                r[yIndex][xIndex] = values[0][yi0][xi0];
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
        }
        throw new IllegalArgumentException("Missing z");
    }

    @Override
    public Complex[][][] computeComplex(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
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
                int xi = (int) ((xx - domain.xmin()) / dx);
                for (int yIndex = ymin; yIndex <= ymax; yIndex++) {
//                    double yy = ymin + yIndex * dy;
                    double yy = y[yIndex];//ymin + yIndex * dy;
                    int yi = (int) ((yy - domain.ymin()) / dy);
                    for (int zIndex = zmin; zIndex <= zmax; zIndex++) {
                        double zz = z[zIndex];//zmin + zIndex * dz;
                        int zi = (int) ((zz - domain.zmin()) / dz);
                        r[zIndex][yIndex][xIndex] = values[zi][yi][xi];
//                        if (contains(xx, yy, zz)) {
//                            int xi = (int) ((xx - domain.xmin) / dx);
//                            int yi = (int) ((yy - domain.ymin) / dy);
//                            int zi = (int) ((zz - domain.ymin) / dz);
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
            Complex[][][] c = ArrayUtils.fillArray3Complex(x.length, y.length, z.length, Complex.ZERO);
            if (ranges != null) {
                ranges.set(null);
            }
            return c;
        }
        return r;
    }

    @Override
    public Complex[] computeComplex(double[] x, double y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex[] computeComplex(double x, double[] y, Domain d0, Out<Range> ranges) {
        return Expressions.computeComplex(this, x, y, d0, ranges);
    }

    @Override
    public Complex computeComplex(double x, double y, OutBoolean defined) {
        switch (dimension) {
            case 1: {
                if (contains(x)) {
                    int xi = (int) ((x - domain.xmin()) / dx);
                    defined.set();
                    return values[0][0][xi];
                }
                return Complex.ZERO;
            }
            case 2: {
                if (contains(x, y)) {
                    int xi = (int) ((x - domain.xmin()) / dx);
                    int yi = (int) ((y - domain.ymin()) / dy);
                    defined.set();
                    return values[0][yi][xi];
                }
                return Complex.ZERO;
            }
        }
        throw new IllegalArgumentException("Missing z");
    }

    public Discrete inv() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].inv();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete neg() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].neg();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete conj() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].conj();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete exp() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].exp();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete log() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].log();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete sqrt() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sqrt();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete sqr() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sqr();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete sin() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].sin();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
    }

    public Discrete cos() {
        Complex[][][] d = new Complex[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].cos();
                }
            }
        }
        return Discrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2], dimension);
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
        return DDiscrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2]);
    }

    //@Override
    public DoubleToDouble abs() {
        double[][][] d = new double[zcount][ycount][xcount];
        for (int i = 0; i < zcount; i++) {
            for (int j = 0; j < ycount; j++) {
                for (int k = 0; k < xcount; k++) {
                    d[i][j][k] = this.values[i][j][k].absdbl();
                }
            }
        }
        return DDiscrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2]);
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
        return DDiscrete.create(domain, d, x, y, z, dx, dy, dz, this.axis[0], this.axis[1], this.axis[2]);
    }

    @Override
    public Domain getDomainImpl() {
        return domain;
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

    public int getDimension() {
        return dimension;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discrete)) return false;
        if (!super.equals(o)) return false;

        Discrete discrete = (Discrete) o;

        if (dimension != discrete.dimension) return false;
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
        if (!Arrays.equals(z, discrete.z)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (axis != null ? Arrays.hashCode(axis) : 0);
        result = 31 * result + (values != null ? ArrayUtils.hashCode(values) : 0);
        result = 31 * result + (x != null ? Arrays.hashCode(x) : 0);
        result = 31 * result + (y != null ? Arrays.hashCode(y) : 0);
        result = 31 * result + (z != null ? Arrays.hashCode(z) : 0);
        result = 31 * result + zcount;
        result = 31 * result + ycount;
        result = 31 * result + xcount;
        temp = Double.doubleToLongBits(dx);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dy);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(dz);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + dimension;
        result = 31 * result + (domain != null ? domain.hashCode() : 0);
        return result;
    }

    private Complex diff0(Complex b, Complex a, double x2, double x1) {
        if (x1 == x2) {
            return Complex.ZERO;
        }
        return a.sub(b).div(x2 - x1);
    }


    public static Discrete discretize(Expr expr, Domain domain, int xSamples, int ySamples, int zSamples) {
        return discretize(expr, domain, xSamples, ySamples, zSamples, 3);
    }

    public static Discrete discretize(Expr expr, Domain domain, int xSamples, int ySamples) {
        return discretize(expr, domain, xSamples, ySamples, 1, 2);
    }

    public static Discrete discretize(Expr expr, Domain domain, int xSamples) {
        return discretize(expr, domain, xSamples, 1, 1, 1);
    }

    public static Discrete discretize(Expr expr, int xSamples, int ySamples, int zSamples) {
        return discretize(expr, null, xSamples, ySamples, zSamples, 3);
    }

    public static Discrete discretize(Expr expr, int xSamples, int ySamples) {
        return discretize(expr, null, xSamples, ySamples, 1, 2);
    }

    public static Discrete discretize(Expr expr, int xSamples) {
        return discretize(expr, null, xSamples, 1, 1, 1);
    }

    public static Discrete discretize(Expr expr, Domain domain, int xSamples, int ySamples, int zSamples, int dim) {
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (expr.isScalarExpr()) {
            switch (dim) {
                case 1: {
                    if (expr.isDouble() && domain.isInfinite()) {
                        Complex[] vv = new Complex[xSamples];
                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples], domain.xmin()) : domain.xtimes(xSamples);
                        return Discrete.create(
                                domain,
                                vv,
                                xx);
                    }
                    AbsoluteSamples samples = domain.times(xSamples);
                    Complex[] model = expr.toDC().computeComplex(samples.getX(), (Domain) null, null);
                    return Discrete.create(domain, model, samples.getX());
                }
                case 2: {
                    if (expr.isDouble() && domain.isInfinite()) {
                        Complex[][] vv = new Complex[ySamples][xSamples];
                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples], domain.xmin()) : domain.xtimes(xSamples);
                        double[] yy = domain.isInfiniteY() ? ArrayUtils.fill(new double[ySamples], domain.ymin()) : domain.ytimes(ySamples);
                        return Discrete.create(
                                domain,
                                vv,
                                xx,
                                yy);
                    }
                    AbsoluteSamples samples = domain.times(xSamples, ySamples);
                    Complex[][] model = expr.toDC().computeComplex(samples.getX(), samples.getY());
                    return Discrete.create(domain, model, samples.getX(), samples.getY());
                }
                case 3: {
                    if (expr.isZero() && domain.isInfinite()) {
                        return Discrete.create(
                                Domain.EMPTY(domain.dimension()),
                                new Complex[0][0][0],
                                new double[0],
                                new double[0],
                                new double[0]);
                    } else if (expr.isDouble() && domain.isInfinite()) {
                        Complex[][][] vv = new Complex[zSamples][ySamples][xSamples];
                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples], domain.xmin()) : domain.x().times(xSamples, 1, 1).getX();
                        double[] yy = domain.isInfiniteY() ? ArrayUtils.fill(new double[ySamples], domain.ymin()) : domain.y().times(ySamples, 1, 1).getX();
                        double[] zz = domain.isInfiniteZ() ? ArrayUtils.fill(new double[zSamples], domain.zmin()) : domain.z().times(zSamples, 1, 1).getX();
                        return Discrete.create(
                                domain,
                                vv,
                                xx,
                                yy,
                                zz);
                    }
                    AbsoluteSamples samples = domain.times(xSamples, ySamples, zSamples);
                    Complex[][][] model = expr.toDC().computeComplex(samples.getX(), samples.getY(), samples.getZ(), null, null);
                    return Discrete.create(domain, model, samples.getX(), samples.getY(), samples.getZ());
                }
            }
            throw new IllegalArgumentException("Unsupported dimension " + dim);
        } else {
            throw new IllegalArgumentException("Expression is not scalar");
        }
    }

    private static AbsoluteSamples toAbsoluteSamples(Samples samples, Domain domain) {
        if (samples instanceof RelativeSamples) {
            return ((RelativeSamples) samples).toAbsolute(domain);
        }
        return (AbsoluteSamples) samples;
    }

    public static Discrete discretize(Expr expr, Domain domain, Samples samples) {
        if (domain == null) {
            domain = expr.getDomain();
        }
        if (expr.isScalarExpr()) {
            switch (samples.getDimension()) {
                case 1: {
//                    if (expr.isDouble() && domain.isInfinite()) {
//                        Complex[] vv = new Complex[xSamples];
//                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples], domain.xmin()) : domain.xtimes(xSamples);
//                        return Discrete.create(
//                                domain,
//                                vv,
//                                xx);
//                    }
                    AbsoluteSamples absoluteSamples = toAbsoluteSamples(samples, domain);
                    Complex[] model = expr.toDC().computeComplex(absoluteSamples.getX(), (Domain) null, null);
                    return Discrete.create(domain, model, absoluteSamples.getX());
                }
                case 2: {
//                    if (expr.isDouble() && domain.isInfinite()) {
//                        Complex[][] vv = new Complex[ySamples][xSamples];
//                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples], domain.xmin()) : domain.xtimes(xSamples);
//                        double[] yy = domain.isInfiniteY() ? ArrayUtils.fill(new double[ySamples], domain.ymin()) : domain.ytimes(ySamples);
//                        return Discrete.create(
//                                domain,
//                                vv,
//                                xx,
//                                yy);
//                    }
                    AbsoluteSamples absoluteSamples = toAbsoluteSamples(samples, domain);
                    Complex[][] model = expr.toDC().computeComplex(absoluteSamples.getX(), absoluteSamples.getY());
                    return Discrete.create(domain, model, absoluteSamples.getX(), absoluteSamples.getY());
                }
                case 3: {
                    if (expr.isZero() && domain.isInfinite()) {
                        return Discrete.create(
                                Domain.EMPTY(domain.dimension()),
                                new Complex[0][0][0],
                                new double[0],
                                new double[0],
                                new double[0]);
                    } else if (expr.isDouble() && domain.isInfinite()) {
//                        Complex[][][] vv = new Complex[zSamples][ySamples][xSamples];
//                        double[] xx = domain.isInfiniteX() ? ArrayUtils.fill(new double[xSamples], domain.xmin()) : domain.x().times(xSamples, 1, 1).getX();
//                        double[] yy = domain.isInfiniteY() ? ArrayUtils.fill(new double[ySamples], domain.ymin()) : domain.y().times(ySamples, 1, 1).getX();
//                        double[] zz = domain.isInfiniteZ() ? ArrayUtils.fill(new double[zSamples], domain.zmin()) : domain.z().times(zSamples, 1, 1).getX();
//                        return Discrete.create(
//                                domain,
//                                vv,
//                                xx,
//                                yy,
//                                zz);
                    }
//                    Samples samples = domain.times(xSamples, ySamples, zSamples);
                    AbsoluteSamples absoluteSamples = toAbsoluteSamples(samples, domain);
                    Complex[][][] model = expr.toDC().computeComplex(absoluteSamples.getX(), absoluteSamples.getY(), absoluteSamples.getZ(), null, null);
                    return Discrete.create(domain, model, absoluteSamples.getX(), absoluteSamples.getY(), absoluteSamples.getZ());
                }
            }
            throw new IllegalArgumentException("Unsupported dimension " + samples.getDimension());
        } else {
            throw new IllegalArgumentException("Expression is not scalar");
        }
    }

    @Override
    public Complex computeComplex(double x, OutBoolean defined) {
        Out<Range> ranges = new Out<>();
        Complex complex = computeComplex(new double[]{x}, null, ranges)[0];
        defined.set(ranges.get().getDefined1().get(0));
        return complex;
    }


    @Override
    public double getDistance(Normalizable other) {
        if (other instanceof Discrete) {
            Discrete o = (Discrete) other;
            return (this.sub(o)).norm() / o.norm();
        } else {
            Normalizable o = other;
            return (this.norm() - o.norm()) / o.norm();
        }
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
//                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].mul(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
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
//                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].mul(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            }
//        }
//        return super.mul(other);
//    }

    @Override
    public Expr div(Expr other) {
        if (other.isDouble()) {
            double dbl = other.toDouble();
            Domain dom = other.getDomain();
            if (dom.isUnconstrained()) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values[i][j][k].div(dbl);
                        }
                    }
                }
                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            } else {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].div(dbl) : Complex.ZERO;
                        }
                    }
                }
                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other.isComplex()) {
            Complex dbl = other.toComplex();
            Domain dom = other.getDomain();
            if (dom.isUnconstrained()) {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = values[i][j][k].div(dbl);
                        }
                    }
                }
                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            } else {
                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
                for (int i = 0; i < values2.length; i++) {
                    for (int j = 0; j < values2[i].length; j++) {
                        for (int k = 0; k < values2[i][j].length; k++) {
                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values[i][j][k].div(dbl) : Complex.ZERO;
                        }
                    }
                }
                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        }
        return super.mul(other);
    }

    @Override
    public Expr add(Expr other) {
        if (other.isDouble()) {
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
                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other.isComplex()) {
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
                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other instanceof Discrete && isSameSampling((Discrete) other)) {
            Discrete dother = (Discrete) other;
            Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
            for (int i = 0; i < values2.length; i++) {
                for (int j = 0; j < values2[i].length; j++) {
                    for (int k = 0; k < values2[i][j].length; k++) {
                        values2[i][j][k] = values[i][j][k].add(dother.values[i][j][k]);
                    }
                }
            }
            return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
        }
        return super.mul(other);
    }

    @Override
    public Expr sub(Expr other) {
        if (other.isDouble()) {
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
                return new Discrete(this.domain, values, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new Discrete(this.domain, values, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other.isComplex()) {
            Complex dbl = other.toComplex();
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
                return new Discrete(this.domain, values, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
//            } else {
//                Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
//                for (int i = 0; i < values2.length; i++) {
//                    for (int j = 0; j < values2[i].length; j++) {
//                        for (int k = 0; k < values2[i][j].length; k++) {
//                            values2[i][j][k] = dom.contains(this.x[k], this.x[j], this.x[i]) ? values2[i][j][k].div(dbl) : Complex.ZERO;
//                        }
//                    }
//                }
//                return new Discrete(this.domain, values, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
            }
        } else if (other instanceof Discrete && isSameSampling((Discrete) other)) {
            Discrete dother = (Discrete) other;
            Complex[/*Z*/][/*Y*/][/*X*/] values2 = new Complex[getCountZ()][getCountY()][getCountX()];
            for (int i = 0; i < values2.length; i++) {
                for (int j = 0; j < values2[i].length; j++) {
                    for (int k = 0; k < values2[i][j].length; k++) {
                        values2[i][j][k] = values[i][j][k].sub(dother.values[i][j][k]);
                    }
                }
            }
            return new Discrete(this.domain, values2, ArrayUtils.copy(x), ArrayUtils.copy(y), ArrayUtils.copy(z), dx, dy, dz, Axis.X, Axis.Y, Axis.Z, dimension);
        }
        return super.mul(other);
    }


    private boolean isSameSampling(Discrete other) {
        if (other == null) {
            return false;
        }
        if (other.getDimension() != getDimension()) {
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
        if (!Arrays.equals(other.getZ(), getZ())) {
            return false;
        }
        return true;
    }

    @Override
    public Expr mul(Expr other) {
        if (other instanceof Domain) {
            return mul((Domain) other);
        }
        if (other.isDouble()) {
            return mul(other.toDouble());
        }
        if (other.isDoubleExpr()) {
            return mul(other.toDouble()).mul(other.getDomain());
        }
        if (other.isComplex()) {
            return mul(other.toComplex());
        }
        if (other.isComplexExpr()) {
            return mul(other.toComplex()).mul(other.getDomain());
        }
        if (other instanceof Discrete) {
            return mul((Discrete) other);
        }
        if (other instanceof DoubleToDouble) {
            return mul((DoubleToDouble) other);
        }
        return Maths.mul(this, other);
    }
}
