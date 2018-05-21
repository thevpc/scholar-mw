package net.vpc.scholar.hadrumaths.plot.swings;

import java.util.Arrays;

public class Utils {

    public static String drawBox(String header) {
        StringBuilder sb = new StringBuilder();
        int width = header.length() + 4;
        //Formatter title = new Formatter(width, 2);
        String asterixBar = "  " + drawLine(width + 2, '*');
        String whiteBar = "  *" + drawLine(width, ' ') + "*";
        sb.append(asterixBar).append("\n").append(whiteBar).append("\n  *").append(header).append("*\n").append(whiteBar).append("\n").append(asterixBar);
        return sb.toString();
    }

    public static String drawLine(int width, char pattern) {
        char[] c = new char[width];
        Arrays.fill(c, pattern);
        return new String(c);
    }

    public static String drawLine(int width, String pattern) {
        int l = pattern.length();
        char[] c = new char[width * l];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < l; j++)
                c[i * l + j] = pattern.charAt(j);

        }

        return new String(c);
    }

}
