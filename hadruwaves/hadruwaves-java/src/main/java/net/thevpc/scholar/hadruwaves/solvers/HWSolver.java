package net.thevpc.scholar.hadruwaves.solvers;

import net.thevpc.scholar.hadruwaves.SolverBuildResult;
import net.thevpc.scholar.hadruwaves.builders.CapacityBuilder;
import net.thevpc.scholar.hadruwaves.builders.CurrentBuilder;
import net.thevpc.scholar.hadruwaves.builders.DirectivityBuilder;
import net.thevpc.scholar.hadruwaves.builders.ElectricFieldBuilder;
import net.thevpc.scholar.hadruwaves.builders.FarFieldBuilder;
import net.thevpc.scholar.hadruwaves.builders.InputImpedanceBuilder;
import net.thevpc.scholar.hadruwaves.builders.MagneticFieldBuilder;
import net.thevpc.scholar.hadruwaves.builders.PoyntingVectorBuilder;
import net.thevpc.scholar.hadruwaves.builders.SParametersBuilder;
import net.thevpc.scholar.hadruwaves.builders.SelfBuilder;
import net.thevpc.scholar.hadruwaves.builders.SourceBuilder;
import net.thevpc.scholar.hadruwaves.builders.TestFieldBuilder;

public interface HWSolver {

    SolverBuildResult build();

    CapacityBuilder capacity();

    CurrentBuilder current();

    DirectivityBuilder directivity();

    ElectricFieldBuilder electricField();

    FarFieldBuilder farField();

    InputImpedanceBuilder inputImpedance();

    void invalidate();

    MagneticFieldBuilder magneticField();

    PoyntingVectorBuilder poyntingVector();

    SelfBuilder self();

    SourceBuilder source();

    SParametersBuilder sparameters();

    TestFieldBuilder testField();

    SolverBuildResult buildResult();

}
