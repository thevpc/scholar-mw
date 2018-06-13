package net.vpc.scholar.hadruwaves.mom.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.text.ParseException;
import java.util.Iterator;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruwaves.mom.*;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMultiMesh;
import net.vpc.scholar.hadruwaves.mom.project.common.Area;
import net.vpc.scholar.hadruwaves.mom.project.common.AreaFilter;
import net.vpc.scholar.hadruwaves.mom.project.common.AreaGroup;
import net.vpc.scholar.hadruwaves.mom.project.common.DefaultAreaFilter;
import net.vpc.scholar.hadruwaves.mom.sources.planar.CstPlanarSource;
import net.vpc.scholar.hadrumaths.util.config.Configuration;
import net.vpc.scholar.hadruwaves.WallBorders;
import net.vpc.scholar.hadruwaves.ModeIndex;
import net.vpc.scholar.hadruwaves.mom.testfunctions.TestFunctionCell;
import net.vpc.scholar.hadrumaths.geom.DefaultGeometryList;
import net.vpc.scholar.hadruwaves.Wall;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.ModalSourceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PecMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PlanarSourceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.SurfaceImpedanceMaterial;
import net.vpc.scholar.hadruwaves.mom.project.common.AreaZone;
import net.vpc.scholar.hadruwaves.mom.project.common.VarUnit;
import net.vpc.scholar.hadruwaves.mom.project.common.VariableExpression;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;
import net.vpc.common.util.IndexedMap;
import net.vpc.scholar.hadruwaves.mom.sources.planar.DefaultPlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.CutOffModalSources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.mom.sources.modal.UserModalSources;
import net.vpc.scholar.hadruwaves.mom.util.MomUtils;

/**
 * User: taha Date: 7 juil. 2003 Time: 16:28:51 To change this template use
 * Options | File Templates.
 */
public class MomProject implements Serializable, Cloneable {

    public static final String DEFAULT_STRUCTURE_PREFIX = "str";
    private String structureConfigurationPrefix = DEFAULT_STRUCTURE_PREFIX;
    private String name;
    private String description;
    public boolean reduceMemoryUsage;
    private AreaGroup rootAreaGroup;
    private String frequencyExpression = "0";
    private ProjectType projectType = ProjectType.PLANAR_STRUCTURE;
    private CircuitType circuitType = CircuitType.SERIAL;
    private HintAxisType hintAxisType = HintAxisType.XY;
    private ModeType[] hintFnModes = null;
    private boolean hintRegularZOperator = false;
    private boolean hintAmatrixSparsify = false;//HINT_AMATRIX_SPARSIFY
    private boolean hintBmatrixSparsify = false;//HINT_AMATRIX_SPARSIFY
    private String hintDiscardFnByScalarProductExpression = null;
    private float hintDiscardFnByScalarProduct = 0;
    private PropertyChangeSupport changeSupport;
    public static final String VALUES_CHANGED = "VALUES_CHANGED";
    public static final String AREA_ADDED = "AREA_ADDED";
    public static final String AREA_UPDATED = "AREA_UPDATED";
    public static final String AREA_REMOVED = "AREA_REMOVED";
    public static final String AREAGROUP_ADDED = "AREAGROUP_ADDED";
    public static final String AREAGROUP_UPDATED = "AREAGROUP_UPDATED";
    public static final String AREAGROUP_REMOVED = "AREAGROUP_REMOVED";
    private Configuration configuration;
    private MomProjectLayers layersInfo;
    public String maxModesExpression;
//    public WallBorders wallBorders = WallBorders.EEEE;
    public String xExpression = "0";
    public String yExpression = "0";
    public String widthExpression = "10";
    public String heightExpression = "10";
    public String southWallExpression = Wall.ELECTRIC.toString().toLowerCase();
    public String northWallExpression = Wall.ELECTRIC.toString().toLowerCase();
    public String westWallExpression = Wall.ELECTRIC.toString().toLowerCase();
    public String eastWallExpression = Wall.ELECTRIC.toString().toLowerCase();
    public boolean cacheEnabled = true;
    private IndexedMap<String, VariableExpression> expressions = new IndexedMap<String, VariableExpression>();
    private double dimensionUnit = 1;
    private double frequencyUnit = 1;
    private VarEvaluator evaluator = new VarEvaluator();

//    public Structure() {
//        this("NO_NAME", null, "1", false, new AreaGroup("root"), new StructureContext());
//    }
    public MomProject(File file) throws IOException, ParseException {
        this();
        load(file);
        recompile();
    }

    public MomProject() {
        layersInfo = new MomProjectLayers();
        rootAreaGroup = new AreaGroup("Default Area Group", this);
        rootAreaGroup.setProject(this);
        configuration = new Configuration();
        configuration.getOptions().setStoreComments(false);
        configuration.getOptions().setAutoSave(false);
        init();
    }

