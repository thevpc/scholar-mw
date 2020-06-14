/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.solver;

import net.vpc.common.props.WritablePList;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectPolygon;

/**
 *
 * @author vpc
 */
public interface MomSolverTestTemplate {

    WritablePValue<String> name();

    WritablePValue<String> description();

    WritablePList<HWProjectPolygon> polygons();

    Expr[] generate(HWSolverMoM solver);
}
