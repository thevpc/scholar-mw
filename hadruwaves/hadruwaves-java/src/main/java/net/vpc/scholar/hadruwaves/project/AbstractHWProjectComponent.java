package net.vpc.scholar.hadruwaves.project;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.vpc.common.props.*;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.scene.Annotation3D;
import net.vpc.scholar.hadruwaves.props.WritablePExpression;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectComponentGroup;

public abstract class AbstractHWProjectComponent implements HWProjectComponent {

    protected final WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected final WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private final WritablePValue<String> name = Props.of("name").valueOf(String.class, null);

    protected final WritablePValue<HWProjectComponentGroup> parent = Props.of("parent").valueOf(HWProjectComponentGroup.class, null);
    protected final WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);

    private final WritablePExpression<Boolean> enabled = Props2.of("enabled").exprBooleanOf(true);
    private final WritablePValue<Boolean> visible = Props.of("visible").valueOf(Boolean.class, true);
    private final WritablePValue<Boolean> selected = Props.of("selected").valueOf(Boolean.class, true);
    private final WritablePList<Annotation3D> annotations = Props.of("annotations").listOf(Annotation3D.class);
    private final WritablePValue<String> description = Props.of("description").valueOf(String.class, null);

    @Override
    public PValue<String> parentPath() {
        return parentPath;
    }

    @Override
    public PValue<HWSolution> solution() {
        return solution.readOnly();
    }

    @Override
    public PValue<HWProject> project() {
        return project.readOnly();
    }

    @Override
    public PValue<HWProjectComponentGroup> parent() {
        return parent.readOnly();
    }

    public WritablePValue<String> name() {
        return name;
    }

    public WritablePValue<String> description() {
        return description;
    }

    @Override
    public WritablePValue<Boolean> selected() {
        return selected;
    }

    @Override
    public WritablePExpression<Boolean> enabled() {
        return enabled;
    }

    @Override
    public WritablePValue<Boolean> visible() {
        return visible;
    }

    public WritablePList<Annotation3D> annotations() {
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
