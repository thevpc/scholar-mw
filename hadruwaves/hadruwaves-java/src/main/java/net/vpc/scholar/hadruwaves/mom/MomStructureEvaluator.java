package net.vpc.scholar.hadruwaves.mom;

import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.HSerializable;
import net.vpc.scholar.hadruwaves.mom.str.*;
import net.vpc.scholar.hadruwaves.mom.str.momstr.*;
import net.vpc.scholar.hadruwaves.str.*;

import java.util.HashMap;
import java.util.Map;

public class MomStructureEvaluator implements HSerializable {
    private MomStructure str;

    private ElectricFieldEvaluator electricFieldEvaluator;
    private FarFieldEvaluator farFieldEvaluator;
    private ElectricFieldFundamentalEvaluator electricFieldFundamentalEvaluator;
    private CurrentEvaluator currentEvaluator;
    private TestFieldEvaluator testFieldEvaluator;
    private PoyntingVectorEvaluator poyntingVectorEvaluator;
    private MagneticFieldEvaluator magneticFieldEvaluator;
    private MatrixAEvaluator matrixAEvaluator;
    private MatrixBEvaluator matrixBEvaluator;
    private SourceEvaluator sourceEvaluator;
    private ZinEvaluator zinEvaluator;
    private MatrixUnknownEvaluator matrixUnknownEvaluator;

    public MomStructureEvaluator(MomStructure str) {
        this.str = str;
    }

    public MagneticFieldEvaluator createMagneticFieldEvaluator() {
        MagneticFieldEvaluator builder = magneticFieldEvaluator;
        if (builder == null) {
            builder = DefaultMagneticFieldEvaluator.INSTANCE;
        }
        return builder;
    }

    public PoyntingVectorEvaluator createPoyntingVectorEvaluator() {
        PoyntingVectorEvaluator builder = poyntingVectorEvaluator;
        if (builder == null) {
            builder = DefaultPoyntingVectorEvaluator.INSTANCE;
        }
        return builder;
    }


    // Cube[z][x][y]
    public ElectricFieldEvaluator createElectricFieldEvaluator() {
        ElectricFieldEvaluator builder = electricFieldEvaluator;
        if (builder == null) {
            switch (str.getCircuitType()) {
                case SERIAL: {
                    builder = ElectricFieldSerialEvaluator.INSTANCE;
                    break;
                }
                case PARALLEL: {
                    builder = ElectricFieldParallelEvaluator.INSTANCE;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown circuit type " + str.getCircuitType());
                }
            }
        }
        return builder;
    }

    public FarFieldEvaluator createFarFieldEvaluator() {
        FarFieldEvaluator builder = farFieldEvaluator;
        if (builder == null) {
            //
            builder = FarFieldEvaluatorPEC.INSTANCE;
        }
        return builder;
    }

    public ElectricFieldFundamentalEvaluator createElectricFieldFundamentalEvaluator() {

        return electricFieldFundamentalEvaluator == null ? ElectricFieldFundamentalSerialParallelEvaluator.INSTANCE : electricFieldFundamentalEvaluator;
    }

    public CurrentEvaluator createCurrentEvaluator() {
        CurrentEvaluator builder = currentEvaluator;
        if (builder == null) {
            switch (str.getCircuitType()) {
                case SERIAL: {
                    builder = CurrentSerialEvaluator.INSTANCE;
                    break;
                }
                case PARALLEL: {
                    builder = CurrentParallelEvaluator.INSTANCE;
                    break;
                }
                default: {
                    throw new IllegalArgumentException("Unknown circuit type " + str.getCircuitType());
                }
            }
        }
        return builder;
    }

    public TestFieldEvaluator createTestFieldEvaluator() {
        TestFieldEvaluator builder = testFieldEvaluator;
        if (builder == null) {
            builder = TestFieldSerialParallelEvaluator.INSTANCE;
        }
        return builder;
    }

