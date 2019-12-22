package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import java.awt.Component;
import javax.swing.*;

import net.vpc.common.strings.StringUtils;
import net.vpc.lib.pheromone.application.ApplicationRenderer;
import net.vpc.lib.pheromone.application.actions.ApplicationAction;
import net.vpc.lib.pheromone.application.swing.AdvancedAction;
import net.vpc.lib.pheromone.application.winman.AppWindow;
import net.vpc.lib.pheromone.ariana.util.Resources;
import net.vpc.scholar.hadruplot.*;

/**
 * Created with IntelliJ IDEA. User: vpc Date: 8/9/12 Time: 5:05 PM To change
 * this template use File | Settings | File Templates.
 */
public abstract class TmwlabAction extends ApplicationAction {

    public TmwlabAction(ApplicationRenderer application, String key, Resources resources) {
        super(application, key, resources);
    }

    public TmwlabAction(ApplicationRenderer application, String key, String iconPath, Resources resources) {
        super(application, key, iconPath, resources);
    }

    public TmwlabAction(ApplicationRenderer application, String key, String name, String iconPath, Resources resources) {
        super(application, key, name, iconPath, resources);
    }

    public TmwlabAction(ApplicationRenderer application, String key, String name, String desc, String securityKey, Icon icon, Resources resources) {
        super(application, key, name, desc, securityKey, icon, resources);
    }

    public TmwlabAction(ApplicationRenderer application, String key) {
        super(application, key);
    }

    public TmwlabAction(ApplicationRenderer application, String key, String iconPath) {
        super(application, key, iconPath);
    }

    public TmwlabAction(ApplicationRenderer application, String key, String name, String iconPath) {
        super(application, key, name, iconPath);
    }

    public TmwlabAction(ApplicationRenderer application, String key, String name, String desc, String securityKey, Icon icon) {
        super(application, key, name, desc, securityKey, icon);
    }

    public TmwlabAction(ApplicationRenderer application, AdvancedAction other) {
        super(application, other);
    }

    private PlotWindowManager getWindowManager(ApplicationRenderer applicationRenderer) {
        Component f = applicationRenderer.getMainComponent();
        PlotWindowManager pwm = null;
        if (f instanceof JComponent) {
            JComponent jf = (JComponent) f;
            pwm = (PlotWindowManager) jf.getClientProperty(PlotWindowManager.class.getName());
            if (pwm == null) {
                pwm = createPlotWindowManager();
                jf.putClientProperty(PlotWindowManager.class.getName(), pwm);
            }
        }
        return pwm;
    }

    public PlotBuilder plotter() {
        return Plot.builder().windowManager(getWindowManager(getApplicationRenderer()));
    }

    private PlotWindowManager createPlotWindowManager() {
        return new AbstractPlotWindowManager() {
            @Override
            public void removePlotComponentImpl(PlotComponent component) {
                JComponent comp = component.toComponent();
                String p = (String) comp.getClientProperty(PlotWindowManager.class.getName() + ":key");
                AppWindow window = getApplicationRenderer().getWindowManager().getWindow(p);
                if (window != null) {
                    window.dispose();
                }
            }

            @Override
            public void addPlotComponentImpl(PlotComponent component, String[] path) {
                JComponent comp = component.toComponent();
                String pathString = StringUtils.join("/", path);
                comp.putClientProperty(PlotWindowManager.class.getName() + ":key", pathString);
                getApplicationRenderer().getWindowManager().addWindow(pathString,
                        component.getPlotTitle(),
                        Resources.loadImageIcon("/net/vpc/research/tmwlab/resources/images/Graph.gif"), comp);
            }
        };
    }

}
