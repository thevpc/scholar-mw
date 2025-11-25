package net.thevpc.scholar.hadrumaths.plot.random;


import net.thevpc.nuts.elem.NElement;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.DoubleParam;
import net.thevpc.scholar.hadrumaths.util.ArrayUtils;
import net.thevpc.scholar.hadrumaths.util.PlatformUtils;
import net.thevpc.scholar.hadrumaths.AbsoluteSamples;
import net.thevpc.scholar.hadruplot.Plot;
import net.thevpc.scholar.hadrumaths.Samples;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExprChecker {
    private int xprecision = 5;//101;
    private int yprecision = 5;//201;
    private int zprecision = 5;
    private boolean domainMargins = true;
    private boolean saveError;
    private boolean checkDD = true;
    private boolean checkDC = true;
    private boolean checkX = true;
    private boolean checkXY = true;
    private boolean checkXYZ = true;
    private boolean dumpExpressions = true;
    private ErrorList errorList = new ErrorList();
    private ExprCoverage coverage = new ExprCoverage();
    private ValDiffHelper testValHelper = new ValDiffHelper(errorList);

    public int getXprecision() {
        return xprecision;
    }

    public ExprChecker setXprecision(int xprecision) {
        this.xprecision = xprecision;
        return this;
    }

    public int getYprecision() {
        return yprecision;
    }

    public ExprChecker setYprecision(int yprecision) {
        this.yprecision = yprecision;
        return this;
    }

    public int getZprecision() {
        return zprecision;
    }

    public ExprChecker setZprecision(int zprecision) {
        this.zprecision = zprecision;
        return this;
    }

    public boolean isSaveError() {
        return saveError;
    }

    public ExprChecker setSaveError(boolean saveError) {
        this.saveError = saveError;
        return this;
    }



    public boolean checkExpression(String expr) {
        return checkExpression(Maths.expr(expr));
    }

    public boolean checkExpression(Expr expr) {
        boolean ok = true;
        String s = expr.toString();
        ok &= checkExpressionType(expr.getClass());
        try {
            ErrInfo info = new ErrInfo();
            info.expr = expr;

            List<Expr> c = expr.getChildren();
            if (c == null) {
                errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " has null getChildren()");
            }
            for (Expr expr1 : c) {
                if (expr1 == null) {
                    errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " has null getChildren() primitiveElement3D");
                }
            }

            Expr validExpr = replaceSpecial(expr);
//            if (!validExpr.equals(expr)) {
//                System.out.println("CHECK  :: " + expr);
//                System.out.println("\tAS :: " + validExpr);
//            }
            ok &= checkExpressionDD(info, validExpr);
            ok &= checkExpressionDC(info, validExpr);
            ok &= checkExpressionDV(info, validExpr);
            ok &= checkExpressionDM(info, validExpr);
            switch (expr.getType()) {
                case DOUBLE_NBR:
                case DOUBLE_EXPR: {
                    if (!(expr instanceof DoubleValue)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleValue");
                        ok = false;
                    }
                    if (!(expr instanceof DoubleToDouble)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleToDouble");
                        ok = false;
                    }
                    if (expr.toDD() != expr) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should return this in toDD()");
                        ok = false;
                    }
                    break;
                }
                case DOUBLE_DOUBLE: {
                    if (!(expr instanceof DoubleToDouble)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleToDouble");
                        ok = false;
                    }
                    if (expr.toDD() != expr) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should return this in toDD()");
                        ok = false;
                    }
                    break;
                }
                case COMPLEX_NBR:
                case COMPLEX_EXPR: {
                    if (!(expr instanceof ComplexValue)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement ComplexValue");
                        ok = false;
                    }
                    if (!(expr instanceof DoubleToComplex)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleToComplex");
                        ok = false;
                    }
                    if (expr.toDC() != expr) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should return this in toDC()");
                        ok = false;
                    }
                    break;
                }
                case DOUBLE_COMPLEX: {
                    if (!(expr instanceof DoubleToComplex)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleToComplex");
                        ok = false;
                    }
                    if (expr.toDC() != expr) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should return this in toDC()");
                        ok = false;
                    }
                    break;
                }
                case DOUBLE_CVECTOR: {
                    if (!(expr instanceof DoubleToVector)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleToVector");
                        ok = false;
                    }
                    if (expr.toDV() != expr) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should return this in toDV()");
                        ok = false;
                    }
                    break;
                }
                case DOUBLE_CMATRIX: {
                    if (!(expr instanceof DoubleToMatrix)) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should implement DoubleToMatrix");
                        ok = false;
                    }
                    if (expr.toDM() != expr) {
                        errorList.addInstanceError(expr, expr.getClass().getSimpleName() + " is " + expr.getType() + " . It should return this in toDM()");
                        ok = false;
                    }
                    break;
                }
            }

