package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.integration.DQuadIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DRectLowIntegralXY;
import net.vpc.scholar.hadrumaths.integration.DRectMidIntegralXY;
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.formal.FormalScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.numeric.NumericScalarProductOperator;
import net.vpc.scholar.hadrumaths.scalarproducts.numeric.SimpleNumericScalarProductOperator;

/**
 * Created by vpc on 6/1/14.
 */
public class ScalarProductOperatorFactory extends AbstractFactory {
    public static final ScalarProductOperator NUMERIC_SCALAR_PRODUCT_OPERATOR = new NumericScalarProductOperator(true);
    public static final ScalarProductOperator SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR = new FormalScalarProductOperator(true, new SimpleNumericScalarProductOperator(true));
    public static final ScalarProductOperator HARD_FORMAL_SCALAR_PRODUCT_OPERATOR = new FormalScalarProductOperator(true, null);
    public static final ScalarProductOperator NUMERIC_SCALAR_PRODUCT_OPERATOR_NON_HERM = new NumericScalarProductOperator(false);
    public static final ScalarProductOperator SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR_NON_HERM = new FormalScalarProductOperator(false, new SimpleNumericScalarProductOperator(false));
    public static final ScalarProductOperator HARD_FORMAL_SCALAR_PRODUCT_OPERATOR_NON_HERM = new FormalScalarProductOperator(false, null);

    public static ScalarProductOperator defaultValue() {
        return numeric();
    }

    public static ScalarProductOperator numeric() {
        return NUMERIC_SCALAR_PRODUCT_OPERATOR;
    }


    public static ScalarProductOperator formal() {
        return SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR;
    }

    public static ScalarProductOperator softFormal() {
        return SOFT_FORMAL_SCALAR_PRODUCT_OPERATOR;
    }

    public static ScalarProductOperator hardFormal() {
        return HARD_FORMAL_SCALAR_PRODUCT_OPERATOR;
    }

    public static ScalarProductOperator formal(boolean hermitian, ScalarProductOperator fallack) {
        return new FormalScalarProductOperator(hermitian, fallack);
    }

    public static ScalarProductOperator quad() {
        return quad(true);
    }

    /**
     * create new instance of NumericScalarProductOperator with quad integrator
     * tolerance = 1E-6;
     * hminCoeff = 1E-52 / 1024.0;
     * hmaxCoeff = 1.0 / 32.0;
     * maxfcnt = 10000;
     *
     * @return new NumericScalarProductOperator(new DQuadIntegralXY())
     */
    public static ScalarProductOperator quad(boolean hermitian) {
        return new NumericScalarProductOperator(hermitian, new DQuadIntegralXY());
    }

    public static ScalarProductOperator quad(double tolerance, double minWindow, double maxWindow, int maxfcnt, boolean hermitian) {
        return new NumericScalarProductOperator(hermitian, new DQuadIntegralXY(tolerance, minWindow, maxWindow, maxfcnt));
    }

    public static ScalarProductOperator rectmid(boolean hermitian) {
        return rectmid(0, 0, 0, hermitian);
    }

    public static ScalarProductOperator rectmid(int xprecision, int yprecision, int zprecision, boolean hermitian) {
        return new NumericScalarProductOperator(hermitian, new DRectMidIntegralXY(xprecision, yprecision, zprecision));
    }

    public static ScalarProductOperator rectmid(int precision, boolean hermitian) {
        return rectmid(precision, precision, precision, hermitian);
    }

    public static ScalarProductOperator rectlow(boolean hermitian) {
        return rectlow(0, 0, 0, hermitian);
    }

    public static ScalarProductOperator rectlow(int xprecision, int yprecision, int zprecision, boolean hermitian) {
        return new NumericScalarProductOperator(hermitian, new DRectLowIntegralXY(xprecision, yprecision, zprecision));
    }

    public static ScalarProductOperator rectlow(int precision, boolean hermitian) {
        return rectlow(precision, precision, precision, hermitian);
    }
}
