/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadruplot.HadruplotService;
import net.vpc.scholar.hadruplot.HadruplotServiceDesc;

/**
 *
 * @author vpc
 */
@HadruplotServiceDesc(order = 10)
public class HadrumathsHadruplotService implements HadruplotService {

    @Override
    public void installService() {
        //just to load Maths
        Maths.getHadrumathsVersion();
    }

}
