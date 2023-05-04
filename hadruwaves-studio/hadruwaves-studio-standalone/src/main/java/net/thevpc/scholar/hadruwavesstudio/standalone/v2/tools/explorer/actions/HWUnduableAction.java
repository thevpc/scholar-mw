package net.thevpc.scholar.hadruwavesstudio.standalone.v2.tools.explorer.actions;

import net.thevpc.echo.Application;
import net.thevpc.echo.api.Action;
import net.thevpc.echo.api.ActionEvent;
import net.thevpc.echo.api.ActionWithId;
import net.thevpc.echo.api.UndoableAction;
import net.thevpc.echo.swing.helpers.actions.SwingAppUndoableAction;

public abstract class HWUnduableAction extends SwingAppUndoableAction implements ActionWithId {
    public HWUnduableAction(Application application, String id) {
        super(application, id);
    }

    public HWUnduableAction(Application application, String id, UndoableAction undoableAction) {
        super(application, id, undoableAction);
    }

    @Override
    public void run(ActionEvent event) {
        super.actionPerformed(new java.awt.event.ActionEvent(
                event.source(),
                1,""
        ));
    }
}
