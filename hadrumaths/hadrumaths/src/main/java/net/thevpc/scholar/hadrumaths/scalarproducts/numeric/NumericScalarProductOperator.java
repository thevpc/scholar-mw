package net.thevpc.scholar.hadrumaths.scalarproducts.numeric;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.integration.DIntegralXY;
import net.thevpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.thevpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.thevpc.scholar.hadrumaths.transform.IdentityExpressionRewriterRule;

public class NumericScalarProductOperator extends AbstractScalarProductOperator {

    private final DIntegralXY integrator;
    private ExpressionRewriterRuleSet simplifier;

    public NumericScalarProductOperator(boolean hermitian) {
        this(hermitian, new DQuadIntegralXY());
    }

    public NumericScalarProductOperator(boolean hermitian, DIntegralXY integrator) {
        super(hermitian);
        this.integrator = integrator;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        ExpressionRewriter ew = getSimplifier();
        f1 = ew.rewriteOrSame(f1, ExprType.DOUBLE_DOUBLE).toDD();
        f2 = ew.rewriteOrSame(f2, ExprType.DOUBLE_DOUBLE).toDD();
        Domain inter = f1.getDomain().intersect(f2.getDomain()).intersect(domain);
        if (inter.isEmpty() || f1.isZero() || f2.isZero()) {
            return 0;
        }
        switch (inter.getDimension()) {
            case 1: {
                return integrator.integrateX(Maths.mul(f1, f2).toDD(), inter.xmin(), inter.xmax());
            }
            case 2: {
                return integrator.integrateXY(Maths.mul(f1, f2).toDD(), inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax());
            }
            case 3: {
                return integrator.integrateXYZ(Maths.mul(f1, f2).toDD(), inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax(), inter.zmin(), inter.zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported dimension");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumericScalarProductOperator)) return false;

        NumericScalarProductOperator that = (NumericScalarProductOperator) o;

        return integrator != null ? integrator.equals(that.integrator) : that.integrator == null;
    }

    @Override
    public int hashCode() {
        return integrator != null ? integrator.hashCode() : 0;
    }

    public ExpressionRewriter getSimplifier() {
        if (simplifier == null) {
            simplifier = new ExpressionRewriterRuleSet("NumericScalarProductNoSimplifyRuleSet");
            simplifier.addRule(IdentityExpressionRewriterRule.INSTANCE);
        }
        return simplifier;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.ofObj(getClass().getSimpleName());
        sb.add("integrator", context.elem(integrator));
        sb.add("hermitian", context.elem(isHermitian()));
        sb.add("hash", context.elem(Integer.toHexString(hashCode()).toUpperCase()));
        return sb.build();
    }

    @Override
    public String toString() {
        return dump();
    }

}
