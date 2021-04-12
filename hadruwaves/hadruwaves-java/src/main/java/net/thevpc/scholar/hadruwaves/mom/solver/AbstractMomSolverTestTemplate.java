/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver;

import net.thevpc.common.props.WritableList;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPolygon;

/**
 *
 * @author vpc
 */
public abstract class AbstractMomSolverTestTemplate implements MomSolverTestTemplate {

    private final WritableValue<String> name = Props2.of("name").valueOf(String.class, null);
    private final WritableValue<String> description = Props2.of("description").valueOf(String.class, null);
    private final WritableList<HWProjectPolygon> polygons = Props2.of("polygons").listOf(HWProjectPolygon.class);

    @Override
    public WritableValue<String> name() {
        return name;
    }

    @Override
    public WritableValue<String> description() {
        return description;
    }

    @Override
    public WritableList<HWProjectPolygon> polygons() {
        return polygons;
    }

}
