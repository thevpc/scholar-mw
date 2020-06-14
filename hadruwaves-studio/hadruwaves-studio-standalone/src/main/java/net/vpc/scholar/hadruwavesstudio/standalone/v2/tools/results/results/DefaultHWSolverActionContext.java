/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.results.results;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

/**
 *
 * @author vpc
 */
public class DefaultHWSolverActionContext implements HWSolverActionContext {

    private HWSolverActionResultRegistry registry;
    private HadruwavesStudio studio;
    private HWConfigurationRun configuration;

    public DefaultHWSolverActionContext(HadruwavesStudio studio, HWConfigurationRun configuration, HWSolverActionResultRegistry registry) {
        this.studio = studio;
        this.configuration = configuration;
        this.registry = registry;
    }

    @Override
    public Application app() {
        return studio().app();
    }

    @Override
    public HadruwavesStudio studio() {
        return studio;
    }

    @Override
    public HWProject project() {
        return configuration.project().get();
    }

    @Override
    public HWConfigurationRun configuration() {
        return configuration;
    }

    public HWSolverResult newResult(String resultId) {
        return registry.createResult(resultId);
    }

    @Override
    public HWSolverResult loadResult(String path, HWSolverResultLocationType type) {
        Pattern f = Pattern.compile("^.*(?<ext2>([.][^.]+)?)(?<ext1>([.][^.]+))$");
        Matcher m = f.matcher(path);
        if (m.find()) {
            String ext1 = m.group("ext1");
            String ext2 = m.group("ext2");
            if (".hwr".equals(ext1) && ext2 != null) {
                AbstractHWSolverActionResult rr = (AbstractHWSolverActionResult) newResult(ext2);
                rr.setLocationType(type);
                rr.loadFile(this, path);
            }
        }
        throw new IllegalArgumentException("Unsupported");
    }

}
