package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.nuts.util.NAssert;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.FunctionFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;
import net.thevpc.scholar.hadruwaves.mom.HintAxisType;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

/**
 *
 */
public final class UserSinePattern extends AbstractGpPatternPQ2 implements Cloneable {
    public static final double EPS = 1E-15;

    private CellBoundaries xboundaries;
    private CellBoundaries yboundaries;
    private boolean autoDetectBoundaries;


    public UserSinePattern(int xCount, int yCount) {
        this(xCount, yCount, null, null, true);
    }

    public UserSinePattern(int xCount, int yCount, CellBoundaries xboundaries, CellBoundaries yboundaries) {
        this(xCount, yCount, xboundaries, yboundaries, false);
    }

    public UserSinePattern(int xCount, int yCount, CellBoundaries xboundaries, CellBoundaries yboundaries, boolean autoDetectBoundaries) {
        super(HintAxisType.XY_SEPARATED, xCount, yCount);
        this.xboundaries = xboundaries;
        this.yboundaries = yboundaries;
        this.autoDetectBoundaries = autoDetectBoundaries;
        if (!autoDetectBoundaries) {
            if (xboundaries == null && xCount > 0) {
                throw new IllegalArgumentException("xboundaries cannot be null while xCount is greater than 0");
            }
            if (yboundaries == null && yCount > 0) {
                throw new IllegalArgumentException("yboundaries cannot be null while yCount is greater than 0");
            }
        }
    }

    @Override
    public DoubleToVector createFunction(int index, int p, int q, Axis axis, Domain d, Domain globalDomain, HintAxisType preferredAxisType, MomStructure str) {
        if (axis == Axis.X) {
            CellBoundaries xboundaries = this.xboundaries;
            if (autoDetectBoundaries && xboundaries == null) {
                xboundaries = findCellBoundaries(Axis.X, d, globalDomain, str);
            }
            CosXCosY fx = createFunction(xboundaries, p, q, d);
            double ax = Maths.scalarProduct(fx, fx);
            fx = (CosXCosY) fx.mul(1 / sqrt(ax), null);
            DoubleToVector f = Maths.vector(
                            fx,
                            Maths.ZERO
                    )
                    .setTitle("SineX_" + xboundaries + "_" + "(" + p + "," + q + "))")
                    .setProperty("Type", "SineX_" + xboundaries)
                    .setProperty("p", p)
                    .setProperty("q", q).toDV();
            return f;
        }
        if (axis == Axis.Y) {
            CellBoundaries yboundaries = this.yboundaries;
            if (autoDetectBoundaries && yboundaries == null) {
                yboundaries = findCellBoundaries(Axis.Y, d, globalDomain, str);
            }
            CosXCosY fy = createFunction(yboundaries, p, q, d);
            double ay = Maths.scalarProduct(fy, fy);
            fy = (CosXCosY) fy.mul(1 / sqrt(ay), null);
            DoubleToVector f = Maths.vector(
                            Maths.ZERO,
                            fy
                    )
                    .setTitle("SineY_" + yboundaries + "(" + p + "," + q + "))")
                    .setProperty("Type", "SineY_" + yboundaries)
                    .setProperty("p", p)
                    .setProperty("q", q).toDV();
//        f.setProperties(properties);
            return f;
        }
        return Maths.DV(Maths.ZERO);
    }

