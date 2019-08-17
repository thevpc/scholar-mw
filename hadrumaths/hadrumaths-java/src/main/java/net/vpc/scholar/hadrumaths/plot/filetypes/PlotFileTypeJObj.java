package net.vpc.scholar.hadrumaths.plot.filetypes;

import net.vpc.common.io.IOUtils;
import java.io.UncheckedIOException;
import net.vpc.scholar.hadrumaths.cache.ObjectCache;
import net.vpc.scholar.hadrumaths.io.HadrumathsIOUtils;
import net.vpc.scholar.hadruplot.*;

import java.io.File;
import java.io.IOException;

public class PlotFileTypeJObj implements PlotFileType {
    public static final PlotFileType INSTANCE = new PlotFileTypeJObj();

    @Override
    public String getTitle() {
        return "JFig Object";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"jobj", ObjectCache.CACHE_OBJECT_FILE_EXTENSION};
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {
        HadrumathsIOUtils.saveZippedObject(file.getPath(), plotProvider.getModel());
    }

    @Override
    public void save(File file, PlotComponent component) throws IOException {
        save(file, new SimplePlotModelProvider(component.getModel(), component.toComponent()));
    }

    @Override
    public PlotModel loadModel(File file) {
        Object o = null;
        try {
            o = IOUtils.loadZippedObject(file.getPath());
        } catch (ClassNotFoundException ee) {
            throw new UncheckedIOException(new IOException(ee));
        } catch (IOException ee) {
            throw new UncheckedIOException(ee);
        }
        if (o != null) {
            return Plot.title(file.getName()).createModel(o);
        } else {
            throw new IllegalArgumentException("Unsupported Type " + file);
        }
    }
}
