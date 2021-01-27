package net.thevpc.scholar.hadrumaths.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastMessageFormat {
    private final Map<String, Evaluator> vars = new HashMap<>();
    private final List<Evaluator> items = new ArrayList<>();

    public FastMessageFormat parse(String message) {
        char[] chars = message.toCharArray();
        int i = 0;
        StringBuilder val = new StringBuilder();

        while (i < chars.length) {
            switch (chars[i]) {
                case '$': {
                    if (i + 1 < chars.length && chars[i + 1] == '{') {
                        if (val.length() > 0) {
                            items.add(new ConstItem(val.toString()));
                            val.delete(0, val.length());
                        }
                        i += 2;
                        StringBuilder name = new StringBuilder();
                        while (i < chars.length) {
                            if (chars[i] == '}') {
                                i++;
                                break;
                            }
                            name.append(chars[i]);
                            i++;
                        }
                        String sname = name.toString();
                        items.add(new VarItem(
                                sname, vars.get(sname)
                        ));
                    }
                    break;
                }
                default: {
                    val.append(chars[i]);
                    i++;
                    break;
                }
            }
        }
        if (val.length() > 0) {
            items.add(new ConstItem(val.toString()));
            val.delete(0, val.length());
        }
        return this;
    }

    public String format(Map<String, Object> context) {
        if (context == null) {
            context = new HashMap<>();
        }
        StringBuilder sb = new StringBuilder();
        for (Evaluator item : items) {
            sb.append(item.eval(context));
        }
        return sb.toString();
    }

    public FastMessageFormat addVar(String varName, Evaluator varExpr) {
        vars.put(varName, varExpr);

        for (Evaluator item : items) {
            if (item instanceof VarItem) {
                ((VarItem) item).evaluator = varExpr;
            }
        }
        return this;
    }

    public interface Evaluator {
        String eval(Map<String, Object> context);
    }

    private class ConstItem implements Evaluator {
        private final String value;

        public ConstItem(String value) {
            this.value = value;
        }

        @Override
        public String eval(Map<String, Object> context) {
            return value;
        }
    }

    private class VarItem implements Evaluator {
        private final String name;
        private Evaluator evaluator;

        public VarItem(String name, Evaluator evaluator) {
            this.name = name;
            this.evaluator = evaluator;
        }

        @Override
        public String eval(Map<String, Object> context) {
            Object v = context.get(name);
            if (v == null) {
                if (evaluator == null) {
                    throw new IllegalArgumentException("Missing evaluator for " + name);
                }
                context.put(name, v = evaluator.eval(context));
            }
            return String.valueOf(v);
        }
    }

}
