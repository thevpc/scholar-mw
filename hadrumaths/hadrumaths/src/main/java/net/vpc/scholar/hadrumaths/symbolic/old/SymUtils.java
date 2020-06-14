package net.vpc.scholar.hadrumaths.symbolic.old;

import net.vpc.scholar.hadrumaths.Align;

import java.util.ArrayList;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 * @creationtime 18 juil. 2007 21:52:28
 */
public class SymUtils {

    public static final BoxFormat NO_FORMAT = new BoxFormat();

    private static String fillAlignEast(String suffix, String patern, int length) {
        StringBuilder sb = new StringBuilder(suffix);
        if (patern.length() > 0) {
            while (sb.length() < length) {
                sb.insert(0, patern);
            }
            if (sb.length() > length) {
                sb.delete(0, length - sb.length());
            }
            return sb.toString();
        }
        return "";
    }

    private static String fillAlignCenter(String suffix, String patern, int length) {
        StringBuilder sb = new StringBuilder(suffix);
        if (patern.length() > 0) {
            boolean before = false;
            while (sb.length() < length) {
                if (before) {
                    sb.insert(0, patern);
                } else {
                    sb.append(patern);
                }
                before = !before;
            }
            before = !before;
            while (sb.length() > length) {
                if (before) {
                    sb.delete(0, 1);
                } else {
                    sb.delete(sb.length() - 1, sb.length());
                }
            }
            return sb.toString();
        }
        return "";
    }

    public static String formatRow(BoxFormat format, String... values) {
        return format(format, new String[][]{values});
    }

    public static String format(BoxFormat format, String[][] values) {
        if (format == null) {
            format = NO_FORMAT;
        }
        int rows = values.length;
        int columns = 0;
        for (String[] value : values) {
            if (value.length > columns) {
                columns = value.length;
            }
        }
        Str[][] cells = new Str[rows][columns];
        StringBuilder sb = new StringBuilder();
        int[] c = new int[columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                String ss = "";
                if (j < values[i].length) {
                    ss = values[i][j];
                }
                cells[i][j] = stringSize(ss);
                if (cells[i][j].c > c[j]) {
                    c[j] = cells[i][j].c;
                }
            }
        }

