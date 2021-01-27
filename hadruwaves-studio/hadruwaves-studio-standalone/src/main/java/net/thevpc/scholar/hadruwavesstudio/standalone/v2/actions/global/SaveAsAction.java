package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import java.io.File;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.echo.AbstractAppAction;
import net.thevpc.echo.Application;
import net.thevpc.common.props.FileObject;
import net.thevpc.scholar.hadruwaves.project.DefaultHWProject;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.echo.AppPropertiesTree;

/**
 *
 * @author vpc
 */
public class SaveAsAction extends AbstractAppAction {

    private final HadruwavesStudio outer;

    public SaveAsAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "SaveAs");
        this.outer = outer;
        outer.app().activeProperties().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                AppPropertiesTree a = event.getNewValue();
                if (a != null) {
                    if (a.root().object() instanceof FileObject) {
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }
                } else {
                    setEnabled(false);
                }
            }
        });
    }

    @Override
    public void actionPerformedImpl(ActionEvent e) {
        AppPropertiesTree a = outer.app().activeProperties().get();
        if (a != null && a.root().object() instanceof FileObject) {
            outer.saveFileObjectAs2((FileObject) a.root().object());
        }
    }

    @Override
    public void refresh() {
        AppPropertiesTree a = outer.app().activeProperties().get();
        setAccessible(a != null);
    }

}
