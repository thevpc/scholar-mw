package net.vpc.scholar.hadruwaves.mom;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadruplot.console.params.*;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadruplot.PlotType;
import net.vpc.scholar.hadruplot.console.ConsoleActionParams;
import net.vpc.scholar.hadruplot.console.ConsoleAwareObject;
import net.vpc.scholar.hadruplot.PlotMatrix;
import net.vpc.scholar.hadruplot.console.yaxis.PlotAxis;
import net.vpc.scholar.hadruplot.console.yaxis.YType;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.ModeType;
import net.vpc.scholar.hadruplot.PlotEvaluator;
import net.vpc.scholar.hadruwaves.console.params.FreqParam;
import net.vpc.scholar.hadruwaves.console.params.OmegaParam;
import net.vpc.scholar.hadruplot.PlotConstantMatrix;
import net.vpc.scholar.hadruwaves.console.plot.PlotConstantVDiscrete;
import net.vpc.scholar.hadruplot.PlotValueMatrix;
import net.vpc.scholar.hadruwaves.console.plot.PlotValueVDiscrete;
import net.vpc.scholar.hadruwaves.console.yaxis.*;
import net.vpc.scholar.hadruwaves.mom.console.params.*;
import net.vpc.scholar.hadruwaves.mom.console.params.hints.*;
import net.vpc.scholar.hadruwaves.mom.console.yaxis.*;
import net.vpc.scholar.hadruwaves.mom.sources.Source;
import net.vpc.scholar.hadruwaves.mom.sources.Sources;

/**
 * Created by vpc on 3/15/15.
 */
public class MomParamFactory extends AbstractFactory {
    public static class axis {
        public static PlotAxis matrixACond(YType... types) {
            return new PlotMatrixACond(types);
        }

        public static PlotAxis matrixA(PlotType plotType, YType... types) {
            return new PlotMatrixA(types, plotType);
        }

        public static PlotAxis matrixA(YType... types) {
            return new PlotMatrixA(types);
        }

        public static PlotAxis testTestScalarProducts(YType... types) {
            return new PlotTestTestScalarProducts(types);
        }

        public static PlotAxis testModesScalarProducts(YType... types) {
            return new PlotTestModeFunctionsScalarProducts(types);
        }

        public static PlotAxis testSourceScalarProducts(YType... types) {
            return new PlotTestSrcScalarProducts(types);
        }

        public static PlotAxis modesModesScalarProducts(YType... types) {
            return new PlotModeFunctionsScalarProducts(types);
        }

        public static PlotAxis matrixB(YType... types) {
            return new PlotMatrixB(types);
        }

        public static PlotAxis matrixB(PlotType plotType, YType... types) {
            return new PlotMatrixB(types, plotType);
        }

        public static PlotAxis zin(YType... types) {
            return new PlotZin(types);
        }

        public static PlotAxis s(YType... types) {
            return new PlotSParameters(types);
        }

        public static PlotAxis modeFunctions(YType... types) {
            return new PlotModeFunctions(types);
        }

        public static PlotAxis poyntingVector3D(YType... types) {
            return new PlotPoyntingVector3D(types);
        }

        public static PlotAxis electricField3D(YType... types) {
            return new PlotElectricField3D(types);
        }

        public static PlotAxis current3D(YType... types) {
            return new PlotCurrent3D(types);
        }

        public static PlotAxis testField2D(Axis axis, YType... types) {
            return new PlotTestField2D(axis, types);
        }

        public static PlotAxis vdiscrete(String name, VDiscrete value) {
            return new PlotConstantVDiscrete(name, value);
        }

        public static PlotAxis discrete(String name, CDiscrete value) {
            return new PlotConstantVDiscrete(name, new VDiscrete(value));
        }

        public static PlotAxis vdiscrete(String name, PlotEvaluator<VDiscrete> value) {
            return new PlotValueVDiscrete(name, value);
        }

