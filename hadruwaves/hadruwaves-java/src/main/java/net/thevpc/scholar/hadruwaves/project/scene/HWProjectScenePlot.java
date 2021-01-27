package net.thevpc.scholar.hadruwaves.project.scene;

import net.thevpc.scholar.hadrumaths.Domain;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Calc3dTool;
import net.thevpc.scholar.hadruplot.libraries.calc3d.core.Preferences;
import net.thevpc.scholar.hadruplot.libraries.calc3d.elements.Element3D;
import net.thevpc.scholar.hadruplot.libraries.calc3d.geometry3d.Box3D;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.scholar.hadruwaves.project.configuration.HWConfigurationRun;

public class HWProjectScenePlot extends JPanel {

    private Calc3dTool chartPanel = new Calc3dTool();
    private HWProjectScene scene;
    private HWConfigurationRun configuration;
    private PropPreferences preferences = new PropPreferences();

    public HWProjectScenePlot(HWProjectScene scene, HWConfigurationRun configuration) {
        super(new BorderLayout());
        this.chartPanel.getSceneManager().setBoxVisible(true);
        this.chartPanel.getSceneManager().setGridXYVisible(true);
        this.chartPanel.getSceneManager().setAxisVisible(true);
        Preferences p = this.chartPanel.getPreferences();
        p.setBoxVisible(true);
        p.setGridXYVisible(true);
        p.setAxisVisible(true);
        p.setBackColor(new GradientPaint(new Point.Double(0, 0), Color.darkGray, new Point.Double(0, 600), Color.darkGray.darker()));
        this.chartPanel.applySettings(p, true);
        this.chartPanel.refreshCanvas();
        add(this.chartPanel.getCanvas(), BorderLayout.CENTER);
        setScene(scene, configuration);
        preferences.setAll(chartPanel.getPreferences());
        preferences.listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                chartPanel.applySettings(preferences.getAll(), true);
            }
        });
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("lookAndFeel".equals(evt.getPropertyName())) {
                    Color c1 = UIManager.getColor("Panel.background");
                    if (c1 == null) {
                        c1 = Color.darkGray;
                    }
                    Color c2 = UIManager.getColor("TextPane.disabledBackground");
                    if (c2 == null) {
                        c2 = Color.lightGray;
                    }
                    preferences.backColor().set(new GradientPaint(new Point.Double(0, 0), c1, new Point.Double(0, 600), c2));
//                    LookAndFeel plaf = UIManager.getLookAndFeel();
//                    switch (plaf.getID()) {
//                        case "Darcula": {
//                            preferences.backColor().set(new GradientPaint(new Point.Double(0, 0), Color.darkGray, new Point.Double(0, 600), Color.darkGray.darker()));
//                            break;
//                        }
//                        case "Motif": {
//                            preferences.backColor().set(new GradientPaint(new Point.Double(0, 0), new Color(174, 178, 195), new Point.Double(0, 600), new Color(147, 151, 165)));
//                            break;
//                        }
//                        case "Nimbus": {
//                            preferences.backColor().set(new GradientPaint(new Point.Double(0, 0), new Color(124, 139, 153), new Point.Double(0, 600), new Color(250, 250, 250)));
//                            break;
//                        }
//                        case "Metal": {
//                            preferences.backColor().set(new GradientPaint(new Point.Double(0, 0), new Color(255, 255, 255), new Point.Double(0, 600), new Color(196, 216, 236)));
//                            break;
//                        }
//                        default: {
//                            preferences.backColor().set(new GradientPaint(new Point.Double(0, 0), new Color(240, 240, 240), new Point.Double(0, 600), new Color(190, 190, 190)));
//                            break;
//                        }
//                    }
                }
            }
        });
    }

    public PropPreferences getPreferences() {
        return preferences;
    }

    public Calc3dTool getChartPanel() {
        return chartPanel;
    }

    public void refresh(HWConfigurationRun configuration) {
        this.configuration = configuration;
        if (scene == null) {
            this.chartPanel.newDocument();
        } else {
            Domain d = scene.getBoundingDomain(configuration, 0.1f);
            this.chartPanel.getSceneManager().removeAll();
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
            List<Element3D> element3DS = scene.toElements3D(configuration);
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

    public void setScene(HWProjectScene scene, HWConfigurationRun configuration) {
        this.scene = scene;
        refresh(configuration);
    }

}
