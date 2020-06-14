package net.vpc.scholar.hadrumaths.util;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRegexpFilter implements StringFilter {
    boolean multilineMode = true;
    Pattern pattern;

    public StringRegexpFilter(String pattern, boolean caseSensitive) {
        int flags = caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
        this.pattern = Pattern.compile(pattern, flags);
    }


    public boolean accept(String value) {
        if (value == null) {
            value = "";
        }
        String input = value;
        if (!multilineMode) {
            Matcher matcher = pattern.matcher(input);
            return matcher.matches();
        } else {
            StringTokenizer st = new StringTokenizer(input, "\n\r");
            while (st.hasMoreTokens()) {
                Matcher matcher = pattern.matcher(st.nextToken());
                if (matcher.matches()) {
                    return true;
                }
            }
            return false;
        }
    }
}
