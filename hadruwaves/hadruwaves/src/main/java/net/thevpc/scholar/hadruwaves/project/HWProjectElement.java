/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.tson.TsonSerializable;

/**
 *
 * @author vpc
 */
public interface HWProjectElement extends TsonSerializable{

    ObservableValue<HWSolution> solution();

    ObservableValue<HWProject> project();

    ObservableValue<String> name();

}
