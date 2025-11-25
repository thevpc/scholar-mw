package net.thevpc.scholar.hadrumaths.cache;

import net.thevpc.common.mon.*;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NElementParser;
import net.thevpc.scholar.hadrumaths.BooleanMarker;
import net.thevpc.scholar.hadrumaths.BooleanRef;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.io.DefaultCacheObjectSerializedForm;
import net.thevpc.scholar.hadrumaths.io.FailStrategy;
import net.thevpc.scholar.hadrumaths.io.HFile;
import net.thevpc.scholar.hadrumaths.util.NElementHelper;

import java.io.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 13:42:55
 */
public class DefaultObjectCache implements ObjectCache {

    private final CacheKey key;
    private final PersistenceCache persistenceCache;
    private Map<String, String> parsedValues;
//    private String folderPath;

    DefaultObjectCache(CacheKey key, PersistenceCache persistenceCache) {
        if (key == null) {
            throw new IllegalArgumentException("Null Key");
        }
        this.key = key;
        this.persistenceCache = persistenceCache;
    }


    public static CacheObjectSerializedForm toSerializedForm(Object value, HFile file) throws IOException {
        if (value == null) {
            return null;
        }
        if (value instanceof CacheObjectSerializedForm) {
            return (CacheObjectSerializedForm) value;
        }
        if (value instanceof CacheObjectSerializerProvider) {
            return ((CacheObjectSerializerProvider) value).createCacheObjectSerializedForm(file);
        }
        if (value instanceof Serializable) {
            return new DefaultCacheObjectSerializedForm((Serializable) value);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public Object load(String name) throws IOException {
        return load(name, null);
    }

    public static Object loadObject(HFile file, Object defaultValue, ProgressMonitorFactory ispmf) throws IOException {
        if (file.existsOrWait()) {
            try {
                //how to know if it is compressed?
                boolean compressed = (Maths.Config.isCompressCache());

                Object o1 = null;
                ObjectInputStream ois = null;
                try {
                    InputStream theIs = null;
                    InputStream fis = new BufferedInputStream(file.getInputStream());
                    long length = file.length();
                    if (length > 10000) {
                        if (ispmf == null) {
                            theIs = new ProgressMonitorInputStream2(fis, length,
                                    new DialogProgressMonitor(null,
                                            "Reading (" + Maths.formatMemory(length) + ") " + file
                                    )
                            );
                        } else {
                            theIs = new ProgressMonitorInputStream2(fis, length,
                                    ispmf.createMonitor("Reading (" + Maths.formatMemory(length) + ") " + file,null)
                            );
                        }
                    } else {
                        theIs = fis;
                    }
                    ois = new ObjectInputStream(compressed ? new GZIPInputStream(theIs) : theIs);
                    o1 = ois.readObject();
                } finally {
                    if (ois != null) ois.close();
                }
                if (o1 instanceof CacheObjectSerializedForm) {
                    HFile serFile = file.getFs().get(file.getPath() + ".ser");
                    CacheObjectSerializedForm s = ((CacheObjectSerializedForm) o1);
                    o1 = s.deserialize(serFile);
                }
                return o1;
            } catch (IOException e) {
                throw e;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return defaultValue;
    }

    public DumpCacheFile getFile(String name) {
        return persistenceCache.getFile(key, name);
    }

    public void log(NElement statName) {

        PrintStream ps = null;
        try {
            try {
                DumpCacheFile cfile = getFile("log" + CACHE_LOG_SUFFIX);
                ps = new PrintStream(cfile.getFile().getOutputStream(true));
                ps.println(statName);
                cfile.touch();
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public String toString() {
        return "cache://" + getFolder().toString();
    }

    @Override
    public HFile getFolder() {
        return persistenceCache.getDumpFolder(key, true);
    }

    @Override
    public boolean exists(String name) {
        return getObjectCacheFile(name).exists();
    }

    @Override
    public <V> V invokeLocked(String name, long lockTimeout, Callable<V> callable, ProgressMonitor monitor) {
        if (monitor != null) {
            try (MonitorableCallable<V> c = new MonitorableCallable<>(monitor, callable)) {
                return getObjectCacheFile(name).getLock().invoke(lockTimeout, c);
            }
        }
        return getObjectCacheFile(name).getLock().invoke(lockTimeout, callable);
    }

    @Override
    public boolean isLocked(String name) {
        return getObjectCacheFile(name).getLock().isLocked();
    }

    @Override
    public DumpCacheFile getObjectCacheFile(String name) {
        return getFile(name + CACHE_OBJECT_SUFFIX);
    }

    @Override
    public Object load(String name, Object o) {
        DumpCacheFile file = getObjectCacheFile(name);
        try {
            return loadObject(file.getFile(), o,persistenceCache.getMonitorFactory());
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void addStat(NElement item) {

        PrintStream ps = null;
        try {
            try {
                DumpCacheFile sf = getFile("statistics" + CACHE_LOG_SUFFIX);
                ps = new PrintStream(sf.getFile().getOutputStream(true));
                ps.println(item.toString(true));
                sf.touch();
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void addSetItem(String setName, NElement item) {
        if (!getSet(setName).contains(item)) {
            PrintStream ps = null;
            try {
                try {
                    DumpCacheFile cfile = getFile(setName + CACHE_LOG_SUFFIX);
                    HFile file = cfile.getFile();
                    file.mkdirParents();
                    ps = new PrintStream(file.getOutputStream(true));
                    ps.println(item);
                    cfile.touch();
                } finally {
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (Exception e) {
                //
            }
        }
    }

    public Set<NElement> getSet(String setName) {
        LinkedHashSet<NElement> set = new LinkedHashSet<>();
        BufferedReader in = null;
        try {
            DumpCacheFile cfile = getFile(setName + CACHE_LOG_SUFFIX);
            HFile file = cfile.getFile();
            if (file.existsOrWait()) {
                in = new BufferedReader(file.getReader());
                String line;
                while ((line = in.readLine()) != null) {
                    String trimmed = line.trim();
                    if (trimmed.length() > 0) {
                        set.add(NElementParser.ofTson().parse(trimmed));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("e = " + e);
            //
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return set;
    }

    @Override
    public long getStat(String statName) {
        int count = 0;
        long value = 0;
        BufferedReader in = null;
        try {
            DumpCacheFile cfile = getFile("statistics" + CACHE_LOG_SUFFIX);
            in = new BufferedReader(cfile.getFile().getReader());
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    NElement e = NElementParser.ofTson().parse(line);
                    count++;
                    value += e.toObject().get().get("eval").get().toObject().get().get("nanos").get().asLongValue().get();
                }
            }
            return count == 0 ? 0 : (value / count);
        } catch (Exception e) {
            System.err.println("e = " + e);
            //
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return 0;
    }

    @Override
    public <T> T evaluate(String cacheItemName, ProgressMonitor monitor, CacheEvaluator evaluator, Object... args) {
        return persistenceCache.evaluate(this, cacheItemName, monitor, evaluator, args);
    }

    @Override
    public boolean delete() {
        return delete(FailStrategy.FAIL_FAST);
    }

    public CacheKey getKey() {
        return key;
    }

    public void store(String name, Object o, ProgressMonitor computationMonitor) {
        ProgressMonitor m = ProgressMonitors.nonnull(computationMonitor);
        String message = "store " + name;
        Maths.invokeMonitoredAction(computationMonitor, message, new VoidMonitoredAction() {
            @Override
            public void invoke(ProgressMonitor monitor, String messagePrefix) throws IOException {
                DumpCacheFile file = getObjectCacheFile(name);
                HFile path = file.getFile();
                storeObject(path, o, m, message);
                file.touch();
            }
        });
    }

    public static void storeObject(HFile path, Object object, ProgressMonitor computationMonitor, String message) throws IOException {
        ProgressMonitor monitor = ProgressMonitors.nonnull(computationMonitor);
        if (object != null && object instanceof CacheObjectSerializerProvider) {
            HFile serFile = path.getFs().get(path.getPath() + ".ser");
            object = ((CacheObjectSerializerProvider) object).createCacheObjectSerializedForm(serFile);
            if (object == null) {
                throw new IOException("CacheObjectSerializedForm could not be null");
            }
        }
        if (Maths.Config.isCompressCache()) {
            if (monitor == null) {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new GZIPOutputStream(path.getOutputStream()));
                    oos.writeObject(object);
                    oos.close();
                } finally {
                    if (oos != null) oos.close();
                }
            } else {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream(new GZIPOutputStream(new ProgressMonitorOutputStream(path.getOutputStream(), monitor, message)));
                    oos.writeObject(object);
                    oos.close();
                } finally {
                    if (oos != null) oos.close();
                }
            }
        } else {
            if (monitor == null) {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream((path.getOutputStream()));
                    oos.writeObject(object);
                    oos.close();
                } finally {
                    if (oos != null) oos.close();
                }
            } else {
                ObjectOutputStream oos = null;
                try {
                    oos = new ObjectOutputStream((new ProgressMonitorOutputStream(path.getOutputStream(), monitor, message)));
                    oos.writeObject(object);
                    oos.close();
                } finally {
                    if (oos != null) oos.close();
                }
            }
        }
    }

    public boolean delete(FailStrategy failStrategy) {
        return getFolder().deleteFolderTree(null, failStrategy);
    }

    private static class MonitorableCallable<V> implements Callable<V>, AutoCloseable {
        private final BooleanRef blocked;
        private final ProgressMonitor monitor;
        private final Callable<V> callable;

        public MonitorableCallable(ProgressMonitor monitor, Callable<V> callable) {
            this.monitor = monitor;
            this.callable = callable;
            this.blocked = BooleanMarker.ref();
            blocked.set(true);
            monitor.setBlocked(true);
        }

        @Override
        public void close() {
            if (blocked.get()) {
                blocked.set(false);
                monitor.setBlocked(false);
            }
        }

        @Override
        public V call() throws Exception {
            blocked.set(false);
            monitor.setBlocked(false);
            return callable.call();
        }

        @Override
        public String toString() {
            return "MonitorableCallable(" + callable + ")";
        }
    }
}
