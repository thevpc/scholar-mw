/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdesktop.swingx.JXDialog;
import static org.jdesktop.swingx.JXDialog.EXECUTE_ACTION_COMMAND;

/**
 *
 * @author vpc
 */
public class StringExpressionDialog {

    private JXDialog dialog;
    private HWTextEditor messagesArea;
    private String returnActionCommand;
    private HadruwavesStudio studio;

    public StringExpressionDialog(HadruwavesStudio studio) {
        this.studio = studio;
        messagesArea = new HWTextEditor(studio,20, 30);
        messagesArea.setType("shell");
        messagesArea.setEditable(true);
        messagesArea.getActionMap().put(EXECUTE_ACTION_COMMAND, new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnActionCommand = "save";
                dialog.setVisible(false);
            }
        });
        this.dialog = new JXDialog(
                (JFrame) studio.appComponent(),
                messagesArea);
        dialog.setTitle("Expression");
        dialog.setLocationByPlatform(true);
//        JXStatusBar jxStatusBar = new JXStatusBar();
//        this.dialog.setStatusBar(jxStatusBar);
//        JXButton saveButton = new JXButton("Save");
//        saveButton.setActionCommand("save");
//        JXButton cancelButton = new JXButton("Cancel");
//        cancelButton.setActionCommand("cancel");
//        jxStatusBar.add(saveButton);
//        jxStatusBar.add(cancelButton);
//        ActionListener actionListener = new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                returnActionCommand = e.getActionCommand();
//                dialog.setVisible(false);
//            }
//        };
//        saveButton.addActionListener(actionListener);
    }

    public void setTitle(String title) {
        dialog.setTitle(title);
    }

    public void setExpression(String expression) {
        messagesArea.setText(expression);
    }

    public String getExpression() {
        return messagesArea.getText();
    }

    public boolean show() {
        returnActionCommand = null;
        dialog.setModal(true);
        dialog.setMinimumSize(new Dimension(400, 400));
        dialog.pack();
        dialog.setVisible(true);
        return "save".equals(returnActionCommand);
    }

}