    protected void init() {
        rootAreaGroup.setProject(this);
        this.changeSupport = new PropertyChangeSupport(this);
        layersInfo.setContext(this);
        evaluator.update(this);
        addProjectListener(new MomProjectListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.startsWith("var.") || name.endsWith("Unit")) {
                    evaluator.update(MomProject.this);
                }
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MomProject other = (MomProject) obj;
        if (this.name != other.name && (this.name == null || !this.name.equals(other.name))) {
            return false;
        }
        if (this.description != other.description && (this.description == null || !this.description.equals(other.description))) {
            return false;
        }
        if (this.reduceMemoryUsage != other.reduceMemoryUsage) {
            return false;
        }
        if (this.rootAreaGroup != other.rootAreaGroup && (this.rootAreaGroup == null || !this.rootAreaGroup.equals(other.rootAreaGroup))) {
            return false;
        }
        if (this.frequencyExpression != other.frequencyExpression && (this.frequencyExpression == null || !this.frequencyExpression.equals(other.frequencyExpression))) {
            return false;
        }
        if (this.projectType != other.projectType) {
            return false;
        }
        if (this.circuitType != other.circuitType) {
            return false;
        }
        if (this.hintAxisType != other.hintAxisType) {
            return false;
        }
        if (this.hintFnModes != other.hintFnModes && (this.hintFnModes == null || !this.hintFnModes.equals(other.hintFnModes))) {
            return false;
        }
        if (this.hintRegularZOperator != other.hintRegularZOperator) {
            return false;
        }
        if (this.hintAmatrixSparsify != other.hintAmatrixSparsify) {
            return false;
        }
        if (this.hintBmatrixSparsify != other.hintBmatrixSparsify) {
            return false;
        }
        if (this.hintDiscardFnByScalarProductExpression != other.hintDiscardFnByScalarProductExpression && (this.hintDiscardFnByScalarProductExpression == null || !this.hintDiscardFnByScalarProductExpression.equals(other.hintDiscardFnByScalarProductExpression))) {
            return false;
        }
        if (this.hintDiscardFnByScalarProduct != other.hintDiscardFnByScalarProduct) {
            return false;
        }
        if (this.configuration != other.configuration && (this.configuration == null || !this.configuration.equals(other.configuration))) {
            return false;
        }
        if (this.layersInfo != other.layersInfo && (this.layersInfo == null || !this.layersInfo.equals(other.layersInfo))) {
            return false;
        }
        if (this.maxModesExpression != other.maxModesExpression && (this.maxModesExpression == null || !this.maxModesExpression.equals(other.maxModesExpression))) {
            return false;
        }
        if (this.northWallExpression != other.northWallExpression && (this.northWallExpression == null || !this.northWallExpression.equals(other.northWallExpression))) {
            return false;
        }
        if (this.southWallExpression != other.southWallExpression && (this.southWallExpression == null || !this.southWallExpression.equals(other.southWallExpression))) {
            return false;
        }
        if (this.eastWallExpression != other.eastWallExpression && (this.eastWallExpression == null || !this.eastWallExpression.equals(other.eastWallExpression))) {
            return false;
        }
        if (this.westWallExpression != other.westWallExpression && (this.westWallExpression == null || !this.westWallExpression.equals(other.westWallExpression))) {
            return false;
        }
        if (this.xExpression != other.xExpression && (this.xExpression == null || !this.xExpression.equals(other.xExpression))) {
            return false;
        }
        if (this.yExpression != other.yExpression && (this.yExpression == null || !this.yExpression.equals(other.yExpression))) {
            return false;
        }
        if (this.widthExpression != other.widthExpression && (this.widthExpression == null || !this.widthExpression.equals(other.widthExpression))) {
            return false;
        }
        if (this.heightExpression != other.heightExpression && (this.heightExpression == null || !this.heightExpression.equals(other.heightExpression))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.description != null ? this.description.hashCode() : 0);
        hash = 59 * hash + (this.reduceMemoryUsage ? 1 : 0);
        hash = 59 * hash + (this.rootAreaGroup != null ? this.rootAreaGroup.hashCode() : 0);
        hash = 59 * hash + (this.frequencyExpression != null ? this.frequencyExpression.hashCode() : 0);
        hash = 59 * hash + (this.projectType != null ? this.projectType.hashCode() : 0);
        hash = 59 * hash + (this.circuitType != null ? this.circuitType.hashCode() : 0);
        hash = 59 * hash + (this.hintAxisType != null ? this.hintAxisType.hashCode() : 0);
        hash = 59 * hash + (this.hintFnModes != null ? this.hintFnModes.hashCode() : 0);
        hash = 59 * hash + (this.hintRegularZOperator ? 1 : 0);
        hash = 59 * hash + (this.hintAmatrixSparsify ? 1 : 0);
        hash = 59 * hash + (this.hintBmatrixSparsify ? 1 : 0);
        hash = 59 * hash + (this.hintDiscardFnByScalarProductExpression != null ? this.hintDiscardFnByScalarProductExpression.hashCode() : 0);
        hash = 59 * hash + Float.floatToIntBits(this.hintDiscardFnByScalarProduct);
        hash = 59 * hash + (this.configuration != null ? this.configuration.hashCode() : 0);
        hash = 59 * hash + (this.layersInfo != null ? this.layersInfo.hashCode() : 0);
        hash = 59 * hash + (this.maxModesExpression != null ? this.maxModesExpression.hashCode() : 0);
        hash = 59 * hash + (this.northWallExpression != null ? this.northWallExpression.hashCode() : 0);
        hash = 59 * hash + (this.southWallExpression != null ? this.southWallExpression.hashCode() : 0);
        hash = 59 * hash + (this.westWallExpression != null ? this.westWallExpression.hashCode() : 0);
        hash = 59 * hash + (this.eastWallExpression != null ? this.eastWallExpression.hashCode() : 0);
        hash = 59 * hash + (this.xExpression != null ? this.xExpression.hashCode() : 0);
        hash = 59 * hash + (this.yExpression != null ? this.yExpression.hashCode() : 0);
        hash = 59 * hash + (this.widthExpression != null ? this.widthExpression.hashCode() : 0);
        hash = 59 * hash + (this.heightExpression != null ? this.heightExpression.hashCode() : 0);
        return hash;
    }

//    public Structure(String name,String desc,double frequency, Area[] sources, Dielectric dielectric, Area[] metals,Complex zs) {
//        this.name=name;
//        this.description=desc;
//        this.frequency = frequency;
//        this.sources = sources;
//        this.dielectric = dielectric;
//        this.metals = metals;
//        this.jxy=new Jxy(this);
//        this.zs=zs;
//        revalidateStructure();
//    }
    public Collection<Area> findAreas(AreaFilter areaFilter) {
        return rootAreaGroup == null ? new ArrayList<Area>() : rootAreaGroup.findAreas(areaFilter);
    }

