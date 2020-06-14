package net.vpc.scholar.hadrumaths.test.symbolic;

import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.plot.random.ExprChecker;
import net.vpc.scholar.hadrumaths.plot.random.ExprRandomObjectGenerator;
import net.vpc.scholar.hadrumaths.symbolic.double2double.*;
import org.junit.jupiter.api.Test;

public class TestExpressions {
    ExprChecker exprChecker = new ExprChecker().setXprecision(19)
            .setYprecision(57)//201
            .setZprecision(5)
            .setCheckDD(true)
            .setCheckDC(true)
            .setCheckX(true)
            .setCheckXY(true)
            .setCheckXYZ(true).setSaveError(true);
    ExprRandomObjectGenerator generator = new ExprRandomObjectGenerator(exprChecker)
            .setAcceptDomain1(true).setAcceptDomain2(true).setAcceptDomain3(true)
            .setComplexity(3)
            .setAcceptBoundaryValue(false).setAcceptNaNValue(false)
            .setAcceptInfDomain(false);
    ;
//
//    public static void main(String[] args) {
//        try {
//            main0(args);
//        } catch (Exception any) {
//            any.printStackTrace();
//        }
//    }

//    public static void main0(String[] args) {
//        new TestExpressions().run();
//    }

    @Test
    public void run() {
        Maths.Config.setCacheEnabled(false);
//        Expr e = Maths.expr("(((1 * (X + 1.0))/((X*X + X + 1.0)) * domain(2.0->6.0, -2.0->0.0)) + î *0)");
//        System.out.println(e.getType());
//        System.out.println(e);
//        exprChecker.checkExpression(e);
//        for (int i = 0; i < 1000; i++) {
//            generator.randomObject(IfExpr.class, DoubleToDouble.class,generator.createContext());
//            generator.randomObject(IfExpr.class, DoubleToComplex.class,generator.createContext());
//            generator.randomObject(IfExpr.class, DoubleToVector.class,generator.createContext());
//            generator.randomObject(IfExpr.class, DoubleToMatrix.class,generator.createContext());
//        }
//        if(true){
//            return;
//        }
//        RandomObjectGenerator.Generator gg = this.generator.getGenerator(DefaultDoubleToVector.class);
//        for (int i = 0; i < 100000; i++) {
//            Object y = gg.generate(DefaultDoubleToVector.class, null, this.generator.createContext());
//            System.out.println(y.getClass().getSimpleName() +" : "+y);
//        }
//        for (int i = 0; i < 100000; i++) {
//            Expr e = generator.randomObject(Expr.class);
////            System.out.println(e.getClass()+" :: "+e);
//            if(!exprChecker.checkExpression(e)){
//                exprChecker.checkExpression(e);
//                return;
//            }
//        }
        //cos(X + 1.0) * cos(Y + 1.0) * domain(0.0->3.0, (-2.0)->0.0) - imag(î)
//        exprChecker.checkExpression("pow(atan(sqr(1)), sin((1 + î *1)))");
//        Expr v = Maths.vector(Complex.valueOf(1, 2), Complex.valueOf(3, 4));
//        System.out.println(v.toString());
//        System.out.println(v.toDM().toString());
//        if(true){
//            return;
//        }
        generator
                .denyType(RWG.class)
                .denyType(PiecewiseSine2XFunctionXY.class)
                .denyType(PiecewiseSineXFunctionXY.class)
                .denyType(SinSeqXY.class)
                .denyType(SinSeqXZ.class)
                .denyType(SinSeqYZ.class)
                .denyType(Rooftop2DFunctionXY.class)
                .denyType(Polyhedron.class)
                .denyType(DDyIntegralX.class)
                .denyType(RooftopXFunctionXY.class)
                .denyType(Rooftop.class)
        ;
        exprChecker.setDumpExpressions(false).runWithCoverage(200, generator);
//        exprChecker.run(10000,generator);
    }

}
