/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver;

import net.thevpc.common.props.WritablePList;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectPolygon;

/**
 *
 * @author vpc
 */
public abstract class AbstractMomSolverTestTemplate implements MomSolverTestTemplate {

    private final WritablePValue<String> name = Props2.of("name").valueOf(String.class, null);
    private final WritablePValue<String> description = Props2.of("description").valueOf(String.class, null);
    private final WritablePList<HWProjectPolygon> polygons = Props2.of("polygons").listOf(HWProjectPolygon.class);

    @Override
    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public WritablePValue<String> description() {
        return description;
    }

    @Override
    public WritablePList<HWProjectPolygon> polygons() {
        return polygons;
    }

}
