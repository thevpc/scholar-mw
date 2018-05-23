package net.vpc.scholar.hadrumaths.test;

import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.test.util.ExprChecker;
import net.vpc.scholar.hadrumaths.test.util.ExprGenerator;
import net.vpc.scholar.hadrumaths.io.IOUtils;

public class TestVals {
    public static void main(String[] args) {
        try {
            main0(args);
        } catch (Exception any) {
            any.printStackTrace();
        }
    }

    public static void main0(String[] args) {
//        System.out.println(Maths.acotan(0));
//        Complex a = Complex.ONE.mul(Complex.NaN);
//        System.out.println(a);
//        MutableComplex b = MutableComplex.One();
//        b.mul(Complex.NaN);
//        System.out.println(b);
//        if(true){
//            return;
//        }
        Maths.Config.setCacheEnabled(false);
//        double[][][] f={
//                {
//                        {
//                                0,1,0
//                        },
//                        {
//                                0,1,0
//                        },
//                        {
//                                0,2,0
//                        },
//                        {
//                                1,1,3
//                        }
//                },
//                {
//                        {
//                                1,1,2
//                        },
//                        {
//                                1,1,2
//                        },
//                        {
//                                1,2,0
//                        },
//                        {
//                                2,1,1
//                        }
//                }
//        };
//        Discrete di=Discrete.create(f);
//        Plot.title("Z0").plot(di.getArray(Axis.Z,0));
//        Plot.title("Z1").plot(di.getArray(Axis.Z,1));
//        Plot.plot(f);
//        Plot.plot(di);
//        if(true){
//            return;
//        }
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
        Expr exp = (Expr) IOUtils.loadObject2("~/a.pbm");
//        exp=exp.getSubExpressions().get(0).toDD();
//        boolean dd = exp.isDD();
//        System.out.println(exp);
//        double vv=exp.computeDouble(3,3,3);
//        System.out.println(vv);
//        if(true){
//            return;
//        }
//        exp=exp.getSubExpressions().get(0).toDD();
//        exp=exp.getSubExpressions().get(0).getSubExpressions().get(0).toDD();
//        DoubleToDouble exp = new Mul(new Asinh(new Cosh(Maths.X)),Maths.domain(0,1,0,1)).toDD();
//        System.out.println(1.0/Maths.log10(0));
//        System.out.println(Complex.ONE.div(Complex.valueOf(0).log10()));
//        if(true){
//            return ;
//        }
        if (exp != null) {
            Expr exp2 = exp;//.getSubExpressions().get(0).getSubExpressions().get(0).getSubExpressions().get(1);//.getSubExpressions().get(1);//.getSubExpressions().get(0);//.getSubExpressions().get(0).getSubExpressions().get(0);
//            ((GteExpr)exp.getSubExpressions().get(1).getSubExpressions().get(0)).resetFunctionType(null);
//            exp2=new IfThenElse(DoubleValue.ONE1,DoubleValue.ONE1,exp2.getSubExpressions().get(2));
//            exp2=new IfThenElse(DoubleValue.ONE1,DoubleValue.ONE1,DoubleValue.TWO1);
            exp2.isInvariant(Axis.Y);
            exp2.isDD();
//            Complex c = exp2.toDC().computeComplex(0, 0, 0);
            //exp.simplify();
            exp.isDD();
            //exp.getSubExpressions().get(0).isDD();
            ExprChecker exprChecker = new ExprChecker()
                    .setXprecision(101)
                    .setYprecision(201)
                    .setZprecision(5)
                    .setCheckDD(true)
                    .setCheckDC(true)
                    .setCheckX(true)
                    .setCheckXY(true)
                    .setCheckXYZ(true);

            exprChecker.setSaveError(false).setShowSuccess(false);

//            exprChecker.checkValue(exp2, -0.8999999999999986,-2.5, 0);
//            if(true){
//                return;
//            }

            exprChecker.checkExpression(exp2);
        } else {
            ExprGenerator g = new ExprGenerator()
                    .setAcceptDomain1(true).setAcceptDomain2(false).setAcceptDomain3(false)
                    .setExpressionComplexity(3)
                    .setAcceptBoundaryValue(false).setAcceptNaNValue(false)
                    .setAcceptInfDomain(false);
//            Sqrtn ss = g.generate(Sqrtn.class);
//            boolean dd = ss.isDD();
            for (int i = 0; i < 100; i++) {
                Expr d = g.generateDD();
                if (!d.isDD()) {
                    System.out.println(i + " Why Not DD :: " + d);
                } else {
                    System.out.println(i + " :: " + d);
                }
                Expr ee = d.toDD();
                if (!ee.isDD()) {
                    ee.isDD();
                    ee.toDD();
                }
                Expr e = null;
                try {
                    e = d.mul(Maths.domain(-5, 5, -5, 5, -5, 5)).toDD();
                } catch (Exception ex) {
                    e = d.mul(Maths.domain(-5, 5, -5, 5, -5, 5)).toDD();

                }
                new ExprChecker().setSaveError(true).setShowSuccess(false).checkExpression(e);
            }
        }

    }

}
