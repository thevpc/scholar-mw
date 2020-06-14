package net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectBuilder;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceConfig;
import net.vpc.scholar.hadrumaths.plot.convergence.ConvergenceEvaluator;
import net.vpc.scholar.hadrumaths.geom.*;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.rect.GridPrecision;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.WallBorders;
import net.vpc.scholar.hadruwaves.mom.*;
import net.vpc.scholar.hadruwaves.mom.sources.modal.ModalSources;
import net.vpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.gppattern.BoxModesPattern;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 12 mai 2005 12:39:21
 */
public class MomStructureFractalZop extends MomStructure {
    public static void main(String[] args) {
        System.out.println(0.1 + 0.2);
    }

    public static final String HINT_USE_OLD_ZS_STYLE = "HINT_USE_OLD_ZS_STYLE";
    public static final String HINT_ZS_CONVERGENCE = "HINT_ZS_CONVERGENCE";
    public static final String HINT_SUB_MODEL_EQUIVALENT = "HINT_SUB_MODEL_EQUIVALENT";
    public static final String HINT_GENERATOR_CONFIGURATOR = "HINT_GENERATOR_CONFIGURATOR";
    public static final String PARAM_MODEL_GRID_PRECISION = "PARAM_MODEL_GRID_PRECISION";
    public static final String PARAM_MODEL_SUB_GRID_PRECISION = "PARAM_MODEL_SUB_GRID_PRECISION";
    public static final String PARAM_MODEL_BASE_K_FACTOR = "PARAM_MODEL_BASE_K_FACTOR";
    public static final String PARAM_SUB_PROPAGATING_MODE_SELECTOR = "PARAM_SUB_PROPAGATING_MODE_SELECTOR";
    int realK;
    private GpAdaptiveMesh directMesh;

    public MomStructureFractalZop() {
        setHintSubModelEquivalent(true);
        evaluator=new MomStructureEvaluator(this){
            @Override
            public MatrixAEvaluator createMatrixAEvaluator() {
                switch (getProjectType()) {
                    case WAVE_GUIDE: {
                        switch (getCircuitType()) {
                            case SERIAL: {
                                return new ZsFactalMatrixAWaveguideSerialEvaluator();
                            }
                            case PARALLEL: {
                                return new ZsFactalMatrixAWaveguideParallelEvaluator();
                            }
                        }
                        break;
                    }
                    case PLANAR_STRUCTURE: {
                        switch (getCircuitType()) {
                            case SERIAL: {
                                //??
                                break;
                            }
                            case PARALLEL: {
                                //?
                                break;
                            }
                        }
                    }
                }
                throw new IllegalArgumentException("Impossible");
            }
        };
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder o=super.toTsonElement(context).toObject().builder();
        o.add("realK", context.elem(realK));
        o.add("directGp", context.elem(directMesh));
        return o.build();
    }
//    @Override
//    public Dumper getDumpStringHelper() {
//        Dumper helper = super.getDumpStringHelper();
//        helper.add("realK", realK);
//        helper.add("directGp", directMesh);
//        return helper;
//    }

    private static GeometryList asRect(GeometryList other) {
        Geometry[] transform = ((FractalAreaGeometryList) other).getTransform();
        for (int i = 0; i < transform.length; i++) {
            transform[i] = GeometryFactory.createPolygon(transform[i].getDomain());
        }
        return new DefaultGeometryList(other.getBounds(), transform);
    }

    @Override
    public MomStructure setTestFunctions(TestFunctions testFunctions) {
        if (realK < getModelBaseKFactor()) {
            throw new IllegalArgumentException("Kifech?");
//            super.setGpTestFunctions(gpEssaiType);
//            return;
        }
        if (testFunctions instanceof GpAdaptiveMesh) {
            GpAdaptiveMesh gpa = (GpAdaptiveMesh) testFunctions;
            gpa = (GpAdaptiveMesh) gpa.clone();
            gpa.setStructure(this);
            MeshAlgo meshAlgo = gpa.getMeshAlgo();
            if (meshAlgo instanceof MeshAlgoRect) {
                //((MeshAlgoRect) meshAlgo).setGridPrecision(getModelGridPrecision());
            }
            directMesh = gpa;
            gpa.setStructure(this);
            GpAdaptiveMesh n = new GpAdaptiveMesh(
                    asRect(gpa.getPolygons(CircuitType.SERIAL)),
                    asRect(gpa.getPolygons(CircuitType.PARALLEL)),
                    gpa.getPattern(),
                    gpa.getSymmetry(),
                    gpa.getMeshAlgo(), null, null);
            n.setStructure(this);
            testFunctions = n;
        } else if (testFunctions != null) {
            throw new IllegalArgumentException("shé pas?");
        }
        return super.setTestFunctions(testFunctions);
    }

