/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot;

import net.thevpc.scholar.hadruplot.model.PlotModel;
import java.awt.image.BufferedImage;
import javax.swing.*;

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

    void saveImageFile(String file)  ;

    PlotModel getModel();

}
