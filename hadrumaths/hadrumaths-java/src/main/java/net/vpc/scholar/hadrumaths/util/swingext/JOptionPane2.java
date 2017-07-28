package net.vpc.scholar.hadrumaths.util.swingext;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JOptionPane2 extends JOptionPane {

    public void selectInitialValueOnFirstDialogShowing() {
        dialog.addWindowListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent we) {
                if (!gotFocus) {
                    selectInitialValue();
                    gotFocus = true;
                }
            }
            private boolean gotFocus= false;
        });
    }

    public JDialog getDialog() {
        return dialog;
    }

    public JOptionPane2(Component parentComponent, String title) {
        dialog = createDialog(parentComponent, title);
    }

    public JOptionPane2(Component parentComponent, Object message, String title) {
        dialog = createDialog(parentComponent, title);
    }

    public JOptionPane2(Component parentComponent, Object message, String title, int messageType) {
        dialog = createDialog(parentComponent, title);
    }

    public JOptionPane2(Component parentComponent, Object message, String title, int messageType, int optionType) {
        super(message, messageType, optionType);
        dialog = createDialog(parentComponent, title);
    }

    public JOptionPane2(Component parentComponent, Object message, String title, int messageType, int optionType, Icon icon) {
        super(message, messageType, optionType, icon);
        dialog = createDialog(parentComponent, title);
    }

    public JOptionPane2(Component parentComponent, Object message, String title, int messageType, int optionType, Icon icon, Object[] options) {
        super(message, messageType, optionType, icon, options);
        dialog = createDialog(parentComponent, title);
    }

    public JOptionPane2(Component parentComponent, Object message, String title, int messageType, int optionType, Icon icon, Object[] options,
                        Object initialValue) {
        super(message, messageType, optionType, icon, options, initialValue);
        dialog = createDialog(parentComponent, title);
    }

    public int showDialog() {
        dialog.setVisible(true);
        Object selectedValue = getValue();
        if (selectedValue == null)
            return -1;
        if (options == null)
            if (selectedValue instanceof Integer)
                return ((Integer) selectedValue).intValue();
            else
                return -1;
        int counter = 0;
        for (int maxCounter = options.length; counter < maxCounter; counter++)
            if (options[counter].equals(selectedValue))
                return counter;

        return -1;
    }

    public void setValue(Object newValue) {
        if (acceptValue(newValue))
            super.setValue(newValue);
    }

    public boolean acceptValue(Object newValue) {
        return true;
    }

    public static int styleFromMessageType(int messageType) {
        switch (messageType) {
            case 0: // '\0'
                return 4;

            case 3: // '\003'
                return 7;

            case 2: // '\002'
                return 8;

            case 1: // '\001'
                return 3;

            case -1:
            default:
                return 2;
        }
    }

    public JDialog createDialog(Component parentComponent, String title)
            throws HeadlessException {
        int style = styleFromMessageType(getMessageType());
        return createDialog(parentComponent, title, style);
    }

    private JDialog createDialog(Component parentComponent, String title, int style)
            throws HeadlessException {
        Window window = getWindowForComponent(parentComponent);
        final JDialog2 dialog = JDialog2.createDialog2(parentComponent,title, true);
        dialog.putClientProperty("optionPane",this);
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
        dialog.setResizable(false);
        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();
            if (supportsWindowDecorations) {
                dialog.setUndecorated(true);
                getRootPane().setWindowDecorationStyle(style);
            }
        }
        dialog.pack();
        dialog.setLocationRelativeTo(parentComponent);
        dialog.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                setValue(null);
            }

        });
        dialog.addComponentListener(new ComponentAdapter() {

            public void componentShown(ComponentEvent ce) {
                setValue(JOptionPane.UNINITIALIZED_VALUE);
            }

        });
        addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {
                if (dialog.isVisible() && event.getSource() == JOptionPane2.this && event.getPropertyName().equals("value") && event.getNewValue() != null && event.getNewValue() != JOptionPane.UNINITIALIZED_VALUE)
                    dialog.setVisible(false);
            }

        });
        return dialog;
    }

    public void pressActionOnDoubleClick(Component component,final int option){
        component.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount()==2 && SwingUtilities.isLeftMouseButton(e)){
                    setValue(new Integer(option));
                }
            }
        });
    }

    static Window getWindowForComponent(Component parentComponent)
            throws HeadlessException {
        if (parentComponent == null)
            return JOptionPane.getRootFrame();
        if ((parentComponent instanceof Frame) || (parentComponent instanceof Dialog))
            return (Window) parentComponent;
        else
            return getWindowForComponent(((Component) (parentComponent.getParent())));
    }

//    public static void showErrorDialog(Component comp,String message){
//        showErrorDialog(comp,message,null);
//    }
//    public static void showErrorDialog(Throwable throwable){
//        showErrorDialog(Swings.DEFAULT_PARENT_COMPONENT,null,throwable);
//    }
//    public static void showErrorDialog(Component comp,Throwable throwable){
//        showErrorDialog(comp,null,throwable);
//    }
//    public static void showErrorDialog(String message){
//        showErrorDialog(Swings.DEFAULT_PARENT_COMPONENT,message,null);
//    }
//    public static void showErrorDialog(String message,Throwable throwable){
//        showErrorDialog(Swings.DEFAULT_PARENT_COMPONENT,message,throwable);
//    }


    private JDialog dialog;
}
