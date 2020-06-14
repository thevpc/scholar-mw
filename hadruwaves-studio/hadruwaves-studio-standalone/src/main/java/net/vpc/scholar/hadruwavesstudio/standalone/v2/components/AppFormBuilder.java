/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXLabel;

/**
 *
 * @author vpc
 */
public class AppFormBuilder {

    public AppFormField create(String id, String label, Class cls) {
        return create(id, label, null, cls);
    }

    public AppFormField create(String id, String label, JComponent component, Class cls) {
        AppFormField i = new AppFormField(id, new JXLabel(label), component == null ? createComponentForClass(cls) : component, null, null);
        i.value = AppFormValues.valueOf(cls, i);
        return i;
    }

    public JComponent createComponentForClass(Class cls) {
        if (cls.equals(String.class)) {
            JTextField jTextField = new JTextField();
            jTextField.setColumns(20);
            return jTextField;
        }
        if (cls.equals(Integer.class)) {
            JTextField jTextField = new JTextField();
            jTextField.setColumns(20);
            return jTextField;
        }
        if (cls.equals(Long.class)) {
            JTextField jTextField = new JTextField();
            jTextField.setColumns(20);
            return jTextField;
        }
        if (cls.equals(Double.class)) {
            JTextField jTextField = new JTextField();
            jTextField.setColumns(20);
            return jTextField;
        }
        if (cls.isEnum()) {
            return new JComboBox(cls.getEnumConstants());
        }
        throw new IllegalArgumentException("Unsupported");
    }

    public AppFormField create(String id, String label, JComponent component, AppFormValue value) {
        return new AppFormField(id, new JXLabel(label), component, null, value);
    }

    public AppFormField create(String id, JLabel label, JComponent component, JComponent suffix, AppFormValue value) {
        return new AppFormField(id, label, component, suffix, value);
    }
}
