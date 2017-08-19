package net.vpc.scholar.hadrumaths.scalarproducts.numeric;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.util.dump.Dumper;

public class SimpleNumericScalarProductOperator extends AbstractScalarProductOperator {

    private DIntegralXY integrator;
    private ExpressionRewriterRuleSet NO_SIMPLIFIER;

    public SimpleNumericScalarProductOperator() {
        this(new DQuadIntegralXY());
    }

    public SimpleNumericScalarProductOperator(DIntegralXY integrator) {
        this.integrator = integrator;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        Domain inter = f1.getDomain().intersect(f2.getDomain()).intersect(domain);
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

    public String dump() {
        return getDumpStringHelper().toString();
    }

    public Dumper getDumpStringHelper() {
        Dumper sb = new Dumper(getClass().getSimpleName());
        sb.add("integrator", integrator);
        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
        return sb;
    }

    @Override
    public String toString() {
        return "NumericScalarProductOperator{" +
                "integrator=" + integrator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleNumericScalarProductOperator)) return false;

        SimpleNumericScalarProductOperator that = (SimpleNumericScalarProductOperator) o;

        if (integrator != null ? !integrator.equals(that.integrator) : that.integrator != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return integrator != null ? integrator.hashCode() : 0;
    }

    public ExpressionRewriter getExpressionRewriter() {
        if (NO_SIMPLIFIER == null) {
            NO_SIMPLIFIER = new ExpressionRewriterRuleSet("NO_SIMPLIFY");
            NO_SIMPLIFIER.setCacheEnabled(false);
        }
        return NO_SIMPLIFIER;
    }

}
