package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.common.mon.MonitoredRunnable;
import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.common.time.Chronometer;
import net.thevpc.common.time.DatePart;
import net.thevpc.common.time.TimeDuration;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.concurrent.AppLockException;
import net.thevpc.scholar.hadrumaths.io.FailStrategy;
import net.thevpc.scholar.hadrumaths.io.HFile;
import net.thevpc.scholar.hadrumaths.io.HFileFilter;
import net.thevpc.scholar.hadrumaths.io.HadrumathsIOUtils;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 13:28:08
 */
public class PersistenceCacheImpl implements PersistenceCache {
    private static final Logger log = Logger.getLogger(PersistenceCacheImpl.class.getName());

    //    public static void main(String[] args) {
//        final PersistenceCache c = new PersistenceCache();
//        for (int i = 0; i < 3; i++) {
//            new Thread() {
//                @Override
//                public void run() {
//                    c.evaluate("toto", new Evaluator() {
//                        @Override
//                        public Object evaluate(Object[] args) {
//                            System.out.println("START ....");
//                            try {
//                                Thread.sleep(20000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("END ....");
//                            return "Hello";
//                        }
//                    });
//                }
//            }.start();
//        }
//    }
    private final PersistenceCache parent;
    private final PathFS pathFS = new PathFS();
    private HFile rootFolder;
    private HFile rootFolderCached;
    private HFile repositoryFolder;
    private String repositoryName;
    private final String dumpFileName;
    private CacheMode mode = CacheMode.INHERITED;
    private final HFileFilter cacheFileFilter;
    private boolean logLoadStatsEnabled = false;
    /**
     * tasks that take more than taskTimeThreshold (in ms)
     * will be logged to console
     */
    private final long minimumTimeForCache = 0;
    private long taskTimeThreshold = 3000;
    private boolean ignorePrevious = false;
    private ProgressMonitorFactory monitorFactory = null;

//    public PersistenceCache() {
//        this((File) null);
//    }
//
//    public PersistenceCache(File cache) {
//        this("dump", cache, null);
//    }
//
//    public PersistenceCache(String id, String cache) {
//        this(id, validateFolder(cache), null);
//    }
//
//    public PersistenceCache(String id) {
//        this(id, (File) null, null);
//    }

    public PersistenceCacheImpl() {
        this(null, null, null,null);
    }

    public PersistenceCacheImpl(HFile rootFolder, String repositoryName, PersistenceCache parent,ProgressMonitorFactory progressMonitorFactory) {
        this.parent = parent;
        this.rootFolder = rootFolder;
        this.repositoryName = repositoryName;
        this.monitorFactory = progressMonitorFactory;
        this.dumpFileName = "dump" + ObjectCache.CACHE_DEF_SUFFIX;
        cacheFileFilter = pathname -> pathname.isDirectory()
                ||
                (pathname.isFile() &&
                        (pathname.getName().equals(PersistenceCacheImpl.this.dumpFileName)
                                || pathname.getName().endsWith(ObjectCache.CACHE_LOG_SUFFIX)
                                || pathname.getName().endsWith(ObjectCache.CACHE_OBJECT_SUFFIX)));
    }

    public PersistenceCacheImpl(String repositoryName) {
        this(null, repositoryName, null,null);
    }

    @Override
    public void setCacheBaseFolder(HFile rootFolder) {
        setRootFolder(rootFolder);
    }    @Override
    public HFile getRepositoryFolder() {
        if (repositoryFolder == null) {
            String n = getRepositoryName();
            if (n == null || n.length() == 0) {
                n = "default";
            }
            repositoryFolder = new HFile(getRootFolder(), n);
        }
        return repositoryFolder;
    }

    public class CacheProcessorImpl implements CacheProcessor {
        private ProgressMonitor monitor;
        private final String cacheItemName;
        private final Object[] args;
        private final ObjectCache objCache;

        public CacheProcessorImpl(String cacheItemName, Object... args) {
            this.cacheItemName = cacheItemName;
            this.args = args;
            objCache = getObjectCache(CacheKey.fct(cacheItemName, args), true);
        }

