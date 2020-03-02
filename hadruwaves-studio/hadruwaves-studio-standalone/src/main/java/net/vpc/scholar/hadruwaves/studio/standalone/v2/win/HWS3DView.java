package net.vpc.scholar.hadruwaves.studio.standalone.v2.win;

import net.vpc.common.prpbind.PropertyEvent;
import net.vpc.common.prpbind.PropertyListener;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwaves.project.HWProjectEnv;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.scene.plot.StructureGraphScenePlot;
import net.vpc.scholar.hadruwaves.studio.standalone.v2.HadruwavesStudioV2;

import javax.swing.*;
import java.awt.*;

public class HWS3DView extends JPanel {
    private HadruwavesStudioV2 application;
    private StructureGraphScenePlot scenePlot;
    private PropertyListener sceneChangeListener = new PropertyListener() {
        @Override
        public void propertyUpdated(PropertyEvent event) {
            projectUpdated();
        }
    };

    public HWS3DView(HadruwavesStudioV2 application) {
        super(new BorderLayout());
        this.application = application;
        this.application.proc().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getProperty().getPropertyName().equals("activeProject")) {
                    HWProject oldValue = event.getOldValue();
                    if (oldValue != null) {
                        oldValue.listeners().remove(sceneChangeListener);
                    }
                    HWProject newValue = event.getNewValue();
                    if (newValue != null) {
                        newValue.listeners().add(sceneChangeListener);
                    }
                    projectUpdated();
                }
            }
        });
        add(scenePlot = new StructureGraphScenePlot(null, null));
    }

    private void projectUpdated() {
        HWSolution hwSolution = this.application.proc().solution().get();
        HWProject p = hwSolution == null ? null : hwSolution.activeProject().get();
        if (p != null) {
            scenePlot.setScene(p.scene().get(), new HWProjectEnv(p, p.configurations().activeConfiguration().get()));
        } else {
            scenePlot.setScene(null, new HWProjectEnv(p, p.configurations().activeConfiguration().get()));
        }
    }
}
