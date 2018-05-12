package net.vpc.scholar.hadrumaths.test.util;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.IOUtils;

import java.util.Arrays;

public class TestVals {
    public static void main(String[] args) {
        Maths.Config.setCacheEnabled(false);
//        Domain dd1 = Maths.xyzdomain(-5, 5, -5, 5, -5, 5);
//        Domain dd2 = Maths.xyzdomain(3, 8, -3, 3, -5, 5);
//        DoubleToDouble exp=new OrExpr(dd1,dd2);
//        System.out.println("Expr Domain:"+exp.getDomain());
//        ParametrizedScalarProduct s=new ParametrizedScalarProduct(Domain.ZEROX,Domain.ZEROX,true);
//        System.out.println(s.isDD());
//        System.out.println(s.isDC());
//        System.out.println(s.isDM());
//        System.out.println(s.getDomainDimension());
//        System.out.println(s.getComponentDimension());
//        if(true){
//            return;
//        }
        DoubleToDouble exp = (DoubleToDouble) IOUtils.loadObject2("~/a.pbm");
        exp=exp.getSubExpressions().get(0).toDD();
//        boolean dd = exp.isDD();
//        System.out.println(exp);
//        double vv=exp.computeDouble(3,3,3);
//        System.out.println(vv);
//        if(true){
//            return;
//        }
//        exp=exp.getSubExpressions().get(0).toDD();
//        exp=exp.getSubExpressions().get(0).getSubExpressions().get(0).toDD();
//        DoubleToDouble exp = new Mul(new Asinh(new Cosh(Maths.X)),Maths.xydomain(0,1,0,1)).toDD();
        if (exp != null) {
            if (exp.isInvariant(Axis.Z)) {
                checkDoubleToDoubleXY(exp, false, true);
            }
            checkDoubleToDoubleXYZ(exp, false, true);
        } else {
            ExprGenerator g = new ExprGenerator()
                    .setAcceptDomain1(true).setAcceptDomain2(false).setAcceptDomain3(false)
                    .setExpressionComplexity(3)
                    .setAcceptBoundaryValue(false).setAcceptNaNValue(false)
                    .setAcceptInfDomain(false);
//            Sqrtn ss = g.generate(Sqrtn.class);
//            boolean dd = ss.isDD();
            for (int i = 0; i < 1000; i++) {
                DoubleToDouble d = g.generateDD();
                System.out.println(i + " :: " + d);
                if (!d.isDD()) {
                    System.out.println("Why");
                }
                Expr ee = d.toDD();
                if (!ee.isDD()) {
                    ee.isDD();
                    ee.toDD();
                }
                DoubleToDouble e = null;
                try {
                    e = d.mul(Maths.xyzdomain(-5, 5, -5, 5, -5, 5)).toDD();
                } catch (Exception ex) {
                    e = d.mul(Maths.xyzdomain(-5, 5, -5, 5, -5, 5)).toDD();

                }
                if (e.isInvariant(Axis.Z)) {
                    checkDoubleToDoubleXY(e, true, false);
                }
                checkDoubleToDoubleXYZ(e, true, false);
            }
        }

    }