        @Override
        public ProgressMonitor getMonitor() {
            return monitor;
        }

        @Override
        public CacheProcessor monitor(ProgressMonitor monitor) {
            this.monitor = monitor;
            return this;
        }

        @Override
        public <T> T evaluate(CacheEvaluator evaluator) {
            return PersistenceCacheImpl.this.evaluate(objCache, cacheItemName, monitor, evaluator, args);
        }
    }    @Override
    public PersistenceCache setRepositoryFolder(HFile repositoryFolder) {
        this.repositoryFolder = repositoryFolder;
        return this;
    }

    public class CacheEvalBuilderImpl implements CacheEvalBuilder {
        private Function<Object[], Object> exec;
        private MonitoredRunnable init;
        private final List<Object> args = new ArrayList<>();
        private final String name;
        private ProgressMonitor monitor;
        private ObjectCache objCache;

        public CacheEvalBuilderImpl(String name) {
            this.name = name;
        }

        @Override
        public CacheEvalBuilder on(ObjectCache objCache) {
            this.objCache = objCache;
            return this;
        }

        @Override
        public CacheEvalBuilder init(MonitoredRunnable init) {
            this.init = init;
            return this;
        }

        @Override
        public CacheEvalBuilder monitor(ProgressMonitor monitor) {
            this.monitor = monitor;
            return this;
        }

        @Override
        public CacheEvalBuilder eval2(Function<Object[], Object> exec) {
            this.exec = exec;
            return this;
        }

        @Override
        public CacheEvalBuilder eval(Supplier<Object> exec) {
            this.exec = new Function<Object[], Object>() {
                @Override
                public Object apply(Object[] objects) {
                    return exec.get();
                }
            };
            return this;
        }

        @Override
        public CacheEvalBuilder args(Object... args) {
            this.args.addAll(Arrays.asList(args));
            return this;
        }

        @Override
        public <T> T get() {
            CacheEvaluator evaluator = new CacheEvaluator() {
                @Override
                public void init(ProgressMonitor cacheMonitor) {
                    if (init != null) {
                        init.run(cacheMonitor);
                    }
                }

                @Override
                public Object evaluate(Object[] args, ProgressMonitor cacheMonitor) {
                    return exec.apply(args);
                }

                @Override
                public String toString() {
                    return "Eval(" + name + ")";
                }
            };
            Object[] argsArray = this.args.toArray(new Object[0]);
            if (objCache != null) {
                return evaluate(objCache, name, monitor, evaluator, argsArray);
            }
            return evaluate(name, monitor, evaluator, argsArray);
        }
    }    @Override
    public HFile getRootFolder() {
        if (rootFolderCached == null) {
            if (rootFolder == null) {
                rootFolderCached = Maths.Config.getCacheFileSystem().get("/");
            } else {
                rootFolderCached = rootFolder;
            }
        }
        return rootFolderCached;
    }

    private class ObjectCacheIterator implements Iterator<ObjectCache> {

        ObjectCache current;
        Stack<HFile> currentFiles = new Stack<HFile>();

        public ObjectCacheIterator() {
            Stack<HFile> stack = new Stack<HFile>();
            stack.push(rootFolder);
            while (!stack.isEmpty()) {
                HFile ff = stack.pop();
                HFile file1 = new HFile(ff, dumpFileName);
                if (file1.existsOrWait()) {
                    currentFiles.push(ff);
                }
                HFile[] files = ff.listFiles();
                if (files != null) {
                    for (HFile file : files) {
                        if (file.isDirectory()) {
                            stack.push(file);
                        }
                    }
                }
            }
        }

        public boolean hasNext() {
            if (currentFiles.isEmpty()) {
                return false;
            }
            while (!currentFiles.isEmpty()) {
                HFile folder = currentFiles.pop();
                current = null;
                try {
                    current = loadCache(folder);
                } catch (Throwable e) {
                    //
                }
                if (current != null) {
                    return true;
                }
            }
            return false;
        }