    public Collection<Area> findAreas(Class clazz, Boolean enabled) {
        return findAreas(new DefaultAreaFilter(clazz == null ? null : new Class[]{clazz}, null, enabled, null));
    }

    public void setValues(MomProject other) {
//        if(!equals(other)){
        name = other.name;
        hintRegularZOperator = other.hintRegularZOperator;
        projectType = other.projectType;
        circuitType = other.circuitType;
        description = other.description;
        frequencyExpression = other.frequencyExpression;
        reduceMemoryUsage = other.reduceMemoryUsage;
        rootAreaGroup = other.rootAreaGroup;
        rootAreaGroup.setProject(this);
        recompile();
        fireChange();
    }

    public double getFrequency() {
        return evaluateFrequency(frequencyExpression);
    }

    public Collection<Area> getSources(Boolean enabled) {
        return findAreas(PlanarSourceMaterial.class, enabled);
    }

    public Collection<Area> getImpedanceSurfaces(Boolean enabled) {
        return findAreas(SurfaceImpedanceMaterial.class, enabled);
    }

    public Sources getSources() {
        switch (getProjectType()) {
            case WAVE_GUIDE: {
                ArrayList<ModalSources> sources = new ArrayList<ModalSources>();
                Stack<AreaZone> stack = new Stack<AreaZone>();
                stack.push(rootAreaGroup);
                rootAreaGroup.setContext(this);
                while (!stack.isEmpty()) {
                    AreaZone o = stack.pop();
                    if (o instanceof AreaGroup) {
                        AreaGroup ag = (AreaGroup) o;
                        int ac = ag.getAreasCount();
                        for (int i = 0; i < ac; i++) {
                            Area sa = ag.getArea(i);
                            stack.push(sa);
                        }
                        int gc = ag.getGroupsCount();
                        for (int i = 0; i < gc; i++) {
                            AreaGroup sa = ag.getGroup(i);
                            stack.push(sa);
                        }
                    } else {
                        Area sa = (Area) o;
                        if (sa.getMaterial() instanceof PlanarSourceMaterial) {
                            throw new IllegalArgumentException("Planar Sources are not allowed in Waveguide Structure Project");
                        } else if (sa.getMaterial() instanceof ModalSourceMaterial) {
                            ModalSourceMaterial ps = (ModalSourceMaterial) sa.getMaterial();
                            int c = ps.getSourcesCount();
                            ModeIndex[] m = ps.getNamedModes();
                            if (m.length == 0) {
                                sources.add(new CutOffModalSources(c));
                            } else {
                                sources.add(new UserModalSources(m, c));
                            }
                        }
                    }
                }
                if (sources.size() > 1) {
                    throw new IllegalArgumentException("You can not define more than one multio source");
                } else if (sources.size() == 0) {
                    return new CutOffModalSources(1);
                } else {
                    return sources.get(0);
                }
            }
            case PLANAR_STRUCTURE: {
                ArrayList<PlanarSource> sources = new ArrayList<PlanarSource>();
                Stack<AreaZone> stack = new Stack<AreaZone>();
                stack.push(rootAreaGroup);
                rootAreaGroup.setContext(this);
                while (!stack.isEmpty()) {
                    AreaZone o = stack.pop();
                    if (o instanceof AreaGroup) {
                        AreaGroup ag = (AreaGroup) o;
                        int ac = ag.getAreasCount();
                        for (int i = 0; i < ac; i++) {
                            Area sa = ag.getArea(i);
                            stack.push(sa);
                        }
                        int gc = ag.getGroupsCount();
                        for (int i = 0; i < gc; i++) {
                            AreaGroup sa = ag.getGroup(i);
                            stack.push(sa);
                        }
                    } else {
                        Area sa = (Area) o;
                        if (sa.getMaterial() instanceof PlanarSourceMaterial) {
                            PlanarSourceMaterial ps = (PlanarSourceMaterial) sa.getMaterial();
                            sources.add(new CstPlanarSource(ps.getXValue(), ps.getYValue(), ps.getCaracteristicImpedance(), ps.getPolarization(), sa.getShape().getPolygons(this.getDomain())));
                        } else if (sa.getMaterial() instanceof ModalSourceMaterial) {
                            throw new IllegalArgumentException("Modal Sources are not allowed in Planar Structure Project");
                        }
                    }
                }
                return new DefaultPlanarSources(sources);
            }
        }
        return null;
    }

