package net.thevpc.scholar.hadrumaths;

import net.thevpc.nuts.artifact.NId;
import net.thevpc.scholar.hadrumaths.util.LogUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

@HadrumathsServiceDesc(order = 0)
public class HadrumathsInitializerService implements HadrumathsService {
    private static final Logger log = Logger.getLogger(HadrumathsInitializerService.class.getName());

    public static String getVersion() {
        return NId.getForClass(HadrumathsInitializerService.class).map(x->x.getVersion().getValue()).orElse("DEV");
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
