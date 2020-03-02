package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.PList;
import net.vpc.common.prpbind.Props;
import net.vpc.common.prpbind.WritablePList;
import net.vpc.common.prpbind.WritablePValue;

public abstract class AbstractStructureNode implements StructureNode {
    protected WritablePList<StructureNode> children = Props.of("children").listOf(StructureNode.class);
    private WritablePValue<Boolean> enabled = Props.of("enabled").valueOf(Boolean.class, true);
    private WritablePList<Annotation3D> annotations = Props.of("annotations").listOf(Annotation3D.class);

    public WritablePValue<Boolean> enabled() {
        return enabled;
    }

    public PList<StructureNode> children() {
        return children;
    }

    public WritablePList<Annotation3D> annotations() {
        return annotations;
    }
}
