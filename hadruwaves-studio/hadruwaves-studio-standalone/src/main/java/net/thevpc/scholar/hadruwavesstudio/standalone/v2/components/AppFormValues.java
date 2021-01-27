/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author vpc
 */
public class AppFormValues {

    public static AppFormValue valueOf(Class type, AppFormField item) {
        JComponent c = item.getComponent();
        if (c instanceof JTextComponent) {
            return new AppFormValue() {
                @Override
                public Object getValue(AppFormField item) {
                    JTextComponent c = (JTextComponent) item.getComponent();
                    return convert(c.getText(), type);
                }

                @Override
                public void setValue(AppFormField item, Object value) {
                    JTextComponent c = (JTextComponent) item.getComponent();
                    c.setText(value == null ? "" : String.valueOf(value));
                }
            };
        } else if (c instanceof JComboBox) {
            return new AppFormValue() {
                @Override
                public Object getValue(AppFormField item) {
                    JComboBox c = (JComboBox) item.getComponent();
                    Object i = c.getSelectedItem();
                    return convert(i, type);
                }

                @Override
                public void setValue(AppFormField item, Object value) {
                    JComboBox c = (JComboBox) item.getComponent();
                    Object v2 = convert(value, type);
                    c.setSelectedItem(v2);
                }
            };
        } else if (c instanceof JCheckBox) {
            return new AppFormValue() {
                @Override
                public Object getValue(AppFormField item) {
                    JCheckBox c = (JCheckBox) item.getComponent();
                    Object i = c.isSelected();
                    return convert(i, type);
                }

                @Override
                public void setValue(AppFormField item, Object value) {
                    JCheckBox c = (JCheckBox) item.getComponent();
                    Boolean v2 = (Boolean) convert(value, Boolean.class);
                    if (v2 == null) {
                        v2 = false;
                    }
                    c.setSelected(v2);
                }
            };
        } else {
            throw new IllegalArgumentException("Unsupported");
        }
    }

    private static Object convert(Object v, Class to) {
        if (to.equals(String.class)) {
            if (v == null) {
                return "";
            }
            return String.valueOf(v);
        }
        if (to.equals(Integer.class)) {
            if (v == null) {
                return null;
            }
            if (v instanceof Number) {
                return ((Number) v).intValue();
            }
            return Integer.parseInt(String.valueOf(convert(v, String.class)));
        }
        if (to.equals(Double.class)) {
            if (v == null) {
                return null;
            }
            if (v instanceof Number) {
                return ((Number) v).doubleValue();
            }
            return Double.parseDouble(String.valueOf(convert(v, String.class)));
        }
        if (to.isEnum()) {
            if (v == null) {
                return null;
            }
            if (v instanceof Number) {
                return to.getEnumConstants()[((Number) v).intValue()];
            }
            String s = String.valueOf(convert(v, String.class));
            for (Object enumConstant : to.getEnumConstants()) {
                if (enumConstant.toString().equals(s)) {
                    return enumConstant;
                }
            }
            return null;
        }
        throw new IllegalArgumentException("Unexpected");
    }
}
