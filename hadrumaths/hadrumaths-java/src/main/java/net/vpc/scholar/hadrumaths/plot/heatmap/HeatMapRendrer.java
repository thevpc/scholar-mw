/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot.heatmap;

import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author vpc
 */
public interface HeatMapRendrer {

    public void paintValue(HeatMapPlotArea area, Graphics g, Dimension size, double value, int x, int y, int width, int heigh, boolean selected);

    public void paintAnnotations(HeatMapPlotArea area, Graphics g);
}