    /**
     * A[p,n]=&lt;Gp,Fn&gt; ; p in [0,MAX_ESSAIS-1] ; n in [0,MAX_SOURCES-1]
     */
    //A[p,n]=<Gp,Fn> ; p in [0,MAX_ESSAIS-1] ; n in [0,MAX_SOURCES-1]
    public MatrixBEvaluator createMatrixBEvaluator() {
        if (matrixBEvaluator != null) {
            return matrixBEvaluator;
        }
        switch (str.getProjectType()) {
            case WAVE_GUIDE: {
                return new MatrixBWaveguideSerialParallelEvaluator();
            }
            case PLANAR_STRUCTURE: {
                return new MatrixBPlanarSerialParallelEvaluator();
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

    public SourceEvaluator createSourceEvaluator() {
        if (sourceEvaluator != null) {
            return sourceEvaluator;
        }
        return new DefaultSourceEvaluator();
    }

    public MatrixAEvaluator createMatrixAEvaluator() {
        if (matrixAEvaluator != null) {
            return matrixAEvaluator;
        }
        switch (str.getProjectType()) {
            case WAVE_GUIDE: {
                switch (str.getCircuitType()) {
                    case SERIAL: {
                        return MatrixAWaveguideSerialEvaluator.INSTANCE;
                    }
                    case PARALLEL: {
                        return MatrixAWaveguideParallelEvaluator.INSTANCE;
                    }
                }
            }
            case PLANAR_STRUCTURE: {
                switch (str.getCircuitType()) {
                    case SERIAL: {
                        return MatrixAPlanarSerialEvaluator.INSTANCE;
                    }
                    case PARALLEL: {
                        return MatrixAPlanarParallelEvaluator.INSTANCE;
                    }
                }
            }
        }
        throw new IllegalArgumentException("Impossible");
    }

    public void setFrom(MomStructureEvaluator other){
        this.electricFieldEvaluator = other.electricFieldEvaluator;
        this.farFieldEvaluator = other.farFieldEvaluator;
        this.electricFieldFundamentalEvaluator = other.electricFieldFundamentalEvaluator;
        this.currentEvaluator = other.currentEvaluator;
        this.testFieldEvaluator = other.testFieldEvaluator;
        this.poyntingVectorEvaluator = other.poyntingVectorEvaluator;
        this.magneticFieldEvaluator = other.magneticFieldEvaluator;
        this.matrixAEvaluator = other.matrixAEvaluator;
        this.matrixBEvaluator = other.matrixBEvaluator;
        this.sourceEvaluator = other.sourceEvaluator;
        this.zinEvaluator = other.zinEvaluator;
        this.matrixUnknownEvaluator = other.matrixUnknownEvaluator;

    }

    //@Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        Map<String, TsonElement> builders = new HashMap<>();
        if (matrixAEvaluator != null) {
            builders.put("matrixA", context.elem(matrixAEvaluator));
        }
        if (matrixBEvaluator != null) {
            builders.put("matrixB", context.elem(matrixBEvaluator));
        }
        if (matrixUnknownEvaluator != null) {
            builders.put("matrixUnknown", context.elem(matrixUnknownEvaluator));
        }
        if (electricFieldEvaluator != null) {
            builders.put("electricField", context.elem(electricFieldEvaluator));
        }
        if (farFieldEvaluator != null) {
            builders.put("farField", context.elem(farFieldEvaluator));
        }
        if (electricFieldFundamentalEvaluator != null) {
            builders.put("electricFieldFundamental", context.elem(electricFieldFundamentalEvaluator));
        }
        if (currentEvaluator != null) {
            builders.put("current", context.elem(currentEvaluator));
        }
        if (testFieldEvaluator != null) {
            builders.put("testField", context.elem(testFieldEvaluator));
        }
        if (poyntingVectorEvaluator != null) {
            builders.put("poyntingVector", context.elem(poyntingVectorEvaluator));
        }
        if (magneticFieldEvaluator != null) {
            builders.put("magneticField", context.elem(magneticFieldEvaluator));
        }
        if (zinEvaluator != null) {
            builders.put("zin", context.elem(zinEvaluator));
        }
        return context.elem(builders);
    }

    public ZinEvaluator createZinEvaluator() {
        if (zinEvaluator != null) {
            return zinEvaluator;
        }
        switch (str.getCircuitType()) {
            case SERIAL: {
                return ZinSerialEvaluator.INSTANCE;
            }
            case PARALLEL: {
                return ZinParallelEvaluator.INSTANCE;
            }
        }
        return null;//never
    }

    public MatrixUnknownEvaluator createMatrixUnknownEvaluator() {
        if (matrixUnknownEvaluator != null) {
            return matrixUnknownEvaluator;
        }
        return DefaultMatrixUnknownEvaluator.INSTANCE;
    }

    public ElectricFieldEvaluator getElectricFieldEvaluator() {
        return electricFieldEvaluator;
    }

    public MomStructureEvaluator setElectricFieldEvaluator(ElectricFieldEvaluator electricFieldEvaluator) {
        this.electricFieldEvaluator = electricFieldEvaluator;
        return this;
    }

    public ElectricFieldFundamentalEvaluator getElectricFieldFundamentalEvaluator() {
        return electricFieldFundamentalEvaluator;
    }

    public MomStructureEvaluator setElectricFieldFundamentalEvaluator(ElectricFieldFundamentalEvaluator electricFieldFundamentalEvaluator) {
        this.electricFieldFundamentalEvaluator = electricFieldFundamentalEvaluator;
        return this;
    }

    public CurrentEvaluator getCurrentEvaluator() {
        return currentEvaluator;
    }

    public MomStructureEvaluator setCurrentEvaluator(CurrentEvaluator currentEvaluator) {
        this.currentEvaluator = currentEvaluator;
        return this;
    }

    public TestFieldEvaluator getTestFieldEvaluator() {
        return testFieldEvaluator;
    }

    public MomStructureEvaluator setTestFieldEvaluator(TestFieldEvaluator testFieldEvaluator) {
        this.testFieldEvaluator = testFieldEvaluator;
        return this;
    }

    public PoyntingVectorEvaluator getPoyntingVectorEvaluator() {
        return poyntingVectorEvaluator;
    }

    public MomStructureEvaluator setPoyntingVectorEvaluator(PoyntingVectorEvaluator poyntingVectorEvaluator) {
        this.poyntingVectorEvaluator = poyntingVectorEvaluator;
        return this;
    }

    public MagneticFieldEvaluator getMagneticFieldEvaluator() {
        return magneticFieldEvaluator;
    }

    public MomStructureEvaluator setMagneticFieldEvaluator(MagneticFieldEvaluator magneticFieldEvaluator) {
        this.magneticFieldEvaluator = magneticFieldEvaluator;
        return this;
    }

    public ZinEvaluator getZinEvaluator() {
        return zinEvaluator;
    }

    public MomStructureEvaluator setZinEvaluator(ZinEvaluator zinEvaluator) {
        this.zinEvaluator = zinEvaluator;
        return this;
    }

    public MatrixAEvaluator getMatrixAEvaluator() {
        return matrixAEvaluator;
    }

    public MomStructureEvaluator setMatrixAEvaluator(MatrixAEvaluator matrixAEvaluator) {
        this.matrixAEvaluator = matrixAEvaluator;
        return this;
    }

    public MatrixBEvaluator getMatrixBEvaluator() {
        return matrixBEvaluator;
    }

    public MomStructureEvaluator setMatrixBEvaluator(MatrixBEvaluator matrixBEvaluator) {
        this.matrixBEvaluator = matrixBEvaluator;
        return this;
    }

    public MatrixUnknownEvaluator getMatrixUnknownEvaluator() {
        return matrixUnknownEvaluator;
    }

    public MomStructureEvaluator setMatrixUnknownEvaluator(MatrixUnknownEvaluator matrixUnknownEvaluator) {
        this.matrixUnknownEvaluator = matrixUnknownEvaluator;
        return this;
    }


}
