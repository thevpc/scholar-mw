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
                    , DDxNavRule.INSTANCE
                    , DDyNavRule.INSTANCE
                    , DDzNavRule.INSTANCE
                    , DDzIntegralXYNavRule.INSTANCE
                    , DCxyExpNavRule.INSTANCE
                    , DCxyNavRule.INSTANCE
//                    ,DDxCosNavRule.INSTANCE
//                    ,DDxIntegralXNavRule.INSTANCE
//                    ,DDxLinearNavRule.INSTANCE
//                    ,DDxPolynomeNavRule.INSTANCE
//                    ,DDxUxNavRule.INSTANCE
//                    ,DDxyToDDxNavRule.INSTANCE
                    , DivNavRule.INSTANCE
                    , ReminderNavRule.INSTANCE
                    , ParamExprNavRule.INSTANCE
//                    ,DoubleXNavRule.INSTANCE
                    , DoubleXYNavRule.INSTANCE
                    , DomainNavRule.INSTANCE
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
                    , RooftopXFunctionXYNavRule.INSTANCE
                    , SinSeqYZNavRule.INSTANCE
                    , DC2DVNavRule.INSTANCE
                    , DC2DMNavRule.INSTANCE
                    , DefaultDoubleToVectorNavRule.INSTANCE
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
                    , ReminderSimplifyRule.INSTANCE
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
                    , VDCxySimplifyRule.INSTANCE
                    , ShapeSimplifyRule.INSTANCE
                    , ConditionSimplifyRule.INSTANCE
                    , ParametrizedScalarProductSimplifyRule.INSTANCE
                    , ExpSimplifyRule.INSTANCE
                    , ComplexNavRule.INSTANCE
                    , ParamExprNavRule.INSTANCE
                    , DiscreteNavRule.INSTANCE
                    , DDiscreteNavRule.INSTANCE
                    , VDiscreteNavRule.INSTANCE
                    , AxisTransformNavRule.INSTANCE
                    , DDxNavRule.INSTANCE
                    , DDzNavRule.INSTANCE
                    , DDzIntegralXYNavRule.INSTANCE
                    , DDyNavRule.INSTANCE
            )
    );
    public final static List<ExpressionRewriterRule> CANONICAL_RULES = new ArrayList<ExpressionRewriterRule>(
            Arrays.asList(
                    CosXCosYSymbolRule.INSTANCE,
                    CosXPlusYSymbolRule.INSTANCE
                    , DDxyLinearSymbolRule.INSTANCE
                    , ReplaceDeprecatedRule.INSTANCE


                    , AnySimplifyRule.INSTANCE
                    , DomainExprSimplifyRule.INSTANCE
            )
    );

    private static final ExpressionRewriterSuite COMPUTATION_SIMPLIFIER = new ExpressionRewriterSuite("ExpressionRewriterFactoryComputationSuite");

    static {
        rebuild();
    }

    public static void rebuild() {
        COMPUTATION_SIMPLIFIER.clear();

//        ExpressionRewriterRuleSet CANONICAL_RULE_SET = new ExpressionRewriterRuleSet("CANONICAL");
//        CANONICAL_RULE_SET.addAllRules(NAVIGATION_RULES);
//        CANONICAL_RULE_SET.addAllRules(CANONICAL_RULES);
//        COMPUTATION_SIMPLIFIER.add(CANONICAL_RULE_SET);


        ExpressionRewriterRuleSet EXPAND_SIMPLIFY_RULE_SET = new ExpressionRewriterRuleSet("ExpressionRewriterFactoryExpandSimplfyRuleSet");
        EXPAND_SIMPLIFY_RULE_SET.addAllRules(SIMPLIFY_RULES);
        COMPUTATION_SIMPLIFIER.add(EXPAND_SIMPLIFY_RULE_SET);
    }

    public static ExpressionRewriter getComputationSimplifier() {
        return COMPUTATION_SIMPLIFIER;
    }

    private ExpressionRewriterFactory() {
    }

}