        public ObjectCache next() {
            return current;
        }

        public void remove() {
            //
        }
    }    @Override
    public PersistenceCache setRootFolder(String rootFolder) {
        return setRootFolder(rootFolder == null ? null : HadrumathsIOUtils.createHFile((rootFolder)));
    }

    //    private MomCache loadDump() throws IOException {
//        return loadDump(getCacheFolder());
//    }
    private ObjectCache loadCache(HFile folder) throws IOException {
        HFile dumpFile = new HFile(folder, dumpFileName);
        if (!dumpFile.existsOrWait() || !dumpFile.isFile()) {
            return null;
        }
        return new DefaultObjectCache(CacheKey.load(new String(dumpFile.loadBytes())), this);
    }

//    public HFile getFolder(String dump) {
//        return getDumpFolder(dump, false);
//    }

//    public ObjectCache getObjectCache(Dumpable d, boolean createIfNotFound) {
//        if (!isEnabled()) {
//            return null;
//        }
//        return getObjectCache(d.dump(), createIfNotFound);
//    }

//    public String getDumpHashCode(String dump) {
//        String hh = Integer.toString(dump.hashCode(), 36).toLowerCase();
//        if (hh.startsWith("-")) {
//            hh = hh.substring(1);
//        }
//        StringBuilder sb = new StringBuilder("");
//        int j = 0;
//        for (int i = 0; i < hh.length(); i++) {
//            if (j == 2) {
//                sb.append("/");
//                j = 0;
//            }
//            sb.append(hh.charAt(i));
//            j++;
//        }
//        return sb.toString();
//    }

    @Override
    public ObjectCache getObjectCache(CacheKey dump, boolean createIfNotFound) {
        if (!isEnabled()) {
            //isEnabled();
            return null;
        }
//        DumpPath dp=new DumpPath(dump);
//        File f = getDumpFolder(dump, createIfNotFound);
//        return f == null ? null : new MomCache(new DumpPath(dump), this);
        return new DefaultObjectCache(dump, this);
    }

    @Override
    public DumpCacheFile getFile(CacheKey dump, String path) {
        return new DumpCacheFile(
                dump, path, new HFile(getDumpFolder(dump, true), path), this
        );
    }

    @Override
    public HFile getDumpFolder(CacheKey dumpObj, boolean createIfNotFound) {
        HFile r = new HFile(getRepositoryFolder(), dumpObj.getPath());
        ObjectCache d = null;
        try {
            d = loadCache(r);
        } catch (IOException e) {
            //
        }
        boolean enabled = isEnabled();
        if (d == null) {
            if (createIfNotFound && enabled) {
                r.mkdirs();
                PrintStream fos = null;
                try {
                    HFile file = new HFile(r, dumpFileName);
                    fos = new PrintStream(file.getOutputStream());
                    fos.print(dumpObj.getDump());
//                    System.out.println(file + " : stored dump");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                } finally {
                    fos.close();
                }
                return r;
            }
        } else if (dumpObj.equals(d.getKey())) {
            return r;
        }

        HFile[] f = r.listFiles();
        int min = 0;
        if (f != null) {
            for (HFile subFolder : f) {
                if (subFolder.isDirectory()) {
                    d = null;
                    try {
                        d = loadCache(subFolder);
                    } catch (IOException e) {
                        //
                    }
                    if (d != null) {
                        if (dumpObj.equals(d.getKey())) {
                            return subFolder;
                        }
                        int x = 0;
                        try {
                            x = Integer.parseInt(subFolder.getName());
                        } catch (NumberFormatException e) {
                            //
                        }
                        if (x > min) {
                            min = x;
                        }
                    }
                }
            }
        }
        if (createIfNotFound && enabled) {
            HFile n = new HFile(r, String.valueOf(min + 1));
            n.mkdirs();
            PrintStream fos = null;
            try {
                HFile file = new HFile(n, dumpFileName);
                fos = new PrintStream(file.getOutputStream());
                fos.print(dumpObj.getDump());
//                System.out.println(file + " : stored dump");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                fos.close();
            }
            return n;
        }
        return null;
    }

