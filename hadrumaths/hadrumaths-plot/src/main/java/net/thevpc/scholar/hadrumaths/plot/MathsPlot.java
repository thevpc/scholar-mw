/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.plot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.thevpc.common.mvn.PomId;
import net.thevpc.common.mvn.PomIdResolver;
import net.thevpc.scholar.hadruplot.*;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.tson.TsonSerializer;
import net.thevpc.tson.TsonSerializerBuilder;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceConfig;
import net.thevpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.thevpc.scholar.hadrumaths.plot.filetypes.PlotFileTypeJObj;
import net.thevpc.scholar.hadrumaths.plot.filetypes.PlotFileTypeBundle;
import net.thevpc.scholar.hadrumaths.plot.filetypes.PlotFileTypeCsv;
import net.thevpc.scholar.hadrumaths.plot.filetypes.PlotFileTypeJFig;
import net.thevpc.scholar.hadrumaths.plot.filetypes.PlotFileTypeMatlab;
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.thevpc.scholar.hadruplot.extension.ClassResolver;
import net.thevpc.scholar.hadruplot.extension.defaults.DefaultPlotModelFactory;
import net.thevpc.scholar.hadruplot.model.value.PlotValueTypeFactory;
import net.thevpc.scholar.hadruplot.filetypes.PlotFileTypeJpeg;
import net.thevpc.scholar.hadruplot.filetypes.PlotFileTypePng;
import net.thevpc.scholar.hadruplot.console.ConsoleParams;
import net.thevpc.scholar.hadruplot.console.PlotConfigManager;
import net.thevpc.scholar.hadruplot.console.params.CParam;
import net.thevpc.scholar.hadruplot.console.params.ParamSet;
import net.thevpc.scholar.hadruplot.util.SimpleDoubleFormat;

/**
 *
 * @author vpc
 */
public class MathsPlot {

    public static ColorPalette DEFAULT_PALETTE = ColorPalettes.DEFAULT_PALETTE;

