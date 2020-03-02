package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteCounter;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteLogger;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class TestSimplify {

    @Test
    public void testSimplify1() {
        Expr e = mul(Complex.ONE, Complex.ONE);
        Assertions.assertEquals(Complex.ONE, e.simplify());
    }

    @Test
    public void testSimplify5() {
        Complex c1 = Complex.of(5.221429478319327E-20, -3.082759711126352E-19);
        System.out.println(c1.toString());
        Expr e = mul(c1, new DefaultComplexValue(Complex.ZERO));
//        String str = e.toString();
        System.out.println(e);
        Assertions.assertEquals(e.simplify(), Complex.ZERO);
    }

    @Test
    public void testSimplify2() {
        Maths.Config.setCacheEnabled(false);
//        System.out.println(X.mul(3).mul(domain(0, 1, 0, 1)).simplify());
        Expr e1 = cos(X.mul(3)).mul(cos(Y.mul(3))).mul(domain(0, 1, 0, 1));
        Expr e2 = cos(X.mul(3)).mul(cos(Y.mul(3)));
        Expr e21 = e1.simplify();
        Expr e22 = e2.simplify();
        System.out.println("e22 = " + e22);
        Expr e12 = mul(e1, e2).simplify();
        //Assertions.assertEquals(e.simplify(),Complex.ONE);
        Complex v = scalarProduct(e1, e2).toComplex();
        Assertions.assertEquals(v, Complex.of(0.22725754890449057));
    }

    @Test
    public void testSimplify3() {
        Maths.Config.setCacheEnabled(false);
        Expr e1 = cos(X.mul(3)).mul(domain(0, 1, 0, 1));
        Expr e21 = e1.simplify();
        System.out.println(e21);
        Assertions.assertEquals(("cos(3.0*X)*domain(0.0->1.0,0.0->1.0)"), e21.toString());
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
//        Assertions.assertEquals(3, count.getPartialModificationInvocationCount());
//        Assertions.assertEquals(6, count.getUnmodifiedInvocationCount());
//        Assertions.assertEquals(1, count.getPartialModificationInvocationCount());
//        Assertions.assertEquals(7, count.getUnmodifiedInvocationCount());
        Assertions.assertEquals(0, count.getPartialModificationInvocationCount());
        Assertions.assertEquals(4, count.getUnmodifiedInvocationCount());
    }

    private ExprRewriteCounter.Count count(boolean withCache, Runnable r) {
        boolean oldWithCache = Config.isCacheEnabled();
        Maths.Config.setCacheEnabled(withCache);
        ExprRewriteCounter counter = new ExprRewriteCounter();
        Maths.Config.getComputationSimplifier()
                .addRewriteListener(ExprRewriteLogger.INSTANCE)
                .addRewriteListener(counter);
        try {
            r.run();
        } finally {
            Maths.Config.getComputationSimplifier()
                    .removeRewriteListener(ExprRewriteLogger.INSTANCE)
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
//        Assertions.assertEquals(6, count.getTotalInvocationCount());
//        Assertions.assertEquals(1, count.getPartialModificationInvocationCount());
//        Assertions.assertEquals(4, count.getUnmodifiedInvocationCount());
        Assertions.assertEquals(4, count.getTotalInvocationCount());
        Assertions.assertEquals(0, count.getPartialModificationInvocationCount());
        Assertions.assertEquals(2, count.getUnmodifiedInvocationCount());
    }

}
