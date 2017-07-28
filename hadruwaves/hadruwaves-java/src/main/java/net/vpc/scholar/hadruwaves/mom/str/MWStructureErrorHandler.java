/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.str;

import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadruwaves.str.MWStructure;

/**
 *
 * @author vpc
 */
public interface MWStructureErrorHandler {
    public void showError(String title, Throwable e, Matrix m,MWStructure str);
}
