/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.customresults;

import net.vpc.common.props.PList;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.HWSolverActionNode;

/**
 *
 * @author vpc
 */
public interface HWSolverCustomResultFolder extends HWSolverActionNode {

    PList<HWSolverActionNode> children();
}
