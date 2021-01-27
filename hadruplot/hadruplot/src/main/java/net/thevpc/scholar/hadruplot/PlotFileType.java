package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import java.io.File;
import java.io.IOException;

public interface PlotFileType {
    String getTitle();

    String[] getExtensions();

    void save(File file, PlotModelProvider plotProvider) throws IOException;

    void save(File file, PlotComponent component) throws IOException;

    PlotModel loadModel(File file);
}
