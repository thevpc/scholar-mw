package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.gpmeshs;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import net.thevpc.scholar.hadruwavesstudio.standalone.v1.MomUIFactory;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.ECExpressionField;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.DataTypeEditorFactory;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.thevpc.scholar.hadrumaths.Axis;
import net.thevpc.scholar.hadruwaves.mom.TestFunctionsSymmetry;
import net.thevpc.scholar.hadruwaves.mom.project.gpmesher.ConstantGpMesher;
import net.thevpc.scholar.hadruwaves.mom.project.gpmesher.GpMesher;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA. User: taha Date: 7 juil. 2003 Time: 10:44:40 To
 * change this template use Options | File Templates.
 */
public class ConstantGpMesherEditor extends JPanel implements GpMesherEditor {

    private ECExpressionField value;
    private ECExpressionField gridX;
    private ECExpressionField gridY;
    private ECComboBox invarianceType;
    private ECComboBox symmetry;
    private MomProjectEditor application;

    public ConstantGpMesherEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application = application;
        gridX = new ECExpressionField("gridX", false);
        gridX.getHelper().setDescription(application.getResources().get("gridX"));
        gridX.getHelper().setObject("1");

        gridY = new ECExpressionField("gridY", false);
        gridY.getHelper().setDescription(application.getResources().get("gridY"));
        gridY.getHelper().setObject("1");

        value = new ECExpressionField("value", false);
        value.getHelper().setDescription(application.getResources().get("value"));
        value.getHelper().setObject("0");

        invarianceType = (ECComboBox) DataTypeEditorFactory.forEnum("axis", Axis.class, true, application.getResources());
        invarianceType.getHelper().setDescription(application.getResources().get("Invariance"));

        symmetry = (ECComboBox) DataTypeEditorFactory.forEnum("testFunctionsSymmetry", TestFunctionsSymmetry.class, true,application.getResources());
        symmetry.getHelper().setDescription(application.getResources().get("Symmetry"));
        symmetry.getHelper().setObject(TestFunctionsSymmetry.NO_SYMMETRY);

        ECGroupPanel ecGroupPanel = new ECGroupPanel();
        ecGroupPanel.add(new DataTypeEditor[]{invarianceType, symmetry, gridX, gridY, value}, 1)
                .setBorder(BorderFactory.createTitledBorder(application.getResources().get("Params")));
        add(ecGroupPanel);
    }

    public JComponent getComponent() {
        return this;
    }

    public MomUIFactory create() {
        return new ConstantGpMesherEditor(application);
    }

    public GpMesher getGpMesher() {
        ConstantGpMesher r = new ConstantGpMesher();
        r.setGridXExpression(gridX.getString());
        r.setGridYExpression(gridY.getString());
        r.setValueExpression(value.getString());
        r.setInvariance((Axis) invarianceType.getHelper().getObject());
        r.setSymmetry((TestFunctionsSymmetry) symmetry.getHelper().getObject());
        return r;
    }

    public void setGpMesher(GpMesher mesher) {
        ConstantGpMesher imesh = (ConstantGpMesher) mesher;
        value.getHelper().setObject(imesh.getValueExpression());
        gridX.getHelper().setObject(imesh.getGridXExpression());
        gridY.getHelper().setObject(imesh.getGridYExpression());
        symmetry.getHelper().setObject(imesh.getSymmetry());
        invarianceType.getHelper().setObject(imesh.getInvariance());
    }

    public String getId() {
        String s = "ConstantGpMesherEditor";
        return s.substring(0, s.length() - "Editor".length());
    }
}
