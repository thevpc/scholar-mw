package net.thevpc.scholar.hadrumaths.transform;

import java.util.ArrayList;
import java.util.List;

public class DefaultExpressionRuleSource implements ExpressionRuleSource {
    private final List<ExpressionRewriterRule> rules = new ArrayList<>();
    private final List<ExpressionRuleSourceListener> listeners = new ArrayList<>();

    public ExpressionRuleSource addAllRules(List<ExpressionRewriterRule> all) {
        for (ExpressionRewriterRule r : all) {
            addRule(r);
        }
        return this;
    }

    @Override
    public void addRule(ExpressionRewriterRule rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
            for (ExpressionRuleSourceListener listener : listeners) {
                listener.onAddRule(rule);
            }
        }
    }

    @Override
    public void removeRule(ExpressionRewriterRule rule) {
        if (rules.remove(rule)) {
            for (ExpressionRuleSourceListener listener : listeners) {
                listener.onRemoveRule(rule);
            }
        }
    }

    @Override
    public void addListener(ExpressionRuleSourceListener listener) {
        if (!rules.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(ExpressionRuleSourceListener listener) {
        listeners.remove(listener);
    }

    @Override
    public List<ExpressionRewriterRule> getRules() {
        return new ArrayList<>(rules);
    }
}