        public static PlotAxis discrete(String name, final PlotEvaluator<CDiscrete> value) {
            return new PlotValueVDiscrete(name, new PlotEvaluator<VDiscrete>() {
                @Override
                public VDiscrete evalValue(ConsoleAwareObject source, ProgressMonitor monitor, ConsoleActionParams p) {
                    return new VDiscrete(value.evalValue(source, monitor, p));
                }
            });
        }

        public static PlotAxis namedMatrix(String name, PlotMatrix value, PlotType plotType) {
            return new PlotConstantMatrix(name, value, plotType);
        }

        public static PlotAxis namedMatrix(String name, PlotEvaluator<PlotMatrix> value, PlotType plotType) {
            return new PlotValueMatrix(name, value, plotType);
        }

        public static PlotAxis matrix(String name, ComplexMatrix value, PlotType plotType) {
            return new PlotConstantMatrix(name, new PlotMatrix(value.getArray()), plotType);
        }

        public static PlotAxis matrix(String name, final PlotEvaluator<ComplexMatrix> value, PlotType plotType) {
            return new PlotValueMatrix(name, new PlotEvaluator<PlotMatrix>() {
                @Override
                public PlotMatrix evalValue(ConsoleAwareObject source, ProgressMonitor monitor, ConsoleActionParams p) {
                    return new PlotMatrix(value.evalValue(source, monitor, p).getArray());
                }
            }, plotType);
        }

        public static PlotAxis vector(String name, ComplexVector value, PlotType plotType) {
            return new PlotConstantMatrix(name, new PlotMatrix(value.toMatrix().getArray()), plotType);
        }

        public static PlotAxis vector(String name, ComplexVector value) {
            if (value.isColumn()) {
                value = value.transpose();
            }
            return new PlotConstantMatrix(name, new PlotMatrix(value.toMatrix().getArray()), PlotType.CURVE);
        }

        public static PlotAxis vector(String name, final PlotEvaluator<ComplexVector> value, PlotType plotType) {
            return new PlotValueMatrix(name, new PlotEvaluator<PlotMatrix>() {
                @Override
                public PlotMatrix evalValue(ConsoleAwareObject source, ProgressMonitor monitor, ConsoleActionParams p) {
                    ComplexVector complexVector = value.evalValue(source, monitor, p);
                    if (complexVector.isColumn()) {
                        complexVector = complexVector.transpose();
                    }
                    return new PlotMatrix(complexVector.toMatrix().getArray());
                }
            }, plotType);
        }

        public static PlotAxis testField3D(Axis axis, YType... types) {
            return new PlotTestField3D(types);
        }

        public static PlotAxis capacity(YType... types) {
            return new PlotCapacity(types);
        }

        public static PlotAxis current2D(Axis axis, YType... types) {
            return new PlotCurrent2D(axis, types);
        }

        public static PlotAxis current2D(Axis axis, double epsilon, int threshold, int fnstep, YType... types) {
            return new PlotCurrent2D(epsilon, threshold, fnstep, axis, types);
        }

        public static PlotAxis structureDefinition(YType... types) {
            return new PlotStructureDefinition(types);
        }

        public static PlotAxis structure(YType... types) {
            if (types.length == 0) {
                return new PlotStructure();
            }
            return new PlotStructure(types[0]);
        }

        public static PlotAxis testFunctions(YType... types) {
            return new PlotTestFunctions(types);
        }
    }

    public static class params {
        public static CParam modesCount() {
            return new ModesCountParam();
        }

        public static CParam modeFunctions() {
            return new ModeFunctionsParam();
        }

        public static CParam frequency() {
            return new FreqParam();
        }

        public static CParam source() {
            return new SourceParam();
        }

        public static CParam sources() {
            return new SourcesParam();
        }

        public static CParam circuitType() {
            return new CircuitTypeParam();
        }

//        public static CParam widthFactor() {
//            return new WidthFactorParam();
//        }

        public static CParam width() {
            return new BoxWithParam();
        }

        public static CParam height() {
            return new BoxHeightParam();
        }

        public static CParam frequencyByWidthFactor() {
            return new FrequencyByWidthFactorParam();
        }

//        public static CParam xminFactor() {
//            return new BoxXminFactorParam();
//        }

