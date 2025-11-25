package net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.thevpc.nuts.elem.NElement;


import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.common.collections.MapUtils;
import net.thevpc.scholar.hadrumaths.geom.*;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.DefaultDoubleToVector;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.TestFunctionsBase;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;

import java.util.*;

import net.thevpc.scholar.hadrumaths.Axis;

import static net.thevpc.scholar.hadrumaths.Expressions.xsymmetric;
import static net.thevpc.scholar.hadrumaths.Expressions.xtranslated;
import static net.thevpc.scholar.hadrumaths.Expressions.ysymmetric;
import static net.thevpc.scholar.hadrumaths.Expressions.ytranslated;

import net.thevpc.scholar.hadruwaves.mom.GpPatternFactory;
import net.thevpc.scholar.hadruwaves.mom.project.MomStructureAware;

public class GpAdaptiveMesh extends TestFunctionsBase implements Cloneable {

    public static final MeshAlgo MESH_RECT = new MeshAlgoRect();
    private GeometryList[] polygons;
    private TestFunctionsSymmetry symmetry = TestFunctionsSymmetry.NO_SYMMETRY;
    private GpPattern pattern;
    private Axis invariance;
    private String name;
    private MeshAlgo meshAlgo = MESH_RECT.clone();
    private Domain domain;
    private CircuitType circuitType;

    public GpAdaptiveMesh(GeometryList polygonsSerial, GpPattern pattern, MeshAlgo meshAlgo) {
        this(polygonsSerial, polygonsSerial, pattern, meshAlgo);
    }

    @Deprecated
    public GpAdaptiveMesh(GeometryList polygonsSerial, GeometryList polygonsParallel, GpPattern pattern, MeshAlgo meshAlgo) {
        this(polygonsSerial, polygonsParallel, pattern, TestFunctionsSymmetry.NO_SYMMETRY, meshAlgo,null,null);
    }

    public GpAdaptiveMesh(GeometryList polygonsSerial, GpPattern pattern, TestFunctionsSymmetry symmetry, MeshAlgo meshAlgo) {
        this(polygonsSerial, polygonsSerial.getDual() == null ? polygonsSerial : polygonsSerial.getDual(), pattern, symmetry, meshAlgo,null,null);
    }

    public GpAdaptiveMesh(GeometryList polygonsSerial, GpPattern pattern, TestFunctionsSymmetry symmetry, MeshAlgo meshAlgo, Domain domain, CircuitType circuitType) {
        this(polygonsSerial, polygonsSerial.getDual() == null ? polygonsSerial : polygonsSerial.getDual(), pattern, symmetry, meshAlgo,domain,circuitType);
    }

    @Deprecated
    public GpAdaptiveMesh(GeometryList polygonsSerial, GeometryList polygonsParallel, GpPattern pattern, TestFunctionsSymmetry symmetry, MeshAlgo meshAlgo, Domain domain, CircuitType circuitType) {
        super();
        this.polygons = new GeometryList[]{polygonsSerial.clone(), polygonsParallel == null ? polygonsSerial.clone() : polygonsParallel.clone()};
        this.domain=domain;
        this.circuitType=circuitType;
        setPattern(pattern);
        setMeshAlgo(meshAlgo);
        setSymmetry(symmetry);
    }

