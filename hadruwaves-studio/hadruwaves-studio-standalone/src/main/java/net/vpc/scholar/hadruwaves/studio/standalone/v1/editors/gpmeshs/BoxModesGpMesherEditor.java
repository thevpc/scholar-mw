package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.gpmeshs;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.MomUIFactory;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.ECExpressionField;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.BoxModesGpMesher;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 10:44:40
 * 
 */
public class BoxModesGpMesherEditor extends JPanel implements GpMesherEditor{

    private ECExpressionField max;
    private ECComboBox invarianceType;
    private ECComboBox symmetry;
    private MomProjectEditor application;

    public BoxModesGpMesherEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application=application;
        max = new ECExpressionField("max", false);
        max.getHelper().setDescription(application.getResources().get("max"));
        max.getHelper().setObject("1");

        invarianceType=(ECComboBox) DataTypeEditorFactory.forEnum("axis", Axis.class, true, application.getResources());
        invarianceType.getHelper().setDescription(application.getResources().get("Invariance"));
        
        symmetry=(ECComboBox) DataTypeEditorFactory.forEnum("testFunctionsSymmetry", TestFunctionsSymmetry.class, true,application.getResources());
        symmetry.getHelper().setDescription(application.getResources().get("Symmetry"));
        symmetry.getHelper().setObject(TestFunctionsSymmetry.NO_SYMMETRY);

        ECGroupPanel ecGroupPanel = new ECGroupPanel();
        ecGroupPanel.add(new DataTypeEditor[]{invarianceType,symmetry,max}, 1)
                .setBorder(BorderFactory.createTitledBorder(application.getResources().get("Params")));
        add(ecGroupPanel);
    }

    public JComponent getComponent() {
        return this;
    }

    public MomUIFactory create() {
        return new BoxModesGpMesherEditor(application);
    }

    public GpMesher getGpMesher() {
        BoxModesGpMesher r= new BoxModesGpMesher();
        r.setMaxExpression(max.getString());
        r.setInvariance((Axis)invarianceType.getHelper().getObject());
        r.setSymmetry((TestFunctionsSymmetry)symmetry.getHelper().getObject());
        return r;
    }

    public void setGpMesher(GpMesher mesher) {
        BoxModesGpMesher imesh = (BoxModesGpMesher) mesher;
        max.getHelper().setObject(imesh.getMaxExpression());
        symmetry.getHelper().setObject(imesh.getSymmetry());
        invarianceType.getHelper().setObject(imesh.getInvariance());
    }
    
    public String getId() {
        String s="BoxModesGpMesherEditor";
        return s.substring(0, s.length()-"Editor".length());
    }
    
}
