package net.vpc.scholar.hadrumaths.util;

import net.vpc.scholar.hadrumaths.MathsBase;

import java.util.ArrayList;

/**
 * Created by vpc on 1/14/15.
 */
public class ThreadStack {
    private Throwable throwable;
    private int ignored;
    private StackTraceElement[] stackTrace;
    private ThreadStackElement[] threadStackElements;

    ThreadStack(int ignored) {
        this(new Throwable(), ignored + 1);
    }

    private ThreadStack(Throwable throwable, int ignored) {
        this.throwable = throwable;
        this.ignored = ignored;
    }

    public ThreadStackElement currentElement() {
        return get(getStackTraceElements().length - 1);
    }

    public int size() {
        return getStackTraceElements().length;
    }

    public ThreadStackElement[] getElements() {
        int size = size();
        ThreadStackElement[] all = new ThreadStackElement[size];
        for (int i = 0; i < size; i++) {
            all[i] = get(i);
        }
        return all;
    }

    public ThreadStackElement get(int level) {
        if (threadStackElements == null) {
            StackTraceElement[] stackTraceElements = getStackTraceElements();
            threadStackElements = new ThreadStackElement[stackTraceElements.length];
        }
        ThreadStackElement ce = threadStackElements[level];
        if (ce == null) {
            StackTraceElement[] stackTraceElements = getStackTraceElements();
            ce = new ThreadStackElement(this, stackTraceElements[level], level);
            threadStackElements[level] = ce;
        }
        return ce;
    }

    private StackTraceElement[] getStackTraceElements() {
        if (stackTrace == null) {
            int ignoredOk = ignored;
            StackTraceElement[] stackTrace1 = throwable.getStackTrace();
            StackTraceElement[] stackTrace2 = new StackTraceElement[stackTrace1.length - ignoredOk];
            System.arraycopy(stackTrace1, ignoredOk, stackTrace2, 0, stackTrace2.length);
            stackTrace = stackTrace2;
        }
        return stackTrace;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (ThreadStackElement e : getElements()) {
            if (first) {
                first = false;
            } else {
                sb.append("\n");
            }
            sb.append(e.toString());
        }
//            if (ignored > 0) {
//                if (first) {
//                    first = false;
//                } else {
//                    sb.append("\n");
//                }
//                sb.append("...ignored ").append(ignored).append(" others");
//            }
        return sb.toString();
    }

    private static ArrayList<String> indents = new ArrayList<String>();

    public static ThreadStackElement currentThreadStackElement() {
        return new ThreadStack(1).currentElement();
    }

    public static ThreadStackElement getThreadStackElement(int level) {
        return new ThreadStack(1).get(level);
    }

    public static int depth() {
        return new ThreadStack(1).size();
    }

    public static ThreadStack current() {
        return new ThreadStack(1);
    }

    public static class ThreadStackElement {
        private StackTraceElement element;
        private boolean parsed;
        private int depth;
        private ThreadStack threadStack;

        public ThreadStackElement(ThreadStack threadStack, StackTraceElement element, int depth) {
            this.element = element;
            this.depth = depth;
            this.threadStack = threadStack;
        }

        public ThreadStack getThreadStack() {
            return threadStack;
        }


        public String getIndent() {
            int nextLevel = depth + 1;
            while (indents.size() < nextLevel) {
                char[] cc = new char[indents.size()];
                for (int i = 0; i < cc.length; i++) {
                    cc[i] = ' ';
                }
                indents.add(new String(cc));
            }
            return indents.get(depth);
        }

        public String getClassName() {
            return element.getClassName();
        }

        public String getMethodName() {
            return element.getMethodName();
        }

        public String getFileName() {
            return element.getFileName();
        }

        public int getLineNumber() {
            return element.getLineNumber();
        }

        public String getDump(Object... params) {
            return getIndent() + getClassName() + "." + getMethodName() + "(" + MathsBase.dump(params) + ")";
        }

        public int depth() {
            return depth;
        }

        @Override
        public String toString() {
            return getClassName() + "." + getMethodName();
        }
    }
}
