package net.vpc.scholar.hadruwaves;

import net.vpc.common.mvn.PomId;
import net.vpc.common.mvn.PomIdResolver;
import net.vpc.common.util.Converter;
import net.vpc.scholar.hadrumaths.HadrumathsService;
import net.vpc.scholar.hadrumaths.HadrumathsServiceDesc;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.Plot;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
        Plot.Config.registerConverter(ModeFunctions.class, new Converter() {
            @Override
            public Object convert(Object value) {
                return ((ModeFunctions) value).toList();
            }
        });
        Plot.Config.registerConverter(TestFunctions.class, new Converter() {
            @Override
            public Object convert(Object value) {
                return ((TestFunctions) value).toList();
            }
        });
    }
}
