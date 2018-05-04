package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 1/24/15.
 */
public final class RewriteResult {

    public static final byte UNMODIFIED = 1;
    public static final byte BEST_EFFORT = 2;
    public static final byte NEW_VAL = 3;
    private final Expr value;
    private final byte type;
//    private boolean rewritten;
//    private boolean bestEffort;

    public static RewriteResult newVal(Expr value) {
//        return new RewriteResult(value);
        return new RewriteResult(value, NEW_VAL);
    }

    public static RewriteResult unmodified(Expr value) {
//        return new RewriteResult(value,false,true);
        return new RewriteResult(value, UNMODIFIED);
    }

    public static RewriteResult bestEffort(Expr value) {
//        return new RewriteResult(value,true,true);
        return new RewriteResult(value, BEST_EFFORT);
    }

//    private RewriteResult(Expr value) {
//        this.value = value;
//        this.rewritten = true;
//        this.bestEffort = false;
//    }
//
//    private RewriteResult(Expr value, boolean rewritten) {
//        this.value = value;
//        this.rewritten = rewritten;
//        this.bestEffort = !rewritten;
//    }
//
//    private RewriteResult(Expr value, boolean rewritten, boolean bestEffort) {
//        this.value = value;
//        this.rewritten = rewritten;
//        this.bestEffort = bestEffort;
//    }
    private RewriteResult(Expr value, byte type) {
        this.value = value;
        this.type = type;
    }

    public Expr getValueOrNull() {
//        return rewritten?value:null;
        return type == UNMODIFIED ? null : value;
    }

    public Expr getValue() {
        return value;
    }

    public byte getType() {
        return type;
    }

    public boolean isBestEffort() {
//        return bestEffort || !rewritten;
        return type == BEST_EFFORT;
    }

    public boolean isRewritten() {
//        return rewritten;
        return type != UNMODIFIED;
//        return type == REWRITTEN || type==BEST_EFFORT;
    }

    public boolean isUnmodified() {
//        return !rewritten;
        return type == UNMODIFIED;
    }

    public boolean isNewVal() {
//        return !rewritten;
        return type == NEW_VAL;
    }

    @Override
    public String toString() {
        return "RewriteResult{" +
                "value=" + value +
                ", " + (isUnmodified()?"Unmodified":isBestEffort()?"BestEffort":isNewVal()?"NewVal":"Unknown") +
                '}';
    }
}
