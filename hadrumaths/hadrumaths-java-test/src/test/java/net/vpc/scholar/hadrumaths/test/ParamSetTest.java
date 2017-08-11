package net.vpc.scholar.hadrumaths.test;

/**
 * Created by vpc on 8/6/16.
 */
public class ParamSetTest {
    //    public static void main(String[] args) {
//        ConvergenceParam<Object> d1 = new DefaultConvergenceParam<Object>("D1") {
//            @Override
//            public Object next(Object o, Object varValue, int index, Map<String, Object> currentParams) {
//                double d = (Double) varValue;
//                double v = 1;//+ 1.0 / d;
//                System.out.println("updating D1 value " + v);
//                return v;
//            }
//        };
//        ConvergenceParam<Object> d2 = new DefaultConvergenceParam<Object>("D2") {
//            @Override
//            public Object next(Object o, Object varValue, int index, Map<String, Object> currentParams) {
//                double d = (Double) varValue;
//                double v = 1;//- 0.5 / d;
//                System.out.println("updating D2 value " + v);
//                return v;
//            }
//        };
//        ObjectEvaluator<Object, Double> evaluator = new ObjectEvaluator<Object, Double>() {
//            @Override
//            public Double evaluate(Object o, ProgressMonitor monitor) {
//                return (Double) o;
//            }
//        };
//
//        ConvergenceEvaluator<Object> c = ConvergenceEvaluator.create(d2, Maths.dsteps(1, 1000, 1))
//                .setMaxIterations(3)
//                .combine(d1, Maths.dsteps(1, 1000, 1));
//        ConvergenceResult v = c.evaluate(new Object(), evaluator, ProgressMonitorFactory.out());
//        System.out.println(v);
//    }

}
