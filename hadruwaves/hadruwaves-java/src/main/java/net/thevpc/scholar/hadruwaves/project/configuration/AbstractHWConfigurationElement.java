package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.UserObjects;
import net.thevpc.common.props.UserObjectsImpl;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.HWSolution;

public class AbstractHWConfigurationElement implements HWConfigurationElement {

    protected final WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    protected final WritableValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected final WritableValue<HWConfigurationFolder> parent = Props.of("parent").valueOf(HWConfigurationFolder.class, null);
    protected final WritableValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    private final WritableValue<String> description = Props.of("description").valueOf(String.class, null);
    private final WritableValue<String> name = Props.of("name").valueOf(String.class, null);
    private final UserObjects userObjects = new UserObjectsImpl();

    @Override
    public UserObjects userObjects() {
        return userObjects;
    }

    @Override
    public ObservableValue<HWProject> project() {
        return project;
    }

    @Override
    public ObservableValue<HWSolution> solution() {
        return solution;
    }

    @Override
    public ObservableValue<HWConfigurationFolder> parent() {
        return parent;
    }

    public ObservableValue<String> parentPath() {
        return parentPath;
    }

    public WritableValue<String> description() {
        return description;
    }

    public WritableValue<String> name() {
        return name;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }
}