    public GpAdaptiveMesh getDirectGpTestFunctions() {
        return directMesh;
    }



    ComplexMatrix getAMatrix(ComplexMatrix ZopValue) {
        TestFunctions gpTestFunctions = testFunctions();
        DoubleToVector[] g = gpTestFunctions.arr();
        Complex[][] b = new Complex[g.length][g.length];
        ModeFunctions fn = modeFunctions();
        ModeInfo[] modes = this.getModes();
        ModeInfo[] n_evan = getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
        ModeInfo[] n_propa = fn.getPropagatingModes();
        ComplexMatrix sp = getTestModeScalarProducts(ProgressMonitors.none());
        for (int p = 0; p < g.length; p++) {
            ComplexVector spp = sp.getRow(p);
            for (int q = 0; q < g.length; q++) {
                ComplexVector spq = sp.getRow(q);
                Complex c = Maths.CZERO;
                for (ModeInfo n : n_evan) {
                    Complex zn = n.impedance.impedanceValue();
                    Complex sp1 = spp.get(n.index);
                    Complex sp2 = spq.get(n.index).conj();
                    c = c.plus(zn.mul(sp1).mul(sp2));
                }
                b[p][q] = c;
            }
        }

        Complex[][] zop = ZopValue == null ? null : ZopValue.getArray();
        for (int p = 0; p < g.length; p++) {
            ComplexVector spp = sp.getRow(p);
            for (int q = 0; q < g.length; q++) {
                ComplexVector spq = sp.getRow(q);
                Complex c = Maths.CZERO;
                if (zop != null) {//zop==null si k==1
                    for (int m = 0; m < zop.length; m++) {
                        for (int n = 0; n < zop[m].length; n++) {
                            Complex sp1 = spp.get(n_propa[n].index);
                            Complex sp2 = spq.get(n_propa[m].index).conj();
                            Complex zs = zop[n_propa[m].index][n_propa[n].index];
                            c = c.plus(zs.mul(sp1).mul(sp2));
                        }
                    }
                }
                b[p][q] = b[p][q].plus(c);
            }
        }

        return Maths.matrix(b);
    }

    ComplexMatrix Bevan() {
        TestFunctions gpTestFunctions = testFunctions();
        DoubleToVector[] g = gpTestFunctions.arr();
        Complex[][] b = new Complex[g.length][g.length];
        ModeFunctions fn = modeFunctions();
        ModeInfo[] modes = this.getModes();
        ModeInfo[] n_evan = getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
        ComplexMatrix sp = getTestModeScalarProducts(ProgressMonitors.none());
        for (int p = 0; p < g.length; p++) {
            ComplexVector spp = sp.getRow(p);
            for (int q = 0; q < g.length; q++) {
                ComplexVector spq = sp.getRow(q);
                Complex c = Maths.CZERO;
                for (ModeInfo n : n_evan) {
                    Complex zn = n.impedance.impedanceValue();
                    Complex sp1 = spp.get(n.index);
                    Complex sp2 = spq.get(n.index).conj();
                    c = c.plus(zn.mul(sp1).mul(sp2));
                }
                b[p][q] = c;
            }
        }
        return Maths.matrix(b);
    }

//    public CMatrix getBMatrixImpl() {
//        GpTestFunctions gpTestFunctions = getGpTestFunctions();
//        CFunctionXY2D[] g = gpTestFunctions.gp();
//        FnIndexes[] n_propa = getFnBaseFunctions().getPropagatingModes();
//        Complex[][] a = new Complex[g.length][n_propa.length];
//        ScalarProductCache sp = getTestModeScalarProducts();
//        for (int n = 0; n < n_propa.length; n++) {
//            for (int p = 0; p < g.length; p++) {
//                a[p][n] = sp.gf(p, n_propa[n].index).negate();
//            }
//        }
//        return new CMatrix(a);
//    }

//    public CMatrix getZinImpl() {
//        CMatrix A_ = getAMatrix();
//
//        CMatrix B_ = getBMatrix();
//
//        CMatrix ZinCond;
//
//        try {
//            ZinCond = B_.transpose().multiply(A_.inverseCond()).multiply(B_).inverse();
//        } catch (Exception e) {
//            getLog().error("Error P3MethodeModeleZop.Zin: " + e);
//            getLog().error("A=" + B_);
//            getLog().error("B=" + A_);
//            throw new RuntimeException(e);
//        }
////        System.out.println(getClass().getName() + ".resolveZin() : END  =" + ZinCond);
//        return ZinCond;
//    }

//    public CMatrix getTestcoeffImpl() {
//        invalidateCache();
//
//        CMatrix A = getBMatrix();
//        CMatrix B = getAMatrix();
//        CMatrix Testcoeff;
//
//        try {
//            Testcoeff = B.solve(A);
//        } catch (Exception e) {
//            getLog().error("Error : " + e);
//            getLog().error("A=" + A);
//            getLog().error("B=" + B);
//            throw new RuntimeException(e);
//        }
//        return Testcoeff;

