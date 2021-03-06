/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.thevpc.common.props.PropertyEvent;
import net.thevpc.common.props.PropertyListener;
import net.thevpc.echo.Application;
import net.thevpc.scholar.hadruwaves.project.HWSolution;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.thevpc.scholar.hadruwaves.project.HWProject;
import net.thevpc.echo.api.AppPropertiesTree;
import net.thevpc.scholar.hadruwavesstudio.standalone.v2.actions.HAction;

/**
 *
 * @author vpc
 */
public class SaveAllAction extends HAction{

    private final HadruwavesStudio outer;

    public SaveAllAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "SaveAll");
        this.outer = outer;
        outer.proc().solution().onChange(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.newValue() instanceof AppPropertiesTree) {
                    AppPropertiesTree a = event.newValue();
                    if (a.root().object() instanceof HWSolution) {
                        setAccessible(true);
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
        HWSolution s = outer.proc().solution().get();
        if (s != null) {
            for (HWProject p : s.findProjects()) {
                outer.saveFileObject(p);
            }
            outer.saveFileObject(s);
        }
    }
    @Override
    public void refresh() {

    }

}
