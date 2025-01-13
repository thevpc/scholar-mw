/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwaves.props;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 *
 * @author vpc
 */
public interface EnumExpression {

    //static Object[] values();
    static <T extends EnumExpression> T valueOf(Class c, String name) {
        if (EnumExpression.class.isAssignableFrom(c)) {
            Method m;
            try {
                m = c.getDeclaredMethod("valueOf", String.class);
                if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
                    return (T) m.invoke(null, name);
                }
            } catch (Exception ex) {
                //
            }
            throw new IllegalArgumentException("Invalid EnumExpression");
        } else {
            throw new IllegalArgumentException("Expexted EnumExpression");
        }
    }

    static Object[] values(Class c) {
        if (EnumExpression.class.isAssignableFrom(c)) {
            Method m;
            try {
                m = c.getDeclaredMethod("values");
                if (Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers())) {
                    return (Object[]) m.invoke(null);
                }
            } catch (Exception ex) {
                //
            }
            throw new IllegalArgumentException("Invalid EnumExpression");
        } else {
            throw new IllegalArgumentException("Expexted EnumExpression");
        }
    }
}