    //    }
    @Override
    public MomStructure setFractalScale(int k) {
//        this.k = k == 0 ? 0 : -1;
        this.fractalScale = getModelBaseKFactor();
        this.realK = k;
        invalidateCache();
        return this;
    }

    public ComplexMatrix resolveNextPointFixe(ComplexMatrix Zinit, int kInit) {
        getLog().debug(1.2337139917695478E-4 / 4.11237997256516E-5);
        double a = getDomain().getXwidth();
        setFractalScale(1);
        for (int ki = 0; ki < kInit; ki++) {
            if (true) {
                throw new IllegalArgumentException("TODO : getMetalQuotient() removed");
            }
            double metalQuotient = 1.0 / 3;
            Domain d = getDomain();
            setDomain(Domain.ofWidth(d.xmin(),a * Math.pow(metalQuotient, (kInit - ki)),d.xmin(),d.ywidth()));
            ComplexMatrix A_ = getAMatrix(Zinit);
            ComplexMatrix B_ = matrixB().evalMatrix();
            ComplexMatrix ZinCond;
            try {
                ComplexMatrix aInv = A_.inv(this.getInvStrategy(), this.getCondStrategy(), this.getNormStrategy());
                //should use conjugate transpose
//                ZinCond = B_.transpose().multiply(aInv).multiply(B_).inv();
                ZinCond = B_.transposeHermitian().mul(aInv).mul(B_).inv();
            } catch (Exception e) {
                getLog().error("Error P3MethodeModeleZop.resolveNextPointFixe: " + e);
                getLog().error("A=" + B_);
                getLog().error("B=" + A_);
                throw new RuntimeException(e);
            }
            getLog().debug("k=" + (kInit - ki) + " => a=" + getDomain().getXwidth());
            getLog().debug("\tz=" + ZinCond);
            getLog().debug("\terr=" + ZinCond.getErrorMatrix(Zinit, 1E-5));
            Zinit = ZinCond;
        }
        return Zinit;
    }

    public GridPrecision getModelGridPrecision() {
        return (GridPrecision) getParameter(PARAM_MODEL_GRID_PRECISION);
    }

    public GridPrecision getSubModelGridPrecision() {
        return (GridPrecision) getParameter(PARAM_MODEL_SUB_GRID_PRECISION);
    }

    /**
     * to unset HintZsConvergence call setHintZsConvergence(null, null)
     *
     * @param fnMax
     * @param convPars
     */
    public void setHintZsConvergence(int[] fnMax, ConvergenceConfig convPars) {
        if (fnMax == null) {
            if (convPars != null) {
                throw new IllegalArgumentException("convPars should be null if fnMax is null");
            }
            getHintsManager().removeHint(HINT_ZS_CONVERGENCE);
            getHintsManager().removeHint(HINT_ZS_CONVERGENCE + ".fn");
            getHintsManager().removeHint(HINT_ZS_CONVERGENCE + ".ConvergenceConfig");
        } else {
            if (convPars == null) {
                throw new IllegalArgumentException("convPars should not be null if fnMax is not");
            }
            getHintsManager().setHint(HINT_ZS_CONVERGENCE, Boolean.TRUE);
            getHintsManager().setHintNotNull(HINT_ZS_CONVERGENCE + ".fnMax", fnMax);
            getHintsManager().setHintNotNull(HINT_ZS_CONVERGENCE + ".ConvergenceConfig", convPars);
        }
    }

    public boolean isHintZsConvergence() {
        return Boolean.TRUE.equals(getHintsManager().getHint(HINT_ZS_CONVERGENCE));
    }

