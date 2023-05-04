package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.materials;

import java.awt.BorderLayout;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.ECExpressionField;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.MomUIFactory;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.thevpc.scholar.hadruwaves.mom.project.areamaterial.ModalSourceMaterial;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

public class ModalSourceMaterialEditor extends JPanel implements AreaMaterialEditor {
    private ECExpressionField sourceCount;
    private ECExpressionField namedModes;
    private MomProjectEditor application;
//    private ECComboBox polarization;

    public ModalSourceMaterialEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application=application;
        sourceCount = new ECExpressionField("sourceCount", false);
        sourceCount.getHelper().setDescription(application.getResources().get("sourceCount"));
        sourceCount.getHelper().setObject("1");
        namedModes = new ECExpressionField("namedModes", true);
        namedModes.getHelper().setDescription(application.getResources().get("namedModes"));
        namedModes.getHelper().setObject("");
        //polarization=new ECComboBox("type", new ListType(new Axis[]{null,Axis.X,Axis.Y}, Axis.class));
        //polarization.setDescription(Application.getResources().get("Polarization"));
        ECGroupPanel ecGroupPanel2 = new ECGroupPanel();
        ecGroupPanel2.add(new DataTypeEditor[]{sourceCount,namedModes/*,polarization*/}, 1);
        this.add(ecGroupPanel2);
    }

    public JComponent getComponent() {
        return this;
    }

    public Color getDefaultColor() {
        return Color.ORANGE;
    }

    public MomUIFactory create() {
        return new ModalSourceMaterialEditor(application);
    }

    public String getId() {
        String s = "ModalSourceMaterialEditor";
        return s.substring(0, s.length() - "Editor".length());
    }

    public AreaMaterial getAreaMaterial() {
        ModalSourceMaterial m= new ModalSourceMaterial();
        m.setSourceCountExpression((String)sourceCount.getHelper().getObject());
        m.setNamedModesExpression((String)namedModes.getHelper().getObject());
        //m.setPolarization((Axis)polarization.getObject());
        return m;
    }

    public void setAreaMaterial(AreaMaterial material) {
         ModalSourceMaterial ss=(ModalSourceMaterial)material;
        if(ss==null){
            sourceCount.getHelper().setObject("1");
            namedModes.getHelper().setObject("");
            //polarization.setObject(null);
        }else{
            sourceCount.getHelper().setObject(ss.getSourcesCount());
            namedModes.getHelper().setObject(ss.getNamedModesExpression());
            //polarization.setObject(ss.getPolarization());
        }
   }
}
