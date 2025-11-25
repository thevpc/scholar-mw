/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.*;
import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurations;
import net.thevpc.scholar.hadruwaves.project.parameter.HWParameters;
import net.thevpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectScene;

import net.thevpc.scholar.hadruwaves.project.parameter.HWUnits;

/**
 *
 * @author vpc
 */
public interface HWProject extends NToElement, HWSolutionElement, Property, FileObject {

    String uuid();

    @Override
    WritableString name();

    @Override
    WritableString description();

    HWParameters parameters();

    HWUnits units();

    HWConfigurations configurations();

    WritableValue<HWProjectScene> scene();

    WritableLiMap<String, HWMaterialTemplate> materials();

    boolean isPersistent();
    
    void requirePersistent();

    void load(NElement elem);
}
