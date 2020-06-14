package net.vpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import java.io.File;
import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;
import net.vpc.common.props.FileObject;
import net.vpc.scholar.hadruwaves.project.DefaultHWProject;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.common.app.AppPropertiesTree;

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
