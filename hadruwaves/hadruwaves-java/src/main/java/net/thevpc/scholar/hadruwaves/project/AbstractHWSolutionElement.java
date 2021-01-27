package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.PValue;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritablePValue;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgoType;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPatternType;

public abstract class AbstractHWSolutionElement implements HWSolutionElement {
    static {
        UnitType.createEnum(CircuitType.class,"MoM Solver");
        UnitType.createEnum(GpPatternType.class,"MoM Solver");
        UnitType.createEnum(MeshAlgoType.class,"MoM Solver");
        UnitType.createEnum(TestFunctionsSymmetry.class,"MoM Solver");
        UnitType.createEnum(Axis.class,"Geometry");
        UnitType.createEnum(Boundary.class,"Boundary");
    }

    private WritablePValue<String> name = Props.of("name").valueOf(String.class, null);
    private WritablePValue<String> description = Props.of("description").valueOf(String.class, null);


    protected WritablePValue<String> parentPath = Props.of("parentPath").valueOf(String.class, null);
    protected WritablePValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class, null);
    protected WritablePValue<HWSolutionFolder> parent = Props.of("parent").valueOf(HWSolutionFolder.class, null);

    @Override
    public WritablePValue<String> name() {
        return name;
    }

    @Override
    public WritablePValue<String> description() {
        return description;
    }

    public WritablePValue<String> parentPath() {
        return parentPath;
    }

    public PValue<HWSolution> solution() {
        return solution.readOnly();
    }

    public PValue<HWSolutionFolder> parent() {
        return parent.readOnly();
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }
}