    static {
        synchronized (PlotConfigManager.class) {
            ComplexAsDoubleValues.init();
            PlotConfigManager.Config = new MathsPlotConfig();
            PlotConfigManager.addPlotFileTypes(
                    PlotFileTypePng.INSTANCE,
                    PlotFileTypeMatlab.INSTANCE,
                    PlotFileTypeJFig.INSTANCE,
                    PlotFileTypeJObj.INSTANCE,
                    PlotFileTypeBundle.INSTANCE,
                    PlotFileTypeJpeg.INSTANCE,
                    PlotFileTypeCsv.INSTANCE
            );
            PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValueAxisVectorType());
            PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValueComplexType());
            PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValueExprType());
            PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValueCustomFunctionType());
            PlotConfigManager.getPlotValueTypeFactory().registerType(new PlotValuePointType());
            PlotConfigManager.getPlotValueTypeFactory().registerType(MathsPlotValueDoubleType.INSTANCE);
            PlotConfigManager.getPlotValueTypeFactory().registerConverter(new PlotValueTypeFactory.AbstractPlotValueTypeConverter("number", "complex") {
            });
            PlotConfigManager.getPlotValueTypeFactory().registerConverter(new PlotValueTypeFactory.AbstractPlotValueTypeConverter("complex", "expr") {
            });

            PlotConfigManager.addPlotCacheSupport(new MathsPlotConsoleCacheSupport());
            PlotConfigManager.addPlotModelPopupFactory(new ValuesPlotModelPopupFactory());
            PlotConfigManager.addPlotModelPanelFactory(new ExpressionsPlotModelPanelFactory());
            PlotConfigManager.addPlotBuilderSupport(new MathsPlotBuilderSupport());
            PlotConfigManager.addPlotValueFactory(MathsPlotValueFactory.INSTANCE);
            List<PlotModelFactory> old = new ArrayList<>();
            old.add(MathsPlotModelFactory.INSTANCE);
            old.add(DefaultPlotModelFactory.INSTANCE);
            old.addAll(Arrays.asList(PlotConfigManager.getPlotModelFactories()));
            PlotConfigManager.setPlotModelFactories(old.toArray(new PlotModelFactory[0]));

            TsonSerializer ser = Tson.serializer();
            TsonSerializerBuilder b = null;
            if (ser == null) {
                b = Tson.serializer()
                        .builder();
            } else {
                b = ser.builder();
            }
            Tson.setSerializer(
                    b
                            .setSerializer(SimpleDoubleFormat.class, (SimpleDoubleFormat object, TsonObjectContext context) -> Tson.ofFunction(object.getClass().getSimpleName()))
                            .setSerializer(PlotBuilder.ListDoubleFormat.class, (PlotBuilder.ListDoubleFormat object, TsonObjectContext context) -> Tson.ofFunction(object.getClass().getSimpleName(), context.elem(object.getValues())))
                            .build()
            );

            PlotConfigManager.getPlotHyperCubeResolvers()
                    .add(new ClassResolver<PlotHyperCube>() {
                        @Override
                        public PlotHyperCube resolve(Object o) {
                            if (o instanceof VDiscrete) {
                                VDiscrete d = (VDiscrete) o;
                                return new PlotHyperCubeFromVDiscrete(d);
                            }
                            return null;
                        }

                    }, VDiscrete.class);
            PlotConfigManager.getPlotDomainResolvers()
                    .add(new ClassResolver<PlotDomain>() {
                        @Override
                        public PlotDomain resolve(Object o) {
                            if (o instanceof Domain) {
                                Domain d = (Domain) o;
                                return new PlotDomainFromDomain(d);
                            }
                            return null;
                        }

                    }, Domain.class);
            PlotConfigManager.getPlotComplexResolvers()
                    .add(new ClassResolver<PlotComplex>() {
                        @Override
                        public PlotComplex resolve(Object o) {
                            if (o instanceof Complex) {
                                Complex d = (Complex) o;
                                return new PlotComplexFromComplex(d);
                            }
                            return null;
                        }

                    }, Complex.class);
            PlotConfigManager.getPlotComplexResolvers()
                    .add(new ClassResolver<PlotComplex>() {
                        @Override
                        public PlotComplex resolve(Object o) {
                            if (o instanceof Number) {
                                Number d = (Complex) o;
                                return new PlotComplexFromComplex(d);
                            }
                            return null;
                        }

                    }, Number.class);
            PlotConfigManager.getPlotSamplesResolvers().add(new ClassResolver<PlotSamples>() {
                @Override
                public PlotSamples resolve(Object o) {
                    if (o instanceof AdaptiveSamples) {
                        AdaptiveSamples d0 = (AdaptiveSamples) o;
                        AdaptivePlotSamples d = new AdaptivePlotSamples();
                        d.setError(d0.getError());
                        d.setMaximumXSamples(d0.getMaximumXSamples());
                        d.setMinimumXSamples(d0.getMinimumXSamples());
                        return d;
                    }
                    if (o instanceof AbsoluteSamples) {
                        AbsoluteSamples d0 = (AbsoluteSamples) o;
                        switch (d0.getDimension()) {
                            case 1:
                                return AbsolutePlotSamples.absolute(d0.getX());
                            case 2:
                                return AbsolutePlotSamples.absolute(d0.getX(), d0.getY());
                            case 3:
                                return AbsolutePlotSamples.absolute(d0.getX(), d0.getY(), d0.getZ());
                        }
                    }
                    if (o instanceof RelativeSamples) {
                        RelativeSamples d0 = (RelativeSamples) o;
                        if (d0.isRelative()) {
                            switch (d0.getDimension()) {
                                case 1:
                                    return RelativePlotSamples.relative(d0.getX());
                                case 2:
                                    return RelativePlotSamples.relative(d0.getX(), d0.getY());
                                case 3:
                                    return RelativePlotSamples.relative(d0.getX(), d0.getY(), d0.getZ());
                            }
                        } else {
                            switch (d0.getDimension()) {
                                case 1:
                                    return RelativePlotSamples.absolute(d0.getX());
                                case 2:
                                    return RelativePlotSamples.absolute(d0.getX(), d0.getY());
                                case 3:
                                    return RelativePlotSamples.absolute(d0.getX(), d0.getY(), d0.getZ());
                            }
                        }
                    }
                    return null;
                }

                ;
            }, Samples.class);
        }
    }

    private static class PlotHyperCubeFromVDiscrete implements PlotHyperCube {

        private final VDiscrete d;

        public PlotHyperCubeFromVDiscrete(VDiscrete d) {
            this.d = d;
        }

        @Override
        public int getCubesCount() {
            return d.getCubesCount();
        }

        @Override
        public PlotCube getCube(int index) {
            CDiscrete i = d.getComponentDiscrete(Axis.cartesian(index));
            return new PlotCube(
                    i.getX(), i.getY(), i.getZ(),
                    i.getValues()
            );
        }

        @Override
        public String getTitle() {
            return d.getTitle();
        }
    }

    public static PlotDoubleConverter resolveComplexAsDouble(Complex[][][] c) {
        if (c == null) {
            return PlotDoubleConverter.REAL;
        }

        for (Complex[][] d : c) {
            PlotDoubleConverter d2 = resolveComplexAsDouble(d);
            if (d2 == PlotDoubleConverter.ABS) {
                return d2;
            }
        }
        return PlotDoubleConverter.REAL;
    }

    public static PlotDoubleConverter resolveComplexAsDouble(Complex[][] c) {
        if (c == null) {
            return PlotDoubleConverter.REAL;
        }

        for (Complex[] d : c) {
            PlotDoubleConverter d2 = resolveComplexAsDouble(d);
            if (d2 == PlotDoubleConverter.ABS) {
                return d2;
            }
        }
        return PlotDoubleConverter.REAL;
    }

    public static PlotDoubleConverter resolveComplexAsDouble(Complex[] c) {
        if (c == null) {
            return PlotDoubleConverter.REAL;
        }
        for (Complex d : c) {
            if (!d.isReal()) {
                return PlotDoubleConverter.ABS;
            }
        }
        return PlotDoubleConverter.REAL;
    }

    public static ConvergenceEvaluator of(ParamSet paramSet, ConvergenceConfig config) {
        return new ConvergenceEvaluator(paramSet, config);
    }

    public static ConvergenceEvaluator of(CParam param, Object[] var) {
        return of(ConsoleParams.paramSet(param, var));
    }

    public static ConvergenceEvaluator of(ParamSet paramSet) {
        return new ConvergenceEvaluator(paramSet);
    }

    public static ConvergenceEvaluator of(CParam param, int[] var) {
        return of(ConsoleParams.paramSet(param, var));
    }

    public static ConvergenceEvaluator of(CParam param, double[] var) {
        return of(ConsoleParams.paramSet(param, var));
    }

    public static ConvergenceEvaluator of(CParam param, float[] var) {
        return of(ConsoleParams.paramSet(param, var));
    }

    public static ConvergenceEvaluator of(CParam param, long[] var) {
        return of(ConsoleParams.paramSet(param, var));
    }

    public static String getHadrumathsPlotVersion() {
        return PomIdResolver.resolvePomId(MathsPlot.class, new PomId("", "", "DEV")).getVersion();
    }

}
