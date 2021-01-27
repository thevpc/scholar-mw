package net.thevpc.scholar.hadrumaths.plot.filetypes;

import net.thevpc.scholar.hadruplot.extension.defaults.SimplePlotModelProvider;
import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.common.io.IOUtils;
import net.thevpc.scholar.hadrumaths.cache.ObjectCache;
import net.thevpc.scholar.hadruplot.*;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public final class PlotFileTypeObjectCache implements PlotFileType {
    public static final PlotFileType INSTANCE = new PlotFileTypeObjectCache();

    private PlotFileTypeObjectCache() {
    }

    @Override
    public String getTitle() {
        return "Cache File";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"jobj",
                ObjectCache.CACHE_OBJECT_FILE_EXTENSION};
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {

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

    @Override
    public boolean equals(Object obj) {
        return getClass().getName().equals(obj.getClass().getName());
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}
