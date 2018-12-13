package net.vpc.scholar.hadrumaths;

import net.vpc.common.mvn.PomId;
import net.vpc.common.mvn.PomIdResolver;
import net.vpc.scholar.hadrumaths.io.HadrumathsIOUtils;
import net.vpc.scholar.hadrumaths.util.LogUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

@HadrumathsServiceDesc(order = 0)
public class HadrumathsInitializerService implements HadrumathsService {
    private static final Logger log = Logger.getLogger(HadrumathsInitializerService.class.getName());
    public static String getVersion() {
        return PomIdResolver.resolvePomId(HadrumathsInitializerService.class,new PomId("","","DEV")).getVersion();
    }

    @Override
    public void installService() {
        LogUtils.initialize();
        log.log(Level.INFO, "Initializing Hadrumaths component...(hadrumaths version " + Maths.getHadrumathsVersion() + ")");
        Maths.Config.setLogMonitorLevel(Level.ALL);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Maths.Config.close();
            }
        }));
    }
}
