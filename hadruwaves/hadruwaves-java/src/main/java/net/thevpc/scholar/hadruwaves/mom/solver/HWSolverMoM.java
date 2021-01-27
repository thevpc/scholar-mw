/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver;

import java.util.ArrayList;
import java.util.List;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.GeometryFactory;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.geom.Point;
import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Epsilon;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Vector3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.thevpc.Point3D;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.Material;
import net.thevpc.scholar.hadruwaves.SolverBuildResult;
import net.thevpc.scholar.hadruwaves.WallBorders;
import net.thevpc.scholar.hadruwaves.builders.CapacityBuilder;
import net.thevpc.scholar.hadruwaves.builders.CurrentBuilder;
import net.thevpc.scholar.hadruwaves.builders.DirectivityBuilder;
import net.thevpc.scholar.hadruwaves.builders.ElectricFieldBuilder;
import net.thevpc.scholar.hadruwaves.builders.FarFieldBuilder;
import net.thevpc.scholar.hadruwaves.builders.InputImpedanceBuilder;
import net.thevpc.scholar.hadruwaves.builders.MagneticFieldBuilder;
import net.thevpc.scholar.hadruwaves.builders.PoyntingVectorBuilder;
import net.thevpc.scholar.hadruwaves.builders.SParametersBuilder;
import net.thevpc.scholar.hadruwaves.builders.SelfBuilder;
import net.thevpc.scholar.hadruwaves.builders.SourceBuilder;
import net.thevpc.scholar.hadruwaves.builders.TestFieldBuilder;
import net.thevpc.scholar.hadruwaves.mom.BoxSpace;
import net.thevpc.scholar.hadruwaves.mom.MomStructure;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.planar.ExprPlanarSource;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.ListTestFunctions;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.Element3DTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectBrick;
import net.thevpc.scholar.hadruwaves.project.scene.AbstractHWProjectComponentMaterial;
import net.thevpc.scholar.hadruwaves.project.scene.HWModalPort;
import net.thevpc.scholar.hadruwaves.project.scene.HWPlanarPort;
import net.thevpc.scholar.hadruwaves.project.scene.Point3DTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DParallelipipedTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;
import net.thevpc.scholar.hadruwaves.solvers.HWSolver;
import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;

/**
 *
 * @author vpc
 */
public class HWSolverMoM implements HWSolver {

    private MomStructure str;
    private HWSolverTemplateMoM template;
    private HWConfigurationRun configuration;
    private ProgressMonitorFactory taskMonitorManager;
    private SolverBuildResult buildResult = new SolverBuildResult();

    public HWSolverMoM(HWSolverTemplateMoM template, HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager) {
        this.template = template;
        this.configuration = configuration;
        this.taskMonitorManager = taskMonitorManager;
    }

    @Override
    public CurrentBuilder current() {
        return str().current();
    }

    @Override
    public SourceBuilder source() {
        return str().source();
    }

    @Override
    public TestFieldBuilder testField() {
        return str().testField();
    }

    @Override
    public ElectricFieldBuilder electricField() {
        return str().electricField();
    }

    @Override
    public PoyntingVectorBuilder poyntingVector() {
        return str().poyntingVector();
    }

    @Override
    public DirectivityBuilder directivity() {
        return str().directivity();
    }

    @Override
    public FarFieldBuilder farField() {
        return str().farField();
    }

    @Override
    public MagneticFieldBuilder magneticField() {
        return str().magneticField();
    }

    @Override
    public CapacityBuilder capacity() {
        return str().capacity();
    }

    @Override
    public SelfBuilder self() {
        return str().self();
    }

    @Override
    public InputImpedanceBuilder inputImpedance() {
        return str().inputImpedance();
    }

    @Override
    public SParametersBuilder sparameters() {
        return str().sparameters();
    }

    public HWSolverTemplateMoM template() {
        return template;
    }

    public HWConfigurationRun configuration() {
        return configuration;
    }

    public void invalidate() {
        str = null;
    }

    public MomStructure str() {
        if (str == null) {
            build().requireNoError();
        }
        return str;
    }

