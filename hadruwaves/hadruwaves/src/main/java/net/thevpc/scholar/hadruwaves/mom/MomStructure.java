package net.thevpc.scholar.hadruwaves.mom;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.common.mon.ProgressMonitors;

import net.thevpc.common.time.Chronometer;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NObjectElementBuilder;
import net.thevpc.scholar.hadrumaths.Vector;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.cache.CacheKey;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadrumaths.cache.PersistenceCache;
import net.thevpc.scholar.hadrumaths.geom.Geometry;
import net.thevpc.scholar.hadrumaths.geom.Polygon;
import net.thevpc.scholar.hadrumaths.io.HadrumathsIOUtils;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;
import net.thevpc.scholar.hadruwaves.*;
import net.thevpc.scholar.hadruwaves.builders.*;
import net.thevpc.scholar.hadruwaves.mom.builders.MomMatrixABuilder;
import net.thevpc.scholar.hadruwaves.mom.builders.MomMatrixBBuilder;
import net.thevpc.scholar.hadruwaves.mom.builders.MomMatrixXBuilder;
import net.thevpc.scholar.hadruwaves.mom.modes.BoxModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.project.MomProject;
import net.thevpc.scholar.hadruwaves.mom.project.MomProjectExtraLayer;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.thevpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.thevpc.scholar.hadruwaves.mom.sources.Source;
import net.thevpc.scholar.hadruwaves.mom.sources.Sources;
import net.thevpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.thevpc.scholar.hadruwaves.mom.str.MomConvergenceManager;
import net.thevpc.scholar.hadruwaves.mom.str.RequiredRebuildException;
import net.thevpc.scholar.hadruwaves.project.DefaultHWcene;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.*;
import net.thevpc.scholar.hadruwaves.str.AbstractMWStructure;
import net.thevpc.scholar.hadruwaves.str.MWStructure;
import net.thevpc.scholar.hadruwaves.str.MWStructureHintsManager;
import net.thevpc.scholar.hadruwaves.str.MomStructureHintsManager;
import net.thevpc.scholar.hadruwaves.util.Impedance;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import net.thevpc.scholar.hadrumaths.geom.GeometryList;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgoType;
import net.thevpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.thevpc.scholar.hadrumaths.meshalgo.triconsdes.MeshConsDesAlgo;
import net.thevpc.scholar.hadruplot.libraries.calc3d.math.Epsilon;
import net.thevpc.scholar.hadruwaves.mom.solver.HWSolverTemplateMoM;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateList;
import net.thevpc.scholar.hadruwaves.mom.solver.test.MomSolverTestTemplateMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpModes;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpPolyedron;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRWG;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpRooftop;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPatternType;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.project.scene.elem.Element3DPolygonTemplate;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;
import net.thevpc.scholar.hadruwaves.util.ProjectFormatter;

public class MomStructure extends AbstractMWStructure<MomStructure> implements Cloneable, ModeFunctionsEnv {

    //    public static final String HINT_AMATRIX_SYMMETRIC = "HINT_AMATRIX_SYMMETRIC";
    public static final String CACHE_ZIN = "zin";
    public static final String CACHE_MATRIX_A = "matrix-operator";
    public static final String CACHE_MATRIX_B = "matrix-src";
    public static final String CACHE_FNGP = "test-mode-scalar-products";
    public static final String CACHE_SRCGP = "test-src-scalar-products";
    public static final String CACHE_MATRIX_UNKNOWN = "matrix-unknown";
    //private double irisQuotient = 1.0 / 3.0;
    protected int fractalScale;
    protected MomStructureEvaluator evaluator = new MomStructureEvaluator(this);
    private ScalarProductOperator scalarProductOperator;
    //    private ModeFunctions modeFunctions = ModeFunctionsFactory.createBox("EMEM");
    private TestFunctions testFunctions = null;
    private Impedance serialZs = null;
    /*new GpAdaptativeMesh(
     new DefaultDPolygonList(new DomainXY(0, 0, 100, 100)),
     new DefaultDPolygonList(new DomainXY(0, 0, 100, 100)),
     GpPatternFactory.ECHELON,
     new GridPrecision(1, 1),
     null
     );*/
    private Sources sources = null;
    private StrLayer[] layers = StrLayer.NO_LAYERS;
    private WallBorders borders = WallBorders.EEEE;
    /**
     * mode functions count
     */
    private CircuitType circuitType = CircuitType.SERIAL;
    private ProjectType projectType = ProjectType.WAVE_GUIDE;
    /**
     * nombre de fonctions d'cache_essai sur chaque domaine metallique si 1, les
     * echelons selons choisis
     */
//    private int testFunctionsCount = 4;
    private BoxSpace firstBoxSpace = BoxSpace.nothing();
    private BoxSpace secondBoxSpace = BoxSpace.nothing();
    private Domain domain;
    /**
     * frequency
     */
    private double frequency;
    //    protected GpEssaiType gpEssaiType = GpEssaiType.GP_SIN_SYM;
    private CacheKey buildHash;
    private ModeFunctions modeFunctions;
    private PropertyChangeListener propertyDispatcher_modeFunctions = new DelegateModeFunctionsPropertyChangeListener();
    private PropertyChangeListener propertyDispatcher_testFunctions = new DelegateTestFunctionsPropertyChangeListener();
    private CacheResolverDelegate cacheResolver = new CacheResolverDelegate();

    public MomStructure(MomStructure other) {
        this();
        load(other);
    }

    public MomStructure() {
        setHintsManager(new MomStructureHintsManager());
        setModeFunctions(new BoxModeFunctions());
    }

