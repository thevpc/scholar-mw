package net.vpc.scholar.hadruwaves.studio.standalone.editors.materials;

import java.awt.BorderLayout;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.vpc.scholar.hadruwaves.studio.standalone.ECExpressionField;
import net.vpc.scholar.hadruwaves.studio.standalone.MomUIFactory;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.PlanarSourceMaterial;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;

public class PlanarSourceMaterialEditor extends JPanel implements AreaMaterialEditor {

    private ECExpressionField caracteristicImpedance;
    private ECExpressionField xValue;
    private ECExpressionField yValue;
    private ECComboBox polarization;
    private MomProjectEditor application;

    public PlanarSourceMaterialEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application = application;
        caracteristicImpedance = new ECExpressionField("value", false);
        caracteristicImpedance.getHelper().setDescription(application.getResources().get("caracteristicImpedance"));
        caracteristicImpedance.getHelper().setObject("50");
        xValue = new ECExpressionField("xValue", false);
        xValue.getHelper().setDescription(application.getResources().get("xValue"));
        xValue.getHelper().setObject("1");
        yValue = new ECExpressionField("yValue", false);
        yValue.getHelper().setDescription(application.getResources().get("yValue"));
        yValue.getHelper().setObject("1");
        polarization = (ECComboBox) DataTypeEditorFactory.forEnum("axis", Axis.class, true, application.getResources());

//                new ECComboBox("type", new ListType(new Axis[]{null,Axis.X,Axis.Y}, new SimpleDataType(Axis.class)));
        polarization.getHelper().setDescription(application.getResources().get("Polarization"));
        ECGroupPanel ecGroupPanel2 = new ECGroupPanel();
        ecGroupPanel2.add(new DataTypeEditor[]{caracteristicImpedance, xValue, yValue, polarization}, 2);
        this.add(ecGroupPanel2);
    }

    public JComponent getComponent() {
        return this;
    }

    public Color getDefaultColor() {
        return Color.YELLOW;
    }

    public MomUIFactory create() {
        return new PlanarSourceMaterialEditor(application);
    }

    public String getId() {
        String s = "PlanarSourceMaterialEditor";
        return s.substring(0, s.length() - "Editor".length());
    }

    public AreaMaterial getAreaMaterial() {
        PlanarSourceMaterial m = new PlanarSourceMaterial();
        m.setCaracteristicImpedanceExpression((String) caracteristicImpedance.getHelper().getObject());
        m.setXvalueExpression((String) xValue.getHelper().getObject());
        m.setYvalueExpression((String) yValue.getHelper().getObject());
        m.setPolarization((Axis) polarization.getHelper().getObject());
        return m;
    }

    public void setAreaMaterial(AreaMaterial material) {
        PlanarSourceMaterial ss = (PlanarSourceMaterial) material;
        if (ss == null) {
            caracteristicImpedance.getHelper().setObject("0");
            xValue.getHelper().setObject("1");
            yValue.getHelper().setObject("1");
            polarization.getHelper().setObject(null);
        } else {
            caracteristicImpedance.getHelper().setObject(ss.getCaracteristicImpedanceExpression());
            xValue.getHelper().setObject(ss.getXvalueExpression());
            yValue.getHelper().setObject(ss.getYvalueExpression());
            polarization.getHelper().setObject(ss.getPolarization());
        }
    }
}
