package net.thevpc.scholar.hadrumaths.scalarproducts.numeric;

import net.thevpc.tson.Tson;
import net.thevpc.tson.TsonElement;
import net.thevpc.tson.TsonObjectBuilder;
import net.thevpc.tson.TsonObjectContext;
import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.Maths;
import net.thevpc.scholar.hadrumaths.integration.DIntegralXY;
import net.thevpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.thevpc.scholar.hadrumaths.scalarproducts.AbstractScalarProductOperator;
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToDouble;
import net.thevpc.scholar.hadrumaths.symbolic.ExprType;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.thevpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.thevpc.scholar.hadrumaths.transform.IdentityExpressionRewriterRule;

import java.util.Objects;

public class SimpleNumericScalarProductOperator extends AbstractScalarProductOperator {

    private final DIntegralXY integrator;
    private ExpressionRewriterRuleSet simplifier;

    public SimpleNumericScalarProductOperator(boolean hermitian) {
        this(hermitian, new DQuadIntegralXY());
    }

    public SimpleNumericScalarProductOperator(boolean hermitian, DIntegralXY integrator) {
        super(hermitian);
        this.integrator = integrator;
    }

    public double evalDD(Domain domain, DoubleToDouble f1, DoubleToDouble f2) {
        Domain inter = f1.getDomain().intersect(f2.getDomain()).intersect(domain);
        Expr f0 = Maths.mul(f1, f2);
        DoubleToDouble sf = getSimplifier().rewriteOrSame(f0, ExprType.DOUBLE_DOUBLE).toDD();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SimpleNumericScalarProductOperator that = (SimpleNumericScalarProductOperator) o;
        return Objects.equals(integrator, that.integrator) &&
                Objects.equals(simplifier, that.simplifier);
    }

//    public String dump() {
//        return getDumpStringHelper().toString();
//    }
//
//    public Dumper getDumpStringHelper() {
//        Dumper sb = new Dumper(getClass().getSimpleName());
//        sb.add("integrator", integrator);
//        sb.add("hermitian", isHermitian());
//        sb.add("hash", Integer.toHexString(hashCode()).toUpperCase());
//        return sb;
//    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), integrator, simplifier);
    }

    public ExpressionRewriter getSimplifier() {
        if (simplifier == null) {
            simplifier = new ExpressionRewriterRuleSet("SimpleNumericScalarProductNoSimplifyRuleSet");
            simplifier.addRule(IdentityExpressionRewriterRule.INSTANCE);
            simplifier.setCacheEnabled(false);
        }
        return simplifier;
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder sb = Tson.ofObjectBuilder(getClass().getSimpleName());
        sb.add("integrator", context.elem(integrator));
        sb.add("hermitian", context.elem(isHermitian()));
        sb.add("simplifier", context.elem(getSimplifier()));
        return sb.build();
    }

    @Override
    public String toString() {
        return dump();
//        return "SimpleNumericScalarProductOperator{" +
//                "integrator=" + integrator +
//                ",hermitian=" + isHermitian() +
//                '}';
    }

}
