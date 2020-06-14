/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.mom.solver;

import net.vpc.common.mon.ProgressMonitorFactory;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.solvers.AbstractHWSolverTemplate;
import net.vpc.scholar.hadruwaves.solvers.HWSolver;

/**
 *
 * @author vpc
 */
public class HWSolverTemplateFDM extends AbstractHWSolverTemplate {

    @Override
    public HWSolver eval(HWConfigurationRun configuration, ProgressMonitorFactory taskMonitorManager) {
        throw new IllegalArgumentException("Not Supported yet");
    }

}