    public TestFunctions getGpTestFunctions() {
        List<TestFunctionCell> _gp = getMetalCells(true);
        return (new GpAdaptiveMultiMesh(this.getDomain(), _gp.toArray(new TestFunctionCell[_gp.size()])));
    }

    public List<TestFunctionCell> getMetalCells(Boolean enabled) {
        DefaultAreaFilter filter = new DefaultAreaFilter(new Class[]{PecMaterial.class}, null, enabled, null);
        ArrayList<TestFunctionCell> found = new ArrayList<TestFunctionCell>();
        Stack<AreaZone> stack = new Stack<AreaZone>();
        stack.push(rootAreaGroup);
        rootAreaGroup.setContext(this);
        while (!stack.isEmpty()) {
            AreaZone o = stack.pop();
            if (o instanceof AreaGroup) {
                AreaGroup ag = (AreaGroup) o;
                GpMesher gpm = ag.getGpMesher();
                if (gpm == null) {
                    int ac = ag.getAreasCount();
                    for (int i = 0; i < ac; i++) {
                        Area sa = ag.getArea(i);
                        stack.push(sa);
                    }
                    int gc = ag.getGroupsCount();
                    for (int i = 0; i < gc; i++) {
                        AreaGroup sa = ag.getGroup(i);
                        stack.push(sa);
                    }
                } else {
                    Stack<AreaZone> children = new Stack<AreaZone>();
                    children.push(o);
                    ArrayList<Geometry> pp = new ArrayList<Geometry>();
                    while (!children.isEmpty()) {
                        o = children.pop();
                        int ac = ag.getAreasCount();
                        for (int i = 0; i < ac; i++) {
                            Area sa = ag.getArea(i);
                            if (filter.accept(sa)) {
                                for (Geometry pol : sa.getShape().getPolygons(this.getDomain())) {
                                    if (!this.getDomain().includes(pol.getDomain())) {
                                        throw new IllegalArgumentException(sa.getMaterial().getName() + " " + sa.getName() + " is outside structure domain");
                                    }
                                    pp.add(pol);
                                }
                            }
                        }
                        if (o instanceof AreaGroup) {
                            int gc = ag.getGroupsCount();
                            for (int i = 0; i < gc; i++) {
                                AreaGroup sa = ag.getGroup(i);
                                children.push(sa);
                            }
                        }
                    }
                    if (pp.size() > 0) {
                        found.add(new TestFunctionCell(
                                ag.getDomain(),
                                new DefaultGeometryList(this.getDomain(), pp),
                                gpm.getPattern(),
                                gpm.getSymmetry(),
                                gpm.getMeshAlgo(),
                                gpm.getInvariance()));
                    }
                }
            } else {
                if (filter.accept(o)) {
                    Area sa = (Area) o;
                    PecMaterial pec = (PecMaterial) sa.getMaterial();
                    GpMesher agpm = pec.getGpMesher();
                    if (agpm == null) {
                        throw new IllegalArgumentException(sa.getMaterial().getName() + " " + sa.getName() + " has no Gp defined");
                    }
                    found.add(new TestFunctionCell(
                            sa.getShape().getDomain(),
                            sa.getShape().getPolygons(this.getDomain()),
                            agpm.getPattern(),
                            agpm.getSymmetry(),
                            agpm.getMeshAlgo(),
                            agpm.getInvariance()));

                }
            }
        }
        return found;
    }

    public Collection<Area> getMetals(Boolean enabled) {
        return findAreas(PecMaterial.class, enabled);
    }

    public void removeArea(Area area) {
        AreaGroup p = area.getParentGroup();
        if (p == null) {
            p = rootAreaGroup;
        }
        p.removeArea(area);
    }

    public boolean contains(Area anyArea) {
        return rootAreaGroup.contains(anyArea);
    }

    public void addArea(Area someArea) {
        rootAreaGroup.addArea(someArea);
        someArea.setProject(this);
        someArea.recompile();
    }

    public void updateArea(Area oldArea, Area newArea) {
        oldArea.getParentGroup().updateArea(oldArea, newArea);
    }

//    public static Structure loadStructure(Configuration configuration, String prefix) {
//        String sname = configuration.getString(prefix + ".name");
//        String sdesc = configuration.getString(prefix + ".desc");
//        boolean _reduceMemoryUsage = configuration.getBoolean(prefix + ".reduceMemoryUsage", false);
//        String sfreqExpr = String.valueOf(configuration.getObject(prefix + ".freq", "0"));
////        double sfreq=configuration.getDouble(prefix+".freq",0);
//        AreaGroup group = AreaGroup.load(configuration, prefix+".root");
//        if(group==null){
//            group=new AreaGroup("root");
//        }
//        StructureContext context = StructureContext.load(configuration, prefix + ".context");
//        Structure s = new Structure(sname, sdesc, sfreqExpr,_reduceMemoryUsage, group, context);
//        s.recompile();
//        return s;
//    }
    public void setFile(File file) throws IOException {
        getConfiguration().getOptions().setFile(file);
    }

