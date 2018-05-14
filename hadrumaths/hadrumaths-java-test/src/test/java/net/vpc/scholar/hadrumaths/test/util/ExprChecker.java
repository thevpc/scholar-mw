package net.vpc.scholar.hadrumaths.test.util;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.transform.RewriteResult;
import net.vpc.scholar.hadrumaths.util.ArrayUtils;
import net.vpc.scholar.hadrumaths.util.IOUtils;

import java.util.Arrays;

public class ExprChecker {
    private int xprecision = 101;
    private int yprecision = 201;
    private int zprecision = 5;
    private boolean domainMargins = true;
    private boolean saveError;
    private boolean showSuccess;
    private boolean checkDD = true;
    private boolean checkDC = true;
    private boolean checkX = true;
    private boolean checkXY = true;
    private boolean checkXYZ = true;

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

    public boolean isShowSuccess() {
        return showSuccess;
    }

    public ExprChecker setShowSuccess(boolean showSuccess) {
        this.showSuccess = showSuccess;
        return this;
    }

    public boolean isDomainMargins() {
        return domainMargins;
    }

    public ExprChecker setDomainMargins(boolean domainMargins) {
        this.domainMargins = domainMargins;
        return this;
    }

    public boolean isCheckX() {
        return checkX;
    }

    public ExprChecker setCheckX(boolean checkX) {
        this.checkX = checkX;
        return this;
    }

    public boolean isCheckXY() {
        return checkXY;
    }

    public ExprChecker setCheckXY(boolean checkXY) {
        this.checkXY = checkXY;
        return this;
    }

    public boolean isCheckXYZ() {
        return checkXYZ;
    }

    public ExprChecker setCheckXYZ(boolean checkXYZ) {
        this.checkXYZ = checkXYZ;
        return this;
    }

    public void checkExpression(Expr exp) {
        try {
            boolean none = true;
            double[][] v2 = null;
            double[][][] v3 = null;
            Complex[][] c2 = null;
            Complex[][][] c3 = null;
            Expr validExpr=replaceSpecial(exp);
            System.out.println("CHECK  :: "+exp);
            System.out.println("\tAS :: "+validExpr);
            if (isCheckDD()) {
                if (validExpr.isDD()) {
                    DoubleToDouble exp2 = null;
                    if (validExpr instanceof DoubleToDouble) {
                        exp2 = (DoubleToDouble) validExpr;
                    } else {
                        System.out.println("DD not DoubleToDouble? How come? :: " + validExpr);
                        exp2 = validExpr.toDD();
                    }
                    if (isCheckXY()) {
                        if (validExpr.isInvariant(Axis.Z)) {
                            v2 = checkDoubleToDoubleXY(exp2);
                            none = false;
                        }
                    }
                    if (isCheckXYZ()) {
                        v3 = checkDoubleToDoubleXYZ(exp2);
                        none = false;
                    }
                }
            }
            if (isCheckDC()) {
                if (validExpr.isDC()) {
                    none = false;
                    DoubleToComplex exp2 = null;
                    if (validExpr instanceof DoubleToComplex) {
                        exp2 = (DoubleToComplex) validExpr;
                    } else {
                        System.out.println("DC not DoubleToComplex? How come? :: " + validExpr);
                        exp2 = validExpr.toDC();
                    }
                    if (isCheckXY()) {
                        if (validExpr.isInvariant(Axis.Z)) {
                            c2 = checkDoubleToComplexXY(exp2);
                            none = false;
                        }
                    }
                    if (isCheckXYZ()) {
                        c3 = checkDoubleToComplexXYZ(exp2);
                        none = false;
                    }
                }
            }
            if (v2 != null && c2 != null) {
                Complex[][] v2c = ArrayUtils.toComplex(v2);
                if (!Arrays.deepEquals(c2, v2c)) {
                    Plot.title("Incompatibility[C2]").plot(c2);
                    Plot.title("Incompatibility[D2]").plot(v2c);

                    Domain d = validExpr.getDomain();
                    Domain domain = d.intersect(Maths.xydomain(-10, 10, -10, 10));
                    domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
                    AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision));
                    double[] x = relative.getX();
                    double[] y = relative.getY();
                    showDiff("Incompatibility[D2][C2]",v2c,c2,x,y);
                    throw new IllegalArgumentException("Incompatibility between DD_XY && DC_XY  :: " + exp);
                }
            }
            if (v3 != null && c3 != null) {
                Complex[][][] v3c = ArrayUtils.toComplex(v3);
                if (!Arrays.deepEquals(c3, v3c)) {
                    Plot.title("Incompatibility[C3]").plot(c3);
                    Plot.title("Incompatibility[D3]").plot(v3c);

                    Domain d = validExpr.getDomain();
                    Domain domain = d.intersect(Maths.xyzdomain(-10, 10, -10, 10, -10, 10));
                    domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
                    AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision, zprecision));
                    double[] x = relative.getX();
                    double[] y = relative.getY();
                    double[] z = relative.getZ();

