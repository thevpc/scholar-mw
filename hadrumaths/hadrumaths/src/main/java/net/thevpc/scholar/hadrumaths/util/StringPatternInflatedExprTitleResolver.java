package net.thevpc.scholar.hadrumaths.util;

import net.thevpc.scholar.hadrumaths.Maths;

import java.util.Map;

public class StringPatternInflatedExprTitleResolver implements InflatedExprTitleResolver {
    public static final InflatedExprTitleResolver DEFAULT = new StringPatternInflatedExprTitleResolver(null);
    private final String titlePattern;

    public StringPatternInflatedExprTitleResolver(String titlePattern) {
        this.titlePattern = titlePattern;
    }

    @Override
    public String resolveTitle(Map<String, Object> values, int index) {
        if (titlePattern != null) {
            if (titlePattern.contains("$")) {
                String t = titlePattern;
                for (Map.Entry<String, Object> ee : values.entrySet()) {
                    String key = ee.getKey();
                    if (key != null && key.length() > 0) {
                        String value = formatValue(ee.getValue());
                        t = t.replace("${" + key + "}", value);
                    }
                }
                if (t.equals(titlePattern)) {
                    return (t + " " + (index + 1));
                }
                return t;
            } else {
                return (titlePattern + " " + (index + 1));
            }
        }
        return String.valueOf(index + 1);
    }

    private String formatValue(Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof Number) {
            double d = ((Number) o).doubleValue();
            if (Maths.isInt(d)) {
                return String.valueOf((int) d);
            }
            return String.valueOf(d);
        }
        return String.valueOf(o);
    }
}
