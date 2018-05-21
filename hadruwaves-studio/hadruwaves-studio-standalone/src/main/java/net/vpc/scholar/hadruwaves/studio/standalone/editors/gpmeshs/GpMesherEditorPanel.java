/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.standalone.editors.gpmeshs;

import net.vpc.scholar.hadruwaves.studio.standalone.editors.SimpleMomUIEditor;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;

/**
 *
 * @author vpc
 */
public class GpMesherEditorPanel extends SimpleMomUIEditor{

    public GpMesherEditorPanel(MomProjectEditor application) {
        super(application,true,application.getAllByType(GpMesherEditor.class),"TestFonctions");
    }
    
    public GpMesher getGpMesher() {
        GpMesherEditor e=(GpMesherEditor)getSelectedMomUIFactory();
        if(e==null){
            return null;
        }
        return e.getGpMesher();
    }

    public void setGpMesher(GpMesher mesher) {
        setSelectedType(mesher==null?null:mesher.getId());
        GpMesherEditor e=(GpMesherEditor)getSelectedMomUIFactory();
        if(e!=null){
           e.setGpMesher(mesher); 
        }
    }
}
