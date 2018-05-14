/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;
import net.vpc.scholar.hadrumaths.Maths;
import net.vpc.scholar.hadrumaths.format.FormatParamSet;
import net.vpc.scholar.hadrumaths.symbolic.Any;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.format.params.DebugFormat;
import net.vpc.scholar.hadrumaths.util.CacheEnabled;
import net.vpc.scholar.hadrumaths.util.ClassMap;
import net.vpc.scholar.hadrumaths.util.dump.DumpManager;

import java.util.*;

/**
 * @author vpc
 */
public class ExpressionRewriterRuleSet extends AbstractExpressionRewriter {
    public List<ExpressionRewriterRule> rules = new ArrayList<ExpressionRewriterRule>();
    public ClassMap<List<ExpressionRewriterRule>> mapRules = new ClassMap<List<ExpressionRewriterRule>>(Expr.class, (Class) List.class);
    public Map<Class, List<ExpressionRewriterRule>> cachedMapRules = new HashMap<Class, List<ExpressionRewriterRule>>();
    public int maxIterations = 100;
    private String name;

    public ExpressionRewriterRuleSet(String name) {
        this.name = name;
    }

    public void addAllRules(ExpressionRewriterRule[] rules) {
        if(rules!=null) {
            addAllRules(Arrays.asList(rules));
        }
    }

    public void addAllRules(Collection<ExpressionRewriterRule> rules) {
        if(rules!=null){
            for (ExpressionRewriterRule rule : rules) {
                addRule(rule);
            }
        }
    }

    public void addRule(ExpressionRewriterRule rule) {
        if(rule==null){
            return;
        }
        for (Class<? extends Expr> c : rule.getTypes()) {
            if (c.isInterface()) {
                throw new IllegalArgumentException("Cannot define Rule for interface " + c);
            }
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
        rules.add(rule);
        ((ArrayList) rules).trimToSize();
        cachedMapRules.clear();
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

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }


    public void clearCache() {
        super.clearCache();
        for (ExpressionRewriterRule rule : rules) {
            if (rule instanceof CacheEnabled) {
                ((CacheEnabled) rule).clearCache();
            }
        }
    }

    @Override
    public RewriteResult rewriteImpl(Expr e) {
        Expr curr = e;
        int maxIterations = this.maxIterations;
        if (maxIterations <= 0) {
            maxIterations = 1;
        }
        boolean simplified = false;
        RewriteResult next = null;
        while (maxIterations > 0) {
//            String msg = DumpManager.getStackDepthWhites()+name+" :: REWRITE "+maxIterations+" :: "+e;
//            System.out.println(msg);
            next = rewriteOnce(curr);
            if (next.isRewritten() /*&& !next.equals(curr)*/) {
                simplified = true;
                if (next.isBestEffort()) {
                    return next;
                }
                curr = next.getValue();
                maxIterations--;
            } else {
                if (simplified) {
                    return RewriteResult.newVal(curr);
                }
                return next;
            }
        }
        return next;
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
        return list;
    }

    public RewriteResult rewriteOnce(Expr e) {
        Expr curr = e;
        boolean modified = false;
        Class<? extends Expr> cls = e.getClass();
        int appliedRulesCount = 0;
        List<ExpressionRewriterRule> rulesByClass = getRulesByClass(cls);
//        if(rulesByClass.size()==0){
//            rulesByClass = getRulesByClass(cls);
//        }
        int bestEfforts = 0;
        boolean debugExpressionRewrite = Maths.Config.isDebugExpressionRewrite();
        for (ExpressionRewriterRule rule : rulesByClass) {

            appliedRulesCount++;

            boolean _debug = false;
            String _debug_msg = "";

            if (debugExpressionRewrite) {
//                    if (ThreadStack.depth() > 200) {
                _debug = true;
                _debug_msg = DumpManager.getStackDepthWhites() + name + "(" + rule.getClass().getSimpleName() + ")  :  " + curr.getClass().getSimpleName() + "[@" + System.identityHashCode(curr) + "]" + " = " + Maths.dump(curr);
//                    msg = DumpManager.getStackDepthWhites() + name + " :: " + rule.getClass().getSimpleName() + "  :  " + System.identityHashCode(curr) + "  :  " + curr.getClass().getSimpleName() + " :: " + curr;
                System.out.println("_" + _debug_msg);
//                    }
            }
            RewriteResult nextResult = rule.rewrite(curr, this);
//                if(next!=null && !next.getClass().equals(curr.getClass())){
//                    System.out.println("???");
//                }
            if (nextResult != null) {
                Expr next = nextResult.getValue();
                if (nextResult.isUnmodified()) {//next next != null && !next.equals(curr)
                    for (ExprRewriteFailListener rewriteListener : rewriteFailListeners) {
                        rewriteListener.onUnmodifiedExpr(this, e);
                    }
                } else if (nextResult.isRewritten()) {//next next != null && !next.equals(curr)
                    for (ExprRewriteListener rewriteListener : rewriteListeners) {
                        rewriteListener.onRewriteExpr(this, e, next);
                    }
                    if (debugExpressionRewrite) {
                        if (next.toString().equals(curr.toString()) && !(curr instanceof Any)) {
                            String s1 = FormatFactory.format(curr, new FormatParamSet(DebugFormat.INSTANCE));
                            String s2 = FormatFactory.format(next, new FormatParamSet(DebugFormat.INSTANCE));
                            if (s1.equals(s2)) {
                                if (curr.getClass().getSimpleName().equals(next.getClass().getSimpleName())) {
                                    if (curr.equals(next)) {
                                        System.out.println("<" + _debug_msg + " : Who come?");
                                        rule.rewrite(curr, this);
                                    } else {
//                                next.equals(curr)
                                        System.out.println("<" + _debug_msg + s1 + " transformed to same class with different content");
                                    }
                                    ;
                                } else {
                                    System.out.println("<" + _debug_msg + s1 + " transformed from " + curr.getClass().getSimpleName() + " to " + (next.getClass().getSimpleName()));
                                }
                            }
                        }
                    }
                    curr = next;
                    if (nextResult.isBestEffort()) {
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
                if (debugExpressionRewrite) {
                    if (_debug) {
                        System.out.println("<" + _debug_msg + " [RESULT] ==> NO CHANGE!!");
                    }
                }
            }

        }
        if (appliedRulesCount == 0) {
            if (curr.getSubExpressions().size() > 0) {
                throw new NoSuchElementException("No rule found for " + curr.getClass().getSimpleName() + " in " + name);
            }
        }
        if (!modified) {
            return RewriteResult.unmodified(e);
        }
        curr = Any.copyProperties(e, curr);
        if (rulesByClass.size() == 1 && bestEfforts == 1) {
            return RewriteResult.bestEffort(curr);
        }
        return RewriteResult.newVal(curr);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpressionRewriterRuleSet)) return false;

        ExpressionRewriterRuleSet that = (ExpressionRewriterRuleSet) o;

        if (maxIterations != that.maxIterations) return false;
        if (mapRules != null ? !mapRules.equals(that.mapRules) : that.mapRules != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (rules != null ? !rules.equals(that.rules) : that.rules != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rules != null ? rules.hashCode() : 0;
        result = 31 * result + (mapRules != null ? mapRules.hashCode() : 0);
        result = 31 * result + maxIterations;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
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

    @Override
    public String toString() {
        return "RuleSet{" + name + "}";
    }
}
