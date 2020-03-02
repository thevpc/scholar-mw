package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePList;
import net.vpc.common.prpbind.WritablePIndexedNode;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;

public class DefaultHWScene implements HWScene {
    private WritablePList<StructureNode> components = Props.of("components").listOf(StructureNode.class);
    private WritablePIndexedNode<StructureNode> nodes = Props.of("root").inodeOf(StructureNode.class, null);
    private DomainTemplate domain = new DomainTemplate();

    public DefaultHWScene() {
        components.listeners().add(x -> {
            if (x.getNewValue() instanceof StructureNodeGroup) {
                for (StructureNode child : ((StructureNodeGroup) x).children()) {
                    WritablePList<StructureNode> components = (WritablePList<StructureNode>) x.getProperty();
                    components.remove(child);
                }
            }
        });
    }

    @Override
    public DomainTemplate domain() {
        return domain;
    }

    @Override
    public WritablePList<StructureNode> components() {
        return components;
    }

    @Override
    public List<StructureNode> findDeepComponents(Predicate<StructureNode> filter) {
        Stack<StructureNode> stack = new Stack<>();
        List<StructureNode> all = new ArrayList<>();
        for (StructureNode component : components) {
            if (filter.test(component)) {
                all.add(component);
            }
            if (component instanceof StructureNodeGroup) {
                for (StructureNode child : ((StructureNodeGroup) component).children()) {
                    stack.push(child);
                }
            }
        }
        while (!stack.isEmpty()) {
            StructureNode component=stack.pop();
            if (filter.test(component)) {
                all.add(component);
            }
            if (component instanceof StructureNodeGroup) {
                for (StructureNode child : ((StructureNodeGroup) component).children()) {
                    stack.push(child);
                }
            }
        }
        return all;
    }

    @Override
    public Domain domain(HWProjectEnv env) {
        return domain().eval(env);
    }

    @Override
    public List<Element3D> toElements3D(HWProjectEnv env) {
        List<Element3D> list = new ArrayList<>();
        // element3D.putUserObject("StructureNode", n);
//        list.add(domain.get());
        for (StructureNode component : components) {
            list.addAll(component.toElements3D(env));
        }
        return list;
    }
}
