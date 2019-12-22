package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.MathsBase;

public class DevUtils {

    public static void run(Runnable r) {
        if (MathsBase.Config.isDevelopmentMode()) {
            r.run();
        }
    }

    public static void log(String r) {
        if (MathsBase.Config.isDevelopmentMode()) {
            System.out.println(r);
        }
    }
}
