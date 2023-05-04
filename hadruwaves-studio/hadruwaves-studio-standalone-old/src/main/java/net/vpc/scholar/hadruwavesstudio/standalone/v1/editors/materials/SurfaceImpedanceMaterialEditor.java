package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials;

import java.awt.BorderLayout;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.MomUIFactory;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.ECExpressionField;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.SurfaceImpedanceMaterial;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 10:44:40
 * 
 */
public class SurfaceImpedanceMaterialEditor extends JPanel implements AreaMaterialEditor{

    private ECExpressionField value;
    private MomProjectEditor application;

    public SurfaceImpedanceMaterialEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application=application;
        value = new ECExpressionField("value", false);
        value.getHelper().setDescription(application.getResources().get("value"));
        value.getHelper().setObject("0");
        ECGroupPanel ecGroupPanel2 = new ECGroupPanel();
        ecGroupPanel2.add(new DataTypeEditor[]{value}, 2);
        this.add(ecGroupPanel2);
    }

    public Color getDefaultColor() {
        return Color.BLUE;
    }
    
    public JComponent getComponent() {
        return this;
    }

    public MomUIFactory create() {
        return new SurfaceImpedanceMaterialEditor(application);
    }

    public AreaMaterial getAreaMaterial() {
        SurfaceImpedanceMaterial s=new SurfaceImpedanceMaterial();
        s.setValueExpression(value.getString());
        return s;
    }
    

    public void setAreaMaterial(AreaMaterial material) {
        SurfaceImpedanceMaterial ss=(SurfaceImpedanceMaterial)material;
        if(ss==null){
            value.getHelper().setObject("0");
        }else{
            value.getHelper().setObject(ss.getValueExpression());
        }
    }

    public String getId() {
        String s="SurfaceImpedanceMaterialEditor";
        return s.substring(0, s.length()-"Editor".length());
    }
    
}