    @Override
    public DoubleToVector[] gpImpl(ProgressMonitor monitor) {
        ArrayList<DoubleToVector> f = new ArrayList<DoubleToVector>();
        MomStructure currentStructure = getStructure();
        Domain globalDomain = domain!=null? domain: currentStructure ==null?null: currentStructure.getDomain();
        CircuitType circuitType1 = circuitType!=null ?circuitType: currentStructure ==null?null: currentStructure.getCircuitType();
        //default circuit!
        if(circuitType1==null){
            circuitType1=CircuitType.SERIAL;
        }

        GeometryList geometryList = polygons[circuitType1.ordinal()];
        Domain polygonDomain = geometryList.getBounds();
//        if(globalDomain==null){
//            globalDomain=globalBounds;
//        }
        List<MeshZone> allZonesInit = new ArrayList<MeshZone>();
        for (Geometry polygon : geometryList) {
            allZonesInit.addAll(meshAlgo.meshPolygon(polygon));
        }
        GpPattern currentPattern = getPattern();
        //if (allZonesInit.size() == 0) {
//            Plot2.show("test", geometryList, getMeshAlgo(), currentPattern,(polygonDomain==null?globalDomain:polygonDomain));
        //}
//        for (MeshZone zone : allZonesInit) {
//            AreaComponent.showDialog("allZonesInit",zone.getGeometry().scale(400,400));
//        }
        allZonesInit = currentPattern.transform(allZonesInit, polygonDomain==null?globalDomain:polygonDomain);
        ArrayList<MeshZone> allZones = new ArrayList<MeshZone>();
        for (MeshZone zone : allZonesInit) {
            zone.setDomainRelative(polygonDomain==null?globalDomain:polygonDomain, globalDomain);
            allZones.add(zone);
        }
        Collections.sort(allZones, MeshZone.ZONES_COMPARATOR);
        int partCounter = 0;
//        for (MeshZone zone : allZones) {
//            AreaComponent.showDialog(zone.getGeometry().scale(400,400));
//        }
        for (MeshZone zone : allZones) {
            DoubleToVector[] allGpFunctions = currentPattern.createFunctions(globalDomain, zone, monitor, currentStructure);
            int goodCount = allGpFunctions.length;
            partCounter++;
            switch (getSymmetry()) {
                case NO_SYMMETRY: {
                    for (int i = 0; i < goodCount; i++) {
                        DoubleToVector fct = allGpFunctions[i];
                        if (fct == null) {

                        }else if(fct.isZero()) {
                            System.out.println("Ignored null or zero");
                        }else  {
                            if (invariance == null || fct.isInvariant(invariance)) {
                                fct= fct.setProperty("Cell", partCounter).toDV();
                                f.add(fct);
                            }
                        }
                    }
                    break;
                }
                case EACH_X_SYMMETRY: {
                    for (int i = 0; i < goodCount; i++) {
                        DoubleToVector fmotif = allGpFunctions[i];
                        if (fmotif != null) {
                            if (zone.getDomain().xmax() <= globalDomain.getCenterX()) {
                                DoubleToVector fct = fmotif;
                                if (invariance == null || fct.isInvariant(invariance)) {
                                    fct= fct.setProperty("Cell", partCounter).toDV();
                                    f.add(fct);
                                }
                            } else {
                                DoubleToVector fct = xsymmetric(fmotif);
                                if (invariance == null || fct.isInvariant(invariance)) {
                                    fct= fct.setProperty("Cell", partCounter + " [Symmetric]").toDV();
                                    f.add(fct);
                                }
                            }
                        }
                    }
                    break;
                }
                case EACH_Y_SYMMETRY: {
                    for (int i = 0; i < goodCount; i++) {
                        DoubleToVector fmotif = allGpFunctions[i];
                        if (fmotif != null) {
                            if (zone.getDomain().ymax() <= globalDomain.getCenterY()) {
                                DoubleToVector fct = fmotif;
                                if (invariance == null || fct.isInvariant(invariance)) {
                                    fct= fct.setProperty("Cell", partCounter).toDV();
                                    f.add(fct);
                                }
                            } else {
                                DoubleToVector fct = ysymmetric(fmotif);
                                if (invariance == null || fct.isInvariant(invariance)) {
                                    fct= fct.setProperty("Cell", partCounter + " [Symmetric]").toDV();
                                    f.add(fct);
                                }
                            }
                        }
                    }
                    break;
                }
                case SUM_X_SYMMETRY: {
                    if (zone.getDomain().xmin() < globalDomain.getCenterX()) {
                        for (int i = 0; i < goodCount; i++) {
                            DoubleToVector c = allGpFunctions[i];
                            if (c != null) {
                                if (zone.getDomain().xmax() <= globalDomain.getCenterX()) {
                                    Domain symDomain = zone.getDomain().getSymmetricX(globalDomain.getCenterX());
                                    DoubleToVector c2 = xtranslated(c, symDomain.xmin() - zone.getDomain().xmin());
                                    c2 = xsymmetric(c2);
                                    if (c2 != null) {
                                        DoubleToVector fct = DefaultDoubleToVector.add(c, c2);
                                        if (invariance == null || fct.isInvariant(invariance)) {
                                            fct= fct.setProperty("Cell", partCounter).toDV();
                                            f.add(fct);
                                        }
                                    }
                                } else {
                                    DoubleToVector fct = allGpFunctions[i];
                                    if (fct != null) {
                                        if (invariance == null || fct.isInvariant(invariance)) {
                                            fct= fct.setProperty("Cell", partCounter).toDV();
                                            f.add(fct);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
                case SUM_Y_SYMMETRY: {
//                    if(("("+0+","+3+")").equals(zone.getProperty("Attach For"))){
//                        System.out.println("goodCount = " + goodCount);
//                    }
                    if (zone.getDomain().ymin() < globalDomain.getCenterY()) {
                        for (int i = 0; i < goodCount; i++) {
                            DoubleToVector c = allGpFunctions[i];
                            if (c != null) {
                                if (zone.getDomain().ymin() <= globalDomain.getCenterY()) {
                                    Domain symDomain = zone.getDomain().getSymmetricY(globalDomain.getCenterY());
                                    DoubleToVector c2 = ytranslated(c, symDomain.ymin() - zone.getDomain().ymin());
                                    c2 = ysymmetric(c2);
                                    if (c2 != null) {
                                        DoubleToVector fct = DefaultDoubleToVector.add(c, c2);
                                        if (invariance == null || fct.isInvariant(invariance)) {

                                            fct= fct.setProperties(
                                                    MapUtils.<String,Object>map(
                                                            "Cell", partCounter,
                                                        "invarianceGp", (fct.isInvariant(Axis.X) ? "X" : "") + (fct.isInvariant(Axis.Y) ? "Y" : "")
                                                    )
                                            ).toDV();
                                            f.add(fct);
                                        }
                                    }
                                } else {
                                    DoubleToVector fct = allGpFunctions[i];
                                    if (fct != null) {
                                        if (invariance == null || fct.isInvariant(invariance)) {
                                            fct= fct.setProperty("Cell", partCounter).toDV();
                                            f.add(fct);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }

        }
        return f.toArray(new DoubleToVector[f.size()]);
    }

    public void setSymmetry(TestFunctionsSymmetry aSymmetry) {
        this.symmetry = aSymmetry == null ? TestFunctionsSymmetry.NO_SYMMETRY : aSymmetry;
        invalidateCache();
    }

    public Axis getInvariance() {
        return invariance;
    }

    public void setInvariance(Axis invariance) {
        this.invariance = invariance;
        invalidateCache();
    }

    
    public GeometryList getPolygons(CircuitType circuit) {
        return getPolygons()[circuit.ordinal()];
    }

    public GeometryList[] getPolygons() {
        //gp();
        return polygons;
    }

    @Override
    public void setStructure(MomStructure structure) {
        super.setStructure(structure);
        for (GeometryList geometryList : polygons) {
            if(geometryList instanceof FractalAreaGeometryList){
                ((FractalAreaGeometryList) geometryList).setLevel(structure.getFractalScale());
            }
            if(geometryList instanceof MomStructureAware){
                ((MomStructureAware) geometryList).setStructure(structure);
            }
        }
    }

    @Override
    protected DoubleToVector[] rebuildCachedFunctions(ProgressMonitor monitor) {
        for (GeometryList geometryList : polygons) {
            if (geometryList instanceof FractalAreaGeometryList) {
                ((FractalAreaGeometryList) geometryList).setLevel(getStructure().getFractalScale());
            }
        }
//        if (meshAlgo instanceof MeshAlgoRect && pattern instanceof RectMeshAttachGpPattern) {
//            MeshAlgoRect ma = (MeshAlgoRect) meshAlgo;
//            ma = ma.clone();
//            //ma.setAttatchX(((RectMeshAttachGpPattern) pattern).getFilter().accept(MeshZoneType.ATTACHX));
//            //ma.setAttatchY(((RectMeshAttachGpPattern) pattern).getFilter().accept(MeshZoneType.ATTACHY));
//            meshAlgo = ma;
//        }
        return super.rebuildCachedFunctions(monitor);
    }

    public void setMeshAlgo(MeshAlgo meshAlgo) {
        this.meshAlgo = meshAlgo == null ? MESH_RECT.clone() : meshAlgo.clone();
        invalidateCache();
    }

    public void setPattern(GpPattern pattern) {
        this.pattern = pattern == null ? GpPatternFactory.ECHELON : pattern;
        invalidateCache();
    }

    public MeshAlgo getMeshAlgo() {
        //gp();
        return meshAlgo;
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder h = super.toElement().toObject().get().builder();
        arr();
        for (CircuitType circuitType : CircuitType.values()) {
            h.add(circuitType.toString(), NElementHelper.elem(polygons[circuitType.ordinal()]));
        }
        h.add("symmetry", NElementHelper.elem(getSymmetry()));
        h.add("pattern", NElementHelper.elem(getPattern()));
        h.add("invariance", NElementHelper.elem(getInvariance()));
//        System.out.println("meshAlgo = " + meshAlgo.dump());
        h.add("meshAlgo", NElementHelper.elem(meshAlgo));
        return h.build();
    }


    @Override
    public String toString() {
        if (name != null) {
            return name;
        }
        return toLongString();
    }

    public String toLongString() {
        StringBuilder sb = new StringBuilder("{");
        for (CircuitType circuitType : CircuitType.values()) {
            if (circuitType.ordinal() > 0) {
                sb.append(",");
            }
            sb.append(circuitType.toString().charAt(0));
            sb.append(":");
            sb.append(polygons[circuitType.ordinal()]);
        }
        if (getSymmetry() != TestFunctionsSymmetry.NO_SYMMETRY) {
            sb.append(",").append(getSymmetry());
        }
        sb.append(",").append(pattern);
        sb.append(",").append(meshAlgo);
        sb.append("}");
        return sb.toString();
    }

    public TestFunctionsSymmetry getSymmetry() {
        //gp();
        return symmetry;
    }

    public GpPattern getPattern() {
        //gp();
        return pattern;
    }

    @Override
    public TestFunctions clone() {
        GpAdaptiveMesh c = (GpAdaptiveMesh) super.clone();
        c.polygons = new GeometryList[]{polygons[CircuitType.SERIAL.ordinal()].clone(), polygons[CircuitType.PARALLEL.ordinal()].clone()};
        c.meshAlgo = meshAlgo.clone();
        return c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Geometry[] getGeometries() {
        return polygons;
    }
}
