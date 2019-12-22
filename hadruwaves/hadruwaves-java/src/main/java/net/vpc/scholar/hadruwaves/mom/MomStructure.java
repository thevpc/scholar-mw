package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.mon.ProgressMonitorCreator;
import net.vpc.common.util.Chronometer;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.cache.CacheAware;
import net.vpc.scholar.hadrumaths.cache.HashValue;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.cache.PersistenceCache;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.console.ConsoleLogger;
import net.vpc.scholar.hadruplot.console.NullConsoleLogger;
import net.vpc.scholar.hadruplot.console.params.ParamTarget;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.io.HadrumathsIOUtils;
import net.vpc.scholar.hadrumaths.util.dump.Dumpable;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;
import net.vpc.scholar.hadruwaves.*;
import net.vpc.scholar.hadruwaves.builders.*;
import net.vpc.scholar.hadruwaves.mom.builders.MomMatrixABuilder;
import net.vpc.scholar.hadruwaves.mom.builders.MomMatrixBBuilder;
import net.vpc.scholar.hadruwaves.mom.builders.MomMatrixXBuilder;
import net.vpc.scholar.hadruwaves.mom.modes.DefaultBoxModeFunctions;
import net.vpc.scholar.hadruwaves.mom.project.MomProject;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectExtraLayer;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSource;
import net.vpc.scholar.hadruwaves.mom.sources.PlanarSources;
import net.vpc.scholar.hadruwaves.mom.sources.Source;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;
import net.vpc.scholar.hadruwaves.mom.str.*;
import net.vpc.scholar.hadruwaves.mom.str.momstr.*;
import net.vpc.scholar.hadruwaves.str.*;
import net.vpc.scholar.hadruwaves.util.Impedance;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.text.ParseException;
import java.util.*;

public class MomStructure implements MWStructure, Serializable, Cloneable, Dumpable, ConsoleAwareObject, CacheAware {

