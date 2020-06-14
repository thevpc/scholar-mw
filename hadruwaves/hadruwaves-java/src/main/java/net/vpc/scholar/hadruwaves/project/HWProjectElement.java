/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.PValue;
import net.vpc.common.tson.TsonSerializable;

/**
 *
 * @author vpc
 */
public interface HWProjectElement extends TsonSerializable{

    PValue<HWSolution> solution();

    PValue<HWProject> project();

    PValue<String> name();

}
