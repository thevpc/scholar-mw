package net.vpc.scholar.hadrumaths.cache;

import net.vpc.common.util.Chronometer;
import net.vpc.common.mon.*;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.io.DefaultCacheObjectSerializedForm;
import net.vpc.scholar.hadrumaths.io.FailStrategy;
import net.vpc.scholar.hadrumaths.io.HFile;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 13:42:55
 */
public class DefaultObjectCache implements ObjectCache {

    private DumpPath dump;
    private Map<String, String> parsedValues;
    private PersistenceCache persistenceCache;
//    private String folderPath;

    DefaultObjectCache(DumpPath dump, PersistenceCache persistenceCache) {
        this.dump = dump;
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

    public static Object loadObject(HFile file, Object defaultValue) throws IOException {
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
                        theIs = new ProgressMonitorInputStream2(fis, length,
                                new DialogProgressMonitor(null,
                                        "Reading (" + Maths.formatMemory(length) + ") " + file
                                )
                        );
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

    public static void storeObject(HFile path, Object object, ProgressMonitor computationMonitor, String message) throws IOException {
        ProgressMonitor monitor = ProgressMonitorFactory.nonnull(computationMonitor);
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

    public String getDump() {
        return dump.getDump();
    }

    public DumpCacheFile getFile(String name) {
        return persistenceCache.getFile(dump, name);
    }

    @Override
    public HFile getFolder() {
        return persistenceCache.getDumpFolder(dump, true);
    }

    @Override
    public boolean exists(String name) {
        return getObjectCacheFile(name).exists();
    }

    public void store(String name, Object o, ProgressMonitor computationMonitor) {
        ProgressMonitor m = ProgressMonitorFactory.nonnull(computationMonitor);
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

    public Object load(String name) throws IOException {
        return load(name, null);
    }

    @Override
    public <V> V invokeLocked(String name, long lockTimeout, Callable<V> runnable) {
        return getObjectCacheFile(name).getLock().invoke(lockTimeout, runnable);
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
            return loadObject(file.getFile(), o);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void addStat(String statName, long statValueNano) {

        PrintStream ps = null;
        try {
            try {
                DumpCacheFile sf = getFile("statistics" + CACHE_LOG_SUFFIX);
                ps = new PrintStream(sf.getFile().getOutputStream(true));
                ps.println(statName + " = " + statValueNano + " (" + Chronometer.formatPeriodNano(statValueNano) + ")");
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

    public Set<String> getSet(String setName) {
        TreeSet<String> set = new TreeSet<String>();
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
                        set.add(trimmed);
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
    public void addSetItem(String setName, String item) {
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

    public void log(String statName) {

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
    public long getStat(String statName) {
        int count = 0;
        long value = 0;
        BufferedReader in = null;
        try {
            DumpCacheFile cfile = getFile("statistics" + CACHE_LOG_SUFFIX);
            in = new BufferedReader(cfile.getFile().getReader());
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith(statName + " = ")) {
                    count++;
                    value += Long.parseLong(line.substring(line.indexOf('=') + 1, line.indexOf('(')).trim());
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
    public String toString() {
        return "cache://" + getFolder().toString();
    }

    @Override
    public <T> T evaluate(String cacheItemName, ProgressMonitor monitor, CacheEvaluator evaluator, Object... args) {
        return persistenceCache.evaluate(this, cacheItemName, monitor, evaluator, args);
    }

    @Override
    public boolean delete() {
        return delete(FailStrategy.FAIL_FAST);
    }

    public boolean delete(FailStrategy failStrategy) {
        return getFolder().deleteFolderTree(null, failStrategy);
    }
}
