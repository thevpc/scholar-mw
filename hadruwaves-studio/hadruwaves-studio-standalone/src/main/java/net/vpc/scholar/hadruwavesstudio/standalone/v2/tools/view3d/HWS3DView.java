package net.vpc.scholar.hadruwavesstudio.standalone.v2.tools.view3d;

import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.scholar.hadruwaves.project.scene.HWProjectScenePlot;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;

import javax.swing.*;
import java.awt.*;
import net.vpc.scholar.hadruplot.libraries.calc3d.core.Calc3dTool;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;
import net.vpc.scholar.hadruwaves.project.scene.PropPreferences;

public class HWS3DView extends JPanel {

    private HadruwavesStudio studio;
    private HWProjectScenePlot scenePlot;
    private PropertyListener sceneChangeListener = new PropertyListener() {
        @Override
        public void propertyUpdated(PropertyEvent event) {
            updateScene();
        }
    };

    public HWS3DView(HadruwavesStudio studio) {
        super(new BorderLayout());
        this.studio = studio;
        this.studio.proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().name().equals("activeProject")) {
                    HWProject oldValue = event.getOldValue();
                    if (oldValue != null) {
                        oldValue.listeners().remove(sceneChangeListener);
                    }
                    HWProject newValue = event.getNewValue();
                    if (newValue != null) {
                        newValue.listeners().add(sceneChangeListener);
                    }
                    updateScene();
                }
            }
        });
        add(scenePlot = new HWProjectScenePlot(null, null));
        studio.proc().selectedConfiguration().listeners().add(e -> updateScene());
    }

//    public void removeSelection(HWProject p) {
//        HWProjectScene s = p.scene().get();
//        if (s != null) {
//            for (HWProjectElement c : s.components()) {
//                updateSelection(c, false);
//            }
//        }
//    }
//
//    public void updateSelection(HWProjectElement e, boolean selected) {
//        e.selected().set(selected);
//        if (e instanceof HWProjectElementGroup) {
//            for (HWProjectElement ee : ((HWProjectElementGroup) e).children().values()) {
//                updateSelection(ee, selected);
//            }
//        }
//    }
    public void updateScene() {
        HWConfigurationRun c = this.studio.proc().selectedConfiguration().get();
        if (c != null) {
//            removeSelection(p);
//            for (Object selectedItemValue : studio.solutionExplorer().getSelectedItemValues()) {
//                if (selectedItemValue instanceof HWProjectElement) {
//                    updateSelection((HWProjectElement) selectedItemValue, true);
//                }
//            }
            try {
                scenePlot.setScene(c.project().get().scene().get(), c);
            } catch (Exception ex) {
                studio.app().errors().add(ex);
            }
        } else {
            scenePlot.setScene(null, null);
        }
    }

    public HWProjectScenePlot getScenePlot() {
        return scenePlot;
    }

    public PropPreferences getPreferences() {
        return scenePlot.getPreferences();
    }

    public Calc3dTool getChartPanel() {
        return scenePlot.getChartPanel();
    }

}