    @Override
    public Iterator<ObjectCache> iterator() {
        return new ObjectCacheIterator();
    }

    @Override
    public CacheMode getEffectiveMode() {
        CacheMode m = mode == null ? CacheMode.INHERITED : mode;
        if (m != CacheMode.INHERITED) {
            return m;
        }
        CacheMode p = parent == null ? (Maths.Config.getPersistenceCacheMode()) : parent.getEffectiveMode();
        return p;
    }
//public CacheMode getEffectiveMode() {
//        CacheMode m = mode == null ? CacheMode.INHERITED : mode;
//        CacheMode p = parent == null ? (Maths.Config.getPersistenceCacheMode()) : parent.getEffectiveMode();
//        if (m == CacheMode.DISABLED || p == CacheMode.DISABLED) {
//            return CacheMode.DISABLED;
//        }
//        if (m == CacheMode.INHERITED) {
//            return p;
//        }
//        switch (p) {
//            case READ_ONLY: {
//                switch (m) {
//                    case ENABLED: {
//                        return CacheMode.READ_ONLY;
//                    }
//                    case READ_ONLY: {
//                        return CacheMode.READ_ONLY;
//                    }
//                    case WRITE_ONLY: {
//                        return CacheMode.DISABLED;
//                    }
//                }
//                return CacheMode.DISABLED;
//            }
//            case WRITE_ONLY: {
//                switch (m) {
//                    case ENABLED: {
//                        return CacheMode.WRITE_ONLY;
//                    }
//                    case READ_ONLY: {
//                        return CacheMode.DISABLED;
//                    }
//                    case WRITE_ONLY: {
//                        return CacheMode.WRITE_ONLY;
//                    }
//                }
//                return CacheMode.DISABLED;
//            }
//            case ENABLED: {
//                return m;
//            }
//        }
//        return CacheMode.DISABLED;
//    }

    @Override
    public boolean isEnabled() {
        return getEffectiveMode() != CacheMode.DISABLED;
    }

    @Override
    public PersistenceCache setEnabled(boolean enabled) {
        this.mode = enabled ? CacheMode.ENABLED : CacheMode.DISABLED;
        return this;
    }

    @Override
    public CacheMode getMode() {
        return mode;
    }

    @Override
    public PersistenceCache setMode(CacheMode mode) {
        this.mode = mode == null ? CacheMode.INHERITED : mode;
        return this;
    }

    @Override
    public boolean clear() {
        System.err.println("Warning, clearing " + getRepositoryFolder().getPath() + " started.");
        boolean b = getRepositoryFolder().deleteFolderTree(cacheFileFilter, FailStrategy.FAIL_SAFE);
        if (!b) {
            System.err.println("Warning, clearing " + getRepositoryFolder().getPath() + " failed.");
        }
        return b;
    }



    @Override
    public PersistenceCache setRootFolder(HFile rootFolder) {
        this.rootFolder = rootFolder;
        this.rootFolderCached = null;
        return this;
    }

    @Override
    public boolean isLogLoadStatsEnabled() {
        return logLoadStatsEnabled;
    }

    @Override
    public PersistenceCache setLogLoadStatsEnabled(boolean logLoadStatsEnabled) {
        this.logLoadStatsEnabled = logLoadStatsEnabled;
        return this;
    }

    @Override
    public long getTaskTimeThreshold() {
        return taskTimeThreshold;
    }

    @Override
    public PersistenceCache setTaskTimeThreshold(long taskTimeThreshold) {
        this.taskTimeThreshold = taskTimeThreshold;
        return this;
    }

    @Override
    public boolean isIgnorePrevious() {
        return ignorePrevious;
    }

    @Override
    public PersistenceCache setIgnorePrevious(boolean ignorePrevious) {
        this.ignorePrevious = ignorePrevious;
        return this;
    }

