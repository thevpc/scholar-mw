package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.util.IOUtils;
import net.vpc.scholar.hadrumaths.util.LogUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

@HadrumathsServiceDesc(order = 0)
public class HadrumathsInitializerService implements HadrumathsService {
    private static final Logger log = Logger.getLogger(HadrumathsInitializerService.class.getName());

    @Override
    public void installService() {
        LogUtils.initialize();
        log.log(Level.INFO, "Initializing Hadrumaths component...(hadrumaths version " + IOUtils.getArtifactVersionOrDev("net.vpc.scholar", "hadrumaths") + ")");
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
    }
}
