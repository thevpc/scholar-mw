/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwaves.mom.str;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadruwaves.str.MWStructure;

/**
 *
 * @author vpc
 */
public interface MWStructureErrorHandler {
    void showError(String title, Throwable e, ComplexMatrix m, MWStructure str);
}