    public SolverBuildResult build() {
        SolverBuildResult messages = new SolverBuildResult();
        int modes = ((Number) template.modesCount().eval(configuration)).intValue();
        WallBorders walls = null;
        BoxSpace topBoxSpace = null;
        BoxSpace bottomBoxSpace = null;
        PolygonCheckingHelper metalPolygons = new PolygonCheckingHelper();
        List<PlanarSource> planarSources = new ArrayList<PlanarSource>();
        Domain domain = null;
        for (HWProjectComponent pe : configuration.project().get().scene().get().findDeepComponents((x) -> x.enabled().eval(configuration))) {
            if (pe instanceof HWProjectBrick) {
                HWProjectBrick br = (HWProjectBrick) pe;
                Element3DParallelipipedTemplate g = (Element3DParallelipipedTemplate) br.geometry().get();
                Point3DTemplate[] points = g.getPoints();
                Point3D a = points[0].eval(configuration);
                Point3D b = points[1].eval(configuration);
                Point3D c = points[2].eval(configuration);
                Point3D d = points[3].eval(configuration);
                Vector3D ab = new Vector3D(a, b);
                Vector3D ac = new Vector3D(a, c);
                Vector3D ad = new Vector3D(a, d);
                if (ab.isParallel_to(new Vector3D(1, 0, 0))
                        && ac.isParallel_to(new Vector3D(0, 1, 0))
                        && ad.isParallel_to(new Vector3D(0, 0, 1))) {
                    Boundary brBottom = br.face(HWProjectBrick.Face.BOTTOM).boundary().eval(configuration);
                    Boundary brNorth = br.face(HWProjectBrick.Face.NORTH).boundary().eval(configuration);
                    Boundary brEast = br.face(HWProjectBrick.Face.EAST).boundary().eval(configuration);
                    Boundary brSouth = br.face(HWProjectBrick.Face.SOUTH).boundary().eval(configuration);
                    Boundary brWest = br.face(HWProjectBrick.Face.WEST).boundary().eval(configuration);
                    Boundary brTop = br.face(HWProjectBrick.Face.TOP).boundary().eval(configuration);
                    if (Epsilon.isEq(a.getZ(), 0)) {
                        double h = d.getZ() - a.getZ();
                        Domain domain0 = Domain.ofBounds(a.getX(), b.getX(), a.getY(), c.getY());
                        if (domain == null) {
                            domain = domain0;
                        } else if (!domain.equals(domain0)) {
                            messages.error("Domain mismatch " + domain + "/" + domain0);
                        }
                        if (brBottom != null
                                && brBottom != Boundary.NOTHING) {
                            messages.error("Expected No bottom boundary in Top Space");
                        }
                        WallBorders walls2 = WallBorders.of(brNorth,
                                brEast,
                                brSouth,
                                brWest
                        );
                        if (walls == null) {
                            walls = walls2;
                        } else if (!walls2.equals(walls)) {
                            messages.error("Walls mismatch " + walls + "/" + walls2 + "");
                        }
                        Boundary bb = brBottom;
                        if (bb == null) {
                            bb = Boundary.NOTHING;
                        }
                        if (bb != Boundary.NOTHING) {
                            messages.error("Expected No top boundary in Top Space");
                        }
                        bb = brTop;
                        if (bb == null) {
                            bb = Boundary.NOTHING;
                        }
                        switch (bb) {
                            case ELECTRIC: {
                                if (h <= 0 || !Double.isFinite(h)) {
                                    messages.error("Invalid Thikness in Top Space");
                                } else {
                                    Material mat = checkMaterial(br.material().get().eval(configuration), messages);
                                    topBoxSpace = BoxSpace.shortCircuit(mat, h);
                                }
                                break;
                            }
                            case OPEN: {
                                if (h <= 0 || !Double.isFinite(h)) {
                                    messages.error("Invalid Thikness in Top Space");
                                } else {
                                    Material mat = checkMaterial(br.material().get().eval(configuration), messages);
                                    topBoxSpace = BoxSpace.openCircuit(mat, h);
                                }
                                break;
                            }
                            case NOTHING: {
                                //material??
                                topBoxSpace = BoxSpace.nothing();
                                break;
                            }
                            case INFINITE: {
                                //material??
                                topBoxSpace = BoxSpace.matchedLoad(br.material().get().eval(configuration));
                                break;
                            }
                            default: {
                                messages.error("Invalid Box space boundary " + bb + "");
                            }
                        }

                    } else if (Epsilon.isEq(d.getZ(), 0)) {
                        double h = d.getZ() - a.getZ();
                        Boundary bb = brTop;
                        if (bb == null) {
                            bb = Boundary.NOTHING;
                        }
                        if (bb != Boundary.NOTHING) {
                            messages.error("Expected No top boundary in Bottom Space");
                        }
                        bb = brBottom;
                        if (bb == null) {
                            bb = Boundary.NOTHING;
                        }
                        Domain domain0 = Domain.ofBounds(a.getX(), b.getX(), a.getY(), c.getY());
                        if (domain == null) {
                            domain = domain0;
                        } else if (!domain.equals(domain0)) {
                            messages.error("Domain mismatch " + domain + "/" + domain0 + "");
                        }
                        WallBorders walls2 = WallBorders.of(brNorth,
                                brEast,
                                brSouth,
                                brWest
                        );
                        if (walls == null) {
                            walls = walls2;
                        } else if (!walls2.equals(walls)) {
                            messages.error("Walls mismatch " + walls + "/" + walls2 + "");
                        }
                        switch (bb) {
                            case ELECTRIC: {
                                if (h <= 0 || !Double.isFinite(h)) {
                                    messages.error("Invalid Thikness in Bottom Space");
                                } else {
                                    Material mat = checkMaterial(br.material().get().eval(configuration), messages);
                                    bottomBoxSpace = BoxSpace.shortCircuit(mat, h);
                                }
                                break;
                            }
                            case OPEN: {
                                if (h <= 0 || !Double.isFinite(h)) {
                                    messages.error("Invalid Thikness in Bottom Space");
                                } else {
                                    Material mat = checkMaterial(br.material().get().eval(configuration), messages);
                                    bottomBoxSpace = BoxSpace.openCircuit(mat, h);
                                }
                                break;
                            }
                            case NOTHING: {
                                //material??
                                bottomBoxSpace = BoxSpace.nothing();
                                break;
                            }
                            case INFINITE: {
                                //material??
                                bottomBoxSpace = BoxSpace.matchedLoad(br.material().get().eval(configuration));
                                break;
                            }
                            default: {
                                messages.error("Invalid Box space boundary " + bb + "");
                            }
                        }
                    } else {
                        messages.error("Unsupported Box space");
                    }
                } else {
                    messages.error("Unsupported Box space");
                }
            } else if (pe instanceof AbstractHWProjectComponentMaterial) {
                AbstractHWProjectComponentMaterial pm = (AbstractHWProjectComponentMaterial) pe;
                Material m = pm.material().get().eval(configuration);
                if (!m.equals(Material.PEC)) {
                    messages.error("Unsupported non PEC surface");
                }
                Element3DTemplate ge = pm.geometry().get();
                if (ge instanceof Element3DPolygonTemplate) {
                    Element3DPolygonTemplate pol = (Element3DPolygonTemplate) ge;
                    Point3DTemplate[] points = pol.getPoints();
                    Point[] points0 = new Point[points.length];
                    int i = 0;
                    for (Point3DTemplate point : points) {
                        Point3D s = point.eval(configuration);
                        Point sp = new Point(s.getX(), s.getY(), s.getZ());
                        if (!Epsilon.isZero(sp.z)) {
                            messages.error("Unsupported Polygon with invalid z");
                        }
                        points0[i++] = sp;
                    }
                    metalPolygons.addPolygon(GeometryFactory.createPolygon(points0));
                } else {
                    messages.error("Unsupported Element " + pe.getClass().getSimpleName());
                }
            } else if (pe instanceof HWPlanarPort) {
                HWPlanarPort pm = (HWPlanarPort) pe;
                Element3DTemplate ge = pm.geometry().get();
                if (ge instanceof Element3DPolygonTemplate) {
                    Element3DPolygonTemplate pol = (Element3DPolygonTemplate) ge;
                    Point3DTemplate[] points = pol.getPoints();
                    Point[] points0 = new Point[points.length];
                    int i = 0;
                    for (Point3DTemplate point : points) {
                        Point3D s = point.eval(configuration);
                        Point sp = new Point(s.getX(), s.getY(), s.getZ());
                        if (!Epsilon.isZero(sp.z)) {
                            messages.error("Unsupported Polygon with invalid z");
                        }
                        points0[i++] = sp;
                    }
                    Polygon sgeo = GeometryFactory.createPolygon(points0);
                    Expr sexpr=pm.expr().eval(configuration);
                    sexpr=combine(sexpr,sgeo);
                    planarSources.add(new ExprPlanarSource(sexpr, pm.impedance().eval(configuration),sgeo));
                } else {
                    messages.error("Unsupported Element " + pe.getClass().getSimpleName());
                }
            } else if (pe instanceof HWModalPort) {
                HWModalPort pm = (HWModalPort) pe;
                Element3DTemplate ge = pm.geometry().get();
                if (ge instanceof Element3DPolygonTemplate) {
                    Element3DPolygonTemplate pol = (Element3DPolygonTemplate) ge;
                    Point3DTemplate[] points = pol.getPoints();
                    Point[] points0 = new Point[points.length];
                    int i = 0;
                    for (Point3DTemplate point : points) {
                        Point3D s = point.eval(configuration);
                        Point sp = new Point(s.getX(), s.getY(), s.getZ());
//                        if (!Epsilon.isZero(sp.z)) {
//                            throw throwMomError("Unsupported Polygon with invalid z");
//                        }
                        points0[i++] = sp;
                    }
                    messages.error("Unsupported Model Sources " + pe.getClass().getSimpleName());
                } else {
                    messages.error("Unsupported Element " + pe.getClass().getSimpleName());
                }
            } else if (pe == null) {
                messages.error("Unsupported Element null");
            } else {
                messages.error("Unsupported Element " + pe.getClass().getSimpleName());
            }
        }
        if (domain == null) {
            messages.error("Missing Domain");
        }
        if (topBoxSpace == null) {
            messages.error("Missing Top Space");
        }
        if (bottomBoxSpace == null) {
            messages.error("Missing Bottom Space");
        }
        if(planarSources.isEmpty()){
            messages.error("Missing Ports");
        }

        //include sources because they should be included in test functions
        for (PlanarSource planarSource : planarSources) {
            for (Polygon polygon : planarSource.getGeometry().toPolygons()) {
                metalPolygons.addPolygon(polygon);
            }
        }
        List<Expr> validExprs=new ArrayList<>();
        for (MomSolverTestTemplate testFunction : template.testFunctions()) {
            Expr[] exprs = testFunction.generate(this);
            for (Expr expr : exprs) {
                expr=metalPolygons.addExpr(expr);
                if(expr!=null){
                    validExprs.add(expr);
                }
            }
        }

        if(validExprs.isEmpty()){
            messages.error("Missing Test Functions");
        }

        if(!metalPolygons.allPolygonsMatched()){
            messages.error("Missing Test Functions");
        }

        if (!messages.isError()) {
            try {
                str = MomStructure.createMomStructure(walls, domain, template.frequency().eval(configuration), modes, bottomBoxSpace, topBoxSpace);
                ListTestFunctions listTestFunctions = new ListTestFunctions();
//        List<Expr> tf = new ArrayList<>();
                for (Expr expr : validExprs) {
                    listTestFunctions.add(expr);
                }
                str.setTestFunctions(listTestFunctions);
                for (PlanarSource src : planarSources) {
                    str.addSource(src);
                }
                str.setMonitorFactory(taskMonitorManager);
            } catch (Exception ex) {
                messages.error(ex.toString());
                str=null;
            }
        }
        buildResult = messages;
        return messages;
    }

