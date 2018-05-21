package net.vpc.scholar.hadruwaves.studio.standalone.editors.materials;

import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import net.vpc.scholar.hadruwaves.studio.standalone.MomUIFactory;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.gpmeshs.GpMesherEditorPanel;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PecMaterial;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 10:44:40
 * To change this template use Options | File Templates.
 */
public class PecMaterialEditor extends JPanel implements AreaMaterialEditor{
    private GpMesherEditorPanel gpMesher;
    private MomProjectEditor application;

    public PecMaterialEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application=application;
        gpMesher=new GpMesherEditorPanel(application);
        add(gpMesher,BorderLayout.CENTER);
    }

    public Color getDefaultColor() {
        return Color.RED;
    }
    

    public JComponent getComponent() {
        return this;
    }

    public MomUIFactory create() {
        return new PecMaterialEditor(application);
    }

    public String getId() {
        String s="PecMaterialEditor";
        return s.substring(0, s.length()-"Editor".length());
    }

    public AreaMaterial getAreaMaterial() {
        PecMaterial p=new PecMaterial();
        p.setGpMesher(gpMesher.getGpMesher());
        return p;
    }
    

    public void setAreaMaterial(AreaMaterial material) {
        gpMesher.setGpMesher(((PecMaterial)material).getGpMesher());
    }

}
