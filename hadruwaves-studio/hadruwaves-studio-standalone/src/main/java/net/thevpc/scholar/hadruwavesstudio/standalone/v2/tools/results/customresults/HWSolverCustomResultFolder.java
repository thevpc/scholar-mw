/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.customresults;

import net.thevpc.common.props.ObservableList;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.HWSolverActionNode;

/**
 *
 * @author vpc
 */
public interface HWSolverCustomResultFolder extends HWSolverActionNode {

    ObservableList<HWSolverActionNode> children();
}
