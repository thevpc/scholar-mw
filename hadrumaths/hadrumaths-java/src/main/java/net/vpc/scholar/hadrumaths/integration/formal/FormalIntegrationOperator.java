package net.vpc.scholar.hadrumaths.integration.formal;

import net.vpc.common.tson.Tson;
import net.vpc.common.tson.TsonElement;
import net.vpc.common.tson.TsonObjectContext;
import net.vpc.common.util.ClassMapList;
import net.vpc.scholar.hadrumaths.*;
import net.vpc.scholar.hadrumaths.integration.AbstractIntegrationOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.symbolic.DoubleValue;
import net.vpc.scholar.hadrumaths.symbolic.ExprRef;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.num.Mul;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;

import java.util.ArrayList;
import java.util.List;

import static net.vpc.scholar.hadrumaths.Maths.expr;
import static net.vpc.scholar.hadrumaths.Maths.mul;

/**
 * Created by vpc on 11/22/16.
 */
public class FormalIntegrationOperator extends AbstractIntegrationOperator {
    private final List<FormalIntegrationOperatorHasher> hashers = new ArrayList<FormalIntegrationOperatorHasher>();
    private final ClassMapList<FormalIntegrationOperatorHasher> hashersMap = new ClassMapList<FormalIntegrationOperatorHasher>(FormalIntegrationOperatorHasher.class);

    private final FormalScalarProductOperator formalOperator;

    public FormalIntegrationOperator(FormalScalarProductOperator formalOperator) {
        this.formalOperator = formalOperator;
    }

    public double evalDD(Domain domain, DoubleToDouble f1) {
        FormalScalarProductOperator delegateScalarProductOperator = formalOperator;
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
                return delegateScalarProductOperator.evalDD(domain, subExpressions.get(0).toDD(), expr(domain));
            }
            if (subExpressions.size() == 2) {
                return delegateScalarProductOperator.evalDD(domain,
                        subExpressions.get(0).toDD(),
                        subExpressions.get(1).toDD());
            }
            Vector<Expr> others = DefaultExprVector.of(true, subExpressions.size());
            for (int i = 1; i < subExpressions.size(); i++) {
                others.add(subExpressions.get(i));
            }
            return delegateScalarProductOperator.evalDD(domain, subExpressions.get(0).toDD(), mul(others.toArray(Expr.class)).toDD());
        } else {
            FormalScalarProductOperator ff = delegateScalarProductOperator;
            if (ff.getScalarProduct0(f1opt.getClass(), DoubleValue.class, 1) != null) {
                return delegateScalarProductOperator.evalDD(f1opt.toDD(), expr(domain));
            }
        }
        return delegateScalarProductOperator.evalDD(domain, f1opt, expr(domain));
    }

    @Override
    public ExpressionRewriter getSimplifier() {
        return formalOperator.getSimplifier();
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        return Tson.obj(getClass().getSimpleName())
                .add("hash", Tson.elem(Integer.toHexString(hashCode()).toUpperCase()))
                .add("spo", context.elem(formalOperator))
                .build();
    }

//    public String dump() {
//        return getDumpStringHelper().toString();
//    }

    @Override
    public int hashCode() {
        int result = hashers != null ? hashers.hashCode() : 0;
        result = 31 * result + (hashersMap != null ? hashersMap.hashCode() : 0);
        result = 31 * result + (formalOperator != null ? formalOperator.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormalIntegrationOperator that = (FormalIntegrationOperator) o;

        if (hashers != null ? !hashers.equals(that.hashers) : that.hashers != null) return false;
        if (hashersMap != null ? !hashersMap.equals(that.hashersMap) : that.hashersMap != null) return false;
        return formalOperator != null ? formalOperator.equals(that.formalOperator) : that.formalOperator == null;
    }

//    public Dumper getDumpStringHelper() {
//        Dumper sb = new Dumper(getClass().getSimpleName());
//        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
//        sb.add("spo", formalOperator);
//        return sb;
//    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public interface FormalIntegrationOperatorHasher {
        Class[] typeFilters();

        boolean accept(Expr expr);
    }

    public interface FormalIntegrationOperatorProcessor {
        double evalDD(Domain domain, DoubleToDouble f1);
    }
}
