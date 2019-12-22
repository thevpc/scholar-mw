/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruplot;

/**
 *
 * @author vpc
 */
public interface PlotModel1D extends PlotModel {

    public boolean is1dColumn();

    public double[] get1dX();

    public Object[] get1dY();

    PlotDoubleConverter getConverter();
}
