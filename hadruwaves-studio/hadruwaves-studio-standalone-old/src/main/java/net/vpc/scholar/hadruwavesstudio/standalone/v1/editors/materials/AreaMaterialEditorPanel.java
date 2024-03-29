/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.SimpleMomUIEditor;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

/**
 *
 * @author vpc
 */
public class AreaMaterialEditorPanel extends SimpleMomUIEditor{

    public AreaMaterialEditorPanel(MomProjectEditor editor) {
        super(editor,false,editor.getAllByType(AreaMaterialEditor.class),"Material");
    }
    
    public AreaMaterial getAreaMaterial() {
        AreaMaterialEditor e=(AreaMaterialEditor)getSelectedMomUIFactory();
        if(e==null){
            return null;
        }
        return e.getAreaMaterial();
    }
    

    public void setAreaMaterial(AreaMaterial material) {
        setSelectedType(material==null?null:material.getId());
        AreaMaterialEditor e=(AreaMaterialEditor)getSelectedMomUIFactory();
        if(e!=null){
           e.setAreaMaterial(material); 
        }
    }
}