    public void store() throws IOException {
        storeConfiguration(getConfiguration());
        configuration.store();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        if (old != name) {
            fireChange();
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String old = this.description;
        this.description = description;
        if (old != description) {
            fireChange();
        }
    }

    public void recompile() {
        this.evaluator.update(this);
        hintDiscardFnByScalarProduct = (hintDiscardFnByScalarProductExpression == null || hintDiscardFnByScalarProductExpression.length() == 0) ? 0 : ((float) evaluateDouble(hintDiscardFnByScalarProductExpression));
        //System.out.println("recompile structure " + name + " (" + System.identityHashCode(this) + ")");

        if (rootAreaGroup != null) {
            rootAreaGroup.recompile();
        }
        layersInfo.recompile();
    }

    public String getFrequencyExpression() {
        return frequencyExpression;
    }

    public void setFrequencyExpression(String frequenceExpression) {
        String old = this.frequencyExpression;
        this.frequencyExpression = frequenceExpression;
        if ((old == null && frequenceExpression != null) || (old != null && !old.equals(this.frequencyExpression))) {
            fireChange();
        }
    }

    public boolean isReduceMemoryUsage() {
        return reduceMemoryUsage;
    }

    public void setReduceMemoryUsage(boolean reduceMemoryUsage) {
        boolean old = this.reduceMemoryUsage;
        this.reduceMemoryUsage = reduceMemoryUsage;
        if (old != reduceMemoryUsage) {
            fireChange();
        }
    }

    public AreaGroup getRootAreaGroup() {
        return rootAreaGroup;
    }

    public void addProjectListener(MomProjectListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removeStructureListener(MomProjectListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void fireChange() {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(VALUES_CHANGED, Boolean.FALSE, Boolean.TRUE);
        }
    }

    public void fireProperty(String property, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(property, oldValue, newValue);
        }
    }

    public File getConfigFile() {
        return configuration.getOptions().getFile();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public File getWorkDir() {
        File f = configuration.getOptions().getFile();
        return f == null ? null : f.getParentFile();
    }

    public void load() throws IOException, ParseException {
        load(configuration.getOptions().getFile());
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        ProjectType old = this.projectType;
        this.projectType = projectType;
        changeSupport.firePropertyChange("projectType", old, projectType);
    }

    public CircuitType getCircuitType() {
        return circuitType;
    }

    public void setCircuitType(CircuitType circuitType) {
        CircuitType old = this.circuitType;
        this.circuitType = circuitType;
        changeSupport.firePropertyChange("circuitType", old, circuitType);
    }

    public HintAxisType getHintAxisType() {
        return hintAxisType;
    }

    public void setHintAxisType(HintAxisType hintAxisType) {
        this.hintAxisType = hintAxisType;
        fireChange();
    }

    public PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    public void setChangeSupport(PropertyChangeSupport changeSupport) {
        this.changeSupport = changeSupport;
    }

    public ModeType[] getHintFnModes() {
        return hintFnModes;
    }

    public void setHintFnModes(ModeType[] hintFnModes) {
        this.hintFnModes = hintFnModes;
        fireChange();
    }

    public boolean isHintRegularZOperator() {
        return hintRegularZOperator;
    }

    public void setHintRegularZOperator(boolean hintRegularZOperator) {
        this.hintRegularZOperator = hintRegularZOperator;
        fireChange();
    }

    public String getHintDiscardFnByScalarProductExpression() {
        return hintDiscardFnByScalarProductExpression;
    }

    public void setHintDiscardFnByScalarProductExpression(String hintDiscardFnByScalarProduct) {
        this.hintDiscardFnByScalarProductExpression = hintDiscardFnByScalarProduct;
        fireChange();
    }

    public boolean isHintAmatrixSparsify() {
        return hintAmatrixSparsify;
    }

    public void setHintAmatrixSparsify(boolean hintAmatrixSparsify) {
        this.hintAmatrixSparsify = hintAmatrixSparsify;
        recompile();
    }

    public boolean isHintBmatrixSparsify() {
        return hintBmatrixSparsify;
    }

    public void setHintBmatrixSparsify(boolean hintBmatrixSparsify) {
        this.hintBmatrixSparsify = hintBmatrixSparsify;
        recompile();
    }

    public float getHintDiscardFnByScalarProduct() {
        return hintDiscardFnByScalarProduct;
    }

    public MomProjectLayers getLayers() {
        return layersInfo;
    }

    public String getMaxModesExpression() {
        return maxModesExpression;
    }

    public void setMaxModesExpression(String maxExpression) {
        this.maxModesExpression = maxExpression;
        recompile();
    }

    public int getMaxModes() {
        return evaluateInt(maxModesExpression);
    }

    public WallBorders getWallBorders() {
        Wall n = (Wall) evaluate(northWallExpression, VarUnit.WALL);
        Wall s = (Wall) evaluate(southWallExpression, VarUnit.WALL);
        Wall e = (Wall) evaluate(eastWallExpression, VarUnit.WALL);
        Wall w = (Wall) evaluate(westWallExpression, VarUnit.WALL);
        if (n == null || s == null || e == null || w == null) {
            return null;
        }
        return WallBorders.valueOf(n, e, s, w);
    }

    public void setWallBorders(WallBorders wallBorders) {
        if (wallBorders == null) {
            setNorthWallExpression(null);
            setSouthWallExpression(null);
            setEastWallExpression(null);
            setWestWallExpression(null);
        } else {
            setNorthWallExpression(MomUtils.toValidEnumString(wallBorders.getNorth()));
            setSouthWallExpression(MomUtils.toValidEnumString(wallBorders.getSouth()));
            setEastWallExpression(MomUtils.toValidEnumString(wallBorders.getEast()));
            setWestWallExpression(MomUtils.toValidEnumString(wallBorders.getWest()));
        }
    }

    public String getSouthWallExpression() {
        return southWallExpression;
    }

    public void setSouthWallExpression(String southWallExpression) {
        this.southWallExpression = MomUtils.toValidEnumString(southWallExpression, Wall.class);
    }

    public String getNorthWallExpression() {
        return northWallExpression;
    }

    public void setNorthWallExpression(String northWallExpression) {
        this.northWallExpression = MomUtils.toValidEnumString(northWallExpression, Wall.class);
    }

    public String getWestWallExpression() {
        return westWallExpression;
    }

    public void setWestWallExpression(String westWallExpression) {
        this.westWallExpression = MomUtils.toValidEnumString(westWallExpression, Wall.class);
    }

    public String getEastWallExpression() {
        return eastWallExpression;
    }

    public void setEastWallExpression(String eastWallExpression) {
        this.eastWallExpression = MomUtils.toValidEnumString(eastWallExpression, Wall.class);
    }

    public Domain getDomain() {
        double x = evaluateDimension(xExpression);
        double y = evaluateDimension(yExpression);
        double width = evaluateDimension(widthExpression);
        double height = evaluateDimension(heightExpression);
        if (width < 0) {
            x = x + width;
            width = -width;
        }
        if (height < 0) {
            y = y + height;
            height = -height;
        }

        return Domain.forWidth(x, width, y, height);
    }

    public void setHeight(double height) {
        this.heightExpression = String.valueOf(height);
        fireChange();
    }

    public String getHeightExpression() {
        return heightExpression;
    }

    public void setHeightExpression(String heightExpression) {
        this.heightExpression = heightExpression;
        fireChange();
    }

    public void setWidth(double width) {
        this.widthExpression = String.valueOf(width);
        fireChange();
    }

    public String getWidthExpression() {
        return widthExpression;
    }

    public void setWidthExpression(String widthExpression) {
        this.widthExpression = widthExpression;
        fireChange();
    }

    public void setX(double x) {
        this.xExpression = String.valueOf(x);
    }

    public String getXExpression() {
        return xExpression;
    }

    public void setXExpression(String xExpression) {
        this.xExpression = xExpression;
    }

    public void setY(double y) {
        this.yExpression = String.valueOf(y);
    }

    public String getYExpression() {
        return yExpression;
    }

    public void setYExpression(String yExpression) {
        this.yExpression = yExpression;
    }

//----------------------------------------------------------------
    public void load(File file) throws IOException, ParseException {
        expressions.clear();
        configuration.clear();
        configuration.getOptions().setFile(file);
        configuration.load();
        String sname = configuration.getString(structureConfigurationPrefix + ".name");
        if (sname == null || sname.length() == 0) {
            sname = "NO_NAME";
        }
        String sdesc = configuration.getString(structureConfigurationPrefix + ".desc");
        boolean _reduceMemoryUsage = configuration.getBoolean(structureConfigurationPrefix + ".reduceMemoryUsage", false);
        this.rootAreaGroup = (AreaGroup) MomProjectFactory.INSTANCE.load(configuration, structureConfigurationPrefix + ".root");
        if (rootAreaGroup == null) {
            rootAreaGroup = new AreaGroup("root", this);
        } else {
            rootAreaGroup.setProject(this);
        }
        ProjectType pt = configuration.getString(structureConfigurationPrefix + ".projectType") == null ? ProjectType.PLANAR_STRUCTURE : ProjectType.valueOf(configuration.getString(structureConfigurationPrefix + ".projectType"));
        CircuitType ct = configuration.getString(structureConfigurationPrefix + ".circuitType") == null ? CircuitType.SERIAL : CircuitType.valueOf(configuration.getString(structureConfigurationPrefix + ".circuitType"));
        HintAxisType ha = configuration.getString(structureConfigurationPrefix + ".hintAxisType") == null ? null : HintAxisType.valueOf(configuration.getString(structureConfigurationPrefix + ".hintAxisType"));
        String modesStr = configuration.getString(structureConfigurationPrefix + ".hintFnModes");
        if (modesStr == null) {
            modesStr = "";
        }
        northWallExpression = configuration.getString(structureConfigurationPrefix + ".northWall", MomUtils.toValidEnumString(Wall.ELECTRIC));
        southWallExpression = configuration.getString(structureConfigurationPrefix + ".southWall", MomUtils.toValidEnumString(Wall.ELECTRIC));
        eastWallExpression = configuration.getString(structureConfigurationPrefix + ".eastWall", MomUtils.toValidEnumString(Wall.ELECTRIC));
        westWallExpression = configuration.getString(structureConfigurationPrefix + ".westWall", MomUtils.toValidEnumString(Wall.ELECTRIC));
        layersInfo.load(configuration, structureConfigurationPrefix + ".layers");

        this.name = sname;
        this.description = sdesc;
        this.maxModesExpression = configuration.getString(structureConfigurationPrefix + ".maxModes", "1000");
        this.frequencyExpression = configuration.getString(structureConfigurationPrefix + ".freq", "0");
        this.xExpression = configuration.getString(structureConfigurationPrefix + ".x", "0");
        this.yExpression = configuration.getString(structureConfigurationPrefix + ".y", "0");
        this.widthExpression = configuration.getString(structureConfigurationPrefix + ".width", "0");
        this.heightExpression = configuration.getString(structureConfigurationPrefix + ".height", "0");
        this.cacheEnabled = Boolean.parseBoolean(configuration.getString(structureConfigurationPrefix + ".cacheEnabled", "true"));
        this.reduceMemoryUsage = _reduceMemoryUsage;
        this.projectType = pt;
        this.circuitType = ct;
        this.hintAxisType = ha;
        frequencyUnit = (configuration.getDouble(structureConfigurationPrefix + ".frequencyUnit", 1));
        dimensionUnit = (configuration.getDouble(structureConfigurationPrefix + ".dimensionUnit", 1));
        String[] vars = configuration.getChildrenKeys(structureConfigurationPrefix + ".vars", true);
        for (String var : vars) {
            String s = var.substring((structureConfigurationPrefix + ".vars.").length());
            int x = s.indexOf(".");
            if (x < 0) {
                setExpression(new VariableExpression(s, configuration.getString(var), VarUnit.NUMBER, ""));
            } else {
                if (var.substring((structureConfigurationPrefix + ".vars.").length() + x + 1).equals("expression")) {
                    String pr = var.substring(0, (structureConfigurationPrefix + ".vars.").length() + x);
                    setExpression(new VariableExpression(s.substring(0, x),
                            configuration.getString(var),
                            configuration.getString(pr + ".type") == null ? VarUnit.NUMBER : VarUnit.valueOf(configuration.getString(pr + ".type")),
                            configuration.getString(pr + ".desc")));
                }
            }
        }
        ArrayList<ModeType> modesArr = new ArrayList<ModeType>();
        for (String ms : modesStr.split(";")) {
            String mss = ms.trim();
            if (mss.length() > 0) {
                modesArr.add(ModeType.valueOf(mss));
            }
        }
        this.hintFnModes = modesArr.toArray(new ModeType[modesArr.size()]);

        if (rootAreaGroup == null) {
            this.rootAreaGroup = new AreaGroup("root", this);
        }
        this.rootAreaGroup.setProject(this);
        recompile();
    }

    public void clear() {
        configuration.deleteTree(structureConfigurationPrefix, true);
    }

    public void storeConfiguration(Configuration configuration) throws IOException {
        clear();
        configuration.setString(structureConfigurationPrefix + ".name", name);
        configuration.setString(structureConfigurationPrefix + ".desc", description);
        configuration.setString(structureConfigurationPrefix + ".freq", frequencyExpression);
        configuration.setString(structureConfigurationPrefix + ".projectType", projectType.toString());
        configuration.setString(structureConfigurationPrefix + ".circuitType", circuitType.toString());
        configuration.setString(structureConfigurationPrefix + ".x", xExpression);
        configuration.setString(structureConfigurationPrefix + ".y", yExpression);
        configuration.setString(structureConfigurationPrefix + ".width", widthExpression);
        configuration.setString(structureConfigurationPrefix + ".height", heightExpression);
        configuration.setString(structureConfigurationPrefix + ".maxModes", maxModesExpression);
        configuration.setString(structureConfigurationPrefix + ".northWall", northWallExpression);
        configuration.setString(structureConfigurationPrefix + ".southWall", southWallExpression);
        configuration.setString(structureConfigurationPrefix + ".eastWall", eastWallExpression);
        configuration.setString(structureConfigurationPrefix + ".westWall", westWallExpression);
        configuration.setString(structureConfigurationPrefix + ".hintAxisType", hintAxisType == null ? null : hintAxisType.toString());
        configuration.setString(structureConfigurationPrefix + ".cacheEnabled", String.valueOf(cacheEnabled));
        StringBuilder ms = new StringBuilder();
        if (hintFnModes != null) {
            for (ModeType mode : hintFnModes) {
                if (mode != null) {
                    if (ms.length() > 0) {
                        ms.append(";");
                    }
                    ms.append(mode.toString());
                }
            }

        }
        configuration.setString(structureConfigurationPrefix + ".hintFnModes", ms.length() == 0 ? null : ms.toString());
        configuration.setBoolean(structureConfigurationPrefix + ".reduceMemoryUsage", reduceMemoryUsage);
        layersInfo.store(configuration, structureConfigurationPrefix + ".layers");
//        int index = 1;
        //TODO
//        for (Iterator i = allAreas.iterator(); i.hasNextImpl();) {
//            Area area = (Area) i.next();
//            area.store(configuration, prefix + ".area[" + (index + 1) + "]");
//            index++;
//        }
        MomProjectFactory.INSTANCE.store(configuration, structureConfigurationPrefix + ".root", rootAreaGroup);
        MomProjectFactory.INSTANCE.store(configuration, structureConfigurationPrefix + ".layers", layersInfo);
        configuration.setDouble(structureConfigurationPrefix + ".frequenceUnit", frequencyUnit);
        configuration.setDouble(structureConfigurationPrefix + ".dimensionUnit", dimensionUnit);
        configuration.deleteTree(structureConfigurationPrefix + ".vars", true);
        for (Iterator i = expressions.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            configuration.setString(structureConfigurationPrefix + ".vars." + entry.getKey() + ".expression", ((VariableExpression) entry.getValue()).getExpression());
            configuration.setString(structureConfigurationPrefix + ".vars." + entry.getKey() + ".desc", ((VariableExpression) entry.getValue()).getDesc());
            configuration.setString(structureConfigurationPrefix + ".vars." + entry.getKey() + ".type", ((VariableExpression) entry.getValue()).getUnit().toString());
        }
    }

    @Override
    public MomProject clone() {
        try {
            MomProject o = (MomProject) super.clone();
            o.rootAreaGroup = rootAreaGroup.clone();
            o.configuration = configuration.clone();
            o.layersInfo = layersInfo.clone();
            o.layersInfo.setContext(this);
            o.expressions = new IndexedMap<String, VariableExpression>();
            for (Map.Entry<String, VariableExpression> entry : expressions.entrySet()) {
                o.setExpression(entry.getValue().clone());
            }
            o.init();
            return o;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(MomProject.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public String getStructureConfigurationPrefix() {
        return structureConfigurationPrefix;
    }

    public void setStructureConfigurationPrefix(String newPrefix) {
        structureConfigurationPrefix = (newPrefix == null) ? DEFAULT_STRUCTURE_PREFIX : newPrefix;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean enableCache) {
        this.cacheEnabled = enableCache;
    }

    public void unsetExpressionAt(int index) {
        VariableExpression old = expressions.remove(name);
        if (old != null) {
            changeSupport.firePropertyChange("var." + old.getName(), old, null);
        }
    }

    public void unsetExpression(String name) {
        VariableExpression old = expressions.remove(name);
        changeSupport.firePropertyChange("var." + name, old, null);
    }

    public void setExpression(VariableExpression expression) {
        VariableExpression old = expressions.put(expression.getName(), expression);
        changeSupport.firePropertyChange("var." + expression.getName(), old, expression);
    }

    public void updateExpression(String name, String expression) {
        VariableExpression e = expressions.get(name);
        if (e == null) {
            throw new NoSuchElementException("Unknown variable " + name);
        }
        e = e.clone();
        e.setExpression(expression);
        VariableExpression old = expressions.put(e.getName(), e);
        changeSupport.firePropertyChange("var." + name, old, e);
    }

    public VariableExpression getVariableExpression(String varName) {
        return expressions.get(varName);
    }

    public VariableExpression getVariableExpressionAt(int index) {
        return expressions.getValueAt(index);
    }

    public int getVariableExpressionsCount() {
        return expressions.size();
    }

    public VariableExpression[] getVariableExpressions() {
        Collection<VariableExpression> c = expressions.values();
        return c.toArray(new VariableExpression[c.size()]);
    }

    public int indexOfVar(String name) {
        return expressions.indexOfKey(name);
    }

    public double getDimensionUnit() {
        return dimensionUnit;
    }

    public void setDimensionUnit(double dimensionUnit) {
        double old = this.dimensionUnit;
        this.dimensionUnit = dimensionUnit;
        changeSupport.firePropertyChange("dimensionUnit", old, this.frequencyUnit);
    }

    public double getFrequencyUnit() {
        return frequencyUnit;
    }

    public void setFrequencyUnit(double frequenceUnit) {
        double old = this.frequencyUnit;
        this.frequencyUnit = frequenceUnit;
        changeSupport.firePropertyChange("frequencyUnit", old, this.frequencyUnit);
    }

    public synchronized Object getVar(String name) {
        return evaluator.getVar(name);
    }

    public synchronized Complex getVarComplex(String name) {
        return evaluator.getVarComplex(name);
    }

    public Complex evaluateComplex(String expr) {
        return evaluator.evaluateComplex(expr);
//        return evaluate(expr, VarUnit.none);
    }

    public double evaluateDouble(String expr) {
        return evaluator.evaluateDouble(expr);
//        Complex c = evaluate(expr, VarUnit.none);
//        if (c.getImag() == 0) {
//            return c.getReal();
//        }
//        throw new ClassCastException("Expected a real value but got " + c);
    }

    public int evaluateInt(String expr) {
        return evaluator.evaluateInt(expr);
//        Complex c = evaluate(expr, VarUnit.INTEGER);
//        if (c.getImag() == 0) {
//            double d = c.getReal();
//            double r = Math.round(c.getReal());
//            if (Math.abs(d - r) >= 0.1) {
//                throw new ClassCastException("Expected an integer value but got " + c);
//            }
//            return (int) r;
//        }
//        throw new ClassCastException("Expected a real value but got " + c);
    }

    public double getVarDouble(String name) {
        return evaluator.getVarDouble(name);
    }

    public Complex getComplexVar(String name) {
        return evaluator.getVarComplex(name);
    }

    public double evaluateDimension(String expression) {
        return evaluator.evaluateDimension(expression);
    }

    public double evaluateFrequency(String expression) {
        return evaluator.evaluateFrequency(expression);
    }

    public void reset() {
        evaluator.update(this);
    }

    public Object evaluate(String expr, VarUnit unit) {
        return evaluator.evaluate(expr, unit);
    }
}
