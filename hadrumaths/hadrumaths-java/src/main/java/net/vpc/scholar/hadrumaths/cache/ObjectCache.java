package net.vpc.scholar.hadrumaths.cache;

import net.vpc.scholar.hadrumaths.Chronometer;
import net.vpc.scholar.hadrumaths.ComputationMonitorFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.util.*;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 2 juin 2007 13:42:55
 */
public class ObjectCache {

    public static final String CACHE_OBJECT_FILE_EXTENSION = "cacheobj";
    public static final String CACHE_LOG_FILE_EXTENSION = "cachelog";
    public static final String CACHE_DEF_FILE_EXTENSION = "cachedef";

    public static final String CACHE_OBJECT_SUFFIX = "." + CACHE_OBJECT_FILE_EXTENSION;
    public static final String CACHE_LOG_SUFFIX = "." + CACHE_LOG_FILE_EXTENSION;
    public static final String CACHE_DEF_SUFFIX = "." + CACHE_DEF_FILE_EXTENSION;
    private DumpPath dump;
    private Map<String, String> parsedValues;
    private PersistenceCache persistenceCache;
//    private String folderPath;

    ObjectCache(DumpPath dump, PersistenceCache persistenceCache) {
        this.dump = dump;
        this.persistenceCache = persistenceCache;
    }

    public String getDump() {
        return dump.getDump();
    }

    public DumpCacheFile getFile(String name) {
        return persistenceCache.getFile(dump, name);
    }

    public File getFolder() {
        return persistenceCache.getDumpFolder(dump, true);
    }

    public boolean exists(String name) {
        return getObjectCacheFile(name).exists();
    }

    public void store(String name, Object o,ComputationMonitor computationMonitor) throws IOException{
        EnhancedComputationMonitor m = ComputationMonitorFactory.enhance(computationMonitor);
        String message = "store " + name;
        Maths.invokeMonitoredAction(computationMonitor, message, new VoidMonitoredAction() {
            @Override
            public void invoke(EnhancedComputationMonitor monitor, String messagePrefix) throws IOException {
                DumpCacheFile file = getObjectCacheFile(name);
                File path = file.getFile().getAbsoluteFile();
                storeObject(path,o,m,message);
                file.touch();
            }
        });
    }

    public static void storeObject(File path, Object o,ComputationMonitor computationMonitor,String message) throws IOException{
        EnhancedComputationMonitor m = ComputationMonitorFactory.enhance(computationMonitor);
        if (o != null && o instanceof CacheObjectSerializerProvider) {
            File serFile = new File(path.getPath() + ".ser");
            o = ((CacheObjectSerializerProvider) o).createCacheObjectSerializedForm(serFile);
            if(o==null){
                throw new IOException("CacheObjectSerializedForm could not be null");
            }
        }
        IOUtils.saveZippedObject(path.getPath(), o,m,message);
    }

    public Object load(String name) throws IOException {
        return load(name, null);
    }

    public DumpCacheFile getObjectCacheFile(String name) {
        return getFile(name + CACHE_OBJECT_SUFFIX);
    }

    public Object load(String name, Object o) throws IOException {
        DumpCacheFile file = getObjectCacheFile(name);
        return loadObject(file.getFile(),o);
    }

    public static CacheObjectSerializedForm toSerializedForm(Object value,File file) throws IOException {
        if(value==null){
            return null;
        }
        if(value instanceof CacheObjectSerializedForm){
            return (CacheObjectSerializedForm) value;
        }
        if(value instanceof CacheObjectSerializerProvider){
            return ((CacheObjectSerializerProvider) value).createCacheObjectSerializedForm(file);
        }
        if(value instanceof Serializable){
            return new DefaultCacheObjectSerializedForm((Serializable) value);
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public static Object loadObject(File file,Object defaultValue) throws IOException {
        if (file.exists()) {
            try {
                Object o1 = loadZippedObject(file.getAbsolutePath());
                if(o1 != null && o1 instanceof CacheObjectSerializedForm) {
                    File path = file.getAbsoluteFile();
                    File serFile = new File(path.getPath() + ".ser");
                    CacheObjectSerializedForm s = ((CacheObjectSerializedForm) o1);
                    o1=s.deserialize(serFile);
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

    public static Object loadZippedObject(String physicalName) throws IOException, ClassNotFoundException {
        File file = new File(physicalName);
        ObjectInputStream ois = null;
        try {
            InputStream theIs = null;
            FileInputStream fis = new FileInputStream(physicalName);
            if (file.length() > 10000) {
                theIs = new ProgressMonitorInputStream2(null, "Reading " + physicalName, fis, file.length());
            } else {
                theIs = fis;
            }
            ois = new ObjectInputStream(new GZIPInputStream(theIs));
            return ois.readObject();
        } finally {
            if (ois != null) ois.close();
        }
    }

    public void addStat(String statName, long statValue) {

        PrintStream ps = null;
        try {
            try {
                DumpCacheFile sf = getFile("statistics" + CACHE_LOG_SUFFIX);
                ps = new PrintStream(new FileOutputStream(sf.getFile(), true));
                ps.println(statName + " = " + statValue + " (" + Chronometer.formatPeriod(statValue) + ")");
                sf.touch();
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (IOException e) {
            //
        }
    }

    public Set<String> getSet(String setName) {
        TreeSet<String> set = new TreeSet<String>();
        BufferedReader in = null;
        try {
            DumpCacheFile cfile = getFile(setName + CACHE_LOG_SUFFIX);
            File file = cfile.getFile();
            if (file.exists()) {
                in = new BufferedReader(new FileReader(file));
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

    public void addSetItem(String setName, String item) {
        if (!getSet(setName).contains(item)) {
            PrintStream ps = null;
            try {
                try {
                    DumpCacheFile cfile = getFile(setName + CACHE_LOG_SUFFIX);
                    File file = cfile.getFile();
                    file.getParentFile().mkdirs();
                    ps = new PrintStream(new FileOutputStream(file, true));
                    ps.println(item);
                    cfile.touch();
                } finally {
                    if (ps != null) {
                        ps.close();
                    }
                }
            } catch (IOException e) {
                //
            }
        }
    }

    public void log(String statName) {

        PrintStream ps = null;
        try {
            try {
                DumpCacheFile cfile = getFile("log" + CACHE_LOG_SUFFIX);
                ps = new PrintStream(new FileOutputStream(cfile.getFile(), true));
                ps.println(statName);
                cfile.touch();
            } finally {
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (IOException e) {
            //
        }
    }

    public long getStat(String statName) {
        int count = 0;
        long value = 0;
        BufferedReader in = null;
        try {
            DumpCacheFile cfile = getFile("statistics" + CACHE_LOG_SUFFIX);
            in = new BufferedReader(new FileReader(cfile.getFile()));
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

}