    //    public static final String HINT_AMATRIX_SYMMETRIC = "HINT_AMATRIX_SYMMETRIC";
    public static final String CACHE_ZIN = "zin";
    public static final String CACHE_MATRIX_A = "matrix-operator";
    public static final String CACHE_MATRIX_B = "matrix-src";
    public static final String CACHE_FNGP = "test-mode-scalar-products";
    public static final String CACHE_SRCGP = "test-src-scalar-products";
    public static final String CACHE_MATRIX_UNKNOWN = "matrix-unknown";
    //private double irisQuotient = 1.0 / 3.0;
    protected int fractalScale;
    protected PersistenceCache persistentCache;
    protected MomStructureMemoryCache memoryCache;
    private HashMap<String, Object> parameters = new HashMap<String, Object>();
    private MomStructureHintsManager hintsManager;
    private ElectricFieldEvaluator electricFieldEvaluator;
    private FarFieldEvaluator farFieldEvaluator;
    private ElectricFieldFundamentalEvaluator electricFieldFundamentalEvaluator;
    private CurrentEvaluator currentEvaluator;
    private TestFieldEvaluator testFieldEvaluator;
    private PoyntingVectorEvaluator poyntingVectorEvaluator;
    private MagneticFieldEvaluator magneticFieldEvaluator;
    private MatrixAEvaluator matrixAEvaluator;
    private MatrixBEvaluator matrixBEvaluator;
    private SourceEvaluator sourceEvaluator;
    private ZinEvaluator zinEvaluator;
    private MatrixUnknownEvaluator matrixUnknownEvaluator;
    private ScalarProductOperator scalarProductOperator;
    /**
     * user objects are not included in the dump and could be used as extra info
     * on structure extra info should not influence structure characteristics
     * calculation
     */
    private HashMap<String, Object> userObjects = new HashMap<String, Object>();
    //    private ModeFunctions modeFunctions = ModeFunctionsFactory.createBox("EMEM");
    private TestFunctions testFunctions = null;
    private Impedance serialZs = Physics.impedance(Complex.ZERO);
    /*new GpAdaptativeMesh(
     new DefaultDPolygonList(new DomainXY(0, 0, 100, 100)),
     new DefaultDPolygonList(new DomainXY(0, 0, 100, 100)),
     GpPatternFactory.ECHELON,
     new GridPrecision(1, 1),
     null
     );*/
    private Sources sources = null;
    /**
     * domaine de la structure
     */
    //    private transient HashMap<String, Object> hints = new HashMap<String, Object>();
    private ConsoleLogger log = NullConsoleLogger.INSTANCE;
    private StrLayer[] layers = StrLayer.NO_LAYERS;
    /**
     * mode functions count
     */
    private CircuitType circuitType = CircuitType.SERIAL;
    private int modeFunctionsMax;
    private ProjectType projectType = ProjectType.WAVE_GUIDE;
    /**
     * nombre de fonctions d'cache_essai sur chaque domaine metallique si 1, les
     * echelons selons choisis
     */
//    private int testFunctionsCount = 4;
    private BoxSpace firstBoxSpace = new BoxSpace(BoxLimit.NOTHING, 1, -1,0);
    private BoxSpace secondBoxSpace = new BoxSpace(BoxLimit.NOTHING, 1, -1,0);
    private Domain domain;
    /**
     * frequency
     */
    private double frequency;
    //    protected GpEssaiType gpEssaiType = GpEssaiType.GP_SIN_SYM;
    private String name;
    private boolean rebuild = true;
    private HashValue buildHash;
    private ProgressMonitorCreator monitor;
    private MWStructureErrorHandler errorHandler = new DefaultMomStructureErrorHandler();
    private PropertyChangeSupport pcs;
    private ModeFunctionsDelegate modeFunctionsDelegate = new ModeFunctionsDelegate(null);
    private PropertyChangeListener propertyDispatcher_modeFunctions = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange("modeFunctions." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    };
    private PropertyChangeListener propertyDispatcher_testFunctions = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            firePropertyChange("testFunctions." + evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
        }
    };

    public MomStructure() {
        init();
    }

    private void init() {
        persistentCache = new PersistenceCache(null, "structure.dump", null);
        hintsManager = new MomStructureHintsManager();
        memoryCache = new MomStructureMemoryCache(this);
        pcs = new PropertyChangeSupport(this);
        modeFunctionsDelegate.setBase(ModeFunctionsFactory.createBox("EMEM"));
        modeFunctionsDelegate.setStructure(this);
        modeFunctionsDelegate.setObjectCacheResolver(new ObjectCacheResolver() {
            @Override
            public ObjectCache resolveObjectCache() {
                return getCurrentCache(true);
            }
        });
        this.modeFunctionsDelegate.addPropertyChangeListener(propertyDispatcher_modeFunctions);
    }

    public MomStructure(MomStructure other) {
        init();
        if (other != null) {

            this.fractalScale = other.fractalScale;
//            this.persistentCache;
//            this.memoryCache;
            this.parameters.putAll(other.parameters);
            ;
            this.hintsManager.setAll(other.getHintsManager());
            this.electricFieldEvaluator = other.electricFieldEvaluator;
            this.farFieldEvaluator = other.farFieldEvaluator;
            this.electricFieldFundamentalEvaluator = other.electricFieldFundamentalEvaluator;
            this.currentEvaluator = other.currentEvaluator;
            this.testFieldEvaluator = other.testFieldEvaluator;
            this.poyntingVectorEvaluator = other.poyntingVectorEvaluator;
            this.magneticFieldEvaluator = other.magneticFieldEvaluator;
            this.matrixAEvaluator = other.matrixAEvaluator;
            this.matrixBEvaluator = other.matrixBEvaluator;
            this.sourceEvaluator = other.sourceEvaluator;
            this.zinEvaluator = other.zinEvaluator;
            this.matrixUnknownEvaluator = other.matrixUnknownEvaluator;
            this.scalarProductOperator = other.scalarProductOperator;
            this.userObjects.putAll(other.userObjects);
//            this.modeFunctions = this.modeFunctions.clone();
            this.testFunctions = other.testFunctions.clone();
            this.sources = other.sources.clone();
            this.log = other.log;
            this.layers = Arrays.copyOf(other.layers, other.layers.length);
            this.circuitType = other.circuitType;
            this.modeFunctionsMax = other.modeFunctionsMax;
            this.projectType = other.projectType;
            this.firstBoxSpace = other.firstBoxSpace;
            this.secondBoxSpace = other.secondBoxSpace;
            this.domain = other.domain;
            this.frequency = other.frequency;
            this.name = other.name;
//            this.rebuild = true;
//            this.buildHash;
            this.errorHandler = other.errorHandler;
            //this.pcs;
            this.modeFunctionsDelegate.setBase(other.getModeFunctions().clone());
            this.serialZs =other.serialZs;
            this.monitor =other.monitor;
        }
    }

    @Override
    public ProgressMonitorCreator getMonitor() {
        return monitor;
    }

    @Override
    public ProgressMonitorCreator monitor() {
        return getMonitor();
    }

    @Override
    public MomStructure setMonitor(ProgressMonitorCreator monitor) {
        this.monitor = monitor;
        return this;
    }
    @Override
    public MomStructure monitor(ProgressMonitorCreator monitor) {
        setMonitor(monitor);
        return this;
    }

    public static MomStructure PPPP(Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.PPPP, domain, frequency, modes, bottom, upper);
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

    public static MomStructure createMomStructure(WallBorders borders, Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        MomStructure m = new MomStructure();
        m.setCircuitType(CircuitType.SERIAL);
        m.setProjectType(ProjectType.PLANAR_STRUCTURE);
        m.setFirstBoxSpace(bottom);
        m.setSecondBoxSpace(upper);
        m.setDomain(domain);
        m.setFrequency(frequency);
        m.setModeFunctionsCount(modes);
        m.setModeFunctions(ModeFunctionsFactory.createBox(borders));
        m.build();
        return m;
    }

    public static MomStructure createMomStructure(String borders, Domain domain, double frequency, int modes, BoxSpace bottom, BoxSpace upper) {
        return createMomStructure(WallBorders.valueOf(borders), domain, frequency, modes, bottom, upper);
    }

    public Impedance getSerialZs() {
        return serialZs;
    }

    public MomStructure setSerialZs(Impedance serialZs) {
        this.serialZs = serialZs ==null? Physics.impedance(Complex.ZERO): serialZs;
        return this;
    }

    public MomStructureHintsManager getHintsManager() {
        return hintsManager;
    }

    public MomStructure loadProject(String projectFile) throws ParseException, IOException {
        loadProject(new MomProject(new File(Maths.Config.expandPath(projectFile))));
        return this;
    }

    public MomStructure loadProject(File projectFile) throws ParseException, IOException {
        loadProject(new MomProject(projectFile));
        return this;
    }

    public MomStructure loadProject(MomProject structureConfig) {
        persistentCache.setRootFolder(HadrumathsIOUtils.createHFile(structureConfig.getConfigFile().getParent() + "/" + structureConfig.getConfigFile().getName() + ".cache"));
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
                structureConfig.getLayers().getTopEpsr(),
                structureConfig.getLayers().getTopThickness(),
                structureConfig.getLayers().getTopConductivity()
                )
        );
        setSecondBoxSpace(new BoxSpace(
                structureConfig.getLayers().getBottomLimit(),
                structureConfig.getLayers().getBottomEpsr(),
                structureConfig.getLayers().getBottomThickness(),
                structureConfig.getLayers().getBottomConductivity()
                )
        );
        setModeFunctions(new DefaultBoxModeFunctions(structureConfig.getWallBorders()));
        setModeFunctionsCount(structureConfig.getMaxModes());
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

    public MomStructure load(MomStructure other) {
        setFractalScale(other.fractalScale);
        this.modeFunctionsMax = other.modeFunctionsMax;
        this.domain = other.domain;
        this.frequency = other.frequency;

        this.setTestFunctions(other.getGpTestFunctionsTemplate());
        this.setSources(other.getSources());
        if (this.sources != null) {
            this.sources = this.sources.clone();
        }
        this.modeFunctionsDelegate.setBase(other.getModeFunctionsTemplate());
        this.parameters = (HashMap<String, Object>) other.parameters.clone();
        this.firstBoxSpace = other.getFirstBoxSpace();
        this.secondBoxSpace = other.getSecondBoxSpace();
        setLayers(other.getLayers());
        this.persistentCache.setAll(other.getPersistentCache());
        this.invalidateCache();
        return this;
    }

    public HashValue hashValue() {
        build();
        return buildHash;
    }

    public String dump() {
        build();
        return getDumpStringHelper().toString();
    }

    private String dump0() {
        return getDumpStringHelper().toString();
    }

    protected Dumper getDumpStringHelper() {
        //        build();
        Dumper sb = new Dumper(getClass().getSimpleName());
        sb.add("this", getClass().getSimpleName() + "-v2.0");
        sb.add("projectType", projectType);
        sb.add("circuitType", circuitType);
        sb.add("frequency", frequency);
        sb.add("domain", domain);

        sb.add("firstBoxSpace", firstBoxSpace);
        sb.add("secondBoxSpace", secondBoxSpace);

        sb.add("testFunctions", testFunctions);
//        sb.add("testFunctions.count", testFunctionsCount);
        sb.add("modeFunctions", modeFunctionsDelegate.getBase());
        sb.add("modeFunctions.count", modeFunctionsMax);
        sb.add("sources", sources);
        if(serialZs !=null){
            sb.add("serialZs", serialZs);
        }
        if(fractalScale!=0){
            sb.add("fractalScale", fractalScale);
        }
        if (getLayers() != null && getLayers().length > 0) {
            sb.add("layers", getLayers());
        }
        Map<String, Object> builders = new HashMap<String, Object>();
        if (matrixAEvaluator != null) {
            builders.put("matrixA", matrixAEvaluator);
        }
        if (matrixBEvaluator != null) {
            builders.put("matrixB", matrixBEvaluator);
        }
        if (matrixUnknownEvaluator != null) {
            builders.put("matrixUnknown", matrixUnknownEvaluator);
        }
        if (electricFieldEvaluator != null) {
            builders.put("electricField", electricFieldEvaluator);
        }
        if (farFieldEvaluator != null) {
            builders.put("farField", farFieldEvaluator);
        }
        if (electricFieldFundamentalEvaluator != null) {
            builders.put("electricFieldFundamental", electricFieldFundamentalEvaluator);
        }
        if (currentEvaluator != null) {
            builders.put("current", currentEvaluator);
        }
        if (testFieldEvaluator != null) {
            builders.put("testField", testFieldEvaluator);
        }
        if (poyntingVectorEvaluator != null) {
            builders.put("poyntingVector", poyntingVectorEvaluator);
        }
        if (magneticFieldEvaluator != null) {
            builders.put("magneticField", magneticFieldEvaluator);
        }
        if (zinEvaluator != null) {
            builders.put("zin", zinEvaluator);
        }
        if (builders.size() > 0) {
            sb.add("builders", builders);
        }
        if (parameters.size() > 0) {
            sb.add("parameters", parameters);
        }
        if (!hintsManager.isEmpty()) {
            sb.add("hints", hintsManager);
        }
        sb.add("scalarProductOperator", getScalarProductOperator());
        return sb;
    }

    public MomStructure copy() {
        return clone();
    }

    @Override
    public MomStructure clone() {
        return new MomStructure(this);
    }

    public int getModeFunctionsCount() {
        return modeFunctionsMax;
    }

    public MomStructure setModeFunctionsCount(int modeFunctionsMax) {
        int old = this.modeFunctionsMax;
        this.modeFunctionsMax = modeFunctionsMax;
        if (old != this.modeFunctionsMax) {
            modeFunctionsDelegate.setMaxSize(this.modeFunctionsMax);
        }
        firePropertyChange("modeFunctionsMax", old, modeFunctionsMax);
        //cache_essai = null;
        return this;
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


    public MomStructure invalidateCache() {
        rebuild = true;
        memoryCache.reset();
        return this;
    }

    /**
     * @return true if revalidate was really performed
     */
    public boolean build() {
        if (rebuild) {
            forceRebuild();
            rebuild = false;
            return true;
        }
        return false;
    }

    public void forceRebuild() {
        if (ProjectType.WAVE_GUIDE.equals(projectType)) {
            if (BoxLimit.NOTHING.equals(firstBoxSpace.getLimit())) {
                firstBoxSpace = BoxSpaceFactory.matchedLoad(firstBoxSpace.getEpsr(),firstBoxSpace.getElectricConductivity());
                //TODO
                //System.err.println("firstBoxSpace overridden to " + firstBoxSpace);
            }
            if (BoxLimit.NOTHING.equals(secondBoxSpace.getLimit())) {
                secondBoxSpace = BoxSpaceFactory.matchedLoad(secondBoxSpace.getEpsr(),firstBoxSpace.getElectricConductivity());
                //TODO
                //System.err.println("secondBoxSpace overridden to " + secondBoxSpace);
            }
        }

        this.modeFunctionsDelegate.setBase(getModeFunctionsTemplate());
        ArrayList<DiscardFnByScalarProductModeInfoFilter> discardFnByScalarProductModeInfoFilterToRemove = new ArrayList<DiscardFnByScalarProductModeInfoFilter>();
        for (ModeInfoFilter f : modeFunctionsDelegate.getModeInfoFilters()) {
            if (f instanceof DiscardFnByScalarProductModeInfoFilter) {
                discardFnByScalarProductModeInfoFilterToRemove.add((DiscardFnByScalarProductModeInfoFilter) f);
            }
        }
        for (DiscardFnByScalarProductModeInfoFilter discardFnByScalarProductModeInfoFilter : discardFnByScalarProductModeInfoFilterToRemove) {
            this.modeFunctionsDelegate.removeModeInfoFilter(discardFnByScalarProductModeInfoFilter);
        }

        applyModeFunctionsChanges(this.modeFunctionsDelegate);

        testFunctions = getGpTestFunctionsTemplateImpl();
        //System.out.println("4- gpTestFunctions = " + gpTestFunctions);
        if (testFunctions != null) {
            testFunctions.setStructure(this);
        }
        if (sources != null) {
            sources.setStructure(this);
        }

        Float hintDiscardFnByScalarProduct = getHintsManager().getHintDiscardFnByScalarProduct();
        TMatrix<Complex> fnGpScalarProducts = null;
        double fnGpMax = Double.NaN;
        if (hintDiscardFnByScalarProduct != null) {
            //TODO fix me
            TMatrix<Complex> spcm = createScalarProductCache(ProgressMonitorFactory.none());
            fnGpMax = spcm.get(0, 0).absdbl();
            double cell;
            for (int i = 0; i < spcm.getRowCount(); i++) {
                TVector<Complex> r = spcm.getRow(i);
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
                TVector<Complex> r = spcm.getColumn(n);
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
                this.modeFunctionsDelegate.addModeInfoFilter(new DiscardFnByScalarProductModeInfoFilter(excludedFns));
            }
        }
        buildHash = new HashValue(dump0());

    }

    public ObjectCache getCurrentCache(boolean autoCreate) {
        if (getPersistentCache().isEnabled()) {
            return getPersistentCache().getObjectCache(hashValue(), autoCreate);
        }
        return null;
    }

    private ObjectCache getObjectCache() {
        if (getPersistentCache().isEnabled()) {
            ObjectCache objectCache = getPersistentCache().getObjectCache(buildHash, true);
            if (objectCache != null) {
                return objectCache;
            }
        }
        return null;
    }

    public void initComputation(ProgressMonitor computationMonitor) {
        ObjectCache objectCache = getObjectCache();
        if (objectCache != null) {
            testFunctions.arr(computationMonitor, objectCache);
            this.modeFunctionsDelegate.getModes(computationMonitor);
        }
    }

    public double getLambda() {
        return Physics.lambda(getFrequency());
    }

    public double getFrequency() {
        return frequency;
    }

    public MomStructure setFrequency(double f) {
        double old = this.frequency;
        this.frequency = f;
        firePropertyChange("frequency", old, this.frequency);
        return this;
        //        cache_essai = null;
    }

    public MomStructure frequency(double f) {
        return setFrequency(f);
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

    @Override
    public String toString() {
        return "modesCount=" + modeFunctionsMax+((fractalScale!=0)?(" ; fractalScale=" + fractalScale):"");
    }


    public InputImpedanceBuilder inputImpedance() {
        return new DefaultInputImpedanceBuilder(this);
    }

    @Override
    public SParametersBuilder sparameters() {
        return new DefaultSParametersBuilder(this);
    }

    @Override
    public CapacityBuilder capacity() {
        return new DefaultCapacityBuilder(this);
    }

    @Override
    public SelfBuilder self() {
        return new DefaultSelfBuilder(this);
    }

    public MomMatrixABuilder matrixA() {
        ObjectCache cc = getPersistentCache().getObjectCache(hashValue(), true);
        if (cc != null) {
            cc.addSetItem("name", getName());
        }
        return new DefaultMomMatrixABuilder(this);
    }

    public MomMatrixBBuilder matrixB() {
        return new DefaultMomMatrixBBuilder(this);
    }


    public MomMatrixXBuilder matrixX() {
        return new DefaultMomMatrixXBuilder(this);

    }
//    public Matrix computeFixedPoint(Matrix Znext) {
//        throw new NoSuchElementException();
//    }
//
    //    public final computeJTest(double[] x, double y, Axis axis, ProgressMonitor monitor) {
//        final double[] x0=x;
//        final double[] y0=y;
//        final double[] z0=z;
//        final Axis axis0=axis;
//        final ProgressMonitor monitor0=monitor==null?ProgressMonitor.none:monitor;
//        DumpHelper p=new DumpHelper("computeEBase").add("x",x).add("y",y).add("z",z).add("axis",axis);
//        return new StrSubCacheSupport<Complex[][][]>("EBase",p.toString()) {
//            public Complex[][][] compute() {
//                return computeEBaseImpl(x0,y0,z0,axis0,monitor0);
//            }
//        }.computeCached(null);
//    }
    //    public Complex[] computeEBase(double[] x, double y, double z0, Axis axis) {
//        //this.invalidateCache();
//        if (false) {
//            return computeEBaseOld(x, y, z0, axis);
//        }
//
//        CFunctionXY2D[] _g = getGpTestFunctions().gp();
//
//        CMatrix Testcoeff = computeMatrixUnknown();
//        CArray r = new CArray(x.length, ZERO);
//        Complex[] J = Testcoeff.getColumn(0).getStructure2();
//        ModeInfo[] n_eva = isParameter(HINT_REGULAR_ZN_OPERATOR) ? fnBaseFunctions.getModes() : fnBaseFunctions.getVanishingModes();
//        ScalarProductCache sp = getTestModeScalarProducts();
//        int axisOrdinal = axis.ordinal();
//        for (int j = 0; j < _g.length; j++) {
//            for (ModeInfo aN_eva : n_eva) {
//                r.addSelf(J[j]
//                        .multiply(aN_eva.fn.F[axisOrdinal].compute(x, y, null))
//                        .multiply(aN_eva.zn)
//                        .multiply(sp.gf(j, aN_eva.index))
//                        .multiply(exp(aN_eva.firstBoxSpaceGamma.multiply(-z0))));
//            }
//        }
//        //TODO pkoa 2ero c tout ? c'est pas nbSources?
//        CArray E0 = exp(n_eva[0].firstBoxSpaceGamma.multiply(-z0)).multiply(n_eva[0].fn.F[axisOrdinal].compute(x, y, null));
//        r.addSelf(E0);
//        return r.getArray();
//    }
    //    public Complex[][] computeEBase_before20070513(double[] x, double y, double[] z0, Axis axis) {
////        this.invalidateCache();
//        CFunctionVector2D[] _g = getGpTestFunctions().gp();
//
//        CMatrix Testcoeff = computeMatrixUnknown();
//
//
//        Complex[] J = Testcoeff.getColumn(0).getStructure2();
//
//        ModeInfo[] n_eva = fnBaseFunctions.getVanishingModes();
//        Complex[][] rets = new Complex[z0.length][];
//        ScalarProductCache sp = getTestModeScalarProducts();
//        int axisOrdinal = axis.ordinal();
//        for (int i = 0; i < z0.length; i++) {
//            CArray r = new CArray(x.length, ZERO);
//            double v = z0[i];
//            for (int j = 0; j < _g.length; j++) {
//                for (ModeInfo aN_eva : n_eva) {
//                    r.addSelf(J[j]
//                            .multiply(aN_eva.fn.F[axisOrdinal].compute(x, y, null))
//                            .multiply(aN_eva.zn)
//                            .multiply(sp.gf(j, aN_eva.index))
//                            .multiply(exp(aN_eva.firstBoxSpaceGamma.multiply(-v))));
//                }
//            }
//            //TODO pkoa 2ero c tout ? c'est pas nbSources?
//            //TO pourkoa -v?????
//
//            r.addSelf(exp(n_eva[0].firstBoxSpaceGamma.multiply(-v)).multiply(n_eva[0].fn.F[axisOrdinal].compute(x, y, null)));
//            rets[i] = r.getArray();
//        }
//        return rets;
    //    }

    @Override
    public ElectricFieldBuilder electricField() {
        return electricField(ElectricFieldPart.FULL);
    }

    public ElectricFieldBuilder electricField(final ElectricFieldPart part) {
        return new DefaultElectricFieldBuilder(this, part);
    }

    @Override
    public FarFieldBuilder farField() {
        return new DefaultFarFieldBuilder(this);
    }

    public MagneticFieldBuilder magneticField() {
        build();
        return new DefaultMagneticFieldBuilder(this);
    }

    public PoyntingVectorBuilder poyntingVector() {
        build();
        return new DefaultPoyntingVectorBuilder(this);
    }


    @Override
    public CurrentBuilder current() {
        build();
        return new DefaultCurrentBuilder(this);
    }

    @Override
    public TestFieldBuilder testField() {
        build();
        return new DefaultTestFieldBuilder(this);
    }


    @Override
    public SourceBuilder source() {
        build();
        return new DefaultSourceBuilder(this);
    }


//    public Complex[][] computePlanarSources(double[] x, double[] y, Axis axis) {
//        return computePlanarSources(x, y, axis, null);
//    }
//
//    public Complex[][] computePlanarSources(double[] x, double[] y, Axis axis, ProgressMonitor monitor) {
//        monitor = ProgressMonitorFactory.makeNotNull(monitor);
//        final double[] x0 = x == null ? new double[]{0} : x;
//        final double[] y0 = y == null ? new double[]{0} : y;
//        final Axis axis0 = axis;
//        final ProgressMonitor monitor0 = monitor == null ? ProgressMonitor.none : monitor;
//        Dumper p = new Dumper("computePlanarSources").add("x", x).add("y", y).add("axis", axis);
//        return new StrSubCacheSupport<Complex[][]>("sources-planar", p.toString()) {
//
//            @Override
//            public Complex[][] compute(ObjectCache momCache) {
//                return computePlanarSourcesImpl(x0, y0, axis0, monitor0);
//            }
//        }.computeCached();
//    }
//
//    public final Complex[][] computePlanarSources(Samples sampler, Axis axis) {
//        return computePlanarSources(sampler, axis, null);
//    }

//    public final Complex[][] computePlanarSources(Samples sampler, Axis axis, ProgressMonitor monitor) {
//        monitor = ProgressMonitorFactory.makeNotNull(monitor);
//        return computePlanarSources(sampler.getX(), sampler.getY(), axis, monitor);
//    }


    protected MagneticFieldEvaluator createMagneticFieldEvaluator() {
        MagneticFieldEvaluator builder = magneticFieldEvaluator;
        if (builder == null) {
            builder = DefaultMagneticFieldEvaluator.INSTANCE;
        }
        return builder;
    }

    protected PoyntingVectorEvaluator createPoyntingVectorEvaluator() {
        PoyntingVectorEvaluator builder = poyntingVectorEvaluator;
        if (builder == null) {
            builder = DefaultPoyntingVectorEvaluator.INSTANCE;
        }
        return builder;
    }

    // Cube[z][x][y]
    protected ElectricFieldEvaluator createElectricFieldEvaluator() {
        ElectricFieldEvaluator builder = electricFieldEvaluator;
        if (builder == null) {
            switch (circuitType) {
                case SERIAL: {
                    builder = ElectricFieldSerialEvaluator.INSTANCE;
                    break;
                }
                case PARALLEL: {
                    builder = ElectricFieldParallelEvaluator.INSTANCE;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown circuit type " + circuitType);
                }
            }
        }
        return builder;
    }

    protected FarFieldEvaluator createFarFieldEvaluator() {
        FarFieldEvaluator builder = farFieldEvaluator;
        if (builder == null) {
            //
            builder = FarFieldEvaluatorPEC.INSTANCE;
        }
        return builder;
    }

    protected ElectricFieldFundamentalEvaluator createElectricFieldFundamentalEvaluator() {

        return electricFieldFundamentalEvaluator == null ? ElectricFieldFundamentalSerialParallelEvaluator.INSTANCE : electricFieldFundamentalEvaluator;
    }

    protected CurrentEvaluator createCurrentEvaluator() {
        CurrentEvaluator builder = currentEvaluator;
        if (builder == null) {
            switch (circuitType) {
                case SERIAL: {
                    builder = CurrentSerialEvaluator.INSTANCE;
                    break;
                }
                case PARALLEL: {
                    builder = CurrentParallelEvaluator.INSTANCE;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown circuit type " + circuitType);
                }
            }
        }
        return builder;
    }

    protected TestFieldEvaluator createTestFieldEvaluator() {
        TestFieldEvaluator builder = testFieldEvaluator;
        if (builder == null) {
            builder = TestFieldSerialParallelEvaluator.INSTANCE;
        }
        return builder;
    }

    //    public double getMetalQuotient() {
//        return (1 - irisQuotient) / 2.0;
//    }
//
//    public double getIrisQuotient() {
//        return irisQuotient;
//    }
//
//    public void setIrisQuotient(double irisQuotient) {
//        if (this.irisQuotient != irisQuotient) {
//            if (irisQuotient < 0 || irisQuotient > 1) {
//                throw new IllegalArgumentException("IrisQuotient must be in [0..1]");
//            }
//            this.irisQuotient = irisQuotient;
//            this.invalidateCache();
//        }
    //    }
    public MomStructure setModeFunctions(String wallBorders) {
        setModeFunctions(ModeFunctionsFactory.createBox(wallBorders));
        return this;
    }

    public MomStructure modeFunctions(String wallBorders) {
        setModeFunctions(wallBorders);
        return this;
    }
    public MomStructure modeFunctions(WallBorders wallBorders) {
        setModeFunctions(wallBorders);
        return this;
    }

    public MomStructure setModeFunctions(WallBorders wallBorders) {
        setModeFunctions(ModeFunctionsFactory.createBox(wallBorders));
        return this;
    }

    public Domain getDomain() {
        return domain;
    }

    @Override
    public Domain domain() {
        return getDomain();
    }

    public MomStructure setDomain(Domain newDomain) {
        Domain old = this.domain;
        this.domain=newDomain;
        firePropertyChange("domain", old, this.domain);
        return this;
    }

    public DoubleToVector[] fn() {
        return getModeFunctions().fn();
    }

    public WallBorders getBorders() {
        return this.modeFunctionsDelegate.getBorders();
    }
    public ModeFunctions getModeFunctions() {
        build();
//        if (build()) {
//            ObjectCache cc = getCurrentCache(true);
//            if (cc != null) {
//                if (!cc.exists("mode-functions")) {
//                    cc.store("mode-functions", modeFunctions.arr());
//                }
//            }
//        }
        return this.modeFunctionsDelegate.getBase();
    }

    public MomStructure modeFunctions(ModeFunctions modeFunctions) {
        setModeFunctions(modeFunctions);
        return this;
    }

    public MomStructure setModeFunctions(ModeFunctions modeFunctions) {
        ModeFunctions old = this.modeFunctionsDelegate.getBase();
        modeFunctionsDelegate.setBase(modeFunctions.clone());
        firePropertyChange("modeFunctions", old, this.modeFunctionsDelegate.getBase());
        return this;
    }

    public TestFunctions getGpTestFunctionsTemplate() {
//        build();
        return getGpTestFunctionsTemplateImpl();
    }

    protected TestFunctions getGpTestFunctionsTemplateImpl() {
        return testFunctions == null ? null : testFunctions.clone();
    }

    public MomStructureHintsManager getHintsManagerTemplate() {
//        build();
        return hintsManager.clone();
    }

    public Sources getSources() {
        return sources == null ? null : sources.clone();
    }

    public MomStructure setSources(Sources src) {
        Sources old = this.sources;
        this.sources = src;
        firePropertyChange("sources", old, this.sources);
        return this;
    }

    public MomStructure setSource(Expr src) {
        return setSources(src);
    }

    public MomStructure setSources(Expr src) {
        return setSources(SourceFactory.createPlanarSource(src, Complex.valueOf(50)));
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
        return setSources(SourceFactory.createPlanarSource(src, characteristicImpedance == null ? Complex.valueOf(50) : characteristicImpedance));
    }

    public MomStructure sources(Source src) {
        return setSources(src);
    }

    public ModeFunctions getModeFunctionsTemplate() {
        ModeFunctions modeFunctions = modeFunctionsDelegate.getBase();
        if (modeFunctions == null) {
            throw new NoSuchElementException("Missing ModeFunctions");
        }
        return modeFunctions.clone();
    }

    public TestFunctions getTestFunctions() {
//        if (build()) {
//            ObjectCache cc = getCurrentCache(true);
//            if (cc != null) {
//                if (!cc.exists("test-functions")) {
//                    cc.store("test-functions", testFunctions.arr());
//                }
//            }
//        }
        return testFunctions;
    }

    public MomStructure setTestFunctions(TVector<Expr> expr) {
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

    //    public Project2Fract_SerieMagn(int fmax, int k, double f, double a, double b, boolean symmetricTestFct) {
//        this.maxFn = fmax;
//        this.k = k;
//        this.f = f;
//        this.a = a;
//        this.b = b;
//        this.symmetricTestFct = symmetricTestFct;
//        this.domain = new Domain(0, a, 0, b);
//        omega = omega(f);
//        K0 = K0(f);// omega * Math.sqrt(U0 * EPS0);//nombre d'onde
//    }
    //    public GpEssaiType getGpEssaiType() {
//        return gpEssaiType;
//    }
    //
    public MomStructure testFunctions(TVector<Expr> expr) {
        return setTestFunctions(expr);
    }

    //    public Object getParameter(Class clazz, boolean required) {
//        Object o = parameters.get(clazz);
//        if (required && o == null) {
//            throw new IllegalArgumentException("Parameter " + clazz.getSimpleName() + " required");
//        }
//        return o;
//    }
//    Complex[][] resolveE(double[] x,double[] z) {
//       CMatrix J =getTestcoeffImpl();
//       Complex[][] r=null;
    //    }
    public String getParametersString() {
        return parameters.values().toString();
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

//    public int getConvergenceFn() {
//        build();
//        FnConvergenceInfo i = getFnConvergenceInfo();
//        if (i != null && i.isEnabled() && getTestFunctions().arr().length > 0) {
//            return getConvergenceFn(i.getMaxCount(), i.getStep(), i.getMaxError());
//        }
//        return -1;
//    }

    public final TMatrix<Complex> getTestModeScalarProducts() {
        return getTestModeScalarProducts(null);
    }

    public final TMatrix<Complex> getTestModeScalarProducts(ProgressMonitor monitor) {
        build();
        return getModeFunctions().scalarProduct(Maths.elist(testFunctions.arr()), monitor);
    }

    public final TMatrix<Complex> getTestSourceScalarProducts() {
        return getTestSourceScalarProducts(null);
    }

    public final TMatrix<Complex> getTestSourceScalarProducts(ProgressMonitor monitor) {
        final ProgressMonitor monitor0 = ProgressMonitorFactory.nonnull(monitor);
        build();
        return new SrcGpScalarProductCacheStrCacheSupport(this, monitor0).get();
    }

    public ConsoleLogger getLog() {
        return log;
    }

    public MomStructure setLog(ConsoleLogger log) {
        this.log = log;
        return this;
    }

    public MomStructure setParameter(String name) {
        setParameter(name, Boolean.TRUE);
        return this;
    }

    public MomStructure setParameterNotNull(String name, Object value) {
        if (value == null) {
            removeParameter(name);
        } else {
            setParameter(name, value);
        }
        return this;
    }

    public MomStructure setParameter(String name, Object value) {
        parameters.put(name, value);
        invalidateCache();
        return this;
    }

    public MomStructure removeParameter(String name) {
        parameters.remove(name);
        invalidateCache();
        return this;
    }

    public Object getParameter(String name) {
        return parameters.get(name);
    }

    public Object getParameter(String name, Object defaultValue) {
        if (parameters.containsKey(name)) {
            return parameters.get(name);
        }
        return defaultValue;
    }

    public Number getParameterNumber(String name, Number defaultValue) {
        return (Number) getParameter(name, defaultValue);
    }

    public boolean containsParameter(String name) {
        return parameters.containsKey(name);
    }

    public boolean isParameter(String name, boolean defaultValue) {
        if (parameters.containsKey(name)) {
            return Boolean.TRUE.equals(parameters.get(name));
        } else {
            return defaultValue;
        }
    }

    public boolean isParameter(String name) {
        return Boolean.TRUE.equals(parameters.get(name));
    }

    public ParamTarget getTarget() {
        ParamTarget t = (ParamTarget) getUserObject("Target");
        return t == null ? ParamTarget.REFERENCE : t;
    }

    public MomStructure setTarget(ParamTarget target) {
        setUserObject("Target", target);
        return this;
    }

    public CircuitType getCircuitType() {
        return circuitType;
    }

    public MomStructure circuitType(CircuitType circuitType) {
        return setCircuitType(circuitType);
    }

    public MomStructure setCircuitType(CircuitType circuitType) {
        this.circuitType = circuitType;
        invalidateCache();
        return this;
    }

    public ProjectType getProjectType() {
        return projectType;
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

    public MomStructure setProjectType(ProjectType projectType) {
        this.projectType = projectType;
        invalidateCache();
        return this;
    }

    public BoxSpace getFirstBoxSpace() {
        return firstBoxSpace;
    }

    public MomStructure firstBoxSpace(BoxSpace firstBoxSpace) {
        return setFirstBoxSpace(firstBoxSpace);
    }

    public MomStructure lastBoxSpace(BoxSpace secondBoxSpace) {
        return setSecondBoxSpace(secondBoxSpace);
    }

    public MomStructure setFirstBoxSpace(BoxSpace firstBoxSpace) {
        this.firstBoxSpace = firstBoxSpace;
        invalidateCache();
        return this;
    }

    public BoxSpace getSecondBoxSpace() {
        return secondBoxSpace;
    }

    public MomStructure setSecondBoxSpace(BoxSpace secondBoxSpace) {
        this.secondBoxSpace = secondBoxSpace;
        invalidateCache();
        return this;
    }

    protected void applyModeFunctionsChanges(ModeFunctions fn) {
        fn.setDomain(getDomain());
        fn.setMaxSize(getModeFunctionsCount());
        fn.setFirstBoxSpace(getFirstBoxSpace());
        fn.setSecondBoxSpace(getSecondBoxSpace());
        fn.setFrequency(getFrequency());
        fn.setSources(getSources());
        MomStructureHintsManager hintsManager = getHintsManager();
        fn.setHintFnModes(hintsManager.getHintFnModeTypes());
        fn.setHintAxisType(hintsManager.getHintAxisType());
        fn.setHintInvariance(hintsManager.getHintInvariance());
        fn.setHintSymmetry(hintsManager.getHintSymmetry());
        fn.setHintSymmetry(hintsManager.getHintSymmetry());
        fn.setLayers(getLayers());
    }

    public PersistenceCache getPersistentCache() {
        return persistentCache;
    }

    public void wdebug(String title, Throwable e) {
        wdebug(title, e, null);
    }

    public void wdebug(String title, Throwable e, ComplexMatrix m) {
        if (errorHandler != null) {
            errorHandler.showError(title, e, m, this);
        }
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
        boolean found = new MatrixXMatrixStrCacheSupport2(this, null).isCached();
        if (!found) {
            throw new RequiredRebuildException();
        }
    }

    public Object getUserObject(String name, Object defaultValue) {
        if (userObjects.containsKey(name)) {
            return userObjects.get(name);
        }
        return defaultValue;
    }

    public Object getUserObject(String name) {
        return userObjects.get(name);
    }

    public MomStructure removeUserObject(String name) {
        userObjects.remove(name);
        return this;
    }

    public MomStructure setUserObject(String name, Object value) {
        userObjects.put(name, value);
        return this;
    }


    public long getExecutionTime(String type) {
        Chronometer chrono = new Chronometer();
        chrono.start();
        if (CACHE_SRCGP.equals(type)) {
            getTestSourceScalarProducts(ProgressMonitorFactory.none());//insure it is calculated
        } else if (CACHE_FNGP.equals(type)) {
            getTestModeScalarProducts(ProgressMonitorFactory.none());//insure it is calculated
        } else if (CACHE_MATRIX_A.equals(type)) {
            matrixA().computeMatrix();//insure it is calculated
        } else if (CACHE_MATRIX_B.equals(type)) {
            matrixB().computeMatrix();//insure it is calculated
        } else if (CACHE_MATRIX_UNKNOWN.equals(type)) {
            matrixX().computeMatrix();//insure it is calculated
        } else if (CACHE_ZIN.equals(type)) {
            inputImpedance().computeMatrix();//insure it is calculated
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

    public Collection<MomCache> getSimilarCaches(String property) {
        ArrayList<MomCache> found = new ArrayList<MomCache>();
        for (Iterator<ObjectCache> i = getPersistentCache().iterate(); i.hasNext(); ) {
            ObjectCache c = i.next();
            MomCache mc=new MomCache(c);
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
            if (s.dump().equals(c.getDump())) {
                found.add(mc);
            }
        }
        return found;
    }

    

    public Collection<MomCache> getAllCaches() {
        ArrayList<MomCache> found = new ArrayList<MomCache>();
        for (Iterator<ObjectCache> i = getPersistentCache().iterate(); i.hasNext(); ) {
            ObjectCache c = i.next();
            found.add(new MomCache(c));
        }
        return found;
    }

    public StrLayer[] getLayers() {
        return layers;
    }

    public MomStructure setLayers(StrLayer[] couches) {
        if (couches == null) {
            this.layers = StrLayer.NO_LAYERS;
        } else {
            this.layers = new StrLayer[couches.length];
            for (int i = 0; i < couches.length; i++) {
                this.layers[i] = couches[i].clone();
            }
        }
        invalidateCache();
        return this;
    }


    /**
     * A[p,n]=&lt;Gp,Fn&gt; ; p in [0,MAX_ESSAIS-1] ; n in [0,MAX_SOURCES-1]
     */
    //A[p,n]=<Gp,Fn> ; p in [0,MAX_ESSAIS-1] ; n in [0,MAX_SOURCES-1]
    public MatrixBEvaluator createMatrixBEvaluator() {
        if (matrixBEvaluator != null) {
            return matrixBEvaluator;
        }
        switch (getProjectType()) {
            case WAVE_GUIDE: {
                return new MatrixBWaveguideSerialParallelEvaluator();
            }
            case PLANAR_STRUCTURE: {
                return new MatrixBPlanarSerialParallelEvaluator();
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

    public SourceEvaluator createSourceEvaluator() {
        if (sourceEvaluator != null) {
            return sourceEvaluator;
        }
        return new DefaultSourceEvaluator();
    }

    public MatrixAEvaluator createMatrixAEvaluator() {
        if (matrixAEvaluator != null) {
            return matrixAEvaluator;
        }
        switch (getProjectType()) {
            case WAVE_GUIDE: {
                switch (getCircuitType()) {
                    case SERIAL: {
                        return MatrixAWaveguideSerialEvaluator.INSTANCE;
                    }
                    case PARALLEL: {
                        return MatrixAWaveguideParallelEvaluator.INSTANCE;
                    }
                }
            }
            case PLANAR_STRUCTURE: {
                switch (getCircuitType()) {
                    case SERIAL: {
                        return MatrixAPlanarSerialEvaluator.INSTANCE;
                    }
                    case PARALLEL: {
                        return MatrixAPlanarParallelEvaluator.INSTANCE;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

//    public Matrix computeZin2() {
//        Complex[] Testcoeff = computeMatrixUnknown(ProgressMonitor.none).getColumn(0).toArray();
////        GpTestFunctions gpTestFunctions = getGpTestFunctions();
//        ModeInfo[] indexes = getModes();
//        Complex u = getScalarProductOperator().evalVDC(
//                indexes[0].fn,
//                indexes[0].fn
//        );
//        Complex l = Complex.ZERO;
//        ScalarProductCache sp = getTestModeScalarProducts(ProgressMonitor.none);
//        for (int i = 0; i < Testcoeff.length; i++) {
//            Complex complex = Testcoeff[i];
//            l = l.add(sp.gf(i, 0).mul(complex));
//        }
//        getLog().debug("Zin2=" + u + "/" + l);
//        return Maths.matrix(new Complex[][]{{u.div(l)}});
//    }

    public ZinEvaluator createZinEvaluator() {
        if (zinEvaluator != null) {
            return zinEvaluator;
        }
        switch (getCircuitType()) {
            case SERIAL: {
                return ZinSerialEvaluator.INSTANCE;
            }
            case PARALLEL: {
                return ZinParallelEvaluator.INSTANCE;
            }
        }
        return null;//never
    }

    public MatrixUnknownEvaluator createMatrixUnknownEvaluator() {
        if (matrixUnknownEvaluator != null) {
            return matrixUnknownEvaluator;
        }
        return DefaultMatrixUnknownEvaluator.INSTANCE;
    }

    public TMatrix<Complex> createScalarProductCache(ProgressMonitor monitor) {
        build();
        return modeFunctionsDelegate.scalarProduct(Maths.elist(testFunctions.arr()), monitor);
//        ProgressMonitor[] mon = ProgressMonitorFactory.split(monitor, 2);
//        fnModeFunctions.getModes(mon[0], getObjectCache());
//        HintAxisType axis = gpTestFunctions.getStructure().getHintsManager().getHintAxisType();
//        return getScalarProductOperator().eval(true, gpTestFunctions.arr(), fnModeFunctions.arr(), axis.toAxisXY(), mon[1]);
    }

    /**
     * Name is a simple value for qualifying a structure. It DOES NOT has any
     * action on dump. It is not dumpable so that changing names WILL NOT
     * invalidate cache
     *
     * @return name
     */
    public String getName() {
        return name == null ? "NONAME" : name;
    }

    public MomStructure setName(String name) {
        this.name = name;
        return this;
    }

    public MWStructureErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public MomStructure setErrorHandler(MWStructureErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public MomConvergenceManager createMomConvergenceManager() {
        return new MomConvergenceManager(this);
    }

    public ModeInfo mode(int n) {
        return getModeFunctions().getMode(n);
    }

    public ModeInfo getMode(int n) {
        return getModeFunctions().getMode(n);
    }

    public ModeInfo getMode(ModeType mode, int m, int n) {
        return getModeFunctions().getMode(mode, m, n);
    }

    public synchronized ModeInfo[] getModes() {
        return getModes(null);
    }

    public synchronized ModeInfo[] getModes(ProgressMonitor monitor) {
        return getModeFunctions().getModes(monitor);
    }

    public synchronized Complex[] getModesImpedances() {
        ModeInfo[] modes = getModes();
        Complex[] impedances = new Complex[modes.length];
        for (int i = 0; i < modes.length; i++) {
            impedances[i] = modes[i].impedance.impedanceValue();
        }
        return impedances;
    }

    public ElectricFieldEvaluator getElectricFieldEvaluator() {
        return electricFieldEvaluator;
    }

    public MomStructure setElectricFieldEvaluator(ElectricFieldEvaluator electricFieldEvaluator) {
        this.electricFieldEvaluator = electricFieldEvaluator;
        return this;
    }

    public ElectricFieldFundamentalEvaluator getElectricFieldFundamentalEvaluator() {
        return electricFieldFundamentalEvaluator;
    }

    public MomStructure setElectricFieldFundamentalEvaluator(ElectricFieldFundamentalEvaluator electricFieldFundamentalEvaluator) {
        this.electricFieldFundamentalEvaluator = electricFieldFundamentalEvaluator;
        return this;
    }

    public CurrentEvaluator getCurrentEvaluator() {
        return currentEvaluator;
    }

    public MomStructure setCurrentEvaluator(CurrentEvaluator currentEvaluator) {
        this.currentEvaluator = currentEvaluator;
        return this;
    }

    public TestFieldEvaluator getTestFieldEvaluator() {
        return testFieldEvaluator;
    }

    public MomStructure setTestFieldEvaluator(TestFieldEvaluator testFieldEvaluator) {
        this.testFieldEvaluator = testFieldEvaluator;
        return this;
    }

    public PoyntingVectorEvaluator getPoyntingVectorEvaluator() {
        return poyntingVectorEvaluator;
    }

    public MomStructure setPoyntingVectorEvaluator(PoyntingVectorEvaluator poyntingVectorEvaluator) {
        this.poyntingVectorEvaluator = poyntingVectorEvaluator;
        return this;
    }

    public MagneticFieldEvaluator getMagneticFieldEvaluator() {
        return magneticFieldEvaluator;
    }

    public MomStructure setMagneticFieldEvaluator(MagneticFieldEvaluator magneticFieldEvaluator) {
        this.magneticFieldEvaluator = magneticFieldEvaluator;
        return this;
    }

    public ZinEvaluator getZinEvaluator() {
        return zinEvaluator;
    }

    public MomStructure setZinEvaluator(ZinEvaluator zinEvaluator) {
        this.zinEvaluator = zinEvaluator;
        return this;
    }

    public MatrixAEvaluator getMatrixAEvaluator() {
        return matrixAEvaluator;
    }

    public MomStructure setMatrixAEvaluator(MatrixAEvaluator matrixAEvaluator) {
        this.matrixAEvaluator = matrixAEvaluator;
        return this;
    }

    public MatrixBEvaluator getMatrixBEvaluator() {
        return matrixBEvaluator;
    }

    public MomStructure setMatrixBEvaluator(MatrixBEvaluator matrixBEvaluator) {
        this.matrixBEvaluator = matrixBEvaluator;
        return this;
    }

    public MatrixUnknownEvaluator getMatrixUnknownEvaluator() {
        return matrixUnknownEvaluator;
    }

    public MomStructure setMatrixUnknownEvaluator(MatrixUnknownEvaluator matrixUnknownEvaluator) {
        this.matrixUnknownEvaluator = matrixUnknownEvaluator;
        return this;
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

    public PersistenceCache getCacheConfig() {
        return getPersistentCache();
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        invalidateCache();
        pcs.firePropertyChange(propertyName, oldValue, newValue);
    }

}