    private class PolygonCheckingHelper {
        List<Polygon> metalPolygons=new ArrayList<>();
        List<Boolean> matchedPolygons =new ArrayList<>();

        public void addPolygon(Polygon p){
            metalPolygons.add(p);
            matchedPolygons.add(false);
        }

        protected Expr addExpr(Expr expr){
            Domain domain = expr.getDomain();
            Geometry gAll=null;
            for (int i = 0; i < metalPolygons.size(); i++) {
                Geometry g = metalPolygons.get(i).intersectGeometry(domain.toGeometry());
                if(!g.isEmpty()){
                    if(gAll==null){
                        gAll=g;
                    }else{
                        gAll=gAll.addGeometry(g);
                    }
                    matchedPolygons.set(i,true);
                }
            }
            if(gAll==null){
                return null;
            }
            return combine(expr,gAll);
        }

        public boolean allPolygonsMatched() {
            for (Boolean m : matchedPolygons) {
                if(!m){
                    return false;
                }
            }
            return true;
        }
    }

    public SolverBuildResult buildResult() {
        return buildResult;
    }

    private Material checkMaterial(Material mat, SolverBuildResult messages) {
        if (mat == null) {
            messages.error("Missing Material");
        } else {
            //
        }
        return mat;
    }

    private Expr combine(Expr expr,Geometry geom){
        if(geom.isRectangular()){
            Domain dom = geom.getDomain();
            if(dom.equals(expr.getDomain())){
                return expr;
            }
            return expr.mul(dom);
        }else{
            return expr.mul(geom);
        }

    }

}
