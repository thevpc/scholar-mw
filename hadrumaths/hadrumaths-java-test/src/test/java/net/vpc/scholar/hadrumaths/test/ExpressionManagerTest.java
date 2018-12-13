package net.vpc.scholar.hadrumaths.test;

import net.vpc.common.jeep.ExprDoubleComplex;
import net.vpc.common.jeep.ExpressionManager;
import net.vpc.common.jeep.ExpressionEvaluatorResolver;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.expeval.ExpressionManagerFactory;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionManagerTest {

    /***
     * Main. To run the program in command line.
     * Usage: java MathEvaluator.main [your math expression]
     */
    @Test
    public void test1() {
        testExpr("(-x) - y");
        testExpr("((-x) - y) + 4î + (-x)");
        testExpr("(-4î) + (1 / 4î)");
        testExpr("(X * 1) + (Y * 0) + (î * sin((-104.79225109758409) * ((X * 1) + (Y * 0))))");
        testExpr("X * 1");
        testExpr("(X * cos(X + X)) ** (X * cos(X + X))");
        testExpr("X * Y * Z * X");
        testExpr("((4.8207259897778947E-17 - 2.6451351309118113E-16î) * (-22.360679774997898) *" +
                " cos((31.41592653589793 * X) - 1.5707963267948966) * domain(0->0.1, (-0.02)->0.02)) ** (cos((-104.79225109758409)" +
                " * ((X * 1) + (Y * 0))) + (î * sin((-104.79225109758409) * ((X * 1) + (Y * 0)))))");
    }


    private void testExpr(String expression) {
        ExpressionManager m = ExpressionManagerFactory.createParser();
        m.configureDefaults();
        m.declareBinaryOperators("**", "->");
        m.addResolver(new ExpressionEvaluatorResolver() {
            @Override
            public Object implicitConvertLiteral(Object literal, ExpressionManager evaluator) {
                if (literal instanceof ExprDoubleComplex) {
                    ExprDoubleComplex li = ((ExprDoubleComplex) literal);
                    return Complex.valueOf(li.getReal(), li.getImag());
                }
                return literal;
            }
        });
        m.declareVar("x", Double.class, Expr.class);
        m.declareVar("y", Double.class, Expr.class);
        Object evaluated = m.createEvaluator(expression).evaluate();
        System.out.println(expression);
        System.out.println(evaluated.toString());
        Assert.assertEquals(expression, evaluated.toString());
        //System.out.println(m.evaluate("(1+2()*3"));
    }
}
