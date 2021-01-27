package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.integration.IntegrationOperator;
import net.thevpc.scholar.hadrumaths.integration.formal.FormalIntegrationOperator;
import net.thevpc.scholar.hadrumaths.integration.formal.NumericIntegrationOperator;
import net.thevpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;

/**
 * Created by vpc on 6/1/14.
 */
public class IntegrationOperatorFactory extends AbstractFactory {
    public static final IntegrationOperator NUMERIC_SCALAR_PRODUCT_OPERATOR = new NumericIntegrationOperator();
    public static final IntegrationOperator SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR = new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR_NON_HERM);
    public static final IntegrationOperator HARD_FORMAL_SCALAR_PRODUCT_OPERATOR = new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.HARD_FORMAL_SCALAR_PRODUCT_OPERATOR_NON_HERM);

    public static IntegrationOperator defaultValue() {
        return softFormal();
    }

    public static IntegrationOperator softFormal() {
        return SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR;
    }

    public static IntegrationOperator numeric() {
        return NUMERIC_SCALAR_PRODUCT_OPERATOR;
    }

    public static IntegrationOperator formal() {
        return SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR;
    }

    public static IntegrationOperator hardFormal() {
        return HARD_FORMAL_SCALAR_PRODUCT_OPERATOR;
    }

//    public static IntegrationOperator formal(ScalarProductOperator fallack) {
//        return new FormalIntegrationOperator();
//    }

    /**
     * create new instance of NumericScalarProductOperator with quad integrator
     * tolerance = 1E-6;
     * hminCoeff = 1E-52 / 1024.0;
     * hmaxCoeff = 1.0 / 32.0;
     * maxfcnt = 10000;
     *
     * @return new NumericScalarProductOperator(new DQuadIntegralXY())
     */
    public static IntegrationOperator quad() {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.quad(false));
    }

    public static IntegrationOperator quad(double tolerance, double minWindow, double maxWindow, int maxfcnt) {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.quad(tolerance, minWindow, maxWindow, maxfcnt, false));
    }

    public static IntegrationOperator rectmid() {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.rectmid(false));
    }

    public static IntegrationOperator rectmid(int precision) {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.rectmid(precision, false));
    }

    public static IntegrationOperator rectmid(int xprecision, int yprecision, int zprecision) {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.rectmid(xprecision, yprecision, zprecision, false));
    }

    public static IntegrationOperator rectlow() {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.rectlow(false));
    }

    public static IntegrationOperator rectlow(int precision) {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.rectlow(precision, false));
    }

    public static IntegrationOperator rectlow(int xprecision, int yprecision, int zprecision) {
        return new FormalIntegrationOperator((FormalScalarProductOperator) ScalarProductOperatorFactory.rectlow(xprecision, yprecision, zprecision, false));
    }
}
