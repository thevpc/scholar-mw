package net.vpc.scholar.hadruwaves.studio.standalone.v1;

import java.util.Arrays;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECComboBox;
import net.vpc.lib.pheromone.application.swing.ECTextArea;
import net.vpc.lib.pheromone.application.swing.ECTextField;
import net.vpc.lib.pheromone.application.swing.EditComponentsPanel;
import net.vpc.lib.pheromone.ariana.types.SimpleDataType;
import net.vpc.scholar.hadruwaves.mom.project.common.VarUnit;
import net.vpc.scholar.hadruwaves.mom.project.common.VariableExpression;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;
import net.vpc.upa.types.ListType;

/**
 * Created by IntelliJ IDEA. User: taha Date: 8 juin 2004 Time: 01:14:26 To
 * change this template use File | Settings | File Templates.
 */
public class VariableExpressionEditor extends EditComponentsPanel {

    private ECTextField nameField;
    private ECComboBox typeField;
    private ECExpressionField expressionField;
    private ECTextArea descField;
//    private TMWLabApplication application;

    public VariableExpressionEditor(MomProjectEditor structureEditor) {
        super();
//        this.application=application;
        nameField = new ECTextField("name", false);
        nameField.getHelper().setDescription(structureEditor.getResources().get("label"));

        expressionField = new ECExpressionField("value", false);
        expressionField.getHelper().setDescription("Expression");

        typeField = new ECComboBox("type", new ListType("", (java.util.List) Arrays.asList(VarUnit.values()), new SimpleDataType(VarUnit.class)));
        typeField.getHelper().setDescription("Unit");
        typeField.getHelper().setObject(VarUnit.NUMBER);

        descField = new ECTextArea("desc", true);
        descField.getHelper().setDescription("Description");

        setup(new DataTypeEditor[]{nameField, expressionField, typeField, descField}, 1, false);
    }

    public String getVarName() {
        return this.nameField.getString();
    }

    public VariableExpression getVariableExpression() {
        return new VariableExpression(this.nameField.getString(),
                this.expressionField.getString(),
                (VarUnit) this.typeField.getHelper().getObject(),
                this.descField.getString());

    }

    public void setVariableExpression(VariableExpression variableExpression) {
        setVarName(variableExpression.getName());
        setVarExpression(variableExpression.getExpression());
        setVarDesc(variableExpression.getDesc());
        setVarType(variableExpression.getUnit());
    }

    public String getVarExpression() {
        return this.expressionField.getString();
    }

    public String getVarDesc() {
        return this.descField.getString();
    }

    public void setVarName(String name) {
        nameField.getHelper().setObject(name);
    }

    public void setVarExpression(String expr) {
        expressionField.getHelper().setObject(expr);
    }

    public void setVarDesc(String desc) {
        descField.getHelper().setObject(desc);
    }

    public void setVarType(VarUnit unit) {
        typeField.getHelper().setObject(unit);
    }
}
