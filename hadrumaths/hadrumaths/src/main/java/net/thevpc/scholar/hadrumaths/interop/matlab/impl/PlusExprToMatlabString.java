/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadrumaths.interop.matlab.impl;

import net.thevpc.scholar.hadrumaths.Expr;
import net.thevpc.scholar.hadrumaths.interop.matlab.MatlabFactory;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabString;
import net.thevpc.scholar.hadrumaths.interop.matlab.ToMatlabStringParam;
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.num.Plus;

import java.util.List;

/**
 * @author vpc
 */
public class PlusExprToMatlabString implements ToMatlabString<Plus> {

    @Override
    public String toMatlabString(Plus o, ToMatlabStringParam... format) {
        List<Expr> segments = o.getChildren();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < segments.size(); i++) {
            if (i > 0) {
                sb.append(" + ");
            }
            sb.append("(");
            sb.append(MatlabFactory.toMatlabString(segments.get(i), format));
            sb.append(")");
        }
        return sb.toString();
    }
}
