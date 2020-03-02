package net.vpc.scholar.hadruwaves.project.scene;

import net.vpc.common.prpbind.WritablePList;

public interface StructureNodeGroup extends StructureNode{
    WritablePList<StructureNode> children();
}
