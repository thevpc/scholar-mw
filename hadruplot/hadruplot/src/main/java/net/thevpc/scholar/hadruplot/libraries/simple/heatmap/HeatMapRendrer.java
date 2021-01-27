/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruplot.libraries.simple.heatmap;

import java.awt.*;

/**
 *
 * @author vpc
 */
public interface HeatMapRendrer {

    public void paintValue(HeatMapPlotArea area, Graphics g, Rectangle bounds, double value, int x, int y, int width, int height, boolean selected);

    public void paintAnnotations(HeatMapPlotArea area, Graphics g);
}
