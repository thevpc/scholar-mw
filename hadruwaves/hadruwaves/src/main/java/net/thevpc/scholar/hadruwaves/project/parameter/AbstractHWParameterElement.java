package net.thevpc.scholar.hadruwaves.project.parameter;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableString;
import net.thevpc.common.props.WritableValue;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public abstract class AbstractHWParameterElement implements HWParameterElement{
    protected WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    protected WritableValue<HWParameterFolder> parent = Props.of("parent").valueOf(HWParameterFolder.class, null);
    protected WritableString parentPath = Props.of("parentPath").stringOf(null);
    private WritableString description = Props.of("description").stringOf(null);
    private WritableString name = Props.of("name").stringOf(null);

    @Override
    public ObservableValue<HWProject> project() {
        return project;
    }

    @Override
    public ObservableValue<HWParameterFolder> parent() {
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

    @Override
    public void remove() {
        if(parent.get()!=null){
            parent.get().children().remove(name().get());
        }else if(project.get()!=null){
            project.get().parameters().children().remove(name().get());
        }
    }


}
