//package net.thevpc.scholar.tmwlib.mom.console;
//
//import net.thevpc.scholar.math.Domain;
//import net.thevpc.scholar.math.geom.DefaultGeometryList;
//import net.thevpc.scholar.math.geom.GeometryList;
//import net.thevpc.scholar.math.meshalgo.MeshAlgo;
//import net.thevpc.scholar.math.meshalgo.rect.GridPrecision;
//import net.thevpc.scholar.math.meshalgo.rect.MeshAlgoRect;
//import net.thevpc.scholar.math.plot.console.*;
//import net.thevpc.scholar.math.plot.console.paramsets.DoubleArrayParamSet;
//import net.thevpc.scholar.math.plot.console.paramsets.ParamSet;
//import net.thevpc.scholar.math.plot.console.yaxis.YType;
//import net.thevpc.scholar.tmwlib.console.paramsets.FreqParam;
//import net.thevpc.scholar.tmwlib.mom.CircuitType;
//import net.thevpc.scholar.tmwlib.mom.TestFunctionsSymmetry;
//import net.thevpc.scholar.tmwlib.mom.console.paramsets.*;
//import net.thevpc.scholar.tmwlib.mom.console.paramsets.hints.*;
//import net.thevpc.scholar.tmwlib.mom.modes.FnEMEMXY;
//import net.thevpc.scholar.tmwlib.mom.testfunctions.gpmesh.GpAdaptiveMesh;
//import net.thevpc.scholar.tmwlib.mom.testfunctions.gpmesh.gppattern.GpPattern;
//import net.thevpc.scholar.tmwlib.mom.GpPatternFactory;
//import net.thevpc.scholar.tmwlib.mom.sources.modal.CutOffModalSources;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//
///**
// * @author : vpc
// * @creationtime 08 10 2006
// * je reviens  � la structure
// * .....###.....
// * .....###.....
// * .....###.....
// * .....###.....
// * .....###.....
// * pour faire mon �tude car la matrice que j'ai est bizarre
// */
//public class MoMPlotConsoleProjectTemplate extends PlotConsoleProjectTemplate{
//
//    private FreqParam freq;
//    private BoxXdimOverLamdaByFreqParamSet widthOverLamdaByFreq;
//    private WidthFactorParamSet widthOverLamda;
//    private BoxXwithParamSet width;
//    private HeightOverWidthParamSet heightOverWidth;
//    private BoxXminOverXdimParamSet xminOverWidth;
//    //private IrisOverWidthPlotParam irisOverWidth;
//    private HintGpFnAxisParamSet hintGpFnAxis;
//    private HintFnModeParamSet hintFnMode;
//    private HintAMatrixSparsifyParamSet hintAMatrixSparsify;
//    private HintBMatrixSparsifyParamSet hintBMatrixSparsify;
//    private HintRegularZnOperatorParamSet hintRegularZnOperator;
//    private HintSubModelEquivalentParam hintSubModelEquivalent;
//    private FractalZopGridPrecisionParamSet gpGridModel;
//    private GpGridSubModelParamSet gpGridSubModel;
//    private PropagatingModeSelectorParamSet propagatingModeSelectorParam;
//    private FractalParamSet fractal;
//    private FractalModelBaseParamSet fractalModelBase;
//    private ModeFunctionsCountParamSet modesCount;
////    private TestFunctionsCountPlotParam gpTestCount;
//    private ModeFunctionsParamSet modeFunctionsParam;
//    //    private GpPattern gpPattern = GpPatternFactory.ECHELON;
//    private TestFunctionsTypePlotParam gpTestTypeParam;
//    private String studyName;
//    private PlotConsole plotConsole;
//    //    protected String projectName;
//    private GeometryList[] structureShape = new GeometryList[2];
//    private CircuitTypeParamSet schemaTypeParam;
//    private DoubleArrayParamSet doubleParameter;
//    private FnConvergenceInfoPlotParam fnConvergenceInfo;
//    private String defaultStudyName;
//    private File folder;
//
//
//
//    public MoMPlotConsoleProjectTemplate() {
//        this(null);
//    }
//
//    public MoMPlotConsoleProjectTemplate(File folder) {
//        super(folder);
//
//        resetParams0();
//    }
//
//    protected void resetParams() {
//        resetParams0();
//    }
//
//
//
//
//
//    protected void resetParams0() {
////        ScalarProductFactory.setDefaultScalarProduct(ScalarProductFactory.FORMAL);
//        setFreq(new FreqParam(1E9));
//        setWidthOverLamda(new WidthFactorParamSet(0.33));
//        setHeightOverWidth(new HeightOverWidthParamSet(1.0 / 3.0));
//        setXminOverWidth(new BoxXminOverXdimParamSet(0));
////        setIrisOverWidth(new IrisOverWidthPlotParam(0.2));
//        setHintGpFnAxis(null);
//        setHintAMatrixSparsify(null);
//        setHintBMatrixSparsify(null);
//        setHintSubModelEquivalent(null);
//        setPropagatingModeSelectorParam(new PropagatingModeSelectorParamSet(new CutOffModalSources(1)));
//        setFractal(new FractalParamSet().dsteps(1));
//        setModeFunctionsCount(new ModeFunctionsCountParamSet().dsteps(200));
////        setTestFunctionsCount(new TestFunctionsCountPlotParam().dsteps(8));
//        setModeFunctionsParam(new ModeFunctionsParamSet(new FnEMEMXY()));
//        setStructureShape(CircuitType.SERIAL, new DefaultGeometryList(Domain.forBounds(0, 100, 0, 100)));
//        setStructureShape(CircuitType.PARALLEL, new DefaultGeometryList(Domain.forBounds(0, 100, 0, 100)));
//        setGpTestTypeParam(new MeshAlgoRect(new GridPrecision(1, 3)), GpPatternFactory.ECHELON);
//        setDoubleParameter(null);
//        setFnConvergenceInfo(null);
//    }
//
//    public void runStudy(ConsoleAxisList list) {
//        if (list.size() > 1) {
//            if (getPlotConsole().isDisposing()) {
//                return;
//            }
//        }
//        getPlotConsole().setWindowTitle(getStudyName());
//        ArrayList<ParamSet> p = getParams();
//        PlotData plotData = new PlotData(
//                getStudyName(), getReferenceStructure(), getModeledStructure(), list,
//                p.toArray(new ParamSet[p.size()])
//        );
//        getPlotConsole().runPlot(plotData);
//    }
//
//    public ArrayList<ParamSet> getParams() {
//        return new ArrayList<ParamSet>(Arrays.asList(
//                getDoubleParameter()
//                , getWidth()
//                , getFreq()
//                , getWidthOverLamdaByFreq()
//                , getWidthOverLamda()
//                , getHeightOverWidth()
//                , getXminOverWidth()
////                , getIrisOverWidth()
//                , modeFunctionsCount()
//                , getFractal()
//                , getFractalModelBase()
//                , getPropagatingModeSelectorParam()
//                , getHintAMatrixSparsify()
//                , getHintBMatrixSparsify()
//                , getHintSubModelEquivalent()
////                , testFunctionsCount()
//                , modeFunctionsParam()
//                , getFnConvergenceInfo()
//                , getGpGridModel()
//                , getGpGridSubModel()
//                , getGpTestTypeParam()
//                , getHintGpFnAxis()
//                , getHintFnMode()
//                , getHintRegularZnOperator()
//                , getSchemaTypeParam()
//        )
//        );
//    }
//
//
//    public void start() {
//        resetParams();
//        getPlotConsole().start();
//        try {
//            run();
//        } catch (ConsoleDisposingException e) {
//            //dispose is called by user
//            return;
//        }
//        getPlotConsole().dispose();
//    }
//
//    public void run() {
//        runPlotChoiceList();
//    }
//
//    protected void newStudy(String title, String prefix, String suffix) {
//        newStudy((prefix == null ? "" : prefix) + title + (suffix == null ? "" : suffix));
//    }
//
//    protected void newStudy() {
//        newStudy(getDefaultStudyName());
//    }
//
//    protected void newStudy(String title) {
//        getPlotConsole().getLog().trace("newStudy " + title);
//        resetParams();
//        setStudyName(title);
//    }
//
//    public void newPlotConsole() {
//        setPlotConsole(null);
//    }
//
//    public synchronized PlotConsole getPlotConsole() {
//        if (plotConsole == null) {
//            plotConsole = new PlotConsole(getClass().getSimpleName(), folder);
//            plotConsole.setAutoSavingFilePattern(getClass().getSimpleName() + "-{date}");
//        }
//        return plotConsole;
//    }
//
////    public MomStructure getModeledStructure() {
////        return modeledStructure;
////    }
////
////    public void setModeledStructure(MomStructure modele) {
////        this.modeledStructure = modele;
////    }
////
////    public MomStructure getReferenceStructure() {
////        return referenceStructure;
////    }
//
////    public void setReferenceStructure(MomStructure referenceStructure) {
////        this.referenceStructure = referenceStructure;
////    }
//
//    public FreqParam getFreq() {
//        return freq;
//    }
//
//    public void setFreq(FreqParam freq) {
//        this.freq = freq;
//    }
//
//    public WidthFactorParamSet getWidthOverLamda() {
//        return widthOverLamda;
//    }
//
//    public BoxXwithParamSet getWidth() {
//        return width;
//    }
//
//    public void setWidthOverLamda(WidthFactorParamSet widthOverLamda) {
//        this.widthOverLamda = widthOverLamda;
//        this.width = null;
//        this.widthOverLamdaByFreq = null;
//    }
//
//    public void setWidth(BoxXwithParamSet width) {
//        this.width = width;
//        this.widthOverLamda = null;
//        this.widthOverLamdaByFreq = null;
//    }
//
//    public HeightOverWidthParamSet getHeightOverWidth() {
//        return heightOverWidth;
//    }
//
//    public void setHeightOverWidth(HeightOverWidthParamSet heightOverWidth) {
//        this.heightOverWidth = heightOverWidth;
//    }
//
//    public BoxXminOverXdimParamSet getXminOverWidth() {
//        return xminOverWidth;
//    }
//
//    public void setXminOverWidth(BoxXminOverXdimParamSet xminOverWidth) {
//        this.xminOverWidth = xminOverWidth;
//    }
//
////    public IrisOverWidthPlotParam getIrisOverWidth() {
////        return irisOverWidth;
////    }
////
////    public void setIrisOverWidth(IrisOverWidthPlotParam irisOverWidth) {
////        this.irisOverWidth = irisOverWidth;
////    }
//
//    public HintGpFnAxisParamSet getHintGpFnAxis() {
//        return hintGpFnAxis;
//    }
//
//    public void setHintGpFnAxis(HintGpFnAxisParamSet hintGpFnAxis) {
//        this.hintGpFnAxis = hintGpFnAxis;
//    }
//
//    public HintAMatrixSparsifyParamSet getHintAMatrixSparsify() {
//        return hintAMatrixSparsify;
//    }
//
//    public void setHintAMatrixSparsify(HintAMatrixSparsifyParamSet hintAMatrixSparsify) {
//        this.hintAMatrixSparsify = hintAMatrixSparsify;
//    }
//
//    public HintBMatrixSparsifyParamSet getHintBMatrixSparsify() {
//        return hintBMatrixSparsify;
//    }
//
//    public void setHintBMatrixSparsify(HintBMatrixSparsifyParamSet hintBMatrixSparsify) {
//        this.hintBMatrixSparsify = hintBMatrixSparsify;
//    }
//
//    public HintRegularZnOperatorParamSet getHintRegularZnOperator() {
//        return hintRegularZnOperator;
//    }
//
//    public void setHintRegularZnOperator(HintRegularZnOperatorParamSet hintRegularZnOperator) {
//        this.hintRegularZnOperator = hintRegularZnOperator;
//    }
//
//    public HintSubModelEquivalentParam getHintSubModelEquivalent() {
//        return hintSubModelEquivalent;
//    }
//
//    public void setHintSubModelEquivalent(HintSubModelEquivalentParam hintSubModelEquivalent) {
//        this.hintSubModelEquivalent = hintSubModelEquivalent;
//    }
//
//    public FractalZopGridPrecisionParamSet getGpGridModel() {
//        return gpGridModel;
//    }
//
//    public FractalZopGridPrecisionParamSet setGpGridModel(FractalZopGridPrecisionParamSet gpGridModel) {
//        this.gpGridModel = gpGridModel;
//        return gpGridModel;
//    }
//
//    public GpGridSubModelParamSet getGpGridSubModel() {
//        return gpGridSubModel;
//    }
//
//    public GpGridSubModelParamSet setGpGridSubModel(GpGridSubModelParamSet gpGridSubModel) {
//        this.gpGridSubModel = gpGridSubModel;
//        return gpGridSubModel;
//    }
//
//    public PropagatingModeSelectorParamSet getPropagatingModeSelectorParam() {
//        return propagatingModeSelectorParam;
//    }
//
//    public void setPropagatingModeSelectorParam(PropagatingModeSelectorParamSet srcCounter) {
//        this.propagatingModeSelectorParam = srcCounter;
//    }
//
//    public FractalParamSet getFractal() {
//        return fractal;
//    }
//
//    public void setFractal(FractalParamSet fractal) {
//        this.fractal = fractal;
//    }
//
//    public FractalModelBaseParamSet getFractalModelBase() {
//        return fractalModelBase;
//    }
//
//    public void setFractalModelBase(FractalModelBaseParamSet fractalModelBase) {
//        this.fractalModelBase = fractalModelBase;
//    }
//
//    public ModeFunctionsCountParamSet modeFunctionsCount() {
//        return modesCount;
//    }
//
//    public void setModeFunctionsCount(ModeFunctionsCountParamSet fnBaseMax) {
//        this.modesCount = fnBaseMax;
//    }
//
////    public TestFunctionsCountPlotParam testFunctionsCount() {
////        return gpTestCount;
////    }
////
////    public void setTestFunctionsCount(TestFunctionsCountPlotParam gpTestCount) {
////        this.gpTestCount = gpTestCount;
////    }
//
//    public ModeFunctionsParamSet modeFunctionsParam() {
//        return modeFunctionsParam;
//    }
//
//    public void setModeFunctionsParam(ModeFunctionsParamSet fnBaseTypeParam) {
//        this.modeFunctionsParam = fnBaseTypeParam;
//    }
//
//    public TestFunctionsTypePlotParam setGpTestTypeParam(GpPattern... gpPattern) {
//        return setGpTestTypeParam(null, null, gpPattern);
//    }
//
//    public TestFunctionsTypePlotParam setGpTestTypeParam(MeshAlgo meshAlgo, GpPattern... gpPattern) {
//        GpAdaptiveMesh[] all = new GpAdaptiveMesh[gpPattern.length];
//        for (int i = 0; i < gpPattern.length; i++) {
//            all[i] = new GpAdaptiveMesh(getStructureShapes()[0], getStructureShapes()[1], gpPattern[i], meshAlgo);
//        }
//        TestFunctionsTypePlotParam gpTestTypeParam1 = new TestFunctionsTypePlotParam(all);
//        return setGpTestTypeParam(gpTestTypeParam1);
//    }
//
//    public TestFunctionsTypePlotParam setGpTestTypeParam(TestFunctionsSymmetry sym, GpPattern... gpPattern) {
//        return setGpTestTypeParam(sym, null, gpPattern);
//    }
//
//    public TestFunctionsTypePlotParam setGpTestTypeParam(TestFunctionsSymmetry sym, MeshAlgo meshAlgo, GpPattern... gpPattern) {
//        GpAdaptiveMesh[] all = new GpAdaptiveMesh[gpPattern.length];
//        for (int i = 0; i < gpPattern.length; i++) {
//            all[i] = new GpAdaptiveMesh(getStructureShape(CircuitType.SERIAL), getStructureShape(CircuitType.PARALLEL), gpPattern[i], sym, meshAlgo,null,null);
//        }
//        TestFunctionsTypePlotParam gpTestTypeParam1 = new TestFunctionsTypePlotParam(all);
//        return setGpTestTypeParam(gpTestTypeParam1);
//    }
//
//    public TestFunctionsTypePlotParam getGpTestTypeParam() {
//        return gpTestTypeParam;
//    }
//
//    public TestFunctionsTypePlotParam setGpTestTypeParam(TestFunctionsTypePlotParam gpTestTypeParam) {
//        this.gpTestTypeParam = gpTestTypeParam;
//        return this.gpTestTypeParam;
//    }
//
//    public String getStudyName() {
//        return studyName;
//    }
//
//    public void setStudyName(String studyName) {
//        this.studyName = studyName;
//    }
//
//    public void setPlotConsole(PlotConsole plotConsole) {
//        this.plotConsole = plotConsole;
//    }
//
//    public GeometryList[] getStructureShapes() {
//        return structureShape;
//    }
//
//    public GeometryList getStructureShape(CircuitType circuit) {
//        return structureShape[circuit.ordinal()];
//    }
//
//    public GeometryList setStructureShape(GeometryList structureShape) {
//        setStructureShape(CircuitType.PARALLEL, structureShape);
//        return setStructureShape(CircuitType.SERIAL, structureShape);
//    }
//
//    public GeometryList setStructureShape(CircuitType circuit, GeometryList structureShape) {
//        this.structureShape[circuit.ordinal()] = structureShape;
////        if (gpTestTypeParamP == null) {
////            setGpTestTypeParam(SchemaType.SERIAL, GpPatternFactory.ECHELON);
////        }
//        return structureShape;
//    }
//
//    public DoubleArrayParamSet getDoubleParameter() {
//        return doubleParameter;
//    }
//
//    public DoubleArrayParamSet setDoubleParameter(DoubleArrayParamSet doubleParameter) {
//        this.doubleParameter = doubleParameter;
//        return doubleParameter;
//    }
//
//    public FnConvergenceInfoPlotParam getFnConvergenceInfo() {
//        return fnConvergenceInfo;
//    }
//
//    public FnConvergenceInfoPlotParam setFnConvergenceInfo(FnConvergenceInfoPlotParam fnConvergenceInfo) {
//        this.fnConvergenceInfo = fnConvergenceInfo;
//        return fnConvergenceInfo;
//    }
//
//    public String getDefaultStudyName() {
//        return defaultStudyName;
//    }
//
//    public void setDefaultStudyName(String defaultStudyName) {
//        this.defaultStudyName = defaultStudyName;
//    }
//
//    public HintFnModeParamSet getHintFnMode() {
//        return hintFnMode;
//    }
//
//    public void setHintFnMode(HintFnModeParamSet hintFnMode) {
//        this.hintFnMode = hintFnMode;
//    }
//
//    public BoxXdimOverLamdaByFreqParamSet getWidthOverLamdaByFreq() {
//        return widthOverLamdaByFreq;
//    }
//
//    public void setWidthOverLamdaByFreq(BoxXdimOverLamdaByFreqParamSet widthOverLamdaByFreq) {
//        this.widthOverLamdaByFreq = widthOverLamdaByFreq;
//        this.width = null;
//        this.widthOverLamda = null;
//    }
//
//    protected YType[] getYTypes() {
//        return getModeledStructure() == null ? new YType[]{YType.REFERENCE} : YType.values();
//    }
//
//    public CircuitTypeParamSet getSchemaTypeParam() {
//        return schemaTypeParam;
//    }
//
//    public void setSchemaTypeParam(CircuitType... vs) {
//        setSchemaTypeParam(new CircuitTypeParamSet(vs));
//    }
//
//    public void setSchemaTypeParam(CircuitTypeParamSet schemaTypeParam) {
//        this.schemaTypeParam = schemaTypeParam;
//    }
//
//}
