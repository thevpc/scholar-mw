package net.vpc.scholar.hadruwaves.project;

import net.vpc.common.props.*;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.scene.DomainTemplate;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectScene;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.plot.d3.BoundDomain;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;

public class DefaultHWcene implements HWProjectScene {

    private WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private WritablePList<HWProjectComponent> components = Props.of("components").listOf(HWProjectComponent.class);
    private DomainTemplate domain = new DomainTemplate();

    public DefaultHWcene(HWProject project) {
        this.project.set(project);
        components.listeners().add(x -> {
            AbstractHWProjectComponent e = x.getNewValue();
            if (e != null) {
                e.project.set(project);
            }
            if (x.getNewValue() instanceof HWProjectComponentGroup) {
                for (HWProjectComponent child : ((HWProjectComponentGroup) x).children().values().toList()) {
                    WritablePList<HWProjectComponent> components = x.getProperty();
                    components.remove(child);
                }
            }
        });
    }

    @Override
    public PValue<HWProject> project() {
        return project.readOnly();
    }

    @Override
    public PValue<HWSolution> solution() {
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
    public WritablePList<HWProjectComponent> components() {
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
        return Tson.obj("scene")
                .add("domain", domain == null ? null : domain.toTsonElement(context))
                .add("components",
                        Tson.array().addAll(components.stream().map(x -> x.toTsonElement(context)).collect(Collectors.toList()))
                )
                .build();
    }

}
