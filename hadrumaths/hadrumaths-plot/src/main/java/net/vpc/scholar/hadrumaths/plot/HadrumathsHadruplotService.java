/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.plot;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.extension.HadruplotService;
import net.vpc.scholar.hadruplot.extension.HadruplotServiceDesc;

/**
 * @author vpc
 */
@HadruplotServiceDesc(order = 10)
public class HadrumathsHadruplotService implements HadruplotService {

    @Override
    public void installService() {
        //just to load Maths
        Maths.getHadrumathsVersion();
        MathsPlot.getHadrumathsPlotVersion();
    }

}
