package net.vpc.scholar.hadrumaths.integration.formal;

import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.integration.AbstractIntegrationOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.numeric.NumericScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.*;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.dump.Dumper;

import java.util.List;

/**
 * Created by vpc on 11/22/16.
 */
public class NumericIntegrationOperator extends AbstractIntegrationOperator {
    public NumericIntegrationOperator() {
    }

    private FormalScalarProductOperator getFormalScalarProductOperator(){
        return (FormalScalarProductOperator) ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR_NON_HERM;
    }
    private NumericScalarProductOperator getNumericScalarProductOperator(){
        return (NumericScalarProductOperator) ScalarProductOperatorFactory.NUMERIC_SCALAR_PRODUCT_OPERATOR_NON_HERM;
    }
    public double evalDD(Domain domain, DoubleToDouble f1) {
        FormalScalarProductOperator delegateScalarProductOperator = getFormalScalarProductOperator();
        ScalarProductOperator delegateNumericProductOperator = getNumericScalarProductOperator();
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
                return delegateNumericProductOperator.evalDD(domain, subExpressions.get(0).toDD(), Maths.expr(domain));
            }
            if (subExpressions.size() == 2) {
                return delegateNumericProductOperator.evalDD(domain,
                        subExpressions.get(0).toDD(),
                        subExpressions.get(1).toDD());
            }
            TVector<Expr> others = new ExprArrayList(true, subExpressions.size());
            for (int i = 1; i < subExpressions.size(); i++) {
                others.add(subExpressions.get(i));
            }
            return delegateNumericProductOperator.evalDD(domain, subExpressions.get(0).toDD(), new Mul(others));
        } else if (f1opt instanceof Plus) {
            List<Expr> subExpressions = f1opt.getSubExpressions();
            double ret=0;
            for (Expr subExpression : subExpressions) {
                ret+=evalDD(subExpression.toDD());
            }
            return ret;
        } else {
            if (delegateScalarProductOperator.isSupported(f1opt.getClass(), DoubleValue.class, 1)) {
                return delegateScalarProductOperator.evalDD(f1opt.toDD(), Maths.expr(domain));
            }
        }
        return delegateNumericProductOperator.evalDD(domain, f1opt, Maths.expr(domain));
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
        sb.add("fspo", getFormalScalarProductOperator());
        sb.add("nspo", getNumericScalarProductOperator());
        return sb;
    }

    @Override
    public ExpressionRewriter getExpressionRewriter() {
        return getFormalScalarProductOperator().getExpressionRewriter();
    }


}
