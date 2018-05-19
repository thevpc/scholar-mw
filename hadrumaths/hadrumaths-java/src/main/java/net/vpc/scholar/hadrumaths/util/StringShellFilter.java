package net.vpc.scholar.hadrumaths.util;


public class StringShellFilter extends StringRegexpFilter {
    public StringShellFilter(String pattern, boolean caseSensitive) {
        super(shellToRegexpPattern(pattern), caseSensitive);
    }

    public static String sqlToRegexpPattern(String dosLikePattern) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dosLikePattern.length(); i++) {
            char c = dosLikePattern.charAt(i);
            switch (c) {
                case '%': {
                    sb.append(".*");
                    break;
                }
                case '?': {
                    sb.append("_");
                    break;
                }
                case '.': {
                    sb.append("\\.");
                    break;
                }
                case ')': {
                    sb.append("\\)");
                    break;
                }
                case '(': {
                    sb.append("\\(");
                    break;
                }
                case '\\': {
                    sb.append("\\");
                    i++;
                    c = dosLikePattern.charAt(i);
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }

    public static boolean shellMatches(String dosLikePattern, String string) {
        return string.matches(shellToRegexpPattern(dosLikePattern));
    }

    public static String shellToRegexpPattern(String dosLikePattern) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dosLikePattern.length(); i++) {
            char c = dosLikePattern.charAt(i);
            switch (c) {
                case '*': {
                    sb.append(".*");
                    break;
                }
                case '?': {
                    sb.append(".");
                    break;
                }
                case '.': {
                    sb.append("\\.");
                    break;
                }
                case ')': {
                    sb.append("\\)");
                    break;
                }
                case '(': {
                    sb.append("\\(");
                    break;
                }
                case '\\': {
                    sb.append("\\");
                    i++;
                    c = dosLikePattern.charAt(i);
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                    break;
                }
            }
        }
        return sb.toString();
    }
}
