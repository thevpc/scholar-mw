/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform;

import net.vpc.common.tson.*;
import net.vpc.common.util.ClassMap;
import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.cache.CacheEnabled;
import net.vpc.scholar.hadrumaths.cache.CacheKey;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;
import net.vpc.scholar.hadrumaths.format.params.DebugObjectFormatParam;
import net.vpc.scholar.hadrumaths.symbolic.ExprDefaults;
import net.vpc.scholar.hadrumaths.symbolic.ExprType;
import net.vpc.scholar.hadrumaths.symbolic.polymorph.Any;
import net.vpc.scholar.hadrumaths.transform.navigaterules.ExprNavRule;
import net.vpc.scholar.hadrumaths.util.dump.DumpManager;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author vpc
 */
public class ExpressionRewriterRuleSet extends AbstractExpressionRewriter {
    //    public static int DEBUG_REWRITE_ONCE=0;
//    public static int DEBUG_REWRITE_SUCCESS=0;
//    public static int DEBUG_REWRITE_FAIL=0;
    public List<ExpressionRewriterRule> rules = new ArrayList<ExpressionRewriterRule>();
    public ClassMap<List<ExpressionRewriterRule>> mapRules = new ClassMap<List<ExpressionRewriterRule>>(Expr.class, (Class) List.class);
    public Map<Class, List<ExpressionRewriterRule>> cachedMapRules = new HashMap<Class, List<ExpressionRewriterRule>>();
    public ExpressionRewriterRule fallbackRule = ExprNavRule.INSTANCE;
    public List<ExpressionRuleSource> sources = new ArrayList<>();

    public ExpressionRewriterRuleSet(String name) {
        super(name);
    }

    public void addAllRules(ExpressionRuleSource source) {
        sources.add(source);
        source.addListener(new InternalExpressionRuleSourceListener());
        addAllRules(source.getRules());
    }

    public void addRule(ExpressionRewriterRule rule) {
        if (rule == null) {
            return;
        }
        boolean some = false;
        for (Class<? extends Expr> c : rule.getTypes()) {
            if (c.equals(Expr.class)) {
                fallbackRule = rule;
            } else {
                some = true;
//                if (c.isInterface()) {
//                    throw new IllegalArgumentException("Cannot define Rule for interface " + c);
//                }
                List<ExpressionRewriterRule> list = mapRules.getExact(c);
                if (list == null) {
                    list = new ArrayList<ExpressionRewriterRule>();
                    mapRules.put(c, list);
                }
                if (!list.contains(rule)) {
                    list.add(rule);
                }
                ((ArrayList) list).trimToSize();
            }
        }
        if (some) {
            rules.add(rule);
            ((ArrayList) rules).trimToSize();
            cachedMapRules.clear();
        }
    }

    public void removeRule(ExpressionRewriterRule rule) {
        for (Class<? extends Expr> c : rule.getTypes()) {
            if (c.isInterface()) {
                throw new IllegalArgumentException("Cannot define Rule for interface " + c);
            }
            List<ExpressionRewriterRule> list = mapRules.getExact(c);
            if (list != null) {
                list.remove(rule);
            }
        }
        rules.remove(rule);
        cachedMapRules.clear();
    }

    public void addAllRules(Collection<ExpressionRewriterRule> rules) {
        if (rules != null) {
            for (ExpressionRewriterRule rule : rules) {
                addRule(rule);
            }
        }
    }

    public void addAllRules(ExpressionRewriterRule[] rules) {
        if (rules != null) {
            addAllRules(Arrays.asList(rules));
        }
    }

    @Override
    public void setCacheEnabled(boolean enabled) {
        super.setCacheEnabled(enabled);
        for (ExpressionRewriterRule rule : rules) {
            if (rule instanceof CacheEnabled) {
                ((CacheEnabled) rule).setCacheEnabled(enabled);
            }
        }
    }

    public void clearCache() {
        super.clearCache();
        for (ExpressionRewriterRule rule : rules) {
            if (rule instanceof CacheEnabled) {
                ((CacheEnabled) rule).clearCache();
            }
        }
    }

