package net.vpc.scholar.hadrumaths.derivation;

import net.vpc.common.util.ClassMap;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.derivation.formal.*;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToComplex;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToVector;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.ExprRef;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.double2complex.ComplexParam;
import net.vpc.scholar.hadrumaths.symbolic.double2double.AxisFunction;
import net.vpc.scholar.hadrumaths.symbolic.double2double.CosXCosY;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DefaultDoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import net.vpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.cond.Neg;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.*;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.trigo.*;

public class FormalDifferentiation implements FunctionDifferentiatorManager {
    private final ClassMap<FunctionDifferentiator> map = new ClassMap<FunctionDifferentiator>(Expr.class, FunctionDifferentiator.class, 40);

    public FormalDifferentiation() {
        register(Acos.class, new AcosDifferentiator());
        register(Acosh.class, new AcoshDifferentiator());
        register(ExprRef.class, new ExprRefDifferentiator());
        register(Asin.class, new AsinDifferentiator());
        register(Asinh.class, new AsinhDifferentiator());
        register(Atan.class, new AtanDifferentiator());
        register(AxisFunction.class, new AxisFunctionDifferentiator());
        register(Complex.class, new ComplexDifferentiator());
        register(Cos.class, new CosDifferentiator());
        register(Cosh.class, new CoshDifferentiator());
        register(CosXCosY.class, new CosXCosYDifferentiator());
        register(Cotan.class, new CotanDifferentiator());
        register(Cotanh.class, new CotanhDifferentiator());
        register(CDiscrete.class, new CDiscreteDifferentiator());
        register(Div.class, new DivDifferentiator());
        register(DoubleToComplex.class, new DoubleToComplexDifferentiator());
        register(DoubleToVector.class, new DoubleToVectorDifferentiator());
        register(Exp.class, new ExpDifferentiator());
        register(Inv.class, new InvDifferentiator());
        register(Mul.class, new MulDifferentiator());
        register(Neg.class, new NegDifferentiator());
        register(DoubleParam.class, new ParamExprDifferentiator());
        register(ComplexParam.class, new ParamExprDifferentiator());
        register(Plus.class, new PlusDifferentiator());
        register(Pow.class, new PowDifferentiator());
        register(Sin.class, new SinDifferentiator());
        register(Sinh.class, new SinhDifferentiator());
        register(Sqr.class, new SqrDifferentiator());
        register(Sqrt.class, new SqrtDifferentiator());
        register(Sub.class, new SubDifferentiator());
        register(Tan.class, new TanDifferentiator());
        register(Tanh.class, new TanhDifferentiator());
        register(VDiscrete.class, new VDiscreteDifferentiator());
        register(DoubleValue.class, new DoubleValueDifferentiator());
    }

    public void register(Class f1, FunctionDifferentiator differentiator) {
        map.put(f1, differentiator);
    }

    public Expr derive(Expr f1, Axis varIndex) {
        return getDerivator(f1.getClass()).derive(f1, varIndex, this);
    }

    public FunctionDifferentiator getDerivator(Class f1Class) {
        return map.getRequired(f1Class);
    }


}