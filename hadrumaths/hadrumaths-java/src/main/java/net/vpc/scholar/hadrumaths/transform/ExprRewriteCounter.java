package net.vpc.scholar.hadrumaths.transform;

import net.vpc.scholar.hadrumaths.Expr;

public class ExprRewriteCounter implements ExprRewriteListener {
    private Count count = new Count();

    @Override
    public void onUnmodifiedExpr(ExpressionRewriter rewriter, Expr oldValue) {
        count.unmodifiedInvocationCount++;
    }

    @Override
    public void onModifiedExpr(ExpressionRewriter rewriter, Expr oldValue, Expr newValue, boolean bestEffort) {
        if(bestEffort) {
            count.bestEffortModificationInvocationCount++;
        }else{
            count.partialModificationInvocationCount++;
        }
    }

    public int getUnmodifiedInvocationCount() {
        return count.getUnmodifiedInvocationCount();
    }

    public int getTotalModifiedInvocationCount() {
        return count.getTotalModifiedInvocationCount();
    }

    public int getPartialModificationInvocationCount() {
        return count.getPartialModificationInvocationCount();
    }

    public int getBestEffortModificationInvocationCount() {
        return count.getBestEffortModificationInvocationCount();
    }

    public int getTotalInvocationCount() {
        return count.getTotalInvocationCount();
    }

    public Count getCount() {
        return count;
    }

    @Override
    public String toString() {
        return getCount().toString();
    }

    public static class Count {
        private int unmodifiedInvocationCount;
        private int partialModificationInvocationCount;
        private int bestEffortModificationInvocationCount;

        public int getUnmodifiedInvocationCount() {
            return unmodifiedInvocationCount;
        }

        public int getTotalModifiedInvocationCount() {
            return partialModificationInvocationCount + bestEffortModificationInvocationCount;
        }

        public int getPartialModificationInvocationCount() {
            return partialModificationInvocationCount;
        }

        public int getTotalInvocationCount() {
            return partialModificationInvocationCount + unmodifiedInvocationCount;
        }

        public int getBestEffortModificationInvocationCount() {
            return bestEffortModificationInvocationCount;
        }

        @Override
        public String toString() {
            return "Count{" +
                    "newValue=" + partialModificationInvocationCount +
                    ", bestEffort=" + bestEffortModificationInvocationCount +
                    ", totalModified=" + (bestEffortModificationInvocationCount+partialModificationInvocationCount) +
                    ", unmodified=" + unmodifiedInvocationCount +
                    ", total=" + (partialModificationInvocationCount + unmodifiedInvocationCount) +
                    '}';
        }
    }
}