package net.vpc.scholar.hadruwaves.studio.standalone.v1.actions;

import java.awt.event.ActionEvent;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;


/**
 * User: taha
 * Date: 2 aout 2003
 * Time: 14:16:56
 */
public class StructureStopAllAction extends TmwlabAction {
    private MomProjectEditor editor;

    public StructureStopAllAction(MomProjectEditor editor, String key) {
        super(editor.getApplicationRenderer(),key, editor.getResources());
        this.editor = editor;
    }

    public MomProjectEditor getEditor() {
        return editor;
    }

    public void applicationActionPerformed(ActionEvent e) {
        editor.stopAllOpeartions();
    }

}
