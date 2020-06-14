/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.customresults;

import net.vpc.common.props.PValue;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.actions.HWSolverActionNode;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results.HWSolverResult;

/**
 *
 * @author vpc
 */
public interface HWSolverCustomResultValue extends HWSolverActionNode {

    PValue<HWSolverResult> value();
}
