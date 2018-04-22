package net.vpc.scholar.hadrumaths.integration.formal;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.integration.AbstractIntegrationOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.MulAddLinerizeRule;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToCosXCosYRule;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToDDxyLinearRule;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterSuite;
import net.vpc.scholar.hadrumaths.util.ClassMapList;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vpc on 11/22/16.
 */
public class FormalIntegrationOperator extends AbstractIntegrationOperator{
    private List<FormalIntegrationOperatorHasher> hashers = new ArrayList<FormalIntegrationOperatorHasher>();
    private ClassMapList<FormalIntegrationOperatorHasher> hashersMap = new ClassMapList<FormalIntegrationOperatorHasher>();

    private final ExpressionRewriterSuite expressionRewriter = new ExpressionRewriterSuite("OPTIMIZE_SP");

    public FormalIntegrationOperator() {
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
    }

    public interface FormalIntegrationOperatorHasher {
        Class[] typeFilters();

        boolean accept(Expr expr);
    }

    public interface FormalIntegrationOperatorProcessor {
        double evalDD(Domain domain, DoubleToDouble f1);
    }

    public double evalDD(Domain domain, DoubleToDouble f1) {
        DoubleToDouble f1opt = expressionRewriter.rewriteOrSame(f1).toDD();
        domain = domain == null ? f1opt.getDomain() : domain.intersect(f1opt.getDomain());
        if (domain.isEmpty() || f1.isZero()) {
            return 0;
        }
        if (f1opt instanceof Any) {
            return evalDD(domain,Any.unwrap(f1opt).toDD());
        }else if (f1 instanceof Mul) {
            List<Expr> subExpressions = f1.getSubExpressions();
            if (subExpressions.size() == 0) {
                return 0;
            }
            if (subExpressions.size() == 1) {
                return ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR.evalDD(domain, subExpressions.get(0).toDD(), Maths.expr(domain));
            }
            if (subExpressions.size() == 2) {
                return ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR.evalDD(domain,
                        subExpressions.get(0).toDD(),
                        subExpressions.get(1).toDD());
            }
            return ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR.evalDD(domain, f1opt, Maths.expr(domain));
        }else if (f1 instanceof Plus) {
            List<Expr> subExpressions = f1.getSubExpressions();
            if (subExpressions.size() == 0) {
                return 0;
            }
            double d = 0;
            for (Expr subExpression : subExpressions) {
                d += evalDD(domain, subExpression.toDD());
            }
            return d;
        }else if (f1 instanceof Sub) {
            return evalDD(domain, ((Sub) f1).getFirst().toDD()) + evalDD(domain, ((Sub) f1).getSecond().toDD());
        }else if (f1.isDoubleExpr()) {
            return ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR.evalDD(domain, f1opt, Maths.expr(domain));
        }
        return ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR.evalDD(domain, f1opt, Maths.expr(domain));
    }

    public String dump() {
        return getDumpStringHelper().toString();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public Dumper getDumpStringHelper() {
        Dumper sb = new Dumper(getClass().getSimpleName());
        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
        return sb;
    }

    @Override
    public ExpressionRewriter getExpressionRewriter() {
        return expressionRewriter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormalIntegrationOperator that = (FormalIntegrationOperator) o;

        if (hashers != null ? !hashers.equals(that.hashers) : that.hashers != null) return false;
        if (hashersMap != null ? !hashersMap.equals(that.hashersMap) : that.hashersMap != null) return false;
        return expressionRewriter != null ? expressionRewriter.equals(that.expressionRewriter) : that.expressionRewriter == null;
    }

    @Override
    public int hashCode() {
        int result = hashers != null ? hashers.hashCode() : 0;
        result = 31 * result + (hashersMap != null ? hashersMap.hashCode() : 0);
        result = 31 * result + (expressionRewriter != null ? expressionRewriter.hashCode() : 0);
        return result;
    }
}
