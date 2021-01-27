package net.thevpc.scholar.hadrumaths.scalarproducts.formal;

import net.thevpc.common.mon.ProgressMonitor;
import net.thevpc.common.mon.ProgressMonitors;
import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.common.util.Chronometer;
import net.thevpc.common.util.ClassMap;
import net.thevpc.common.util.IntTuple2;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.ExpressionRewriterFactory;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.rewriter.MulAddLinerizeRule;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToCosXCosYRule;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToDDxyLinearRule;
import net.thevpc.scholar.hadrumaths.symbolic.*;
import net.thevpc.scholar.hadrumaths.symbolic.double2double.*;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Pow;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterSuite;
import net.thevpc.common.util.IntPairIterator;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static net.thevpc.scholar.hadrumaths.Maths.chrono;

public class FormalScalarProductOperator extends AbstractScalarProductOperator {

    private final static Logger log = Logger.getLogger(FormalScalarProductOperator.class.getName());
    private final ExpressionRewriterSuite expressionRewriter = new ExpressionRewriterSuite("FormalScalarProductExpandOptimizeRuleSuite");
    private final Map<ClassClassKey, FormalScalarProductHelper> map = new HashMap<ClassClassKey, FormalScalarProductHelper>();
    private final Map<ClassClassKey, FormalScalarProductHelper> cache = new HashMap<ClassClassKey, FormalScalarProductHelper>();
    private final Set<ClassClassKey> noHelperCache = new HashSet<ClassClassKey>();
    private final ScalarProductOperator fallback;
    private FormalScalarProductHelper fallbackHelper;
    private Set<Class> excludedExprHierarchy = new HashSet<>(Arrays.asList(
            DoubleToDoubleDefaults.DoubleToDoubleSimple1.class,
            DoubleToDoubleDefaults.DoubleToDoubleSimple2.class,
            DoubleToDoubleDefaults.DoubleToDoubleSimple3.class,
            DoubleToDoubleDefaults.DoubleToDoubleSimple.class,
            DoubleToDoubleDefaults.DoubleToDoubleNormal.class,
            DoubleToDoubleDefaults.DoubleToDoubleBinaryDDDD.class,
            DoubleToDoubleDefaults.DoubleToDoubleUnaryDD.class,
            DoubleToComplexDefaults.DoubleToComplexNormal.class,
            DoubleToComplexDefaults.DoubleToComplexSimple.class,
            DoubleToComplexDefaults.DoubleToComplexUnaryDC.class,
            DoubleToComplexDefaults.DoubleToComplexDCDC.class,
            DoubleToComplexDefaults.DoubleToComplexUnaryDC.class,
            DoubleToComplexDefaults.DoubleToComplexAggregator1.class,
            DoubleToComplexDefaults.DoubleToComplexAggregator2.class,
            DoubleToComplexDefaults.DoubleToComplexSimple3.class
//            ,PlotDomain.class
    ));

    //
//    public FormalScalarProductHelper getScalarProduct(Class f1Class, Class f2Class) {
//    }
    private Map<Class, Class[]> cachedExprHierarchy = new HashMap<>();

