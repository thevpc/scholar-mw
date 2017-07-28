/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadruwaves.mom.project.gpmesher;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPattern;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectItem;

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
