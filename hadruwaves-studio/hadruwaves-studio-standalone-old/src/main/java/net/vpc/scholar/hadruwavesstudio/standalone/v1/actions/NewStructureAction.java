package net.thevpc.scholar.hadruwavesstudio.standalone.v1.actions;

import java.awt.event.ActionEvent;

import net.vpc.lib.pheromone.application.ApplicationRenderer;
import net.thevpc.scholar.hadruwavesstudio.standalone.v1.editors.MomProjectEditor;

/**
 * Created by IntelliJ IDEA. User: taha Date: 7 juil. 2003 Time: 14:00:39 To
 * change this template use Options | File Templates.
 */
public class NewStructureAction extends TmwlabAction {

    public NewStructureAction(ApplicationRenderer application) {
        super(application, "NewStructureAction", application.getResources());
    }

    public void applicationActionPerformed(ActionEvent e) {
        getApplicationRenderer().getWindowManager().addWindow("Structure", "Nouvelle Structure", null, new MomProjectEditor(getApplicationRenderer()));
    }
}
