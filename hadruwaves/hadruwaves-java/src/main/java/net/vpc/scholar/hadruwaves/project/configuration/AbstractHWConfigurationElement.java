package net.vpc.scholar.hadruwaves.project.configuration;

import net.vpc.common.props.PValue;
import net.vpc.common.props.Props;
import net.vpc.common.props.UserObjects;
import net.vpc.common.props.UserObjectsImpl;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWSolution;

public class AbstractHWConfigurationElement implements HWConfigurationElement {

    protected final WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    protected final WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected final WritablePValue<HWConfigurationFolder> parent = Props.of("parent").valueOf(HWConfigurationFolder.class, null);
    protected final WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    private final WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private final WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private final UserObjects userObjects = new UserObjectsImpl();

    @Override
    public UserObjects userObjects() {
        return userObjects;
    }

    @Override
    public PValue<HWProject> project() {
        return project;
    }

    @Override
    public PValue<HWSolution> solution() {
        return solution;
    }

    @Override
    public PValue<HWConfigurationFolder> parent() {
        return parent;
    }

    public PValue<String> parentPath() {
        return parentPath;
    }

    public WritablePValue<String> description() {
        return description;
    }

    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }
}
