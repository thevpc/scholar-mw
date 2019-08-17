/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths.format.impl;

import net.vpc.scholar.hadrumaths.Complex;
import net.vpc.scholar.hadrumaths.FormatFactory;
import net.vpc.scholar.hadrumaths.Matrix;
import net.vpc.scholar.hadrumaths.format.ObjectFormat;
import net.vpc.scholar.hadrumaths.format.ObjectFormatParamSet;

/**
 * @author vpc
 */
public class MatrixObjectFormat implements ObjectFormat<Matrix> {
    public MatrixObjectFormat() {
    }

    @Override
    public String format(Matrix o, ObjectFormatParamSet format) {
        StringBuilder sb = new StringBuilder();
        format(sb, o, format);
        return sb.toString();

    }

    @Override
    public void format(StringBuilder sb, Matrix o, ObjectFormatParamSet format) {
        Complex[][] elements = o.getArray();
        int[] colsWidth = new int[o.getColumnCount()];
        for (Complex[] element : elements) {
            for (int j = 0; j < element.length; j++) {
                int len = String.valueOf(element[j]).length();
                if (len > colsWidth[j]) {
                    colsWidth[j] = len;
                }
            }
        }

        String lineSep = System.getProperty("line.separator");
        sb.append("[");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0 || elements.length > 1) {
                sb.append(lineSep);
            }
            for (int j = 0; j < elements[i].length; j++) {
                StringBuilder sbl = new StringBuilder(colsWidth[j]);
                //sbl.clear();
                if (j > 0) {
                    sbl.append(' ');
                }
                int oldLen = sbl.length();
                FormatFactory.format(sbl, elements[i][j], format);
                int newLen = sbl.length();
                int x = colsWidth[j] - (newLen - oldLen);
                while (x > 0) {
                    sbl.append(' ');
                    x--;
                }
                //sbl.append(' ');
                sb.append(sbl.toString());
            }
        }
        if (elements.length > 1) {
            sb.append(lineSep);
        }
        sb.append("]");
    }
}