    public static MomStructure PPPP(Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.PPPP, domain, frequency, modes, bottom, upper);
    }

    public static MomStructure createMomStructure(WallBorders borders, Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        MomStructure m = new MomStructure();
        m.setCircuitType(CircuitType.SERIAL);
        m.setBorders(borders);
        m.setProjectType(ProjectType.PLANAR_STRUCTURE);
        m.setFirstBoxSpace(bottom);
        m.setSecondBoxSpace(upper);
        m.setDomain(domain);
        m.setFrequency(frequency);
        m.modeFunctions().setSize(modes);
//        m.build();
        return m;
    }

    public static MomStructure EEEE(Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.EEEE, domain, frequency, modes, bottom, upper);
    }

    public static MomStructure EMEM(Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.EMEM, domain, frequency, modes, bottom, upper);
    }

    public static MomStructure MMMM(Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.MMMM, domain, frequency, modes, bottom, upper);
    }

    public static MomStructure createMomStructure(String borders, Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.valueOf(borders), domain, frequency, modes, bottom, upper);
    }

    public BoxSpace getFirstBoxSpace() {
        return firstBoxSpace;
    }

    public WallBorders getBorders() {
        return borders;
    }

    public BoxSpace getSecondBoxSpace() {
        return secondBoxSpace;
    }

    public Domain getDomain() {
        return domain;
    }

    public MomStructure setDomain(Domain newDomain) {
        Domain old = this.domain;
        this.domain = newDomain;
        firePropertyChange("domain", old, this.domain);
        return this;
    }

    public Sources getSources() {
        return sources;
    }

    public double getFrequency() {
        return frequency;
    }

    public MomStructure setFrequency(double f) {
        double old = this.frequency;
        this.frequency = f;
        firePropertyChange("frequency", old, this.frequency);
        return this;
    }

    public StrLayer[] getLayers() {
        return layers;
    }

    @Override
    public Object getHint(String name, Object defaultValue) {
        return getHintsManager().getHint(name, defaultValue);
    }

    public MomStructure setLayers(StrLayer... couches) {
        StrLayer[] old = this.layers;
        if (couches == null) {
            this.layers = StrLayer.NO_LAYERS;
        } else {
            this.layers = new StrLayer[couches.length];
            for (int i = 0; i < couches.length; i++) {
                this.layers[i] = couches[i].clone();
            }
        }
        firePropertyChange("layers", old, couches);
        invalidateCache();
        return this;
    }

    public MomStructure setSources(Sources src) {
        Sources old = this.sources;
        this.sources = src;
        firePropertyChange("sources", old, this.sources);
        return this;
    }

    public MomStructure setSources(Expr src) {
        return setSources(SourceFactory.createPlanarSource(src, Complex.of(50)));
    }

    public MomStructure setSources(Source src) {
        if (src == null) {
            return setSources((Sources) null);
        } else if (src instanceof PlanarSource) {
            return setSources(SourceFactory.createPlanarSources((PlanarSource) src));
        } else {
            throw new IllegalArgumentException("Unsupported Source Type " + src.getClass().getName());
        }

    }

    public MomStructure setSecondBoxSpace(BoxSpace secondBoxSpace) {
        BoxSpace old = this.secondBoxSpace;
        this.secondBoxSpace = secondBoxSpace;
        firePropertyChange("secondBoxSpace", old, secondBoxSpace);
        invalidateCache();
        return this;
    }

    public void setBorders(WallBorders borders) {
        if (borders == null) {
            throw new IllegalArgumentException("Unsupported null borders");
        }
        WallBorders old = this.borders;
        this.borders = borders;
        firePropertyChange("borders", old, borders);
    }

    public MomStructure setFirstBoxSpace(BoxSpace firstBoxSpace) {
        BoxSpace old = this.firstBoxSpace;
        this.firstBoxSpace = firstBoxSpace;
        firePropertyChange("firstBoxSpace", old, firstBoxSpace);
        invalidateCache();
        return this;
    }

    public MomStructureEvaluator evaluator() {
        return evaluator;
    }

    public ModeFunctions modeFunctions() {
        return this.modeFunctions;
    }

    public MomStructure setModeFunctions(ModeFunctions modeFunctions) {
        ModeFunctions old = this.modeFunctions;
        if (old != modeFunctions) {
            if (old != null) {
                old.setObjectCacheResolver(null);
                for (PropertyChangeListener propertyChangeListener : old.getPropertyChangeListeners()) {
                    if (propertyChangeListener instanceof DelegateModeFunctionsPropertyChangeListener) {
                        old.removePropertyChangeListener(propertyChangeListener);
                    }
                }
                old.removePropertyChangeListener(propertyDispatcher_modeFunctions);
                old.setEnv(null);
            }
            this.modeFunctions = modeFunctions;
            if (modeFunctions != null) {
                this.modeFunctions.setObjectCacheResolver(cacheResolver);
                this.modeFunctions.addPropertyChangeListener(propertyDispatcher_modeFunctions);
                this.modeFunctions.setEnv(this);
            }
            invalidateCache();
            firePropertyChange("modeFunctions", old, modeFunctions);
        }
        return this;
    }

    public MomStructureHintsManager getHintsManager() {
        return (MomStructureHintsManager) super.getHintsManager();
    }

    public MomStructure setHintsManager(MomStructureHintsManager hintsManager) {
        return (MomStructure) super.setHintsManager(hintsManager);
    }

    @Override
    public MomStructure setHintsManager(MWStructureHintsManager hintsManager) {
        return (MomStructure) super.setHintsManager((MomStructureHintsManager) hintsManager);
    }

    public void forceRebuild() {
        if (ProjectType.WAVE_GUIDE.equals(projectType)) {
            if (Boundary.NOTHING.equals(firstBoxSpace.getLimit())) {
                setFirstBoxSpace(BoxSpace.matchedLoad(firstBoxSpace.getMaterial()));
            }
            if (Boundary.NOTHING.equals(secondBoxSpace.getLimit())) {
                setSecondBoxSpace(BoxSpace.matchedLoad(secondBoxSpace.getMaterial()));
            }
        }
        ArrayList<DiscardFnByScalarProductModeInfoFilter> discardFnByScalarProductModeInfoFilterToRemove = new ArrayList<DiscardFnByScalarProductModeInfoFilter>();
        for (ModeInfoFilter f : modeFunctions.getModeInfoFilters()) {
            if (f instanceof DiscardFnByScalarProductModeInfoFilter) {
                discardFnByScalarProductModeInfoFilterToRemove.add((DiscardFnByScalarProductModeInfoFilter) f);
            }
        }
        for (DiscardFnByScalarProductModeInfoFilter discardFnByScalarProductModeInfoFilter : discardFnByScalarProductModeInfoFilterToRemove) {
            this.modeFunctions.removeModeInfoFilter(discardFnByScalarProductModeInfoFilter);
        }

        testFunctions = getGpTestFunctionsTemplateImpl();
        //System.out.println("4- gpTestFunctions = " + gpTestFunctions);
        if (testFunctions != null) {
            testFunctions.setStructure(this);
        }
        if (sources != null) {
            sources.setStructure(this);
        }

        Float hintDiscardFnByScalarProduct = getHintsManager().getHintDiscardFnByScalarProduct();
        ComplexMatrix fnGpScalarProducts = null;
        double fnGpMax = Double.NaN;
        if (hintDiscardFnByScalarProduct != null) {
            //TODO fix me
            ComplexMatrix spcm = createScalarProductCache(ProgressMonitors.none());
            fnGpMax = spcm.get(0, 0).absdbl();
            double cell;
            for (int i = 0; i < spcm.getRowCount(); i++) {
                ComplexVector r = spcm.getRow(i);
                for (int j = 0; j < r.size(); j++) {
                    cell = r.get(j).absdbl();
                    if (cell > fnGpMax) {
                        fnGpMax = cell;
                    }
                }
            }
            double epsl = hintDiscardFnByScalarProduct;
            ArrayList<Integer> excludedFns = new ArrayList<Integer>();
            for (int n = 0; n < spcm.getRowCount(); n++) {// toutes les fn
                boolean doExclude = true;
                ComplexVector r = spcm.getColumn(n);
                for (int j = 0; j < r.size(); j++) {
                    Complex cc = r.get(j);
                    if ((cc.absdbl() / fnGpMax) > epsl) {
                        doExclude = false;
                        break;
                    }
                }
                if (doExclude) {
                    excludedFns.add(n);
                }
            }
            if (excludedFns.size() > 0) {
                for (ModeInfoFilter modeInfoFilter : this.modeFunctions.getModeInfoFilters()) {
                    if (modeInfoFilter instanceof DiscardFnByScalarProductModeInfoFilter) {
                        this.modeFunctions.removeModeInfoFilter(modeInfoFilter);
                    }
                }
                this.modeFunctions.addModeInfoFilter(new DiscardFnByScalarProductModeInfoFilter(excludedFns));
            }
        }
        buildHash = CacheKey.of(toElement());
    }

    public MomStructure load(MWStructure st) {
        if (st != null) {
            super.load(st);
            if (st instanceof MomStructure) {
                MomStructure other = (MomStructure) st;
                setFractalScale(other.fractalScale);
                this.borders = other.getBorders();
                this.firstBoxSpace = other.getFirstBoxSpace();
                this.secondBoxSpace = other.getSecondBoxSpace();
                setLayers(other.getLayers());
                this.fractalScale = other.fractalScale;
                this.evaluator.setFrom(other.evaluator());
                this.scalarProductOperator = other.scalarProductOperator;
                this.testFunctions = other.testFunctions.clone();
                this.sources = other.sources.clone();
                this.layers = Arrays.copyOf(other.layers, other.layers.length);
                this.circuitType = other.circuitType;
                this.borders = other.borders;
                this.projectType = other.projectType;
                this.firstBoxSpace = other.firstBoxSpace;
                this.secondBoxSpace = other.secondBoxSpace;
                setDomain(other.domain);
                setFrequency(other.frequency);
                setModeFunctions(other.modeFunctions().clone());
                this.serialZs = other.serialZs;
                invalidateCache();
            }
        }
        return this;
    }

    public String dump() {
        build();
        return toElement().toString(false);
    }

    @Override
    public MomStructure clone() {
        return new MomStructure(this);
    }

