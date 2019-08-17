package net.vpc.scholar.hadrumaths;

import net.vpc.common.strings.StringConverter;
import net.vpc.common.strings.StringUtils;
import net.vpc.common.util.*;
import net.vpc.common.mon.LogProgressMonitor;
import net.vpc.scholar.hadrumaths.cache.CacheEnabled;
import net.vpc.scholar.hadrumaths.cache.CacheMode;
import net.vpc.scholar.hadrumaths.derivation.FormalDifferentiation;
import net.vpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.vpc.scholar.hadrumaths.integration.IntegrationOperator;
import net.vpc.scholar.hadrumaths.interop.jblas.JBlasMatrixFactory;
import net.vpc.scholar.hadrumaths.interop.ojalgo.OjalgoMatrixFactory;
import net.vpc.scholar.hadrumaths.io.FailStrategy;
import net.vpc.scholar.hadrumaths.io.FolderHFileSystem;
import net.vpc.scholar.hadrumaths.io.HFileSystem;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.util.LogUtils;
import net.vpc.scholar.hadrumaths.util.dump.DumpManager;
import net.vpc.scholar.hadruplot.ColorPalette;
import net.vpc.scholar.hadruplot.console.PlotConfigManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MathsConfig {
    public static final MathsConfig INSTANCE = new MathsConfig();

    private MathsConfig() {
    }

    private final DumpManager dumpManager = new DumpManager();
    private final String defaultRootCachePath = "${user.home}/.cache/mathcache";
    private MatrixFactory defaultLargeMatrixFactory = null;
    private String largeMatrixCachePath = "${cache.folder}/large-matrix";
    private int simplifierCacheSize = 2000;
    //        private  float largeMatrixThreshold = 0.7f;
    private boolean debugExpressionRewrite = false;
    private boolean strictComputationMonitor = false;
    private float maxMemoryThreshold = 0.7f;
    private FrequencyFormat frequencyFormatter = FrequencyFormat.INSTANCE;
    private BytesSizeFormat memorySizeFormatter = BytesSizeFormat.INSTANCE;
    private MetricFormat metricFormatter = new MetricFormat();
    private net.vpc.common.util.TimePeriodFormat timePeriodFormat = new DefaultTimePeriodFormat();
    private ExprSequenceFactory exprSequenceFactory = DefaultExprSequenceFactory.INSTANCE;
    private ExprMatrixFactory exprMatrixFactory = DefaultExprMatrixFactory.INSTANCE;
    private ExprCubeFactory exprCubeFactory = DefaultExprCubeFactory.INSTANCE;
    private int matrixBlockPrecision = 256;
    private InverseStrategy defaultMatrixInverseStrategy = InverseStrategy.BLOCK_SOLVE;
    private SolveStrategy defaultMatrixSolveStrategy = SolveStrategy.DEFAULT;
    private MatrixFactory defaultMatrixFactory = SmartMatrixFactory.INSTANCE;
    private CacheMode persistenceCacheMode = CacheMode.ENABLED;
    private boolean cacheEnabled = true;
    private boolean expressionWriterCacheEnabled = true;
    private boolean cacheExpressionPropertiesEnabled = true;
    private boolean cacheExpressionPropertiesEnabledEff = true;
    private boolean developmentMode = false;
    private boolean compressCache = true;
    private String rootCachePath = defaultRootCachePath;
    private String appCacheName = "default";
    //        private  String largeMatrixCachePath = "${cache.folder}/large-matrix";
    //    public  final ScalarProduct NUMERIC_SIMP_SCALAR_PRODUCT = new NumericSimplifierScalarProduct();
    private ScalarProductOperator defaultScalarProductOperator = null;
    private IntegrationOperator defaultIntegrationOperator = null;
    private FunctionDifferentiatorManager functionDifferentiatorManager = new FormalDifferentiation();
    private Map<ClassPair, Converter> converters = new HashMap<>();
    private Map<String, TMatrixFactory> matrixFactories = new HashMap<>();

    public static ColorPalette DEFAULT_PALETTE = PlotConfigManager.DEFAULT_PALETTE;


    private PropertyChangeSupport pcs = new PropertyChangeSupport(MathsConfig.class);
    private DoubleFormat defaultDblFormat = new DoubleFormat() {
        @Override
        public String formatDouble(double value) {
            return String.valueOf(value);
        }
    };
    private DoubleFormat percentFormat = new DecimalDoubleFormat("0.00%");

    {
        registerConverter(Double.class, Complex.class, Maths.DOUBLE_TO_COMPLEX);
        registerConverter(Complex.class, Double.class, Maths.COMPLEX_TO_DOUBLE);
        registerConverter(Double.class, TVector.class, Maths.DOUBLE_TO_TVECTOR);
        registerConverter(TVector.class, Double.class, Maths.TVECTOR_TO_DOUBLE);
        registerConverter(Double.class, Expr.class, Maths.DOUBLE_TO_EXPR);
        registerConverter(Expr.class, Double.class, Maths.EXPR_TO_DOUBLE);

        registerConverter(Complex.class, TVector.class, Maths.COMPLEX_TO_TVECTOR);
        registerConverter(TVector.class, Complex.class, Maths.TVECTOR_TO_COMPLEX);
        registerConverter(Complex.class, Expr.class, Maths.COMPLEX_TO_EXPR);
        registerConverter(Expr.class, Complex.class, Maths.EXPR_TO_COMPLEX);
    }

    public boolean isCompressCache() {
        return compressCache;
    }

    public void setCompressCache(boolean compressCache) {
        this.compressCache = compressCache;
    }

    public boolean memoryCanStores(long bytesToStore) {
        float maxMemoryThreshold = getMaxMemoryThreshold();
        if (maxMemoryThreshold <= 0) {
            return true;
        }
        return (bytesToStore <= (Maths.maxFreeMemory() * ((double) maxMemoryThreshold)));
    }

    public float getMaxMemoryThreshold() {
        return maxMemoryThreshold;
    }

    public void setMaxMemoryThreshold(float maxMemoryThreshold) {
        this.maxMemoryThreshold = maxMemoryThreshold;
    }

    public TMatrixFactory getTMatrixFactory(String id) {
        TMatrixFactory fac = matrixFactories.get(id);
        if (fac == null) {
            if (SmartMatrixFactory.INSTANCE.getId().equals(id)) {
                registerTMatrixFactory(SmartMatrixFactory.INSTANCE);
                return SmartMatrixFactory.INSTANCE;
            }
            if (MemMatrixFactory.INSTANCE.getId().equals(id)) {
                registerTMatrixFactory(MemMatrixFactory.INSTANCE);
                return MemMatrixFactory.INSTANCE;
            }
            if (OjalgoMatrixFactory.INSTANCE.getId().equals(id)) {
                registerTMatrixFactory(OjalgoMatrixFactory.INSTANCE);
                return OjalgoMatrixFactory.INSTANCE;
            }
            if (JBlasMatrixFactory.INSTANCE.getId().equals(id)) {
                registerTMatrixFactory(JBlasMatrixFactory.INSTANCE);
                return JBlasMatrixFactory.INSTANCE;
            }
            String id1 = DBLargeMatrixFactory.createId(id);
            if (id1 != null) {
                DBLargeMatrixFactory dbLargeMatrixFactory = new DBLargeMatrixFactory(id);
                registerTMatrixFactory(dbLargeMatrixFactory);
                return dbLargeMatrixFactory;
            }
            throw new IllegalArgumentException("Factory not Found : " + id);
        } else {
            return fac;
        }
    }

    public void registerTMatrixFactory(TMatrixFactory factory) {
        TMatrixFactory fac = matrixFactories.get(factory.getId());
        if (fac == null) {
            matrixFactories.put(factory.getId(), factory);
        } else {
            throw new IllegalArgumentException("Already registered");
        }
    }

    public <A, B> void registerConverter(Class<A> a, Class<B> b, Converter<A, B> c) {
        ClassPair k = new ClassPair(a, b);
        if (c == null) {
            converters.remove(k);
        } else {
            converters.put(k, c);
        }
    }

    public <A, B> Converter<A, B> getRegisteredConverter(Class<A> a, Class<B> b) {
        ClassPair k = new ClassPair(a, b);
        return converters.get(k);
    }

    public <A, B> Converter<A, B> getConverter(Class<A> a, Class<B> b) {
        if (a.equals(b)) {
            return Maths.IDENTITY;
        }
        Converter converter = getRegisteredConverter(a, b);
        if (converter == null) {
            throw new NoSuchElementException("No such element : converter for " + a + " and " + b);
        }
        return converter;
    }

    public <A, B> Converter<A, B> getConverter(TypeName<A> a, TypeName<B> b) {
        return getConverter(a.getTypeClass(), b.getTypeClass());
    }

    public boolean isDevelopmentMode() {
        return developmentMode;
    }

    public void setDevelopmentMode(boolean developmentMode) {
        this.developmentMode = developmentMode;
    }

    public FrequencyFormat getFrequencyFormatter() {
        return frequencyFormatter;
    }

    public void setFrequencyFormatter(FrequencyFormat frequencyFormatter) {
        this.frequencyFormatter = frequencyFormatter;
    }

    public BytesSizeFormat getMemorySizeFormatter() {
        return memorySizeFormatter;
    }

    public void setMemorySizeFormatter(BytesSizeFormat memorySizeFormatter) {
        this.memorySizeFormatter = memorySizeFormatter;
    }

    public MetricFormat getMetricFormatter() {
        return metricFormatter;
    }

    public void setMetricFormatter(MetricFormat metricFormatter) {
        this.metricFormatter = metricFormatter;
    }

    public MatrixFactory getDefaultMatrixFactory() {
        return defaultMatrixFactory;
    }

    public void setDefaultMatrixFactory(MatrixFactory defaultMatrixFactory) {
        this.defaultMatrixFactory = defaultMatrixFactory;
    }

    public <T> TMatrixFactory<T> getDefaultMatrixFactory(TypeName<T> baseType) {
        throw new IllegalArgumentException("Not Yet Supported");
    }

    public String getRootCachePath(boolean expand) {
        return expand ? this.expandPath(rootCachePath) : rootCachePath;
    }

    public void setRootCachePath(String rootCachePath) {
        this.rootCachePath = rootCachePath;
    }

    public String getDefaultCacheFolderName(boolean expand) {
        return expand ? this.expandPath(appCacheName) : appCacheName;
    }

    public void setAppCacheName(String appCacheName) {
        this.appCacheName = appCacheName;
    }


    public boolean deleteAllCache() {
        return getCacheFileSystem().get("/").deleteFolderTree(null, FailStrategy.FAIL_SAFE);
    }

    public HFileSystem getCacheFileSystem() {
        return new FolderHFileSystem(new File(getCacheFolder()));
    }

    public String getCacheFolder() {
        return getCacheFolder(null);
    }

    public String getCacheFolder(String folder) {
        String baseCacheFolder = getRootCachePath(true);
        if (folder == null) {
            return baseCacheFolder + "/" + getDefaultCacheFolderName(true);
        } else if (
                (!new File(folder).isAbsolute())
                        && !folder.startsWith("./")
                        && !folder.startsWith("../")
                        && !folder.startsWith(".\\")
                        && !folder.startsWith("..\\")
                        && !folder.equals(".")
                        && !folder.equals("..")
                ) {//folder.indexOf('/') < 0 && folder.indexOf('\\') < 0
            return (baseCacheFolder + "/" + folder);
        } else {
            return (folder);
        }
    }

    public boolean isExpressionWriterCacheEnabled() {
        return cacheEnabled && expressionWriterCacheEnabled;
    }

    public void setExpressionWriterCacheEnabled(boolean enabled) {
        boolean old = expressionWriterCacheEnabled;
        expressionWriterCacheEnabled = enabled;
        pcs.firePropertyChange("expressionWriterCacheEnabled", old, enabled);
    }

    public CacheMode getPersistenceCacheMode() {
        return cacheEnabled ? persistenceCacheMode : CacheMode.DISABLED;
    }

    public void setPersistenceCacheMode(CacheMode persistenceCacheMode) {
        if (persistenceCacheMode == null) {
            persistenceCacheMode = CacheMode.DISABLED;
        }
        switch (persistenceCacheMode) {
            case INHERITED: {
                persistenceCacheMode = CacheMode.ENABLED;
                break;
            }
        }
        this.persistenceCacheMode = persistenceCacheMode;

    }

    public ExpressionRewriter getScalarProductSimplifier() {
        return getScalarProductOperator().getExpressionRewriter();
    }

    public ExpressionRewriter getIntegrationSimplifier() {
        return getIntegrationOperator().getExpressionRewriter();
    }

    public ExpressionRewriter getComputationSimplifier() {
        return ExpressionRewriterFactory.getComputationSimplifier();
    }


    public void addConfigChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addConfigChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void removeConfigChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public void removeConfigChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public void setCacheEnabled(boolean enabled) {
        boolean old = cacheEnabled;
        cacheEnabled = enabled;
        cacheExpressionPropertiesEnabledEff = cacheExpressionPropertiesEnabled && cacheEnabled;
        pcs.firePropertyChange("cacheEnabled", old, cacheEnabled);
    }

    public boolean isCacheExpressionPropertiesEnabled() {
        return cacheExpressionPropertiesEnabledEff;
    }

    public void setCacheExpressionPropertiesEnabled(boolean cacheExpressionPropertiesEnabled) {
        boolean old = this.cacheExpressionPropertiesEnabled;
        this.cacheExpressionPropertiesEnabled = cacheExpressionPropertiesEnabled;
        cacheExpressionPropertiesEnabledEff = cacheExpressionPropertiesEnabled && cacheEnabled;
        pcs.firePropertyChange("cacheEnabled", old, this.cacheExpressionPropertiesEnabled);
    }

    public void setCacheExpressionRewriterSize(ExpressionRewriter ew, int size) {
        if (ew instanceof CacheEnabled) {
            ((CacheEnabled) ew).setCacheSize(size);
        }
    }

    public FunctionDifferentiatorManager getFunctionDerivatorManager() {
        if (getFunctionDifferentiatorManager() == null) {
            setFunctionDifferentiatorManager(new FormalDifferentiation());
        }
        return getFunctionDifferentiatorManager();
    }

    public void setFunctionDerivatorManager(FunctionDifferentiatorManager manager) {
        setFunctionDifferentiatorManager(manager);
    }

    public ScalarProductOperator getScalarProductOperator() {
        if (defaultScalarProductOperator == null) {
            defaultScalarProductOperator = ScalarProductOperatorFactory.formal();
        }
        return defaultScalarProductOperator;
    }

    public void setScalarProductOperator(ScalarProductOperator sp) {
        defaultScalarProductOperator = sp == null ? ScalarProductOperatorFactory.defaultValue() : sp;
    }

    public IntegrationOperator getIntegrationOperator() {
        if (defaultIntegrationOperator == null) {
            defaultIntegrationOperator = IntegrationOperatorFactory.defaultValue();
        }
        return defaultIntegrationOperator;
    }

    public void setIntegrationOperator(IntegrationOperator op) {
        defaultIntegrationOperator = op == null ? IntegrationOperatorFactory.defaultValue() : op;
    }


    public String getLargeMatrixCachePath(boolean expand) {
        if (expand) {
            return this.expandPath(largeMatrixCachePath);
        }
        return largeMatrixCachePath;
    }

    public void seLargeMatrixCachePath(String largeMatrixPath) {
        largeMatrixCachePath = largeMatrixPath;
    }

    public MatrixFactory getMatrixFactory() {
        return defaultMatrixFactory;
    }

    public MatrixFactory getLargeMatrixFactory() {
        if (defaultLargeMatrixFactory == null) {
            synchronized (Maths.class) {
                if (defaultLargeMatrixFactory == null) {
                    LargeMatrixFactory s = (LargeMatrixFactory) getTMatrixFactory(
                            DBLargeMatrixFactory.createLocalId(this.getLargeMatrixCachePath(false), true, null)
                    );
                    s.setResetOnClose(true);
                    defaultLargeMatrixFactory = s;
                }
            }
        }
        return defaultLargeMatrixFactory;
    }

    public void setLargeMatrixFactory(MatrixFactory m) {
        synchronized (Maths.class) {
            if (defaultLargeMatrixFactory != null) {
                defaultLargeMatrixFactory.close();
            }
            defaultLargeMatrixFactory = m;
        }
    }

    public void setLogMonitorLevel(Level level) {
        Logger logger = LogProgressMonitor.getDefaultLogger();
        logger.setLevel(level);
        Handler handler = null;
        for (Handler h : logger.getHandlers()) {
            handler = h;
            h.setLevel(level);
        }
        if (handler == null) {
            handler = new ConsoleHandler();
            handler.setLevel(level);
            handler.setFormatter(LogUtils.LOG_FORMATTER_2);
            logger.addHandler(handler);
        }
    }

    public String expandPath(String format) {
        if (format == null) {
            return format;
        }
        String s = replaceVars(format);
        if (format.equals("~")) {
            return System.getProperty("user.home");
        }
        if (format.startsWith("~") && format.length() > 1 && (format.charAt(1) == '/' || format.charAt(1) == '\\')) {
            return System.getProperty("user.home") + format.substring(1);
        }
        if (format.equals("~~")) {
            return replaceVars(defaultRootCachePath);
        }
        if (format.startsWith("~~") && format.length() > 2 && (format.charAt(2) == '/' || format.charAt(2) == '\\')) {
            return replaceVars(defaultRootCachePath) + format.substring(2);
        }
        s = replaceVars(s);
        return s;
    }

    public String replaceVars(String format) {
        return StringUtils.replaceDollarPlaceHolders(format, new StringConverter() {
            @Override
            public String convert(String key) {
                String val = System.getProperty(key);
                if (val == null) {
                    switch (key) {
                        case "cache.root": {
                            val = getRootCachePath(true);
                            break;
                        }
                        case "cache.folder": {
                            val = getCacheFolder();
                            break;
                        }
                        case "cache.large-matrix": {
                            val = getLargeMatrixCachePath(true);
                            break;
                        }
                        default: {
                            val = "${" + key + "}";
                        }
                    }
                }
                return val;
            }
        });
    }

    public int getMatrixBlockPrecision() {
        return matrixBlockPrecision;
    }

    public void setMatrixBlockPrecision(int matrixBlockPrecision) {
        this.matrixBlockPrecision = matrixBlockPrecision;
    }

    public InverseStrategy getDefaultMatrixInverseStrategy() {
        return defaultMatrixInverseStrategy;
    }

    public void setDefaultMatrixInverseStrategy(InverseStrategy defaultMatrixInverseStrategy) {
        this.defaultMatrixInverseStrategy = defaultMatrixInverseStrategy;
    }

    public SolveStrategy getDefaultMatrixSolveStrategy() {
        return defaultMatrixSolveStrategy;
    }

    public void setDefaultMatrixSolveStrategy(SolveStrategy defaultMatrixSolveStrategy) {
        this.defaultMatrixSolveStrategy = defaultMatrixSolveStrategy;
    }

    public FunctionDifferentiatorManager getFunctionDifferentiatorManager() {
        return functionDifferentiatorManager;
    }

    public void setFunctionDifferentiatorManager(FunctionDifferentiatorManager functionDifferentiatorManager) {
        this.functionDifferentiatorManager = functionDifferentiatorManager;
    }

    public int getSimplifierCacheSize() {
        return simplifierCacheSize;
    }

    public void setSimplifierCacheSize(int simplifierCacheSize) {
        this.simplifierCacheSize = simplifierCacheSize;
    }

    public boolean isDebugExpressionRewrite() {
        return debugExpressionRewrite;
    }

    public void setDebugExpressionRewrite(boolean debugExpressionRewrite) {
        this.debugExpressionRewrite = debugExpressionRewrite;
    }

    public boolean isStrictComputationMonitor() {
        return strictComputationMonitor;
    }

    public void setStrictComputationMonitor(boolean strictComputationMonitor) {
        this.strictComputationMonitor = strictComputationMonitor;
    }

    public DumpManager getDumpManager() {
        return dumpManager;
    }

    public ExprSequenceFactory getExprSequenceFactory() {
        return exprSequenceFactory;
    }

    public ExprMatrixFactory getExprMatrixFactory() {
        return exprMatrixFactory;
    }

    public ExprCubeFactory getExprCubeFactory() {
        return exprCubeFactory;
    }

    public net.vpc.common.util.TimePeriodFormat getTimePeriodFormat() {

        return timePeriodFormat;
    }

    public DoubleFormat getPercentFormat() {
        return percentFormat;
    }

    public DoubleFormat getDoubleFormat() {
        return defaultDblFormat;
    }

    public void close() {
        if (defaultLargeMatrixFactory != null) {
            try {
                defaultLargeMatrixFactory.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