        public static CParam xmin() {
            return new BoxXminParam();
        }

        public static CParam ymin() {
            return new BoxYminParam();
        }

        public static CParam omega() {
            return new OmegaParam();
        }


        public static CParam fractalScale() {
            return new FractalScaleParam();
        }

        public static CParam testFunctions() {
            return new TestFunctionsParam();
        }


        public static CParam hintAMatrixSparsify() {
            return new HintAMatrixSparsifyParam();
        }

        public static CParam hintBMatrixSparsify() {
            return new HintBMatrixSparsifyParam();
        }

        public static CParam hintDiscardFnByScalarProduct() {
            return new HintDiscardFnByScalarProductParam();
        }

        public static CParam hintFnMode() {
            return new HintFnModeParam();
        }

        public static CParam hintGpFnAxis() {
            return new HintGpFnAxisParam();
        }

        public static CParam hintRegularZnOperator() {
            return new HintRegularZnOperatorParam();
        }

        public static CParam hintSubModelEquivalent() {
            return new HintSubModelEquivalentParam();
        }

        public static CParam gridPrecision() {
            return new GridPrecisionParam();
        }
    }

    public static class paramsets {
//        public static DoubleArrayParamSet dtimes(CParam param, double min, double max, int times) {
//            return new DoubleArrayParamSet(param, min, max, times);
//        }
//
//        public static DoubleArrayParamSet dsteps(CParam param, double min, double max, double step) {
//            return new DoubleArrayParamSet(param, min, max, step);
//        }
//
//        public static IntArrayParamSet itimes(CParam param, int min, int max, int times) {
//            return new IntArrayParamSet(param, min, max, times);
//        }
//
//        public static IntArrayParamSet isteps(CParam param, int min, int max, int step) {
//            return new IntArrayParamSet(param, min, max, (double) step);
//        }
//
//        public static FloatArrayParamSet ftimes(CParam param, int min, int max, int times) {
//            return new FloatArrayParamSet(param, min, max, times);
//        }
//
//        public static FloatArrayParamSet fsteps(CParam param, int min, int max, int step) {
//            return new FloatArrayParamSet(param, min, max, (float) step);
//        }
//
//        public static LongArrayParamSet ltimes(CParam param, int min, int max, int times) {
//            return new LongArrayParamSet(param, min, max, times);
//        }
//
//        public static LongArrayParamSet lsteps(CParam param, int min, int max, long step) {
//            return new LongArrayParamSet(param, min, max, step);
//        }
//
//        public static DoubleArrayParamSet doubleParamSet(CParam param) {
//            return new DoubleArrayParamSet(param);
//        }
//
//        public static DoubleArrayParamSet doubleParamSet(CParam param, double[] values) {
//            return new DoubleArrayParamSet(param, values);
//        }
//
//        public static FloatArrayParamSet floatParamSet(CParam param, float[] values) {
//            return new FloatArrayParamSet(param, values);
//        }
//
//        public static FloatArrayParamSet floatParamSet(CParam param) {
//            return new FloatArrayParamSet(param);
//        }
//
//        public static LongArrayParamSet longParamSet(CParam param, long[] values) {
//            return new LongArrayParamSet(param, values);
//        }
//
//        public static LongArrayParamSet longParamSet(CParam param) {
//            return new LongArrayParamSet(param);
//        }
//
//        public static <T> ArrayParamSet<T> objectParamSet(CParam param, T[] values) {
//            return new ArrayParamSet<T>(param, values);
//        }
//
//        public static <T> ArrayParamSet<T> objectParamSet(CParam param) {
//            return new ArrayParamSet<T>(param);
//        }
//
//        public static IntArrayParamSet intParamSet(CParam param, int[] values) {
//            return new IntArrayParamSet(param, values);
//        }
//
//        public static IntArrayParamSet intParamSet(CParam param) {
//            return new IntArrayParamSet(param);
//        }
//
//        public static BooleanArrayParamSet boolParamSet(CParam param, boolean[] values) {
//            return new BooleanArrayParamSet(param, values);
//        }
//
//        public static BooleanArrayParamSet boolParamSet(CParam param) {
//            return new BooleanArrayParamSet(param);
//        }


//        public static XParamSet x(int xsamples) {
//            return new XParamSet(xsamples);
//        }
//
//        public static XParamSet xy(int xsamples, int ysamples) {
//            return new XParamSet(xsamples, ysamples);
//        }
//
//        public static XParamSet xyz(int xsamples, int ysamples, int zsamples) {
//            return new XParamSet(xsamples, ysamples, zsamples);
//        }

