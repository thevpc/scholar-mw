package net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import javax.swing.JTabbedPane;
import net.vpc.lib.pheromone.application.swing.DataTypeEditor;
import net.vpc.lib.pheromone.application.swing.ECGroupPanel;
import net.vpc.lib.pheromone.application.swing.ECTextField;
import net.vpc.lib.pheromone.application.swing.Swings;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.gpmeshs.GpMesherEditorPanel;
import net.thevpc.scholar.hadruwaves.mom.project.common.AreaGroup;
import net.vpc.upa.types.ConstraintsException;


/**
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 7 juil. 2003
 * Time: 10:44:40
 * 
 */
public class AreaGroupEditor extends JPanel {

    private ECTextField areaTitle;
    private ECGroupPanel ecGroupPanel;
    private GpMesherEditorPanel gpMesher;
//    private MomProjectEditor structureEditor;

    public AreaGroupEditor(MomProjectEditor structureEditor) {
        super(new BorderLayout());
//        this.structureEditor = structureEditor;
        gpMesher=new GpMesherEditorPanel(structureEditor);
        areaTitle = new ECTextField("title", 16, 1, 64, false);
        areaTitle.getHelper().setDescription(structureEditor.getResources().get("label"));
        areaTitle.getHelper().setObject("Group");
        
        ecGroupPanel = new ECGroupPanel();
        ecGroupPanel.add(new DataTypeEditor[]{areaTitle}, 1);

        this.add(ecGroupPanel,BorderLayout.PAGE_START);
        JTabbedPane p=new JTabbedPane();
        p.addTab("Test Functions", gpMesher);
        this.add(p,BorderLayout.CENTER);
        revalidateVisibleEditors(null);
        
    }
    JComponent currentPlot;

    public void clearTitle() {
        areaTitle.getHelper().setObject("", false);
    }

    public void revalidateVisibleEditors(AreaGroup area) {
        Swings.pack(this);
    }

    public AreaGroup getAreaGroup(AreaGroup toUpdate) throws ConstraintsException, NumberFormatException {
        String title = (String) areaTitle.getHelper().getObject();

        toUpdate.setName(title);
        toUpdate.setGpMesher(gpMesher.getGpMesher());
        return toUpdate;
    }

    public void setAreaGroup(AreaGroup area) {
        areaTitle.getHelper().setObject(area.getName());
        gpMesher.setGpMesher(area.getGpMesher());
    }
}
