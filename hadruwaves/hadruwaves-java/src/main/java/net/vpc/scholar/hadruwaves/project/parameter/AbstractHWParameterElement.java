package net.vpc.scholar.hadruwaves.project.parameter;

import net.vpc.common.props.PValue;
import net.vpc.common.props.Props;
import net.vpc.common.props.WritablePValue;
import net.vpc.scholar.hadruwaves.project.HWProject;

public class AbstractHWParameterElement implements HWParameterElement{
    protected WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    protected WritablePValue<HWParameterFolder> parent = Props.of("parent").valueOf(HWParameterFolder.class, null);
    protected WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);
    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);

    @Override
    public PValue<HWProject> project() {
        return project;
    }

    @Override
    public PValue<HWParameterFolder> parent() {
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

    @Override
    public void remove() {
        if(parent.get()!=null){
            parent.get().children().remove(name().get());
        }else if(project.get()!=null){
            project.get().parameters().children().remove(name().get());
        }
    }


}
