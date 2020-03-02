package net.vpc.scholar.hadruwaves.studio.standalone.v1.editors;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;

import javax.swing.JTabbedPane;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECCheckBox;
import net.vpc.lib.pheromone.application.swing.ECColorChooser;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.lib.pheromone.application.swing.ECTextField;
import net.vpc.lib.pheromone.application.swing.Swings;

import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.areashapes.AreaShapeEditorPanel;
import net.vpc.scholar.hadruwaves.studio.standalone.v1.editors.materials.AreaMaterialEditorPanel;
import net.vpc.scholar.hadruwaves.mom.project.areamaterial.AreaMaterial;
import net.vpc.scholar.hadruwaves.mom.project.common.Area;
import net.vpc.upa.types.ConstraintsException;

/**
 * Created by IntelliJ IDEA. User: taha Date: 7 juil. 2003 Time: 10:44:40 To
 * change this template use Options | File Templates.
 */
public class AreaEditor extends JPanel {

    private ECTextField areaTitle;
    private ECCheckBox areaEnabled;
    private ECColorChooser ecColorChooser;
    private ECGroupPanel ecGroupPanel;
    private AreaShapeEditorPanel areaShape;
    private AreaMaterialEditorPanel areaMaterial;
    private MomProjectEditor editor;
    private AreaMaterial defaultMaterial;

    public AreaEditor(MomProjectEditor editor, AreaMaterial defaultMaterial) {
        super(new BorderLayout());
        this.defaultMaterial = defaultMaterial;
        this.editor = editor;
//        TMWLabApplication application = editor.getApplication();
        areaShape = new AreaShapeEditorPanel(editor);
        areaMaterial = new AreaMaterialEditorPanel(editor);
        areaTitle = new ECTextField("title", 16, 1, 64, false);
        areaTitle.getHelper().setDescription(editor.getResources().get("label"));
        areaTitle.getHelper().setObject(defaultMaterial.getName());

        areaEnabled = new ECCheckBox("enabled");
        areaEnabled.getHelper().setDescription(editor.getResources().get("enabled"));
        areaEnabled.getHelper().setObject(Boolean.TRUE);

        ecColorChooser = new ECColorChooser("color", null, true);
        ecColorChooser.setDescription(editor.getResources().get("color"));
        ecGroupPanel = new ECGroupPanel();
        ecGroupPanel.add(new DataTypeEditor[]{areaTitle}, 1);
        ecGroupPanel.add(new DataTypeEditor[]{areaEnabled, ecColorChooser}, 3);

        this.add(ecGroupPanel, BorderLayout.PAGE_START);
        JTabbedPane p = new JTabbedPane();
        p.addTab("Shape", areaShape);
        p.addTab("Material", areaMaterial);
        areaMaterial.setAreaMaterial(defaultMaterial);
        this.add(p, BorderLayout.CENTER);
        revalidateVisibleEditors(null);

    }
    JComponent currentPlot;

    public void clearTitle() {
        areaTitle.getHelper().setObject("", false);
    }

    public void revalidateVisibleEditors(Area area) {
        Swings.pack(this);
    }

    public Area getArea() throws ConstraintsException, NumberFormatException {
        String title = (String) areaTitle.getHelper().getObject();

        boolean enabled = ((Boolean) areaEnabled.getHelper().getObject(false));

        Area area = new Area();
        area.setColor((Color) ecColorChooser.getObject());
        area.setEnabled(enabled);
        area.setName(title);
        area.setMaterial(areaMaterial.getAreaMaterial());
        area.setShape(areaShape.getAreaShape());
        return area;
    }

    public void setArea(Area area) {
        areaTitle.getHelper().setObject(area.getName());
        areaEnabled.getHelper().setObject(area.isEnabled());
        ecColorChooser.getHelper().setObject(area.getColor());
        areaMaterial.setAreaMaterial(area.getMaterial());
        areaShape.setAreaShape(area.getShape());
        revalidateVisibleEditors(area);
//        path = area.getParentGroup().getPath();
//        currentAreaPropertiesEditor.setArea(area);

    }
}
