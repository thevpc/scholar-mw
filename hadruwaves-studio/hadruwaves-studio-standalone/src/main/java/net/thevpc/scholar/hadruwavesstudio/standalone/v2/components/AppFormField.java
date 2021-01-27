/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author vpc
 */
public class AppFormField extends AppFormItem {

    private JLabel label;
    private JComponent component;
    private JComponent suffix;
    private boolean area;
    AppFormValue value;

    public AppFormField(String id, JLabel label, JComponent component, JComponent suffix, AppFormValue value) {
        super(id);
        this.label = label;
        this.component = component;
        this.suffix = suffix;
        if (component instanceof JTextArea) {
            area = true;
        }
    }

    public <T> T getValue() {
        return (T) value.getValue(this);
    }

    public <T> AppFormField setValue(T t) {
        value.setValue(this, t);
        return this;
    }

    public JLabel getLabel() {
        return label;
    }

    public JComponent getComponent() {
        return component;
    }

    public JComponent getSuffix() {
        return suffix;
    }

    public boolean isArea() {
        return area;
    }

    public AppFormField newLine(boolean newLine) {
        return (AppFormField) super.newLine(newLine);
    }

    @Override
    public void build(AppForm.FormBuildHelper bh) {
        int cs = colspan();
        if (isArea()) {
            bh.span(cs, 2);
        } else {
            bh.span(cs, 1);
        }
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = bh.gridx() * 3 + 0;
        c.gridy = bh.gridy();
        if (isArea()) {
            if (getSuffix() != null) {
                c.gridwidth = 2 + 3 * (cs - 1);
            } else {
                c.gridwidth = 3 + 3 * (cs - 1);
            }
        } else {
            c.gridwidth = 1;
        }
        c.gridheight = 1;
        c.insets = new Insets(2, 2, 2, 2 + 5);
        bh.addComponent(getLabel(), c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 3 * bh.gridx() + 1;
        c.gridy = bh.gridy();
        c.weightx = 2;
        c.weighty = isArea() ? 2 : 0;
        c.gridwidth = (isArea() ? 3 : 1) + 3 * (cs - 1);
        c.gridheight = 1;
        c.insets = new Insets(2, 2, 2, 2);
        bh.addComponent(getComponent(), c);
        if (getSuffix() != null) {
            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 3 * bh.gridx() + 2 + 3 * (cs - 1);
            c.gridy = bh.gridy();
            c.weightx = 0;
            c.weighty = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(2, 2, 2, 2);
            bh.addComponent(getSuffix(), c);
        }
        if (isNewLine()) {
            bh.newLine();
        }
    }

}