    public RewriteResult rewriteImpl(Expr e, ExprType targetExprType) {
//        DEBUG_REWRITE_ONCE++;
        Expr curr = e;
        boolean modified = false;
        Class<? extends Expr> cls = e.getClass();
        int appliedRulesCount = 0;
        List<ExpressionRewriterRule> rulesByClass = getRulesByClass(cls);
        int bestEfforts = 0;
        boolean debugExpressionRewrite = Maths.Config.isDebugExpressionRewrite();
        boolean lastIsBestEffort=false;
        for (ExpressionRewriterRule rule : rulesByClass) {
            lastIsBestEffort=false;
            appliedRulesCount++;

            boolean _debug = false;
            String _debug_msg = "";

            if (debugExpressionRewrite) {
//                    if (ThreadStack.depth() > 200) {
                _debug = true;
                _debug_msg = DumpManager.getStackDepthWhites() + getName() + "(" + rule.getClass().getSimpleName() + ")  :  " + curr.getClass().getSimpleName() + "[@" + System.identityHashCode(curr) + "]" + " = " + Maths.dump(curr);
//                    msg = DumpManager.getStackDepthWhites() + name + " :: " + rule.getClass().getSimpleName() + "  :  " + System.identityHashCode(curr) + "  :  " + curr.getClass().getSimpleName() + " :: " + curr;
                System.out.println("_" + _debug_msg);
//                    }
            }
            RewriteResult nextResult = rule.rewrite(curr, this, targetExprType);
            if(debugExpressionRewrite) {
                if (nextResult == null) {
                    throw new IllegalArgumentException("[BUG] Expected non Null simplification result");
                }
                if (nextResult.isRewritten()) {
                    if (curr.equals(nextResult.getValue())) {
                        nextResult = rule.rewrite(curr, this, targetExprType);
                        throw new IllegalArgumentException("Expected Expression Unmodified but simplification returned : " + nextResult.getType());
                    }
                }
            }
//                if(next!=null && !next.getClass().equals(curr.getClass())){
//                    System.out.println("???");
//                }
            if (nextResult != null) {
                if (nextResult.isUnmodified()) {//next next != null && !next.equals(curr)
//                    DEBUG_REWRITE_FAIL++;
                    for (ExprRewriteFailListener rewriteListener : rewriteFailListeners) {
                        rewriteListener.onUnmodifiedExpr(this, e);
                    }
                } else if (nextResult.isRewritten()) {//next next != null && !next.equals(curr)
//                    DEBUG_REWRITE_SUCCESS++;
                    boolean bestEffort = nextResult.isBestEffort();
                    Expr next = nextResult.getValue();
                    for (ExprRewriteSuccessListener rewriteListener : rewriteSuccessListeners) {
                        rewriteListener.onModifiedExpr(this, e, next, bestEffort);
                    }
                    if (debugExpressionRewrite) {
                        if (next.toString().equals(curr.toString()) && !(curr instanceof Any)) {
                            String s1 = FormatFactory.format(curr, new ObjectFormatParamSet(DebugObjectFormatParam.INSTANCE));
                            String s2 = FormatFactory.format(next, new ObjectFormatParamSet(DebugObjectFormatParam.INSTANCE));
                            if (s1.equals(s2)) {
                                if (curr.getClass().getSimpleName().equals(next.getClass().getSimpleName())) {
                                    if (curr.equals(next)) {
                                        System.out.println("<" + _debug_msg + " : Who come?");
                                        rule.rewrite(curr, this, targetExprType);
                                    } else {
//                                next.equals(curr)
                                        System.out.println("<" + _debug_msg + s1 + " transformed to same class with different content");
                                    }
                                } else {
                                    System.out.println("<" + _debug_msg + s1 + " transformed from " + curr.getClass().getSimpleName() + " to " + (next.getClass().getSimpleName()));
                                }
                            }
                        }
                    }
                    curr = next;
                    if (bestEffort) {
                        lastIsBestEffort=true;
                        bestEfforts++;
                    }
                    modified = true;
                    if (debugExpressionRewrite) {
                        if (_debug) {
                            System.out.println("<" + _debug_msg + " [RESULT] ==> " + next);
                        }
                    }
                    break;
                }
            } else {
                lastIsBestEffort=true;
//                DEBUG_REWRITE_FAIL++;
                for (ExprRewriteFailListener rewriteListener : rewriteFailListeners) {
                    rewriteListener.onUnmodifiedExpr(this, e);
                }
                if (debugExpressionRewrite) {
                    if (_debug) {
                        System.out.println("<" + _debug_msg + " [RESULT] ==> NO CHANGE!!");
                    }
                }
            }

        }
        if (appliedRulesCount == 0) {
            if (curr.getChildren().size() > 0) {
                throw new NoSuchElementException("No rule found for " + curr.getClass().getSimpleName() + " in " + getName());
            }
        }
        if (!modified) {
            return RewriteResult.unmodified();
        }
        curr = ExprDefaults.copyProperties(e, curr);
        if (lastIsBestEffort/*rulesByClass.size() == 1 && bestEfforts == 1*/) {
            return RewriteResult.bestEffort(curr);
        }
        return RewriteResult.newVal(curr);
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ExpressionRewriterRuleSet that = (ExpressionRewriterRuleSet) o;
        return rules.equals(that.rules) &&
                Objects.equals(fallbackRule, that.fallbackRule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rules, fallbackRule);
    }

    public List<ExpressionRewriterRule> getRulesByClass(Class<? extends Expr> cls) {
        List<ExpressionRewriterRule> list = cachedMapRules.get(cls);
        if (list == null) {
            //should be cached!
            list = new ArrayList<ExpressionRewriterRule>();
            for (List<ExpressionRewriterRule> ruleList : mapRules.getAll(cls)) {
                list.addAll(ruleList);
            }
            cachedMapRules.put(cls, list);
        }
        if (list.isEmpty() && fallbackRule != null) {
            return Arrays.asList(fallbackRule);
        }
        return list;
    }

    @Override
    public String toString() {
        return "RuleSet{" + getName() + "}";
    }

    @Override
    public TsonElement toTsonElement(TsonObjectContext context) {
        TsonObjectBuilder obj = Tson.obj(getClass().getSimpleName(), new TsonElementBase[]{
                Tson.pair("name", Tson.elem(getName())),
                Tson.pair("itr", Tson.elem(getMaxIterations()))
        });
        TreeSet<String> allRuleNames = new TreeSet<>(rules.stream().map(x -> x.getClass().getName()).collect(Collectors.toSet()));
        if (fallbackRule != null) {
            allRuleNames.add(fallbackRule.getClass().getName());
        }
        obj.add("rulesCount", Tson.elem(allRuleNames.size()));
        obj.add("fingerPrint", context.elem(CacheKey.toHashString(String.join(";", allRuleNames))));
        return obj.build();
    }

    private class InternalExpressionRuleSourceListener implements ExpressionRuleSourceListener {
        @Override
        public void onAddRule(ExpressionRewriterRule rule) {
            ExpressionRewriterRuleSet.this.addRule(rule);
        }

        @Override
        public void onRemoveRule(ExpressionRewriterRule rule) {
            ExpressionRewriterRuleSet.this.removeRule(rule);
        }
        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
