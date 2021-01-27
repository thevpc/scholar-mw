package net.thevpc.scholar.hadruwaves;

import net.thevpc.common.mvn.PomId;
import net.thevpc.common.mvn.PomIdResolver;
import net.thevpc.scholar.hadrumaths.HadrumathsService;
import net.thevpc.scholar.hadrumaths.HadrumathsServiceDesc;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadruwaves.mom.ModeFunctions;
import net.thevpc.scholar.hadruwaves.mom.TestFunctions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by vpc on 3/20/17.
 */
@HadrumathsServiceDesc(order = 100)
public class HadruwavesService implements HadrumathsService {
    private static final Logger log = Logger.getLogger(HadruwavesService.class.getName());
    public static String getVersion() {
        return PomIdResolver.resolvePomId(HadruwavesService.class,new PomId("","","DEV")).getVersion();
    }

    @Override
    public void installService() {
        log.log(Level.INFO, "Initializing Hadruwaves component...(hadruwaves version "+ getVersion()+")");
        Maths.Config.addConfigChangeListener("cacheEnabled", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                ModeIndex.updateCache();
            }
        });
        Plot.Config.registerConverter(ModeFunctions.class, new Function() {
            @Override
            public Object apply(Object value) {
                return ((ModeFunctions) value).toList();
            }
        });
        Plot.Config.registerConverter(TestFunctions.class, new Function() {
            @Override
            public Object apply(Object value) {
                return ((TestFunctions) value).toList();
            }
        });
    }
}
