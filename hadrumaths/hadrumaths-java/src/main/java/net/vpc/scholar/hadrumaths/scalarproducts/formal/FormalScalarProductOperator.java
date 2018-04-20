package net.vpc.scholar.hadrumaths.scalarproducts.formal;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.ExpressionRewriterFactory;
import net.vpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.MulAddLinerizeRule;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToCosXCosYRule;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToDDxyLinearRule;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterSuite;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormalScalarProductOperator extends AbstractScalarProductOperator {

    private final static Logger log = Logger.getLogger(FormalScalarProductOperator.class.getName());
    private Map<ClassClassKey, FormalScalarProductHelper> map = new HashMap<ClassClassKey, FormalScalarProductHelper>();
    private Map<ClassClassKey, FormalScalarProductHelper> cache = new HashMap<ClassClassKey, FormalScalarProductHelper>();
    private Set<ClassClassKey> noHelperCache = new HashSet<ClassClassKey>();
    private ScalarProductOperator fallback;
    private FormalScalarProductHelper fallbackHelper;

    private final ExpressionRewriterSuite expressionRewriter = new ExpressionRewriterSuite("OPTIMIZE_SP");

    public FormalScalarProductOperator(ScalarProductOperator fallback) {
        this.fallback = fallback;
        if (fallback != null) {
            fallbackHelper = new OperatorToFormalScalarProductHelper(this.fallback);
        }

        expressionRewriter.clear();
        ExpressionRewriterRuleSet CANONICAL_RULE_SET = new ExpressionRewriterRuleSet("CANONICAL");
        for (ExpressionRewriterRule r : ExpressionRewriterFactory.NAVIGATION_RULES) {
            CANONICAL_RULE_SET.addRule(r);
        }
        for (ExpressionRewriterRule r : ExpressionRewriterFactory.CANONICAL_RULES) {
            CANONICAL_RULE_SET.addRule(r);
        }
        expressionRewriter.add(CANONICAL_RULE_SET);


        ExpressionRewriterRuleSet EXPAND_SIMPLIFY_RULE_SET = new ExpressionRewriterRuleSet("EXPAND_SIMPLIFY");
        for (ExpressionRewriterRule r : ExpressionRewriterFactory.SIMPLIFY_RULES) {
            EXPAND_SIMPLIFY_RULE_SET.addRule(r);
        }
        EXPAND_SIMPLIFY_RULE_SET.addRule(MulAddLinerizeRule.INSTANCE);
        EXPAND_SIMPLIFY_RULE_SET.addRule(ToCosXCosYRule.INSTANCE);
        EXPAND_SIMPLIFY_RULE_SET.addRule(ToDDxyLinearRule.INSTANCE);
        expressionRewriter.add(EXPAND_SIMPLIFY_RULE_SET);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(),2);
        register(CosXCosY.class, CosXPlusY.class, new CosCosVsCosXPlusYProduct(),2);
        register(CosXPlusY.class, CosXPlusY.class, new CosXPlusYVsCosXPlusYProduct(),2);
        register(CosXPlusY.class, DoubleValue.class, new CosXPlusYVsCstProduct(),2);
        register(CosXPlusY.class, Linear.class, new CosXPlusYVsLinearProduct(),2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(CosXCosY.class, DoubleValue.class, new CosCosVsDoubleXYScalarProduct(),2);
        register(CosXCosY.class, DoubleValue.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(CosXCosY.class, Linear.class, new CosCosVsLinearScalarProduct(),2);
        register(CosXCosY.class, Linear.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(Linear.class, Linear.class, new LinearVsLinearScalarProduct(),2);
        register(Linear.class, Linear.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(Linear.class, DoubleValue.class, new LinearVsCstScalarProduct(),2);
        register(Linear.class, DoubleValue.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(DoubleValue.class, DoubleValue.class, new DoubleDoubleValueVsDoubleDoubleValueScalarProduct(),1,2,3);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(),2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(),2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(),2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(CosXCosY.class, CosXCosY.class, new CosCosVsCosCosScalarProduct(),2);
        register(CosXCosY.class, CosXCosY.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(CosXCosY.class, UFunction.class, new CosCosVsUScalarProduct(),2);
        register(CosXCosY.class, UFunction.class, Domain2To1ScalarProduct.INSTANCE,1);

        register(DDiscrete.class, Object.class, new DDiscreteVsAnyScalarProduct(),1,2,3);

//        register(DDxyDisc DDiscreteVsAnyScalarProduct DDxyDiscreteVsAnyScalarProduct());
        register(Plus.class, Object.class, new PlusVsAnyScalarProduct(),2);
        register(Plus.class, Object.class, Domain2To1ScalarProduct.INSTANCE,1);
    }

//    public FormalScalarProductHelper getScalarProduct(Class f1Class, Class f2Class) {
//
//    }
    public FormalScalarProductHelper getScalarProduct(Class f1Class, Class f2Class,int domainDimension) {
        ClassClassKey c0 = new ClassClassKey(f1Class, f2Class,domainDimension);
        FormalScalarProductHelper p = cache.get(c0);
        if (p != null) {
            return p;
        }
        if(!noHelperCache.contains(c0)) {
            ClassClassKey c;
            Stack<ClassClassKey> stack = new Stack<ClassClassKey>();
            stack.push(c0);
            while (!stack.isEmpty()) {
                c = stack.pop();
                p = map.get(c);
                if (p != null) {
                    break;
                }
                p = map.get(c.invert());
                if (p != null) {
                    p = new ReverseFormalScalarProductHelper(p);
                    break;
                }
                if (!c.c1.equals(Object.class)) {
                    stack.push(new ClassClassKey(c.c1.getSuperclass(), c.c2, domainDimension));
                }
                if (!c.c2.equals(Object.class)) {
                    stack.push(new ClassClassKey(c.c1, c.c2.getSuperclass(), domainDimension));
                }
            }
        }
        if (p != null) {
            cache.put(c0, p);
            return p;
        }else{
            noHelperCache.add(c0);
        }
        if (fallbackHelper != null) {
            log.log(Level.FINE, "Not unsupported Scalar product : <" + f1Class + "," + f2Class + ">, fallback to " + fallback);
            return fallbackHelper;
        }
        throw new NoSuchElementException("Not unsupported Scalar product : <" + f1Class + "," + f2Class + ">");
    }

    public void register(Class f1, Class f2,FormalScalarProductHelper sp, int domainDimension,int ... domainDimensions) {
        TreeSet<Integer> all=new TreeSet<Integer>();
        all.add(domainDimension);
        for (int dimension : domainDimensions) {
            all.add(dimension);
        }
        for (Integer dim : all) {
            ClassClassKey c = new ClassClassKey(f1, f2,dim);
            map.put(c, sp);
            cache.clear();
            noHelperCache.clear();
            cache.put(c, sp);
        }
    }

    public double[] evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble[] f2) {
        DoubleToDouble f1opt= expressionRewriter.rewriteOrSame(f1).toDD();
        if(f1opt instanceof Mul){
            f1opt= expressionRewriter.rewriteOrSame(f1).toDD();
        }
        double[] rets=new double[f2.length];
        boolean f1zero = f1opt.isZero();
        Domain f1domain = f1opt.getDomain().intersect(domain);
        for (int i = 0; i < rets.length; i++) {
            DoubleToDouble f2i = f2[i];
            DoubleToDouble f2opt= expressionRewriter.rewriteOrSame(f2i).toDD();
//            if(f2opt instanceof Mul){
//                f2opt= expressionRewriter.rewriteOrSame(f2i).toDD();
//            }
            Domain inter = f1domain.intersect(f2opt.getDomain());
            if (f1zero || f2opt.isZero()) {
                rets[i]=0;
            }else if (inter.isEmpty()) {
                rets[i]=0;
            }else if (inter.isInfinite()) {
                throw new IllegalArgumentException("Cannot integrate over infinite interval "+inter);
            }else {
                rets[i] = getScalarProduct(f1opt.getClass(), f2opt.getClass(), inter.getDimension()).compute(inter, f1opt, f2opt, this);
            }
        }
        return rets;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        DoubleToDouble f1opt= expressionRewriter.rewriteOrSame(f1).toDD();
        DoubleToDouble f2opt= expressionRewriter.rewriteOrSame(f2).toDD();
//        if(f1opt instanceof Mul){
//            f1opt= expressionRewriter.rewriteOrSame(f1).toDD();
//        }
//        if(f2opt instanceof Mul){
//            f2opt= expressionRewriter.rewriteOrSame(f2).toDD();
//        }
        Domain inter = f1opt.getDomain().intersect(f2opt.getDomain()).intersect(domain);
        if (f1opt.isZero() || f2opt.isZero()) {
            return 0;
        }
        if (inter.isEmpty()) {
            return 0;
        }
        if (inter.isInfinite()) {
            throw new IllegalArgumentException("Cannot integrate over infinite interval "+inter);
        }
        return getScalarProduct(f1opt.getClass(), f2opt.getClass(),inter.getDimension()).compute(inter, f1opt, f2opt, this);
    }

    public ExpressionRewriter getExpressionRewriter(){
        return expressionRewriter;
    }

    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public String toString() {
        return "FormalScalarProductOperator";
    }

    public Dumper getDumpStringHelper() {
        Dumper sb = new Dumper(getClass().getSimpleName());
        sb.add("fallback", fallback);
        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
        return sb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormalScalarProductOperator)) return false;

        FormalScalarProductOperator that = (FormalScalarProductOperator) o;

        if (expressionRewriter != null ? !expressionRewriter.equals(that.expressionRewriter) : that.expressionRewriter != null) return false;
        if (fallback != null ? !fallback.equals(that.fallback) : that.fallback != null) return false;
        if (map != null ? !map.equals(that.map) : that.map != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = map != null ? map.hashCode() : 0;
        result = 31 * result + (fallback != null ? fallback.hashCode() : 0);
        result = 31 * result + (expressionRewriter != null ? expressionRewriter.hashCode() : 0);
        return result;
    }

//    public static void main(String[] args) {
//        for (int i = 0; i < 2; i++) {
//            System.out.println("-----------------------------");
//            FormalScalarProductOperator formalScalarProductOperator = new FormalScalarProductOperator(null);
//            dump(formalScalarProductOperator);
////            for (ExpressionRewriter set : formalScalarProductOperator.expressionRewriter.sets) {
////                ExpressionRewriterRuleSet s=(ExpressionRewriterRuleSet)set;
////                dump(s.mapRules);
////            }
////            for (Map.Entry<ClassClassKey, FormalScalarProductHelper> entry : formalScalarProductOperator.map.entrySet()) {
////                System.out.println(entry.getValue().getClass().getSimpleName()+":"+entry.getKey().hashCode()+":"+entry.getValue().hashCode());
////            }
//        }
//    }

    private static void dump(Object o){
        System.out.println(o.getClass().getSimpleName()+":"+o.hashCode());
    }

    private static class OperatorToFormalScalarProductHelper implements FormalScalarProductHelper {
        private ScalarProductOperator fallback;

        private OperatorToFormalScalarProductHelper(ScalarProductOperator fallback) {
            this.fallback = fallback;
        }

        public double compute(Domain domain, DoubleToDouble f1, DoubleToDouble f2, FormalScalarProductOperator sp) {
            return this.fallback.evalDD(domain, f1, f2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OperatorToFormalScalarProductHelper)) return false;

            OperatorToFormalScalarProductHelper that = (OperatorToFormalScalarProductHelper) o;

            if (fallback != null ? !fallback.equals(that.fallback) : that.fallback != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return fallback != null ? fallback.hashCode() : 0;
        }
    }

}