    public FormalScalarProductOperator(boolean hermitian, ScalarProductOperator fallback) {
        super(hermitian);
        this.fallback = fallback;
        if (fallback != null) {
            fallbackHelper = new OperatorToFormalScalarProductHelper(this.fallback);
        }

        expressionRewriter.clear();
//        ExpressionRewriterRuleSet CANONICAL_RULE_SET = new ExpressionRewriterRuleSet("CANONICAL");
//        CANONICAL_RULE_SET.addAllRules(ExpressionRewriterFactory.NAVIGATION_RULES);
//        CANONICAL_RULE_SET.addAllRules(ExpressionRewriterFactory.CANONICAL_RULES);
//        expressionRewriter.add(CANONICAL_RULE_SET);


        ExpressionRewriterRuleSet EXPAND_SIMPLIFY_RULE_SET = new ExpressionRewriterRuleSet(" FormalScalarProductExpandSimplifyRuleSet");
        EXPAND_SIMPLIFY_RULE_SET.addAllRules(ExpressionRewriterFactory.SIMPLIFY_RULES);
        EXPAND_SIMPLIFY_RULE_SET.addRule(MulAddLinerizeRule.INSTANCE);
        EXPAND_SIMPLIFY_RULE_SET.addRule(ToCosXCosYRule.INSTANCE);
        EXPAND_SIMPLIFY_RULE_SET.addRule(ToDDxyLinearRule.INSTANCE);
        expressionRewriter.add(EXPAND_SIMPLIFY_RULE_SET);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(), 2);
        register(CosXCosY.class, CosXPlusY.class, new CosCosVsCosXPlusYProduct(), 2);
        register(CosXPlusY.class, CosXPlusY.class, new CosXPlusYVsCosXPlusYProduct(), 2);
        register(CosXPlusY.class, DoubleValue.class, new CosXPlusYVsCstProduct(), 2);
        register(CosXPlusY.class, Linear.class, new CosXPlusYVsLinearProduct(), 2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(CosXCosY.class, DoubleValue.class, new CosCosVsDoubleXYScalarProduct(), 2);
        register(CosXCosY.class, DoubleValue.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(CosXCosY.class, Linear.class, new CosCosVsLinearScalarProduct(), 2);
        register(CosXCosY.class, Linear.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(Linear.class, Linear.class, new LinearVsLinearScalarProduct(), 2);
        register(Linear.class, Linear.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(Linear.class, DoubleValue.class, new LinearVsCstScalarProduct(), 2);
        register(Linear.class, DoubleValue.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(DoubleValue.class, DoubleValue.class, new DoubleDoubleValueVsDoubleDoubleValueScalarProduct(), 1, 2, 3);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(), 2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(), 2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(), 2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(), 2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(CosXCosY.class, UFunction.class, new CosCosVsUScalarProduct(), 2);
        register(CosXCosY.class, UFunction.class, Domain2To1ScalarProduct.INSTANCE, 1);

        register(DDiscrete.class, Expr.class, new DDiscreteVsAnyScalarProduct(), 1, 2, 3);

//        register(DDxyDisc DDiscreteVsAnyScalarProduct DDxyDiscreteVsAnyScalarProduct());
        register(Plus.class, Expr.class, new PlusVsAnyScalarProduct(), 2);
        register(Plus.class, Expr.class, Domain2To1ScalarProduct.INSTANCE, 1);
    }

    public void register(Class f1, Class f2, FormalScalarProductHelper sp, int domainDimension, int... domainDimensions) {
        TreeSet<Integer> all = new TreeSet<Integer>();
        all.add(domainDimension);
        for (int dimension : domainDimensions) {
            all.add(dimension);
        }
        for (Integer dim : all) {
            ClassClassKey c = new ClassClassKey(f1, f2, dim);
            map.put(c, sp);
            cache.clear();
            noHelperCache.clear();
            cache.put(c, sp);
        }
    }

    public boolean isSupported(Class f1Class, Class f2Class, int domainDimension) {
        return getScalarProduct0(f1Class, f2Class, domainDimension) != null;
    }

    public FormalScalarProductHelper getScalarProduct0(Class f1Class, Class f2Class, int domainDimension) {
        ClassClassKey c0 = new ClassClassKey(f1Class, f2Class, domainDimension);
        FormalScalarProductHelper p = cache.get(c0);
        if (p != null) {
            return p;
        }
        if (!noHelperCache.contains(c0)) {
            ClassClassKey c;
            Class[] c1Values = getExprClassHierarchy(c0.getC1());
            Class[] c2Values = getExprClassHierarchy(c0.getC2());

            IntPairIterator it = new IntPairIterator();
            while (it.hasNext()) {
                IntTuple2 v = it.next();
                int v1 = v.getValue1();
                int v2 = v.getValue2();
                if (v1 >= c1Values.length && v2 >= c2Values.length) {
                    break;
                }
                if (v1 < c1Values.length && v2 < c2Values.length) {
                    c = new ClassClassKey(c1Values[v1], c2Values[v2], domainDimension);
                    p = map.get(c);
                    if (p != null) {
                        break;
                    }
                    p = map.get(c.invert());
                    if (p != null) {
                        p = new ReverseFormalScalarProductHelper(p);
                        break;
                    }
                }
            }
        }
        if (p != null) {
            cache.put(c0, p);
            return p;
        } else {
            noHelperCache.add(c0);
        }
        return null;
    }

    protected Class[] getExprClassHierarchy(Class c1) {
        Class[] found = cachedExprHierarchy.get(c1);
        if (found == null) {
            found = Stream.of(ClassMap.findClassHierarchy(c1, null)).filter(x -> !excludedExprHierarchy.contains(x)
                    && Expr.class.isAssignableFrom(x)).toArray(Class[]::new);
            cachedExprHierarchy.put(c1, found);
        }
        return found;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        DoubleToDouble[] opts = toCanonicalScalarProductPair(f1, f2);
        DoubleToDouble f1opt = opts[0];
        DoubleToDouble f2opt = opts[1];
//        DoubleToDouble f1opt= expressionRewriter.rewriteOrSame(f1).toDD();
//        DoubleToDouble f2opt= expressionRewriter.rewriteOrSame(f2).toDD();

        Domain inter = f1opt.getDomain().intersect(f2opt.getDomain()).intersect(domain);
        if (f1opt.isZero() || f2opt.isZero()) {
            return 0;
        }
        if (inter.isEmpty()) {
            return 0;
        }
        if (inter.isInfinite()) {
            throw new IllegalArgumentException("Cannot integrate over infinite interval " + inter);
        }
        return getScalarProduct(f1opt.getClass(), f2opt.getClass(), inter.getDimension()).eval(inter, f1opt, f2opt, this);
    }

    public double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2, ProgressMonitor monitor) {
        ProgressMonitors.incremental(monitor, f2.length);
        DoubleToDouble f1opt = expressionRewriter.rewriteOrSame(f1, ExprType.DOUBLE_DOUBLE).toDD();

        double[] rets = new double[f2.length];
        boolean f1zero = f1opt.isZero();
        Domain f1domain = f1opt.getDomain().intersect(domain);
        for (int i = 0; i < rets.length; i++) {
            DoubleToDouble f2i = f2[i];
            DoubleToDouble f2opt = expressionRewriter.rewriteOrSame(f2i, ExprType.DOUBLE_DOUBLE).toDD();
//            if(f2opt instanceof Mul){
//                f2opt= expressionRewriter.rewriteOrSame(f2i).toDD();
//            }
            Domain inter = f1domain.intersect(f2opt.getDomain());
            if (f1zero || f2opt.isZero()) {
                rets[i] = 0;
            } else if (inter.isEmpty()) {
                rets[i] = 0;
            } else if (inter.isInfinite()) {
                throw new IllegalArgumentException("Cannot integrate over infinite interval " + inter);
            } else {
                rets[i] = getScalarProduct(f1opt.getClass(), f2opt.getClass(), inter.getDimension()).eval(inter, f1opt, f2opt, this);
            }
        }
        return rets;
    }

//    public static void main(String[] args) {
//        Maths.Config.setCacheEnabled(false);
////        Expr f1=cos(add(mul(complex(0) , X) , mul(complex(0) , Y) ,complex(0.9708732020474193)));
////        Expr f2=mul(cos(add(mul(complex(0) , X) , mul(complex(0) , Y) ,complex(0.9708732020474193))) , cos(add(mul(complex(0) , X) ,complex(0))) , cos(add(mul(complex(0), Y) ,complex(0.08808395516141376))));
//        Expr f1=cos(add(mul(complex(0.5561038709368364) , X) , mul(CZERO , Y), CZERO));
//        Expr f2=mul(cos(add(mul(CZERO , X) , CZERO)) , cos(add(mul(CZERO , Y) , CZERO)));
//        System.out.println(f1.simplify());
//        System.out.println(f2.simplify());
//        DoubleToDouble[] r = new FormalScalarProductOperator(null).optimizeFunctions(f1.toDD(), f2.toDD());
//        System.out.println("__");
//    }

    public FormalScalarProductHelper getScalarProduct(Class f1Class, Class f2Class, int domainDimension) {
        FormalScalarProductHelper p = getScalarProduct0(f1Class, f2Class, domainDimension);
        if (p != null) {
            return p;
        }
        if (fallbackHelper != null) {
            log.log(Level.FINE, "Not unsupported Scalar product : <" + f1Class + "," + f2Class + ">, fallback to " + fallback);
            return fallbackHelper;
        }
        throw new NoSuchElementException("Not unsupported Scalar product : <" + f1Class + "," + f2Class + ">");
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + map.hashCode();
//        result = 31 * result + (cache != null ? cache.hashCode() : 0);
//        result = 31 * result + (noHelperCache != null ? noHelperCache.hashCode() : 0);
        result = 31 * result + (fallback != null ? fallback.hashCode() : 0);
//        result = 31 * result + (fallbackHelper != null ? fallbackHelper.hashCode() : 0);
        result = 31 * result + (expressionRewriter != null ? expressionRewriter.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FormalScalarProductOperator that = (FormalScalarProductOperator) o;

        if (!map.equals(that.map)) return false;
//        if (cache != null ? !cache.equals(that.cache) : that.cache != null) return false;
//        if (noHelperCache != null ? !noHelperCache.equals(that.noHelperCache) : that.noHelperCache != null)
//            return false;
        if (fallback != null ? !fallback.equals(that.fallback) : that.fallback != null) return false;
//        if (fallbackHelper != null ? !fallbackHelper.equals(that.fallbackHelper) : that.fallbackHelper != null)
//            return false;
        return expressionRewriter != null ? expressionRewriter.equals(that.expressionRewriter) : that.expressionRewriter == null;
    }

    private DoubleToDouble[] toCanonicalScalarProductPair(DoubleToDouble f1, DoubleToDouble f2) {
//        if(true){
//            return new DoubleToDouble[]{
//                    expressionRewriter.rewriteOrSame(f1).toDD(),
//                    expressionRewriter.rewriteOrSame(f2).toDD()
//            };
//        }
        Expr m = f1.mul(f2);
        Chronometer c = chrono();
        DoubleToDouble f1opt = expressionRewriter.rewriteOrSame(m, ExprType.DOUBLE_DOUBLE).toDD();
        return toCanonicalScalarProductPairExpansion(f1opt);

    }

//    public String dump() {
//        return getDumpStringHelper().toString();
//    }

    private DoubleToDouble[] toCanonicalScalarProductPairExpansion(DoubleToDouble expressionToExpand) {
        Domain domain = expressionToExpand.getDomain();
        int domainDimension = domain.getDimension();
        List<Expr> cst = new ArrayList<>();
        List<Expr> foundMul = new ArrayList<>();
        Expr e1 = null;
        Expr e2 = null;
        for (Expr expr : ((expressionToExpand instanceof Mul) ? expressionToExpand.getChildren() : Arrays.asList(expressionToExpand))) {
            if (expr.isNarrow(ExprType.COMPLEX_EXPR)) {
                cst.add(expr);
            } else {
                if (expr instanceof Pow) {
                    Expr powExpr = ((Pow) expr).getSecond();
                    if (powExpr.isNarrow(ExprType.DOUBLE_NBR) && powExpr.toDouble() == 2) {
                        foundMul.add(((Pow) expr).getFirst());
                        foundMul.add(((Pow) expr).getFirst());
                    } else {
                        foundMul.add(expr);
                    }
                } else {
                    foundMul.add(expr);
                }
            }
        }
        switch (foundMul.size()) {
            case 0: {
                switch (cst.size()) {
                    case 0: {
                        e1 = Maths.expr(domain);
                        e2 = Maths.expr(1);
                        break;
                    }
                    case 1: {
                        e1 = Maths.expr(domain);
                        e2 = cst.get(0);
                        break;
                    }
                    case 2: {
                        e1 = cst.get(0);
                        e2 = cst.get(1);
                        break;
                    }
                    default: {
                        e1 = cst.get(0);
                        e2 = Mul.of(cst.subList(1, cst.size()).toArray(new Expr[0])).simplify();
                        break;
                    }
                }
                break;
            }
            case 1: {
                e1 = foundMul.get(0);
                switch (cst.size()) {
                    case 0: {
                        e2 = Maths.expr(domain);
                        break;
                    }
                    case 1: {
                        e2 = cst.get(0);
                        break;
                    }
                    default: {
                        e2 = Mul.of(cst.toArray(new Expr[0])).simplify();
                        break;
                    }
                }
                break;
            }
            default: {
                e1 = foundMul.get(0);
                ArrayList<Expr> e2l = new ArrayList<>(foundMul.subList(1, foundMul.size()));
                e2l.addAll(cst);
                e2 = Mul.of(e2l.toArray(new Expr[0])).simplify();
                break;
            }

        }

        if (getScalarProduct0(e1.getClass(), e2.getClass(), domainDimension) != null) {
            return new DoubleToDouble[]{e1.toDD(), e2.toDD()};
        }
        throw new IllegalArgumentException("Unsupported expansion " + expressionToExpand.getClass() + " :: " + expressionToExpand);
    }

//    public Dumper getDumpStringHelper() {
//        Dumper sb = new Dumper(getClass().getSimpleName());
//        if (fallback != null) {
//            sb.add("fallback", fallback);
//        }
//        sb.add("hermitian", isHermitian());
//        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
//        return sb;
//    }

    public ExpressionRewriter getSimplifier() {
        return expressionRewriter;
    }

    @Override
    public String toString() {
        return dump();
//        return "FormalScalarProductOperator";
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.obj(getClass().getSimpleName());
        if (fallback != null) {
            sb.add("fallback", context.elem(fallback));
        }
        sb.add("hermitian", context.elem(isHermitian()));
        sb.add("hash", context.elem(Integer.toHexString(hashCode()).toUpperCase()));
        return sb.build();
    }

    private static class OperatorToFormalScalarProductHelper implements FormalScalarProductHelper {
        private final ScalarProductOperator fallback;

        private OperatorToFormalScalarProductHelper(ScalarProductOperator fallback) {
            this.fallback = fallback;
        }

        public double eval(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
            return this.fallback.evalDD(domain, f1, f2);
        }

        @Override
        public int hashCode() {
            return fallback != null ? fallback.hashCode() : 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OperatorToFormalScalarProductHelper)) return false;

            OperatorToFormalScalarProductHelper that = (OperatorToFormalScalarProductHelper) o;

            return fallback != null ? fallback.equals(that.fallback) : that.fallback == null;
        }
    }

}
