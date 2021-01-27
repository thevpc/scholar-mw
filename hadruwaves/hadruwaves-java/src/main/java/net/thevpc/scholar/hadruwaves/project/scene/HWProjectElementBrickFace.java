package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.common.props.PValue;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwaves.project.HWProjectElement;
import net.thevpc.scholar.hadruwaves.project.HWSolution;
import net.thevpc.scholar.hadruwaves.project.Props2;
import net.thevpc.scholar.hadruwaves.props.WritablePExpression;

public class HWProjectElementBrickFace implements HWProjectElement {

    protected final WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected final WritablePValue<HWProject> project = Props.of("project").valueOf(HWProject.class, null);
    private final WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private HWProjectBrick.Face index;
    private final WritablePExpression<Boundary> boundary = Props2.of("boundary").exprEnumOf(Boundary.class, Boundary.NOTHING);
    private final WritablePValue<Boolean> visible = Props.of("visible").valueOf(Boolean.class, true);
    private HWProjectBrick parent;

    public HWProjectElementBrickFace(String boundary, HWProjectBrick.Face index, String name, HWProjectBrick parent) {
        this.boundary.set(boundary);
        this.parent = parent;
        this.name.set(name);
        this.project.set(parent.project().get());
        this.solution.set(parent.solution().get());

        this.index = index;
    }

    @Override
    public PValue<HWSolution> solution() {
        return solution;
    }

    @Override
    public PValue<HWProject> project() {
        return project;
    }

    @Override
    public PValue<String> name() {
        return name;
    }

    public HWProjectBrick.Face index() {
        return index;
    }

    public WritablePExpression<Boundary> boundary() {
        return boundary;
    }

    public WritablePValue<Boolean> visible() {
        return visible;
    }

    public HWProjectBrick parent() {
        return parent;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(index.name())
                .add("name",name.get())
                .add("boundary",boundary.get())
                .add("visible",visible.get())
                .build();
    }
    

}
