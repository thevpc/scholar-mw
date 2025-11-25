package net.thevpc.scholar.hadrumaths;

import net.thevpc.common.mon.LogProgressMonitor;
import net.thevpc.common.strings.StringConverter;
import net.thevpc.common.strings.StringUtils;


import net.thevpc.nuts.elem.*;
import net.thevpc.common.util.*;
import net.thevpc.common.collections.*;
import net.thevpc.common.time.*;
import net.thevpc.scholar.hadrumaths.cache.CacheEnabled;
import net.thevpc.scholar.hadrumaths.cache.CacheMode;
import net.thevpc.scholar.hadrumaths.derivation.FormalDifferentiation;
import net.thevpc.scholar.hadrumaths.derivation.FunctionDifferentiatorManager;
import net.thevpc.scholar.hadrumaths.integration.IntegrationOperator;
import net.thevpc.scholar.hadrumaths.io.FailStrategy;
import net.thevpc.scholar.hadrumaths.io.FolderHFileSystem;
import net.thevpc.scholar.hadrumaths.io.HFileSystem;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.DefaultExprCubeFactory;
import net.thevpc.scholar.hadrumaths.symbolic.ExprCubeFactory;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.util.LogUtils;
import net.thevpc.scholar.hadrumaths.util.ToStringDoubleFormat;
import net.thevpc.scholar.hadrumaths.util.dump.DumpManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MathsConfig {

    public static final MathsConfig INSTANCE = new MathsConfig();
    private final DumpManager dumpManager = new DumpManager();
    private final String defaultRootCachePath = "${user.home}/.cache/hadrumaths";
    private NElements elemsStore;
    private ComplexMatrixFactory defaultLargeComplexMatrixFactory = null;
    private String largeMatrixCachePath = "${cache.folder}/large-matrix";
    private int simplifierCacheSize = 2000;
    //        private  float largeMatrixThreshold = 0.7f;
    private boolean debugExpressionRewrite = false;
    private boolean strictComputationMonitor = false;
    private float maxMemoryThreshold = 0.7f;
    private FrequencyFormat frequencyFormatter = FrequencyFormat.INSTANCE;
    private BytesSizeFormat memorySizeFormatter = BytesSizeFormat.INSTANCE;
    private MetricFormat metricFormatter = new MetricFormat();
    private final TimeDurationFormat timePeriodFormat = new DefaultTimeDurationFormat();
    private final ExprCubeFactory exprCubeFactory = DefaultExprCubeFactory.INSTANCE;
    private int matrixBlockPrecision = 256;
    private InverseStrategy defaultMatrixInverseStrategy = InverseStrategy.BLOCK_SOLVE;
    private SolveStrategy defaultMatrixSolveStrategy = SolveStrategy.DEFAULT;
    private ComplexMatrixFactory complexMatrixFactory = SmartComplexMatrixFactory.INSTANCE;
    private ExprMatrixFactory exprMatrixFactory = MemExprMatrixFactory.INSTANCE;
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
    private final Map<ClassPair, Function> converters = new HashMap<>();
    private final Map<String, MatrixFactory> matrixFactories = new HashMap<>();
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(MathsConfig.class);
    private final DoubleFormat defaultDblFormat = new ToStringDoubleFormat();
    private final DoubleFormat percentFormat = new DecimalDoubleFormat("0.00%");

    {
        registerConverter(Double.class, Complex.class, Maths.DOUBLE_TO_COMPLEX);
        registerConverter(Complex.class, Double.class, Maths.COMPLEX_TO_DOUBLE);
        registerConverter(Double.class, Vector.class, Maths.DOUBLE_TO_TVECTOR);
        registerConverter(Vector.class, Double.class, Maths.TVECTOR_TO_DOUBLE);
        registerConverter(Double.class, Expr.class, Maths.DOUBLE_TO_EXPR);
        registerConverter(Expr.class, Double.class, Maths.EXPR_TO_DOUBLE);

        registerConverter(Complex.class, Vector.class, Maths.COMPLEX_TO_TVECTOR);
        registerConverter(Vector.class, Complex.class, Maths.TVECTOR_TO_COMPLEX);
        registerConverter(Complex.class, Expr.class, Maths.COMPLEX_TO_EXPR);
        registerConverter(Expr.class, Complex.class, Maths.EXPR_TO_COMPLEX);
    }

    private MathsConfig() {
        NElementMapperStore ms = getElements().mapperStore();
        ms.setMapper(DefaultTimeDurationFormat.class, new ClassNameNElementMapper<DefaultTimeDurationFormat>());
        ms.setMapper(PercentDoubleFormat.class, new ClassNameNElementMapper<ToStringDoubleFormat>());
        ms.setMapper(ToStringDoubleFormat.class, new ClassNameNElementMapper<PercentDoubleFormat>());
        ms.setMapper(DecimalDoubleFormat.class, new NElementMapper<DecimalDoubleFormat>() {
            @Override
            public NElement createElement(DecimalDoubleFormat object, Type typeOfSrc, NElementFactoryContext context) {
                return NElement.ofUplet(object.getClass().getSimpleName(), NElement.ofString(object.toPattern()));
            }
        });
        ms.setMapper(FrequencyFormat.class, new NElementMapper<FrequencyFormat>() {
            @Override
            public NElement createElement(FrequencyFormat object, Type typeOfSrc, NElementFactoryContext context) {
                return NElement.ofUplet(object.getClass().getSimpleName(), NElement.ofString(object.toPattern()));
            }
        });
        ms.setMapper(BytesSizeFormat.class, new NElementMapper<BytesSizeFormat>() {
            @Override
            public NElement createElement(BytesSizeFormat object, Type typeOfSrc, NElementFactoryContext context) {
                return NElement.ofUplet(object.getClass().getSimpleName(), NElement.ofString(object.toPattern()));
            }
        });
        ms.setMapper(MetricFormat.class, new NElementMapper<MetricFormat>() {
            @Override
            public NElement createElement(MetricFormat object, Type typeOfSrc, NElementFactoryContext context) {
                return NElement.ofUplet(object.getClass().getSimpleName(), NElement.ofString(object.toPattern()));
            }
        });
    }

    public boolean isCompressCache() {
        return compressCache;
    }

    public MathsConfig setCompressCache(boolean compressCache) {
        this.compressCache = compressCache;
        return this;
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

    public MathsConfig setMaxMemoryThreshold(float maxMemoryThreshold) {
        this.maxMemoryThreshold = maxMemoryThreshold;
        return this;
    }

    public MatrixFactory getTMatrixFactory(String id) {
        MatrixFactory fac = matrixFactories.get(id);
        if (fac == null) {
            if (SmartComplexMatrixFactory.INSTANCE.getId().equals(id)) {
                registerMatrixFactory(SmartComplexMatrixFactory.INSTANCE);
                return SmartComplexMatrixFactory.INSTANCE;
            }
            if (MemComplexMatrixFactory.INSTANCE.getId().equals(id)) {
                registerMatrixFactory(MemComplexMatrixFactory.INSTANCE);
                return MemComplexMatrixFactory.INSTANCE;
            }
            String id1 = DBLargeComplexMatrixFactory.createId(id);
            if (id1 != null) {
                DBLargeComplexMatrixFactory dbLargeMatrixFactory = new DBLargeComplexMatrixFactory(id);
                registerMatrixFactory(dbLargeMatrixFactory);
                return dbLargeMatrixFactory;
            }
            throw new IllegalArgumentException("Factory not Found : " + id);
        } else {
            return fac;
        }
    }

    public MathsConfig registerMatrixFactory(MatrixFactory factory) {
        MatrixFactory fac = matrixFactories.get(factory.getId());
        if (fac == null) {
            matrixFactories.put(factory.getId(), factory);
        } else {
            throw new IllegalArgumentException("Already registered");
        }
        return this;
    }

    public <A, B> void registerConverter(Class<A> a, Class<B> b, Function<A, B> c) {
        ClassPair k = new ClassPair(a, b);
        if (c == null) {
            converters.remove(k);
        } else {
            converters.put(k, c);
        }
    }

    public <A, B> Function<A, B> getConverter(TypeName<A> a, TypeName<B> b) {
        return getConverter(a.getTypeClass(), b.getTypeClass());
    }

    public <A, B> Function<A, B> getConverter(Class<A> a, Class<B> b) {
        if (a.equals(b)) {
            return Maths.IDENTITY;
        }
        Function converter = getRegisteredConverter(a, b);
        if (converter == null) {
            throw new NoSuchElementException("No such primitiveElement3D : converter for " + a + " and " + b);
        }
        return converter;
    }

    public <A, B> Function<A, B> getRegisteredConverter(Class<A> a, Class<B> b) {
        ClassPair k = new ClassPair(a, b);
        return converters.get(k);
    }

    public boolean isDevelopmentMode() {
        return developmentMode;
    }

    public MathsConfig setDevelopmentMode(boolean developmentMode) {
        this.developmentMode = developmentMode;
        return this;
    }

    public FrequencyFormat getFrequencyFormatter() {
        return frequencyFormatter;
    }

    public MathsConfig setFrequencyFormatter(FrequencyFormat frequencyFormatter) {
        this.frequencyFormatter = frequencyFormatter;
        return this;
    }

    public BytesSizeFormat getMemorySizeFormatter() {
        return memorySizeFormatter;
    }

    public MathsConfig setMemorySizeFormatter(BytesSizeFormat memorySizeFormatter) {
        this.memorySizeFormatter = memorySizeFormatter;
        return this;
    }

    public MetricFormat getMetricFormatter() {
        return metricFormatter;
    }

    public MathsConfig setMetricFormatter(MetricFormat metricFormatter) {
        this.metricFormatter = metricFormatter;
        return this;
    }

    public ComplexMatrixFactory getComplexMatrixFactory() {
        return complexMatrixFactory;
    }

    public MathsConfig setComplexMatrixFactory(ComplexMatrixFactory defaultComplexMatrixFactory) {
        this.complexMatrixFactory = defaultComplexMatrixFactory;
        return this;
    }

    public ExprMatrixFactory getExprMatrixFactory() {
        return exprMatrixFactory;
    }

    public MathsConfig setExprMatrixFactory(ExprMatrixFactory factory) {
        this.exprMatrixFactory = factory;
        return this;
    }

    public <T> MatrixFactory<T> getComplexMatrixFactory(TypeName<T> baseType) {
        MatrixFactory<T> r = null;
        if (baseType == Maths.$EXPR) {
            r = (MatrixFactory<T>) exprMatrixFactory;
        } else if (baseType == Maths.$COMPLEX) {
            r = (MatrixFactory<T>) complexMatrixFactory;
        }
        if (r == null) {
            throw new IllegalArgumentException("Not Supported Matrices for " + baseType);
        }
        return r;
    }

    public String getRootCachePath(boolean expand) {
        return expand ? this.expandPath(rootCachePath) : rootCachePath;
    }

    public MathsConfig setRootCachePath(String rootCachePath) {
        this.rootCachePath = rootCachePath;
        return this;
    }

    public String getDefaultCacheFolderName(boolean expand) {
        return expand ? this.expandPath(appCacheName) : appCacheName;
    }

    public MathsConfig setAppCacheName(String appCacheName) {
        this.appCacheName = appCacheName;
        return this;
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
        } else if ((!new File(folder).isAbsolute())
                && !folder.startsWith("./")
                && !folder.startsWith("../")
                && !folder.startsWith(".\\")
                && !folder.startsWith("..\\")
                && !folder.equals(".")
                && !folder.equals("..")) {//folder.indexOf('/') < 0 && folder.indexOf('\\') < 0
            return (baseCacheFolder + "/" + folder);
        } else {
            return (folder);
        }
    }

    public boolean isExpressionWriterCacheEnabled() {
        return cacheEnabled && expressionWriterCacheEnabled;
    }

    public MathsConfig setExpressionWriterCacheEnabled(boolean enabled) {
        boolean old = expressionWriterCacheEnabled;
        expressionWriterCacheEnabled = enabled;
        pcs.firePropertyChange("expressionWriterCacheEnabled", old, enabled);
        return this;
    }

    public CacheMode getPersistenceCacheMode() {
        return cacheEnabled ? persistenceCacheMode : CacheMode.DISABLED;
    }

    public MathsConfig setPersistenceCacheMode(CacheMode persistenceCacheMode) {
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

        return this;
    }

    public ExpressionRewriter getScalarProductSimplifier() {
        return getScalarProductOperator().getSimplifier();
    }

    public ScalarProductOperator getScalarProductOperator() {
        if (defaultScalarProductOperator == null) {
            defaultScalarProductOperator = ScalarProductOperatorFactory.formal();
        }
        return defaultScalarProductOperator;
    }

    public MathsConfig setScalarProductOperator(ScalarProductOperator sp) {
        defaultScalarProductOperator = sp == null ? ScalarProductOperatorFactory.defaultValue() : sp;
        return this;
    }

    public ExpressionRewriter getIntegrationSimplifier() {
        return getIntegrationOperator().getSimplifier();
    }

    public IntegrationOperator getIntegrationOperator() {
        if (defaultIntegrationOperator == null) {
            defaultIntegrationOperator = IntegrationOperatorFactory.defaultValue();
        }
        return defaultIntegrationOperator;
    }

    public MathsConfig setIntegrationOperator(IntegrationOperator op) {
        defaultIntegrationOperator = op == null ? IntegrationOperatorFactory.defaultValue() : op;
        return this;
    }

    public ExpressionRewriter getComputationSimplifier() {
        return ExpressionRewriterFactory.getComputationSimplifier();
    }

    public MathsConfig addConfigChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
        return this;
    }

    public MathsConfig addConfigChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
        return this;
    }

    public MathsConfig removeConfigChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
        return this;
    }

    public MathsConfig removeConfigChangeListener(String property, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(property, listener);
        return this;
    }

    public boolean isCacheEnabled() {
        return cacheEnabled;
    }

    public MathsConfig setCacheEnabled(boolean enabled) {
        boolean old = cacheEnabled;
        cacheEnabled = enabled;
        cacheExpressionPropertiesEnabledEff = cacheExpressionPropertiesEnabled && cacheEnabled;
        pcs.firePropertyChange("cacheEnabled", old, cacheEnabled);
        return this;
    }

    public boolean isCacheExpressionPropertiesEnabled() {
        return cacheExpressionPropertiesEnabledEff;
    }

    public MathsConfig setCacheExpressionPropertiesEnabled(boolean cacheExpressionPropertiesEnabled) {
        boolean old = this.cacheExpressionPropertiesEnabled;
        this.cacheExpressionPropertiesEnabled = cacheExpressionPropertiesEnabled;
        cacheExpressionPropertiesEnabledEff = cacheExpressionPropertiesEnabled && cacheEnabled;
        pcs.firePropertyChange("cacheExpressionPropertiesEnabled", old, this.cacheExpressionPropertiesEnabled);
        return this;
    }

    public MathsConfig setCacheExpressionRewriterSize(ExpressionRewriter ew, int size) {
        if (ew instanceof CacheEnabled) {
            ((CacheEnabled) ew).setCacheSize(size);
        }
        return this;
    }

    public FunctionDifferentiatorManager getFunctionDerivatorManager() {
        if (getFunctionDifferentiatorManager() == null) {
            setFunctionDifferentiatorManager(new FormalDifferentiation());
        }
        return getFunctionDifferentiatorManager();
    }

    public MathsConfig setFunctionDerivatorManager(FunctionDifferentiatorManager manager) {
        setFunctionDifferentiatorManager(manager);
        return this;
    }

    public FunctionDifferentiatorManager getFunctionDifferentiatorManager() {
        return functionDifferentiatorManager;
    }

    public MathsConfig setFunctionDifferentiatorManager(FunctionDifferentiatorManager functionDifferentiatorManager) {
        this.functionDifferentiatorManager = functionDifferentiatorManager;
        return this;
    }

    public String getLargeMatrixCachePath(boolean expand) {
        if (expand) {
            return this.expandPath(largeMatrixCachePath);
        }
        return largeMatrixCachePath;
    }

    public MathsConfig seLargeMatrixCachePath(String largeMatrixPath) {
        largeMatrixCachePath = largeMatrixPath;
        return this;
    }

    public ComplexMatrixFactory getLargeMatrixFactory() {
        if (defaultLargeComplexMatrixFactory == null) {
            synchronized (MathsConfig.class) {
                if (defaultLargeComplexMatrixFactory == null) {
                    LargeComplexMatrixFactory s = (LargeComplexMatrixFactory) getTMatrixFactory(
                            DBLargeComplexMatrixFactory.createLocalId(this.getLargeMatrixCachePath(false), true, null)
                    );
                    s.setResetOnClose(true);
                    defaultLargeComplexMatrixFactory = s;
                }
            }
        }
        return defaultLargeComplexMatrixFactory;
    }

    public MathsConfig setLargeMatrixFactory(ComplexMatrixFactory m) {
        synchronized (MathsConfig.class) {
            if (defaultLargeComplexMatrixFactory != null) {
                defaultLargeComplexMatrixFactory.close();
            }
            defaultLargeComplexMatrixFactory = m;
        }
        return this;
    }

    public MathsConfig setLogMonitorLevel(Level level) {
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
        return this;
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

    public MathsConfig setMatrixBlockPrecision(int matrixBlockPrecision) {
        this.matrixBlockPrecision = matrixBlockPrecision;
        return this;
    }

    public InverseStrategy getDefaultMatrixInverseStrategy() {
        return defaultMatrixInverseStrategy;
    }

    public MathsConfig setDefaultMatrixInverseStrategy(InverseStrategy defaultMatrixInverseStrategy) {
        this.defaultMatrixInverseStrategy = defaultMatrixInverseStrategy;
        return this;
    }

    public SolveStrategy getDefaultMatrixSolveStrategy() {
        return defaultMatrixSolveStrategy;
    }

    public MathsConfig setDefaultMatrixSolveStrategy(SolveStrategy defaultMatrixSolveStrategy) {
        this.defaultMatrixSolveStrategy = defaultMatrixSolveStrategy;
        return this;
    }

    public int getSimplifierCacheSize() {
        return simplifierCacheSize;
    }

    public MathsConfig setSimplifierCacheSize(int simplifierCacheSize) {
        this.simplifierCacheSize = simplifierCacheSize;
        return this;
    }

    public boolean isDebugExpressionRewrite() {
        return debugExpressionRewrite;
    }

    public MathsConfig setDebugExpressionRewrite(boolean debugExpressionRewrite) {
        this.debugExpressionRewrite = debugExpressionRewrite;
        return this;
    }

    public boolean isStrictComputationMonitor() {
        return strictComputationMonitor;
    }

    public MathsConfig setStrictComputationMonitor(boolean strictComputationMonitor) {
        this.strictComputationMonitor = strictComputationMonitor;
        return this;
    }

    public DumpManager getDumpManager() {
        return dumpManager;
    }

    public ExprCubeFactory getExprCubeFactory() {
        return exprCubeFactory;
    }

    public net.thevpc.common.time.TimeDurationFormat getTimePeriodFormat() {

        return timePeriodFormat;
    }

    public DoubleFormat getPercentFormat() {
        return percentFormat;
    }

    public DoubleFormat getDoubleFormat() {
        return defaultDblFormat;
    }

    public MathsConfig close() {
        if (defaultLargeComplexMatrixFactory != null) {
            try {
                defaultLargeComplexMatrixFactory.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }

    public NElements getElements() {
        if(elemsStore==null){
            elemsStore=NElements.of();
        }
        return elemsStore;
    }

    private static class ClassNameNElementMapper<T> implements NElementMapper<T> {
        @Override
        public NElement createElement(T object, Type typeOfSrc, NElementFactoryContext context) {
            return NElement.ofUplet(object.getClass().getSimpleName());
        }
    }
}