    public static void checkDoubleToDoubleXYZ(DoubleToDouble e, boolean saveError, boolean showSuccess) {
        IllegalArgumentException exx = null;
        try {
            for (ParamExpr p : e.getParams()) {
                if (p.isDoubleTyped()) {
                    e = e.setParam(p, 3.6).toDD();
                }
            }
            System.out.println("EXPRESSION= " + e);
            Domain d = e.getDomain();
            Domain domain = d.intersect(Maths.xydomain(-10, 10, -10, 10)).expandAll(5, 5);
            AbsoluteSamples relative = domain.toAbsolute(Samples.relative(21, 21, 5));
            double[] x = relative.getX();
            double[] y = relative.getY();
            double[] z = relative.getZ();
            double[][][] doublesA = e.computeDouble(x, y, z);
            double[][][] doublesB = new double[z.length][y.length][x.length];
            normalize(doublesA);
            for (int i = 0; i < z.length; i++) {
                for (int j = 0; j < y.length; j++) {
                    for (int k = 0; k < x.length; k++) {
                        doublesB[i][j][k] = e.computeDouble(x[k], y[j], z[i]);
                    }
                }
            }
            normalize(doublesB);
            System.out.println(d);
            System.out.println(domain);
            boolean ok = ArrayUtils.equals(doublesA, doublesB);
            if (!ok) {
                System.out.println("Problem ");
                System.out.println(e);
                System.out.println("Why");
                Plot.title("AXYZ(Bulk)").xsamples(x).ysamples(x).zsamples(z).plot((Object) doublesA);
                Plot.title("BXYZ(Each)").xsamples(x).ysamples(x).zsamples(z).plot((Object) doublesB);
                System.out.println(Arrays.deepToString(doublesA));
                System.out.println(Arrays.deepToString(doublesB));
//            JOptionPane.showConfirmDialog(null,null);
                exx = new IllegalArgumentException("Invalid");
            } else {
                if (showSuccess) {
                    Plot.title("AXYZ").xsamples(x).ysamples(x).zsamples(z).plot((Object) doublesA);
                }
            }
        } catch (Exception ex) {
            exx = new IllegalArgumentException(ex);
        }
        if (exx != null) {
            if (saveError) {
                IOUtils.saveObject2("~/a.pbm", e);
            }
            throw exx;
        }
    }

    public static void checkDoubleToDoubleXY(DoubleToDouble e, boolean saveError, boolean showSuccess) {
        IllegalArgumentException exx = null;
        try {
            for (ParamExpr p : e.getParams()) {
                if (p.isDoubleTyped()) {
                    e = e.setParam(p, 3.6).toDD();
                }
            }
            System.out.println("EXPRESSION= " + e);
            Domain d = e.getDomain();
            Domain domain = d.intersect(Maths.xydomain(-10, 10, -10, 10)).expandAll(5, 5);
            AbsoluteSamples relative = domain.toAbsolute(Samples.relative(21, 21));
            double[] x = relative.getX();
            double[] y = relative.getY();
            double[][] doublesA = e.computeDouble(x, y);
            double[][] doublesB = new double[y.length][x.length];
            normalize(doublesA);
            for (int j = 0; j < y.length; j++) {
                for (int k = 0; k < x.length; k++) {
                    doublesB[j][k] = e.computeDouble(x[k], y[j]);
                }
            }
            normalize(doublesB);
            System.out.println(d);
            System.out.println(domain);
            boolean ok = ArrayUtils.equals(doublesA, doublesB);
            if (!ok) {
                System.out.println("Problem ");
                System.out.println(e);
                System.out.println("Why");
                Plot.title("AXY(Bulk)").xsamples(x).ysamples(x).plot((Object) doublesA);
                Plot.title("BXY(Each)").xsamples(x).ysamples(x).plot((Object) doublesB);
                System.out.println(Arrays.deepToString(doublesA));
                System.out.println(Arrays.deepToString(doublesB));
//            JOptionPane.showConfirmDialog(null,null);
                exx = new IllegalArgumentException("Invalid");
            } else {
                if (showSuccess) {
                    Plot.title("AXY").xsamples(x).ysamples(x).plot((Object) doublesA);
                }
            }
        } catch (Exception ex) {
            exx = new IllegalArgumentException(ex);
        }
        if (exx != null) {
            if (saveError) {
                IOUtils.saveObject2("~/a.pbm", e);
            }
            throw exx;
        }
    }

    private static void normalize(double[][][] doublesB) {
        for (int i = 0; i < doublesB.length; i++) {
            for (int j = 0; j < doublesB[i].length; j++) {
                for (int k = 0; k < doublesB[i][j].length; k++) {
                    if (doublesB[i][j][k] == 0) {
                        doublesB[i][j][k] = 0;
                    }
                }
            }
        }
    }

    private static void normalize(double[][] doublesB) {
        for (int j = 0; j < doublesB.length; j++) {
            for (int k = 0; k < doublesB[j].length; k++) {
                if (doublesB[j][k] == 0) {
                    doublesB[j][k] = 0;
                }
            }
        }
    }
}
