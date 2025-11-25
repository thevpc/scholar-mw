package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.UserObjects;
import net.thevpc.common.props.DefaultUserObjects;
import net.thevpc.common.props.WritableString;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.HWSolution;

public abstract class AbstractHWConfigurationElement implements HWConfigurationElement {

    protected final WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    protected final WritableValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected final WritableValue<HWConfigurationFolder> parent = Props.of("parent").valueOf(HWConfigurationFolder.class, null);
    protected final WritableString parentPath = Props.of("parentPath").stringOf(null);
    private final WritableString description = Props.of("description").stringOf(null);
    private final WritableString name = Props.of("name").stringOf(null);
    private final UserObjects userObjects = new DefaultUserObjects();

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

    public WritableString description() {
        return description;
    }

    public WritableString name() {
        return name;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }
}