    public int[] getHintZsConvergenceFnMax() {
        return (int[]) getHintsManager().getHint(HINT_ZS_CONVERGENCE + ".fnMax");
    }

    public ConvergenceConfig getHintZsConvergenceParameters() {
        return (ConvergenceConfig) getHintsManager().getHint(HINT_ZS_CONVERGENCE + ".ConvergenceConfig");
    }

    /**
     * this an optimization for not rercalculating Zin fo each submodel.
     * It defaults to true if not specified.
     *
     * @param eq true if submodels are equals
     */
    public void setHintSubModelEquivalent(boolean eq) {
        getHintsManager().setHint(HINT_SUB_MODEL_EQUIVALENT, eq);
    }

    public void setHintUseOldZsStyle(boolean eq) {
        getHintsManager().setHint(HINT_USE_OLD_ZS_STYLE, eq);
    }

    public boolean isHintUseOldZsStyle() {
        return Boolean.TRUE.equals(getHintsManager().getHint(HINT_USE_OLD_ZS_STYLE, false));
    }

    public void setHintZsFractalGeneratorConfigurator(ZsFractalGeneratorConfigurator zsFractalGeneratorConfigurator) {
        getHintsManager().setHintNotNull(HINT_GENERATOR_CONFIGURATOR, zsFractalGeneratorConfigurator);
    }

    public ZsFractalGeneratorConfigurator getZsFractalGeneratorConfigurator() {
        return (ZsFractalGeneratorConfigurator) getHintsManager().getHint(HINT_GENERATOR_CONFIGURATOR);
    }

    public int getModelBaseKFactor() {
        return getParameterNumber(PARAM_MODEL_BASE_K_FACTOR, 1).intValue();
    }

    public ModalSources getSubPropagatingModeSelector() {
        return (ModalSources) getParameter(PARAM_SUB_PROPAGATING_MODE_SELECTOR);
    }

    public void setSubPropagatingModeSelector(ModalSources propagatingModeSelector) {
        setParameterNotNull(PARAM_SUB_PROPAGATING_MODE_SELECTOR, propagatingModeSelector);
    }

    public Zoperator[] Zop() {
        MomStructureFractalZop str2 = this;
        GpAdaptiveMesh gpAdaptatif = ((GpAdaptiveMesh) str2.getDirectGpTestFunctions());
        FractalAreaGeometryList polygon = (FractalAreaGeometryList) gpAdaptatif.getPolygons(str2.getCircuitType());
        Geometry[] transform = polygon.getTransform();
        Zoperator[] ops = new Zoperator[transform.length];
        Domain domain = str2.getDomain();
        int theK = str2.realK;
        boolean isSimple0 = (theK) <= str2.getModelBaseKFactor();
        boolean isSimple = (theK - 1) <= str2.getModelBaseKFactor();
        if (isSimple0) {
            return new Zoperator[0];
        } else {
            ModalSources subModeSelector = str2.getSubPropagatingModeSelector();
            Domain globalDomain = (Domain) getParameter("globalDomain", null);
            WallBorders globalWallBorders = (WallBorders) getParameter("globalWallBorders", null);
            if (globalDomain == null) {
                globalDomain = getDomain();
            }
            if (globalWallBorders == null) {
                globalWallBorders = getBorders();
            }
            for (int i = 0; i < transform.length; i++) {
                MomStructure str = isSimple ? null : str2.clone();
                if (str == null) {
                    str = new MomStructure();
                    str.load(str2);
                }
                str.setParameter("globalDomain", globalDomain);
                str.setParameter("globalWallBorders", globalWallBorders);
                str.setDomain(polygon.getDomain(transform[i].getDomain(), domain));
                str.setBorders(BoxModesPattern.getVirtualWalls(str.getDomain(), globalDomain, getCircuitType(), globalWallBorders));
                if (subModeSelector != null) {
                    str.setSources(subModeSelector);
                    str.removeParameter(PARAM_SUB_PROPAGATING_MODE_SELECTOR);
                }
                str.setFractalScale(str2.realK - 1);
                if (isSimple) {
                    ZsFractalGeneratorConfigurator generatorConfigurator = str2.getZsFractalGeneratorConfigurator();
                    if (generatorConfigurator == null) {
                        GpAdaptiveMesh gpAdaptatif2 = ((GpAdaptiveMesh) str.testFunctions());
                        MeshAlgo meshAlgo = gpAdaptatif2.getMeshAlgo();
                        if (meshAlgo instanceof MeshAlgoRect) {
                            ((MeshAlgoRect) meshAlgo).setGridPrecision(str2.getSubModelGridPrecision());
                        }
                        str.setTestFunctions(gpAdaptatif2);
                    } else {
                        generatorConfigurator.configure(str, str2);
                    }
                }
                str.invalidateCache();
//                try {
//                    saveString(DumpStringUtils.dump(str),new File("zop"+(i+1)+".txt"));
//                } catch (IOException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//                System.out.println("op : " + DumpStringUtils.dump(str));
                ComplexMatrix cMatrix;
                if (i > 0 && str2.getHintsManager().isHint(MomStructureFractalZop.HINT_SUB_MODEL_EQUIVALENT, true)) {
                    cMatrix = ops[0].getMatrix();
                } else {
                    if (isHintZsConvergence()) {
                        cMatrix = str
                                .inputImpedance()
                                .converge(ConvergenceEvaluator.of(MomParamFactory.params.modesCount(), getHintZsConvergenceFnMax()).setConfig(getHintZsConvergenceParameters()))
                                .evalMatrix();
                    } else {
                        cMatrix = str.inputImpedance().evalMatrix();
                    }
                    boolean useZParity = str.getProjectType() == ProjectType.WAVE_GUIDE;
                    //TODO pourquoi paire ?
                    if (useZParity) {
                        //cMatrix = cMatrix.multiply(2);
                    }
                }
//                System.out.println("op"+(i+1)+" = " + cMatrix);
                ops[i] = new Zoperator(cMatrix, str.modeFunctions());
            }
            return ops;
        }
    }

