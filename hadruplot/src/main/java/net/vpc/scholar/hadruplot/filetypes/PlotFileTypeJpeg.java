package net.vpc.scholar.hadruplot.filetypes;

import net.vpc.scholar.hadruplot.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlotFileTypeJpeg implements PlotFileType {
    public static final PlotFileType INSTANCE=new PlotFileTypeJpeg();
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

}
