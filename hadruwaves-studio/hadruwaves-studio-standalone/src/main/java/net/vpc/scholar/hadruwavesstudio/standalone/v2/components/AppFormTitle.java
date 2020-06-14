/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.components;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import org.jdesktop.swingx.JXLabel;

/**
 *
 * @author vpc
 */
public class AppFormTitle extends AppFormItem {

    private JLabel label;
    private JSeparator separator;

    public AppFormTitle(String id, String label) {
        super(id);
        this.label = label == null ? null : new JXLabel(label);
        this.separator = new JSeparator();
    }

    public AppFormTitle newLine(boolean newLine) {
        return (AppFormTitle) super.newLine(newLine);
    }

    @Override
    public void build(AppForm.FormBuildHelper bh) {
        int cs = colspan();
        bh.set(cs, 1);
        if (label == null) {
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 3 * bh.gridx() + 0;
            c.gridy = bh.gridy();
            c.weightx = 3;
            c.weighty = 0;
            c.gridwidth = 3 * cs;
            c.gridheight = 1;
            c.insets = new Insets(7, 2, 2, 2);
            bh.addComponent(separator, c);
        } else {
            GridBagConstraints c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.BOTH;
            c.gridx = bh.gridx() * 3 + 0;
            c.gridy = bh.gridy();
            c.gridwidth = 1;
            c.gridheight = 1;
            c.insets = new Insets(2, 2, 2, 2 + 5);
            bh.addComponent(label, c);

            c = new GridBagConstraints();
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 3 * bh.gridx() + 1;
            c.gridy = bh.gridy();
            c.weightx = 2;
            c.weighty = 0;
            c.gridwidth = 2 + (cs - 1) * 3;
            c.gridheight = 1;
            c.insets = new Insets(7, 2, 2, 2);
            bh.addComponent(separator, c);

        }

        if (isNewLine()) {
            bh.newLine();
        }
    }

}