    public CellBoundaries findCellBoundaries(Axis axis, Domain d, Domain globalDomain, MomStructure str) {
        boolean westReached = Math.abs(d.xmin() - globalDomain.xmin()) < EPS;
        boolean eastReached = Math.abs(d.xmax() - globalDomain.xmax()) < EPS;
        boolean northReached = Math.abs(d.ymin() - globalDomain.ymin()) < EPS;
        boolean southReached = Math.abs(d.ymax() - globalDomain.ymax()) < EPS;
        CircuitType circuit = str.getCircuitType();
        ModeFunctions fn = str.modeFunctions();
        WallBorders b = str.getBorders();
        Boundary eastWall = eastReached ? b.getEast() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary westWall = westReached ? b.getWest() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary northWall = northReached ? b.getNorth() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary southWall = southReached ? b.getSouth() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        switch (axis) {
            case X: {
                boolean eastMax = Boundary.ELECTRIC.equals(eastWall);
                boolean westMax = Boundary.ELECTRIC.equals(westWall);
                boolean northMax = Boundary.MAGNETIC.equals(northWall);
                boolean southMax = Boundary.MAGNETIC.equals(southWall);
                return CellBoundaries.eval(eastMax, westMax, northMax, southMax);
            }
            case Y: {
                boolean eastMax = Boundary.MAGNETIC.equals(eastWall);
                boolean westMax = Boundary.MAGNETIC.equals(westWall);
                boolean northMax = Boundary.ELECTRIC.equals(northWall);
                boolean southMax = Boundary.ELECTRIC.equals(southWall);
                return CellBoundaries.eval(eastMax, westMax, northMax, southMax);
            }
        }
//        switch (str.getCircuitType()) {
//            case SERIAL: {//modeliser le courant
//                if (fn instanceof FnElectricXY) {
//                    return CellBoundaries.process(east, west, !north, !south);
//                } else if (fn instanceof FnEMEMXY) {
//                    return CellBoundaries.process(false, false, !north, !south);
//                } else if (fn instanceof FnMagneticXY) {
//                    return CellBoundaries.process(false, false, true, true);
//                } else if (fn instanceof FnPeriodicXY) {
//                    return CellBoundaries.process(east, west, !north, !south);
//                }
//            }
//            case PARALLEL: {
//                if (fn instanceof FnElectricXY) {
//                    return CellBoundaries.process(!east, !west, north, south);
//                } else if (fn instanceof FnEMEMXY) {
//                    return CellBoundaries.process(true, true, north, south);
//                } else if (fn instanceof FnMagneticXY) {
//                    return CellBoundaries.process(true, true, false, false);
//                } else if (fn instanceof FnPeriodicXY) {
//                    return CellBoundaries.process(!east, !east, north, south);
//                }
//            }
//        }
        throw new IllegalArgumentException("Not supported Fn Type : " + fn.getClass().getName());

    }

//    public static DoubleToVector createFunction(CellBoundaries xboundaries, CellBoundaries yboundaries, int index, int p, int q, Axis axis, Domain d, Domain globalDomain, HintAxisType preferredAxisType) {
//        if(xboundaries==null && yboundaries==null){
//            return Maths.DV(Maths.ZERO);
//        }
//        if(axis==Axis.X){
//            CosXCosY fx = createFunction(xboundaries, p, q, d);
//            double ax = Maths.scalarProduct(fx, fx);
//            fx = (CosXCosY) fx.mul(1 / sqrt(ax), null);
//            DoubleToVector f = Maths.vector(
//                            fx,
//                            Maths.ZERO
//                    )
//                    .setTitle("SineX_" + xboundaries + "_"  + "(" + p + "," + q + "))")
//                    .setProperty("Type", "SineX_" + xboundaries)
//                    .setProperty("p", p)
//                    .setProperty("q", q).toDV();
//            return f;
//        }
//        if(axis==Axis.Y){
//            CosXCosY fy = createFunction(yboundaries, p, q, d);
//            double ay = Maths.scalarProduct(fy, fy);
//            fy = (CosXCosY) fy.mul(1 / sqrt(ay), null);
//            DoubleToVector f = Maths.vector(
//                            Maths.ZERO,
//                            fy
//                    )
//                    .setTitle("SineY_" + yboundaries + "(" + p + "," + q + "))")
//                    .setProperty("Type", "SineY_" + yboundaries)
//                    .setProperty("p", p)
//                    .setProperty("q", q).toDV();
////        f.setProperties(properties);
//            return f;
//        }
//        return Maths.DV(Maths.ZERO);

