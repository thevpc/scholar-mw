/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.PValue;
import net.thevpc.tson.TsonSerializable;

/**
 *
 * @author vpc
 */
public interface HWProjectElement extends TsonSerializable{

    PValue<HWSolution> solution();

    PValue<HWProject> project();

    PValue<String> name();

}
