package net.vpc.scholar.hadrumaths.scalarproducts.numeric;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.integration.DIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.vpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.dump.Dumper;

public class SimpleNumericScalarProductOperator extends AbstractScalarProductOperator {

    private DIntegralXY integrator;
    private ExpressionRewriterRuleSet simplifier;

    public SimpleNumericScalarProductOperator(boolean hermitian) {
        this(hermitian,new DQuadIntegralXY());
    }

    public SimpleNumericScalarProductOperator(boolean hermitian,DIntegralXY integrator) {
        super(hermitian);
        this.integrator = integrator;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        Domain inter = f1.getDomain().intersect(f2.getDomain()).intersect(domain);
        Expr f0 = Maths.mul(f1, f2);
        DoubleToDouble sf = getExpressionRewriter().rewriteOrSame(f0).toDD();
//        Plot.title(f0.toString()).plot(f0,sf);
        switch (inter.getDimension()) {
            case 1: {
                return integrator.integrateX(sf, inter.xmin(), inter.xmax());
//                double v1 = integrator.integrateX(sf, inter.xmin(), inter.xmax());
//                double v2 = integrator.integrateX(f0.toDD(), inter.xmin(), inter.xmax());
//                System.out.println(Maths.rerr(v1,v2)+" -- "+v1+" -- "+v2);
//                return v1;
            }
            case 2: {
                return integrator.integrateXY(sf, inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax());
//                double v1 = integrator.integrateXY(sf, inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax());
//                double v2 = integrator.integrateXY(f0.toDD(), inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax());
//                if(v1!=v2) {
//                    System.out.println(Maths.rerr(v1, v2) + " -- " + v1 + " -- " + v2);
//                }
//                return v1;
            }
            case 3: {
                return integrator.integrateXYZ(sf, inter.xmin(), inter.xmax(), inter.ymin(), inter.ymax(), inter.zmin(), inter.zmax());
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
        if (simplifier == null) {
            simplifier = new ExpressionRewriterRuleSet("NO_SIMPLIFY");
            simplifier.setCacheEnabled(false);
        }
        return simplifier;
    }

}
