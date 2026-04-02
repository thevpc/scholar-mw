package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.nuts.io.NIOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class HIOUtils {
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
}
