/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.areashapes;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.SimpleMomUIEditor;
import net.thevpc.scholar.hadruwaves.mom.project.shapes.AreaShape;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

/**
 *
 * @author vpc
 */
public class AreaShapeEditorPanel extends SimpleMomUIEditor {

    public AreaShapeEditorPanel(MomProjectEditor editor) {
        super(editor, false, editor.getAllByType(AreaShapeEditor.class), "Shape");
    }

    public AreaShape getAreaShape() {
        AreaShapeEditor e = (AreaShapeEditor) getSelectedMomUIFactory();
        if (e == null) {
            return null;
        }
        return e.getAreaShape();
    }

    public void setAreaShape(AreaShape shape) {
        setSelectedType(shape == null ? null : shape.getId());
        AreaShapeEditor e = (AreaShapeEditor) getSelectedMomUIFactory();
        if (e != null) {
            e.setAreaShape(shape);
        }
    }
}
