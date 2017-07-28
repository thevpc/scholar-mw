/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths;

import net.vpc.scholar.hadrumaths.transform.ExpressionRewriter;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRule;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterRuleSet;
import net.vpc.scholar.hadrumaths.transform.ExpressionRewriterSuite;
import net.vpc.scholar.hadrumaths.transform.canonicalrules.CosXCosYSymbolRule;
import net.vpc.scholar.hadrumaths.transform.canonicalrules.CosXPlusYSymbolRule;
import net.vpc.scholar.hadrumaths.transform.canonicalrules.DDxyLinearSymbolRule;
import net.vpc.scholar.hadrumaths.transform.canonicalrules.ReplaceDeprecatedRule;
import net.vpc.scholar.hadrumaths.transform.navigaterules.*;
import net.vpc.scholar.hadrumaths.transform.simplifycore.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author vpc
 */
public class ExpressionRewriterFactory extends AbstractFactory {


    public static final List<ExpressionRewriterRule> NAVIGATION_RULES = new ArrayList<ExpressionRewriterRule>(
            Arrays.asList(
                    AnyNavRule.INSTANCE
                    , ComplexNavRule.INSTANCE
                    , ComplexXYNavRule.INSTANCE
                    , DCxyExpNavRule.INSTANCE
                    , DCxyNavRule.INSTANCE
//                    ,DDxCosNavRule.INSTANCE
//                    ,DDxIntegralXNavRule.INSTANCE
//                    ,DDxLinearNavRule.INSTANCE
//                    ,DDxPolynomeNavRule.INSTANCE
//                    ,DDxUxNavRule.INSTANCE
//                    ,DDxyToDDxNavRule.INSTANCE
                    , DDyNavRule.INSTANCE
                    , DivNavRule.INSTANCE
                    , ParamExprNavRule.INSTANCE
//                    ,DoubleXNavRule.INSTANCE
                    , DoubleXYNavRule.INSTANCE
                    , GenericFunctionNavRule.INSTANCE
                    , ImagNavRule.INSTANCE
                    , InvNavRule.INSTANCE
                    , MulNavRule.INSTANCE
                    , NegNavRule.INSTANCE
                    , PlusNavRule.INSTANCE
                    , PowNavRule.INSTANCE
                    , RealNavRule.INSTANCE
                    , SubNavRule.INSTANCE
                    , VDCxyNavRule.INSTANCE
                    , ShapeSimplifyRule.INSTANCE
                    , DiscreteNavRule.INSTANCE
                    , DDiscreteNavRule.INSTANCE
                    , VDiscreteNavRule.INSTANCE
                    , AxisTransformNavRule.INSTANCE
            )
    );
    public static final List<ExpressionRewriterRule> SIMPLIFY_RULES = new ArrayList<ExpressionRewriterRule>(
            Arrays.asList(
                    AnySimplifyRule.INSTANCE
                    , ComplexXYSimplifyRule.INSTANCE
                    , DomainExprSimplifyRule.INSTANCE
                    , CosXCosYSimplifyRule.INSTANCE
                    , CosXPlusYSimplifyRule.INSTANCE
                    , DCxySimplifyRule.INSTANCE
//                    , DDxToDDxySimplifyRule.INSTANCE
                    , DDxyLinearSimplifyRule.INSTANCE
//                    , DDxyToDDxSimplifyRule.INSTANCE
                    , DivSimplifyRule.INSTANCE
                    , DoubleXYSimplifyRule.INSTANCE
                    , GenericFunctionSimplifyRule.INSTANCE
                    , GenericFunctionXSimplifyRule.INSTANCE
                    , ImagSimplifyRule.INSTANCE
                    , InvSimplifyRule.INSTANCE
                    , MulSimplifyRule.INSTANCE
                    , NegSimplifyRule.INSTANCE
                    , PlusSimplifyRule.INSTANCE
                    , PowSimplifyRule.INSTANCE
                    , RealSimplifyRule.INSTANCE
                    , SubSimplifyRule.INSTANCE
                    , ComplexNavRule.INSTANCE
                    , VDCxySimplifyRule.INSTANCE
                    , ShapeSimplifyRule.INSTANCE
                    , ParamExprNavRule.INSTANCE
                    , DiscreteNavRule.INSTANCE
                    , DDiscreteNavRule.INSTANCE
                    , VDiscreteNavRule.INSTANCE
                    , AxisTransformNavRule.INSTANCE
                    , ConditionSimplifyRule.INSTANCE
                    , ParametrizedScalarProductSimplifyRule.INSTANCE
            )
    );
    public final static List<ExpressionRewriterRule> CANONICAL_RULES = new ArrayList<ExpressionRewriterRule>(
            Arrays.asList(
//                    DDxCosSymbolRule.INSTANCE
                    CosXCosYSymbolRule.INSTANCE,
                    CosXPlusYSymbolRule.INSTANCE
                    , DomainExprSimplifyRule.INSTANCE
//                    , DDxLinearSymbolRule.INSTANCE
//                    , DDxPolynomeSymbolRule.INSTANCE
//                    , DDxUxSymbolRule.INSTANCE
                    , DDxyLinearSymbolRule.INSTANCE
//                    , DoubleXSymbolRule.INSTANCE
                    , ReplaceDeprecatedRule.INSTANCE
                    , AnySimplifyRule.INSTANCE
            )
    );

    private static final ExpressionRewriterSuite OPTIMIZE_COMPUTE = new ExpressionRewriterSuite("OPTIMIZE_COMPUTE");

    static {
        rebuild();
    }

    public static void rebuild() {
        OPTIMIZE_COMPUTE.clear();
        ExpressionRewriterRuleSet CANONICAL_RULE_SET = new ExpressionRewriterRuleSet("CANONICAL");
        for (ExpressionRewriterRule r : NAVIGATION_RULES) {
            CANONICAL_RULE_SET.addRule(r);
        }
        for (ExpressionRewriterRule r : CANONICAL_RULES) {
            CANONICAL_RULE_SET.addRule(r);
        }
        OPTIMIZE_COMPUTE.add(CANONICAL_RULE_SET);


        ExpressionRewriterRuleSet EXPAND_SIMPLIFY_RULE_SET = new ExpressionRewriterRuleSet("EXPAND_SIMPLIFY");
//        for (ExpressionRewriterRule r : NAVIGATION_RULES) {
//            EXPAND_SIMPLIFY_RULE_SET.addRule(r);
//        }
        for (ExpressionRewriterRule r : SIMPLIFY_RULES) {
            EXPAND_SIMPLIFY_RULE_SET.addRule(r);
        }
        OPTIMIZE_COMPUTE.add(EXPAND_SIMPLIFY_RULE_SET);
    }

    public static ExpressionRewriter getComputationOptimizer() {
        return OPTIMIZE_COMPUTE;
    }

    private ExpressionRewriterFactory() {
    }

}
