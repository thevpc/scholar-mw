/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.core.ui;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import net.vpc.scholar.hadruwaves.mom.project.MomProjectListener;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import org.netbeans.core.spi.multiview.CloseOperationState;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.MultiViewElementCallback;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.UndoRedo;
import org.openide.filesystems.FileChangeAdapter;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.InstanceContent;

//@MultiViewElement.Registration(
//        displayName = "#LBL_HWSProject_VISUAL",
//        iconBase = "net/vpc/scholar/hadruwaves/studio/core/icon-16.png",
//        mimeType = "application/hadruwaves-studio-project",
//        persistenceType = TopComponent.PERSISTENCE_NEVER,
//        preferredID = "HWSProjectVisual",
//        position = 2000
//)
//@Messages("LBL_HWSProject_VISUAL=Visual")
public final class HWSProjectVisualElement extends JPanel implements MultiViewElement {

    InstanceContent ic = new InstanceContent();

    private HWSProjectDataObject obj;
    private JToolBar toolbar = new JToolBar();
    private transient MultiViewElementCallback callback;
    private MomProjectEditor projectEditor;

    public HWSProjectVisualElement(Lookup lkp) {
        obj = lkp.lookup(HWSProjectDataObject.class);
        assert obj != null;
        initComponents();
        projectEditor = new MomProjectEditor(null);
        add(projectEditor);
        obj.getPrimaryFile().addFileChangeListener(new FileChangeAdapter() {
            @Override
            public void fileChanged(FileEvent fe) {
                reloadFile();
            }

            @Override
            public void fileRenamed(FileRenameEvent fe) {
                reloadFile();
            }

        });
        projectEditor.addMomProjectListener(new MomProjectListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                onModified();
            }
        });
        toolbar.add(new JButton("Hello"));
        toolbar.add(new JButton("Bye"));
        reloadFile();
    }

    private void onModified() {
        if (getLookup().lookup(MySavable.class) == null) {
            ic.add(new MySavable());
        }
    }

    private void saveFile() {
        File file = FileUtil.toFile(obj.getPrimaryFile());
        try {
            projectEditor.saveStruct(file);
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void reloadFile() {
        File file = FileUtil.toFile(obj.getPrimaryFile());
        if (file.isFile()) {
            try {
                projectEditor.load(file);
            } catch (Exception ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        onModified();
    }

    @Override
    public String getName() {
        return "HWSProjectVisualElement";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public JComponent getVisualRepresentation() {
        return this;
    }

    @Override
    public JComponent getToolbarRepresentation() {
        return toolbar;
    }

    @Override
    public Action[] getActions() {
        Action[] retValue;
        // the multiviewObserver was passed to the primitiveElement3D in setMultiViewCallback() method.
        if (callback != null) {
            retValue = callback.createDefaultActions();
            // add you own custom actions here..
        } else {
            // fallback..
            retValue = new Action[0];
        }
        return retValue;
    }

    @Override
    public Lookup getLookup() {
        return obj.getLookup();
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
        System.out.println("");
    }

    @Override
    public void componentShowing() {
    }

    @Override
    public void componentHidden() {
    }

    @Override
    public void componentActivated() {
    }

    @Override
    public void componentDeactivated() {
    }

    @Override
    public UndoRedo getUndoRedo() {
        return UndoRedo.NONE;
    }

    @Override
    public void setMultiViewCallback(MultiViewElementCallback callback) {
        this.callback = callback;
    }

    @Override
    public CloseOperationState canCloseElement() {
        return CloseOperationState.STATE_OK;
    }

    private class MySavable extends AbstractSavable /*implements Icon*/ {

        MySavable() {
            register();
        }

        @Override
        protected String findDisplayName() {
            return String.valueOf(projectEditor.getProjectName());
        }

        @Override
        protected void handleSave() throws IOException {
            saveFile();
            tc().ic.remove(this);
            unregister();
        }

        HWSProjectVisualElement tc() {
            return HWSProjectVisualElement.this;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MySavable) {
                MySavable m = (MySavable) obj;
                return tc() == m.tc();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return tc().hashCode();
        }
//        @Override
//        public void paintIcon(Component c, Graphics g, int x, int y) {
//            ICON.paintIcon(c, g, x, y);
//        }
//        @Override
//        public int getIconWidth() {
//            return ICON.getIconWidth();
//        }
//        @Override
//        public int getIconHeight() {
//            return ICON.getIconHeight();
//        }
    }

}
