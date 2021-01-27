package net.thevpc.scholar.hadrumaths.transform;

import net.thevpc.scholar.hadrumaths.Expr;

/**
 * Created by vpc on 1/24/15.
 */
public abstract class RewriteResult {
    private static final Unmodified UU = new Unmodified();
//    private boolean rewritten;
//    private boolean bestEffort;

    public static RewriteResult newVal(Expr value) {
//        return new RewriteResult(value);
        return new NewVal(value);
    }

    public static RewriteResult unmodified() {
        return UU;//new RewriteResult(value, UNMODIFIED);
    }

    public static RewriteResult bestEffort(Expr value) {
//        return new RewriteResult(value,true,true);
        return new BestEffort(value);
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
//    private RewriteResult(Expr value, byte type) {
//        this.value = value;
//        this.type = type;
//    }

    public abstract Expr getValueOrNull(); /*{
//        return rewritten?value:null;
        return type == UNMODIFIED ? null : value;
    }*/

    public abstract Expr getValue(Expr e);

    public abstract Expr getValue();/*{
        return value;
    }*/

    public abstract RewriteResultType getType();/*{
        return type;
    }*/

    public abstract boolean isBestEffort();/*{
//        return bestEffort || !rewritten;
        return type == BEST_EFFORT;
    }*/

    public abstract boolean isRewritten();/*{
//        return rewritten;
        return type != UNMODIFIED;
//        return type == REWRITTEN || type==BEST_EFFORT;
    }*/

    public abstract boolean isUnmodified();/*{
//        return !rewritten;
        return type == UNMODIFIED;
    }*/

    public abstract boolean isNewVal();/*{
//        return !rewritten;
        return type == NEW_VAL;
    }*/


    private static class Unmodified extends RewriteResult {
        public Unmodified() {
        }

        @Override
        public String toString() {
            return "Unmodified";
        }

        @Override
        public Expr getValueOrNull() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Expr getValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public RewriteResultType getType() {
            return RewriteResultType.UNMODIFIED;
        }

        @Override
        public boolean isBestEffort() {
            return false;
        }

        @Override
        public boolean isRewritten() {
            return false;
        }

        @Override
        public boolean isUnmodified() {
            return true;
        }

        @Override
        public boolean isNewVal() {
            return false;
        }

        @Override
        public Expr getValue(Expr e) {
            return e;
        }
    }

    private static class NewVal extends RewriteResult {
        private final Expr value;

        public NewVal(Expr e) {
            this.value = e;
        }

        @Override
        public Expr getValueOrNull() {
            return getValue();
        }

        @Override
        public Expr getValue() {
            return value;
        }

        @Override
        public Expr getValue(Expr e) {
            return value;
        }

        @Override
        public RewriteResultType getType() {
            return RewriteResultType.NEW_VAL;
        }

        @Override
        public boolean isBestEffort() {
            return false;
        }

        @Override
        public boolean isRewritten() {
            return true;
        }

        @Override
        public boolean isUnmodified() {
            return false;
        }

        @Override
        public boolean isNewVal() {
            return true;
        }

        @Override
        public String toString() {
            return "NewVal(" +
                    value +
                    ')';
        }
    }

    private static class BestEffort extends RewriteResult {
        private final Expr value;

        public BestEffort(Expr e) {
            this.value = e;
        }

        @Override
        public Expr getValueOrNull() {
            return getValue();
        }

        @Override
        public Expr getValue() {
            return value;
        }

        @Override
        public Expr getValue(Expr e) {
            return value;
        }

        @Override
        public RewriteResultType getType() {
            return RewriteResultType.BEST_EFFORT;
        }

        @Override
        public boolean isBestEffort() {
            return true;
        }

        @Override
        public boolean isRewritten() {
            return true;
        }

        @Override
        public boolean isUnmodified() {
            return false;
        }

        @Override
        public boolean isNewVal() {
            return false;
        }

        @Override
        public String toString() {
            return "BestEffort(" +
                    value +
                    ')';
        }
    }
}