//            ok &= checkExpressionCC(info, validExpr);
//            ok &= checkExpressionCV(info, validExpr);
//            ok &= checkExpressionCM(info, validExpr);
            showErrors(info, validExpr);
            if (info.none) {
                ok = false;
                errorList.addError(expr.getClass(), "Unsupported Nothing to check :: " + expr.getType() + " :: " + expr.getClass().getSimpleName() + " :: " + expr);
            }
        } catch (Exception ex) {
            ok &= false;
            errorList.addInstanceError(expr, ex);
        }
        return ok;
    }

    public boolean checkExpressionType(Class cls) {
        BooleanRef err = BooleanMarker.ref();
        if (Expr.class.isAssignableFrom(cls) && !PlatformUtils.isAbstract(cls)) {
            if (!PlatformUtils.hasValidEqualsAndHashCode(cls)) {
                err.set();
                errorList.addError(cls, "Class " + cls.getName() + " should implement equals() & hashCode() methods ");
            }
        }
        if (!PlatformUtils.containsHierarchyConcreteMethodButObject(cls, "toString")) {
            err.set();
            errorList.addError(cls, "Class " + cls.getName() + " should implement toString()");
        }
        if (!PlatformUtils.containsHierarchyConcreteMethodButObject(cls, "equals", Object.class)) {
            err.set();
            errorList.addError(cls, "Class " + cls.getName() + " should implement equals()");
        }
        if (!PlatformUtils.containsHierarchyConcreteMethodButObject(cls, "hashCode")) {
            err.set();
            errorList.addError(cls, "Class " + cls.getName() + " should implement hashCode()");
        }
        boolean e = PlatformUtils.containsImmediateConcreteMethod(cls, "narrow", ExprType.class);
        boolean t = PlatformUtils.containsImmediateConcreteMethod(cls, "getNarrowType");

        if (PlatformUtils.containsImmediateConcreteMethod(cls, "isNarrow", ExprType.class)) {
            err.set();
            errorList.addError(cls, "Class " + cls.getName() + " should not implement isNarrow(ExprType). Instead you should implement narrow(ExprType) and getNarrowType() methods");
        }
        if (!(e == t)) {
            err.set();
            errorList.addError(cls,
                    "Class " + cls.getName() + " should implement All of narrow(ExprType) and getNarrowType() methods : missing " +
                            Stream.of((e ? "" : "narrow(ExprType)"), (t ? "" : "getNarrowType()")).filter(x -> x.length() > 0)
                                    .collect(Collectors.joining(","))
            );
        }

        ////////////////////
        if (PlatformUtils.isImplements(cls, ComplexValue.class)) {
            if (!PlatformUtils.isImplements(cls, DoubleToComplex.class)) {
                err.set();
                errorList.addError(cls, "Class " + cls.getName() + " should implement DoubleToComplex");
            }
        }
        if (PlatformUtils.isImplements(cls, DoubleValue.class)) {
            if (!PlatformUtils.isImplements(cls, DoubleToDouble.class)) {
                err.set();
                errorList.addError(cls, "Class " + cls.getName() + " should implement DoubleToDouble");
            }
        }

        ////////////////////
        e = PlatformUtils.containsImmediateConcreteMethod(cls, "mul", Double.TYPE);
        t = PlatformUtils.containsImmediateConcreteMethod(cls, "isSmartMulDouble");
        if (!(e == t)) {
            err.set();
            errorList.addError(cls,
                    "Class " + cls.getName() + " should implement All of mul(double) and isSmartMulDouble() methods : missing " +
                            Stream.of((e ? "" : "mul(double)"), (t ? "" : "isSmartMulDouble()")).filter(x -> x.length() > 0)
                                    .collect(Collectors.joining(","))
            );
        }

        ////////////////////
        e = PlatformUtils.containsImmediateConcreteMethod(cls, "mul", Complex.class);
        t = PlatformUtils.containsImmediateConcreteMethod(cls, "isSmartMulComplex");
        if (!(e == t)) {
            err.set();
            errorList.addError(cls,
                    "Class " + cls.getName() + " should implement All of mul(Complex) and isSmartMulComplex() methods : missing " +
                            Stream.of((e ? "" : "mul(Complex)"), (t ? "" : "isSmartMulComplex()")).filter(x -> x.length() > 0)
                                    .collect(Collectors.joining(","))
            );
        }

        ////////////////////
        e = PlatformUtils.containsImmediateConcreteMethod(cls, "mul", Domain.class);
        t = PlatformUtils.containsImmediateConcreteMethod(cls, "isSmartMulDomain");
        if (!(e == t)) {
            err.set();
            errorList.addError(cls,
                    "Class " + cls.getName() + " should implement All of mul(Domain) and isSmartMulDomain() methods : missing " +
                            Stream.of((e ? "" : "mul(Domain)"), (t ? "" : "isSmartMulDomain()")).filter(x -> x.length() > 0)
                                    .collect(Collectors.joining(","))
            );
        }
        return !err.get();
    }

    public Expr replaceSpecial(Expr e) {
        for (Param param : e.getParams()) {
            e = e.setParam(param, 3.6);
        }
        return e;
    }

    public boolean checkExpressionDD(ErrInfo info, Expr validExpr) {
        BooleanRef err = BooleanMarker.ref();
        if (info.expr instanceof DoubleParam) {
            info.none = false;//ignored
        }
        if (validExpr.is(ExprType.DOUBLE_DOUBLE) || validExpr.is(ExprType.DOUBLE_EXPR) || validExpr.is(ExprType.DOUBLE_NBR)) {
            info.none = false;
            if (isCheckDD()) {
                DoubleToDouble exp2 = null;
                if (validExpr instanceof DoubleToDouble) {
                    exp2 = (DoubleToDouble) validExpr;
                } else {
                    err.set();
                    errorList.addInstanceError(info.expr, "DD not DoubleToDouble? How come? :: " + validExpr);
                }
                switch (validExpr.getDomain().dimension()) {
                    case 1: {
                        if (isCheckX() && exp2.getDomain().dimension() == 1 && exp2.isEvaluatable()) {
                            info.v1 = checkDoubleToDoubleX(exp2, err);
                        }
                        break;
                    }
                    case 2: {
                        if (isCheckXY() && exp2.getDomain().dimension() == 2 && exp2.isEvaluatable()) {
                            info.v2 = checkDoubleToDoubleXY(exp2, err);
                        }
                        break;
                    }
                    case 3: {
                        if (isCheckXYZ() && exp2.getDomain().dimension() == 3 && exp2.isEvaluatable()) {
                            info.v3 = checkDoubleToDoubleXYZ(exp2, err);
                        }
                        break;
                    }
                }
            }
        }
        return !err.get();
    }

    public boolean checkExpressionDC(ErrInfo info, Expr validExpr) {
        BooleanRef err = BooleanMarker.ref();
        if (validExpr.is(ExprType.COMPLEX_NBR) || validExpr.is(ExprType.COMPLEX_EXPR) || validExpr.is(ExprType.DOUBLE_COMPLEX)) {
            info.none = false;
            if (isCheckDC()) {
                DoubleToComplex exp2 = null;
                if (validExpr instanceof DoubleToComplex) {
                    exp2 = (DoubleToComplex) validExpr;
                } else {
                    err.set();
                    errorList.addError(info.expr.getClass(), "DC not DoubleToComplex? How come? :: " + validExpr);
                    exp2 = validExpr.toDC();
                }
                if (isCheckX()) {
                    if (exp2.isEvaluatable() && exp2.getDomain().dimension() == 1) {
                        try {
                            info.c1 = checkDoubleToComplexX(exp2, err);
                        } catch (Exception ex) {
                            errorList.addError(info.expr.getClass(), ex);
                        }
                        info.none = false;
                    }
                }
                if (isCheckXY()) {
                    if (exp2.isEvaluatable() && exp2.getDomain().dimension() == 2) {
                        try {
                            info.c2 = checkDoubleToComplexXY(exp2, err);
                        } catch (Exception ex) {
                            errorList.addError(info.expr.getClass(), ex);
                        }
                        info.none = false;
                    }
                }
                if (isCheckXYZ() && exp2.isEvaluatable() && exp2.getDomain().dimension() == 3) {
                    info.c3 = checkDoubleToComplexXYZ(exp2, err);
                    info.none = false;
                }
            }
        }
        return !err.get();
    }

    private boolean checkExpressionDV(ErrInfo info, Expr validExpr) {
        BooleanRef err = BooleanMarker.ref();
        if (info.expr.is(ExprType.DOUBLE_CVECTOR) || info.expr.is(ExprType.CVECTOR_NBR) || info.expr.is(ExprType.CVECTOR_EXPR)) {
            DoubleToVector dv = info.expr.toDV();
//            int componentSize = dv.getComponentSize();
//            for (int i = 0; i < componentSize; i++) {
//                DoubleToComplex dc = dv.getComponent(Axis.cartesianValues()[i]).toDC();
//            }
            DoubleToMatrix dm = dv.toDM();
            info.none = false;
        }
        return !err.get();
    }

    private boolean checkExpressionDM(ErrInfo info, Expr validExpr) {
        BooleanRef err = BooleanMarker.ref();
        if (info.expr.is(ExprType.DOUBLE_CMATRIX) || info.expr.is(ExprType.CMATRIX_NBR) || info.expr.is(ExprType.CMATRIX_EXPR)) {
            DoubleToMatrix dm = info.expr.toDM();
            ComponentDimension dim = dm.getComponentDimension();
            for (int i = 0; i < dim.rows; i++) {
                for (int j = 0; j < dim.columns; j++) {
                    dm.getComponent(i, j).toDC();
                    String s = dm.getComponentTitle(i, j);
                }
            }
            info.none = false;
        }
        return !err.get();
    }

