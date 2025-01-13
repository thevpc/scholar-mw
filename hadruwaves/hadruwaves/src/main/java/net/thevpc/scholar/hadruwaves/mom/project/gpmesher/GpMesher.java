/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadruwaves.mom.project.gpmesher;

import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.thevpc.scholar.hadruwaves.mom.project.MomProjectItem;

/**
 *
 * @author vpc
 */
public interface GpMesher extends MomProjectItem{
    MeshAlgo getMeshAlgo();
    GpPattern getPattern();
    Axis getInvariance();
    TestFunctionsSymmetry getSymmetry();

}
