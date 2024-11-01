package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.WritableLiMap;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.common.props.WritableValue;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruwaves.project.HWProject;

public class HWConfigurationFolder extends AbstractHWConfigurationElement {

    private HWConfigurationFolderHelper childrenHelper = new HWConfigurationFolderHelper(this, () -> project().get());

    public HWConfigurationFolder(String name) {
        name().set(name);
        childrenHelper.children().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                Object ov = event.oldValue();
                if (ov instanceof AbstractHWConfigurationElement) {
                    WritableValue<HWProject> p = (WritableValue<HWProject>) (((AbstractHWConfigurationElement) ov).project());
                    p.set(null);
                }
                Object nv = event.newValue();
                if (nv instanceof AbstractHWConfigurationElement) {
                    WritableValue<HWProject> p = (WritableValue<HWProject>) (((AbstractHWConfigurationElement) nv).project());
                    p.set(project().get());
                }
            }
        });
    }

    public WritableLiMap<String, HWConfigurationElement> children() {
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
        return Tson.ofObj("Folder")
                .add("name", name().get())
                .add("description", description().get())
                .add("children", Tson.ofArray().addAll(children().values().stream().map(x -> x.toTsonElement(context)).collect(Collectors.toList())))
                .build();
    }
}
