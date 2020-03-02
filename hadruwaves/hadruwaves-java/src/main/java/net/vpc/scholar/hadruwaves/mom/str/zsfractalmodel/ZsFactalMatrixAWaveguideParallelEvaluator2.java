package net.vpc.scholar.hadruwaves.mom.str.zsfractalmodel;

import net.vpc.common.mon.ProgressMonitors;
import net.vpc.common.mon.VoidMonitoredAction;
import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.geom.FractalAreaGeometryList;
import net.vpc.scholar.hadrumaths.geom.Geometry;
import net.vpc.scholar.hadrumaths.meshalgo.MeshAlgo;
import net.vpc.scholar.hadrumaths.meshalgo.rect.MeshAlgoRect;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.common.mon.MonitoredAction;
import net.vpc.common.mon.ProgressMonitor;
import net.vpc.scholar.hadruwaves.ModeInfo;
import net.vpc.scholar.hadruwaves.mom.ModeFunctions;
import net.vpc.scholar.hadruwaves.mom.MomStructure;
import net.vpc.scholar.hadruwaves.mom.TestFunctions;
import net.vpc.scholar.hadruwaves.mom.str.MatrixAEvaluator;
import net.vpc.scholar.hadruwaves.mom.testfunctions.gpmesh.GpAdaptiveMesh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 24 mai 2007 21:55:23
 */
