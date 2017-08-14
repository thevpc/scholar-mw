package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.scalarproducts.MemComplexScalarProductCache;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductCache;
import net.vpc.scholar.hadrumaths.util.Converter;
import net.vpc.scholar.hadrumaths.util.LogUtils;

import java.util.logging.Level;

public class MathsInitializerService {
    public static void initialize() {
        LogUtils.initialize();
        Maths.Config.setLogMonitorLevel(Level.ALL);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (Maths.Config.DEFAULT_LARGE_MATRIX_FACTORY != null) {
                    try {
                        Maths.Config.DEFAULT_LARGE_MATRIX_FACTORY.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }));
        Plot.Config.registerConverter(ScalarProductCache.class, new Converter() {
            @Override
            public Object convert(Object value) {
                return ((ScalarProductCache) value).toMatrix();
            }
        });

    }
}
