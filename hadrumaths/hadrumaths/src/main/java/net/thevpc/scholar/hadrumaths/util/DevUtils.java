package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.scholar.hadrumaths.Maths;

public class DevUtils {

    public static void run(Runnable r) {
        if (Maths.Config.isDevelopmentMode()) {
            r.run();
        }
    }

    public static void log(String r) {
        if (Maths.Config.isDevelopmentMode()) {
            System.out.println(r);
        }
    }
}
