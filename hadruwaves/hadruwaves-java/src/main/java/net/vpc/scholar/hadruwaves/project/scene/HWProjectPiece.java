/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.scholar.hadruwaves.project.HWProjectComponent;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruwaves.project.WritableSelectable;

/**
 *
 * @author vpc
 */
public interface HWProjectPiece extends HWProjectComponent, WritableSelectable {

    WritablePValue<Element3DTemplate> geometry();

    WritablePValue<HWMaterialTemplate> material();
}
