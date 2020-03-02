package net.vpc.scholar.hadruwaves.project.scene.plot;

import net.vpc.scholar.hadrumaths.Domain;
import net.vpc.scholar.hadruplot.backends.calc3d.core.Calc3dTool;
import net.vpc.scholar.hadruplot.backends.calc3d.core.Preferences;
import net.vpc.scholar.hadruplot.backends.calc3d.elements.Element3D;
import net.vpc.scholar.hadruplot.backends.calc3d.geometry3d.Box3D;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.scene.HWScene;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StructureGraphScenePlot extends JPanel {
    private Calc3dTool chartPanel = new Calc3dTool();
    private HWScene scene;
    private HWProjectEnv env;

    public StructureGraphScenePlot(HWScene scene, HWProjectEnv env) {
        super(new BorderLayout());
        this.chartPanel.getSceneManager().setBoxVisible(true);
        this.chartPanel.getSceneManager().setGridXYVisible(true);
        this.chartPanel.getSceneManager().setAxisVisible(true);
        this.chartPanel.refreshCanvas();
        add(this.chartPanel.getCanvas(), BorderLayout.CENTER);
        setScene(scene,env);
    }

    public void setScene(HWScene scene,HWProjectEnv env) {
        this.scene = scene;
        this.env = env;
        if (scene == null) {
            this.chartPanel.newDocument();
        } else {
            Domain d = scene.domain().eval(env);
            Preferences preferences = this.chartPanel.getSettings().getPreferences();
            preferences.setClipBox(new Box3D(
                    d.xmin(),
                    d.xmax(),
                    d.ymin(),
                    d.ymax(),
                    d.zmin(),
                    d.zmax()
            ));
            this.chartPanel.applySettings(preferences, true);
            List<Element3D> element3DS = scene.toElements3D(env);
            if (element3DS != null) {
                for (Element3D element3D : element3DS) {
                    if (element3D != null) {
                        this.chartPanel.getSceneManager().addElement(element3D);
                    }
                }
            }
            this.chartPanel.refreshCanvas(false);
        }
    }

}
