package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.random.ErrorList;
import net.vpc.scholar.hadrumaths.random.ValDiffHelper;
import net.vpc.scholar.hadrumaths.symbolic.CustomDDFunctionX;
import net.vpc.scholar.hadruplot.Plot;
import org.junit.jupiter.api.Test;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class TestExpressions2 {
    private ValDiffHelper vdh = new ValDiffHelper(new ErrorList());

    @Test
    public void testDDXMany() throws InterruptedException{
        testDDX(new ExprPair(
                new CustomDDFunctionX() {
                    @Override
                    public double eval(double x) {
                        return Math.sin(x) + Math.cos(x) + Math.tan(x) + 1 / Math.tan(x);
                    }
                },
                add(sin(add(mul(expr(2),expr(0.0)),X)), cos(X), tan(X), cotan(X)),
                Domain.ofBounds(PI / 10, 3 * PI / 10)
        ));
        testDDX(new ExprPair(
                new CustomDDFunctionX() {
                    @Override
                    public double eval(double x) {
                        return Math.sin(x) + Math.cos(x) + Math.tan(x) + 1 / Math.tan(x);
                    }
                },
                add(sin(add(mul(expr(2),expr(0.0)),X)), cos(X), tan(X), cotan(X)).simplify(),
                Domain.ofBounds(PI / 10, 3 * PI / 10)
        ));
        Thread.sleep(100000);
    }

    public void testDDX(ExprPair a) throws InterruptedException {
        if(a.j instanceof CustomDDFunctionX) {
            double[] xs = a.d.xtimes(3);
            double[] v1 = a.e.toDD().evalDouble(xs);
            double[] v2 = new double[xs.length];
            for (int i = 0; i < xs.length; i++) {
                v2[i] = ((CustomDDFunctionX)a.j).eval(xs[i]);
            }
            vdh.checkAndSay(v1, v2, xs, a.e, a.d);
            Plot.plot(v1);
            Plot.plot(v2);
        }
    }

    class ExprPair<T> {
        private T j;
        private Expr e;
        private Domain d;

        public ExprPair(T j, Expr e, Domain d) {
            this.j = j;
            this.e = e;
            this.d = d;
        }
    }
}
