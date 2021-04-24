package net.thevpc.scholar.hadruwaves.project;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.thevpc.common.props.*;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.thevpc.scholar.hadruwaves.project.scene.Annotation3D;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;
import net.thevpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;

public abstract class AbstractHWProjectComponent implements HWProjectComponent {

    protected final WritableValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected final WritableValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private final WritableValue<String> name = Props.of("name").valueOf(String.class, null);

    protected final WritableValue<HWProjectComponentGroup> parent = Props.of("parent").valueOf(HWProjectComponentGroup.class, null);
    protected final WritableValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);

    private final WritablePExpression<Boolean> enabled = Props2.of("enabled").exprBooleanOf(true);
    private final WritableValue<Boolean> visible = Props.of("visible").valueOf(Boolean.class, true);
    private final WritableValue<Boolean> selected = Props.of("selected").valueOf(Boolean.class, true);
    private final WritableList<Annotation3D> annotations = Props.of("annotations").listOf(Annotation3D.class);
    private final WritableValue<String> description = Props.of("description").valueOf(String.class, null);

    @Override
    public ObservableValue<String> parentPath() {
        return parentPath;
    }

    @Override
    public ObservableValue<HWSolution> solution() {
        return solution.readOnly();
    }

    @Override
    public ObservableValue<HWProject> project() {
        return project.readOnly();
    }

    @Override
    public ObservableValue<HWProjectComponentGroup> parent() {
        return parent.readOnly();
    }

    public WritableValue<String> name() {
        return name;
    }

    public WritableValue<String> description() {
        return description;
    }

    @Override
    public WritableValue<Boolean> selected() {
        return selected;
    }

    @Override
    public WritablePExpression<Boolean> enabled() {
        return enabled;
    }

    @Override
    public WritableValue<Boolean> visible() {
        return visible;
    }

    public WritableList<Annotation3D> annotations() {
        return annotations;
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }

    @Override
    public List<Element3D> toElements3D(HWConfigurationRun configuration) {
        if (visible().get()) {
            if (configuration.evalBoolean(enabled().get(), true)) {
                return toElements3DImpl(configuration);
            }
        }
        return Collections.emptyList();
    }

    protected abstract List<Element3D> toElements3DImpl(HWConfigurationRun configuration);

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName())
                .add("name", name.get())
                .add("description", description.get())
                .add("enabled", enabled.get())
                .add("visible", visible.get())
                .add("annotations",
                        Tson.array().addAll(
                                annotations().stream().map(x -> x.toTsonElement(context)).collect(
                                        Collectors.toList()
                                ))
                )
                .build();
    }

}