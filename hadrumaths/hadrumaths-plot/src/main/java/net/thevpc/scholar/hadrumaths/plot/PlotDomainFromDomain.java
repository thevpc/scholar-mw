/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.plot;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.AbsolutePlotSamples;
import net.thevpc.scholar.hadruplot.PlotDomain;
import net.thevpc.scholar.hadruplot.PlotSamples;
import net.thevpc.scholar.hadruplot.RelativePlotSamples;

/**
 *
 * @author vpc
 */
public class PlotDomainFromDomain implements PlotDomain{
    private Domain domain;

    public PlotDomainFromDomain(Domain d) {
        this.domain = d;
    }

    public AbsolutePlotSamples toAbsolute(PlotSamples s) {
        if (s instanceof AbsolutePlotSamples) {
            return (AbsolutePlotSamples) s;
        }
        if (s instanceof RelativePlotSamples) {
            RelativePlotSamples r = (RelativePlotSamples) s;
            return new AbsolutePlotSamples(
                    toAbsolute(r.getX(), domain.getXMin(), domain.getXMax()),
                     toAbsolute(r.getY(), domain.getYMin(), domain.getYMax()),
                     toAbsolute(r.getZ(),domain. getZMin(), domain.getZMax())
            );
        }
        throw new IllegalArgumentException("Unsupported yet");
    }

    public Domain getDomain() {
        return domain;
    }
    
    
    private static double[] toAbsolute(double[] base, double min, double max) {
        if (base == null) {
            return null;
        }
        double[] r = new double[base.length];
        if (Double.isInfinite(min) || Double.isInfinite(max)) {
            if (Double.isInfinite(min) && Double.isInfinite(max)) {
                //all zero
//                for (int i = 0; i < r.length; i++) {
//                    r[i] = 0;
//                }
            } else {
                for (int i = 0; i < r.length; i++) {
                    r[i] = min;
                }
            }
            return r;
        } else {
            double w = max - min;
            for (int i = 0; i < r.length; i++) {
                r[i] = min + w * base[i];
            }
            return r;
        }
    }
}
