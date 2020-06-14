/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.FileObject;
import net.vpc.common.props.WithListeners;
import net.vpc.common.props.WritablePLMap;
import net.vpc.common.props.WritablePValue;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonSerializable;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurations;
import net.vpc.scholar.hadruwaves.project.parameter.HWParameters;
import net.vpc.scholar.hadruwaves.project.scene.HWMaterialTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectScene;

import net.vpc.scholar.hadruwaves.project.parameter.HWUnits;

/**
 *
 * @author vpc
 */
public interface HWProject extends TsonSerializable, HWSolutionElement, WithListeners, FileObject {

    String uuid();

    @Override
    WritablePValue<String> name();

    @Override
    WritablePValue<String> description();

    HWParameters parameters();

    HWUnits units();

    HWConfigurations configurations();

    WritablePValue<HWProjectScene> scene();

    WritablePLMap<String, HWMaterialTemplate> materials();

    boolean isPersistent();
    
    void requirePersistent();

    void load(TsonElement tson);
}
