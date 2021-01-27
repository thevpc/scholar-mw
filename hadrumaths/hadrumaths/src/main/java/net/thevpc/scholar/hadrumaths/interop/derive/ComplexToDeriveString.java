/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths.interop.derive;

import net.thevpc.scholar.hadrumaths.Complex;
import net.thevpc.scholar.hadrumaths.ComplexMatrix;

/**
 * @author vpc
 */
public class ComplexToDeriveString implements ToDeriveString<ComplexMatrix> {
    public ComplexToDeriveString() {
    }

    @Override
    public String toDeriveString(ComplexMatrix o, ToDeriveStringParam... format) {
        Complex[][] elements = o.getArray();
        StringBuilder sb = new StringBuilder();
//        String lineSep=System.getProperty("line.separator");
        sb.append('[');

        for (int i = 0; i < elements.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append('[');
            for (int j = 0; j < elements[i].length; j++) {
                if (j > 0) {
                    sb.append(',');
                }
                String disp = DeriveFactory.toDeriveString(elements[i][j], format);
                sb.append(disp);
            }
            sb.append(']');
        }
        sb.append(']');
        return sb.toString();
    }

}
