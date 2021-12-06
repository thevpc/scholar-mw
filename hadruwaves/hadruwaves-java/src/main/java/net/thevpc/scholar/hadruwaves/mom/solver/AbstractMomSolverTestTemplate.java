/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver;

import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.WritableString;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPolygon;

/**
 *
 * @author vpc
 */
public abstract class AbstractMomSolverTestTemplate implements MomSolverTestTemplate {

    private final WritableString name = Props2.of("name").stringOf(null);
    private final WritableString description = Props2.of("description").stringOf(null);
    private final WritableList<HWProjectPolygon> polygons = Props2.of("polygons").listOf(HWProjectPolygon.class);

    @Override
    public WritableString name() {
        return name;
    }

    @Override
    public WritableString description() {
        return description;
    }

    @Override
    public WritableList<HWProjectPolygon> polygons() {
        return polygons;
    }

}
