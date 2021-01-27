package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.WritablePLMap;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public class HWConfigurationFolder extends AbstractHWConfigurationElement {

    private HWConfigurationFolderHelper childrenHelper = new HWConfigurationFolderHelper(this, () -> project().get());

    public HWConfigurationFolder(String name) {
        name().set(name);
        childrenHelper.children().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                Object ov = event.getOldValue();
                if (ov instanceof AbstractHWConfigurationElement) {
                    WritablePValue<HWProject> p = (WritablePValue<HWProject>) (((AbstractHWConfigurationElement) ov).project());
                    p.set(null);
                }
                Object nv = event.getNewValue();
                if (nv instanceof AbstractHWConfigurationElement) {
                    WritablePValue<HWProject> p = (WritablePValue<HWProject>) (((AbstractHWConfigurationElement) nv).project());
                    p.set(project().get());
                }
            }
        });
    }

    public WritablePLMap<String, HWConfigurationElement> children() {
        return childrenHelper.children();
    }

    public List<HWConfigurationElement> removeDeepComponents(Predicate<HWConfigurationElement> filter, boolean prune) {
        return childrenHelper.removeDeepComponents(filter, prune);
    }

    public List<HWConfigurationRun> findRuns() {
        return childrenHelper.findRuns();
    }

    public HWConfigurationElement findElement(String path) {
        return childrenHelper.findElement(path);
    }

    public HWConfigurationElement findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj("Folder")
                .add("name", name().get())
                .add("description", description().get())
                .add("children", Tson.array().addAll(children().values().stream().map(x -> x.toTsonElement(context)).collect(Collectors.toList())))
                .build();
    }
}