    public Yoperator[] Yop() {
        MomStructureFractalZop str2 = this;
        GpAdaptiveMesh gpAdaptatif = ((GpAdaptiveMesh) str2.getDirectGpTestFunctions());
        FractalAreaGeometryList polygon = (FractalAreaGeometryList) gpAdaptatif.getPolygons(str2.getCircuitType());
        Geometry[] transform = polygon.getTransform();
        Yoperator[] ops = new Yoperator[transform.length];
        Domain domain = str2.getDomain();
        int theK = str2.realK;
        boolean isSimple0 = (theK) <= str2.getModelBaseKFactor();
        boolean isSimple = (theK - 1) <= str2.getModelBaseKFactor();
        if (isSimple0) {
            return new Yoperator[0];
        } else {
            ModalSources subModeSelector = str2.getSubPropagatingModeSelector();
            for (int i = 0; i < transform.length; i++) {
                MomStructure str = isSimple ? null : str2.clone();
                if (str == null) {
                    str = new MomStructure();
                    str.load(str2);
                }
                if (subModeSelector != null) {
                    str.setSources(subModeSelector);
                    str.removeParameter(PARAM_SUB_PROPAGATING_MODE_SELECTOR);
                }
                str.setFractalScale(str2.realK - 1);
                str.setDomain(polygon.getDomain(transform[i].getDomain(), domain));
                if (isSimple) {
                    GpAdaptiveMesh gpAdaptatif2 = ((GpAdaptiveMesh) str.testFunctions());
                    MeshAlgo meshAlgo = gpAdaptatif2.getMeshAlgo();
                    if (meshAlgo instanceof MeshAlgoRect) {
                        ((MeshAlgoRect) meshAlgo).setGridPrecision(str2.getSubModelGridPrecision());
                    }
                    str.setTestFunctions(gpAdaptatif2);
                }
                str.invalidateCache();
//                try {
//                    saveString(DumpStringUtils.dump(str),new File("zop"+(i+1)+".txt"));
//                } catch (IOException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//                System.out.println("op : " + DumpStringUtils.dump(str));
                ComplexMatrix cMatrix;
                if (i > 0 && str2.getHintsManager().isHint(MomStructureFractalZop.HINT_SUB_MODEL_EQUIVALENT, true)) {
                    cMatrix = ops[0].getMatrix();
                } else {
                    if (isHintZsConvergence()) {
                        cMatrix = str
                                .inputImpedance()
                                .converge(ConvergenceEvaluator.of(MomParamFactory.params.modesCount(), getHintZsConvergenceFnMax()).setConfig(getHintZsConvergenceParameters()))
                                .evalMatrix();
                    } else {
                        cMatrix = str.inputImpedance().evalMatrix();
                    }
                    cMatrix = cMatrix.inv();
                }
//                System.out.println("op"+(i+1)+" = " + cMatrix);
                ops[i] = new Yoperator(cMatrix, str.modeFunctions());
            }
            return ops;
        }
    }
}
