package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.Plot;
import net.vpc.scholar.hadrumaths.ScalarProductOperatorFactory;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import org.junit.Assert;
import org.junit.Test;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class SimplifyTest {
    @Test
    public void test2(){
    }
    @Test
    public void test1(){
        FormalScalarProductOperator o=(FormalScalarProductOperator) ScalarProductOperatorFactory.formal();
        Expr simplfied = o.getExpressionRewriter().rewrite(sin(X)).getValue();
        String s = simplfied.toString();
        Expr f1= mul(X , expr(3), expr(6) , Y);
        Expr f2= mul(X , expr(18), Y);
        System.out.println(f1);
        System.out.println(f2);
        Assert.assertEquals(simplify(f1),f2);
    }
}
