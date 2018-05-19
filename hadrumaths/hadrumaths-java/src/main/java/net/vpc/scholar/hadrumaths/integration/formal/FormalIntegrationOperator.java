package net.vpc.scholar.hadrumaths.integration.formal;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.integration.AbstractIntegrationOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.MulAddLinerizeRule;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToCosXCosYRule;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.rewriter.ToDDxyLinearRule;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.Mul;
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
public class FormalIntegrationOperator extends AbstractIntegrationOperator {
    private List<FormalIntegrationOperatorHasher> hashers = new ArrayList<FormalIntegrationOperatorHasher>();
    private ClassMapList<FormalIntegrationOperatorHasher> hashersMap = new ClassMapList<FormalIntegrationOperatorHasher>();

    private final ExpressionRewriterSuite expressionRewriter = new ExpressionRewriterSuite("OPTIMIZE_SP");
    private FormalScalarProductOperator formalOperator;

    public FormalIntegrationOperator(FormalScalarProductOperator formalOperator) {
        this.formalOperator = formalOperator;
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
        FormalScalarProductOperator delegateScalarProductOperator = formalOperator;
        DoubleToDouble f1opt = delegateScalarProductOperator.getExpressionRewriter().rewriteOrSame(f1).toDD();
        domain = domain == null ? f1opt.getDomain() : domain.intersect(f1opt.getDomain());
        if (domain.isEmpty() || f1opt.isZero()) {
            return 0;
        }
        if (f1opt instanceof Any) {
            return evalDD(domain, Any.unwrap(f1opt).toDD());
        } else if (f1opt instanceof Mul) {
            List<Expr> subExpressions = f1opt.getSubExpressions();
            if (subExpressions.size() == 0) {
                return 0;
            }
            if (subExpressions.size() == 1) {
                return delegateScalarProductOperator.evalDD(domain, subExpressions.get(0).toDD(), Maths.expr(domain));
            }
            if (subExpressions.size() == 2) {
                return delegateScalarProductOperator.evalDD(domain,
                        subExpressions.get(0).toDD(),
                        subExpressions.get(1).toDD());
            }
            TVector<Expr> others = new ExprArrayList(true, subExpressions.size());
            for (int i = 1; i < subExpressions.size(); i++) {
                others.add(subExpressions.get(i));
            }
            return delegateScalarProductOperator.evalDD(domain, subExpressions.get(0).toDD(), new Mul(others));
        } else {
            FormalScalarProductOperator ff = (FormalScalarProductOperator) delegateScalarProductOperator;
            if (ff.getScalarProduct0(f1opt.getClass(), DoubleValue.class, 1) != null) {
                return delegateScalarProductOperator.evalDD(f1opt.toDD(), Maths.expr(domain));
            }
        }
        return delegateScalarProductOperator.evalDD(domain, f1opt, Maths.expr(domain));
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
