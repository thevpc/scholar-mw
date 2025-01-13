/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.mom.solver;

import net.thevpc.common.mon.ProgressMonitorFactory;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.solvers.AbstractHWSolverTemplate;
import net.thevpc.scholar.hadruwaves.solvers.HWSolver;

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
