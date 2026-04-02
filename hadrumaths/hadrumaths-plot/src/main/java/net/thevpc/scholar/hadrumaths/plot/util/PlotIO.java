package net.thevpc.scholar.hadrumaths.plot.util;

import net.thevpc.nuts.io.NIOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class PlotIO {

    public static Object loadZippedObject(String physicalName) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new GZIPInputStream(new FileInputStream(NIOUtils.expandPath(physicalName))));
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }

    public static Object loadObject2(String physicalName) {
        physicalName = NIOUtils.expandPath(physicalName);
        try {
            File f = new File(physicalName);
            if (f.isFile()) {
                return loadObject(physicalName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Object loadObject(String physicalName) throws IOException, ClassNotFoundException {
        physicalName = NIOUtils.expandPath(physicalName);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(physicalName));
            return ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
            }
        }
    }


    public static void saveObject2(String physicalName, Object object) {
        try {
            saveObject(physicalName, object);
        } catch (Exception e) {
            e.printStackTrace();
            //ignore
        }
    }

    public static void saveObject(String physicalName, Object object) throws IOException {
        physicalName = NIOUtils.expandPath(physicalName);

        ObjectOutputStream oos = null;
        try {
            File f = new File(physicalName);
            if (f.getParentFile() != null) {
                f.getParentFile().mkdirs();
            }
            oos = new ObjectOutputStream(new FileOutputStream(physicalName));
            oos.writeObject(object);
            oos.close();
        } finally {
            if (oos != null) {
                oos.close();
            }
        }

    }
}