        public static ArrayParamSet<TestFunctions> testFunctions() {
            return Maths.objectParamSet(params.testFunctions());
        }

        public static ArrayParamSet<TestFunctions> testFunctions(net.vpc.scholar.hadruwaves.mom.TestFunctions[] functions) {
            return Maths.paramSet(params.testFunctions(), functions);
        }

        public static ArrayParamSet<ModeFunctions> modeFunctions() {
            return Maths.objectParamSet(params.modeFunctions());
        }

        public static ArrayParamSet<ModeFunctions> modeFunctions(net.vpc.scholar.hadruwaves.mom.ModeFunctions[] functions) {
            return Maths.paramSet(params.modeFunctions(), functions);
        }

        public static ArrayParamSet<Source> sources(Source[] functions) {
            return Maths.paramSet(params.source(), functions);
        }

        public static ArrayParamSet<Sources> sources(Sources[] functions) {
            return Maths.paramSet(params.sources(), functions);
        }

//        public static TestFunctionsCountPlotParam testFunctionsCount() {
//            return new TestFunctionsCountPlotParam();
//        }

        public static IntArrayParamSet modeFunctionsCount() {
            return Maths.intParamSet(params.modesCount());
        }

        public static DoubleArrayParamSet frequency() {
            return Maths.doubleParamSet(params.frequency());
        }

        public static DoubleArrayParamSet width() {
            return Maths.doubleParamSet(params.width());
        }

        public static DoubleArrayParamSet height() {
            return Maths.doubleParamSet(params.height());
        }

        public static DoubleArrayParamSet xmin() {
            return Maths.doubleParamSet(params.xmin());
        }

//        public static DoubleArrayParamSet xminFactor() {
//            return Maths.doubleParamSet(params.xminFactor());
//        }

        public static DoubleArrayParamSet ymin() {
            return Maths.doubleParamSet(params.ymin());
        }

//        public static DoubleArrayParamSet widthFactor() {
//            return Maths.doubleParamSet(params.widthFactor());
//        }

        public static DoubleArrayParamSet freqByWidthFactor() {
            return Maths.doubleParamSet(params.frequencyByWidthFactor());
        }

        public static DoubleArrayParamSet omega() {
            return Maths.doubleParamSet(params.omega());
        }

        public static ArrayParamSet<GridPrecision> gridPrecision() {
            return Maths.objectParamSet(params.gridPrecision());
        }


        public static FloatArrayParamSet hintAMatrixSparsify() {
            return Maths.floatParamSet(params.hintAMatrixSparsify());
        }

        public static FloatArrayParamSet hintBMatrixSparsify() {
            return Maths.floatParamSet(params.hintBMatrixSparsify());
        }

        public static FloatArrayParamSet hintDiscardFnByScalarProduct() {
            return Maths.floatParamSet(params.hintDiscardFnByScalarProduct());
        }

        public static ArrayParamSet<ModeType> hintFnMode() {
            return Maths.objectParamSet(params.hintFnMode());
        }

        public static ArrayParamSet<HintAxisType> hintGpFnAxis() {
            return Maths.objectParamSet(params.hintGpFnAxis());
        }

        public static BooleanArrayParamSet hintRegularZnOperator() {
            return Maths.boolParamSet(params.hintRegularZnOperator());
        }

        public static BooleanArrayParamSet hintSubModelEquivalent() {
            return Maths.boolParamSet(params.hintSubModelEquivalent());
        }


    }
}
