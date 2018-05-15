package net.vpc.scholar.hadrumaths.test;

import junit.framework.Assert;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteFailLogger;
import net.vpc.scholar.hadrumaths.transform.ExprRewriteLogger;
import org.junit.Test;

import java.io.IOException;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class TestSimplify {

    @Test
    public void testSimplify1(){
        Expr e= mul(Complex.ONE,Complex.ONE);
        Assert.assertEquals(e.simplify(),Complex.ONE);
    }

    @Test
    public void testSimplify2(){
        Maths.Config.setCacheEnabled(false);
        System.out.println(X.mul(3).mul(xydomain(0,1,0,1)).simplify());
        Expr e1= cos(X.mul(3)).mul(cos(Y.mul(3))).mul(xydomain(0,1,0,1));
        Expr e2= cos(X.mul(3)).mul(cos(Y.mul(3)));
        Expr e21 = e1.simplify();
        Expr e22 = mul(e1, e2).simplify();
        //Assert.assertEquals(e.simplify(),Complex.ONE);
        Complex v = scalarProduct(e1, e2);
        Assert.assertEquals(v,Complex.valueOf(0.22725754890449057));
    }

    @Test
    public void testSimplify3(){
        Maths.Config.setCacheEnabled(false);
        Expr e1= cos(X.mul(3)).mul(xydomain(0,1,0,1));
        Expr e21 = e1.simplify();
        System.out.println(e21);
        Assert.assertEquals(("cos(3 * domain(0->1, 0->1) * X)"),e21.toString());
    }

    @Test
    public void testSimplify4(){
        Maths.Config.setCacheEnabled(false);
        Expr e1= cos(X.mul(3)).mul(cos(Y.mul(3))).mul(xydomain(0,1,0,1));
        Expr e21 = Maths.Config.getIntegrationOperator().getExpressionRewriter().rewriteOrSame(e1);
        System.out.println(e21);
//        Assert.assertEquals(e21.toString(),("cos((3.0 * X * domain(0->1, 0->1)))"));
    }

//    @Test
//    public void testSimplify5() throws IOException, ClassNotFoundException {
//        Maths.Config.setCacheEnabled(false);
//        Expr e1=(Expr) IOUtils.loadObject("/home/vpc/a.ser");
//        Maths.Config.getIntegrationOperator().getExpressionRewriter().addRewriteListener(new ExprRewriteListener() {
//            @Override
//            public void onRewriteExpr(Expr oldValue, Expr newValue) {
//                System.out.println(oldValue.getClass().getSimpleName()+"->"+newValue.getClass().getSimpleName()+"  :: "+oldValue+"\n\t\t"+newValue);
//            }
//        });
//        Expr e21 = Maths.Config.getIntegrationOperator().getExpressionRewriter().rewriteOrSame(e1);
//        System.out.println(e21);
////        Assert.assertEquals(e21.toString(),("cos((3.0 * X * domain(0->1, 0->1)))"));
//    }

    @Test
    public void testSimplifyOpCount() throws IOException, ClassNotFoundException {
        Maths.Config.setCacheEnabled(false);
        ExprRewriteLogger ll = new ExprRewriteLogger();
        Maths.Config.getIntegrationOperator().getExpressionRewriter().addRewriteListener(ll);
//        Expr e1=(Expr) IOUtils.loadObject("/home/vpc/a.ser");
//        Expr e1=cos(X+X);
        Expr e1=cos(X+X+X);

        System.out.println(e1);
        Expr e21 = Maths.Config.getIntegrationOperator().getExpressionRewriter().rewriteOrSame(e1);
        System.out.println(e21);
        System.out.println(ll.getInvocationCount());
        Assert.assertEquals(5,ll.getInvocationCount());
    }

    @Test
    public void testSimplifyOpCount2() throws IOException, ClassNotFoundException {
        Maths.Config.setCacheEnabled(true);
        ExprRewriteLogger ok = new ExprRewriteLogger();
        ExprRewriteFailLogger ko = new ExprRewriteFailLogger();
        Maths.Config.getIntegrationOperator().getExpressionRewriter()
                .addRewriteListener(ok)
                .addRewriteFailListener(ko);
//        Expr e1=(Expr) IOUtils.loadObject("/home/vpc/a.ser");
//        Expr e1=cos(X+X);
        Expr e1=cos(X+X+X);

        System.out.println(e1);
        Expr e21 = Maths.Config.getIntegrationOperator().getExpressionRewriter().rewriteOrSame(e1);
        System.out.println(e21);
        System.out.println(ok.getInvocationCount()+"/"+ko.getInvocationCount());
        Assert.assertEquals(3,ok.getInvocationCount());
    }

//    @Test
//    public void testSimplifyOpCount3() throws IOException, ClassNotFoundException {
//        Maths.Config.setCacheEnabled(true);
//        ExprRewriteLogger ll = new ExprRewriteLogger();
//        Maths.Config.getIntegrationOperator().getExpressionRewriter().addRewriteListener(ll);
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

}
