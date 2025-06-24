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
    private final WritableString name = Props.of("name").stringOf(null);

    protected final WritableValue<HWProjectComponentGroup> parent = Props.of("parent").valueOf(HWProjectComponentGroup.class, null);
    protected final WritableString parentPath = Props.of("parentPath").stringOf(null);

    private final WritablePExpression<Boolean> enabled = Props2.of("enabled").exprBooleanOf(true);
    private final WritableBoolean visible = Props.of("visible").booleanOf(true);
    private final WritableBoolean selected = Props.of("selected").booleanOf(true);
    private final WritableList<Annotation3D> annotations = Props.of("annotations").listOf(Annotation3D.class);
    private final WritableString description = Props.of("description").stringOf(null);

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

    public WritableString name() {
        return name;
    }

    public WritableString description() {
        return description;
    }

    @Override
    public WritableBoolean selected() {
        return selected;
    }

    @Override
    public WritablePExpression<Boolean> enabled() {
        return enabled;
    }

    @Override
    public WritableBoolean visible() {
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
        return Tson.ofObjectBuilder(getClass().getSimpleName())
                .add("name", name.get())
                .add("description", description.get())
                .add("enabled", enabled.get())
                .add("visible", visible.get())
                .add("annotations",
                        Tson.ofArrayBuilder().addAll(
                                annotations().stream().map(x -> x.toTsonElement(context)).collect(
                                        Collectors.toList()
                                ))
                )
                .build();
    }

}
