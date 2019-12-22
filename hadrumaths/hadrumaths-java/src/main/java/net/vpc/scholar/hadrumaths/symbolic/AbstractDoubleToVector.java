package net.vpc.scholar.hadrumaths.symbolic;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.conv.DV2DM;

/**
 * Created by vpc on 8/24/14.
 */
public abstract class AbstractDoubleToVector extends AbstractExprPropertyAware implements DoubleToVector {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean isDVImpl() {
        return true;
    }

    @Override
    public DoubleToVector toDV() {
        return this;
    }

    @Override
    public DoubleToComplex toDC() {
        if (isDC()) {
            return getComponent(Axis.X).toDC();
        }
        throw new IllegalArgumentException("Unsupported toDC in " + getClass().getName() + " :: " + toString());
    }

    @Override
    protected boolean isDDImpl() {
        return isDC() && toDC().isDD();
    }

    @Override
    protected boolean isDCImpl() {
        return isScalarExpr();
    }

    @Override
    protected boolean isDMImpl() {
        return true;
    }

    @Override
    public DoubleToMatrix toDM() {
        return new DV2DM(this);
    }


    //@Override
    public ComplexVector computeVector(double x, double y, BooleanMarker defined) {
        switch (getComponentSize()) {
            case 1: {
                return MathsBase.columnVector(
                        getComponent(Axis.X).toDC().computeComplex(x, y, defined)
                );
            }
            case 2: {
                return MathsBase.columnVector(
                        getComponent(Axis.X).toDC().computeComplex(x, y, defined),
                        getComponent(Axis.Y).toDC().computeComplex(x, y, defined)
                );
            }
            case 3: {
                return MathsBase.columnVector(
                        getComponent(Axis.X).toDC().computeComplex(x, y, defined),
                        getComponent(Axis.Y).toDC().computeComplex(x, y, defined),
                        getComponent(Axis.Z).toDC().computeComplex(x, y, defined)
                );
            }
        }
        throw new UnsupportedComponentDimensionException(getComponentDimension());
    }

    //@Override
    public ComplexVector computeVector(double x, BooleanMarker defined) {
        switch (getComponentSize()) {
            case 1: {
                return MathsBase.columnVector(
                        getComponent(Axis.X).toDC().computeComplex(x, defined)
                );
            }
            case 2: {
                return MathsBase.columnVector(
                        getComponent(Axis.X).toDC().computeComplex(x, defined),
                        getComponent(Axis.Y).toDC().computeComplex(x, defined)
                );
            }
            case 3: {
                return MathsBase.columnVector(
                        getComponent(Axis.X).toDC().computeComplex(x, defined),
                        getComponent(Axis.Y).toDC().computeComplex(x, defined),
                        getComponent(Axis.Z).toDC().computeComplex(x, defined)
                );
            }
        }
        throw new UnsupportedComponentDimensionException(getComponentDimension());
    }


    //@Override
    public ComplexVector computeVector(double x, double y, double z, BooleanMarker defined) {

        switch (getComponentSize()) {
            case 1: {
                DoubleToComplex fx = getComponent(Axis.X).toDC();
                Complex _x = fx.computeComplex(x, y, z);
                return _x.toVector();
            }
            case 2: {
                DoubleToComplex fx = getComponent(Axis.X).toDC();
                DoubleToComplex fy = getComponent(Axis.Y).toDC();
                Complex _x = fx.computeComplex(x, y, z);
                Complex _y = fy.computeComplex(x, y, z);
                return MathsBase.columnVector(_x, _y);
            }
            case 3: {
                DoubleToComplex fx = getComponent(Axis.X).toDC();
                DoubleToComplex fy = getComponent(Axis.Y).toDC();
                DoubleToComplex fz = getComponent(Axis.Z).toDC();
                Complex _x = fx.computeComplex(x, y, z);
                Complex _y = fy.computeComplex(x, y, z);
                Complex _z = fz.computeComplex(x, y, z);
                return MathsBase.columnVector(_x, _y, _z);
            }
        }
        throw new UnsupportedComponentDimensionException(getComponentDimension());
    }

