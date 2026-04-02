package net.thevpc.scholar.hadruwaves.interop;

import net.thevpc.nuts.util.NStringBuilder;
import net.thevpc.scholar.hadrumaths.ArrayDoubleVector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

    public boolean accept(NStringBuilder s, char c) {
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
        NStringBuilder sb = new NStringBuilder();
        char[] cc = titles.toCharArray();
        int par = 0;
        int brak = 0;
        for (char c : cc) {
            switch (c) {
                case '(': {
                    sb.append(c);
                    par++;
                    break;
                }
                case ')': {
                    sb.append(c);
                    par--;
                    break;
                }
                case '[': {
                    sb.append(c);
                    brak++;
                    break;
                }
                case ']': {
                    sb.append(c);
                    brak--;
                    break;
                }
                case ' ': {
                    sb.append(c);
                    break;
                }
                default: {
                    if (par > 0 ||
                            brak > 0 ||
                            (!sb.toString().trim().endsWith("]") && !sb.toString().trim().endsWith(")"))) {
                        sb.append(c);
                    } else {
                        all.add(sb.toString());
                        sb.clear();
                        sb.append(c);
                    }
                }
            }
        }
        String s = sb.toString().trim();
        if (!s.isEmpty()) {
            all.add(s);
        }
        return all.toArray(new String[0]);
    }
}
