package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteCounter;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteLogger;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import org.junit.Test;

import java.io.IOException;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class TestSimplify {

    @Test
    public void testSimplify1() {
        Expr e = mul(Complex.ONE, Complex.ONE);
        Assert.assertEquals(e.simplify(), Complex.ONE);
    }

    @Test
    public void testSimplify2() {
        Maths.Config.setCacheEnabled(false);
        System.out.println(X.mul(3).mul(xydomain(0, 1, 0, 1)).simplify());
        Expr e1 = cos(X.mul(3)).mul(cos(Y.mul(3))).mul(xydomain(0, 1, 0, 1));
        Expr e2 = cos(X.mul(3)).mul(cos(Y.mul(3)));
        Expr e21 = e1.simplify();
        Expr e22 = mul(e1, e2).simplify();
        //Assert.assertEquals(e.simplify(),Complex.ONE);
        Complex v = scalarProduct(e1, e2);
        Assert.assertEquals(v, Complex.valueOf(0.22725754890449057));
    }

    @Test
    public void testSimplify3() {
        Maths.Config.setCacheEnabled(false);
        Expr e1 = cos(X.mul(3)).mul(xydomain(0, 1, 0, 1));
        Expr e21 = e1.simplify();
        System.out.println(e21);
        Assert.assertEquals(("cos((3.0 * X * domain(0->1, 0->1)))"), e21.toString());
    }

    @Test
    public void testSimplify4() {
        Maths.Config.setCacheEnabled(false);
        Expr e1 = cos(X.mul(3)).mul(cos(Y.mul(3))).mul(xydomain(0, 1, 0, 1));
        Expr e21 = Maths.Config.getIntegrationOperator().getExpressionRewriter().rewriteOrSame(e1);
        System.out.println(e21);
        Assert.assertEquals(("cos(3.0 * X) * cos(3.0 * Y) * domain(0->1, 0->1)"),e21.toString());
    }

    @Test
    public void testSimplifyOpCount() throws IOException, ClassNotFoundException {
        ExprRewriteCounter.Count count = count(false, new Runnable() {
            @Override
            public void run() {
                Expr e1 = cos(X + X + X);
                System.out.println(e1);
                RewriteResult e2 = Config.getComputationSimplifier().rewrite(e1);
                System.out.println(e2);
            }
        });
        Assert.assertEquals(3, count.getPartialModificationInvocationCount());
        Assert.assertEquals(10, count.getUnmodifiedInvocationCount());
    }

    @Test
    public void testSimplifyOpCount2() throws IOException, ClassNotFoundException {
        ExprRewriteCounter.Count count = count(true, new Runnable() {
            @Override
            public void run() {
                Expr e1 = cos(X + X + X);
                System.out.println(e1);
                RewriteResult e2 = Config.getComputationSimplifier().rewrite(e1);
                System.out.println(e2);
            }
        });
        System.out.println(count);
        Assert.assertEquals(3, count.getPartialModificationInvocationCount());
        Assert.assertEquals(6, count.getUnmodifiedInvocationCount());
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
////        Assert.assertEquals(3,ll.getInvocationCount());
//    }

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

}
