package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Maths;

public class DevTools {

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
