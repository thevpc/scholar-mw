/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.scholar.hadruwaves.project.HWProjectComponent;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.WritableSelectable;

/**
 *
 * @author vpc
 */
public interface HWProjectPiece extends HWProjectComponent, WritableSelectable {

    WritableValue<Element3DTemplate> geometry();

    WritableValue<HWMaterialTemplate> material();
}
