/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver;

import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPolygon;

/**
 *
 * @author vpc
 */
public interface MomSolverTestTemplate {

    WritableString name();

    WritableString description();

    WritableList<HWProjectPolygon> polygons();

    Expr[] generate(HWSolverMoM solver);
}