    @Override
    public PathFS getFS() {
        return pathFS;
    }

    @Override
    public PersistenceCache setAll(PersistenceCache other) {
        if (other != null) {
            if (other instanceof PersistenceCacheImpl) {
                PersistenceCacheImpl o = (PersistenceCacheImpl) other;
                mode = o.mode;
                rootFolder = o.rootFolder;
                logLoadStatsEnabled = o.logLoadStatsEnabled;
                ignorePrevious = o.ignorePrevious;
            }
        }
        return this;
    }

//    public <T> T evaluate(String cacheItemName, T oldValue, final Evaluator evaluator, final Object args) {
//        return evaluate(cacheItemName, oldValue, evaluator, toObjArr(args));
//    }

//    public <T> T evaluate(final String cacheItemName, ProgressMonitor monitor, final Evaluator evaluator, final Object... args) {
//        return evaluate(cacheItemName, monitor, new Evaluator2() {
//            @Override
//            public void init() {
////                for (Method m : evaluator.getClass().getDeclaredMethods()) {
////                    Init initMethod = m.getAnnotation(Init.class);
////                    if (initMethod != null) {
////                        if (m.getParameterTypes().length != 0) {
////                            System.err.println("Ignored method " + m + ". Too many arguments");
////                        } else {
////                            m.setAccessible(true);
////                            try {
////                                m.invoke(evaluator);
////                            } catch (IllegalAccessException e) {
////                                throw new IllegalArgumentException(e);
////                            } catch (InvocationTargetException e) {
////                                throw new IllegalArgumentException(e);
////                            }
////                        }
////                    }
////                }
//            }
//
//            @Override
//            public Object evaluate(Object[] args) {
//                return evaluator.evaluate(args);
//            }
//        }, args);
//    }



    @Override
    public CacheProcessor get(String cacheItemName, Object... args) {
        return new CacheProcessorImpl(cacheItemName, args);
    }

    /**
     * retrieve cache get named <code>cacheItemName</code> from cache if already evaluated, or reevaluate it and store to cache
     *
     * @param cacheItemName cache get name to evaluated
     * @param evaluator     evaluator invoked when value could not be retrieved
     * @param args          ALL arguments needed for evaluation. This argument should include all global and inherited values on which depends evaluation.
     *                      If this arguments are wrong, cache may return a value evaluated for other parameters.
     *                      All arguments should be Dumpable (@see {@link Maths#dump(Object)})
     * @param <T>           cache get type/class
     * @return oldValue if not null, or loaded cached if already evaluated or reevaluate it at call time
     */
    @Override
    public <T> T evaluate(final String cacheItemName, ProgressMonitor monitor, final CacheEvaluator evaluator, final Object... args) {

        final ObjectCache objCache = getObjectCache(CacheKey.fct(cacheItemName, args), true);
        return evaluate(objCache, cacheItemName, monitor, evaluator, args);
    }

    @Override
    public CacheEvalBuilder of(String cacheItemName, Object... args) {
        return new CacheEvalBuilderImpl(cacheItemName).args(args);
    }



    /**
     * retrieve cache get named <code>cacheItemName</code> from cache if already evaluated, or reevaluate it and store to cache
     *
     * @param cacheItemName cache get name to evaluated
     * @param evaluator     evaluator invoked when value could not be retrieved
     * @param args          ALL arguments needed for evaluation. This argument should include all global and inherited values on which depends evaluation.
     *                      If this arguments are wrong, cache may return a value evaluated for other parameters.
     *                      All arguments should be Dumpable (@see {@link Maths#dump(Object)})
     * @param <T>           cache get type/class
     * @return oldValue if not null, or loaded cached if already evaluated or reevaluate it at call time
     */
    @Override
    public <T> T evaluate(final ObjectCache objCache, final String cacheItemName, ProgressMonitor monitor, final CacheEvaluator evaluator, final Object... args) {
        monitor= ProgressMonitors.nonnull(monitor);
        if (objCache == null) {
            ProgressMonitor[] mons = monitor.split(.1, .9);
            ProgressMonitor initMon=mons[0];
            ProgressMonitor runMon=mons[1];
            evaluator.init(initMon);
            return (T) evaluator.evaluate(args, runMon);
        }
        return objCache.invokeLocked(cacheItemName,
                PersistenceCache.LOCK_TIMEOUT,
                new PersistenceLockedCache<>(this, objCache, cacheItemName, evaluator, monitor, args),
                monitor);

    }

//    private Object[] toObjArr(Object args) {
//        return (args instanceof Object[]) ? ((Object[]) args) : new Object[]{args};
//    }

//    public <T> T evaluate(String key, final Evaluator evaluator, final Object args) {
//        return evaluate(key, evaluator, toObjArr(args));
//    }

//    public <T> T evaluate(String key, final Evaluator evaluator, final Object... args) {
//        return evaluate(key, null, evaluator, args);
//    }

