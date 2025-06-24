package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.*;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruwaves.project.scene.DomainTemplate;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;

public class DefaultHWcene implements HWProjectScene {

    private WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private WritableList<HWProjectComponent> components = Props.of("components").listOf(HWProjectComponent.class);
    private DomainTemplate domain = new DomainTemplate();

    public DefaultHWcene(HWProject project) {
        this.project.set(project);
        components.onChange(x -> {
            AbstractHWProjectComponent e = x.newValue();
            if (e != null) {
                e.project.set(project);
            }
            if (x.newValue() instanceof HWProjectComponentGroup) {
                for (HWProjectComponent child : ((HWProjectComponentGroup) x).children().values().toList()) {
                    WritableList<HWProjectComponent> components = x.property();
                    components.remove(child);
                }
            }
        });
    }

    @Override
    public ObservableValue<HWProject> project() {
        return project.readOnly();
    }

    @Override
    public ObservableValue<HWSolution> solution() {
        return project.get().solution();
    }

    @Override
    public Domain getBoundingDomain(HWConfigurationRun configuration, float margin) {
        BoundDomain d = new BoundDomain();
        return updateBoundDomain(configuration, d).expandPercent(margin);
    }

    public Domain updateBoundDomain(HWConfigurationRun configuration, BoundDomain domain) {
        for (HWProjectComponent cmp : findDeepComponents(x -> x.enabled().eval(configuration))) {
            cmp.updateBoundDomain(configuration, domain);
        }
        Domain d1 = this.domain.eval(configuration);
        if (d1 != null) {
            return d1.ensureFiniteBounds(domain.toDomain());
        }
        return domain.toDomain();
    }

    @Override
    public DomainTemplate domain() {
        return domain;
    }

    @Override
    public WritableList<HWProjectComponent> components() {
        return components;
    }

    @Override
    public List<HWProjectComponent> findDeepComponents(Predicate<HWProjectComponent> filter) {
        Stack<HWProjectComponent> stack = new Stack<>();
        List<HWProjectComponent> all = new ArrayList<>();
        for (HWProjectComponent component : components) {
            if (filter.test(component)) {
                all.add(component);
            }
            if (component instanceof HWProjectComponentGroup) {
                for (HWProjectComponent child : ((HWProjectComponentGroup) component).children().values()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            HWProjectComponent component = stack.pop();
            if (filter.test(component)) {
                all.add(component);
            }
            if (component instanceof HWProjectComponentGroup) {
                for (HWProjectComponent child : ((HWProjectComponentGroup) component).children().values()) {
                    stack.push(child);
                }
            }
        }
        return all;
    }

    @Override
    public List<HWProjectComponent> removeDeepComponents(Predicate<HWProjectComponent> filter) {
        return _removeDeepComponents(null, filter);
    }

    @Override
    public Domain domain(HWConfigurationRun configuration) {
        return domain().eval(configuration);
    }

    @Override
    public List<Element3D> toElements3D(HWConfigurationRun configuration) {
        List<Element3D> list = new ArrayList<>();
        // element3D.putUserObject("HWProjectComponent", n);
//        list.add(domain.get());
        for (HWProjectComponent component : components) {
            list.addAll(component.toElements3D(configuration));
        }
        return list;
    }

    private List<HWProjectComponent> _removeDeepComponents(Object parent, Predicate<HWProjectComponent> filter) {
        List<HWProjectComponent> all = new ArrayList<>();
        if (parent == null) {
            for (HWProjectComponent component : components) {
                if (filter.test(component)) {
                    components.remove(component);
                    all.add(component);
                }
            }
            for (HWProjectComponent component : components) {
                if (component instanceof HWProjectComponentGroup) {
                    all.addAll(_removeDeepComponents(component, filter));
                }
            }
        } else if (parent instanceof HWProjectComponentGroup) {
            HWProjectComponentGroup g = (HWProjectComponentGroup) parent;
            for (HWProjectComponent component : g.children().values().toList()) {
                if (filter.test(component)) {
                    g.children().remove(component.name().get());
                    all.add(component);
                }
            }
            for (HWProjectComponent component : g.children().values()) {
                if (component instanceof HWProjectComponentGroup) {
                    all.addAll(_removeDeepComponents(component, filter));
                }
            }
        }
        return all;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofObjectBuilder("scene")
                .add("domain", domain == null ? null : domain.toTsonElement(context))
                .add("components",
                        Tson.ofArrayBuilder().addAll(components.stream().map(x -> x.toTsonElement(context)).collect(Collectors.toList()))
                )
                .build();
    }

}
