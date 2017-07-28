package net.vpc.scholar.hadrumaths.util.swingext;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class JDialog2 extends JDialog{
    private Map clientProperties;


    public final Object getClientProperty(Object key) {
         if(clientProperties == null) {
            return null;
        } else {
            return getClientProperties().get(key);
        }
    }

    public final void putClientProperty(Object key, Object value) {
        Object oldValue=getClientProperties().get(key);
        if (value != null) {
                getClientProperties().put(key, value);
                firePropertyChange(key.toString(), oldValue, value);
        } else if (oldValue != null) {
            getClientProperties().remove(key);
            firePropertyChange(key.toString(), oldValue, value);
        }
    }

    private Map getClientProperties() {
        if (clientProperties == null) {
            clientProperties = new HashMap(2);
        }
        return clientProperties;
    }

    public JDialog2() throws HeadlessException {
    }

    public JDialog2(Dialog owner) throws HeadlessException {
        super(owner);
    }

    public JDialog2(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public JDialog2(Dialog owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public JDialog2(Dialog owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
    }

    public JDialog2(Dialog owner, String title, boolean modal,
                 GraphicsConfiguration gc) throws HeadlessException {
        super(owner, title, modal, gc);
    }

    public JDialog2(Frame owner) throws HeadlessException {
        super(owner);
    }

    public JDialog2(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);
    }

    public JDialog2(Frame owner, String title) throws HeadlessException {
        super(owner, title);
    }

    public JDialog2(Frame owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);
    }

    public JDialog2(Frame owner, String title, boolean modal,
                 GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
    }

    public static JDialog createDialog(
            Component owner,
            String title,
            boolean modal,
            GraphicsConfiguration gc){
        if(isAncestorDialog(owner)){
            return new JDialog(getDialogAncestor(owner),title,modal,gc);
        }else{
            return new JDialog(getFrameAncestor(owner),title,modal,gc);
        }
    }

    public static JDialog createDialog(
            Component owner,
            String title,
            boolean modal){
        if(isAncestorDialog(owner)){
            return new JDialog(getDialogAncestor(owner),title,modal);
        }else{
            return new JDialog(getFrameAncestor(owner),title,modal);
        }
    }

    public static JDialog createDialog(
            Component owner,
            String title){
        if(isAncestorDialog(owner)){
            return new JDialog(getDialogAncestor(owner),title);
        }else{
            return new JDialog(getFrameAncestor(owner),title);
        }
    }

    public static JDialog createDialog(
            Component owner){
        if(isAncestorDialog(owner)){
            return new JDialog(getDialogAncestor(owner));
        }else{
            return new JDialog(getFrameAncestor(owner));
        }
    }

    public static JDialog2 createDialog2(
            Component owner,
            String title,
            boolean modal,
            GraphicsConfiguration gc){
        if(isAncestorDialog(owner)){
            return new JDialog2(getDialogAncestor(owner),title,modal,gc);
        }else{
            return new JDialog2(getFrameAncestor(owner),title,modal,gc);
        }
    }

    public static JDialog2 createDialog2(
            Component owner,
            String title,
            boolean modal){
        if(isAncestorDialog(owner)){
            return new JDialog2(getDialogAncestor(owner),title,modal);
        }else{
            return new JDialog2(getFrameAncestor(owner),title,modal);
        }
    }

    public static JDialog2 createDialog2(
            Component owner,
            String title){
        if(isAncestorDialog(owner)){
            return new JDialog2(getDialogAncestor(owner),title);
        }else{
            return new JDialog2(getFrameAncestor(owner),title);
        }
    }

    public static JDialog2 createDialog2(
            Component owner){
        if(isAncestorDialog(owner)){
            return new JDialog2(getDialogAncestor(owner));
        }else{
            return new JDialog2(getFrameAncestor(owner));
        }
    }
    public static boolean isAncestorDialog(Component component){
        return getDialogAncestor(component)!=null;
    }

    public static Dialog getDialogAncestor(Component component){
        return (Dialog) SwingUtilities.getAncestorOfClass(Dialog.class,component);
    }

    public static boolean isAncestorFrame(Component component){
        return getDialogAncestor(component)!=null;
    }

    public static Frame getFrameAncestor(Component component){
        return (Frame) SwingUtilities.getAncestorOfClass(Frame.class,component);
    }

}
