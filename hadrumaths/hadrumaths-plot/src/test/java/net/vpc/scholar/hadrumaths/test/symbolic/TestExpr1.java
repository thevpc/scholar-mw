package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.DefaultComplexValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static net.vpc.scholar.hadrumaths.Maths.*;

public class TestExpr1 {

    @Test
    public void testMul() {
        Expr e=prod(X,X,expr(2));
        System.out.println(e);
    }

    @Test
    public void testCompare() {
        DoubleParam p=param("p");
        DoubleParam q=param("q");
        Expr e=or(p.ne(0),q.ne(0));
        Assertions.assertEquals(e.setParam(p,2).setParam(q,2).simplify().toString(),"1");
        Assertions.assertEquals(e.setParam(p,0).setParam(q,0).simplify().toString(),"0");
    }

    @Test
    public void testDomain() {
        Expr d = Domain.ofBounds(-5, 6);
        TestExprHelper.checkDDApply(d, new double[]{-6, -5, 0, 6, 7}, new double[]{0, 1, 1, 0, 0});
        TestExprHelper.checkDDApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[][]{{0, 1, 0}, {0, 1, 0}, {0, 1, 0}});
        TestExprHelper.checkDDApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[][][]{
                {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}},
                {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}},
                {{0, 1, 0}, {0, 1, 0}, {0, 1, 0}}
        });
        Assertions.assertFalse(d.isNaN());
        Assertions.assertFalse(d.isZero());
        Assertions.assertFalse(d.isInfinite());
        Assertions.assertTrue(d.isInvariant(Axis.X));
        Assertions.assertTrue(d.isInvariant(Axis.Y));
        Assertions.assertTrue(d.isInvariant(Axis.Z));
        Assertions.assertTrue(d.isSmartMulDouble());
        Assertions.assertTrue(d.isSmartMulComplex());
        Assertions.assertTrue(d.isSmartMulDomain());
        TestExprHelper.checkIs(d, ExprType.DOUBLE_EXPR, ExprType.DOUBLE_EXPR);
        TestExprHelper.checkNarrowNo(d, ExprType.DOUBLE_NBR, ExprType.COMPLEX_NBR, ExprType.CVECTOR_NBR, ExprType.CMATRIX_NBR);
    }

    @Test
    public void testDoubleValueImpl() {
        double cst = 3;
        Expr d = Maths.expr(cst, Domain.ofBounds(-5, 6));
        TestExprHelper.checkDDApply(d, new double[]{-6, -5, 0, 6, 7}, new double[]{0, cst, cst, 0, 0});
        TestExprHelper.checkDDApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[][]{{0, cst, 0}, {0, cst, 0}, {0, cst, 0}});
        TestExprHelper.checkDDApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[][][]{
                {{0, cst, 0}, {0, cst, 0}, {0, cst, 0}},
                {{0, cst, 0}, {0, cst, 0}, {0, cst, 0}},
                {{0, cst, 0}, {0, cst, 0}, {0, cst, 0}}
        });
        Assertions.assertFalse(d.isNaN());
        Assertions.assertFalse(d.isZero());
        Assertions.assertFalse(d.isInfinite());
        Assertions.assertTrue(d.isInvariant(Axis.X));
        Assertions.assertTrue(d.isInvariant(Axis.Y));
        Assertions.assertTrue(d.isInvariant(Axis.Z));
        Assertions.assertTrue(d.isSmartMulDouble());
        Assertions.assertTrue(d.isSmartMulComplex());
        Assertions.assertTrue(d.isSmartMulDomain());
        TestExprHelper.checkIs(d, ExprType.DOUBLE_EXPR, ExprType.DOUBLE_EXPR);
        TestExprHelper.checkNarrowNo(d, ExprType.DOUBLE_NBR, ExprType.COMPLEX_NBR, ExprType.CVECTOR_NBR, ExprType.CMATRIX_NBR);
    }

    @Test
    public void testComplexValueImpl() {
        Complex cst = Complex.of(3);
        Complex z = Complex.ZERO;
        Expr d = new DefaultComplexValue(cst, Domain.ofBounds(-5, 6));
        TestExprHelper.checkDCApply(d, new double[]{-6, -5, 0, 6, 7}, new Complex[]{z, cst, cst, z, z});
        TestExprHelper.checkDCApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new Complex[][]{{z, cst, z}, {z, cst, z}, {z, cst, z}});
        TestExprHelper.checkDCApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new Complex[][][]{
                {{z, cst, z}, {z, cst, z}, {z, cst, z}},
                {{z, cst, z}, {z, cst, z}, {z, cst, z}},
                {{z, cst, z}, {z, cst, z}, {z, cst, z}}
        });
        Assertions.assertFalse(d.isNaN());
        Assertions.assertFalse(d.isZero());
        Assertions.assertFalse(d.isInfinite());
        Assertions.assertTrue(d.isInvariant(Axis.X));
        Assertions.assertTrue(d.isInvariant(Axis.Y));
        Assertions.assertTrue(d.isInvariant(Axis.Z));
        Assertions.assertTrue(d.isSmartMulDouble());
        Assertions.assertTrue(d.isSmartMulComplex());
        Assertions.assertTrue(d.isSmartMulDomain());
        TestExprHelper.checkIs(d, ExprType.COMPLEX_EXPR, ExprType.COMPLEX_EXPR);
        TestExprHelper.checkNarrowNo(d, ExprType.DOUBLE_NBR, ExprType.COMPLEX_NBR, ExprType.CVECTOR_NBR, ExprType.CMATRIX_NBR, ExprType.DOUBLE_EXPR, ExprType.DOUBLE_DOUBLE);
    }

    @Test
    public void testComplexValueImpl2() {
        Complex cst = Complex.of(3);
        Complex z = Complex.ZERO;
        Expr d = new DefaultComplexValue(cst, Domain.ofBounds(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        TestExprHelper.checkDCApply(d, new double[]{-6, -5, 0, 6, 7}, new Complex[]{cst, cst, cst, cst, cst});
        TestExprHelper.checkDCApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new Complex[][]{{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}});
        TestExprHelper.checkDCApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new Complex[][][]{
                {{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}},
                {{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}},
                {{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}}
        });
        Assertions.assertFalse(d.isNaN());
        Assertions.assertFalse(d.isZero());
        Assertions.assertFalse(d.isInfinite());
        Assertions.assertTrue(d.isInvariant(Axis.X));
        Assertions.assertTrue(d.isInvariant(Axis.Y));
        Assertions.assertTrue(d.isInvariant(Axis.Z));
        Assertions.assertTrue(d.isSmartMulDouble());
        Assertions.assertTrue(d.isSmartMulComplex());
        Assertions.assertTrue(d.isSmartMulDomain());
        TestExprHelper.checkIs(d, ExprType.COMPLEX_EXPR, ExprType.DOUBLE_NBR);
        TestExprHelper.checkNarrowNo(d);
    }

    @Test
    public void testComplex() {
        Complex cst = Complex.of(3);
        Complex z = Complex.ZERO;
        Expr d = cst;
        TestExprHelper.checkDCApply(d, new double[]{-6, -5, 0, 6, 7}, new Complex[]{cst, cst, cst, cst, cst});
        TestExprHelper.checkDCApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new Complex[][]{{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}});
        TestExprHelper.checkDCApply(d, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new double[]{-6, 0, 6}, new Complex[][][]{
                {{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}},
                {{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}},
                {{cst, cst, cst}, {cst, cst, cst}, {cst, cst, cst}}
        });
        Assertions.assertFalse(d.isNaN());
        Assertions.assertFalse(d.isZero());
        Assertions.assertFalse(d.isInfinite());
        Assertions.assertTrue(d.isInvariant(Axis.X));
        Assertions.assertTrue(d.isInvariant(Axis.Y));
        Assertions.assertTrue(d.isInvariant(Axis.Z));
        Assertions.assertTrue(d.isSmartMulDouble());
        Assertions.assertTrue(d.isSmartMulComplex());
        Assertions.assertTrue(d.isSmartMulDomain());
        TestExprHelper.checkIs(d, ExprType.COMPLEX_NBR, ExprType.DOUBLE_NBR);
        TestExprHelper.checkNarrowNo(d);
    }

}
