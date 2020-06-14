package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.common.util.Chronometer;
import net.vpc.common.util.Collections2;
import net.vpc.common.util.DatePart;
import net.vpc.common.util.ListValueMap;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteCounter;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteLogger;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;

import static net.vpc.scholar.hadrumaths.Maths.*;
import net.vpc.scholar.hadrumaths.ScalarProductOperatorFactory;

public class TestSimplify2 {
    static {
        Config.setCacheEnabled(false);
    }

    @Test
    public void testSimplifySimple3() {
        String[] all = new String[]{
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 0) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 0) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 1) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 1) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 2) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 2) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 3) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 3) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (cos((((2 * 4) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995)) * cos((((2 * 4) + 1) * 3.141592653589793 * X) / 0.020863333333333338) * domain(0.0->0.010431666666666669, (-0.001406)->0.001406) * 261.1141991942995"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((1 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((2 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960354)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960354"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((3 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((1 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((2 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((3 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "((domain(0.0->7.86E-4, (-0.001406)->0.001406) * 672.6374890636885) ** (sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348)) * sin((4 * 3.141592653589793 * (X - 0.005690000000000001)) / 0.022760000000000002) * cos((4 * 3.141592653589793 * (Y + 0.0029945)) / 0.005989) * domain(0.005690000000000001->0.028450000000000003, (-0.0029945)->0.0029945) * 171.30368691960348"
                ,
                "real(((((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995)+(((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*1)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*1)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995)+(((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*2)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*2)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995)+(((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*3)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*3)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))-(domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885))"
                ,
                "2*pow((cos(X)*cos((Y-1.5707963267948966))+cos((X-1.5707963267948966))*cos(Y)),2)*domain((-Infinity)->Infinity,(-Infinity)->Infinity)"
                ,
                "2*pow((cos(X)*cos((Y-1.5707963267948966))+cos((X-1.5707963267948966))*cos(Y)),2)"
                ,
                "2*pow((cos(X)*cos((Y-1.5707963267948966))+cos((X-1.5707963267948966))*cos(Y)),2)"
        };
        ListValueMap<String, Expr> visited = Collections2.listValueMap();
        for (String es : all) {
            System.out.println(es);
            Expr e = ScalarProductOperatorFactory.formal().getSimplifier().rewriteOrSame(expr(es), null);
            visited.add(es, e);
        }
        System.out.println(all.length);
        System.out.println(visited.keySize());
        for (Map.Entry<String, List<Expr>> e : visited.multiValueEntrySet()) {
            if (e.getValue().size() > 1) {
                System.out.println(e.getKey() + ":");
                for (Expr expr : e.getValue()) {
                    System.out.println("\t" + expr);
                }
            }
        }
    }

    @Test
    public void testSimplifySimple4() {
        for (boolean boolVal : new boolean[]{false,true}) {
            Config.setCacheEnabled(boolVal);
            System.out.println("------------------------ CASE "+boolVal);
            Expr e = Maths.expr("real(((((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995)+(((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*1)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*1)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995)+(((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*2)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*2)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995)+(((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*3)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*3)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))-(domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885))");
//            Expr e = Maths.expr("((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))*cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)");
//            Expr e = Maths.expr("((domain(0.0->7.86E-4,(-0.001406)->0.001406)*672.6374890636885)**(cos((((2*0)+1)*3.141592653589793*X)/0.020863333333333338)*domain(0.0->0.010431666666666669,(-0.001406)->0.001406)*261.1141991942995))");
            ExpressionRewriter sp = ScalarProductOperatorFactory.formal().getSimplifier();
            ExprRewriteCounter counter = new ExprRewriteCounter();
            sp.addRewriteListener(ExprRewriteLogger.INSTANCE).addRewriteListener(counter);
            sp.rewriteOrSame(e, null);
            sp.removeRewriteListener(ExprRewriteLogger.INSTANCE);
            sp.removeRewriteListener(counter);
            System.out.println(counter);
            Chronometer chrono = chrono();
            int iterationsCount = 100000;
            for (int i = 0; i < iterationsCount; i++) {
                sp.rewriteOrSame(e, null);
            }
            chrono.stop();
            System.out.println(chrono.getDuration().toString(DatePart.NANOSECOND));
            System.out.println(chrono.getDuration().div(iterationsCount).toString(DatePart.NANOSECOND));
            //Count{newValue=24, bestEffort=68, unmodified=152, totalModified=92, total=244}
            // 6s 603ms  16us 498ns
            // 66us  30ns
            //Count{newValue=0, bestEffort=92, unmodified=98, totalModified=92, total=190}
            // 5s 844ms 816us 372ns
            // 58us 448ns
            //
            //-----------------------
            //Count{newValue=16, bestEffort=35, unmodified=63, totalModified=51, total=114}
            //169ms 558us 459ns
            //  1us 695ns
            //Count{newValue=0, bestEffort=51, unmodified=28, totalModified=51, total=79}
            //183ms 654us 516ns
            //  1us 836ns
        }
    }

    @Test
    public void testSimplifySimple1() {
        Expr e = Maths.expr(5.3);
        Expr e1 = e.simplify();
        Expr e2 = Complex.of(5.3);
        Assertions.assertEquals(e2, e1);
    }

    @Test
    public void testSimplifySimple2() {
        Expr e = Complex.of(5.3);
        Expr e1 = e.simplify();
        Expr e2 = Complex.of(5.3);
        Assertions.assertEquals(e2, e1);
    }

    @Test
    public void testSimplifyAdd1() {
        Expr e = expr("(0.5i+1)+(1+0.5i)");
        Expr e1 = e.simplify();
        Expr e2 = expr("2.0+1i").simplify();
        Assertions.assertEquals(e2, e1);
    }

    @Test
    public void testSimplifyAdd2() {
        Expr e = expr("X+(0.5i+1)+(1+0.5i)+(2*sin(Y)+X)+X+(X+2*sin(Y))");
        Expr actual = e.simplify();
        System.out.println(actual);
        Expr expected = expr("(4 * X) + (4 * sin(Y)) + (2+î)").simplify();
        boolean b = actual.equals(expected);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testSimplify2() {
        Maths.Config.setCacheEnabled(false);
        Expr e0 = expr("X*3*domain(0, 1, 0, 1)");
        Expr e1 = expr("cos(X*3)*cos(Y*3)*domain(0, 1, 0, 1)");
        Expr e2 = expr("cos(X*3)*cos(Y*3)");
        Expr e21 = e1.simplify();
        Expr e22 = mul(e1, e2).simplify();
        RewriteResult e1r = ScalarProductOperatorFactory.formal().getSimplifier().rewrite(e1, null);
        RewriteResult e2r = ScalarProductOperatorFactory.formal().getSimplifier().rewrite(e2, null);
        System.out.println(e0);
        System.out.println(e1r);
        System.out.println(e2r);
        //Assertions.assertEquals(e.simplify(),Complex.ONE);
//        Complex v = scalarProduct(e1, e2);
//        Assertions.assertEquals(v, Complex.valueOf(0.22725754890449057));
    }

    @Test
    public void testSimplify3() {
        Maths.Config.setCacheEnabled(false);
        Expr e1 = cos(X.mul(3)).mul(domain(0, 1, 0, 1));
        Expr e21 = e1.simplify();
        System.out.println(e21);
        Assertions.assertEquals("cos(3.0*X)*domain(0.0->1.0,0.0->1.0)", e21.toString());
    }

    @Test
    public void testSimplify4() {
        Maths.Config.setCacheEnabled(false);
        Expr e1 = cos(X.mul(3)).mul(cos(Y.mul(3))).mul(domain(0, 1, 0, 1));
        Expr e21 = Maths.Config.getIntegrationOperator().getSimplifier().rewriteOrSame(e1, null);
        System.out.println(e21);
        Assertions.assertEquals(("cos(3.0*X)*cos(3.0*Y)*domain(0.0->1.0,0.0->1.0)"), e21.toString());
    }

    @Test
    public void testSimplifyOpCount() throws IOException, ClassNotFoundException {
        ExprRewriteCounter.Count count = count(false, new Runnable() {
            @Override
            public void run() {
                Expr e1 = expr("cos(X + X + X)");
                System.out.println(e1);
                RewriteResult e2 = Config.getComputationSimplifier().rewrite(e1, null);
                System.out.println(e2);
            }
        });
        Assertions.assertEquals(3, count.getPartialModificationInvocationCount());
        Assertions.assertEquals(6, count.getUnmodifiedInvocationCount());
    }

    private ExprRewriteCounter.Count count(boolean withCache, Runnable r) {
        boolean oldWithCache = Config.isCacheEnabled();
        Maths.Config.setCacheEnabled(withCache);
        ExprRewriteLogger ok = new ExprRewriteLogger();
        ExprRewriteCounter counter = new ExprRewriteCounter();
        Maths.Config.getComputationSimplifier()
                .addRewriteListener(ok)
                .addRewriteListener(counter);
        try {
            r.run();
        } finally {
            Maths.Config.getComputationSimplifier()
                    .removeRewriteListener(ok)
                    .removeRewriteListener(counter)
            ;
            Maths.Config.setCacheEnabled(oldWithCache);
        }
        System.out.println(counter.getCount());
        return counter.getCount();
    }

//    @Test
//    public void testSimplifyOpCount3() throws IOException, ClassNotFoundException {
//        Maths.Config.setCacheEnabled(true);
//        ExprRewriteSuccessLogger ll = new ExprRewriteSuccessLogger();
//        Maths.Config.getIntegrationOperator().getExpressionRewriter().addRewriteSuccessListener(ll);
//        Expr e1=(Expr) IOUtils.loadObject("/home/vpc/a.ser");
////        Expr e1=cos(X+X);
////        Expr e1=cos(X+X+X);
//
//        System.out.println(e1);
//        Expr e21 = Maths.Config.getIntegrationOperator().getExpressionRewriter().rewriteOrSame(e1);
//        System.out.println(e21);
//        System.out.println(ll.getInvocationCount());
////        Assertions.assertEquals(3,ll.getInvocationCount());
//    }

    @Test
    public void testSimplifyOpCount2() throws IOException, ClassNotFoundException {
        ExprRewriteCounter.Count count = count(true, new Runnable() {
            @Override
            public void run() {
                Expr e1 = expr("cos(X + X + X)");
                System.out.println(e1);
                RewriteResult e2 = Config.getComputationSimplifier().rewrite(e1, null);
                System.out.println(e2);
            }
        });
        System.out.println(count);
//        Assertions.assertEquals(8, count.getTotalInvocationCount());
//        Assertions.assertEquals(3, count.getPartialModificationInvocationCount());
//        Assertions.assertEquals(5, count.getUnmodifiedInvocationCount());
        Assertions.assertEquals(4, count.getTotalInvocationCount());
        Assertions.assertEquals(0, count.getPartialModificationInvocationCount());
        Assertions.assertEquals(2, count.getUnmodifiedInvocationCount());
    }

}
