/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.common.props.FileObject;
import net.thevpc.echo.AbstractAppAction;
import net.thevpc.echo.AppPropertiesTree;
import net.thevpc.echo.Application;

/**
 *
 * @author vpc
 */
public class SaveAction extends AbstractAppAction {

    private final HadruwavesStudio outer;

    public SaveAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "Save");
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
        if (a!=null && a.root().object() instanceof FileObject) {
            outer.saveFileObject((FileObject) a.root().object());
        }
    }
    @Override
    public void refresh() {
        AppPropertiesTree a = outer.app().activeProperties().get();
        setAccessible((a!=null && a.root().object() instanceof FileObject));
    }

}
