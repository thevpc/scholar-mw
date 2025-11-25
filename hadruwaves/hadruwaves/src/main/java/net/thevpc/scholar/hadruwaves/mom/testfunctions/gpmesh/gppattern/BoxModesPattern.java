package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern;


import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.ModeInfo;
import net.thevpc.scholar.hadruwaves.ModeType;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.mom.*;
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.sources.modal.CutOffModalSources;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 */
public final class BoxModesPattern implements RectangularGpPattern {
    public static final double EPS = 1E-3;
    private static final long serialVersionUID = 1L;
    //TODO this is the last serialVersionUID
    private int maxLoopCount = 10;
    private TreeSet<Integer> includedFunctions;
    private TreeSet<Integer> excludedFunctions;
    private int complexity;
    private boolean keepNormalization = true;
    private boolean keepAllModes = true;
    private Axis axisInvariance = null;
    private boolean inheritInvariance = true;
    private boolean inheritSymmetry = true;
    private AxisXY axisSymmetry = null;

    public BoxModesPattern(int complexity, Axis axisInvariance) {
        this(complexity, false, axisInvariance, null, null);
    }

    public BoxModesPattern(int complexity) {
        this(complexity, true, null, null, null);
    }

    public BoxModesPattern(int complexity, Axis axisInvariance, boolean include, int... includedExcludedFunctions) {
        this(complexity, false, axisInvariance, include ? includedExcludedFunctions : null, include ? null : includedExcludedFunctions);
    }

    public BoxModesPattern(int complexity, boolean include, int... includedExcludedFunctions) {
        this(complexity, true, null, include ? includedExcludedFunctions : null, include ? null : includedExcludedFunctions);
    }

    public BoxModesPattern(int complexity, Axis axisInvariance, int[] includedFunctions, int[] excludedFunctions) {
        this(complexity, false, axisInvariance, includedFunctions, excludedFunctions);
    }

    public BoxModesPattern(int complexity, int[] includedFunctions, int[] excludedFunctions) {
        this(complexity, true, null, includedFunctions, excludedFunctions);
    }

    public BoxModesPattern(int complexity, boolean inheritInvariance, Axis axisInvariance, int[] includedFunctions, int[] excludedFunctions) {
        if (complexity <= 0) {
            throw new IllegalArgumentException();
        }
        this.complexity = complexity;
        this.axisInvariance = axisInvariance;
        this.inheritInvariance = inheritInvariance;
        if (includedFunctions != null && includedFunctions.length > 0) {
            this.includedFunctions = new TreeSet<Integer>();
            for (int i : includedFunctions) {
                this.includedFunctions.add(i);
            }
        }
        this.excludedFunctions = null;
        if (excludedFunctions != null && excludedFunctions.length > 0) {
            this.excludedFunctions = new TreeSet<Integer>();
            for (int i : excludedFunctions) {
                this.excludedFunctions.add(i);
            }
        }
        //includedFunctions==null?null:new TreeSet<Integer>(Arrays.asList(new Integer[0]));
        //this.excludedFunctions = excludedFunctions;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = NElement.ofObjectBuilder(getClass().getSimpleName());
        h.add("complexity", NElementHelper.elem(complexity));
        h.add("axisInvariance", NElementHelper.elem(axisInvariance));
        h.add("inheritInvariance", NElementHelper.elem(inheritInvariance));
        h.add("inheritSymmetry", NElementHelper.elem(inheritSymmetry));
        h.add("axisSymmetry", NElementHelper.elem(axisSymmetry));
        h.add("keepNormalization", NElementHelper.elem(keepNormalization));
        h.add("keepAllModes", NElementHelper.elem(keepAllModes));
        h.add("maxLoopCount", NElementHelper.elem(maxLoopCount));
        h.add("includedFunctions", NElementHelper.elem(includedFunctions));
        h.add("excludedFunctions", NElementHelper.elem(excludedFunctions));
        return h.build();
    }

    public static WallBorders getVirtualWalls(MeshZone zone, Domain globalDomain, MomStructure str) {
        return getVirtualWalls(zone.getDomain(), globalDomain, str.getCircuitType(), str.getBorders());
    }

