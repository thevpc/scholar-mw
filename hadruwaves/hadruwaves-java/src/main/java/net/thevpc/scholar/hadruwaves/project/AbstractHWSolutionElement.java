package net.thevpc.scholar.hadruwaves.project;

import net.thevpc.common.props.ObservableValue;
import net.thevpc.common.props.Props;
import net.thevpc.common.props.WritableString;
import net.thevpc.common.props.WritableValue;
import net.thevpc.common.props.impl.PropertyBase;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadrumaths.meshalgo.MeshAlgoType;
import net.thevpc.scholar.hadrumaths.units.UnitType;
import net.thevpc.scholar.hadruwaves.Boundary;
import net.thevpc.scholar.hadruwaves.mom.CircuitType;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.GpPatternType;

public abstract class AbstractHWSolutionElement extends PropertyBase implements HWSolutionElement {
    static {
        UnitType.createEnum(CircuitType.class,"MoM Solver");
        UnitType.createEnum(GpPatternType.class,"MoM Solver");
        UnitType.createEnum(MeshAlgoType.class,"MoM Solver");
        UnitType.createEnum(TestFunctionsSymmetry.class,"MoM Solver");
        UnitType.createEnum(Axis.class,"Geometry");
        UnitType.createEnum(Boundary.class,"Boundary");
    }

    private WritableString name = Props.of("name").stringOf( null);
    private WritableString description = Props.of("description").stringOf( null);


    protected WritableString parentPath = Props.of("parentPath").stringOf( null);
    protected WritableValue<HWSolution> solution = Props.of("solution").valueOf(HWSolution.class);
    protected WritableValue<HWSolutionFolder> parent = Props.of("parent").valueOf(HWSolutionFolder.class);

    @Override
    public WritableString name() {
        return name;
    }

    @Override
    public WritableString description() {
        return description;
    }

    public WritableString parentPath() {
        return parentPath;
    }

    public ObservableValue<HWSolution> solution() {
        return solution.readOnly();
    }

    public ObservableValue<HWSolutionFolder> parent() {
        return parent.readOnly();
    }

    @Override
    public String toString() {
        return String.valueOf(name.get());
    }
}
