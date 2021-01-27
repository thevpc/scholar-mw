package net.thevpc.scholar.hadrumaths.transform;

import java.util.List;

public interface ExpressionRuleSource {
    void addRule(ExpressionRewriterRule rule);

    void removeRule(ExpressionRewriterRule rule);

    void addListener(ExpressionRuleSourceListener listener);

    void removeListener(ExpressionRuleSourceListener listener);

    List<ExpressionRewriterRule> getRules();
}
