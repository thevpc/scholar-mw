package net.thevpc.scholar.hadruwaves.project.configuration;

import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.DefaultPropertyListeners;
import net.thevpc.common.props.impl.PropertyBase;

import net.thevpc.nuts.elem.NElement;
import net.thevpc.nuts.elem.NToElement;
import net.thevpc.scholar.hadruwaves.project.HWProject;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.thevpc.common.props.impl.PropertyAdjusterContext;

import net.thevpc.scholar.hadruwaves.project.HWSolution;

public class HWConfigurations extends PropertyBase implements NToElement {

    private HWConfigurationFolderHelper childrenHelper = new HWConfigurationFolderHelper(null, () -> project().get());
    private WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private WritableValue<HWConfigurationRun> activeConfiguration = Props.of("activeConfiguration")
            .adjust(new PropertyAdjuster() {
                @Override
                public void adjust(PropertyAdjusterContext context) {
                    Object nv = context.newValue();
                    if (nv == null) {
                        HWConfigurationRun v = findInitialDefault();
                        if (v != null) {
                            context.doInstead(() -> context.writableValue().set(v));
                        }
                    } else {
                        //
                    }
                }
            })
            .valueOf(HWConfigurationRun.class, null);
    private DefaultPropertyListeners listeners = new DefaultPropertyListeners(this);

    public HWConfigurations(HWProject project0) {
        super("configurations");
        project.set(project0);
        listeners.addDelegate(childrenHelper.children);
        listeners.addDelegate(activeConfiguration);
        HWConfigurationRun aDefault = new HWConfigurationRun("default");
        ((WritableValue<HWProject>) aDefault.project()).set(project.get());
        aDefault.name().vetos().add(new PropertyVeto() {
            @Override
            public void vetoableChange(PropertyEvent event) {
                switch (event.eventType()) {
                    case UPDATE: {
                        if (!"default".equals(event.newValue())) {
                            throw new IllegalArgumentException("Default Configuration cannot be renamed");
                        }
                        break;
                    }
                }
            }
        });
        children().add(aDefault);
        activeConfiguration.set(aDefault);
        children().vetos().add(new PropertyVeto() {
            @Override
            public void vetoableChange(PropertyEvent event) {
                switch (event.eventType()) {
                    case REMOVE: {
                        WritableNamedNode<HWConfigurationElement> oldValue = event.oldValue();
                        if ("default".equals(oldValue.get().name().get())) {
                            throw new IllegalArgumentException("Default Configuration cannot be removed");
                        }
                    }
                }
            }
        });
        children().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                Object ov = event.oldValue();
                if (ov instanceof AbstractHWConfigurationElement) {
                    WritableValue<HWProject> p = (WritableValue<HWProject>) (((AbstractHWConfigurationElement) ov).project());
                    p.set(null);
                    WritableValue<HWSolution> s = (WritableValue<HWSolution>) (((AbstractHWConfigurationElement) ov).solution());
                    s.set(project.get().solution().get());
                }
                Object nv = event.newValue();
                if (nv instanceof AbstractHWConfigurationElement) {
                    WritableValue<HWProject> p = (WritableValue<HWProject>) (((AbstractHWConfigurationElement) nv).project());
                    p.set(project.get());
                    WritableValue<HWSolution> s = (WritableValue<HWSolution>) (((AbstractHWConfigurationElement) nv).solution());
                    s.set(project.get().solution().get());
                }
            }
        });
    }

    public ObservableValue<HWProject> project() {
        return project.readOnly();
    }

    private HWConfigurationRun findInitialDefault() {
        for (HWConfigurationElement value : children().values()) {
            HWConfigurationElement e = value;
            if (e instanceof HWConfigurationRun) {
                if ("default".equals(e.name().get())) {
                    return (HWConfigurationRun) e;
                }
            }
        }
        return null;
    }

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    public List<HWConfigurationRun> findRuns() {
        return childrenHelper.findRuns();
    }

    public WritableValue<HWConfigurationRun> activeConfiguration() {
        return activeConfiguration;
    }

    public WritableLiMap<String, HWConfigurationElement> children() {
        return childrenHelper.children();
    }

    @Override
    public NElement toElement() {
        return NElement.ofArrayBuilder("Configurations")
                .addAll(children().values().stream().map(x -> x.toElement()).collect(Collectors.toList()))
                .build();
    }

    public HWConfigurationElement findElement(String path) {
        return childrenHelper.findElement(path);
    }

    public HWConfigurationElement findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

    public HWConfigurationElement remove(String path) {
        return childrenHelper.remove(path);
    }

    public void add(HWConfigurationElement element, String path) {
        childrenHelper.add(element, path);
    }

    public List<HWConfigurationElement> removeDeepComponents(Predicate<HWConfigurationElement> filter, boolean prune) {
        return childrenHelper.removeDeepComponents(filter, prune);
    }
}