                    showDiff("Incompatibility[D2][C2]",v3c,c3,x,y,z);

                    throw new IllegalArgumentException("Incompatibility between DD_XYZ && DC_XYZ  :: " + exp);
                }
            }
            if (none) {
                System.out.println("Unsupported Nothing to check :: " + exp);
            }
        } catch (Exception ex) {
            if (saveError) {
                IOUtils.saveObject2("~/a.pbm", exp);
            }
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
            throw new IllegalArgumentException(ex);
        }
    }

    public boolean isCheckDD() {
        return checkDD;
    }

    public ExprChecker setCheckDD(boolean checkDD) {
        this.checkDD = checkDD;
        return this;
    }

    public boolean isCheckDC() {
        return checkDC;
    }

    public ExprChecker setCheckDC(boolean checkDC) {
        this.checkDC = checkDC;
        return this;
    }

    public double[][][] checkDoubleToDoubleXYZ(DoubleToDouble e) {
        double[][][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.xyzdomain(-10, 10, -10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision, zprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();
        double[] z = relative.getZ();
        doublesA = e.computeDouble(x, y, z);
        double[][][] doublesB = new double[z.length][y.length][x.length];
        normalize(doublesA);
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < y.length; j++) {
                for (int k = 0; k < x.length; k++) {
                    BooleanRef defined = BooleanMarker.ref();
                    double v = e.computeDouble(x[k], y[j], z[i], defined);
                    if (!defined.get() && v != 0) {
                        throw new IllegalArgumentException("Undefined with non zero value");
                    }
                    doublesB[i][j][k] = v;
                }
            }
        }
        normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            System.out.println("Problem :: " + e);
            Plot.title("DD_XYZ(Bulk) :" + e).xsamples(x).ysamples(y).zsamples(z).plot(Discrete.create(doublesA));
            Plot.title("DD_XYZ(Each):" + e).xsamples(x).ysamples(y).zsamples(z).plot(Discrete.create(doublesB));
//            System.out.println(Arrays.deepToString(doublesA));
//            System.out.println(Arrays.deepToString(doublesB));
            showDiff("DD_XYZ Diff",doublesA,doublesB,x,y,z);
//            JOptionPane.showConfirmDialog(null,null);
            throw new IllegalArgumentException("Invalid");
        } else {
            if (showSuccess) {
                Plot.title("(OK)DD_XYZ:" + e).xsamples(x).ysamples(y).zsamples(z).plot(Discrete.create(doublesA));
            }
        }
        return doublesA;
    }

    public Complex[][][] checkDoubleToComplexXYZ(DoubleToComplex e) {
        Complex[][][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.xyzdomain(-10, 10, -10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision, zprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();
        double[] z = relative.getZ();

        doublesA = e.computeComplex(x, y, z);

        Complex[][][] doublesB = new Complex[z.length][y.length][x.length];
        normalize(doublesA);
        for (int i = 0; i < z.length; i++) {
            for (int j = 0; j < y.length; j++) {
                for (int k = 0; k < x.length; k++) {
                    BooleanRef defined = BooleanMarker.ref();
                    Complex v = e.computeComplex(x[k], y[j], z[i], defined);
                    if (!defined.get() && !v.isZero()) {
                        throw new IllegalArgumentException("Undefined with non zero value");
                    }
                    doublesB[i][j][k] = v;
                }
            }
        }
        normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            System.out.println("Problem :: " + e);
            Plot.title("DC_XYZ(Bulk):" + e).xsamples(x).ysamples(y).zsamples(z).plot(Discrete.create(doublesA));
            Plot.title("DC_XYZ(Each):" + e).xsamples(x).ysamples(y).zsamples(z).plot(Discrete.create(doublesB));
//            System.out.println(Arrays.deepToString(doublesB));
//            System.out.println(Arrays.deepToString(doublesA));
//            JOptionPane.showConfirmDialog(null,null);
            showDiff("DC_XYZ Diff",doublesA,doublesB,x,y,z);
            throw new IllegalArgumentException("Invalid");
        } else {
            if (showSuccess) {
                Plot.title("(OK)DC_XYZ:" + e).xsamples(x).ysamples(y).zsamples(z).plot(Discrete.create(doublesA));
            }
        }
        return doublesA;
    }

    public double[][] checkDoubleToDoubleXY(DoubleToDouble e) {
        double[][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.xydomain(-10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();

        doublesA = e.computeDouble(x, y);

        double[][] doublesB = new double[y.length][x.length];
        normalize(doublesA);
        for (int j = 0; j < y.length; j++) {
            for (int k = 0; k < x.length; k++) {
                BooleanRef defined = BooleanMarker.ref();
                double v = e.computeDouble(x[k], y[j], defined);
                if (!defined.get() && v != 0) {
                    throw new IllegalArgumentException("Undefined with non zero value");
                }
                doublesB[j][k] = v;
            }
        }
        normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            System.out.println("Problem :: " + e);
            Plot.title("DD_XY(Bulk):" + e).xsamples(x).ysamples(y).plot((Object) doublesA);
            Plot.title("DD_XY(Each):" + e).xsamples(x).ysamples(y).plot((Object) doublesB);
//            System.out.println(Arrays.deepToString(doublesA));
//            System.out.println(Arrays.deepToString(doublesB));
//            JOptionPane.showConfirmDialog(null,null);
            showDiff("DD_XY Diff",doublesA,doublesB,x,y);
            throw new IllegalArgumentException("Invalid");
        } else {
            if (showSuccess) {
                Plot.title("(OK)DD_XY:" + e).xsamples(x).ysamples(y).plot((Object) doublesA);
            }
        }
        return doublesA;
    }

    public Complex[][] checkDoubleToComplexXY(DoubleToComplex e) {
        Complex[][] doublesA = null;
//        System.out.println("EXPRESSION= " + e);
        Domain d = e.getDomain();
        Domain domain = d.intersect(Maths.xydomain(-10, 10, -10, 10));
        domain = isDomainMargins() ? domain.expandAll(5, 5) : domain;
        AbsoluteSamples relative = domain.toAbsolute(Samples.relative(xprecision, yprecision));
        double[] x = relative.getX();
        double[] y = relative.getY();

        doublesA = e.computeComplex(x, y);

        Complex[][] doublesB = new Complex[y.length][x.length];
        normalize(doublesA);
        for (int j = 0; j < y.length; j++) {
            for (int k = 0; k < x.length; k++) {
                BooleanRef defined = BooleanMarker.ref();
                Complex v = e.computeComplex(x[k], y[j], defined);
                if (!defined.get() && !v.isZero()) {
                    throw new IllegalArgumentException("Undefined with non zero value");
                }
                doublesB[j][k] = v;
            }
        }
        normalize(doublesB);
//        System.out.println(d);
//        System.out.println(domain);
        boolean ok = ArrayUtils.equals(doublesA, doublesB);
        if (!ok) {
            System.out.println("Problem :: " + e);
            Plot.title("DC_XY(Bulk):" + e).xsamples(x).ysamples(y).plot((Object) doublesA);
            Plot.title("DC_XY(Each):" + e).xsamples(x).ysamples(y).plot((Object) doublesB);
//            System.out.println(Arrays.deepToString(doublesA));
//            System.out.println(Arrays.deepToString(doublesB));
            showDiff("DC_XY Diff",doublesA,doublesB,x,y);
//            JOptionPane.showConfirmDialog(null,null);
            throw new IllegalArgumentException("Invalid");
        } else {
            if (showSuccess) {
                Plot.title("(OK)DC_XY:" + e).xsamples(x).ysamples(y).plot((Object) doublesA);
            }
        }
        return doublesA;
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

    private static void normalize(Complex[][][] doublesB) {
        for (int i = 0; i < doublesB.length; i++) {
            for (int j = 0; j < doublesB[i].length; j++) {
                for (int k = 0; k < doublesB[i][j].length; k++) {
                    if (doublesB[i][j][k].isZero()) {
                        doublesB[i][j][k] = Complex.ZERO;
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

    private static void normalize(Complex[][] doublesB) {
        for (int j = 0; j < doublesB.length; j++) {
            for (int k = 0; k < doublesB[j].length; k++) {
                if (doublesB[j][k].isZero()) {
                    doublesB[j][k] = Complex.ZERO;
                }
            }
        }
    }

    public Expr replaceSpecial(Expr e){
        ExpressionRewriterRuleSet r=new ExpressionRewriterRuleSet("Example");
        r.addRule(new ExpressionRewriterRule() {
            @Override
            public Class<? extends Expr>[] getTypes() {
                return new Class[]{DomainExpr.class};
            }

            @Override
            public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
                Expr r = Maths.simplify(e);
                return r==e?RewriteResult.unmodified(e) : RewriteResult.newVal(r);
            }
        });
        r.addRule(new ExpressionRewriterRule() {
            @Override
            public Class<? extends Expr>[] getTypes() {
                return new Class[]{DoubleParam.class};
            }

            @Override
            public RewriteResult rewrite(Expr e, ExpressionRewriter ruleset) {
                return RewriteResult.newVal(Complex.valueOf(3.6));
            }
        });
        for (ExpressionRewriterRule rule : ExpressionRewriterFactory.NAVIGATION_RULES) {
            r.addRule(rule);
        }
        return r.rewriteOrSame(e);
    }

    public void checkValue(Expr exp2, double vx, double vy, double vz) {
        Expr sexpr = replaceSpecial(exp2);
        System.out.println("#### "+exp2+" ;; values "+vx+" , "+vy+" , "+vz);
        double v = sexpr.toDD().computeDouble(vx, vy, vz);
        Complex vc = sexpr.toDC().computeComplex(vx, vy, vz);
        double[][][] v3 = sexpr.toDD().computeDouble(new double[]{vx}, new double[]{vy}, new double[]{vz});
        Complex[][][] vc3 = sexpr.toDC().computeComplex(new double[]{vx}, new double[]{vy}, new double[]{vz});
        System.out.println(v);
        System.out.println(vc);
        System.out.println(v3[0][0][0]);
        System.out.println(vc3[0][0][0]);
    }

    private void showDiff(String message,Complex[][][] c3,Complex[][][] v3c,double[] x,double[] y,double[] z){
        for (int i = 0; i < c3.length; i++) {
            for (int j = 0; j < c3[i].length; j++) {
                for (int k = 0; k < c3[i][j].length; k++) {
                    if(!c3[i][j][k].equals(v3c[i][j][k])){
                        System.err.println(message+" : x["+k+"]="+x[k]+",y["+j+"]="+y[j]+",z["+i+"]="+z[i]+"  : "+v3c[i][j][k]+"  <>  "+c3[i][j][k]);
                    }
                }
            }
        }
    }
    private void showDiff(String message,double[][][] c3,double[][][] v3c,double[] x,double[] y,double[] z){
        for (int i = 0; i < c3.length; i++) {
            for (int j = 0; j < c3[i].length; j++) {
                for (int k = 0; k < c3[i][j].length; k++) {
                    if(Double.compare(c3[i][j][k],v3c[i][j][k])!=0){
                        System.err.println(message+" : x["+k+"]="+x[k]+",y["+j+"]="+y[j]+",z["+i+"]="+z[i]+"  : "+v3c[i][j][k]+"  <>  "+c3[i][j][k]);
                    }
                }
            }
        }
    }
    private void showDiff(String message,Complex[][] c3,Complex[][] v3c,double[] x,double[] y){
            for (int j = 0; j < c3.length; j++) {
                for (int k = 0; k < c3[j].length; k++) {
                    if(!c3[j][k].equals(v3c[j][k])){
                        System.err.println(message+" : x["+k+"]="+x[k]+",y["+j+"]="+y[j]+"  : "+v3c[j][k]+"  <>  "+c3[j][k]);
                    }
                }
            }

    }
    private void showDiff(String message,double[][] c3,double[][] v3c,double[] x,double[] y){
        for (int j = 0; j < c3.length; j++) {
            for (int k = 0; k < c3[j].length; k++) {
                if(Double.compare(c3[j][k],v3c[j][k])!=0){
                    System.err.println(message+" : x["+k+"]="+x[k]+",y["+j+"]="+y[j]+"  : "+v3c[j][k]+"  <>  "+c3[j][k]);
                }
            }
        }
    }

}