        int ccc = 0;
        for (int j = 0; j < columns; j++) {
            if (j > 0) {
                ccc += format.getColumnsSeparator().length();
            }
            ccc += c[j];
        }
        if (format.getTop().length() > 0) {
            sb.append(fillAlignWest(format.getCornerNW(), " ", (format.getLeft().length())));
            sb.append(fillAlignWest("", format.getTop(), ccc));
            sb.append(fillAlignWest(format.getCornerNE(), " ", (format.getLeft().length())));
            sb.append('\n');
        }
        for (int i = 0; i < rows; i++) {
            if (i > 0 && format.getRowsSeparator().length() > 0) {
                sb.append(fillAlignWest(format.getCornerW().length() == 0 ? format.getLeft() : format.getCornerW(), " ", (format.getLeft().length())));
                for (int j = 0; j < columns; j++) {
                    if (j > 0) {
                        sb.append(fillAlignWest(format.getCornerC(), format.getRowsSeparator(), format.getColumnsSeparator().length()));
                    }
                    sb.append(fillAlignWest("", format.getRowsSeparator(), c[j]));
                }
                sb.append(fillAlignWest(format.getCornerE().length() == 0 ? format.getRight() : format.getCornerE(), " ", (format.getRight().length())));
                sb.append('\n');
            }

            int r = 0;
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].r > r) {
                    r = cells[i][j].r;
                }
            }
            for (int j = 0; j < columns; j++) {
                cells[i][j].align(format.getAlign(), r, c[j]);
            }
            Str[] cellsLine = cells[i];
            for (int ii = 0; ii < r; ii++) {
                sb.append(format.getLeft());
                for (int jj = 0; jj < cellsLine.length; jj++) {
                    if (jj > 0) {
                        sb.append(format.getColumnsSeparator());
                    }
                    sb.append(cellsLine[jj].lines[ii]);
                }
                sb.append(format.getRight());
                sb.append('\n');
            }
        }
        if (format.getBottom().length() > 0) {
            sb.append(fillAlignWest(format.getCornerSW(), " ", (format.getLeft().length())));
            sb.append(fillAlignWest("", format.getBottom(), ccc));
            sb.append(fillAlignWest(format.getCornerSE(), " ", (format.getLeft().length())));
            sb.append('\n');
        }
        return sb.toString();
    }

    private static Str stringSize(String s) {
        String[] lines = s.split("\n");
        int c = 0;
        int r = lines.length;
        for (String line : lines) {
            if (line.length() > c) {
                c = line.length();
            }
        }
        return new Str(lines, r, c);
    }

    private static String fillAlignWest(String prefix, String patern, int length) {
        StringBuilder sb = new StringBuilder(prefix);
        if (patern.length() > 0) {
            while (sb.length() < length) {
                sb.append(patern);
            }
            return sb.substring(0, length);
        }
        return "";
    }

    public static String formatColumn(BoxFormat format, String... values) {
        String[][] r = new String[values.length][1];
        for (int i = 0; i < values.length; i++) {
            r[i][0] = values[i];

        }
        return format(format, r);
    }

    private static class Str {
        int r;
        int c;
        String[] lines;

        public Str(String[] lines, int r, int c) {
            this.lines = lines;
            this.r = r;
            this.c = c;
        }

        private void align(Align align, int rmax, int cmax) {
            ArrayList<String> newLines = new ArrayList<String>();
            for (String line : lines) {
                switch (align) {
                    case EAST:
                    case NORTH_EAST:
                    case SOUTH_EAST: {
                        newLines.add(fillAlignEast(line, " ", cmax));
                        break;
                    }
                    case WEST:
                    case NORTH_WEST:
                    case SOUTH_WEST: {
                        newLines.add(fillAlignWest(line, " ", cmax));
                        break;
                    }
                    case CENTER: {
                        newLines.add(fillAlignCenter(line, " ", cmax));
                        break;
                    }
                }
            }
            String blanc = fillAlignWest("", " ", cmax);
            switch (align) {
                case NORTH:
                case NORTH_EAST:
                case NORTH_WEST: {
                    while (newLines.size() < rmax) {
                        newLines.add(blanc);
                    }
                    break;
                }
                case SOUTH:
                case SOUTH_EAST:
                case SOUTH_WEST: {
                    while (newLines.size() < rmax) {
                        newLines.add(0, blanc);
                    }
                    break;
                }
                case CENTER: {
                    boolean before = false;
                    while (newLines.size() < rmax) {
                        if (before) {
                            newLines.add(0, blanc);
                        } else {
                            newLines.add(blanc);
                        }
                        before = !before;
                    }
                    break;
                }
            }
            lines = newLines.toArray(new String[0]);
        }

    }

    public static class BoxFormat {
        private Align align = Align.CENTER;
        private String cornerNE = "";
        private String cornerNW = "";
        private String cornerSE = "";
        private String cornerSW = "";
        private String cornerC = "";
        private String cornerW = "";
        private String cornerE = "";
        private String top = "";
        private String bottom = "";
        private String left = "";
        private String right = "";
        private String columnsSeparator = "";
        private String rowsSeparator = "";

        public BoxFormat() {
        }


        public String getBottom() {
            return bottom;
        }

        public BoxFormat setBottom(String bottom) {
            this.bottom = bottom == null ? "" : bottom;
            return this;
        }

        public String getLeft() {
            return left;
        }

        public BoxFormat setLeft(String left) {
            this.left = left == null ? "" : left;
            return this;
        }

        public String getRight() {
            return right;
        }

        public BoxFormat setRight(String right) {
            this.right = right == null ? "" : right;
            return this;
        }

        public String getColumnsSeparator() {
            return columnsSeparator;
        }

        public BoxFormat setColumnsSeparator(String space) {
            this.columnsSeparator = space == null ? "" : space;
            return this;
        }

        public String getRowsSeparator() {
            return rowsSeparator;
        }

        public BoxFormat setRowSeparator(String space) {
            this.rowsSeparator = space == null ? "" : space;
            return this;
        }

        public String getTop() {
            return top;
        }

        public BoxFormat setTop(String top) {
            this.top = top == null ? "" : top;
            return this;
        }

        public String getCornerNE() {
            return cornerNE;
        }

        public BoxFormat setCornerNE(String cornerNE) {
            this.cornerNE = cornerNE == null ? "" : cornerNE;
            return this;
        }

        public String getCornerNW() {
            return cornerNW;
        }

        public BoxFormat setCornerNW(String cornerNW) {
            this.cornerNW = cornerNW == null ? "" : cornerNW;
            return this;
        }

        public String getCornerSE() {
            return cornerSE;
        }

        public BoxFormat setCornerSE(String cornerSE) {
            this.cornerSE = cornerSE == null ? "" : cornerSE;
            return this;
        }

        public String getCornerSW() {
            return cornerSW;
        }

        public BoxFormat setCornerSW(String cornerSW) {
            this.cornerSW = cornerSW == null ? "" : cornerSW;
            return this;
        }

        public String getCornerC() {
            return cornerC;
        }

        public BoxFormat setCornerC(String corner) {
            this.cornerC = corner == null ? "" : corner;
            return this;
        }

        public String getCornerE() {
            return cornerE;
        }

        public BoxFormat setCornerE(String corner) {
            this.cornerE = corner == null ? "" : corner;
            return this;
        }

        public String getCornerW() {
            return cornerW;
        }

        public BoxFormat setCornerW(String corner) {
            this.cornerW = corner == null ? "" : corner;
            return this;
        }

        public Align getAlign() {
            return align;
        }

        public BoxFormat setAlign(Align align) {
            this.align = align == null ? Align.CENTER : align;
            return this;
        }
    }

}
