package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.common.jeep.*;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.expeval.ExpressionManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class JeepTest {

    /***
     * Main. To run the program in command line.
     * Usage: java MathEvaluator.main [your math expression]
     */
    @Test
    public void test1() {
//        testExpr("(-x) - y");
//        testExpr("((-x) - y) + 4î + (-x)");
//        testExpr("(-4î) + (1 / 4î)");
//        testExpr("(X * 1) + (Y * 0) + (î * sin((-104.79225109758409) * ((X * 1) + (Y * 0))))");
//        testExpr("X * 1");
//        testExpr("(X * cos(X + X)) ** (X * cos(X + X))");
//        testExpr("X * Y * Z * X");
//        testExpr("domain(0, 1,2)");
//        testExpr("domain(0.0->0.1,-0.02->0.02,-0.02->0.02)");
//        testExpr("domain(0.0->0.1,-0.02->0.02)");
//        testExpr("(4.8207259897778947E-17-2.6451351309118113E-16î)*(-22.360679774997898)*" +
//                "cos((31.41592653589793*X)-1.5707963267948966)*(domain(0.0->0.1,(-0.02)->0.02)**(cos((-104.79225109758409)" +
//                "*((X*1)+(Y*0)))+(î*sin((-104.79225109758409)*((X*1)+(Y*0))))))");
        testExpr("{X+1}");
    }


    private void testExpr(String expression) {
        JContext m = ExpressionManagerFactory.createParser();
        m.resolvers().addResolver(new JResolver() {
            @Override
            public Object implicitConvertLiteral(Object literal, JContext context) {
//                if (literal instanceof JComplex64) {
//                    JComplex64 li = ((JComplex64) literal);
//                    return Complex.of(li.real(), li.imag());
//                }
                return literal;
            }
        });
        m.vars().declareVar("x", Expr.class, Maths.param("x"));
        m.vars().declareVar("y", Expr.class, Maths.param("y"));
        Object evaluated = m.evaluate(expression);
        System.out.println(expression);
        System.out.println(evaluated.toString());
        Assertions.assertEquals(expression, evaluated.toString());
        //System.out.println(m.evaluate("(1+2()*3"));
    }
}