    public static WallBorders getVirtualWalls(Domain d, Domain globalDomain, CircuitType circuit, WallBorders b) {
        boolean westReached = Math.abs(d.xmin() - globalDomain.xmin()) / globalDomain.xwidth() < EPS;
        boolean eastReached = Math.abs(d.xmax() - globalDomain.xmax()) / globalDomain.xwidth() < EPS;
        boolean northReached = Math.abs(d.ymin() - globalDomain.ymin()) / globalDomain.ywidth() < EPS;
        boolean southReached = Math.abs(d.ymax() - globalDomain.ymax()) / globalDomain.ywidth() < EPS;
        Boundary eastWall = eastReached ? b.getEast() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary westWall = westReached ? b.getWest() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary northWall = northReached ? b.getNorth() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary southWall = southReached ? b.getSouth() : CircuitType.SERIAL.equals(circuit) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        return WallBorders.valueOf(northWall, eastWall, southWall, westWall);
    }

    public boolean isKeepNormalization() {
        return keepNormalization;
    }

    public BoxModesPattern setKeepNormalization(boolean keepNormalization) {
        this.keepNormalization = keepNormalization;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + complexity + ((includedFunctions == null) ? "" : "+" + includedFunctions) + ((excludedFunctions == null) ? "" : "-" + excludedFunctions);
    }

