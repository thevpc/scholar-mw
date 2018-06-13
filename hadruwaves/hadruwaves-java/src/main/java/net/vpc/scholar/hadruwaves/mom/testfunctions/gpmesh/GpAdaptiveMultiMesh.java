package net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh;

import net.vpc.common.util.mon.MonitoredAction;
import net.vpc.common.util.mon.ProgressMonitor;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expressions;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.dump.Dumper;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.geom.GeometryList;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.MeshZone;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.symbolic.DefaultDoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.common.util.MapUtils;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadruwaves.mom.testfunctions.TestFunctionCell;
import net.vpc.scholar.hadruwaves.mom.testfunctions.TestFunctionsBase;

import java.util.*;

import static net.vpc.scholar.hadrumaths.Expressions.*;

public class GpAdaptiveMultiMesh extends TestFunctionsBase implements Cloneable {

    public static final MeshAlgo MESH_RECT = new MeshAlgoRect();
    private String name;
    private TestFunctionCell[] cells;
    private Domain domain;

    public GpAdaptiveMultiMesh(Domain domain, TestFunctionCell... cells) {
        super();
        this.cells = cells;
        this.domain = domain;
    }

    @Override
    public DoubleToVector[] gpImpl(ProgressMonitor monitor) {
        return Maths.invokeMonitoredAction(monitor, "Gp Detection", new MonitoredAction<DoubleToVector[]>() {
            @Override
            public DoubleToVector[] process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                ArrayList<DoubleToVector> all = new ArrayList<DoubleToVector>();
                for (int i = 0; i < cells.length; i++) {
                    TestFunctionCell gpCell = cells[i];
                    all.addAll(gpImpl(gpCell, monitor));
                    monitor.setProgress(1.0 * i / cells.length, "Gp Detection");
                }
                return all.toArray(new DoubleToVector[all.size()]);
            }
        });
    }

    private Collection<DoubleToVector> gpImpl(TestFunctionCell cell, ProgressMonitor monitor) {
        ArrayList<DoubleToVector> f = new ArrayList<DoubleToVector>();
        Domain globalDomain = getStructure().getDomain();

        GeometryList geometryList = cell.getAreaGeometryList();
        Domain globalBounds = geometryList.getBounds();
        List<MeshZone> allZonesInit = new ArrayList<MeshZone>();
        for (Geometry polygon : geometryList) {
            allZonesInit.addAll(cell.getMeshAlgo().meshPolygon(polygon));
        }
        allZonesInit = cell.getPattern().transform(allZonesInit, globalBounds);
        ArrayList<MeshZone> allZones = new ArrayList<MeshZone>();
        for (MeshZone zone : allZonesInit) {
            zone.setDomainRelative(globalBounds, globalDomain);
            allZones.add(zone);
        }
        Collections.sort(allZones, MeshZone.ZONES_COMPARATOR);
        int partCounter = 0;
        TestFunctionsSymmetry gps = cell.getSymmetry();
        if (gps == null) {
            gps = TestFunctionsSymmetry.NO_SYMMETRY;
        }
        for (MeshZone zone : allZones) {
            DoubleToVector[] allGpFunctions = cell.getPattern().createFunctions(globalDomain, zone, monitor, getStructure());
            int goodCount = allGpFunctions.length;
            partCounter++;
            switch (gps) {
                case NO_SYMMETRY: {
                    for (int i = 0; i < goodCount; i++) {
                        DoubleToVector fct = allGpFunctions[i];
                        if (fct != null) {
                            fct = (DoubleToVector) fct.setProperty("Cell", partCounter);
                            f.add(fct);
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
                                fct = (DoubleToVector) fct.setProperty("Cell", partCounter);
                                f.add(fct);
                            } else {
                                DoubleToVector fct = Expressions.symmetric(fmotif, Axis.X, null);
                                fct = (DoubleToVector) fct.setProperty("Cell", partCounter + " [Symmetric]");
                                f.add(fct);
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
                                fct = (DoubleToVector) fct.setProperty("Cell", partCounter);
                                f.add(fct);
                            } else {
                                DoubleToVector fct = symmetric(fmotif, Axis.Y, null);
                                fct = (DoubleToVector) fct.setProperty("Cell", partCounter + " [Symmetric]");
                                f.add(fct);
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
                                    c2 = symmetric(c2, Axis.X, null);
                                    if (c2 != null) {
                                        DoubleToVector fct = DefaultDoubleToVector.add(c, c2);
                                        fct = (DoubleToVector) fct.setProperty("Cell", partCounter);
//                                        fct.setProperties(properties);
                                        f.add(fct);
                                    }
                                } else {
                                    DoubleToVector fct = allGpFunctions[i];
                                    if (fct != null) {
                                        fct = (DoubleToVector) fct.setProperty("Cell", partCounter);
                                        f.add(fct);
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

                                        fct = (DoubleToVector) fct.setMergedProperties(
                                                MapUtils.<String, Object>map(
                                                        "Cell", partCounter,
                                                        "invarianceGp", (fct.isInvariant(Axis.X) ? "X" : "") + (fct.isInvariant(Axis.Y) ? "Y" : "")
                                                )
                                        );
                                        f.add(fct);
                                    }
                                } else {
                                    DoubleToVector fct = allGpFunctions[i];
                                    if (fct != null) {
                                        fct = (DoubleToVector) fct.setProperty("Cell", partCounter);
                                        f.add(fct);
                                    }
                                }
                            }
                        }
                    }
                    break;
                }
            }

        }
        return f;
    }

    @Override
    public void setStructure(MomStructure structure) {
        super.setStructure(structure);
    }

    @Override
    protected DoubleToVector[] rebuildCachedFunctions(ProgressMonitor monitor) {
        return super.rebuildCachedFunctions(monitor);
    }

    @Override
    public Dumper getDumpStringHelper() {
        arr();
        Dumper h = super.getDumpStringHelper();
        h.add("cells", cells);
        h.add("domain", domain);
        return h;
    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        }
        return toLongString();
    }

    public String toLongString() {
        return Arrays.asList(cells).toString();
    }

    @Override
    public TestFunctionsBase clone() {
        GpAdaptiveMultiMesh c = (GpAdaptiveMultiMesh) super.clone();
        c.cells = new TestFunctionCell[c.cells.length];
        for (int i = 0; i < cells.length; i++) {
            TestFunctionCell gpCell = cells[i];
            c.cells[i] = cells[i];//clone?
        }
        return c;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
