/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.interop;

import net.vpc.scholar.hadrumaths.Matrix;

/**
 *
 * @author vpc
 */
public interface InteropHelper {

    public Matrix inv(Matrix c);

    public Matrix mul(Matrix a,Matrix b);

    public Matrix add(Matrix a,Matrix b);

    public Matrix solve(Matrix a, Matrix b);
}
