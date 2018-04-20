package net.vpc.scholar.hadruwaves;

import net.vpc.scholar.hadrumaths.HadrumathsService;
import net.vpc.scholar.hadrumaths.HadrumathsServiceDesc;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.util.Converter;
import net.vpc.scholar.hadrumaths.util.IOUtils;
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

    @Override
    public void installService() {
        log.log(Level.INFO, "Initializing Hadruwaves component...(hadruwaves version "+ IOUtils.getArtifactVersionOrDev("net.vpc.scholar","hadruwaves")+")");
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
