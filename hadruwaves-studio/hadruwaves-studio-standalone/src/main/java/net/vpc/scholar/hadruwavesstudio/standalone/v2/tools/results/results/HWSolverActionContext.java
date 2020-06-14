/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results;

import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public interface HWSolverActionContext {

    Application app();
    
    HadruwavesStudio studio();

    HWConfigurationRun configuration();

    HWProject project();

    HWSolverResult loadResult(String path,HWSolverResultLocationType type);


//    HWSolverResult newResult(String resultId);

}
