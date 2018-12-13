/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import net.vpc.common.io.RuntimeIOException;
import net.vpc.scholar.hadrumaths.Plot;

/**
 * @author vpc
 */
public interface PlotComponent {

    PlotWindowManager getPlotWindowManager();

    void setPlotWindowManager(PlotWindowManager windowManager);

    PlotContainer getParentPlotContainer();

    void setParentPlotContainer(PlotContainer parentPlotContainer);

    String getPlotTitle();

    void setPlotTitle(String title);

    String getLayoutConstraints();

    PlotComponent setLayoutConstraints(String layoutConstraints);

    JComponent toComponent();

    void display();

    void addPlotPropertyListener(PlotPropertyListener listener);

    void removePlotPropertyListener(PlotPropertyListener listener);

    BufferedImage getImage();

    default void saveImageFile(String file) throws RuntimeIOException {
        Plot.saveImageFile(this, file);
    }

}
