/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.format.impl;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.FormatFactory;
import net.thevpc.scholar.hadrumaths.format.ObjectFormat;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatContext;
import net.thevpc.scholar.hadrumaths.format.ObjectFormatParamSet;

/**
 * @author vpc
 */
public class MatrixObjectFormat implements ObjectFormat<ComplexMatrix> {
    public MatrixObjectFormat() {
    }

    @Override
    public String format(ComplexMatrix o, ObjectFormatParamSet format, ObjectFormatContext context) {
        StringBuilder sb = new StringBuilder();
        format(o, context);
        return sb.toString();

    }

    @Override
    public void format(ComplexMatrix o, ObjectFormatContext context) {
        ObjectFormatParamSet format=context.getParams();
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
        context.append("[");
        for (int i = 0; i < elements.length; i++) {
            if (i > 0 || elements.length > 1) {
                context.append(lineSep);
            }
            for (int j = 0; j < elements[i].length; j++) {
//                StringBuilder sbl = new StringBuilder(colsWidth[j]);
                //sbl.clear();
                if (j > 0) {
                    context.append(" ");
                }
                long oldLen = context.length();
                context.format( elements[i][j], format);
                long newLen = context.length();
                int x = (int)(colsWidth[j] - (newLen - oldLen));
                while (x > 0) {
                    context.append(' ');
                    x--;
                }
                //sbl.append(' ');
            }
        }
        if (elements.length > 1) {
            context.append(lineSep);
        }
        context.append("]");
    }
}
