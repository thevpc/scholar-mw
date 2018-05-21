package net.vpc.scholar.hadruwaves.studio.standalone.editors.gpmeshs;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.vpc.scholar.hadruwaves.studio.standalone.MomUIFactory;
import net.vpc.scholar.hadruwaves.studio.standalone.ECExpressionField;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.ECCheckBox;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.scholar.hadrumaths.Axis;
import net.vpc.scholar.hadrumaths.symbolic.RooftopType;
import net.vpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;
import net.vpc.scholar.hadruwaves.mom.project.gpmesher.RooftopGpMesher;
import net.vpc.scholar.hadruwaves.studio.standalone.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 10:44:40
 * To change this template use Options | File Templates.
 */
public class RooftopGpMesherEditor extends JPanel implements GpMesherEditor{

    private ECCheckBox cross;
    private ECExpressionField gridX;
    private ECExpressionField gridY;
    private ECComboBox type;
    private ECComboBox invarianceType;
    private ECComboBox symmetry;
    private MomProjectEditor application;

    public RooftopGpMesherEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application=application;
        gridX = new ECExpressionField("gridX", false);
        gridX.getHelper().setDescription(application.getResources().get("cellX"));
        gridX.getHelper().setObject("1");

//        type = new ECComboBox("type", new ListType(RooftopType.values(), new SimpleDataType(RooftopType.class, false)));
        type = (ECComboBox) DataTypeEditorFactory.forEnum("type", RooftopType.class, false,application.getResources());
        type.getHelper().setDescription(application.getResources().get("rooftop.type"));
        type.getHelper().setObject(RooftopType.FULL);

        gridY = new ECExpressionField("gridY", false);
        gridY.getHelper().setDescription(application.getResources().get("cellY"));
        gridY.getHelper().setObject("1");

        cross = new ECCheckBox("cross");
        cross.getHelper().setDescription(application.getResources().get("cross"));
        cross.getHelper().setObject(Boolean.TRUE);

//        invarianceType=new ECComboBox("invarianceType", new ListType(new Axis[]{null,Axis.X,Axis.Y}, new SimpleDataType(Axis.class)));
        invarianceType=(ECComboBox) DataTypeEditorFactory.forEnum("invarianceType", Axis.class, true,application.getResources());
        invarianceType.getHelper().setDescription(application.getResources().get("Invariance"));
        
//        symmetry=new ECComboBox("symmetry", new ListType(TestFunctionsSymmetry.values(), new SimpleDataType(TestFunctionsSymmetry.class)));
        symmetry=(ECComboBox) DataTypeEditorFactory.forEnum("testFunctionsSymmetry", TestFunctionsSymmetry.class, true,application.getResources());
        symmetry.getHelper().setDescription(application.getResources().get("Symmetry"));
        symmetry.getHelper().setObject(TestFunctionsSymmetry.NO_SYMMETRY);

        ECGroupPanel ecGroupPanel = new ECGroupPanel();
        ecGroupPanel.add(new DataTypeEditor[]{invarianceType,symmetry,type,gridX, gridY, cross}, 1)
                .setBorder(BorderFactory.createTitledBorder(application.getResources().get("Params")));
        add(ecGroupPanel);
    }

    public JComponent getComponent() {
        return this;
    }

    public MomUIFactory create() {
        return new RooftopGpMesherEditor(application);
    }

    public String getId() {
        String s="RooftopGpMesherEditor";
        return s.substring(0, s.length()-"Editor".length());
    }

    public GpMesher getGpMesher() {
        RooftopGpMesher r= new RooftopGpMesher();
        r.setGridXExpression(gridX.getString());
        r.setGridYExpression(gridY.getString());
        r.setCross(((Boolean) cross.getHelper().getObject()).booleanValue());
        r.setRooftopType((RooftopType) type.getHelper().getObject());
        r.setInvariance((Axis)invarianceType.getHelper().getObject());
        r.setSymmetry((TestFunctionsSymmetry)symmetry.getHelper().getObject());
        return r;
    }

    public void setGpMesher(GpMesher mesher) {
        RooftopGpMesher roofTopAttache = (RooftopGpMesher) mesher;
        type.getHelper().setObject(roofTopAttache.getRooftopType());
        gridX.getHelper().setObject(roofTopAttache.getGridXExpression());
        gridY.getHelper().setObject(roofTopAttache.getGridYExpression());
        cross.getHelper().setObject(roofTopAttache.isCross() ? Boolean.TRUE : Boolean.FALSE);
        symmetry.getHelper().setObject(roofTopAttache.getSymmetry());
        invarianceType.getHelper().setObject(roofTopAttache.getInvariance());
    }
}
