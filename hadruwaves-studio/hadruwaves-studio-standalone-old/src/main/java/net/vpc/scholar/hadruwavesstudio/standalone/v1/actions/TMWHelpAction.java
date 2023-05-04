/*
 * Created by IntelliJ IDEA.
 * User: taha
 * Date: 25 dec. 02
 * Time: 22:39:15
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;


import net.vpc.lib.pheromone.application.ApplicationRenderer;

import java.awt.event.ActionEvent;
import net.vpc.lib.pheromone.application.actions.ApplicationAction;
import net.vpc.lib.pheromone.application.actions.ApplicationActionManager;

public class TMWHelpAction extends ApplicationAction {
    public TMWHelpAction(ApplicationRenderer application) {
        super(application, ApplicationActionManager.HELP_ACTION);
    }

    public void applicationActionPerformed(ActionEvent e) {
        //getApplication().getFileManager().getFileTypeManager().openDocumentByType(address, "html", "GotoWebSiteAction");
    }
}
