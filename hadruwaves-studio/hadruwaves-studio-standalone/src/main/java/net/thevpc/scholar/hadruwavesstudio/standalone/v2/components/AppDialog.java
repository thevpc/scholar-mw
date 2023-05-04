/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.jdesktop.swingx.JXDialog;
import static org.jdesktop.swingx.JXDialog.EXECUTE_ACTION_COMMAND;

/**
 *
 * @author vpc
 */
public abstract class AppDialog {

    private Object parent;
    private JComponent component;
    private String okButtonName = "Ok";
    private String returnActionCommand = null;
    private String title = null;
    JXDialog dialog = null;

    public AppDialog(Object parent, String title) {
        this.parent = parent;
        this.title = title;
    }

    protected abstract JComponent createComponent();

    public boolean show() {
        show0();
        return "Ok".equals(returnActionCommand);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }
    

    private void show0() {
        returnActionCommand = null;
        if (component == null) {
            component = createComponent();
            component.getActionMap().put(EXECUTE_ACTION_COMMAND, new AbstractAction(okButtonName) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    returnActionCommand = "Ok";
                    dialog.setVisible(false);
                }
            });
        }else{
            SwingUtilities.updateComponentTreeUI(component);
        }
        Object parent = this.parent;
        if (parent instanceof Application) {
            parent = ((Application) parent).mainFrame().get().peer().toolkitComponent();
        } else if (parent instanceof HadruwavesStudio) {
            parent = ((HadruwavesStudio) parent).app().mainFrame().get().peer().toolkitComponent();
        }
        if (parent == null) {
            dialog = new JXDialog(component);
        } else if (parent instanceof Frame) {
            dialog = new JXDialog((Frame) parent, component);
            dialog.setLocationRelativeTo((Frame) parent);
        } else if (parent instanceof Dialog) {
            dialog = new JXDialog((Dialog) parent, component);
            dialog.setLocationRelativeTo((Dialog) parent);
        } else if (parent instanceof Component) {
            //chek parent
            dialog = new JXDialog(component);
            dialog.setLocationRelativeTo((Component) parent);
        } else {
            dialog = new JXDialog(component);
        }
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.pack();

        dialog.setVisible(true);
    }
}