//    /**
//     * updates frequency to reach a xdim over lambda of wol
//     *
//     * @param wol
//     */
//    public MomStructure setFrequencyByWOL(double wol) {
//        //width=C*wol/freq;
//        return setFrequency(Maths.C * wol / getXwidth());
//    }
//
//    /**
//     * set xdim as fraction Width over WaveLength
//     *
//     * @param wol xdim over wavelength
//     */
//    public MomStructure setWidthByWOL(double wol) {
//        //freq * xdim/C=wol;
//        return setXwidth(wol / getFrequency() * Maths.C);
//    }
    protected TestFunctions getGpTestFunctionsTemplateImpl() {
        return testFunctions == null ? null : testFunctions.clone();
    }

    public ComplexMatrix createScalarProductCache(ProgressMonitor monitor) {
        build();
        return modeFunctions.scalarProduct(Maths.evector(testFunctions.arr()), monitor);
    }

    public Impedance getSerialZs() {
        return serialZs;
    }

    public MomStructure setSerialZs(Impedance serialZs) {
        Impedance old = this.serialZs;
        this.serialZs = serialZs;
        firePropertyChange("serialZs", old, serialZs);
        return this;
    }

    public MomStructure loadProject(String projectFile) throws ParseException, IOException {
        loadProject(new MomProject(new File(Maths.Config.expandPath(projectFile))));
        return this;
    }

    public MomStructure loadProject(MomProject structureConfig) {
        getPersistentCache().setRootFolder(HadrumathsIOUtils.createHFile(structureConfig.getConfigFile().getParent() + "/" + structureConfig.getConfigFile().getName() + ".cache"));
        structureConfig.recompile();
        getHintsManager().setHintFnMode(structureConfig.getHintFnModes());
        //setHintDiscardFnByScalarProduct();
        getHintsManager().setHintAxisType(structureConfig.getHintAxisType());

        setCircuitType(structureConfig.getCircuitType());
        setDomain(structureConfig.getDomain());///? non gere ?
        setProjectType(structureConfig.getProjectType());
        ArrayList<StrLayer> ll = new ArrayList<StrLayer>();
        for (MomProjectExtraLayer projectLayer : structureConfig.getLayers().getExtraLayers()) {
            ll.add(new StrLayer(projectLayer.getWidth(), projectLayer.getImpedance()));
        }
        setLayers(ll.toArray(new StrLayer[0]));
        setFirstBoxSpace(new BoxSpace(
                structureConfig.getLayers().getTopLimit(),
                new Material("first", structureConfig.getLayers().getTopEpsr(), 1, structureConfig.getLayers().getTopConductivity()),
                structureConfig.getLayers().getTopThickness()
        )
        );
        setSecondBoxSpace(new BoxSpace(
                structureConfig.getLayers().getBottomLimit(),
                new Material("second", structureConfig.getLayers().getBottomEpsr(), 1, structureConfig.getLayers().getBottomConductivity()),
                structureConfig.getLayers().getBottomThickness()
        )
        );
        modeFunctions().setSize(structureConfig.getMaxModes());
        setFrequency(structureConfig.getFrequency());
        getHintsManager().setHintRegularZnOperator(structureConfig.isHintRegularZOperator() ? Boolean.TRUE : null);
        Float aFloat = structureConfig.getHintDiscardFnByScalarProduct() <= 0 ? null : structureConfig.getHintDiscardFnByScalarProduct();
        getHintsManager().setHintDiscardFnByScalarProduct(aFloat);
        getHintsManager().setHintAMatrixSparcify(aFloat);
        getHintsManager().setHintBMatrixSparcify(aFloat);
        setTestFunctions(structureConfig.getGpTestFunctions());
        setSources(structureConfig.getSources());
        getPersistentCache().setEnabled(structureConfig.isCacheEnabled());
//        setPropagativeModeSelector(structureConfig.getBox().boxType);//
//        setFractalScale(structureConfig.getBox().boxType);//
//        setHintDiscardFnByScalarProduct(structureConfig.getBox().y);//
//        setHintFnMode(structureConfig.getBox().y);//
//        setHintAxisType(structureConfig.getBox().y);//
//        setHintRegularZnOperator(structureConfig.getBox().y);//
        return this;
    }

    public MomStructure loadProject(File projectFile) throws ParseException, IOException {
        loadProject(new MomProject(projectFile));
        return this;
    }

    private String dump0() {
        return toElement().toString(false);
//        return getDumpStringHelper().toString();
    }

    @Override
    public NElement toElement() {
        NObjectElementBuilder sb = NElement.ofObjectBuilder(getClass().getSimpleName());
        sb.addAll(new NElement[]{NElement.ofPair("version", NElementHelper.elem("3.0"))});
        sb.add("projectType", NElementHelper.elem(projectType));
        sb.add("circuitType", NElementHelper.elem(circuitType));
        sb.add("frequency", NElementHelper.elem(frequency));
        sb.add("domain", NElementHelper.elem(domain));
        sb.add("borders", NElementHelper.elem(borders));

        sb.add("firstBoxSpace", NElementHelper.elem(firstBoxSpace));
        sb.add("secondBoxSpace", NElementHelper.elem(secondBoxSpace));

        sb.add("testFunctions", NElementHelper.elem(testFunctions));
//        sb.addElement("testFunctions.count", context.defaultObjectToElement(testFunctionsCount));
        sb.add("modeFunctions", NElementHelper.elem(modeFunctions).toObject().get().builder().removeEntry("environment").build());
        sb.add("sources", NElementHelper.elem(sources));
        sb.add("layers", NElementHelper.elem(getLayers()));

        if (serialZs != null) {
            sb.add("serialZs", NElementHelper.elem(serialZs));
        }
        if (fractalScale != 0) {
            sb.add("fractalScale", NElementHelper.elem(fractalScale));
        }
        sb.add("builders", NElementHelper.elem(evaluator()));
//        sb.add("evaluator", NElementHelper.elem(evaluator()));
        sb.add("scalarProductOperator", NElementHelper.elem(getScalarProductOperator()));

        sb.add("parameters", NElementHelper.elem(getParameters()));
        sb.add("hints", NElementHelper.elem(getHintsManager()));
        return sb.build();
    }

    public ScalarProductOperator getScalarProductOperator() {
        return getScalarProductOperator(true);
    }

    public MomStructure setScalarProductOperator(ScalarProductOperator scalarProductOperator) {
        this.scalarProductOperator = scalarProductOperator;
        return this;
    }

    public ScalarProductOperator getScalarProductOperator(boolean useDefault) {
        if (scalarProductOperator != null) {
            return scalarProductOperator;
        }
        if (!useDefault) {
            return null;
        }
        return Maths.Config.getScalarProductOperator();
    }

    public MomStructure copy() {
        return clone();
    }

    public int getFractalScale() {
        return fractalScale;
    }

    public MomStructure setFractalScale(int fractalScale) {
        int old = this.fractalScale;
        this.fractalScale = fractalScale;
        firePropertyChange("fractalScale", old, fractalScale);
        return this;
    }

    public void initComputation(ProgressMonitor computationMonitor) {
        ObjectCache objectCache = getObjectCache();
        if (objectCache != null) {
            testFunctions.arr(computationMonitor, objectCache);
            this.modeFunctions.getModes(computationMonitor);
        }
    }

    public double waveLength() {
        return Physics.waveLength(getFrequency());
    }

    public MomStructure frequency(double f) {
        return setFrequency(f);
    }

    @Override
    public String toString() {
        return ((fractalScale != 0) ? (" fractalScale=" + fractalScale) : "");
    }

    @Override
    public Domain domain() {
        return getDomain();
    }

    @Override
    public CurrentBuilder current() {
        build();
        return new DefaultCurrentBuilder(this);
    }

    @Override
    public SourceBuilder source() {
        build();
        return new DefaultSourceBuilder(this);
    }

    @Override
    public TestFieldBuilder testField() {
        build();
        return new DefaultTestFieldBuilder(this);
    }

    @Override
    public ElectricFieldBuilder electricField() {
        return new DefaultElectricFieldBuilder(this);
    }

    @Override
    public PoyntingVectorBuilder poyntingVector() {
        return new DefaultPoyntingVectorBuilder(this);
    }

    @Override
    public DirectivityBuilder directivity() {
        return new DefaultDirectivityBuilder(this);
    }

    @Override
    public FarFieldBuilder farField() {
        return new DefaultFarFieldBuilder(this);
    }

    public MagneticFieldBuilder magneticField() {
        build();
        return new DefaultMagneticFieldBuilder(this);
    }

    @Override
    public CapacityBuilder capacity() {
        return new DefaultCapacityBuilder(this);
    }

    @Override
    public SelfBuilder self() {
        return new DefaultSelfBuilder(this);
    }

    @Override
    public InputImpedanceBuilder inputImpedance() {
        return new DefaultInputImpedanceBuilder(this);
    }

    @Override
    public SParametersBuilder sparameters() {
        return new DefaultSParametersBuilder(this);
    }

    @Override
    public ObjectCache getCurrentCache(boolean autoCreate) {
        if (getPersistentCache().isEnabled()) {
            return getPersistentCache().getObjectCache(getKey(), autoCreate);
        }
        return null;
    }

    @Override
    public CacheKey getKey() {
        build();
        return buildHash;
    }

    public DoubleToVector[] fn() {
        return modeFunctions().fn();
    }

    public TestFunctions getGpTestFunctionsTemplate() {
        return getGpTestFunctionsTemplateImpl();
    }

    public MomStructure setSource(Expr src) {
        return setSources(src);
    }

    public MomStructure addSource(Source src) {
        if (src instanceof PlanarSource) {
            if (this.sources == null) {
                setSources(SourceFactory.createPlanarSources((PlanarSource) src));
            } else if (this.sources instanceof PlanarSources) {
                PlanarSources p = (PlanarSources) sources;
                ArrayList<PlanarSource> n = new ArrayList<PlanarSource>(Arrays.asList(p.getPlanarSources()));
                n.add((PlanarSource) src);
                setSources(SourceFactory.createPlanarSources(n.toArray(new PlanarSource[0])));
            } else {
                throw new IllegalArgumentException("Not supported");
            }
        } else {
            throw new IllegalArgumentException("Not supported");
        }
        return this;
    }

    public MomStructure source(Expr src) {
        return setSources(src);
    }

    public MomStructure sources(Expr src) {
        return setSources(src);
    }

    public MomStructure sources(Expr src, Complex characteristicImpedance) {
        return setSources(src, characteristicImpedance);
    }

    public MomStructure setSources(Expr src, Complex characteristicImpedance) {
        return setSources(SourceFactory.createPlanarSource(src, characteristicImpedance == null ? Complex.of(50) : characteristicImpedance));
    }

    public MomStructure setSources(Expr src, double characteristicImpedance) {
        return setSources(src, Complex.of(characteristicImpedance));
    }

    public MomStructure sources(Source src) {
        return setSources(src);
    }

    public TestFunctions testFunctions() {

        return testFunctions;
    }

    public MomStructure setTestFunctions(Vector<Expr> expr) {
        return setTestFunctions(TestFunctionsFactory.createList().addAll(expr));
    }

    public MomStructure setTestFunctions(TestFunctions testFunctions) {
        TestFunctions old = this.testFunctions;
        if (old != null) {
            old.removePropertyChangeListener(propertyDispatcher_testFunctions);
        }
        this.testFunctions = testFunctions;
        if (this.testFunctions != null) {
            this.testFunctions.setStructure(this);
        }
        if (this.testFunctions != null) {
            this.testFunctions.addPropertyChangeListener(propertyDispatcher_testFunctions);
        }
        firePropertyChange("testFunctions", old, testFunctions);
        invalidateCache();
        return this;
    }

    public MomStructure testFunctions(TestFunctions gpEssaiType) {
        return setTestFunctions(gpEssaiType);
    }

    public MomStructure testFunctions(Vector<Expr> expr) {
        return setTestFunctions(expr);
    }

    public double toXForDomainCoeff(double domainCoeff) {
        Domain domainXY = getDomain();
        return domainXY.xmin() + domainCoeff * domainXY.xwidth();
    }

    public double[] toXForDomainCoeff(double[] domainCoeff) {
        Domain domainXY = getDomain();
        double[] ret = new double[domainCoeff.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = domainXY.xmin() + domainCoeff[i] * domainXY.xwidth();
        }
        return ret;
    }

    /**
     * @param domainCoeff (Z en fonction de a)
     * @return Z absolu
     */
    public double[] toZForDomainCoeff(double[] domainCoeff) {
        Domain domainXY = getDomain();
        double[] ret = new double[domainCoeff.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = domainCoeff[i] * domainXY.xwidth();
        }
        return ret;
    }

    public double toYForDomainCoeff(double domainCoeff) {
        Domain domainXY = getDomain();
        return domainXY.ymin() + domainCoeff * domainXY.ywidth();
    }

    public double[] toYForDomainCoeff(double[] domainCoeff) {
        Domain domainXY = getDomain();
        double[] ret = new double[domainCoeff.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = domainXY.ymin() + domainCoeff[i] * domainXY.ywidth();
        }
        return ret;
    }

    public final ComplexMatrix testModeScalarProducts() {
        return getTestModeScalarProducts();
    }

    public final ComplexMatrix getTestModeScalarProducts() {
        return getTestModeScalarProducts(null);
    }

    public final ComplexMatrix getTestModeScalarProducts(ProgressMonitor monitor) {
        build();
        return modeFunctions().scalarProduct(Maths.evector(testFunctions.arr()), monitorOf("TestModeScalarProducts", monitor));
    }

