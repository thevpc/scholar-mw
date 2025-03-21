package net.thevpc.scholar.hadrumaths.integration.formal;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.*;
import net.thevpc.scholar.hadrumaths.integration.AbstractIntegrationOperator;
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.thevpc.scholar.hadrumaths.scalarproducts.numeric.NumericScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.thevpc.scholar.hadrumaths.symbolic.ExprRef;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;

import java.util.List;

import static net.thevpc.scholar.hadrumaths.Maths.mul;
import static net.thevpc.scholar.hadrumaths.Maths.prod;

/**
 * Created by vpc on 11/22/16.
 */
public class NumericIntegrationOperator extends AbstractIntegrationOperator {
    public NumericIntegrationOperator() {
    }

    public double evalDD(Domain domain, DoubleToDouble f1) {
        FormalScalarProductOperator delegateScalarProductOperator = getFormalScalarProductOperator();
        ScalarProductOperator delegateNumericProductOperator = getNumericScalarProductOperator();
        DoubleToDouble f1opt = delegateScalarProductOperator.getSimplifier().rewriteOrSame(f1, ExprType.DOUBLE_DOUBLE).toDD();
        domain = domain == null ? f1opt.getDomain() : domain.intersect(f1opt.getDomain());
        if (domain.isEmpty() || f1opt.isZero()) {
            return 0;
        }
        if (f1opt instanceof ExprRef) {
            return evalDD(domain, ((ExprRef) f1opt).getReference().toDD());
        } else if (f1opt instanceof Mul) {
            List<Expr> subExpressions = f1opt.getChildren();
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
            Vector<Expr> others = DefaultExprVector.of(true, subExpressions.size());
            for (int i = 1; i < subExpressions.size(); i++) {
                others.add(subExpressions.get(i));
            }
            return delegateNumericProductOperator.evalDD(domain, subExpressions.get(0).toDD(), prod(others.toArray(Expr.class)).toDD());
        } else if (f1opt instanceof Plus) {
            List<Expr> subExpressions = f1opt.getChildren();
            double ret = 0;
            for (Expr subExpression : subExpressions) {
                ret += evalDD(subExpression.toDD());
            }
            return ret;
        } else {
            if (delegateScalarProductOperator.isSupported(f1opt.getClass(), DoubleValue.class, 1)) {
                return delegateScalarProductOperator.evalDD(f1opt.toDD(), Maths.expr(domain));
            }
        }
        return delegateNumericProductOperator.evalDD(domain, f1opt, Maths.expr(domain));
    }

    @Override
    public ExpressionRewriter getSimplifier() {
        return getFormalScalarProductOperator().getSimplifier();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

//    public String dump() {
//        return getDumpStringHelper().toString();
//    }

    //    public Dumper getDumpStringHelper() {
//        Dumper sb = new Dumper(getClass().getSimpleName());
//        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
//        sb.add("fspo", getFormalScalarProductOperator());
//        sb.add("nspo", getNumericScalarProductOperator());
//        return sb;
//    }
    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.ofObj(getClass().getSimpleName())
                .add("hash", Tson.of(Integer.toHexString(hashCode()).toUpperCase()))
                .add("fspo", context.elem(getFormalScalarProductOperator()))
                .add("nspo", context.elem(getNumericScalarProductOperator()))
                .build();
    }

    private FormalScalarProductOperator getFormalScalarProductOperator() {
        return (FormalScalarProductOperator) ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR_NON_HERM;
    }

    private NumericScalarProductOperator getNumericScalarProductOperator() {
        return (NumericScalarProductOperator) ScalarProductOperatorFactory.NUMERIC_SCALAR_PRODUCT_OPERATOR_NON_HERM;
    }


}
