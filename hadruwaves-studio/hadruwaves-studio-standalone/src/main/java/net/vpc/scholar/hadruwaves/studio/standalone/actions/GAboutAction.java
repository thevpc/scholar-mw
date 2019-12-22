/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwaves.studio.standalone.actions;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import net.vpc.common.swings.JImageBackgroundPanel;
import net.vpc.lib.pheromone.application.ApplicationRenderer;
import net.vpc.lib.pheromone.application.actions.ApplicationActionManager;
import net.vpc.lib.pheromone.application.swing.Swings;

/**
 *
 * @author vpc
 */
public class GAboutAction extends TmwlabAction {

    public GAboutAction(ApplicationRenderer application) {
        super(application, ApplicationActionManager.ABOUT_ACTION, "/images/net/vpc/application/AboutAction.gif");
    }

    public void applicationActionPerformed(ActionEvent ae) {
        JImageBackgroundPanel splashPanel = new JImageBackgroundPanel(new BorderLayout());
        splashPanel.setBackgroundImageURL("/net/vpc/research/tmwlab/resources/images/splashscreen.gif", JImageBackgroundPanel.IMAGE_SIZE);
        Window w = Swings.createSplashScreen(splashPanel, true, 30000);
        w.setVisible(true);
    }
}
