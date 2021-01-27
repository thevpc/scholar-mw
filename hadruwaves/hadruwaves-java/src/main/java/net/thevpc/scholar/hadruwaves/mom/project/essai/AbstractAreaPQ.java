//package net.thevpc.scholar.tmwlib.mom.project.essai;
//
//import net.thevpc.scholar.tmwlib.mom.project.common.MetalArea;
//import net.thevpc.scholar.math.util.Configuration;
//
///**
// * User: taha
// * Date: 2 juil. 2003
// * Time: 12:03:05
// */
//public abstract class AbstractAreaPQ extends MetalArea {
//    protected int max_p;
//    protected int max_q;
//    protected String max_pExpression;
//    protected String max_qExpression;
//
//    public AbstractAreaPQ(String type) {
//        super(type);
//        this.max_pExpression = "1";
//        this.max_qExpression = "1";
//    }
//
//    @Override
//    public void load(Configuration conf, String key) {
//        super.load(conf, key);
//        this.max_pExpression = String.valueOf(conf.getObject(key + ".max_p", "1"));
//        this.max_qExpression = String.valueOf(conf.getObject(key + ".max_q", "1"));
//    }
//
//    public AbstractAreaPQ(String type, String name, String x, String y, String width, String height, String max_p, String max_q) {
//        super(type, name, x, y, width, height);
//        this.max_pExpression = max_p;
//        this.max_qExpression = max_q;
//    }
//
//    public int getFunctionMaxP() {
//        return max_p;
//    }
//
//    public int getFunctionMaxQ() {
//        return max_q;
//    }
//
//    public int getFunctionMax() {
//        return max_p * max_q;
//    }
//
//    public void store(Configuration c, String key) {
//        super.store(c, key);
//        c.setString(key + ".max_p", max_pExpression);
//        c.setString(key + ".max_q", max_qExpression);
//    }
//
//    public boolean equals(Object other) {
//        if (super.equals(other)) {
//            AbstractAreaPQ s = (AbstractAreaPQ) other;
//            return max_p == s.max_p && max_q == s.max_q;
//        } else {
//            return false;
//        }
//    }
//
//    public void recompile() {
//        super.recompile();
//        max_p = getContext().evaluateInt(max_pExpression);
//        max_q = getContext().evaluateInt(max_qExpression);
//    }
//
//    public String getMax_pExpression() {
//        return max_pExpression;
//    }
//
//    public String getMax_qExpression() {
//        return max_qExpression;
//    }
//
//}
