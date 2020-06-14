package net.vpc.scholar.hadruwaves.solvers;

import net.vpc.scholar.hadruwaves.SolverBuildResult;
import net.vpc.scholar.hadruwaves.builders.CapacityBuilder;
import net.vpc.scholar.hadruwaves.builders.CurrentBuilder;
import net.vpc.scholar.hadruwaves.builders.DirectivityBuilder;
import net.vpc.scholar.hadruwaves.builders.ElectricFieldBuilder;
import net.vpc.scholar.hadruwaves.builders.FarFieldBuilder;
import net.vpc.scholar.hadruwaves.builders.InputImpedanceBuilder;
import net.vpc.scholar.hadruwaves.builders.MagneticFieldBuilder;
import net.vpc.scholar.hadruwaves.builders.PoyntingVectorBuilder;
import net.vpc.scholar.hadruwaves.builders.SParametersBuilder;
import net.vpc.scholar.hadruwaves.builders.SelfBuilder;
import net.vpc.scholar.hadruwaves.builders.SourceBuilder;
import net.vpc.scholar.hadruwaves.builders.TestFieldBuilder;

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
