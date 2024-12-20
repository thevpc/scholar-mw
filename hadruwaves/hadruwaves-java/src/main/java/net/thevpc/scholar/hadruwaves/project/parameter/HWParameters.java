package net.thevpc.scholar.hadruwaves.project.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;
import net.thevpc.common.props.*;
import net.thevpc.common.props.impl.DefaultPropertyListeners;
import net.thevpc.common.props.impl.PropertyBase;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.tson.TsonSerializable;
import net.thevpc.scholar.hadrumaths.units.ParamUnit;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class HWParameters extends PropertyBase implements TsonSerializable {

    protected WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private HWParameterFolderHelper childrenHelper = new HWParameterFolderHelper(null, () -> project().get());
    private DefaultPropertyListeners listeners = new DefaultPropertyListeners(this);

    public HWParameters(HWProject project) {
        super("parameters");
        this.project.set(project);
        listeners.addDelegate(childrenHelper.children());
    }

    public ObservableValue<HWProject> project() {
        return project;
    }

    @Override
    public PropertyListeners events() {
        return listeners;
    }

    public WritableLiMap<String, HWParameterElement> children() {
        return childrenHelper.children();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofArray("Parameters")
                .addAll(children().values().stream().map(x->x.toTsonElement(context)).collect(Collectors.toList()))
                .build();
    }

    public HWParameterElement findElement(String path) {
        return childrenHelper.findElement(path);
    }

    public HWParameterElement findElement(String path, boolean createFolder) {
        return childrenHelper.findElement(path, createFolder);
    }

    public HWParameterElement removeValue(String name) {
        return childrenHelper.removeValue(name);
    }

    public HWParameterElement remove(String path) {
        return childrenHelper.remove(path);
    }

    public void addAt(String path, HWParameterElement element) {
        childrenHelper.addAt(path, element);
    }

    public void addAt(String path, String name, String description, UnitType type, ParamUnit unit, String expression, boolean discriminator) {
        HWParameterValue e = new HWParameterValue(name, type, unit);
        e.discriminator().set(discriminator);
        e.description().set(description);
        childrenHelper.addAt(path, e);
        for (HWConfigurationRun r : project().get().configurations().findRuns()) {
            r.parameters().put(name, expression);
        }
    }

    public HWParameterValue getParameneter(String name) {
        return childrenHelper.getValue(name);
    }

//    @Override
    public Map<String, HWParameterValue> toMap() {
        Map<String, HWParameterValue> m = new HashMap<>();
        Stack<Object> stack = new Stack<>();
        stack.push(this);
        while (!stack.isEmpty()) {
            Object a = stack.pop();
            if (a instanceof HWParameters) {
                for (HWParameterElement child : ((HWParameters) a).children().values()) {
                    HWParameterElement c = child;
                    if (c instanceof HWParameterValue) {
                        m.put(c.name().get(), (HWParameterValue) c);
                    } else {
                        stack.push(child);
                    }
                }
            } else if (a instanceof HWParameterFolder) {
                for (MapEntry<String, HWParameterElement> c : ((HWParameterFolder) a).children()) {
                    stack.push(c.getValue());
                }
            } else {
                HWParameterValue c = (HWParameterValue) a;
                m.put(c.name().get(), c);
            }
        }
        return m;
    }
}
