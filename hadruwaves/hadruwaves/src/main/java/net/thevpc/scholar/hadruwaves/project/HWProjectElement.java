/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.nuts.elem.NToElement;
import net.thevpc.nuts.elem.NToElement;

/**
 *
 * @author vpc
 */
public interface HWProjectElement extends NToElement {

    ObservableValue<HWSolution> solution();

    ObservableValue<HWProject> project();

    ObservableValue<String> name();

}