    /// /        if(xboundaries==null){
    /// /            CosXCosY fy = createFunction(yboundaries, p, q, d);
    /// /            double ay = Maths.scalarProduct(fy, fy);
    /// /            fy = (CosXCosY) fy.mul(1 / sqrt(ay), null);
    /// /            DoubleToVector f = Maths.vector(
    /// /                            Maths.ZERO,
    /// /                            fy
    /// /                    )
    /// /                    .setTitle("SineY_" + yboundaries + "(" + p + "," + q + "))")
    /// /                    .setProperty("Type", "SineY_" + yboundaries)
    /// /                    .setProperty("p", p)
    /// /                    .setProperty("q", q).toDV();
    /// ///        f.setProperties(properties);
    /// /            return f;
    /// /        }
    /// /        if(yboundaries==null){
    /// /            CosXCosY fx = createFunction(xboundaries, p, q, d);
    /// /
    /// /
    /// /            double ax = Maths.scalarProduct(fx, fx);
    /// /            fx = (CosXCosY) fx.mul(1 / sqrt(ax), null);
    /// /            DoubleToVector f = Maths.vector(
    /// /                            fx,
    /// /                            Maths.ZERO
    /// /                    )
    /// /                    .setTitle("SineX_" + xboundaries + "_"  + "(" + p + "," + q + "))")
    /// /                    .setProperty("Type", "SineX_" + xboundaries)
    /// /                    .setProperty("p", p)
    /// /                    .setProperty("q", q).toDV();
    /// ///        f.setProperties(properties);
    /// /            return f;
    /// /        }
    /// /        CosXCosY fx = createFunction(xboundaries, p, q, d);
    /// /        CosXCosY fy = createFunction(yboundaries, p, q, d);
    /// /
    /// /
    /// /        double ax = Maths.scalarProduct(fx, fx);
    /// /        double ay = Maths.scalarProduct(fy, fy);
    /// /        fx = (CosXCosY) fx.mul(1 / sqrt(ax), null);
    /// /        fy = (CosXCosY) fy.mul(1 / sqrt(ay), null);
    /// /        DoubleToVector f = Maths.vector(
    /// /                fx,
    /// /                fy
    /// /        )
    /// /        .setTitle("Sine_" + xboundaries + "_" + yboundaries + "(" + p + "," + q + "))")
    /// /                .setProperty("Type", "Sine_" + xboundaries + "_" + yboundaries)
    /// /                .setProperty("p", p)
    /// /                .setProperty("q", q).toDV();
    /// ///        f.setProperties(properties);
    /// /        return f;
//    }

//    public DoubleToVector createFunction(int index, int p, int q, Domain d, Domain globalDomain, HintAxisType preferredAxisType, MomStructure str) {
//        return createFunction(xboundaries, yboundaries, index, p, q, d, globalDomain, preferredAxisType);
//    }
    public static CosXCosY createFunction(CellBoundaries boundaries, int p, int q, Domain d) {
        switch (boundaries) {
            //--UUy
            case UDxUUy: {
                return FunctionFactory.cosXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxUUy: {
                return FunctionFactory.sinXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxUUy: {
                return FunctionFactory.sinXcosY0(
                        1,
                        (p + 1) * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxUUy: {
                return FunctionFactory.cosXcosY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        q * PI / d.ywidth(),
                        0,
                        d
                );
            }
            //--DDy
            case UDxDDy: {
                return FunctionFactory.cosXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (q + 1) * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxDDy: {
                return FunctionFactory.sinXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (q + 1) * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxDDy: {
                return FunctionFactory.sinXsinY0(
                        1,
                        (p + 1) * PI / d.xwidth(),
                        0,
                        (q + 1) * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxDDy: {
                return FunctionFactory.cosXsinY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (q + 1) * PI / d.ywidth(),
                        0,
                        d
                );
            }
            //--UDy
            case UDxUDy: {
                return FunctionFactory.cosXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxUDy: {
                return FunctionFactory.sinXcosY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxUDy: {
                return FunctionFactory.sinXcosY0(
                        1,
                        (p + 1) * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxUDy: {
                return FunctionFactory.cosXcosY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            //--DUy
            case UDxDUy: {
                return FunctionFactory.cosXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DUxDUy: {
                return FunctionFactory.sinXsinY0(
                        1,// / Math.sqrt(d.width * d.height),
                        (2.0 * p + 1) / 2 * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case DDxDUy: {
                return FunctionFactory.sinXsinY0(
                        1,
                        (p + 1) * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }
            case UUxDUy: {
                return FunctionFactory.cosXsinY0(
                        1,
                        p * PI / d.xwidth(),
                        0,
                        (2.0 * q + 1) / 2 * PI / d.ywidth(),
                        0,
                        d
                );
            }

        }
        return null;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        h.add("x", NElementHelper.elem(xboundaries));
        h.add("y", NElementHelper.elem(yboundaries));
        return h.build();
    }

}
