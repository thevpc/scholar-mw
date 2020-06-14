/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.vpc.scholar.hadruwavesstudio.standalone.v2.actions.global;

import java.awt.event.ActionEvent;
import net.vpc.common.props.PMapEntry;
import net.vpc.common.props.PropertyEvent;
import net.vpc.common.props.PropertyListener;
import net.vpc.common.props.WritablePNamedNode;
import net.vpc.common.app.AbstractAppAction;
import net.vpc.common.app.Application;
import net.vpc.scholar.hadruwaves.project.HWSolution;
import net.vpc.scholar.hadruwaves.project.HWSolutionElement;
import net.vpc.scholar.hadruwavesstudio.standalone.v2.HadruwavesStudio;
import net.vpc.scholar.hadruwaves.project.HWProject;
import net.vpc.common.app.AppPropertiesTree;

/**
 *
 * @author vpc
 */
public class SaveAllAction extends AbstractAppAction {

    private final HadruwavesStudio outer;

    public SaveAllAction(Application aplctn, final HadruwavesStudio outer) {
        super(aplctn, "SaveAll");
        this.outer = outer;
        outer.proc().solution().listeners().add(new PropertyListener() {
            @Override
            public void propertyUpdated(PropertyEvent event) {
                if (event.getNewValue() instanceof AppPropertiesTree) {
                    AppPropertiesTree a = event.getNewValue();
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
