/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths;

import net.thevpc.scholar.hadrumaths.transform.*;
import net.thevpc.scholar.hadrumaths.transform.canonicalrules.CosXCosYSymbolRule;
import net.thevpc.scholar.hadrumaths.transform.canonicalrules.CosXPlusYSymbolRule;
import net.thevpc.scholar.hadrumaths.transform.canonicalrules.DDxyLinearSymbolRule;
import net.thevpc.scholar.hadrumaths.transform.navigaterules.ExprNavRule;
import net.thevpc.scholar.hadrumaths.transform.simplifycore.*;

import java.util.Arrays;


/**
 * @author vpc
 */
public class ExpressionRewriterFactory extends AbstractFactory {


    public static final ExpressionRuleSource NAVIGATION_RULES = new DefaultExpressionRuleSource().addAllRules(
            Arrays.asList(
                    ExprNavRule.INSTANCE
            )
    );
    public static final ExpressionRuleSource SIMPLIFY_RULES = new DefaultExpressionRuleSource().addAllRules(
            Arrays.asList(
                    AnySimplifyRule.INSTANCE
                    , ComplexSimplifyRule.INSTANCE
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
                    , FunctionExprSimplifyRule.INSTANCE
                    , ImagSimplifyRule.INSTANCE
                    , InvSimplifyRule.INSTANCE
                    , MulSimplifyRule.INSTANCE
                    , NegSimplifyRule.INSTANCE
                    , ConjSimplifyRule.INSTANCE
                    , PlusSimplifyRule.INSTANCE
                    , PowSimplifyRule.INSTANCE
                    , RealSimplifyRule.INSTANCE
                    , SubSimplifyRule.INSTANCE
                    , VDCxySimplifyRule.INSTANCE
                    , ConditionSimplifyRule.INSTANCE
                    , ParametrizedScalarProductSimplifyRule.INSTANCE
                    , ExpSimplifyRule.INSTANCE
            )
    );
    public static final ExpressionRuleSource CANONICAL_RULES = new DefaultExpressionRuleSource().addAllRules(
            Arrays.asList(
                    CosXCosYSymbolRule.INSTANCE,
                    CosXPlusYSymbolRule.INSTANCE
                    , DDxyLinearSymbolRule.INSTANCE

                    , AnySimplifyRule.INSTANCE
                    , DomainExprSimplifyRule.INSTANCE
            )
    );

    private static final ExpressionRewriterSuite COMPUTATION_SIMPLIFIER = new ExpressionRewriterSuite("ExpressionRewriterFactoryComputationSuite");

    static {
        rebuild();
    }

    private ExpressionRewriterFactory() {
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

}