    @Override
    public ComplexVector[][][] computeVector(double[] x, double[] y, double[] z, Domain d0, Out<Range> ranges) {
        switch (getComponentSize()) {
            case 1: {
                Out<Range> _xr = new Out<Range>();
                Complex[][][] _x = getComponent(Axis.X).toDC().computeComplex(x, y, z, d0, _xr);
                ComplexVector[][][] r = new ComplexVector[z.length][y.length][x.length];
                for (int t = 0; t < z.length; t++) {
                    for (int i = 0; i < y.length; i++) {
                        for (int j = 0; j < x.length; j++) {
                            r[t][i][j] = MathsBase.columnVector(
                                            _x[t][i][j]
                                    );
                        }
                    }
                }
                if (ranges != null) {
                    ranges.set(_xr.get());
                }
                return r;
            }
            case 2: {
                Out<Range> _xr = new Out<Range>();
                Out<Range> _yr = new Out<Range>();
                Complex[][][] _x = getComponent(Axis.X).toDC().computeComplex(x, y, z, d0, _xr);
                Complex[][][] _y = getComponent(Axis.Y).toDC().computeComplex(x, y, z, d0, _yr);
                ComplexVector[][][] r = new ComplexVector[z.length][y.length][x.length];
                for (int t = 0; t < z.length; t++) {
                    for (int i = 0; i < y.length; i++) {
                        for (int j = 0; j < x.length; j++) {
                            r[t][i][j] = MathsBase.columnVector(
                                            _x[t][i][j],
                                            _y[t][i][j]
                                    );
                        }
                    }
                }
                if (ranges != null) {

                    Range rangex = _xr.get();
                    Range rangey = _yr.get();
                    ranges.set(rangex == null ? rangey : rangey == null ? rangex : rangex.union(rangey));
                }
                return r;
            }
            case 3: {
                Out<Range> _xr = new Out<Range>();
                Out<Range> _yr = new Out<Range>();
                Out<Range> _zr = new Out<Range>();
                Complex[][][] _x = getComponent(Axis.X).toDC().computeComplex(x, y, z, d0, _xr);
                Complex[][][] _y = getComponent(Axis.Y).toDC().computeComplex(x, y, z, d0, _yr);
                Complex[][][] _z = getComponent(Axis.Z).toDC().computeComplex(x, y, z, d0, _zr);
                ComplexVector[][][] r = new ComplexVector[z.length][y.length][x.length];
                for (int t = 0; t < z.length; t++) {
                    for (int i = 0; i < y.length; i++) {
                        for (int j = 0; j < x.length; j++) {
                            r[t][i][j] = MathsBase.columnVector(
                                            _x[t][i][j],
                                            _y[t][i][j],
                                            _z[t][i][j]
                                    );
                        }
                    }
                }
                if (ranges != null) {
                    Range rangex = _xr.get();
                    Range rangey = _yr.get();
                    Range rangez = _zr.get();
                    rangex = rangex == null ? rangey : rangey == null ? rangex : rangex.union(rangey);
                    rangex = rangex == null ? rangez : rangez == null ? rangex : rangex.union(rangez);
                    ranges.set(rangex);
                }
                return r;
            }
        }
        throw new UnsupportedComponentDimensionException(getComponentDimension());
    }

    @Override
    public ComplexVector[] computeVector(double[] x, Domain d0, Out<Range> ranges) {
        switch (getComponentSize()) {
            case 1: {
                Out<Range> _xr = new Out<Range>();
                Complex[] _x = getComponent(Axis.X).toDC().computeComplex(x, d0, _xr);
                ComplexVector[] r = new ComplexVector[x.length];
                for (int j = 0; j < x.length; j++) {
                    r[j] = MathsBase.columnVector(_x[j]);
                }
                if (ranges != null) {
                    ranges.set(_xr.get());
                }
                return r;
            }
            case 2: {
                Out<Range> _xr = new Out<Range>();
                Out<Range> _yr = new Out<Range>();
                Complex[] _x = getComponent(Axis.X).toDC().computeComplex(x, d0, _xr);
                Complex[] _y = getComponent(Axis.Y).toDC().computeComplex(x, d0, _yr);
                ComplexVector[] r = new ComplexVector[x.length];
                for (int j = 0; j < x.length; j++) {
                    r[j] = MathsBase.columnVector(_x[j], _y[j]);
                }
                if (ranges != null) {

                    Range rangex = _xr.get();
                    Range rangey = _yr.get();
                    ranges.set(rangex == null ? rangey : rangey == null ? rangex : rangex.union(rangey));
                }
                return r;
            }
            case 3: {
                Out<Range> _xr = new Out<Range>();
                Out<Range> _yr = new Out<Range>();
                Out<Range> _zr = new Out<Range>();
                Complex[] _x = getComponent(Axis.X).toDC().computeComplex(x, d0, _xr);
                Complex[] _y = getComponent(Axis.Y).toDC().computeComplex(x, d0, _yr);
                Complex[] _z = getComponent(Axis.Z).toDC().computeComplex(x, d0, _zr);
                ComplexVector[] r = new ComplexVector[x.length];
                for (int j = 0; j < x.length; j++) {
                    r[j] = MathsBase.columnVector(_x[j], _y[j], _z[j]);
                }
                if (ranges != null) {
                    Range rangex = _xr.get();
                    Range rangey = _yr.get();
                    Range rangez = _zr.get();
                    rangex = rangex == null ? rangey : rangey == null ? rangex : rangex.union(rangey);
                    rangex = rangex == null ? rangez : rangez == null ? rangex : rangex.union(rangez);
                    ranges.set(rangex);
                }
                return r;
            }
        }
        throw new UnsupportedComponentDimensionException(getComponentDimension());
    }