public class ZsFactalMatrixAWaveguideParallelEvaluator2 implements MatrixAEvaluator {
    public static void storeString(String content, File file) throws IOException {
        System.out.println("file = " + file.getCanonicalPath());
        PrintStream os = null;
        try {
            os = new PrintStream(new FileOutputStream(file));
            os.print(content);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

    @Override
    public ComplexMatrix evaluate(final MomStructure str, ProgressMonitor monitor) {
        final MomStructureFractalZop str2 = (MomStructureFractalZop) str;
        final TestFunctions gpTestFunctions = str.getTestFunctions();
        final String simpleName = getClass().getSimpleName();
        return Maths.invokeMonitoredAction(monitor, simpleName, new MonitoredAction<ComplexMatrix>() {
            @Override
            public ComplexMatrix process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                final DoubleToVector[] g = gpTestFunctions.arr();
                final Complex[][] b = new Complex[g.length][g.length];
                ModeFunctions fn = str.getModeFunctions();
                ModeInfo[] modes = str.getModes();
                final ModeInfo[] n_evan = str.getHintsManager().isHintRegularZnOperator() ? modes : fn.getVanishingModes();
                final ComplexMatrix sp = str.getTestModeScalarProducts(ProgressMonitors.none());
                boolean complex = fn.isComplex() || gpTestFunctions.isComplex();
                boolean symMatrix = !complex;
                final String monMessage = simpleName;
                if (symMatrix) {
                    ProgressMonitor[] mons = ProgressMonitors.split(monitor, 3);
                    final ProgressMonitor m0 = ProgressMonitors.incremental(mons[0], (g.length * g.length));
                    Maths.invokeMonitoredAction(m0, monMessage + " (g x g)", new VoidMonitoredAction() {
                        @Override
                        public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                            for (int p = 0; p < g.length; p++) {
                                ComplexVector spp = sp.getRow(p);
                                for (int q = p; q < g.length; q++) {
                                    ComplexVector spq = sp.getRow(q);
                                    Complex c = Maths.CZERO;
                                    for (ModeInfo n : n_evan) {
                                        Complex yn = n.impedance.admittanceValue();
                                        c = c.add(yn.mul(spp.get(n.index)).mul(spq.get(n.index).conj()));
                                    }
                                    b[p][q] = c;
                                    m0.inc(monMessage);
                                }
                            }
                        }
                    });


                    final Yoperator[] opValues = Yop(str2, mons[1]);
                    final ProgressMonitor m2 = ProgressMonitors.incremental(mons[2], (opValues.length * g.length * g.length));
                    Maths.invokeMonitoredAction(m2, monMessage + " (g x g x p)", new VoidMonitoredAction() {
                        @Override
                        public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                            for (Yoperator opValue : opValues) {
                                Complex[][] op = opValue == null ? null : opValue.getMatrix().getArray();
                                if (op != null) {//op==null si k==1
                                    //System.out.println("op = " + opValue.getMatrix());
                                    ModeInfo[] n_propa = opValue.getFn().getPropagatingModes();
                                    ComplexMatrix spc2 = Maths.scalarProductCache(g, opValue.getFn().arr(), str.getHintsManager().getHintAxisType().toAxisXY(), ProgressMonitors.none());
                                    for (int p = 0; p < g.length; p++) {
                                        ComplexVector spc2p = spc2.getRow(p);
                                        for (int q = p; q < g.length; q++) {
                                            ComplexVector spc2q = spc2.getRow(q);
                                            Complex c = Maths.CZERO;
                                            for (int m = 0; m < op.length; m++) {
                                                for (int n = 0; n < op[m].length; n++) {
                                                    //#@ ID=1@20070423
                                                    //#@   OLD
//                                    Complex sp1 = spc2.fg(n, p);
//                                    Complex sp2 = spc2.gf(q, m);
//                                    c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
                                                    //#@   NEW
//                                    Complex sp1 = spc2.fg(n_propa[n].index, p);
//                                    Complex sp2 = spc2.gf(q, n_propa[m].index);
                                                    Complex sp1 = spc2p.get(n_propa[m].index);
                                                    Complex sp2 = spc2q.get(n_propa[n].index).conj();
                                                    c = c.add((op[m][n]).mul(sp1).mul(sp2));
                                                    //#@   END
                                                }
                                            }
                                            b[p][q] = b[p][q].add(c);
                                            m2.inc(monMessage);
                                        }
                                    }
                                }
                            }
                            for (int p = 0; p < g.length; p++) {
                                for (int q = 0; q < p; q++) {
                                    b[p][q] = b[q][p];
                                    m2.inc(monMessage);
                                }
                            }
                        }
                    });
                } else {
                    ProgressMonitor[] mons = ProgressMonitors.split(monitor, 3);
                    final ProgressMonitor m0 = ProgressMonitors.incremental(mons[0], (g.length * g.length));
                    Maths.invokeMonitoredAction(m0, monMessage + " (g x g)", new VoidMonitoredAction() {
                        @Override
                        public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                            for (int p = 0; p < g.length; p++) {
                                ComplexVector spp = sp.getRow(p);
                                for (int q = 0; q < g.length; q++) {
                                    ComplexVector spq = sp.getRow(q);
                                    Complex c = Maths.CZERO;
                                    for (ModeInfo n : n_evan) {
                                        c = c.add(n.impedance.impedanceValue().mul(spp.get(n.index)).mul(spq.get(n.index).conj()));
                                    }
                                    b[p][q] = c;
                                    m0.inc(monMessage);
                                }
                            }
                        }
                    });

                    final Yoperator[] opValues = Yop(str2, mons[1]);
                    final ProgressMonitor m2 = ProgressMonitors.incremental(mons[2], (opValues.length * g.length * g.length));
                    Maths.invokeMonitoredAction(m2, monMessage + " (g x g x p)", new VoidMonitoredAction() {
                        @Override
                        public void invoke(ProgressMonitor monitor, String messagePrefix) throws Exception {
                            for (Yoperator opValue : opValues) {
                                Complex[][] op = opValue == null ? null : opValue.getMatrix().getArray();
                                if (op != null) {//op==null si k==1
                                    ModeInfo[] n_propa = opValue.getFn().getPropagatingModes();
                                    ComplexMatrix spc2 = Maths.scalarProductCache(g, opValue.getFn().arr(), str.getHintsManager().getHintAxisType().toAxisXY(), ProgressMonitors.none());
                                    for (int p = 0; p < g.length; p++) {
                                        ComplexVector spp2 = spc2.getRow(p);
                                        for (int q = 0; q < g.length; q++) {
                                            ComplexVector spq2 = spc2.getRow(q);
                                            Complex c = Maths.CZERO;
                                            for (int m = 0; m < op.length; m++) {
                                                for (int n = 0; n < op[m].length; n++) {
                                                    //#@ ID=1@20070423
                                                    //#@   OLD
//                                    Complex sp1 = spc2.fg(n, p);
//                                    Complex sp2 = spc2.gf(q, m);
//                                    c = c.add((zop[n_propa[m].index][n_propa[n].index]).multiply(sp1).multiply(sp2));
                                                    //#@   NEW
                                                    Complex sp1 = spp2.get(n_propa[n].index);
                                                    Complex sp2 = spq2.get(n_propa[m].index).conj();
                                                    c = c.add((op[m][n]).mul(sp1).mul(sp2));
                                                    //#@   END
                                                }
                                            }
                                            b[p][q] = b[p][q].add(c);
                                            m2.inc(monMessage);
                                        }
                                    }
                                }
                            }
                        }
                    });
                }

                return Maths.matrix(b);
            }
        });

    }

    private Yoperator[] Yop(final MomStructureFractalZop str2, ProgressMonitor cmonitor) {
        ProgressMonitor monitor = ProgressMonitors.nonnull(cmonitor);
        GpAdaptiveMesh gpAdaptatif = ((GpAdaptiveMesh) str2.getDirectGpTestFunctions());
        final FractalAreaGeometryList polygon = (FractalAreaGeometryList) gpAdaptatif.getPolygons(str2.getCircuitType());
        final Geometry[] transform = polygon.getTransform();
        final Yoperator[] ops = new Yoperator[transform.length];
        final Domain domain = str2.getDomain();
        int theK = str2.realK;
        final boolean isSimple0 = (theK) <= str2.getModelBaseKFactor();
        final boolean isSimple = (theK - 1) <= str2.getModelBaseKFactor();

        return Maths.invokeMonitoredAction(monitor, getClass().getSimpleName() + " Yop", new MonitoredAction<Yoperator[]>() {
            @Override
            public Yoperator[] process(ProgressMonitor monitor, String messagePrefix) throws Exception {
                if (isSimple0) {
                    return new Yoperator[0];
                } else {
                    for (int i = 0; i < transform.length; i++) {
                        MomStructure str = isSimple ? null : str2.clone();
                        if (str == null) {
                            str = new MomStructure();
                            str.load(str2);
                        }
                        str.setFractalScale(str2.realK - 1);
                        str.setDomain(polygon.getDomain(transform[i].getDomain(), domain));
                        if (isSimple) {
                            GpAdaptiveMesh gpAdaptatif2 = ((GpAdaptiveMesh) str.getTestFunctions());
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
                        if (i > 0 && str2.getHintsManager().isHint(MomStructureFractalZop.HINT_SUB_MODEL_EQUIVALENT)) {
                            cMatrix = ops[0].getMatrix();
                        } else {
                            cMatrix = str.self().evalMatrix().inv();
                        }
//                System.out.println("op"+(i+1)+" = " + cMatrix);
                        ops[i] = new Yoperator(cMatrix, str.getModeFunctions());
                        monitor.setProgress((i + 1.0) / transform.length, getClass().getSimpleName());
                    }
                    return ops;
                }
            }
        });
    }

    @Override
    public String toString() {
        return dump();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.function(getClass().getSimpleName()).build();
    }

}