    public DoubleToVector[] createFunctions(Domain globalDomain, MeshZone zone, ProgressMonitor monitor, MomStructure str) {
        int max = getCount();
        ArrayList<DoubleToVector> all = new ArrayList<DoubleToVector>();
        Domain d = zone.getDomain();
        boolean westReached = Math.abs(d.xmin() - globalDomain.xmin()) / globalDomain.xwidth() < EPS;
        boolean eastReached = Math.abs(d.xmax() - globalDomain.xmax()) / globalDomain.xwidth() < EPS;
        boolean northReached = Math.abs(d.ymin() - globalDomain.ymin()) / globalDomain.xwidth() < EPS;
        boolean southReached = Math.abs(d.ymax() - globalDomain.ymax()) / globalDomain.xwidth() < EPS;

        CircuitType circuitType = CircuitType.SERIAL;
        WallBorders b = WallBorders.EEEE;

        if (str != null) {
            circuitType = str.getCircuitType();
            b = str.getBorders();
        }
        Boundary eastWall = eastReached ? b.getEast() : CircuitType.SERIAL.equals(circuitType) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary westWall = westReached ? b.getWest() : CircuitType.SERIAL.equals(circuitType) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary northWall = northReached ? b.getNorth() : CircuitType.SERIAL.equals(circuitType) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        Boundary southWall = southReached ? b.getSouth() : CircuitType.SERIAL.equals(circuitType) ? Boundary.MAGNETIC : Boundary.ELECTRIC;
        BoxModeFunctions ff= new BoxModeFunctions();
        DefaultModeFunctionsEnv env=new DefaultModeFunctionsEnv();
        ff.setEnv(env);
        env.setBorders(WallBorders.of(northWall, eastWall, southWall, westWall));
        if (Boundary.PERIODIC.equals(eastWall) && Boundary.PERIODIC.equals(westWall) && Boundary.PERIODIC.equals(northWall) && Boundary.PERIODIC.equals(southWall)) {
            ff.setPolarization(Axis.Y);
        }
        if (ff == null) {
            throw new IllegalArgumentException("Unsupported " + northWall + "," + eastWall + "," + southWall + "," + westWall);
        }
        env.setDomain(d);
        env.setFrequency(str.getFrequency());
//        ff.setProjectType(str.getProjectType());
        env.setFirstBoxSpace(str.getFirstBoxSpace());
        env.setSecondBoxSpace(str.getSecondBoxSpace());
        if (inheritInvariance) {
            ff.setHintInvariance(str.getHintsManager().getHintInvariance());
        } else {
            ff.setHintInvariance(axisInvariance);
        }

        if (inheritSymmetry) {
            ff.setHintSymmetry(str.getHintsManager().getHintSymmetry());
        } else {
            ff.setHintSymmetry(axisSymmetry);
        }

        int fnMax = complexity * 3;
        int fnMaxStep = complexity;
        int maxLoops = maxLoopCount;
        env.setSources(new CutOffModalSources(1));
        ff.setHintAxisType(str.getHintsManager().getHintAxisType());
        if (!keepAllModes) {
            ModeType[] allowedModes = ff.getAllowedModes();
            boolean te = false;
            boolean tm = false;
            boolean tem = false;
            ModeType first = null;
            for (ModeType allowedMode : allowedModes) {
                switch (allowedMode) {
                    case TE: {
                        te = true;
                        if (first == null) {
                            first = allowedMode;
                        }
                        break;
                    }
                    case TM: {
                        tm = true;
                        if (first == null) {
                            first = allowedMode;
                        }
                        break;
                    }
                    case TEM: {
                        tem = true;
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(getClass().getSimpleName() + " : Unsupported " + allowedMode);
                    }
                }
            }
            if (te && tm) {
                if (tem) {
                    ff.setHintFnModes(ModeType.TEM, first);
                } else {
                    ff.setHintFnModes(first);
                }
            }
        }
        while (maxLoops > 0) {
            all.clear();
            maxLoops--;
            ff.setSize(fnMax);
            ModeInfo[] fnIndexes = ff.getModes(monitor);
            for (int index = 0; (index < fnIndexes.length && index < complexity); index++) {
                if (includedFunctions != null && !includedFunctions.contains(index)) {
                    continue;
                }
                if (excludedFunctions != null && excludedFunctions.contains(index)) {
                    continue;
                }
                ModeInfo modeInfo = fnIndexes[index];
                DoubleToVector d1 = modeInfo.fn;
                DoubleToComplex fx = d1.getComponent(Axis.X).toDC();
                DoubleToComplex fy = d1.getComponent(Axis.Y).toDC();
                boolean ok = false;
                if (!isKeepNormalization()) {
                    if (fx.getRealDD() instanceof CosXCosY && fx.getImagDD().isZero()) {
                        ok = true;
                        CosXCosY f = (CosXCosY) fx.getRealDD();
                        fx = Maths.complex(new CosXCosY(1, f.getA(), f.getB(), f.getC(), f.getD(), f.getDomain()));
                    }
                    if (fy.getRealDD() instanceof CosXCosY && fy.getImagDD().isZero()) {
                        ok = true;
                        CosXCosY f = (CosXCosY) fy.getRealDD();
                        fy = Maths.complex(new CosXCosY(1, f.getA(), f.getB(), f.getC(), f.getD(), f.getDomain()));
                    }
                    if (ok) {
                        DoubleToVector cfv2d =  Maths.vector(fx, fy).setProperties(d1.getProperties()).toDV();
                        d1 = cfv2d;
                    }
                }
                final Axis axisIndependent1 = getAxisInvariance();
                if (axisIndependent1 != null && !HintAxisType.XY.equals(str.getHintsManager().getHintAxisType())) {
                    //si non XY_COUPLED alors separer les variables
                    if (!d1.getComponent(Axis.X).isZero() && (axisIndependent1 == null || d1.getComponent(Axis.X).isInvariant(axisIndependent1))) {
                        Expr f = Maths.vector(d1.getComponent(Axis.X), Maths.CZEROXY);
                        f = f.setTitle(d1.getTitle())
                                .setMergedProperties(d1.getProperties())
                                .setProperty("Borders.E", env.getBorders().getExDescription() + "," + env.getBorders().getEyDescription())
                                .setProperty("Borders.J", env.getBorders().getJxDescription() + "," + env.getBorders().getJyDescription()).toDV();
                        all.add(f.toDV());
//                        System.out.println(">>OK "+axisIndependent1+" invariant d1.fx " + d1.fx);
                    } else {
//                        System.out.println("not "+axisIndependent1+" invariant d1.fx " + d1.fx);
                    }
                    if (!d1.getComponent(Axis.Y).isZero() && (axisIndependent1 == null || d1.getComponent(Axis.Y).isInvariant(axisIndependent1))) {
                        Expr f = Maths.vector(Maths.CZEROXY, d1.getComponent(Axis.Y));
                        f = f.setTitle(d1.getTitle())
                                .setMergedProperties(d1.getProperties())
                                .setProperty("Borders.E", env.getBorders().getExDescription() + "," + env.getBorders().getEyDescription())
                                .setProperty("Borders.J", env.getBorders().getJxDescription() + "," + env.getBorders().getJyDescription()).toDV();
                        all.add(f.toDV());
                        //System.out.println(">>> OK   :"+(""+d1.getProperties().get("Mode")+"["+d1.getProperties().get("m")+","+d1.getProperties().get("n")+"]  ")+"is "+axisIndependent1+" invariant d1.fy " + d1.fx);
                    } else {
                        //System.out.println((""+d1.getProperties().get("Mode")+"["+d1.getProperties().get("m")+","+d1.getProperties().get("n")+"]  ")+"not "+axisIndependent1+" invariant d1.fy " + d1.fx);
                    }
                } else {
                    switch (str.getHintsManager().getHintAxisType()) {
                        case Y_ONLY: {
                            if (!d1.getComponent(Axis.Y).isZero()) {
                                d1= d1.setProperty("Borders.E", env.getBorders().getExDescription() + "," + env.getBorders().getEyDescription()).toDV();
                                d1= d1.setProperty("Borders.J", env.getBorders().getJxDescription() + "," + env.getBorders().getJyDescription()).toDV();
                                all.add(d1);
                            }
                            break;
                        }
                        case X_ONLY: {
                            if (!d1.getComponent(Axis.X).isZero()) {
                                d1= d1.setProperty("Borders.E", env.getBorders().getExDescription() + "," + env.getBorders().getEyDescription()).toDV();
                                d1= d1.setProperty("Borders.J", env.getBorders().getJxDescription() + "," + env.getBorders().getJyDescription()).toDV();
                                all.add(d1);
                            }
                            break;
                        }
                        default: {
                            d1= d1.setProperty("Borders.E", env.getBorders().getExDescription() + "," + env.getBorders().getEyDescription()).toDV();
                            d1= d1.setProperty("Borders.J", env.getBorders().getJxDescription() + "," + env.getBorders().getJyDescription()).toDV();
                            all.add(d1);
                            break;
                        }
                    }
                }
            }
            while (all.size() > complexity) {
                all.remove(all.size() - 1);
            }
            if (all.size() == complexity) {
                return all.toArray(new DoubleToVector[all.size()]);
            }
            System.out.println("maxLoops = " + maxLoops + "; found only " + all.size() + " for " + fnMax + " (complexity=" + complexity + ")");
            //System.out.println("got only "+all.size()+" gps for complexity "+fnMax+". try to add "+fnMaxStep);
            fnMax += fnMaxStep;
        }
        throw new IllegalArgumentException("Unable to find Gp Functions. Symmetry or invariance you may have used is perhaps invalid!");
    }

    public int getCount() {
        return complexity;
    }

    public List<MeshZone> transform(List<MeshZone> zones, Domain globalBounds) {
        return zones;
    }

    public Axis getAxisInvariance() {
        return axisInvariance;
    }

    public BoxModesPattern setAxisIndependent(Axis axisIndependent) {
        this.axisInvariance = axisIndependent;
        return this;
    }

    public boolean isKeepAllModes() {
        return keepAllModes;
    }

    public BoxModesPattern setKeepAllModes(boolean keepAllModes) {
        this.keepAllModes = keepAllModes;
        return this;
    }

    public boolean isInheritSymmetry() {
        return inheritSymmetry;
    }

    public void setInheritSymmetry(boolean inheritSymmetry) {
        this.inheritSymmetry = inheritSymmetry;
    }

    public boolean isInheritInvariance() {
        return inheritInvariance;
    }

    public void setInheritInvariance(boolean inheritInvariance) {
        this.inheritInvariance = inheritInvariance;
    }

    public AxisXY getAxisSymmetry() {
        return axisSymmetry;
    }

    public void setAxisSymmetry(AxisXY axisSymmetry) {
        this.axisSymmetry = axisSymmetry;
    }
}
