package net.vpc.scholar.hadruwaves.interop;

import net.vpc.scholar.hadrumaths.ArrayDoubleVector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import net.vpc.common.strings.StringBuilder2;

public final class CstMwsUtils {

    private CstMwsUtils() {
    }

    public static CstPlotDoubleTable loadCSTLinearPlot(File file) throws UncheckedIOException {
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            String[] titles = parseTitles(r.readLine());
            String dashes = r.readLine();
            int count = titles.length;
            CstPlotDoubleColumn[] columns = new CstPlotDoubleColumn[count];
            ArrayDoubleVector[] rowsData = new ArrayDoubleVector[count];
            for (int i = 0; i < count; i++) {
                rowsData[i] = new ArrayDoubleVector();
            }
            String line = null;
            while ((line = r.readLine()) != null) {
                if (line.trim().length() > 0) {
                    String[] cc = line.trim().split(" +");
                    for (int i = 0; i < count; i++) {
                        rowsData[i].append(Double.valueOf(cc[i]));
                    }
                }
            }
            for (int i = 0; i < count; i++) {
                columns[i] = new CstPlotDoubleColumn(titles[i], rowsData[i].toDoubleArray());
            }
            return new CstPlotDoubleTable(columns);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public boolean accept(StringBuilder2 s, char c) {
        if (s.length() == 0) {
            return true;
        }
        switch (c) {
            case ' ':
            case '(':
            case ')':
            case '[':
            case ']': {
                return true;
            }
            default: {
                if (s.endsWith(" ")) {
                    return false;
                }
                return true;
            }
        }
    }

    public static String[] parseTitles(String titles) {
        List<String> all = new ArrayList<>();
        StringBuilder2 sb = new StringBuilder2();
        char[] cc = titles.toCharArray();
        int par = 0;
        int brak = 0;
        for (int i = 0; i < cc.length; i++) {
            switch (cc[i]) {
                case '(': {
                    sb.append(cc[i]);
                    par++;
                    break;
                }
                case ')': {
                    sb.append(cc[i]);
                    par--;
                    break;
                }
                case '[': {
                    sb.append(cc[i]);
                    brak++;
                    break;
                }
                case ']': {
                    sb.append(cc[i]);
                    brak--;
                    break;
                }
                case ' ': {
                    sb.append(cc[i]);
                    break;
                }
                default: {
                    if (par > 0 ||
                            brak > 0 ||
                            (!sb.toString().trim().endsWith("]") && !sb.toString().trim().endsWith(")"))) {
                        sb.append(cc[i]);
                    } else {
                        all.add(sb.toString());
                        sb.delete();
                        sb.append(cc[i]);
                    }
                }
            }
        }
        String s = sb.toString().trim();
        if (s.length() > 0) {
            all.add(s);
        }
        return all.toArray(new String[0]);
    }
}