//    protected ProgressMonitor createDefaultMonitor(String name) {
//        ProgressMonitorFactory m = getMonitorFactory();
//        return m == null ? ProgressMonitors.none() : m.createMonitor(name, null);
//    }

    public final ComplexMatrix testModeScalarProducts(ProgressMonitor monitor) {
        return getTestModeScalarProducts(monitor);
    }

    public final ComplexMatrix testSourceScalarProducts(ProgressMonitor monitor) {
        return getTestSourceScalarProducts(monitor);
    }

    public final ComplexMatrix getTestSourceScalarProducts(ProgressMonitor monitor) {
        final ProgressMonitor monitor0 = monitorOf("TestSourceScalarProducts", monitor);
        build();
        return new SrcGpScalarProductCacheStrCacheSupport(this, monitor0).get();
    }

    public final ComplexMatrix testSourceScalarProducts() {
        return getTestSourceScalarProducts();
    }

    public final ComplexMatrix getTestSourceScalarProducts() {
        return getTestSourceScalarProducts(null);
    }

    public CircuitType getCircuitType() {
        return circuitType;
    }

    public MomStructure setCircuitType(CircuitType circuitType) {
        this.circuitType = circuitType;
        invalidateCache();
        return this;
    }

    public MomStructure circuitType(CircuitType circuitType) {
        return setCircuitType(circuitType);
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public MomStructure setProjectType(ProjectType projectType) {
        this.projectType = projectType;
        invalidateCache();
        return this;
    }

    public MomStructure asPlanarStructure() {
        return setProjectType(ProjectType.PLANAR_STRUCTURE);
    }

    public MomStructure asWaveguide() {
        return setProjectType(ProjectType.WAVE_GUIDE);
    }

    public MomStructure projectType(ProjectType projectType) {
        return setProjectType(projectType);
    }

    public MomStructure firstBoxSpace(BoxSpace firstBoxSpace) {
        return setFirstBoxSpace(firstBoxSpace);
    }

    public MomStructure lastBoxSpace(BoxSpace secondBoxSpace) {
        return setSecondBoxSpace(secondBoxSpace);
    }

    public PersistenceCache getDerivedPersistentCache(String name) {
        return getPersistentCache().derive(getKey(), name);
    }

    public InverseStrategy getInvStrategy() {
        return (InverseStrategy) getParameter("InverseStrategy", InverseStrategy.DEFAULT);
    }

    public MomStructure setInvStrategy(InverseStrategy i) {
        setParameterNotNull("InverseStrategy", i);
        return this;
    }

    public NormStrategy getNormStrategy() {
        return (NormStrategy) getParameter("NormStrategy", NormStrategy.DEFAULT);
    }

    public MomStructure setNormStrategy(NormStrategy i) {
        setParameterNotNull("NormStrategy", i);
        return this;
    }

    public ConditioningStrategy getCondStrategy() {
        return (ConditioningStrategy) getParameter("ConditioningStrategy", ConditioningStrategy.NONE);
    }

    public MomStructure setCondStrategy(ConditioningStrategy enable) {
        setParameterNotNull("ConditioningStrategy", enable);
        return this;
    }

    public void checkBuildIsRequired() throws RequiredRebuildException {
        boolean found = new MatrixXMatrixStrCacheSupport2(this, monitorOf("checkBuildIsRequired",null)).isCached();
        if (!found) {
            throw new RequiredRebuildException();
        }
    }

    public long getExecutionTime(String type) {
        Chronometer chrono = Chronometer.start();
        if (CACHE_SRCGP.equals(type)) {
            getTestSourceScalarProducts();//insure it is calculated
        } else if (CACHE_FNGP.equals(type)) {
            getTestModeScalarProducts();//insure it is calculated
        } else if (CACHE_MATRIX_A.equals(type)) {
            matrixA().evalMatrix();//insure it is calculated
        } else if (CACHE_MATRIX_B.equals(type)) {
            matrixB().evalMatrix();//insure it is calculated
        } else if (CACHE_MATRIX_UNKNOWN.equals(type)) {
            matrixX().evalMatrix();//insure it is calculated
        } else if (CACHE_ZIN.equals(type)) {
            inputImpedance().evalMatrix();//insure it is calculated
        } else {
            throw new IllegalArgumentException("Unknown type use one of (" + CACHE_SRCGP + "," + CACHE_FNGP + "," + CACHE_MATRIX_A + "," + CACHE_MATRIX_B + "," + CACHE_MATRIX_UNKNOWN + "," + CACHE_ZIN + ")");
        }
        chrono.stop();
        if (getPersistentCache().isEnabled()) {
            return getCurrentCache(true).getStat(type);
        } else {
            return chrono.getTime();
        }
    }

    public MomMatrixABuilder matrixA() {
        ObjectCache cc = getPersistentCache().getObjectCache(getKey(), true);
        if (cc != null) {
            cc.addSetItem("name", NElement.ofString(getName()));
        }
        return new DefaultMomMatrixABuilder(this);
    }

    public MomMatrixBBuilder matrixB() {
        return new DefaultMomMatrixBBuilder(this);
    }

    public MomMatrixXBuilder matrixX() {
        return new DefaultMomMatrixXBuilder(this);

    }

    public Collection<MomCache> getSimilarCaches(String property) {
        ArrayList<MomCache> found = new ArrayList<MomCache>();
        for (Iterator<ObjectCache> i = getPersistentCache().iterator(); i.hasNext();) {
            ObjectCache c = i.next();
            MomCache mc = new MomCache(c);
            Map<String, String> indexes = mc.parseCacheValues();
            MomStructure s = this.clone();
            String val = indexes.get(property);
            if ("circuitType".equals(property)) {
                s.setCircuitType(CircuitType.valueOf(val));
            } else if ("projectType".equals(property)) {
                s.setProjectType(ProjectType.valueOf(val));
            } else if ("frequency".equals(property)) {
                s.setFrequency(Double.valueOf(val));
            } else if ("domain".equals(property)) {
                //TODO!! FIX ME
                //s.setDomain(Double.valueOf(val));
            }
            if (s.getKey().equals(c.getKey())) {
                found.add(mc);
            }
        }
        return found;
    }

    public Collection<MomCache> getAllCaches() {
        ArrayList<MomCache> found = new ArrayList<MomCache>();
        for (Iterator<ObjectCache> i = getPersistentCache().iterator(); i.hasNext();) {
            ObjectCache c = i.next();
            found.add(new MomCache(c));
        }
        return found;
    }

    public MomConvergenceManager createMomConvergenceManager() {
        return new MomConvergenceManager(this);
    }

    public ModeInfo mode(int n) {
        return modeFunctions().getMode(n);
    }

    public ModeInfo getMode(int n) {
        return modeFunctions().getMode(n);
    }

    public ModeInfo getMode(ModeType mode, int m, int n) {
        return modeFunctions().getMode(mode, m, n);
    }

    public synchronized ModeInfo[] modes() {
        return getModes();
    }

    public synchronized ModeInfo[] getModes() {
        return getModes(null);
    }

    public synchronized ModeInfo[] getModes(ProgressMonitor monitor) {
        return modeFunctions().getModes(monitor);
    }

    public synchronized ModeInfo[] modes(ProgressMonitor monitor) {
        return getModes(monitor);
    }

    public synchronized Complex[] modesImpedances() {
        return getModesImpedances();
    }

    public synchronized Complex[] getModesImpedances() {
        ModeInfo[] modes = getModes();
        Complex[] impedances = new Complex[modes.length];
        for (int i = 0; i < modes.length; i++) {
            impedances[i] = modes[i].impedance.impedanceValue();
        }
        return impedances;
    }

    public void borders(String wallBorders) {
        setBorders(WallBorders.valueOf(wallBorders));
    }

    public HWProjectScene createScene() {
        return createScene(null);
    }

    public HWProjectScene createScene(HWProject project) {
        Domain domain = getDomain();
        Domain domain3D = domain.toDomainXYZ();
        domain3D = domain3D.expandPercent(0.1f);
        double zinf = SceneHelper.resolveInfinityValue(domain);
        HWProjectScene scene = new DefaultHWcene(project);
//        Point3D p=new Point3D(-2,-2,-2);
//        Point3D p=new Point3D(-2,-2,-2);
//        scene.components().add(new HWProjectElementMaterial(
//                new Material("dielectric",
//                        firstBoxSpace.getEpsr(),
//                        1,
//                        firstBoxSpace.getElectricConductivity()
//                ),
//                SceneHelper.createBrick(
//                        new Point3D(0, 0,0).add(p),
//                        new Point3D(1, 0, 0).add(p),
//                        new Point3D(0, 1, 0).add(p),
//                        new Point3D(0, 0, 1).add(p)
//                )
//        ));
//        if(true){
//            return scene;
//        }
        double zw = 0;
        Boundary zmaterial = null;

        zw = firstBoxSpace.getWidth();
        zmaterial = firstBoxSpace.getLimit();
        double zeroZlevel = 0 - Epsilon.E;
        if (zw != 0) {
            domain3D = Domain.ofBounds(domain3D.xmin(), domain3D.xmax(), domain3D.ymin(), domain3D.ymax(), -zw, zeroZlevel);
            HWProjectBrick mb = new HWProjectBrick("Bottom Space", new HWMaterialTemplate(firstBoxSpace.getMaterial(), project),
                    SceneHelper.createBrickTemplate(
                            new DomainTemplate(
                                    Domain.ofBounds(domain.xmin(), domain.xmax(), domain.ymin(), domain.ymax(), -zw, zeroZlevel))
                    ));
//            mb.setFaceMaterial(5, firstBoxSpace.getMaterial());
            mb.face(HWProjectBrick.Face.BOTTOM).boundary().set(zmaterial.name());
            Boundary[] walls = getBorders().toArray();
            for (int i = 0; i < walls.length; i++) {
                switch (walls[i]) {
                    case ELECTRIC: {
                        mb.face(HWProjectBrick.Face.values()[i]).boundary().set(Boundary.ELECTRIC.name());
                        break;
                    }
                    case MAGNETIC: {
                        mb.face(HWProjectBrick.Face.values()[i]).boundary().set(Boundary.MAGNETIC.name());
                        break;
                    }
                    case PERIODIC: {
                        mb.face(HWProjectBrick.Face.values()[i]).boundary().set(Boundary.PERIODIC.name());
                        break;
                    }
                }
            }
            mb.visible().userObjects().put("visible-of-BottomSpace-" + project.name().get(), "visible-of-BottomSpace-" + project.name().get());
            scene.components().add(mb);
        }
        double h = 0;

        HWMaterialTemplate PEC_TEMPLATE = project.materials().get("PEC");//new HWMaterialTemplate(Material.PEC, project);
        HWMaterialTemplate VACUUM_TEMPLATE = project.materials().get("Vacuum");// new HWMaterialTemplate(Material.VACUUM, project);
        if (true) {
            LinkedHashSet<Polygon> polygons = new LinkedHashSet<>();
            for (Geometry geometry : testFunctions().getGeometries()) {
                Collections.addAll(polygons, geometry.toPolygons());
            }
            int index = 0;
            for (Polygon polygon : polygons) {
                index++;
                scene.components().add(new HWProjectPolygon(
                        "Microstrip Element #" + index, PEC_TEMPLATE,
                        SceneHelper.createPolygonTemplate(polygon, 0)));
            }
        }
        if (true) {
            HWSolverTemplateMoM mom = new HWSolverTemplateMoM();
            mom.frequency().set(ProjectFormatter.formatFrequency(project, getFrequency(), ProjectFormatter.Mode.LONG));
            mom.modesCount().set(String.valueOf(this.modeFunctions().count()));
            mom.circuitType().set(String.valueOf(this.getCircuitType()));
            if (testFunctions() instanceof GpModes) {
                GpModes gp = (GpModes) testFunctions();
                MomSolverTestTemplateMesh m = new MomSolverTestTemplateMesh();
                m.complexity().set(String.valueOf(gp.getPattern().getCount()));
                m.pattern().set(String.valueOf(GpPatternType.MODES));
                m.mesh().set(String.valueOf(MeshAlgoType.RECT));
                for (Element3DPolygonTemplate polygon : polygonsOf(gp)) {
                    m.polygons().add(new HWProjectPolygon(CACHE_ZIN, PEC_TEMPLATE, polygon));
                }
                mom.testFunctions().add(m);
            } else if (testFunctions() instanceof GpRWG) {
                GpRWG gp = (GpRWG) testFunctions();
                MomSolverTestTemplateMesh m = new MomSolverTestTemplateMesh();
                m.complexity().set(String.valueOf(((MeshConsDesAlgo) gp.getMeshAlgo()).getOption().getMaxTriangles()));
                m.pattern().set(String.valueOf(GpPatternType.RWG));
                m.mesh().set(String.valueOf(MeshAlgoType.TRIANGLE_CONS_DES));
                m.symmetry().set(String.valueOf(gp.getSymmetry()));
                m.invariance().set(String.valueOf(gp.getInvariance()));
                for (Element3DPolygonTemplate polygon : polygonsOf(gp)) {
                    m.polygons().add(new HWProjectPolygon(CACHE_ZIN, PEC_TEMPLATE, polygon));
                }
                mom.testFunctions().add(m);
            } else if (testFunctions() instanceof GpPolyedron) {
                GpPolyedron gp = (GpPolyedron) testFunctions();
                MomSolverTestTemplateMesh m = new MomSolverTestTemplateMesh();
                m.complexity().set(String.valueOf(((MeshConsDesAlgo) gp.getMeshAlgo()).getOption().getMaxTriangles()));
                m.pattern().set(String.valueOf(GpPatternType.POLYEDRON));
                m.mesh().set(String.valueOf(MeshAlgoType.TRIANGLE_CONS_DES));
                m.symmetry().set(String.valueOf(gp.getSymmetry()));
                m.invariance().set(String.valueOf(gp.getInvariance()));
                for (Element3DPolygonTemplate polygon : polygonsOf(gp)) {
                    m.polygons().add(new HWProjectPolygon(CACHE_ZIN, PEC_TEMPLATE, polygon));
                }
                mom.testFunctions().add(m);
            } else if (testFunctions() instanceof GpRooftop) {
                GpRooftop gp = (GpRooftop) testFunctions();
                MomSolverTestTemplateMesh m = new MomSolverTestTemplateMesh();
                MeshAlgoRect rr = (MeshAlgoRect) gp.getMeshAlgo();
                m.complexity().set(String.valueOf(1));
                m.pattern().set(String.valueOf(GpPatternType.ROOFTOP));
                m.mesh().set(String.valueOf(MeshAlgoType.RECT));
                m.symmetry().set(String.valueOf(gp.getSymmetry()));
                m.invariance().set(String.valueOf(gp.getInvariance()));
                for (Element3DPolygonTemplate polygon : polygonsOf(gp)) {
                    m.polygons().add(new HWProjectPolygon(CACHE_ZIN, PEC_TEMPLATE, polygon));
                }
                mom.testFunctions().add(m);
            } else {
                TestFunctions gp = (TestFunctions) testFunctions();
                MomSolverTestTemplateList m = new MomSolverTestTemplateList();
                int findex = 1;
                for (DoubleToVector ex : gp.arr()) {
                    WritablePExpression<Expr> ees = Props2.of("fct_" + findex).exprOf(Maths.expr(0));
                    String fctExpr = ex.toString();
                    Expr pe = Maths.parseExpression(fctExpr);
                    ees.set(fctExpr);
                    m.expressions().add(ees);
                    findex++;
                }
                m.complexity().set(String.valueOf(m.expressions().size()));
                mom.testFunctions().add(m);
            }
            if (project != null) {
                project.configurations().activeConfiguration().get().solver().set(mom);
            }
        }

        if (true) {
            for (StrLayer layer : getLayers()) {
                scene.components().add(new HWProjectBrick(
                        layer.getName(), VACUUM_TEMPLATE,
                                SceneHelper.createBrickTemplate(
                                        new DomainTemplate(
                                                Domain.ofBounds(domain.xmin(), domain.xmax(), domain.ymax(), domain.ymax(), h, h + layer.getWidth())
                                        )))
                );
            }
        }

        if (true) {
            zw = secondBoxSpace.getWidth();
            zmaterial = secondBoxSpace.getLimit();

            if (zw != 0) {
                HWProjectBrick mb = new HWProjectBrick(
                        "Top Space", new HWMaterialTemplate(secondBoxSpace.getMaterial(), project),
                        SceneHelper.createBrickTemplate(
                                new DomainTemplate(Domain.ofBounds(domain.xmin(), domain.xmax(), domain.ymin(), domain.ymax(), h, h + zw))
                        ));
                mb.visible().userObjects().put("visible-of-TopSpace-" + project.name().get(), "visible-of-TopSpace-" + project.name().get());
                mb.face(HWProjectBrick.Face.TOP).boundary().set(zmaterial.name());
                Boundary[] walls = getBorders().toArray();
                for (int i = 0; i < walls.length; i++) {
                    switch (walls[i]) {
                        case ELECTRIC: {
                            mb.face(HWProjectBrick.Face.values()[i]).boundary().set(Boundary.ELECTRIC.name());
                            break;
                        }
                        case MAGNETIC: {
                            mb.face(HWProjectBrick.Face.values()[i]).boundary().set(Boundary.MAGNETIC.name());
                            break;
                        }
                        case PERIODIC: {
                            mb.face(HWProjectBrick.Face.values()[i]).boundary().set(Boundary.PERIODIC.name());
                            break;
                        }
                    }
                }
                scene.components().add(mb);
            }
        }
        if (true) {
            Sources sources = getSources();
            if (sources instanceof PlanarSources) {
                for (DoubleToVector sourceFunction : ((PlanarSources) sources).getSourceFunctions()) {
                    LinkedHashSet<Polygon> polygons = new LinkedHashSet<>();
                    for (Geometry geometry : sourceFunction.getDomain().toGeometry().toPolygons()) {
                        Collections.addAll(polygons, geometry.toPolygons());
                    }
                    int index = 0;
                    for (Polygon polygon : polygons) {
                        index++;
                        scene.components().add(new HWPlanarPort(
                                                                                "Planar Source #" + index, SceneHelper.createPolygonTemplate(polygon, 0)));
                    }
                }
            } else if (sources instanceof ModalSources) {
                LinkedHashSet<Polygon> polygons = new LinkedHashSet<>();
                for (Geometry geometry : Domain.ofBounds(domain3D.xmin(), domain3D.xmax(), domain3D.ymin(), domain3D.ymax(), domain3D.zmin(), h + zw)
                        .toGeometry().toPolygons()) {
                    Collections.addAll(polygons, geometry.toPolygons());
                }
                int index = 0;
                for (Polygon polygon : polygons) {
                    index++;
                    scene.components().add(
                            new HWModalPort(
                                    SceneHelper.createPolygonTemplate(polygon, 0),
                                    "Modal Source #" + index));
                }
            }
        }
        domain3D = domain3D.expandPercent(0.1f);
        scene.domain().set(domain3D);
        if (project != null) {
            project.scene().set(scene);
        }
        return scene;
    }

    protected ProgressMonitor monitorOf(String name, ProgressMonitor other) {
        if (other == null) {
            ProgressMonitorFactory f = getMonitorFactory();
            if (f != null) {
                other = f.createMonitor(name, null);
            } else {
                other = ProgressMonitors.none();
            }
        }
        return other;
    }

    private List<Element3DPolygonTemplate> polygonsOf(GpAdaptiveMesh gp) {
        List<Element3DPolygonTemplate> ret = new ArrayList<>();
        LinkedHashSet<Polygon> polygons = new LinkedHashSet<Polygon>();

        for (GeometryList polygon : gp.getPolygons()) {
            for (Geometry geometry : polygon) {
                for (Polygon pp : geometry.toPolygons()) {
                    polygons.add(pp);
                }
            }
        }
        for (Polygon polygon : polygons) {
            ret.add(new Element3DPolygonTemplate(polygon));
        }
        return ret;
    }

    private class CacheResolverDelegate implements ObjectCacheResolver {

        @Override
        public ObjectCache resolveObjectCache() {
            return getCurrentCache(true);
        }
    }

    private class DelegateModeFunctionsPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange("modeFunctions." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }

    private class DelegateTestFunctionsPropertyChangeListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange("testFunctions." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    }
}