    public boolean isNaNImpl() {
        int cs = getComponentSize();
        Axis[] axisValues = Axis.values();
        for (int i = 0; i < cs; i++) {
            if (getComponent(axisValues[i]).isNaN()) {
                return true;
            }
        }
        return false;
    }

    public boolean isInfiniteImpl() {
        int cs = getComponentSize();
        Axis[] axisValues = Axis.values();
        for (int i = 0; i < cs; i++) {
            if (getComponent(axisValues[i]).isInfinite()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isZeroImpl() {
        int cs = getComponentSize();
        Axis[] axisValues = Axis.values();
        for (int i = 0; i < cs; i++) {
            if (!getComponent(axisValues[i]).isZero()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isDoubleExprImpl() {
        for (int i = 0; i < getComponentDimension().rows; i++) {
            if (!getComponent(Axis.values()[i]).isDoubleExpr()) {
                return false;
            }
        }
        return true;
    }

    public ComplexVector[][] computeVector(double[] x, double[] y, Domain d0, Out<Range> ranges) {
        switch (getComponentSize()) {
            case 1: {
                Out<Range> _xr = new Out<Range>();
                Complex[][] _x = getComponent(Axis.X).toDC().computeComplex(x, y, d0, _xr);
                ExpressionsDebug.debug_check(_x,_xr);
                ComplexVector[][] r = new ComplexVector[y.length][x.length];
                for (int i = 0; i < y.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        r[i][j] = MathsBase.columnVector(
                                        _x[i][j]
                                );
                    }
                }
                if (ranges != null) {
                    ranges.set(_xr.get());
                }
                return r;
            }
            case 2: {
                Out<Range> _xr = new Out<Range>();
                Out<Range> _yr = new Out<Range>();
                Complex[][] _x = getComponent(Axis.X).toDC().computeComplex(x, y, d0, _xr);
                ExpressionsDebug.debug_check(_x,_xr);
                Complex[][] _y = getComponent(Axis.Y).toDC().computeComplex(x, y, d0, _yr);
                ExpressionsDebug.debug_check(_y,_yr);
                ComplexVector[][] r = new ComplexVector[y.length][x.length];
                for (int i = 0; i < y.length; i++) {
                    for (int j = 0; j < x.length; j++) {
                        r[i][j] = MathsBase.columnVector(
                                        _x[i][j],
                                        _y[i][j]
                                );
                    }
                }
                if (ranges != null) {

                    Range rangex = _xr.get();
                    Range rangey = _yr.get();
                    ranges.set(rangex == null ? rangey : rangey == null ? rangex : rangex.union(rangey));
                }
                return r;
            }
        }
        Out<Range> _xr = new Out<Range>();
        Out<Range> _yr = new Out<Range>();
        Out<Range> _zr = new Out<Range>();
        Complex[][] _x = getComponent(Axis.X).toDC().computeComplex(x, y, d0, _xr);
        ExpressionsDebug.debug_check(_x,_xr);
        Complex[][] _y = getComponent(Axis.Y).toDC().computeComplex(x, y, d0, _yr);
        ExpressionsDebug.debug_check(_y,_yr);
        Complex[][] _z = getComponent(Axis.Z).toDC().computeComplex(x, y, d0, _zr);
        ExpressionsDebug.debug_check(_z,_zr);
        ComplexVector[][] r = new ComplexVector[y.length][x.length];
        for (int i = 0; i < y.length; i++) {
            for (int j = 0; j < x.length; j++) {
                r[i][j] = MathsBase.columnVector(
                        _x[i][j],
                        _y[i][j],
                        _z[i][j]);
            }
        }
        if (ranges != null) {
            Range rangex = _xr.get();
            Range rangey = _yr.get();
            Range rangez = _zr.get();
            rangex = rangex == null ? rangey : rangey == null ? rangex : rangex.union(rangey);
            rangex = rangex == null ? rangez : rangez == null ? rangex : rangex.union(rangez);
            ranges.set(rangex);
        }
        return r;
    }

}