    @Override
    public String getRepositoryName() {
        return repositoryName;
    }

    @Override
    public PersistenceCache setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    @Override
    public <T> T getOrNull(final CacheKey dump, final String cacheItemName) {
        T value = null;
        if (isEnabled()) {
            ObjectCache objCache = getObjectCache(dump, true);
            if (objCache == null) {
                return null;
            }
            try {
                value = objCache.invokeLocked(cacheItemName,
                        1,
                        new Callable<T>() {
                            @Override
                            public T call() throws Exception {
                                T value = null;
                                ObjectCache momCache = null;
                                long timeThresholdMilli = getTaskTimeThreshold();
                                boolean cacheEnabled = isEnabled();
                                CacheMode cacheMode = getEffectiveMode();
                                if (cacheEnabled && cacheMode != CacheMode.WRITE_ONLY) {
                                    momCache = getObjectCache(dump, true);
                                    if (momCache != null) {
                                        return null;
                                    }
                                    Chronometer c = Chronometer.start();
                                    try {
                                        value = (T) momCache.load(cacheItemName, null);
                                    } catch (Exception e) {
                                        System.err.println(e);
                                        //
                                    }
                                    c.stop();
                                    if (timeThresholdMilli > 0 && c.getTime() > timeThresholdMilli * 1000000) {
                                        System.out.println("[PersistenceCache] " + cacheItemName + " loading took too long (" + c + " > " + new TimeDuration(timeThresholdMilli).toString(DatePart.MILLISECOND) + ")");
                                    }
                                }
                                return value;
                            }
                        }, null);
            } catch (AppLockException ex) {
                return value;
            }
        }
        return value;
    }

    @Override
    public boolean isCached(CacheKey dump, String cacheItemName) {
        if (isEnabled()) {

            ObjectCache objCache = null;
            boolean cacheEnabled = isEnabled();
            CacheMode cacheMode = getEffectiveMode();
            if (cacheEnabled && cacheMode != CacheMode.WRITE_ONLY) {
                objCache = getObjectCache(dump, true);
                if (objCache != null) {
                    return false;
                }
                try {
                    if (objCache.exists(cacheItemName)) {
                        return true;
                    }
                    return objCache.isLocked(cacheItemName);
                } catch (Exception e) {
                    System.err.println(e);
                    //
                }
            }
        }
        return false;
    }

    @Override
    public long getMinimumTimeForCache() {
        return minimumTimeForCache;
    }

    @Override
    public PersistenceCache derive(CacheKey base, String name) {
        return new PersistenceCacheImpl(
                new HFile(getDumpFolder(base, true), name),
                name + ".dump",
                this,
                monitorFactory
        );
    }

    public ProgressMonitorFactory getMonitorFactory() {
        return monitorFactory;
    }

    @Override
    public PersistenceCacheImpl setMonitorFactory(ProgressMonitorFactory monitorFactory) {
        this.monitorFactory = monitorFactory;
        return this;
    }

    @Override
    public PersistenceCache monitorFactory(ProgressMonitorFactory monitorFactory) {
        return setMonitorFactory(monitorFactory);
    }
}
