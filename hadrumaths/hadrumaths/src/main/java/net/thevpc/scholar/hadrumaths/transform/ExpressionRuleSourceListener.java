package net.thevpc.scholar.hadrumaths.transform;

public interface ExpressionRuleSourceListener {
    void onAddRule(ExpressionRewriterRule rule);

    void onRemoveRule(ExpressionRewriterRule rule);
}
