package net.vpc.scholar.hadrumaths.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vpc on 1/15/17.
 */
public class StringUtils {
    public static final DecimalFormat PERCENT_FORMAT =new DecimalFormat("#00.00%");

    public static StringBuilder clear(StringBuilder c) {
        return c.delete(0, c.length());
    }

    //    public static void main(String[] args) {
//        try {
//            String encrypted = SecurityUtils.httpEncrypt("hello hello hello hello hello hello hello hello".getBytes(), "mypwd");
//            System.out.println(encrypted);
//            byte[] decrypted = SecurityUtils.httpDecrypt(encrypted, "mypwd");
//            System.out.println(new String(decrypted ));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static String toPath(String[] array1, String separator) {
        if(array1==null||array1.length==0){
            return separator;
        }
        StringBuilder sb=new StringBuilder();
        for (String s : array1) {
            sb.append(separator).append(s);
        }
        return sb.toString();
    }

    public static String toPath(List<String> array1, String separator) {
        if(array1==null||array1.size()==0){
            return separator;
        }
        StringBuilder sb=new StringBuilder();
        for (String s : array1) {
            sb.append(separator).append(s);
        }
        return sb.toString();
    }

    public static String[] concat(String[] array1, String[] array2) {
        String[] r = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, r, 0, array1.length);
        System.arraycopy(array2, 0, r, array1.length, array2.length);
        return r;
    }

    public static String[] removeFirst(String[] array) {
        String[] r = new String[array.length - 1];
        System.arraycopy(array, 1, r, 0, r.length);
        return r;
    }

    public static int getStartingInt(String v1) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v1.length(); i++) {
            char c = v1.charAt(i);
            if (c >= '0' && c <= '9') {
                sb.append(c);
            }
        }
        if (sb.length() > 0) {
            return Integer.parseInt(sb.toString());
        }
        return -1;
    }

    public static boolean isInt(String v1) {
        try {
            if (v1==null || v1.length() == 0) {
                return false;
            }
            if (!Character.isDigit(v1.charAt(0))) {
                return false;
            }
            Integer.parseInt(v1);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int parseInt(String v1, int defaultValue) {
        try {
            if (isEmpty(v1)) {
                return defaultValue;
            }
            return Integer.parseInt(trim(v1));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String trim(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }

    public static String[] splitToArr(String str, String separators) {
        List<String> s = split(str, separators);
        return s.toArray(new String[s.size()]);
    }

    public static List<String> split(String str, String separators) {
        if (str == null) {
            return Collections.EMPTY_LIST;
        }
        StringTokenizer st = new StringTokenizer(str, separators);
        List<String> result = new ArrayList();
        while (st.hasMoreElements()) {
            result.add(st.nextToken());
        }
        return result;
    }

    public static String trimToNull(String str) {
        if (str == null) {
            return null;
        }
        String trimmed = str.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }

    public static String fillString(char x, int width) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < width; i++) {
            sb.append(x);
        }
        return sb.toString();
    }

    public static String alignLeft(String s, int width) {
        StringBuilder sb = new StringBuilder();
        if (s != null) {
            sb.append(s);
            int x = width - sb.length();
            if (x > 0) {
                sb.append(fillString(' ', x));
            }
        }
        return sb.toString();
    }

    public static String checkNotEmpty(String str, String name) {
        str = trim(str);
        if (isEmpty(str)) {
            throw new RuntimeException("Empty string not allowed for " + name);
        }
        return str.trim();
    }

    /**
     * code from org.apache.tools.ant.types.Commandline copyrights goes to
     * Apache Ant Authors (Licensed to the Apache Software Foundation (ASF))
     * Crack a command line.
     *
     * @param line the command line to process.
     * @return the command line broken into strings. An empty or null toProcess
     * parameter results in a zero sized array.
     */
    public static String[] translateCommandline(String line) {
        if (line == null || line.length() == 0) {
            //no command? no string
            return new String[0];
        }
        // parse with a simple finite state machine

        final int normal = 0;
        final int inQuote = 1;
        final int inDoubleQuote = 2;
        int state = normal;
        final StringTokenizer tok = new StringTokenizer(line, "\"\' ", true);
        final ArrayList<String> result = new ArrayList<String>();
        final StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;

        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch (state) {
                case inQuote:
                    if ("\'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                case inDoubleQuote:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = normal;
                    } else {
                        current.append(nextTok);
                    }
                    break;
                default:
                    if ("\'".equals(nextTok)) {
                        state = inQuote;
                    } else if ("\"".equals(nextTok)) {
                        state = inDoubleQuote;
                    } else if (" ".equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() != 0) {
                            result.add(current.toString());
                            current.setLength(0);
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }
        if (lastTokenHasBeenQuoted || current.length() != 0) {
            result.add(current.toString());
        }
        if (state == inQuote || state == inDoubleQuote) {
            throw new RuntimeException("unbalanced quotes in " + line);
        }
        return result.toArray(new String[result.size()]);
    }

    public static String join(String sep, Collection<String> items) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> i = items.iterator();
        if (i.hasNext()) {
            sb.append(i.next());
        }
        while (i.hasNext()) {
            sb.append(sep);
            sb.append(i.next());
        }
        return sb.toString();
    }

    public static String exceptionToString(Throwable ex) {
        return ex.getMessage();
    }

    /**
     * *
     * **
     *
     * @param pattern
     * @return
     */
    public static String simpexpToRegexp(String pattern, boolean contains) {
        if (pattern == null) {
            pattern = "*";
        }
        int i = 0;
        char[] cc = pattern.toCharArray();
        StringBuilder sb = new StringBuilder();
        while (i < cc.length) {
            char c = cc[i];
            switch (c) {
                case '.':
                case '!':
                case '$':
                case '[':
                case ']':
                case '(':
                case ')':
                case '?':
                case '^':
                case '\\': {
                    sb.append('\\').append(c);
                    break;
                }
                case '*': {
//                    if (i + 1 < cc.length && cc[i + 1] == '*') {
//                        i++;
//                        sb.append("[a-zA-Z_0-9_$.-]*");
//                    } else {
//                        sb.append("[a-zA-Z_0-9_$-]*");
//                    }
                    sb.append(".*");
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
            i++;
        }
        if (!contains) {
            sb.insert(0, '^');
            sb.append('$');
        }
        return sb.toString();
    }

    private static Pattern pattern = Pattern.compile("\\$\\{(?<key>[^}]*)\\}");

    public static String replaceVars(String format, Map<String,String> map) {
        return replaceVars(format,new MapStringMapper(map));
    }

    public static String replaceVars(String format, Properties map) {
        return replaceVars(format,new MapStringMapper((Map) map));
    }

    public static String replaceVars(String format, StringMapper map) {
        return replaceVars(format, map, new HashSet<>());
    }

    private static String replaceVars(String format, StringMapper map, Set<String> visited) {
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(format);
        while (m.find()) {
            String key = m.group("key");
            if (visited.contains(key)) {
                m.appendReplacement(sb, key);
            } else {
                Set<String> visited2 = new HashSet<>(visited);
                visited2.add(key);
                String replacement = map.get(key);
                if (replacement != null) {//replace if founded key exists in map
                    replacement = replaceVars(replacement, map, visited2);
                    m.appendReplacement(sb, escapeReplacementStrings(replacement));
                } else {//do not replace, or to be precise replace with same value
                    m.appendReplacement(sb, escapeReplacementStrings(m.group()));
                }
            }
        }
        m.appendTail(sb);

        return sb.toString();
    }

    private static String escapeReplacementStrings(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            switch (c) {
                case '\\':
                case '{':
                case '}':
                case '$': {
                    sb.append("\\");
                    sb.append(c);
                    break;
                }
                default: {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//        System.out.println(parseMap("a='b&b'&c=\"\\ne\"", "&"));
////        StringReader r = new StringReader("aa='bbb'&cc='ddd'");
////        StringBuilder result = new StringBuilder();
////        try {
////            int n = readToken(r, "&=", result);
////            System.out.println(result);
////            System.out.println((char) n);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }

    /**
     *
     * @param text
     * @param compact if true, quotes will not be used unless necessary
     * @param entrySeparators
     * @return
     */
    public static String simpleQuote(String text, boolean compact, String entrySeparators) {
        StringBuilder sb = new StringBuilder();
        boolean q = !compact;
        for (char c : text.toCharArray()) {
            switch (c) {
                case '\n': {
                    q = true;
                    sb.append("\\n");
                    break;
                }
                case '\f': {
                    q = true;
                    sb.append("\\f");
                    break;
                }
                case '\r': {
                    q = true;
                    sb.append("\\r");
                    break;
                }
                case '\'':
                case '\"':
                    {
                    q = true;
                    sb.append("\\").append(c);
                    break;
                }
                default: {
                    if(entrySeparators.indexOf(c)>=0){
                        q = true;
                        sb.append("\\").append(c);
                    }else{
                        sb.append(c);
                    }
                    break;
                }
            }
        }
        if(q){
            sb.insert(0,'\'');
            sb.append('\'');
        }
        return sb.toString();
    }

    public static Map<String, String> parseMap(String text, String entrySeparators) {
        return parseMap(text, "=", entrySeparators);
    }

    public static Map<String, String> parseMap(String text, String eqSeparators, String entrySeparators) {
        Map<String, String> m = new HashMap<>();
        StringReader reader = new StringReader(text);
        while (true) {
            StringBuilder key = new StringBuilder();
            int r = 0;
            try {
                r = readToken(reader, eqSeparators + entrySeparators, key);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String t = key.toString();
            if (r == -1) {
                if (!t.isEmpty()) {
                    m.put(t, null);
                }
                break;
            } else {
                char c = (char) r;
                if (eqSeparators.indexOf(c) >= 0) {
                    StringBuilder value = new StringBuilder();
                    try {
                        r = readToken(reader, entrySeparators, value);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    m.put(t, value.toString());
                    if (r == -1) {
                        break;
                    }
                } else {
                    //
                }
            }
        }
        return m;
    }

    public static int readToken(Reader reader, String stopTokens, StringBuilder result) throws IOException {
        while (true) {
            int r = reader.read();
            if (r == -1) {
                return -1;
            }
            if (r == '\"' || r == '\'') {
                char s = (char) r;
                while (true) {
                    r = reader.read();
                    if (r == -1) {
                        throw new RuntimeException("Expected " + '\"');
                    }
                    if (r == s) {
                        break;
                    }
                    if (r == '\\') {
                        r = reader.read();
                        if (r == -1) {
                            throw new RuntimeException("Expected " + '\"');
                        }
                        switch ((char) r) {
                            case 'n': {
                                result.append('\n');
                                break;
                            }
                            case 'r': {
                                result.append('\r');
                                break;
                            }
                            case 'f': {
                                result.append('\f');
                                break;
                            }
                            default: {
                                result.append((char) r);
                            }
                        }
                    } else {
                        char cr = (char) r;
                        result.append(cr);
                    }
                }
            } else {
                char cr = (char) r;
                if (stopTokens != null && stopTokens.indexOf(cr) >= 0) {
                    return cr;
                }
                result.append(cr);
            }
        }
    }
    public static String formatLeft(Object number,int size){
        StringBuilder sb=new StringBuilder(size);
        sb.append(number);
        while (sb.length()<size){
            sb.append(' ');
        }
        return sb.toString();
    }
    public static String formatRight(Object number,int size){
        StringBuilder sb=new StringBuilder(size);
        sb.append(number);
        while (sb.length()<size){
            sb.insert(0,' ');
        }
        return sb.toString();
    }
    public static String toCapitalized(String name){
        char[] chars = name.toLowerCase().toCharArray();
        chars[0]=Character.toUpperCase(chars[0]);
        return new String(chars);
    }

}
