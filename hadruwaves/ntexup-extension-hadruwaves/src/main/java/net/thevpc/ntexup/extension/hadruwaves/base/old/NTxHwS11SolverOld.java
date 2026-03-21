//package net.thevpc.ntexup.extension.hadruwaves.base.old;
//
//import net.thevpc.ntexup.extension.hadruwaves.MoMStrSimulationQuery;
//import net.thevpc.ntexup.extension.mwsimulator.NTxStrSimulationQuery;
//import net.thevpc.ntexup.extension.mwsimulator.NTxTargetSolver;
//import net.thevpc.scholar.hadrumaths.*;
//import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector;
//import net.thevpc.scholar.hadruplot.Plot;
//import net.thevpc.scholar.hadruwaves.mom.MomStructure;
//
//import java.util.Arrays;
//
//import static net.thevpc.scholar.hadrumaths.Maths.matrix;
//
//public class NTxHwS11SolverOld implements NTxTargetSolver {
//    @Override
//    public Object solve(NTxStrSimulationQuery query) {
//        Maths.Config.setCacheEnabled(false);
//        MomStructure str = ((MoMStrSimulationQuery) query).str;
//        String sfx = String.valueOf(str.getFrequency());
//
//        String prefix = "[freq " + sfx + "][modes="+str.modes().length+"]";
//
//        Plot.title("zmn "+ prefix).asHeatMap().plot(
//                Arrays.asList(str.getModes())
//                        .stream().map(m->m.impedance.impedance().get()).toArray(Complex[]::new)
//        );//.display();
//        Arrays.asList(str.getModes())
//                .stream().limit(10).forEach(m->{
//                    System.out.println(prefix+m.mode+":: zmn="+m.impedance.impedance().get());
//                });
//        ComplexMatrix testModeScalarProducts = str.getTestModeScalarProducts();
//        DoubleToVector[] mfa = str.modeFunctions().arr();
//        DoubleToVector[] tfa = str.testFunctions().arr();
//        for (int mn = 0; mn < 5; mn++) {
//            for (int pq = 0; pq < tfa.length; pq++) {
//                Complex sp = testModeScalarProducts.get(pq, mn);
//                System.out.println(prefix+":: <"+str.modeFunctions().mode(mn).mode+",g"+pq+">="+sp);
//            }
//        }
//        System.out.println(prefix + ":: (First=Bottom)BoxSpace=<" + str.getFirstBoxSpace());
//        System.out.println(prefix+":: (Second=Upper)BoxSpace=<"+str.getSecondBoxSpace());
//        System.out.println(prefix+":: (BoxSize)BoxSpace=<"+str.getDomain());
//
//        ComplexMatrix A = str.matrixA().evalMatrix();
//        System.out.println(prefix+":: A[0,0]="+A.get(0,0));
//        System.out.println(prefix+":: <g0,g0>="+Maths.scalarProduct(tfa[0],tfa[0]));
//        System.out.println(prefix+":: norm(g0)="+Maths.norm(tfa[0]));
//
//        Plot.title("<fn,gp> "+ prefix).asMatrix().plot(matrix(testModeScalarProducts));//.display();
//        Plot.title("JY "+ prefix).asMatrix().plot(matrix(str.current().evalMatrix(Axis.Y, str.domain().times(200))));//.display();
//        Plot.title("JX "+ prefix).asMatrix().plot(matrix(str.current().evalMatrix(Axis.X, str.domain().steps(200))));//.display();
//        Plot.title("A "+ prefix).asMatrix().plot(A);//.display();
//        Plot.title("B "+ prefix).asMatrix().plot(str.matrixB().evalMatrix());//.display();
//        Plot.title("X "+ prefix).asMatrix().plot(str.matrixX().evalMatrix());//.display();
//        Complex complex = str.sparameters().evalComplex();
//        System.out.println("NTxHwS11Solver:"+ prefix +"::"+complex);
//        return complex;
//    }
//}
