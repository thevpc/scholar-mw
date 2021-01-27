package net.thevpc.scholar.hadruplot.filetypes;

import net.thevpc.scholar.hadruplot.extension.PlotModelProvider;
import net.thevpc.scholar.hadruplot.model.PlotModel;
import net.thevpc.scholar.hadruplot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class PlotFileTypeJpeg implements PlotFileType {
    public static final PlotFileType INSTANCE=new PlotFileTypeJpeg();

    private PlotFileTypeJpeg() {
    }

    @Override
    public String getTitle() {
        return "JPG";
    }

    @Override
    public String[] getExtensions() {
        return new String[]{"jpg","jpeg"};
    }

    @Override
    public void save(File file, PlotModelProvider plotProvider) throws IOException {
        BufferedImage img = Plot.getImage(plotProvider);
        if (img != null) {
            ImageIO.write(img, "JPG", file);
        }
    }

    @Override
    public void save(File file, PlotComponent component) throws IOException {
        BufferedImage img = Plot.getImage(component);
        if (img != null) {
            ImageIO.write(img, "JPG", file);
        }

    }

    @Override
    public PlotModel loadModel(File file) {
        throw new IllegalArgumentException("Unsupported");
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
