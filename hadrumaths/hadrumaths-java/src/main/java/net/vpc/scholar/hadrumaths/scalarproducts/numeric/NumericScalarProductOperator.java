package net.vpc.scholar.hadrumaths.scalarproducts.numeric;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.MathsBase;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.transform.IdentityExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

public class NumericScalarProductOperator extends AbstractScalarProductOperator {

    private DIntegralXY integrator;
    private ExpressionRewriterRuleSet simplifier;

    public NumericScalarProductOperator(boolean hermitian) {
        this(hermitian,new DQuadIntegralXY());
    }

    public NumericScalarProductOperator(boolean hermitian, DIntegralXY integrator) {
        super(hermitian);
        this.integrator = integrator;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        ExpressionRewriter ew = getExpressionRewriter();
        f1 = ew.rewriteOrSame(f1).toDD();
        f2 = ew.rewriteOrSame(f2).toDD();
        Domain inter = f1.getDomain().intersect(f2.getDomain()).intersect(domain);
        if (inter.isEmpty() || f1.isZero() || f2.isZero()) {
            return 0;
        }
        switch (inter.getDimension()) {
            case 1: {
                return integrator.integrateX(MathsBase.mul(f1, f2).toDD(), inter.xmin(), inter.xmax());
            }
            case 2: {
                return integrator.integrateXY(MathsBase.mul(f1, f2).toDD(), inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax());
            }
            case 3: {
                return integrator.integrateXYZ(MathsBase.mul(f1, f2).toDD(), inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax(), inter.zmin(), inter.zmax());
            }
        }
        throw new IllegalArgumentException("Unsupported dimension");
    }

    public String dump() {
        return getDumpStringHelper().toString();
    }

    public Dumper getDumpStringHelper() {
        Dumper sb = new Dumper(getClass().getSimpleName());
        sb.add("integrator", integrator);
        sb.add("hermitian", isHermitian());
        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
        return sb;
    }

    @Override
    public String toString() {
        return "NumericScalarProductOperator{" +
                "integrator=" + integrator +
                ",hermitian=" + isHermitian() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumericScalarProductOperator)) return false;

        NumericScalarProductOperator that = (NumericScalarProductOperator) o;

        if (integrator != null ? !integrator.equals(that.integrator) : that.integrator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return integrator != null ? integrator.hashCode() : 0;
    }

    public ExpressionRewriter getExpressionRewriter() {
        if (simplifier == null) {
            simplifier = new ExpressionRewriterRuleSet("NumericScalarProductNoSimplifyRuleSet");
            simplifier.addRule(IdentityExpressionRewriterRule.INSTANCE);
        }
        return simplifier;
    }

}