//    private boolean checkExpressionCC(ErrInfo info, Expr validExpr) {
//        BooleanRef err = BooleanMarker.ref();
//        if (info.expr.is(ExprType.COMPLEX_COMPLEX)) {
//            info.none = false;
//        }
//        return !err.get();
//    }
//
//    private boolean checkExpressionCV(ErrInfo info, Expr validExpr) {
//        BooleanRef err = BooleanMarker.ref();
//        if (info.expr.is(ExprType.COMPLEX_CVECTOR)) {
//            info.none = false;
//        }
//        return !err.get();
//    }
//
//    private boolean checkExpressionCM(ErrInfo info, Expr validExpr) {
//        BooleanRef err = BooleanMarker.ref();
//        if (info.expr.is(ExprType.COMPLEX_CMATRIX)) {
//            info.none = false;
//        }
//        return !err.get();
//    }

    protected void showErrors(ErrInfo info, Expr validExpr) {
        if (info.v1 != null && info.c1 != null) {
            Complex[] v1c = ArrayUtils.toComplex(info.v1);
            if (!Arrays.deepEquals(info.c1, v1c)) {
                Domain d = validExpr.getDomain();
                Domain domain = d.intersect(Maths.xdomain(-10, 10));
                domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
                AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision));
                double[] x = relative.getX();
                testValHelper.showDiff("Incompatibility[D1][C1]", v1c, info.c1, x);
                errorList.addInstanceError(info.expr, "Incompatibility between DD_X && DC_X  :: " + info.expr);
                testValHelper.plotDiff("Incompatibility", "C1", "D1", info.c1, v1c, null,
                        NElement.ofPair("expr", NElement.ofString(info.expr.toString())),
                        NElement.ofPair("domain", NElement.ofString(info.expr.getDomain().toString()))
                );
            }
        }
        if (info.v2 != null && info.c2 != null) {
            Complex[][] v2c = ArrayUtils.toComplex(info.v2);
            if (!Arrays.deepEquals(info.c2, v2c)) {
                testValHelper.plotDiff("Incompatibility", "C2", "D2", info.c2, v2c, null,
                        NElement.ofPair("expr", NElement.ofString(info.expr.toString())),
                        NElement.ofPair("domain", NElement.ofString(info.expr.getDomain().toString())));

                Domain d = validExpr.getDomain();
                Domain domain = d.intersect(Maths.domain(-10, 10, -10, 10));
                domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
                AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision));
                double[] x = relative.getX();
                double[] y = relative.getY();
                testValHelper.showDiff("Incompatibility[D2][C2]", v2c, info.c2, x, y);
                errorList.addInstanceError(info.expr, "Incompatibility between DD_XY && DC_XY  :: " + info.expr);
            }
        }
        if (info.v3 != null && info.c3 != null) {
            Complex[][][] v3c = ArrayUtils.toComplex(info.v3);
            if (!Arrays.deepEquals(info.c3, v3c)) {
                testValHelper.plotDiff("Incompatibility", "C3", "D3", info.c3, v3c, null,
                        NElement.ofPair("expr", NElement.ofString(info.expr.toString())),
                        NElement.ofPair("domain", NElement.ofString(info.expr.getDomain().toString())));

                Domain d = validExpr.getDomain();
                Domain domain = d.intersect(Maths.domain(-10, 10, -10, 10, -10, 10));
                domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
                AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision, zprecision));
                double[] x = relative.getX();
                double[] y = relative.getY();
                double[] z = relative.getZ();

                testValHelper.showDiff("Incompatibility[C3][D3]", v3c, info.c3, x, y, z);
                errorList.addInstanceError(info.expr, "Incompatibility between DD_XYZ && DC_XYZ  :: " + info.expr);
            }
        }
    }

    public boolean isCheckDD() {
        return checkDD;
    }

    public boolean isCheckX() {
        return checkX;
    }

    public ExprChecker setCheckX(boolean checkX) {
        this.checkX = checkX;
        return this;
    }

    public double[] checkDoubleToDoubleX(DoubleToDouble expr, BooleanMarker error) {
        double[] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = expr.getDomain();
        Domain domain = d.intersect(Maths.xdomain(-10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision));
        double[] x = relative.getX();

        doublesA = expr.evalDouble(x);

        double[] doublesB = new double[x.length];
        testValHelper.normalize(doublesA);
        boolean undefinedWithZero = false;
        for (int k = 0; k < x.length; k++) {
            BooleanRef defined = BooleanMarker.ref();
            double v = expr.evalDouble(x[k], defined);
            if (!defined.get() && v != 0) {
                error.set();
                undefinedWithZero = true;
            }
            doublesB[k] = v;
        }
        if (undefinedWithZero) {
            error.set();
            errorList.addInstanceError(expr, "Undefined with non zero value");
        }
        testValHelper.normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            testValHelper.plotDiff("Incompatibility", "DD_X(Bulk)", "DD_X(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x),
                    NElement.ofPair("expr", NElement.ofString(expr.toString())),
                    NElement.ofPair("domain", NElement.ofString(expr.getDomain().toString())));
//            Plot.title(":" + e).xsamples(x).plot();
//            Plot.title(":" + e).xsamples(x).plot((Object) );
//            JOptionPane.showConfirmDialog(null,null);
            testValHelper.showDiff("DD_X Diff", doublesA, doublesB, x);
            errorList.addInstanceError(expr, "Invalid values");
        } else {
            if (testValHelper.isShowSuccess()) {
                Plot.title("(OK)DD_X:" + expr).xsamples(x).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public boolean isCheckXY() {
        return checkXY;
    }

    public ExprChecker setCheckXY(boolean checkXY) {
        this.checkXY = checkXY;
        return this;
    }

    public double[][] checkDoubleToDoubleXY(DoubleToDouble expr, BooleanMarker error) {
        double[][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = expr.getDomain();
        Domain domain = d.intersect(Maths.domain(-10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();
//        System.out.println("-------------------------------");
        doublesA = expr.evalDouble(x, y);

        double[][] doublesB = new double[y.length][x.length];
        testValHelper.normalize(doublesA);
        boolean undefinedWithNonZero = false;
//        System.out.println("-------------------------------");
        for (int j = 0; j < y.length; j++) {
            for (int k = 0; k < x.length; k++) {
                BooleanRef defined = BooleanMarker.ref();
                double v = expr.evalDouble(x[k], y[j], defined);
                if (!defined.get() && v != 0) {
                    defined.reset();
                    v = expr.evalDouble(x[k], y[j], defined);
                    undefinedWithNonZero = true;
                    error.set();
                }
                doublesB[j][k] = v;
            }
        }
//        System.out.println("END -------------------------------");
        testValHelper.normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok || undefinedWithNonZero) {
            error.set();
            System.out.println("Problem :: " + expr);
            if (!ok) {
                testValHelper.plotDiff("Incompatibility", "DD_XY(Bulk)", "DD_XY(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x, y),
                        NElement.ofPair("expr", NElement.ofString(expr.toString())),
                        NElement.ofPair("domain", NElement.ofString(expr.getDomain().toString())));
//                Plot.title("DD_XY(Bulk):" + e).xsamples(x).ysamples(y).plot((Object) doublesA);
//                Plot.title("DD_XY(Each):" + e).xsamples(x).ysamples(y).plot((Object) doublesB);
                testValHelper.showDiff("DD_XY Diff", doublesA, doublesB, x, y);
            }
//            System.out.println(Arrays.deepToString(doublesA));
//            System.out.println(Arrays.deepToString(doublesB));
//            JOptionPane.showConfirmDialog(null,null);
            String errorString = Arrays.asList(!ok ? "Values do not match" : "", undefinedWithNonZero ? "Undefined with non zero" : "").stream().filter(s -> !s.isEmpty()).collect(Collectors.joining());
            errorList.addInstanceError(expr, "checkDoubleToDoubleXY:" + errorString);
        } else {
            if (testValHelper.isShowSuccess()) {
                Plot.title("(OK)DD_XY:" + expr).xsamples(x).ysamples(y).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public boolean isCheckXYZ() {
        return checkXYZ;
    }

    public ExprChecker setCheckXYZ(boolean checkXYZ) {
        this.checkXYZ = checkXYZ;
        return this;
    }

    public double[][][] checkDoubleToDoubleXYZ(DoubleToDouble e, BooleanMarker error) {
        double[][][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.domain(-10, 10, -10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision, zprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();
        double[] z = relative.getZ();
        doublesA = e.evalDouble(x, y, z);
        double[][][] doublesB = new double[z.length][y.length][x.length];
        testValHelper.normalize(doublesA);
        boolean undefinedWithNonZero = false;
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < y.length; j++) {
                for (int k = 0; k < x.length; k++) {
                    BooleanRef defined = BooleanMarker.ref();
                    double v = e.evalDouble(x[k], y[j], z[i], defined);
                    if (!defined.get() && v != 0) {
                        undefinedWithNonZero = true;
                    }
                    doublesB[i][j][k] = v;
                }
            }
        }
        if (undefinedWithNonZero) {
            error.set();
            errorList.addInstanceError(e, "checkDoubleToDoubleXYZ:Undefined with non zero value");
        }
        testValHelper.normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            error.set();
            testValHelper.plotDiff("Incompatibility", "DD_XYZ(Bulk)", "DD_XYZ(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x, y, z),
                    NElement.ofPair("expr", NElement.ofString(e.toString())),
                    NElement.ofPair("domain", NElement.ofString(e.getDomain().toString())));
            testValHelper.showDiff("DD_XYZ Diff", doublesA, doublesB, x, y, z);
            errorList.addInstanceError(e, "checkDoubleToDoubleXYZ:Invalid values");
        } else {
            if (testValHelper.isShowSuccess()) {
                Plot.title("(OK)DD_XYZ:" + e).xsamples(x).ysamples(y).zsamples(z).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public boolean isCheckDC() {
        return checkDC;
    }

    public Complex[] checkDoubleToComplexX(DoubleToComplex e, BooleanMarker error) {
        Complex[] doublesA = null;
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.xdomain(-10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision));
        double[] x = relative.getX();

        doublesA = e.evalComplex(x);
        boolean undefinedWithNonZero = false;

        Complex[] doublesB = new Complex[x.length];
        testValHelper.normalize(doublesA);
        for (int k = 0; k < x.length; k++) {
            BooleanRef defined = BooleanMarker.ref();
            Complex v = e.evalComplex(x[k], defined);
            if (!defined.get() && !v.isZero()) {
                undefinedWithNonZero = true;
            }
            doublesB[k] = v;
        }
        if (undefinedWithNonZero) {
            error.set();
            errorList.addInstanceError(e, "checkDoubleToComplexX:Undefined with non zero value");
        }
        testValHelper.normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            error.set();
            testValHelper.plotDiff("Incompatibility", "DC_X(Bulk)", "DC_X(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x),
                    NElement.ofPair("expr", NElement.ofString(e.toString())),
                    NElement.ofPair("domain", NElement.ofString(e.getDomain().toString())));
            testValHelper.showDiff("DC_X Diff", doublesA, doublesB, x);
            errorList.addInstanceError(e, "checkDoubleToComplexX:Invalid values");
        } else {
            if (testValHelper.isShowSuccess()) {
                Plot.title("(OK)DC_X:" + e).xsamples(x).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public Complex[][] checkDoubleToComplexXY(DoubleToComplex e, BooleanMarker error) {
        Complex[][] doublesA = null;
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.domain(-10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();

        doublesA = e.evalComplex(x, y);

        Complex[][] doublesB = new Complex[y.length][x.length];
        testValHelper.normalize(doublesA);
        boolean undefinedWithNonZero = false;
        for (int j = 0; j < y.length; j++) {
            for (int k = 0; k < x.length; k++) {
                BooleanRef defined = BooleanMarker.ref();
                Complex v = e.evalComplex(x[k], y[j], defined);
                if (!defined.get() && !v.isZero()) {
                    undefinedWithNonZero = true;
                }
                doublesB[j][k] = v;
            }
        }
        if (undefinedWithNonZero) {
            error.set();
            errorList.addInstanceError(e, "checkDoubleToComplexX:Undefined with non zero value");
        }
        testValHelper.normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            error.set();
            testValHelper.plotDiff("Incompatibility", "DC_XY(Bulk)", "DC_XY(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x, y),
                    NElement.ofPair("expr", NElement.ofString(e.toString())),
                    NElement.ofPair("domain", NElement.ofString(e.getDomain().toString())));
            testValHelper.showDiff("DC_XY Diff", doublesA, doublesB, x, y);
            errorList.addInstanceError(e, "Invalid values");
        } else {
            if (testValHelper.isShowSuccess()) {
                Plot.title("(OK)DC_XY:" + e).xsamples(x).ysamples(y).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public Complex[][][] checkDoubleToComplexXYZ(DoubleToComplex e, BooleanMarker error) {
        Complex[][][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.domain(-10, 10, -10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision, zprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();
        double[] z = relative.getZ();

        doublesA = e.evalComplex(x, y, z);

        boolean undefinedWithNonZero = false;
        Complex[][][] doublesB = new Complex[z.length][y.length][x.length];
        testValHelper.normalize(doublesA);
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < y.length; j++) {
                for (int k = 0; k < x.length; k++) {
                    BooleanRef defined = BooleanMarker.ref();
                    Complex v = e.evalComplex(x[k], y[j], z[i], defined);
                    if (!defined.get() && !v.isZero()) {
                        defined.unset();
                        v = e.evalComplex(x[k], y[j], z[i], defined);
                        undefinedWithNonZero = true;
                    }
                    doublesB[i][j][k] = v;
                }
            }
        }
        if (undefinedWithNonZero) {
            error.set();
            errorList.addInstanceError(e, "checkDoubleToComplexX:Undefined with non zero value");
        }
        testValHelper.normalize(doublesB);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            error.set();
            System.out.println("Problem :: " + e);
            testValHelper.plotDiff("Incompatibility", "DC_XYZ(Bulk)", "DC_XYZ(Each)", doublesA, doublesB, AbsoluteSamples.absolute(x, y, z),
                    NElement.ofPair("expr", NElement.ofString(e.toString())),
                    NElement.ofPair("domain", NElement.ofString(e.getDomain().toString())));
            testValHelper.showDiff("DC_XYZ Diff", doublesA, doublesB, x, y, z);
            errorList.addInstanceError(e, "Invalid values");
        } else {
            if (testValHelper.isShowSuccess()) {
                Plot.title("(OK)DC_XYZ:" + e).xsamples(x).ysamples(y).zsamples(z).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public boolean isDomainMargins() {
        return domainMargins;
    }

    public ExprChecker setDomainMargins(boolean domainMargins) {
        this.domainMargins = domainMargins;
        return this;
    }

    public ExprChecker setCheckDC(boolean checkDC) {
        this.checkDC = checkDC;
        return this;
    }

    public ExprChecker setCheckDD(boolean checkDD) {
        this.checkDD = checkDD;
        return this;
    }

    public void checkValue(Expr exp2, double vx, double vy, double vz) {
        Expr sexpr = replaceSpecial(exp2);
        System.out.println("#### " + exp2 + " ;; values " + vx + " , " + vy + " , " + vz);
        if (exp2.isInvariant(Axis.Y) && exp2.isInvariant(Axis.Z)) {
            double v = sexpr.toDD().evalDouble(vx);
            Complex vc = sexpr.toDC().evalComplex(vx);
            double v3 = sexpr.toDD().evalDouble(new double[]{vx})[0];
            Complex vc3 = sexpr.toDC().evalComplex(new double[]{vx})[0];
            System.out.println("#X   :: " + v);
            System.out.println("#X   :: " + vc);
            System.out.println("#X   :: " + v3);
            System.out.println("#X   :: " + vc3);
//            Assertions.assertEquals(v, v3);
//            Assertions.assertEquals(vc, vc3);
        }
        if (exp2.isInvariant(Axis.Z)) {
            double v = sexpr.toDD().evalDouble(vx, vy);
            Complex vc = sexpr.toDC().evalComplex(vx, vy);
            double[][] v3 = sexpr.toDD().evalDouble(new double[]{vx}, new double[]{vy});
            Complex[][] vc3 = sexpr.toDC().evalComplex(new double[]{vx}, new double[]{vy});
            System.out.println("#XY  :: " + v);
            System.out.println("#XY  :: " + vc);
            System.out.println("#XY  :: " + v3[0][0]);
            System.out.println("#XY  :: " + vc3[0][0]);
        }
        {
            double v = sexpr.toDD().evalDouble(vx, vy, vz);
            Complex vc = sexpr.toDC().evalComplex(vx, vy, vz);
            double[][][] v3 = sexpr.toDD().evalDouble(new double[]{vx}, new double[]{vy}, new double[]{vz});
            Complex[][][] vc3 = sexpr.toDC().evalComplex(new double[]{vx}, new double[]{vy}, new double[]{vz});
            System.out.println("#XYZ :: " + v);
            System.out.println("#XYZ :: " + vc);
            System.out.println("#XYZ :: " + v3[0][0][0]);
            System.out.println("#XYZ :: " + vc3[0][0][0]);
        }
    }

    public ExprChecker rerunLastErrors() {
        int count = 0;
        for (ErrorList.ErrorGroup error : errorList.load()) {
            count++;
            checkExpression((Expr) error.getObj());
            error.delete();
        }
        if (count == 0) {
            System.err.println("No errors to replay");
        } else {
            System.err.println("Replayed " + count + " errors");
        }
        return this;
    }

    public void runWithCoverage(int count, ExprRandomObjectGenerator generator, Class... classes) {
        if (classes.length == 0) {
            classes = generator.findObjectClasses();
        }
        for (Class cls : classes) {
            if (Expr.class.isAssignableFrom(cls)) {
                if (generator.getClsTypes().isAccept(cls)) {
                    if (generator.getConstructors(cls, generator.getExpectedClass()).length > 0) {
                        coverage.uncoverClass(cls);
                    }
                }
            }
        }
        this.checkAllClasses();
        this.getErrorList().writeSummary();
        Set<Class> uncoveredClasses = coverage.getUncoveredClasses();
        int uncoveredClassIndex = 0;
        this.getErrorList().addOpTotalCount(uncoveredClasses.size());
        for (Class uncoveredClass : uncoveredClasses) {
            boolean ok = true;
            for (int i = 0; i < count; i++) {
                Class[] a = generator.getProducedExprInterfaces(uncoveredClass);
                try {
                    Expr expr = (Expr) generator.randomObject(uncoveredClass);
                    if (isDumpExpressions()) {
                        System.out.printf("%-3d/%-3d-%-3d Test %-30s %-17s %-17s : %s%n", uncoveredClassIndex + 1, uncoveredClasses.size(), i + 1, uncoveredClass.getSimpleName(), expr.getType(), expr.getNarrowType(), expr.toString());
                    }
                    ok &= this.checkExpression(expr);
                } catch (ExprRandomObjectGenerator.GenerateException ex) {
                    //ignore
                }
                for (Class aClass : a) {
                    try {
                        Expr expr = (Expr) generator.randomObject(uncoveredClass, aClass, generator.createContext());
                        if (isDumpExpressions()) {
                            System.out.printf("%-3d/%-3d-%-3d Test %-30s %-17s=%-30s %-17s : %s%n", uncoveredClassIndex + 1, uncoveredClasses.size(), i + 1, uncoveredClass.getSimpleName(), expr.getType(), aClass.getSimpleName(), expr.getNarrowType(), expr.toString());
                        }
                        ok &= this.checkExpression(expr);
                    } catch (ExprRandomObjectGenerator.GenerateException ex) {
                        //ignore
                    }
                }
            }
            uncoveredClassIndex++;
            if (!ok) {
                this.getErrorList().addOpErrorCount(1);
            }
        }
//        for (int i = 0; i < count; i++) {
//            System.out.println("Iteration " + (i + 1));
//            try {
//                this.checkExpression(generator.randomObject(Expr.class));
//            } catch (ExprRandomObjectGenerator.GenerateException ex) {
//                //ignore
//            }
//        }
//        this.getErrorList().save();
        this.getErrorList().writeSummary();
    }

    public ExprChecker checkAllClasses() {
        for (Class cls : ExprProjectClasses.getProjectClasses()) {
            if (Expr.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers()) && !Modifier.isInterface(cls.getModifiers())) {
                checkExpressionType(cls);
            }
        }
        return this;
    }

    public ErrorList getErrorList() {
        return errorList;
    }

    public boolean isDumpExpressions() {
        return dumpExpressions;
    }

    public ExprChecker setDumpExpressions(boolean dumpExpressions) {
        this.dumpExpressions = dumpExpressions;
        return this;
    }

//    private static class DomainExprExpressionRewriterRule extends AbstractExpressionRewriterRule {
//        @Override
//        public Class<? extends Expr>[] getTypes() {
//            return new Class[]{DomainExpr.class};
//        }
//
//        @Override
//        public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
//            Expr r = Maths.simplify(e);
//            return r == e ? RewriteResult.unmodified() : RewriteResult.newVal(r);
//        }
//    }

//    private static class DoubleParamExpressionRewriterRule extends AbstractExpressionRewriterRule {
//        @Override
//        public Class<? extends Expr>[] getTypes() {
//            return new Class[]{DoubleParam.class};
//        }
//
//        @Override
//        public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
//            return RewriteResult.newVal(Complex.valueOf(3.6));
//        }
//    }

    public void run(int count, ExprRandomObjectGenerator generator) {
//        this.rerunLastErrors();
        this.checkAllClasses();
        this.getErrorList().writeSummary();
        for (int i = 0; i < count; i++) {
            System.out.println("Iteration " + (i + 1));
            try {
                this.checkExpression(generator.randomObject(Expr.class));
            } catch (ExprRandomObjectGenerator.GenerateException ex) {
                //ignore
            }
        }
//        this.getErrorList().save();
        this.getErrorList().writeSummary();
    }

    private static class ErrInfo {
        Expr expr;
        boolean none = true;
        double[] v1 = null;
        Complex[] c1 = null;
        double[][] v2 = null;
        double[][][] v3 = null;
        Complex[][] c2 = null;
        Complex[][][] c3 = null;
    }
}
