package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.areashapes;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.MomUIFactory;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.ECExpressionField;
import net.vpc.scholar.hadruwaves.mom.project.shapes.AreaShape;
import net.vpc.scholar.hadruwaves.mom.project.shapes.RectAreaShape;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 10:44:40
 * 
 */
public class RectAreaShapeEditor extends JPanel implements AreaShapeEditor{

    private ECExpressionField areaX;
    private ECExpressionField areaY;
    private ECExpressionField areaWidth;
    private ECExpressionField areaHeight;
    private MomProjectEditor application;

    public RectAreaShapeEditor(MomProjectEditor application) {
        super(new BorderLayout());
        this.application=application;
        areaX = new ECExpressionField("x", false);
        areaX.getHelper().setDescription(application.getResources().get("x"));
        areaY = new ECExpressionField("y", false);
        areaY.getHelper().setDescription(application.getResources().get("y"));
        areaWidth = new ECExpressionField("width", false);
        areaWidth.getHelper().setDescription(application.getResources().get("width"));
        areaHeight = new ECExpressionField("height", false);
        areaHeight.getHelper().setDescription(application.getResources().get("height"));
        areaX.getHelper().setObject("0");
        areaY.getHelper().setObject("0");
        areaWidth.getHelper().setObject("1");
        areaHeight.getHelper().setObject("1");
        ECGroupPanel ecGroupPanel2 = new ECGroupPanel();
        ecGroupPanel2.add(new DataTypeEditor[]{areaX, areaWidth, areaY, areaHeight}, 2).setBorder(BorderFactory.createTitledBorder(application.getResources().get("Dimension.title")));
        this.add(ecGroupPanel2);
    }

    public JComponent getComponent() {
        return this;
    }

    public MomUIFactory create() {
        return new RectAreaShapeEditor(application);
    }

    public String getId() {
        String s="RectAreaShapeEditor";
        return s.substring(0, s.length()-"Editor".length());
    }

    public AreaShape getAreaShape() {
        RectAreaShape s=new RectAreaShape();
        s.setXExpression(areaX.getString());
        s.setYExpression(areaY.getString());
        s.setWidthExpression(areaWidth.getString());
        s.setHeightExpression(areaHeight.getString());
        return s;
    }
    

    public void setAreaShape(AreaShape shape) {
        RectAreaShape ss=(RectAreaShape)shape;
        if(ss==null){
            areaX.getHelper().setObject("0");
            areaY.getHelper().setObject("0");
            areaWidth.getHelper().setObject("0");
            areaHeight.getHelper().setObject("0");
        }else{
            areaX.getHelper().setObject(ss.getXExpression());
            areaY.getHelper().setObject(ss.getYExpression());
            areaWidth.getHelper().setObject(ss.getWidthExpression());
            areaHeight.getHelper().setObject(ss.getHeightExpression());
        }
    }

    
}
